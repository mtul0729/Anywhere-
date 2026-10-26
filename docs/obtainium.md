# 用 Obtainium 跟踪和更新 Anywhere-

这个 fork 的 GitHub Actions 会在每次推送到 `master` 后自动构建、签名并发布 APK。Obtainium 可以直接从这些 GitHub Releases 跟踪更新，不需要应用商店，也不需要手动下载新版本。

## 先确认版本和架构

- 下载源：<https://github.com/mtul0729/Anywhere->
- 正式 Release 示例：<https://github.com/mtul0729/Anywhere-/releases/tag/v2.5.5-2440c9f.6>
- 包名：`com.absinthe.anywhere_`
- APK 架构：`arm64-v8a`（ARMv8 64 位）和 `armeabi-v7a`（32 位 ARM）
- 不支持：x86、x86_64

当前 Release 只有一个 APK 文件，文件名类似：

```text
Anywhere-2.5.5-2440c9f.6-2050506-release.apk
```

只要手机是近年的 ARM 安卓设备，通常都兼容。若手机只支持 64 位应用，`arm64-v8a` 也能正常安装。

## 安装 Obtainium

从 Obtainium 官方渠道安装：

1. 打开官网 <https://obtainium.imranr.dev/>，或 GitHub Releases <https://github.com/ImranR98/Obtainium/releases>。
2. 下载并安装 Obtainium 的 APK。
3. 如果系统提示，允许 Obtainium 安装未知来源应用。

也可以从 F-Droid 安装 `dev.imranr.obtainium.fdroid`。不要从来源不明的第三方站点下载 Obtainium。

## 添加 Anywhere-

1. 打开 Obtainium，点击底部的“添加应用”。
2. 在“来源 URL”中填入仓库主页：

   ```text
   https://github.com/mtul0729/Anywhere-
   ```

3. 确认应用来源被识别为 **GitHub**。不要填 Release 页面或 APK 下载链接，仓库主页才是长期有效的跟踪地址。
4. 展开“附加选项”，按下面配置：

   | 选项 | 设置 |
   | --- | --- |
   | 包含预发行版 | 关闭 |
   | 通过正则表达式筛选链接 / 通过正则表达式筛选 APK | `^Anywhere-.*-release\.apk$` |
   | 尽可能按设备支持的 CPU 架构筛选 APK 文件 | 打开（可选，但推荐） |
   | 仅追踪 | 关闭 |

5. 点击“添加”。添加成功后，Obtainium 会显示最新版本和 APK 信息。

APK 正则表达式的含义：

```regex
^Anywhere-.*-release\.apk$
```

- `^`、`$`：从文件名开头匹配到结尾。
- `Anywhere-`：只匹配本项目的 APK。
- `.*`：允许中间包含版本号、提交短哈希和构建号。
- `-release\.apk`：只选择正式签名的 Release APK，排除 debug、unsigned 等文件。

如果 Obtainium 的另一版本界面只提供“筛选发行标题的正则表达式”，不要把它当成 APK 过滤器使用；找到“通过正则表达式筛选链接/APK”的选项再填写。

## 第一次安装：签名迁移

这个 fork 使用自己的签名证书。它与上游 `zhaobozhen/Anywhere-` 的官方 APK、debug 版 APK，以及之前用其他证书签名的版本**不能直接覆盖安装**。

第一次从这些版本切换过来时：

1. 先在 Anywhere- 中完成备份（如果旧版本能正常打开）。
2. 卸载旧的 Anywhere-。
3. 回到 Obtainium，打开 Anywhere- 条目并选择最新版本。
4. 点击“安装”，按 Android 提示确认。
5. 打开 Anywhere-，确认能正常启动。

卸载会删除应用数据；备份和恢复能否成功取决于旧版本的数据格式。不要为了“试试看”反复卸载已经能正常更新的版本。

安装后，后续版本都使用同一个 fork 签名证书，因此可以直接覆盖更新，**不要再卸载**。

## 以后的正常更新

1. 上游或本 fork 推送新提交到 `master` 后，GitHub Actions 会自动构建并创建新的 GitHub Release。
2. 打开 Obtainium，下拉刷新；也可以在设置中启用后台更新检查和通知。Obtainium 对 GitHub Releases 有缓存，刚发布后可能需稍等一会儿，或在应用详情页手动刷新。
3. 看到 Anywhere- 有新版本后，点击“更新”或“安装/更新”。
4. Android 会覆盖安装新版本，应用数据通常保留。

Obtainium 也可以在后台自动检查并尝试静默安装，但是否成功取决于 Android 版本、系统权限和安装方式。首次使用建议手动确认一次。

## 确认下载的是正确 APK

在 Obtainium 的应用详情页中应看到：

- 版本号以 `2.5.5-` 开头，并带有提交短哈希和构建号；
- 只有一个以 `-release.apk` 结尾的下载文件；
- APK 包名是 `com.absinthe.anywhere_`；
- 来源仓库是 `https://github.com/mtul0729/Anywhere-`。

不要选择 debug 或 unsigned APK：

- `*-debug.apk` 使用不同的包名和调试签名，不能用于正式更新。
- `*-unsigned.apk` 未签名，无法直接安装，仅供 PR 检查。

## 常见问题

### 提示“应用未安装”

最常见原因是手机上已有同包名、但签名不同的 Anywhere-。请确认旧版本来源，备份后卸载旧版本，再安装本 fork 的 Release APK。不要通过给新 APK 重签名来绕过签名校验；那会再次导致以后无法正常更新。

### Obtainium 没有发现新版本

- 手动下拉刷新。
- 确认来源 URL 是仓库主页，而不是某个固定的 Release 页面。
- 确认“包含预发行版”设置符合预期；正常使用应关闭。
- 检查 APK 正则表达式是否为 `^Anywhere-.*-release\.apk$`。
- 打开 GitHub Releases 页面，确认新 Release 已成功创建。

### 无法下载 APK

确认设备网络可以访问 GitHub。Obtainium 的 GitHub 请求受 API 速率限制；如果跟踪了很多 GitHub 应用，可以在 Obtainium 设置中配置 GitHub 个人访问令牌。

### 更新后数据丢失了

正常覆盖更新不会主动清除数据。数据丢失通常发生在卸载、签名不兼容后重装，或从不同包名的 debug 版本切换时。以后确认签名一致后，始终通过 Obtainium 覆盖更新。

## 可选：用 Obtainium 深链添加

如果 Obtainium 已安装，可以尝试打开下面的深链，让它预填同一个 GitHub 仓库：

```text
obtainium://add/https%3A%2F%2Fgithub.com%2Fmtul0729%2FAnywhere-
```

深链只负责打开“添加应用”页面；APK 过滤规则仍需按上表核对。
