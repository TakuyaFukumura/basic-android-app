# Room データベース実装について

## 実装概要

この実装では、Android Room データベースライブラリを使用して、アプリ内でのデータ永続化機能を追加しました。

## アーキテクチャ

### データ層の構造

```
data/
├── entity/
│   └── GreetingEntity.kt    # データベーステーブルのエンティティ
├── dao/
│   └── GreetingDao.kt       # データアクセスオブジェクト
├── database/
│   └── AppDatabase.kt       # Room データベースクラス
└── repository/
    └── GreetingRepository.kt # データ管理リポジトリ
```

## 主要コンポーネント

### 1. GreetingEntity
- `greetings` テーブルに対応するエンティティクラス
- `id` (主キー) と `name` フィールドを持つ

### 2. GreetingDao
- データベースアクセス用のインターフェース
- `getGreeting()`: 挨拶データの取得
- `insertGreeting()`: 挨拶データの挿入/更新

### 3. AppDatabase
- Room データベースのメインクラス
- シングルトンパターンで実装
- アプリ全体でデータベースインスタンスを共有

### 4. GreetingRepository
- データアクセスの抽象化層
- 初回アクセス時にデフォルトデータ「Android」を自動挿入
- UI層とデータ層の分離を実現

## 動作フロー

1. アプリ起動時にMainActivityがGreetingRepositoryを初期化
2. GreetingRepositoryがデータベースから挨拶データを取得
3. データが存在しない場合、「Android」をデフォルトとして挿入
4. 取得した挨拶名をUI（Compose）で表示

## 技術的特徴

- **Kotlin Coroutines**: 非同期データベースアクセス
- **Jetpack Compose**: 現代的なUI実装
- **MVVM パターン**: データとUI の分離
- **Unit Testing**: リポジトリクラスのテスト実装

## 今後の拡張可能性

- 複数の挨拶メッセージの管理
- ユーザーによる挨拶メッセージのカスタマイズ
- データベースマイグレーション対応
- LiveData や StateFlow を使用したリアクティブな更新
