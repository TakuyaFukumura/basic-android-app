package com.example.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.repository.StringRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * メイン画面のViewModel
 * データベースから文字列を取得して画面に表示する
 */
class MainViewModel(private val repository: StringRepository) : ViewModel() {
    
    private val _greeting = MutableStateFlow("Loading...")
    val greeting: StateFlow<String> = _greeting.asStateFlow()
    
    init {
        loadGreeting()
    }
    
    /**
     * データベースから文字列を読み込む
     */
    private fun loadGreeting() {
        viewModelScope.launch {
            try {
                val stringEntity = repository.getFirstString()
                _greeting.value = stringEntity?.value ?: "Android"
            } catch (e: Exception) {
                // エラーが発生した場合はデフォルト値を使用
                _greeting.value = "Android"
            }
        }
    }
}

/**
 * ViewModelのファクトリークラス
 */
class MainViewModelFactory(private val repository: StringRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
