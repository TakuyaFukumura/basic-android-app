package com.example.myapplication.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 挨拶メッセージを格納するエンティティクラス
 */
@Entity(tableName = "greetings")
data class Greeting(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String
)