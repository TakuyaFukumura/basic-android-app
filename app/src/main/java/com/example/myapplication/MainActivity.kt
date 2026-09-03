package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.myapplication.data.entity.StringEntity
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
 * メイン画面を構成するComposable関数（Hilt対応版 + CRUD機能）
 *
 * アプリのメインコンテンツを表示します。
 * CRUD機能による文字列の管理機能を含みます。
 *
 * 技術的な詳細:
 * - hiltViewModel()でMainViewModelを自動取得
 * - StateFlowを使用してデータの変更を監視
 * - Hiltが自動的に依存関係を解決
 *
 * 機能:
 * - 現在の文字列の表示（Hello 文字列!形式）
 * - 文字列の追加機能
 * - 文字列の一覧表示
 * - 文字列の更新・削除機能
 * - 操作結果の表示
 *
 * @param modifier UIコンポーネントのレイアウト調整用修飾子。
 *                デフォルト値はModifier（何も変更しない）です。
 */
@Composable
fun MainScreen(modifier: Modifier = Modifier) {
    // Hiltを使用してViewModelを取得（依存関係は自動注入される）
    val viewModel: MainViewModel = hiltViewModel()
    
    // StateFlowから各種状態を取得
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val stringList = uiState.strings
    val operationStatus = uiState.operationMessage ?: uiState.errorMessage.orEmpty()
    
    // 入力フィールドの状態管理
    var inputText by remember { mutableStateOf("") }
    var editingId by remember { mutableStateOf<Int?>(null) }
    var editText by remember { mutableStateOf("") }
    var showDeleteAllConfirmation by remember { mutableStateOf(false) }
    var deleteTarget by remember { mutableStateOf<StringEntity?>(null) }
    
    // 操作ステータスの自動クリア（5秒後）
    LaunchedEffect(operationStatus.takeIf { it.isNotEmpty() }) {
        kotlinx.coroutines.delay(5000)
        viewModel.clearOperationStatus()
    }
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // メインの挨拶表示
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.greeting_format, uiState.greeting),
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }
        
        // 操作ステータス表示
        if (operationStatus.isNotEmpty()) {
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = operationStatus,
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (uiState.errorMessage != null) {
                        MaterialTheme.colorScheme.error
                    } else {
                        MaterialTheme.colorScheme.onPrimaryContainer
                    }
                )
            }
        }
        
        // 新規追加セクション
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = stringResource(R.string.add_string_title),
                    style = MaterialTheme.typography.titleMedium
                )
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = inputText,
                        onValueChange = { inputText = it.take(100) },
                        label = { Text(stringResource(R.string.string_input_label)) },
                        supportingText = {
                            if (inputText.isNotEmpty() && inputText.isBlank()) {
                                Text(stringResource(R.string.string_input_error))
                            }
                        },
                        isError = inputText.isNotEmpty() && inputText.isBlank(),
                        singleLine = true,
                        maxLines = 1,
                        modifier = Modifier.weight(1f),
                        keyboardOptions = androidx.compose.foundation.text.KeyboardOptions(
                            imeAction = androidx.compose.ui.text.input.ImeAction.Done
                        ),
                        keyboardActions = androidx.compose.foundation.text.KeyboardActions(
                            onDone = {
                                if (inputText.isNotBlank() && !uiState.isOperationInProgress) {
                                    viewModel.addString(inputText)
                                    inputText = ""
                                }
                            }
                        )
                    )
                    
                    Button(
                        onClick = {
                            if (inputText.isNotBlank()) {
                                viewModel.addString(inputText)
                                inputText = ""
                            }
                        },
                        enabled = inputText.isNotBlank() && !uiState.isOperationInProgress
                    ) {
                        Text(stringResource(R.string.add))
                    }
                }
            }
        }
        
        // 文字列一覧セクション
        Card(
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.string_list_title, stringList.size),
                        style = MaterialTheme.typography.titleMedium
                    )
                    
                    if (stringList.isNotEmpty()) {
                        TextButton(
                            onClick = { showDeleteAllConfirmation = true }
                        ) {
                            Text(stringResource(R.string.delete_all))
                        }
                    }
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                if (stringList.isEmpty()) {
                    Text(
                        text = stringResource(R.string.empty_strings_message),
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(stringList, key = { it.id }) { stringEntity ->
                            StringListItem(
                                stringEntity = stringEntity,
                                editingId = editingId,
                                editText = editText,
                                onEditStart = { id, currentValue ->
                                    editingId = id
                                    editText = currentValue
                                },
                                onEditSave = { id ->
                                    viewModel.updateString(id, editText)
                                    editingId = null
                                    editText = ""
                                },
                                onEditCancel = {
                                    editingId = null
                                    editText = ""
                                },
                                onEditTextChange = { editText = it },
                                onDelete = { entity ->
                                    deleteTarget = entity
                                }
                            )
                        }
                    }
                }
            }
        }

        if (showDeleteAllConfirmation) {
            AlertDialog(
                onDismissRequest = { showDeleteAllConfirmation = false },
                title = { Text(stringResource(R.string.delete_all_confirmation_title)) },
                text = { Text(stringResource(R.string.delete_all_confirmation_message)) },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showDeleteAllConfirmation = false
                            viewModel.deleteAllStrings()
                        }
                    ) {
                        Text(stringResource(R.string.delete_all))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showDeleteAllConfirmation = false }) {
                        Text(stringResource(R.string.cancel))
                    }
                }
            )
        }

        deleteTarget?.let { target ->
            AlertDialog(
                onDismissRequest = { deleteTarget = null },
                title = { Text(stringResource(R.string.delete_confirmation_title)) },
                text = { Text(stringResource(R.string.delete_confirmation_message, target.value)) },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.deleteString(target.id)
                            deleteTarget = null
                        }
                    ) {
                        Text(stringResource(R.string.delete))
                    }
                },
                dismissButton = {
                    TextButton(onClick = { deleteTarget = null }) {
                        Text(stringResource(R.string.cancel))
                    }
                }
            )
        }
    }
}

