package com.example.myapplication.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.myapplication.data.entity.GreetingEntity

/**
 * 挨拶データにアクセスするためのDAO (Data Access Object)
 */
@Dao
interface GreetingDao {

    /**
     * 挨拶メッセージを取得する
     * @return 挨拶エンティティ（存在しない場合はnull）
     */
    @Query("SELECT * FROM greetings WHERE id = 1")
    suspend fun getGreeting(): GreetingEntity?

    /**
     * 挨拶メッセージを挿入または更新する
     * @param greeting 挿入する挨拶エンティティ
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertGreeting(greeting: GreetingEntity)
}