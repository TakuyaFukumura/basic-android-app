package com.example.myapplication.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * 挨拶メッセージを格納するためのRoomエンティティ
 * データベースのgreetingsテーブルに対応
 */
@Entity(tableName = "greetings")
data class GreetingEntity(
    @PrimaryKey val id: Int = 1,
    val name: String
)