# basic-android-app
Androidアプリ開発のベースとなるリポジトリ

## セマンティックバージョニング

このプロジェクトは[セマンティックバージョニング](https://semver.org/lang/ja/)を採用しています。

### 現在のバージョン
- **バージョン**: 0.1.0
- **バージョンコード**: 100

### バージョン管理

バージョンは `version.properties` ファイルで管理されています：

```
VERSION_MAJOR=0  # 互換性のない API の変更
VERSION_MINOR=1  # 後方互換性を保ちながら機能を追加
VERSION_PATCH=0  # 後方互換性を保ったバグ修正
```

### バージョンの更新

バージョンの更新には、提供されているスクリプトを使用してください：

```bash
# 現在のバージョンを確認
./scripts/version.sh current

# パッチバージョンをアップ (例: 0.1.0 → 0.1.1)
./scripts/version.sh patch

# マイナーバージョンをアップ (例: 0.1.1 → 0.2.0)
./scripts/version.sh minor

# メジャーバージョンをアップ (例: 0.2.0 → 1.0.0)  
./scripts/version.sh major
```

### ビルド設定

アプリの `versionName` と `versionCode` は `version.properties` から自動的に生成されます：
- `versionName`: セマンティックバージョン (例: "0.1.0")
- `versionCode`: 数値形式 (計算式: major * 10000 + minor * 100 + patch)
