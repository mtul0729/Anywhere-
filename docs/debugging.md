# 调试 Anywhere-

普通开发验证只需要构建 debug APK。只有需要确认安装、界面交互或系统集成时，才安装到设备或模拟器。

## 1. 本地构建

推荐用仓库提供的可选 Nix 环境，它会同时提供 JDK 17、Android SDK 35、NDK、CMake 和 Ninja：

```bash
nix develop -c bash ./gradlew assembleDebug --console=plain
```

如果宿主机已经具备相同工具链，也可以直接运行：

```bash
bash ./gradlew assembleDebug --console=plain
```

`gradlew` 在仓库里没有可执行位，因此命令中要保留 `bash`。构建成功后会生成：

```text
app/build/outputs/apk/debug/Anywhere-<version>-<code>-debug.apk
```

debug 包的包名带 `.debug` 后缀，例如 `com.absinthe.anywhere_.debug`，使用 debug 签名。它和正式 Release 包不能互相覆盖安装。

## 2. 从 CI 获取 APK

每次推送 `master` / `alpha` 的 `Android CI` 只验证 `assembleDebug`，不提供下载文件。需要 APK 时，在 GitHub Actions 手动运行 **Full Build**：

1. 选择 **Full Build**。
2. 需要 unsigned Release 时保留 `build_release`；只要 debug 包则关闭。
3. 构建完成后下载 `apk-debug` artifact，保留期为 7 天。

## 3. 安装到设备

先确认设备已连接：

```bash
adb devices -l
```

安装或覆盖同包名的 debug 版本：

```bash
adb install -r app/build/outputs/apk/debug/Anywhere-<version>-<code>-debug.apk
```

如果设备上有另一个签名的同包名应用，`install -r` 会失败。此时先备份需要保留的数据，再卸载旧 debug 包；不要为了安装 debug 包卸载正式 Release 包。

启动主界面：

```bash
adb shell am start -n com.absinthe.anywhere_.debug/com.absinthe.anywhere_.ui.main.MainActivity
```

## 4. 基础检查

查看应用日志：

```bash
adb logcat --pid="$(adb shell pidof com.absinthe.anywhere_.debug)"
```

确认已安装版本：

```bash
adb shell dumpsys package com.absinthe.anywhere_.debug | rg 'versionName|versionCode'
```

`rg` 不可用时，把最后一段换成 `grep -E 'versionName|versionCode'`。

## 5. 验证桌面快捷方式

### 手动路径

1. 在应用卡片上选择“创建桌面快捷方式”，按启动器提示确认。
2. 回到桌面，确认图标出现。
3. 点击图标，确认目标卡片动作执行；点击后进入浏览器或目标应用是正常行为，不代表快捷方式失败。

### 检查 pinned shortcut

成功请求后，系统会把快捷方式记录为 `pinned_<entityId>`：

```bash
adb shell dumpsys shortcut | rg -C 4 'pinned_|shortcutsId'
```

应同时看到：

- `Pinned: pinned_<entityId>`
- `ShortcutInfo {id=pinned_<entityId>`
- Intent extras 中的 `shortcutsId=<entityId>`，不带 `pinned_` 前缀

Intent extra 必须保存真实实体 ID；`pinned_` 只用于避免 pinned shortcut 与动态快捷方式使用同一个 ShortcutInfo ID。

### 直接测试打开路径

先从上一步取得真实实体 ID，再模拟快捷方式启动：

```bash
adb shell am start \
  -n com.absinthe.anywhere_.debug/com.absinthe.anywhere_.ui.shortcuts.ShortcutsActivity \
  -a START_ENTITY \
  --es shortcutsId <entityId>
```

如果目标卡片是 URL Scheme 类型，点击后进入浏览器就是成功结果。也可以从日志确认调用链：

```bash
adb logcat -d | rg 'START_ENTITY|ShortcutsActivity|Opener'
```

可以用下面的命令确认当前前台 Activity：

```bash
adb shell dumpsys activity activities | rg 'mResumedActivity|topResumedActivity'
```

## 6. Waydroid 可选说明

Waydroid 容器可能被冻结，表现为 `adb devices` 能看到设备但命令长时间无响应。解冻容器：

```bash
gdbus call --system --dest id.waydro.Container \
  --object-path /ContainerManager \
  --method id.waydro.ContainerManager.Unfreeze
```

然后等待系统完成启动：

```bash
adb wait-for-device
adb shell getprop sys.boot_completed
```

最后一项输出 `1` 后再安装。Waydroid 使用 x86_64 系统，而本项目正式 APK 只包含 `arm64-v8a` / `armeabi-v7a`；debug 包能否运行取决于容器是否提供 ARM 转译。系统集成问题应尽量再用真实 ARM 设备复核。
