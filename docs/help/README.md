# Anywhere- 用户帮助

Anywhere- 是一个 Android 快捷卡片应用。每张卡片保存一个动作，例如打开 URL Scheme、启动指定 Activity、执行 Shell、展示图片、运行一组 Workflow，或通过无障碍服务模拟点击。

这份帮助以当前 fork 的实际代码为准，适用于 `com.absinthe.anywhere_` 的 Release 版本和对应 debug 版本。应用内“帮助”入口也会打开这里。

## 从哪里开始

1. 完成首次设置并选择工作模式：[快速上手](getting-started.md)
2. 了解不同工作模式和权限要求：[权限与工作模式](permissions.md)
3. 创建或编辑卡片：[卡片类型](cards.md)
4. 添加动态 Shortcuts 或桌面快捷方式：[快捷方式](shortcuts.md)
5. 备份、恢复或同步到 WebDAV：[备份与恢复](backup.md)
6. 遇到问题：[常见问题](faq.md)

## 最短路径

如果你只需要“打开某个 App 的某个页面”，按这个顺序操作：

1. 首次启动时选择 **URL Scheme** 模式并完成设置。
2. 点击主界面右下角按钮，选择“添加卡片”。
3. 选择 **URL Scheme**，填写名称和 URL Scheme。
4. 保存后点击卡片测试。
5. 需要从桌面直接打开时，在卡片编辑器中打开菜单，选择“添加桌面快捷方式”。

普通 URL Scheme 和 Activity 卡片通常不需要 Root。Shell、冻结应用解冻、Collector 和部分自动化功能才需要额外权限。

## 版本与安装

- 正式版本使用包名 `com.absinthe.anywhere_`，从本 fork 的 GitHub Releases 安装。
- debug 版本包名带 `.debug` 后缀，不能覆盖正式版本，也不适合日常升级。
- 第一次从其他签名的 Anywhere- 切换过来时，通常需要备份后卸载旧版本。详见 [Obtainium 安装说明](../obtainium.md)。
- Release APK 支持 `arm64-v8a` 和 `armeabi-v7a`，不支持 x86/x86_64 模拟器原生安装。

## 数据和安全

- 卡片保存在应用本地数据库中；本地备份文件使用 `.awbackups` 后缀并加密。
- 备份默认不包含 **图片卡片** 和 **文件卡片**，也不保留卡片的自定义图标 URI。
- 分享单张卡片会生成 `anywhere://share/<encrypted>` 链接或二维码；拿到链接的人可以在 Anywhere- 中打开并导入卡片。
- Shell、无障碍和 Root/Shizuku 功能会执行有实际影响的操作。只运行你理解并信任的卡片，不要导入来源不明的 Shell 或无障碍配置。

## 文档导航

| 文档 | 内容 |
| --- | --- |
| [快速上手](getting-started.md) | 首次设置、主界面、创建第一张卡片 |
| [权限与工作模式](permissions.md) | Normal/URL Scheme、Root、Shizuku、悬浮窗、MIUI 后台弹出、无障碍 |
| [卡片类型](cards.md) | 已定义的 11 种卡片及编辑字段、执行条件和限制 |
| [快捷方式](shortcuts.md) | 动态 Shortcuts、桌面快捷方式、旧版创建方式、清除操作 |
| [备份与恢复](backup.md) | 本地备份、恢复、分享配置、WebDAV 云备份 |
| [常见问题](faq.md) | 无法启动、快捷方式没出现、Shell 无反应、恢复失败等 |

维护者文档：[调试说明](../debugging.md)、[发布说明](../releasing.md)、[Obtainium](../obtainium.md)。
