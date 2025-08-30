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
 * Androidアプリの最初に起動されるコンポーネントです。
 *
 * Hiltの利点:
 * - 自動的な依存関係注入
 * - ViewModelの簡潔な取得
 * - エラーが起きにくいコード
 * - テスト時のモック注入の簡易化
 *
 * 主な機能:
 * - Edge-to-edge表示の有効化
 * - Composeテーマの適用
 * - メイン画面の表示
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    /**
     * アクティビティが作成されたときに呼び出されるメソッド
     *
     * Androidシステムがアクティビティのライフサイクル中で最初に呼び出すメソッドです。
     * ここでUIの初期化を行います。
     *
     * @param savedInstanceState 以前のアクティビティ状態の保存データ。
     *                          アプリが初回起動の場合はnullになります。
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Edge-to-edge表示を有効化（ステータスバーやナビゲーションバーの領域も使用）
        enableEdgeToEdge()

        // Jetpack ComposeのUIを設定
        setContent {
            // アプリのテーマを適用
            MyApplicationTheme {
                // Material3のScaffoldを使用してレイアウトの基本構造を作成
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    // メイン画面を表示（パディングを適用してシステムバーとの重複を回避）
                    MainScreen(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}

/**
 * メイン画面を構成するComposable関数（Hilt対応版）
 *
 * アプリのメインコンテンツを表示します。
 *
 * 技術的な詳細:
 * - hiltViewModel()でMainViewModelを自動取得
 * - StateFlowを使用してデータの変更を監視
 * - Hiltが自動的に依存関係を解決
 *
 * @param modifier UIコンポーネントのレイアウト調整用修飾子。
 *                デフォルト値はModifier（何も変更しない）です。
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

/**
 * 挨拶メッセージを表示するComposable関数
 *
 * 渡された名前を「Hello 名前!」の形式で表示します。
 * シンプルなテキスト表示コンポーネントです。
 *
 * @param name 表示する名前（通常は「Android」）
 * @param modifier UIコンポーネントのレイアウト調整用修飾子。
 *                デフォルト値はModifier（何も変更しない）です。
 */
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

/**
 * Greeting Composableのプレビュー用関数
 *
 * Android Studioのデザインタブでレイアウトを確認するために使用します。
 * 実際のアプリ実行時には呼び出されません。
 *
 * 表示内容: "Hello Android!" というテキスト
 */
@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }
}
