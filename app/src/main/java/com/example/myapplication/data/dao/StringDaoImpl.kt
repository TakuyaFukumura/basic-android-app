package com.example.myapplication.data.dao

import com.example.myapplication.data.entity.StringEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class StringDaoImpl : StringDao {
    private val stringsFlow = MutableStateFlow<List<StringEntity>>(emptyList())

    override fun getAllStrings(): Flow<List<StringEntity>> = stringsFlow.asStateFlow()

    // Roomの@Insertや@Deleteが必要な場合は追加実装可能
    // 例: 文字列追加
    fun insertString(entity: StringEntity) {
        val current = stringsFlow.value.toMutableList()
        current.add(entity)
        stringsFlow.value = current
    }
}

