package com.example.myapplication.data.dao

import com.example.myapplication.data.entity.StringEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class StringDaoImpl : StringDao {
    private val stringsFlow = MutableStateFlow<List<StringEntity>>(emptyList())

    override fun getAllStrings(): Flow<List<StringEntity>> = stringsFlow.asStateFlow()

    override suspend fun insertString(string: StringEntity) {
        val current = stringsFlow.value.toMutableList()
        current.add(string)
        stringsFlow.value = current
    }

    override suspend fun getFirstString(): StringEntity? {
        return stringsFlow.value.firstOrNull()
    }

    override suspend fun deleteAllStrings() {
        stringsFlow.value = emptyList()
    }
}
