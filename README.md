# Anywhere-

Anywhere- 是一个 Android 快捷卡片应用：把 URL Scheme、Activity、Shell、文件、图片、广播、Workflow 和无障碍操作保存成卡片，一键执行。

![Android CI](https://github.com/mtul0729/Anywhere-/actions/workflows/android.yml/badge.svg)

## 安装和更新

从本 fork 的 [GitHub Releases](https://github.com/mtul0729/Anywhere-/releases) 下载 `*-release.apk`。正式包只支持 ARM 设备（`arm64-v8a` / `armeabi-v7a`），最低 Android 8.0。

推荐用 [Obtainium](docs/obtainium.md) 跟踪更新。普通提交不会发布 APK；只有发布 tag 才会签名并创建 Release。第一次从其他签名版本切换时需要先备份，再卸载旧版本。

## 用户帮助

完整用户手册在 [docs/help](docs/help/README.md)，包括：

- [快速上手](docs/help/getting-started.md)
- [权限与工作模式](docs/help/permissions.md)
- [卡片类型](docs/help/cards.md)
- [快捷方式](docs/help/shortcuts.md)
- [备份与恢复](docs/help/backup.md)
- [常见问题](docs/help/faq.md)

应用内“帮助”入口也指向这份文档。

## 最短上手路径

1. 首次启动选择 **URL Scheme** 模式。
2. 点击右下角按钮，选择“添加卡片”。
3. 选择 **URL Scheme**，填写名称和 URL Scheme。
4. 保存后点击卡片测试。
5. 需要桌面图标时，在卡片编辑器菜单中创建桌面快捷方式。

Shell、Collector、冻结应用解冻和无障碍功能需要额外权限，详见 [权限与工作模式](docs/help/permissions.md)。

## 开发

- 构建验证：`bash ./gradlew assembleDebug`
- 可选 Nix 环境：`nix develop -c bash ./gradlew assembleDebug --console=plain`
- 本地构建、设备安装和 Waydroid 说明：[调试说明](docs/debugging.md)
- 发布 tag、审批和 Release 流程：[发布说明](docs/releasing.md)

推送到 `master` 只运行 debug 验证，不上传 artifact。需要安装包时手动运行 **Full Build**；发布 tag 才会触发签名和 GitHub Release。
