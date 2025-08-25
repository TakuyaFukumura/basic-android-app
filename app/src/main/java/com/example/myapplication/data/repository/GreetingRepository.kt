package com.example.myapplication.data.repository

import com.example.myapplication.data.dao.GreetingDao
import com.example.myapplication.data.entity.Greeting
import kotlinx.coroutines.flow.Flow

/**
 * 挨拶メッセージのリポジトリクラス
 * データアクセス層の抽象化を提供
 */
class GreetingRepository(private val greetingDao: GreetingDao) {
    
    /**
     * 全ての挨拶メッセージを取得
     */
    fun getAllGreetings(): Flow<List<Greeting>> = greetingDao.getAllGreetings()
    
    /**
     * 最初の挨拶メッセージを取得
     */
    suspend fun getFirstGreeting(): Greeting? = greetingDao.getFirstGreeting()
    
    /**
     * 挨拶メッセージを挿入
     */
    suspend fun insertGreeting(greeting: Greeting): Long = greetingDao.insertGreeting(greeting)
}