# basic-android-app

[![Android CI](https://github.com/TakuyaFukumura/basic-android-app/workflows/Android%20CI/badge.svg)](https://github.com/TakuyaFukumura/basic-android-app/actions/workflows/ci.yml)
[![Kotlin](https://img.shields.io/badge/Kotlin-2.2.10-blue.svg?style=flat&logo=kotlin)](https://kotlinlang.org)
[![Android API](https://img.shields.io/badge/API-24%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=24)
[![Target SDK](https://img.shields.io/badge/Target%20SDK-36-orange.svg?style=flat)](https://developer.android.com/studio/releases/platforms)
[![Version](https://img.shields.io/badge/Version-0.7.0-blue.svg?style=flat)](https://github.com/TakuyaFukumura/basic-android-app/releases)

KotlinとJetpack Composeで構築されたモダンなAndroidアプリケーション。文字列データベース管理機能を持つCRUDアプリケーションのサンプルです。

## 概要

このアプリは以下の機能を提供します：
- **メインスクリーン**: データベースから取得した文字列を使用した動的な挨拶メッセージの表示
- **文字列CRUD操作**: 文字列の作成、読み取り、更新、削除機能
- **リアルタイムUI更新**: 操作結果の即座な反映と状態管理
- **Roomデータベース統合**: SQLiteデータベースを使用した永続的なデータ保存
- **Hilt依存関係注入**: 依存関係の自動管理と型安全な注入
- **Jetpack Composeを使用したモダンなUI**: 宣言的UIによる効率的な開発
- **Material 3デザインシステムの採用**: 最新のデザインガイドラインに準拠
- **ダークテーマとライトテーマの対応**: システム設定に応じた自動切り替え
- **動的カラー（Android 12+）の対応**: デバイスの壁紙に基づいた色調整

### アプリの構成
- **MainActivity**: アプリのエントリーポイント、Composeを設定（Hilt対応）
- **MainViewModel**: データベースCRUD操作を管理するViewModel（Hiltによる依存関係注入）
- **MainScreen**: 文字列管理UI（追加、編集、削除、一覧表示）を提供するCompose画面
- **Room Database**: 文字列データを格納するSQLiteデータベース
- **Repository Pattern**: データアクセスの抽象化レイヤー
- **Hilt DI**: 依存関係注入によるコンポーネント間の疎結合化
- **テーマシステム**: Color、Theme、Typeファイルによる一貫したデザイン

## 開発環境要件

- **Java**: 17
- **Android SDK**: API 36対応
- **Kotlin**: 2.2.10
- **Android Gradle Plugin**: 8.12.2
- **ターゲットSDK**: 36
- **最小SDK**: 24（Android 7.0以上）

## 技術スタック

- **UI フレームワーク**: Jetpack Compose (BOM 2025.08.01)
- **デザインシステム**: Material 3
- **アーキテクチャ**: MVVM + Repository Pattern
- **依存関係注入**: Hilt 2.57.1
- **データベース**: Room (SQLite) 2.7.2
- **非同期処理**: Kotlin Coroutines + StateFlow
- **ビルドシステム**: Gradle 8.14.3 with Kotlin DSL
- **テスト**: JUnit 4 + Espresso

## セットアップと実行

### 前提条件

⚠️ **注意**: ビルドにはインターネット接続が必要です。Android Gradle Pluginの依存関係をGoogleのMavenリポジトリからダウンロードする必要があります。

### 1. プロジェクトのクローン
```bash
git clone https://github.com/TakuyaFukumura/basic-android-app.git
cd basic-android-app
```

### 2. ビルドと実行
```bash
# 実行権限の付与
chmod +x gradlew

# 依存関係の確認
./gradlew dependencies

# デバッグビルド（初回は時間がかかります）
./gradlew assembleDebug

# デバイスまたはエミュレーターにインストール
./gradlew installDebug
```

### 3. 開発用のタスク
```bash
# Lintチェック
./gradlew lintDebug

# ユニットテスト実行
./gradlew testDebugUnitTest

# 全体的なコード品質チェック
./gradlew check
```

### トラブルシューティング

- 初回ビルドでネットワークエラーが発生する場合は、インターネット接続を確認してください
- Gradleデーモンの問題が発生した場合は `./gradlew --stop` で停止後に再実行してください

## CI/CD設定

GitHub Actionsを使用した自動化されたビルドパイプラインが設定されています。

### 自動実行される処理

- **Lintチェック**: コード品質の検証
- **ユニットテスト**: 自動化されたテスト実行
- **デバッグビルド**: APKファイルの生成
- **Gradleセットアップとキャッシュ**: gradle/actions/setup-gradle@v4による最適化されたビルド環境

### ワークフロートリガー

`.github/workflows/ci.yml` で定義され、以下のタイミングで実行されます：
- 任意のブランチへのプッシュ時
- 並行実行制御により重複実行を防止

## プロジェクト構造

```
app/src/main/java/com/example/myapplication/
├── MainActivity.kt              # メインアクティビティ（Hilt対応）
├── MyApplication.kt             # アプリケーションクラス（Hilt対応）
├── data/                        # データレイヤー
│   ├── entity/
│   │   └── StringEntity.kt      # データベースエンティティ
│   ├── dao/
│   │   └── StringDao.kt         # データアクセスオブジェクト
│   ├── database/
│   │   └── AppDatabase.kt       # Roomデータベース設定
│   └── repository/
│       └── StringRepository.kt  # リポジトリパターン実装
├── di/                          # 依存関係注入レイヤー
│   └── DatabaseModule.kt        # Hiltデータベースモジュール
├── ui/
│   ├── viewmodel/
│   │   └── MainViewModel.kt     # ViewModelクラス（Hilt対応）
│   └── theme/                   # UIテーマ設定
│       ├── Color.kt             # カラーパレット
│       ├── Theme.kt             # Material 3テーマ
│       └── Type.kt              # タイポグラフィ設定
```

## バージョン履歴

- **v0.7.0**: Hilt依存関係注入の導入、CRUD機能の完全実装、Kotlin 2.2.10への更新
- **v0.6.0**: Android Gradle Plugin 8.12.2への更新、依存関係最新化
- **v0.5.0**: CI/CDワークフローの改善（gradle/actions/setup-gradle@v4導入）
- **v0.3.0**: Roomデータベース統合、MVVM+Repository パターンの実装
- **v0.2.0**: Java 17への移行、最新依存関係への更新
- **v0.1.0**: 初期バージョン（Java 11ベース）
