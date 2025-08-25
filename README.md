# basic-android-app
Androidアプリ開発のベースとなるリポジトリ

## 要件
- Java 17
- Android SDK
- Kotlin 2.0.21

## CI/CD設定

このプロジェクトには GitHub Actions を使用したCI設定が含まれています。

### 自動実行されるタスク

- **ビルド**: デバッグ版APKの作成
- **テスト**: 単体テストの実行
- **Lint**: コード品質チェック
- **成果物**: APKファイルとテスト結果のアーティファクト保存

### ワークフロー

`.github/workflows/ci.yml` ファイルに設定されており、以下のタイミングで自動実行されます：

- `main` または `develop` ブランチへのプッシュ
- `main` または `develop` ブランチに対するプルリクエストの作成・更新

## 開発環境

- **Java**: 17
- **Android Gradle Plugin**: 8.1.0
- **Kotlin**: 2.0.21
- **ターゲットSDK**: 36
- **最小SDK**: 24

## バージョン履歴
- v0.2.0: Java 17に移行
- v0.1.0: 初期バージョン（Java 11）
