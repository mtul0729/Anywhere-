# 产品范围与维护边界

## 当前唯一明确的核心需求

当前唯一确认必须保持可用的用户路径是：

1. 创建一张 **URL Scheme** 卡片。
2. 将这张卡片添加为 **桌面快捷方式**。
3. 点击桌面快捷方式后，执行卡片中保存的 URL Scheme。

这条路径不需要 Root 或 Shizuku。它是后续功能取舍、问题排查和发布验证的第一优先级。

## 核心路径的验收标准

### 创建 URL Scheme 卡片

- 可以填写卡片名称和 URL Scheme，并保存到本地数据库。
- 保存后可以回到主页执行卡片。
- URL Scheme 支持普通 URL、`scheme://...` 和 `#Intent;...` 形式。
- 普通 URL Scheme 卡片不要求工作模式为 Root 或 Shizuku。

### 添加桌面快捷方式

- 可以从卡片编辑器菜单进入“添加桌面快捷方式”。
- 可以设置快捷方式名称和图标；未设置图标时使用卡片图标。
- Android 8.0 及以上通过系统 pinned shortcut API 发起请求。
- 启动器不支持固定快捷方式、请求尚未确认或创建成功时，应有明确反馈。
- 创建请求失败不能损坏卡片本身。

### 从桌面执行

- 点击桌面快捷方式后，打开对应卡片保存的 URL Scheme。
- 快捷方式使用独立 ID，不与动态 Shortcuts 冲突。
- 卡片仍可从主页执行；桌面快捷方式是额外入口，不是唯一入口。

## 其他功能的处理原则

其他卡片类型和功能默认保留，但不属于当前核心范围，包括但不限于：

- Activity、Shell、Switch Shell、File、Image、Broadcast、Workflow、Accessibility 等卡片类型。
- 动态 Shortcuts、Collector、扫码合集、Cloud Rules、Widget、Tile。
- Root、Shizuku、无障碍、解冻模式和 WebDAV 备份。

“非核心”不等于“需要删除”。除非用户明确要求删除，否则不要：

- 删除功能入口、菜单、页面或资源。
- 禁用权限、协议、数据库字段或兼容逻辑。
- 以重构、清理或统一 UI 为理由顺手移除旧功能。
- 为旧功能增加新的兼容机制或迁移。

如果旧功能存在缺陷，先记录问题和影响范围，等待用户决定是否投入修复。

## 变更判断顺序

1. 是否影响“URL Scheme 卡片 → 桌面快捷方式 → 执行”这条核心路径。
2. 是否会造成现有用户数据丢失、升级失败或无法回退。
3. 是否是用户明确要求的功能或删除操作。
4. 其他旧功能和外围模块：默认不动，只报告。

## 相关代码入口

- `app/src/main/java/com/absinthe/anywhere_/ui/editor/impl/SchemeEditorFragment.kt`
- `app/src/main/java/com/absinthe/anywhere_/ui/editor/EditorActivity.kt`
- `app/src/main/java/com/absinthe/anywhere_/ui/shortcuts/CreateShortcutDialogFragment.kt`
- `app/src/main/java/com/absinthe/anywhere_/utils/ShortcutsUtils.kt`
- `app/src/main/java/com/absinthe/anywhere_/receiver/PinShortcutReceiver.kt`
- `app/src/main/java/com/absinthe/anywhere_/ui/shortcuts/ShortcutsActivity.kt`

## 当前不做的事

- 不因为旧功能看起来过时就直接删除。
- 不为一次性引导内容增加数据库迁移。
- 不在没有明确需求时扩大重构范围。
- 不把“代码更干净”当成删除用户仍可能依赖的功能的理由。
