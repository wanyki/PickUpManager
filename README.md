# PickUpManager · 取件清单

一个简洁、轻量的 Android 取件码管理应用。它可以集中记录取件码、快递公司、存放地点、到达日期和备注，并按照地点自动整理包裹，减少在短信和通知中反复查找取件码的麻烦。

## 功能特点

- 添加、编辑和删除取件记录
- 自定义包裹到达日期
- 按存放地点自动分组
- 按“全部 / 待取 / 已取”筛选
- 一键确认取件或恢复待取状态
- 长按卡片进行编辑和删除
- 已取列表支持长按进入批量删除模式
- Room 本地持久化，关闭应用后数据仍然保留
- Material 3 界面，支持系统深色模式
- 紧凑卡片布局，一屏可以查看更多取件码

## 技术栈

- [Kotlin](https://kotlinlang.org/)
- [Jetpack Compose](https://developer.android.com/compose)
- [Material 3](https://m3.material.io/)
- [Room](https://developer.android.com/training/data-storage/room)
- ViewModel、Kotlin Flow 与 Coroutines
- Gradle Kotlin DSL

## 环境要求

- Android Studio
- JDK 11 或更高版本
- Android SDK 35
- Android 8.0（API 26）或更高版本

## 开始使用

克隆仓库：

```bash
git clone https://github.com/wanyki/PickUpManager.git
cd PickUpManager
```

使用 Android Studio 打开项目根目录，等待 Gradle 同步完成后，选择模拟器或 Android 设备并点击 **Run**。

### Firebase 配置

项目当前启用了 Google Services 插件，`app/google-services.json` 包含具体 Firebase 项目信息，因此不会提交到仓库。

首次在新电脑上构建时，需要在 Firebase 控制台创建对应的 Android 应用，下载 `google-services.json`，并放到：

```text
app/google-services.json
```

如果不需要 Firebase，也可以从 `app/build.gradle.kts` 中移除 Google Services 插件和 Firebase AI 依赖。

## 构建 APK

在项目根目录运行：

```bash
./gradlew assembleDebug
```

Windows PowerShell 或命令提示符：

```powershell
.\gradlew.bat assembleDebug
```

构建完成后的 Debug APK 位于：

```text
app/build/outputs/apk/debug/app-debug.apk
```

需要生成正式签名 APK 时，在 Android Studio 中选择：

```text
Build → Generate Signed App Bundle or APK → APK
```

签名文件和密码请妥善备份，不要提交到 GitHub。

## 项目结构

```text
app/src/main/java/com/example/pickupmanager/
├── data/               # Room 实体、DAO 与数据库
├── ui/                 # 页面、卡片、弹窗与 ViewModel
├── ui/theme/           # Material 3 主题、颜色与字体
└── MainActivity.kt     # 应用入口
```

## 数据与隐私

取件记录保存在设备本地的 Room 数据库中。签名文件、APK、Android SDK 本地配置及 Firebase 配置均已通过 `.gitignore` 排除。

## 后续计划

- 搜索取件码与快递公司
- 导入短信或通知中的取件信息
- 数据备份与恢复
- 自定义地点排序

欢迎提交 Issue 或 Pull Request。
