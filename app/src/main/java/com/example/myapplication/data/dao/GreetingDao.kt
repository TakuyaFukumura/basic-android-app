package com.example.myapplication.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.myapplication.data.entity.Greeting
import kotlinx.coroutines.flow.Flow

/**
 * 挨拶メッセージのデータアクセスオブジェクト
 */
@Dao
interface GreetingDao {
    
    /**
     * 全ての挨拶メッセージを取得
     */
    @Query("SELECT * FROM greetings")
    fun getAllGreetings(): Flow<List<Greeting>>
    
    /**
     * IDで挨拶メッセージを取得
     */
    @Query("SELECT * FROM greetings WHERE id = :id")
    suspend fun getGreetingById(id: Long): Greeting?
    
    /**
     * 最初の挨拶メッセージを取得
     */
    @Query("SELECT * FROM greetings LIMIT 1")
    suspend fun getFirstGreeting(): Greeting?
    
    /**
     * 挨拶メッセージを挿入
     */
    @Insert
    suspend fun insertGreeting(greeting: Greeting): Long
}
