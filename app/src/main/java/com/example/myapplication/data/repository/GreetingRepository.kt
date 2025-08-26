package com.example.myapplication.data.repository

import com.example.myapplication.data.dao.GreetingDao
import com.example.myapplication.data.entity.GreetingEntity

/**
 * 挨拶データの管理を行うリポジトリクラス
 * データベースアクセスの抽象化を提供
 */
class GreetingRepository(private val greetingDao: GreetingDao) {

    /**
     * 挨拶メッセージを取得する
     * データが存在しない場合は初期データを挿入してから返す
     * @return 挨拶名（"Android"など）
     */
    suspend fun getGreetingName(): String {
        val greeting = greetingDao.getGreeting()
        return if (greeting != null) {
            greeting.name
        } else {
            // 初期データを挿入
            val defaultGreeting = GreetingEntity(id = 1, name = "Android")
            greetingDao.insertGreeting(defaultGreeting)
            defaultGreeting.name
        }
    }

    /**
     * 挨拶メッセージを更新する
     * @param name 新しい挨拶名
     */
    suspend fun updateGreetingName(name: String) {
        val greeting = GreetingEntity(id = 1, name = name)
        greetingDao.insertGreeting(greeting)
    }
}