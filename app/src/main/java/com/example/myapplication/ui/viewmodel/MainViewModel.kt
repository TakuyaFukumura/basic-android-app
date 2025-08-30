package com.example.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.entity.StringEntity
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
 * MVVM（Model-View-ViewModel）アーキテクチャパターンのViewModel層を実装しています。
 *
 * 主な責務:
 * - データベースから文字列データを取得
 * - CRUD操作（作成、読み取り、更新、削除）の管理
 * - UI状態の管理（Loading、成功、エラー）
 * - Compose UIとのリアクティブな連携
 * - ライフサイクルを考慮した非同期処理
 *
 * StateFlowの利点:
 * - UIスレッドでの状態変更通知
 * - 最新の状態値の保持
 * - 設定変更（画面回転など）での状態維持
 * - Composeとの自動的な再描画連携
 *
 * @property repository データベースアクセスを提供するStringRepository（Hiltにより注入）
 */
@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: StringRepository
) : ViewModel() {

    /**
     * 内部で管理される挨拶メッセージの状態
     *
     * MutableStateFlowを使用してUIの状態を管理します。
     * privateにすることで外部からの直接変更を防ぎ、
     * ViewModelを通じてのみ状態変更が可能になります。
     *
     * 初期値: "Loading..." - データ取得中であることを示す
     */
    private val _greeting = MutableStateFlow("Loading...")

    /**
     * UI側からアクセス可能な挨拶メッセージの状態
     *
     * StateFlowとして公開することで:
     * - 読み取り専用アクセスを提供
     * - Composeでの自動再描画を可能にする
     * - 状態の変更を安全に監視
     *
     * Compose側では collectAsState() を使用してこの値を監視し、
     * 状態が変更されると自動的にUIが再描画されます。
     */
    val greeting: StateFlow<String> = _greeting.asStateFlow()

    /**
     * 内部で管理される文字列エンティティの一覧状態
     *
     * すべての文字列データの状態を管理し、CRUD操作の結果を反映します。
     * 一覧表示やデータ管理画面で使用されます。
     */
    private val _stringList = MutableStateFlow<List<StringEntity>>(emptyList())

    /**
     * UI側からアクセス可能な文字列エンティティの一覧状態
     *
     * CRUD操作や一覧表示画面で使用される読み取り専用の状態です。
     */
    val stringList: StateFlow<List<StringEntity>> = _stringList.asStateFlow()

    /**
     * 内部で管理される操作ステータス状態
     *
     * CRUD操作の実行状況やエラー状態を管理します。
     * ユーザーへのフィードバック表示に使用されます。
     */
    private val _operationStatus = MutableStateFlow("")

    /**
     * UI側からアクセス可能な操作ステータス状態
     *
     * CRUD操作の結果をユーザーに通知するための状態です。
     */
    val operationStatus: StateFlow<String> = _operationStatus.asStateFlow()

    /**
     * ViewModelの初期化ブロック
     *
     * ViewModelインスタンスが作成されたときに自動実行されます。
     * ここでデータの初期読み込みを開始し、UIに表示するデータを準備します。
     */
    init {
        loadGreeting()
        loadAllStrings()
    }

    /**
     * データベースから挨拶用の文字列を非同期で読み込む
     *
     * リポジトリを通じてデータベースから文字列を取得し、
     * UIの状態を更新します。エラーハンドリングも実装されており、
     * データ取得に失敗した場合はデフォルト値を使用します。
     *
     * 非同期処理の流れ:
     * 1. viewModelScopeでコルーチンを起動
     * 2. repository.getFirstString()でデータベースアクセス
     * 3. 取得結果をStateFlowに反映
     * 4. ComposeUIが自動的に再描画
     *
     * エラーハンドリング:
     * - データベースアクセスエラー: デフォルト値"world"を使用
     * - データが存在しない場合: デフォルト値"world"を使用
     * - ユーザーエクスペリエンスを損なわない設計
     *
     * ライフサイクル考慮:
     * - viewModelScopeを使用することで、ViewModelが破棄されると
     *   自動的にコルーチンもキャンセルされる
     * - メモリリークの防止
     * - 適切なリソース管理
     */
    private fun loadGreeting() {
        viewModelScope.launch {
            try {
                // リポジトリを通じてデータベースから最初の文字列を取得
                val stringEntity = repository.getFirstString()
                // 取得した文字列をUIに反映（nullの場合はデフォルト値を使用）
                _greeting.value = stringEntity?.value ?: "world"
            } catch (_: Exception) {
                // エラーが発生した場合はデフォルト値を使用
                // ログ出力やエラー報告などの追加処理も可能
                _greeting.value = "world"
            }
        }
    }

    /**
     * すべての文字列エンティティを非同期で読み込む
     *
     * リポジトリを通じてデータベースからすべての文字列データを取得し、
     * 一覧表示用の状態を更新します。CRUD操作後の一覧更新にも使用されます。
     */
    private fun loadAllStrings() {
        viewModelScope.launch {
            try {
                repository.getAllStrings().collect { strings ->
                    _stringList.value = strings
                }
            } catch (_: Exception) {
                _stringList.value = emptyList()
                _operationStatus.value = "データの読み込みに失敗しました"
            }
        }
    }

    /**
     * 新しい文字列を追加する（Create操作）
     *
     * ユーザーが入力した文字列を新しいStringEntityとしてデータベースに保存します。
     * 操作完了後、挨拶メッセージと一覧を更新して最新の状態を反映します。
     *
     * @param value 追加する文字列値
     */
    fun addString(value: String) {
        if (value.isBlank()) {
            _operationStatus.value = "空の文字列は追加できません"
            return
        }

        viewModelScope.launch {
            try {
                val newEntity = StringEntity(value = value.trim())
                repository.insertString(newEntity)
                _operationStatus.value = "文字列「$value」を追加しました"
                
                // 最新データで画面を更新
                loadGreeting()
            } catch (_: Exception) {
                _operationStatus.value = "文字列の追加に失敗しました"
            }
        }
    }

    /**
     * 既存の文字列を更新する（Update操作）
     *
     * 指定されたIDの文字列エンティティの値を新しい値で更新します。
     * 操作完了後、挨拶メッセージと一覧を更新して最新の状態を反映します。
     *
     * @param id 更新対象のエンティティID
     * @param newValue 新しい文字列値
     */
    fun updateString(id: Int, newValue: String) {
        if (newValue.isBlank()) {
            _operationStatus.value = "空の文字列には更新できません"
            return
        }

        viewModelScope.launch {
            try {
                val entity = StringEntity(id = id, value = newValue.trim())
                repository.updateString(entity)
                _operationStatus.value = "文字列を「$newValue」に更新しました"
                
                // 最新データで画面を更新
                loadGreeting()
            } catch (_: Exception) {
                _operationStatus.value = "文字列の更新に失敗しました"
            }
        }
    }

    /**
     * 文字列を削除する（Delete操作）
     *
     * 指定されたIDの文字列エンティティをデータベースから削除します。
     * 削除後、データが存在しない場合はデフォルト値"world"を表示します。
     *
     * @param id 削除対象のエンティティID
     */
    fun deleteString(id: Int) {
        viewModelScope.launch {
            try {
                val deletedCount = repository.deleteStringById(id)
                if (deletedCount > 0) {
                    _operationStatus.value = "文字列を削除しました"
                } else {
                    _operationStatus.value = "削除対象の文字列が見つかりませんでした"
                }
                
                // 最新データで画面を更新
                loadGreeting()
            } catch (_: Exception) {
                _operationStatus.value = "文字列の削除に失敗しました"
            }
        }
    }

    /**
     * すべての文字列を削除する（Delete All操作）
     *
     * データベース内のすべての文字列エンティティを削除します。
     * 削除後はデフォルト値"world"が表示されます。
     *
     * 注意: この操作は元に戻すことができません
     */
    fun deleteAllStrings() {
        viewModelScope.launch {
            try {
                repository.deleteAllStrings()
                _operationStatus.value = "すべての文字列を削除しました"
                
                // 最新データで画面を更新（デフォルト値が表示される）
                loadGreeting()
            } catch (_: Exception) {
                _operationStatus.value = "文字列の削除に失敗しました"
            }
        }
    }

    /**
     * 操作ステータスをクリアする
     *
     * 表示されている操作結果メッセージをクリアします。
     * UI側でメッセージを一定時間表示した後に呼び出されます。
     */
    fun clearOperationStatus() {
        _operationStatus.value = ""
    }
}
