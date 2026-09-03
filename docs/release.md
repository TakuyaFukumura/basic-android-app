# リリース手順

## ローカル確認

```powershell
.\gradlew.bat clean
.\gradlew.bat testDebugUnitTest
.\gradlew.bat lintDebug
.\gradlew.bat assembleRelease
```

ReleaseビルドはR8とリソース縮小を有効にしている。Room、Hilt、Composeを含む主要画面を、難読化後のAPKで確認する。

## 署名

キーストアとパスワードはリポジトリへ保存しない。派生アプリではGitHub Actions Secretsまたはローカル環境変数から次の値を読み込み、`signingConfigs`へ設定する。

- `ANDROID_KEYSTORE_BASE64`
- `ANDROID_KEY_ALIAS`
- `ANDROID_KEYSTORE_PASSWORD`
- `ANDROID_KEY_PASSWORD`

署名設定を追加した後も、`assembleRelease`で生成物を確認し、署名済みAPK/AABをActionsの成果物として保存する。

## バージョン

`app/build.gradle.kts`の`versionName`をSemVerで更新し、`versionCode`は必ず増加させる。READMEのバッジと履歴も同じ変更で更新する。

## 公開前チェック

- アプリ名、アイコン、権限、プライバシーポリシーを確認する
- バックアップ対象とデータ抽出ルールを確認する
- リリースノートとテスト結果を保存する
