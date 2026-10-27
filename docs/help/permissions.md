# 权限与工作模式

Anywhere- 的“工作模式”决定应用通过什么方式执行需要系统权限的动作。普通 URL Scheme 和 Activity 卡片在大多数情况下不需要 Root 或 Shizuku；Shell、Collector、冻结应用解冻等功能才依赖更高权限。

## 三种工作模式

### URL Scheme（Normal）

默认模式。应用直接通过 Android Intent 打开 URL Scheme 或 Activity，不执行 `am`、`pm` 等 Shell 命令。

- 适合普通 URL Scheme、Activity、图片、文件、广播和大多数 Workflow。
- 不需要 Root 或 Shizuku。
- Collector 不可用。
- 标记为“使用 root 执行”的卡片不会在这个模式下获得 root 权限。
- 页面标题显示为 **Somewhere-**，表示动作不要求全局系统权限。

### Root

应用通过 Root Shell 执行系统命令。

- 需要设备已正确 Root，并允许 Anywhere- 获取 Root。
- 首次设置时需要授予悬浮窗权限；MIUI 还需要“后台弹出界面”权限。
- 适合 Shell 卡片、Collector、解冻 Root 模式，以及需要 `am`/`pm` 的操作。
- 页面标题显示为 **Anywhere-**。
- Root 权限被拒绝时，Shell 或 Collector 会失败或提示权限错误。

### Shizuku

应用通过 Shizuku 提供的特权进程执行系统命令。

- 需要安装并启动 Shizuku，再向 Anywhere- 授予 Shizuku 权限。
- 首次设置时需要授予悬浮窗权限；MIUI 还需要“后台弹出界面”权限。
- 适合不想给应用完整 Root，但愿意通过 Shizuku 执行 `am`/`pm` 的用户。
- 页面标题显示为 **Anywhere-**。
- Shizuku 服务未运行或权限被撤销后，依赖它的功能会失败。

工作模式可以在 **设置 → 工作模式** 中切换，切换后建议重新检查对应权限。

## 权限清单

| 权限/能力 | 用途 | 典型场景 |
| --- | --- | --- |
| 悬浮窗 | 显示 Collector 或卡片悬浮按钮 | Root/Shizuku 模式的 Collector、卡片悬浮窗 |
| MIUI 后台弹出界面 | 允许应用在后台启动界面 | MIUI 上 Root/Shizuku 模式 |
| Root | 以超级用户执行命令 | Shell、Collector、解冻 |
| Shizuku | 通过特权服务执行命令 | Shell、Collector、解冻 |
| 无障碍服务 | 读取界面节点并执行点击、长按、手势 | 无障碍卡片、Workflow 中的无障碍步骤 |
| 通知 | 显示备份、Workflow、Collector 等前台服务通知 | Android 13 及以上首次使用相关功能时 |

## 首次设置的行为

设置向导会根据工作模式动态显示权限卡片：

- **URL Scheme**：不要求 Root、Shizuku 或悬浮窗。
- **Root**：显示 Root 和悬浮窗；MIUI 额外显示后台弹出界面。
- **Shizuku**：显示 Shizuku 和悬浮窗；MIUI 额外显示后台弹出界面。

权限没有全部授予时仍可点击完成，但会弹出确认提示。进入主页后，相关功能可能不可用。

Android 11 及以上申请悬浮窗权限时，需要在系统列表中选择 **Anywhere-** 并允许显示在其他应用上层。只打开总开关而不勾选 Anywhere- 不算授权成功。

## 无障碍服务

无障碍卡片和 Workflow 中的无障碍步骤需要开启 Anywhere- 的无障碍服务。首次执行时应用会显示无障碍声明；同意后仍需在系统设置中手动启用服务。

无障碍服务用于：

- 等待目标 Activity 出现。
- 按文本、View ID 或坐标查找控件。
- 执行点击、长按和手势。

无障碍自动化依赖目标 App 的界面结构。目标 App 更新、语言变化、分辨率变化或控件 ID 改变，都可能让卡片失效。

## 解冻模式

打开被冻结或停用的 App 前，Anywhere- 会按 **设置 → 解冻模式** 选择的方式尝试解冻。可用方式包括：

- **DSM/托管设备管理员**：需要另一台设备管理员授予委托范围。
- **IceBox SDK**：需要安装冰箱 App 并获得其 SDK 权限。
- **设备管理员（DPM）**：需要把 Anywhere- 设为设备管理员。
- **Root**：需要 Root 权限。
- **Shizuku**：需要 Shizuku 正常运行，通常要求 Shizuku 由 Root 方式启动。

没有选择或配置正确的解冻方式时，打开冻结 App 的卡片会提示“请选择一项解冻模式”。设备管理员模式影响范围较大，启用前应了解如何撤销设备管理员。

## 安全建议

- 不要给来源不明的 Shell 卡片授予 Root/Shizuku 权限。
- 不要导入要求开启无障碍但无法解释其操作的卡片。
- 只从本 fork 的 GitHub Releases 或自己构建的 APK 更新，避免签名不一致和恶意替换。
- 不需要 Root/Shizuku 时保持 URL Scheme 模式，减少攻击面。
