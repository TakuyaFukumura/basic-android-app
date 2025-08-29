# Hilt導入実装ガイド

## 概要

このドキュメントでは、AndroidアプリにHilt（Dependency Injection）を導入する方法を詳しく説明します。Hiltを使用することで、手動で行っていた依存関係の管理を自動化し、コードの保守性とテスタビリティを大幅に向上させることができます。

## 実装済み内容

### 1. 依存関係とプラグインの設定

#### gradle/libs.versions.toml
```toml
[versions]
hilt = "2.48"
hiltNavigationCompose = "1.1.0"

[libraries]
# Hilt dependencies
hilt-android = { module = "com.google.dagger:hilt-android", version.ref = "hilt" }
hilt-compiler = { module = "com.google.dagger:hilt-compiler", version.ref = "hilt" }
hilt-navigation-compose = { module = "androidx.hilt:hilt-navigation-compose", version.ref = "hiltNavigationCompose" }

[plugins]
hilt = { id = "com.google.dagger.hilt.android", version.ref = "hilt" }
```

#### app/build.gradle.kts
```kotlin
plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.ksp)
    alias(libs.plugins.hilt)
    kotlin("kapt")
}

dependencies {
    // Hilt dependencies
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.hilt.navigation.compose)
}
```

### 2. Applicationクラスの修正

#### MyApplication.kt (Hilt対応版)
```kotlin
package com.example.myapplication

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * アプリケーションクラス（Hilt対応版）
 * 
 * @HiltAndroidApp:
 * - Hiltによる依存関係注入を有効化
 * - アプリケーション全体でHiltコンポーネントを利用可能にする
 * - 他のAndroidコンポーネント（Activity、Fragment等）でのHilt使用を許可
 */
@HiltAndroidApp
class MyApplication : Application()
```

**変更点:**
- @HiltAndroidAppアノテーションの追加
- 手動でのデータベースとリポジトリ管理を削除
- Hiltが自動的に依存関係を管理

### 3. Hiltモジュールの作成

#### di/DatabaseModule.kt
```kotlin
package com.example.myapplication.di

import android.content.Context
import com.example.myapplication.data.dao.StringDao
import com.example.myapplication.data.database.AppDatabase
import com.example.myapplication.data.repository.StringRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Qualifier
import javax.inject.Singleton

/**
 * Hilt用データベースモジュール
 * 
 * @InstallIn(SingletonComponent::class):
 * - アプリケーション全体で単一のインスタンスを共有
 * - アプリの起動から終了まで同じインスタンスを使用
 */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @ApplicationScope
    @Provides
    @Singleton
    fun provideApplicationScope(): CoroutineScope = CoroutineScope(SupervisorJob())

    @Provides
    @Singleton
    fun provideAppDatabase(
        @ApplicationContext context: Context,
        @ApplicationScope applicationScope: CoroutineScope
    ): AppDatabase {
        return AppDatabase.getDatabase(context, applicationScope)
    }

    @Provides
    fun provideStringDao(database: AppDatabase): StringDao {
        return database.stringDao()
    }

    @Provides
    @Singleton
    fun provideStringRepository(stringDao: StringDao): StringRepository {
        return StringRepository(stringDao)
    }
}

/**
 * アプリケーションスコープのCoroutineScopeを識別するための修飾子
 */
@Qualifier
@Retention(AnnotationRetention.RUNTIME)
annotation class ApplicationScope
```

### 4. ViewModelの修正

#### ui/viewmodel/MainViewModel.kt (Hilt対応版)
```kotlin
package com.example.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.repository.StringRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * メイン画面のViewModel（Hilt対応版）
 * 
 * 主な変更点:
 * - @HiltViewModelアノテーションの追加
 * - @Inject constructorによる依存関係注入
 * - ViewModelFactoryの削除（Hiltが自動管理）
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: StringRepository
) : ViewModel() {

    private val _greeting = MutableStateFlow("Loading...")
    val greeting: StateFlow<String> = _greeting.asStateFlow()

    init {
        loadGreeting()
    }

    private fun loadGreeting() {
        viewModelScope.launch {
            try {
                val stringEntity = repository.getFirstString()
                _greeting.value = stringEntity?.value ?: "Android"
            } catch (_: Exception) {
                _greeting.value = "Android"
            }
        }
    }
}
```

**変更点:**
- @HiltViewModelアノテーションの追加
- @Inject constructorによる依存関係注入
- ViewModelFactoryクラスの削除

### 5. UIレイヤーの修正

#### MainActivity.kt (Hilt対応版)
```kotlin
package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.example.myapplication.ui.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint

/**
 * アプリケーションのメインアクティビティ（Hilt対応版）
 * 
 * 主な変更点:
 * - @AndroidEntryPointアノテーションの追加
 * - hiltViewModel()によるViewModel取得
 * - 手動でのApplicationインスタンス取得の削除
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

/**
 * メイン画面を構成するComposable関数（Hilt対応版）
 * 
 * 主な変更点:
 * - hiltViewModel()によるViewModel取得
 * - ApplicationインスタンスやFactoryの手動管理を削除
 */
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    // Hiltを使用してViewModelを取得（依存関係は自動注入される）
    val viewModel: MainViewModel = hiltViewModel()
    
    // StateFlowから文字列を取得
    val greeting by viewModel.greeting.collectAsState()
    
    Greeting(
        name = greeting,
        modifier = modifier
    )
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }
}
```

**変更点:**
- @AndroidEntryPointアノテーションの追加
- hiltViewModel()によるViewModel取得
- 手動のファクトリー使用を削除

## Hiltの利点

### 1. 自動的な依存関係注入
- 手動でのインスタンス管理が不要
- 型安全な依存関係解決
- コンパイル時の検証

### 2. コードの簡素化
- ViewModelFactoryの削除
- 手動でのApplicationキャストの削除
- ボイラープレートコードの大幅削減

### 3. テスタビリティの向上
- モックの注入が容易
- 単体テストでの依存関係の置き換えが簡単
- テスト用のHiltモジュールの作成が可能

### 4. 保守性の向上
- 依存関係の変更が一箇所で完結
- より明確なアーキテクチャ
- 依存関係の循環参照の検出

## ビルド互換性について

現在の実装では、最新のKotlin、Android Gradle Plugin、Hiltのバージョン間で互換性問題が発生する場合があります。これは開発中の一時的な問題で、以下の安定したバージョンの組み合わせを使用することで解決できます：

### 推奨バージョン組み合わせ
```toml
[versions]
agp = "8.1.4"
kotlin = "1.9.10"
hilt = "2.48"
ksp = "1.9.10-1.0.13"
composeBom = "2023.10.01"
```

## 今後の改善提案

1. **テストモジュールの追加**: Hiltテスト用のモジュール作成
2. **スコープの活用**: Fragment、Activity固有のスコープの利用
3. **Qualifierの活用**: 複数の同じ型の依存関係の管理
4. **アシスト注入**: ファクトリーパターンが必要な場合の対応

## 結論

Hiltの導入により、Androidアプリの依存関係管理が大幅に改善されます。手動での依存関係管理から解放され、より保守性が高く、テストしやすいコードを書くことができるようになります。