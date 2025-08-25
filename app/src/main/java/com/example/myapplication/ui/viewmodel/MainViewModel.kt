package com.example.myapplication.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.database.AppDatabase
import com.example.myapplication.data.repository.GreetingRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * メイン画面のViewModelクラス
 * UIの状態管理とデータベース操作を担当
 */
class MainViewModel(application: Application) : AndroidViewModel(application) {
    
    private val greetingRepository: GreetingRepository
    
    private val _greetingName = MutableStateFlow("Loading...")
    val greetingName: StateFlow<String> = _greetingName.asStateFlow()
    
    init {
        val database = AppDatabase.getDatabase(application)
        greetingRepository = GreetingRepository(database.greetingDao())
        loadGreeting()
    }
    
    /**
     * データベースから挨拶メッセージを読み込む
     */
    private fun loadGreeting() {
        viewModelScope.launch {
            try {
                val greeting = greetingRepository.getFirstGreeting()
                _greetingName.value = greeting?.name ?: "Android"
            } catch (e: Exception) {
                // エラーが発生した場合はデフォルト値を使用
                _greetingName.value = "Android"
            }
        }
    }
}
