# 发布 Anywhere-

普通提交和发布是两件事：推送代码只验证能否构建，推送 tag 才签名并创建 GitHub Release。

## 日常验证

推送到 `master` 或 `alpha`、以及 PR，都会运行 `Android CI`：

- 只执行 `assembleDebug`
- 不上传 artifact
- 不读取签名密钥
- 不创建 Release

需要可安装的 debug APK 或 unsigned release APK 时，在 Actions 中手动运行 **Full Build**。artifact 保留 7 天。本地构建、安装和 Waydroid 验证见 [调试说明](debugging.md)。

## 版本规则

正式 tag：

```text
vMAJOR.MINOR.PATCH
```

alpha tag：

```text
vMAJOR.MINOR.PATCH-alpha.N
```

`N` 必须在 `1..98`。versionCode 固定由 tag 推导：

```text
base = major * 1000000 + minor * 10000 + patch * 100
alpha = base + N
final = base + 99
```

例如：

- `v2.5.5-alpha.16` → `2050516`
- `v2.5.5` → `2050599`
- 下一个补丁版本 `v2.5.6-alpha.1` → `2050601`

发布后不能修改或复用 tag。发布失败也消耗编号，修复后使用下一个 alpha 编号或新版本号。

## 发布步骤

1. 在目标提交上本地构建并实际验证。
2. 确认该提交已经在 `alpha`（alpha 发布）或 `master`（正式发布）的祖先链上。
3. 创建并推送 tag：

   ```bash
   git tag v2.5.5-alpha.16 <commit>
   git push origin v2.5.5-alpha.16
   ```

4. `Release` workflow 会先无密钥构建 unsigned APK。
5. 在 `release` environment 中批准发布任务。
6. workflow 下载同一份构建产物，签名、校验包名/版本/证书/SHA256，然后创建 Release。

alpha tag 必须能从 `alpha` 到达，正式 tag 必须能从 `master` 到达。versionCode 必须高于已有 Release。

## 失败与重跑

- 构建失败：tag 保留，修复后使用新版本号或新 alpha 编号。
- 审批前失败：可以重跑 workflow，仍使用同一个 tag 和 versionCode。
- 已存在 Release：重跑只做验证，不替换已有 APK。
- 已发布版本有问题：保留旧 Release，发布下一个 versionCode 的修复版。

不要删除、移动或强制更新已推送的发布 tag。

## 维护者设置

- GitHub environment `release` 必须配置 required reviewer。
- 签名 secrets 应放在 `release` environment，而不是仓库级 secrets。
- 为 `v*` tag 配置禁止更新和删除的 ruleset。

如果这些设置未完成，`Release` 会失败而不是降级为无审批发布。