/**
 * 文字列一覧の各アイテムを表示するComposable関数
 *
 * 個別の文字列エンティティの表示と編集・削除機能を提供します。
 *
 * @param stringEntity 表示する文字列エンティティ
 * @param editingId 現在編集中のエンティティのID（編集モードかどうかの判定に使用）
 * @param editText 編集中のテキスト内容
 * @param onEditStart 編集開始時のコールバック
 * @param onEditSave 編集保存時のコールバック
 * @param onEditCancel 編集キャンセル時のコールバック
 * @param onEditTextChange 編集テキスト変更時のコールバック
 * @param onDelete 削除時のコールバック
 */
@Composable
fun StringListItem(
    stringEntity: StringEntity,
    editingId: Int?,
    editText: String,
    onEditStart: (Int, String) -> Unit,
    onEditSave: (Int) -> Unit,
    onEditCancel: () -> Unit,
    onEditTextChange: (String) -> Unit,
    onDelete: (StringEntity) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        if (editingId == stringEntity.id) {
            // 編集モード
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = editText,
                    onValueChange = { onEditTextChange(it.take(100)) },
                    label = { Text(stringResource(R.string.edit_string_label)) },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Button(
                        onClick = { onEditSave(stringEntity.id) },
                        enabled = editText.isNotBlank()
                    ) {
                        Text(stringResource(R.string.save))
                    }
                    
                    OutlinedButton(
                        onClick = onEditCancel
                    ) {
                        Text(stringResource(R.string.cancel))
                    }
                }
            }
        } else {
            // 表示モード
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = stringEntity.value,
                        style = MaterialTheme.typography.bodyLarge
                    )
                    Text(
                        text = "ID: ${stringEntity.id}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                }
                
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    TextButton(
                        onClick = { onEditStart(stringEntity.id, stringEntity.value) }
                    ) {
                        Text(stringResource(R.string.edit))
                    }
                    
                    TextButton(
                        onClick = { onDelete(stringEntity) }
                    ) {
                        Text(stringResource(R.string.delete))
                    }
                }
            }
        }
    }
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
        text = stringResource(R.string.greeting_format, name),
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
        Greeting("world")
    }
}
