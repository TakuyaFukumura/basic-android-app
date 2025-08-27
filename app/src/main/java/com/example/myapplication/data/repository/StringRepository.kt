package com.example.myapplication.data.repository

import com.example.myapplication.data.dao.StringDao
import com.example.myapplication.data.entity.StringEntity
import kotlinx.coroutines.flow.Flow

/**
 * 文字列データにアクセスするリポジトリ
 */
class StringRepository(private val stringDao: StringDao) {
    
    /**
     * すべての文字列を取得する
     */
    fun getAllStrings(): Flow<List<StringEntity>> = stringDao.getAllStrings()
    
    /**
     * 最初の文字列を取得する（"Android"文字列）
     */
    suspend fun getFirstString(): StringEntity? = stringDao.getFirstString()
    
    /**
     * 文字列を挿入する
     */
    suspend fun insertString(string: StringEntity) = stringDao.insertString(string)
}
