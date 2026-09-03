package com.example.myapplication.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.entity.StringEntity
import com.example.myapplication.data.repository.StringRepository
import com.example.myapplication.di.ErrorLogger
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class MainUiState(
    val greeting: String = "world",
    val strings: List<StringEntity> = emptyList(),
    val isLoading: Boolean = true,
    val isOperationInProgress: Boolean = false,
    val operationMessage: String? = null,
    val errorMessage: String? = null
)

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: StringRepository,
    private val errorLogger: ErrorLogger
) : ViewModel() {
    private companion object {
        const val TAG = "MainViewModel"
    }

    private val _uiState = MutableStateFlow(MainUiState())
    val uiState: StateFlow<MainUiState> = _uiState.asStateFlow()

    init {
        observeStrings()
    }

    private fun observeStrings() {
        viewModelScope.launch {
            try {
                repository.getAllStrings().collect { strings ->
                    _uiState.value = _uiState.value.copy(
                        strings = strings,
                        greeting = strings.firstOrNull()?.value ?: "world",
                        isLoading = false,
                        errorMessage = null
                    )
                }
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                errorLogger.error(TAG, "Failed to observe strings", exception)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    errorMessage = "データの読み込みに失敗しました"
                )
            }
        }
    }

    fun addString(value: String) {
        val normalizedValue = value.trim()
        if (normalizedValue.isEmpty()) {
            setOperationMessage("空の文字列は追加できません")
            return
        }
        runOperation {
            repository.insertString(StringEntity(value = normalizedValue))
            "文字列「$normalizedValue」を追加しました"
        }
    }

    fun updateString(id: Int, newValue: String) {
        val normalizedValue = newValue.trim()
        if (normalizedValue.isEmpty()) {
            setOperationMessage("空の文字列には更新できません")
            return
        }
        runOperation {
            repository.updateString(StringEntity(id = id, value = normalizedValue))
            "文字列を「$normalizedValue」に更新しました"
        }
    }

    fun deleteString(id: Int) {
        runOperation {
            if (repository.deleteStringById(id) > 0) {
                "文字列を削除しました"
            } else {
                "削除対象の文字列が見つかりませんでした"
            }
        }
    }

    fun deleteAllStrings() {
        runOperation {
            repository.deleteAllStrings()
            "すべての文字列を削除しました"
        }
    }

    fun clearOperationStatus() {
        _uiState.value = _uiState.value.copy(operationMessage = null)
    }

    private fun setOperationMessage(message: String) {
        _uiState.value = _uiState.value.copy(operationMessage = message, errorMessage = null)
    }

    private fun runOperation(operation: suspend () -> String) {
        if (_uiState.value.isOperationInProgress) return
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isOperationInProgress = true,
                operationMessage = null,
                errorMessage = null
            )
            try {
                val message = operation()
                _uiState.value = _uiState.value.copy(
                    isOperationInProgress = false,
                    operationMessage = message
                )
            } catch (exception: CancellationException) {
                throw exception
            } catch (exception: Exception) {
                errorLogger.error(TAG, "Database operation failed", exception)
                _uiState.value = _uiState.value.copy(
                    isOperationInProgress = false,
                    errorMessage = "データ操作に失敗しました"
                )
            }
        }
    }
}
