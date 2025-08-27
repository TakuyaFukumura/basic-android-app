package com.example.myapplication.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.myapplication.data.entity.StringEntity
import kotlinx.coroutines.flow.Flow

/**
 * 文字列データにアクセスするためのDAO
 */
@Dao
interface StringDao {
    
    /**
     * すべての文字列を取得する
     */
    @Query("SELECT * FROM strings")
    fun getAllStrings(): Flow<List<StringEntity>>
    
    /**
     * 最初の文字列を取得する（Android文字列用）
     */
    @Query("SELECT * FROM strings LIMIT 1")
    suspend fun getFirstString(): StringEntity?
    
    /**
     * 文字列を挿入する
     */
    @Insert
    suspend fun insertString(string: StringEntity)
    
    /**
     * すべての文字列を削除する
     */
    @Query("DELETE FROM strings")
    suspend fun deleteAllStrings()
}
