package com.example.myapplication.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * データベースに格納する文字列を表すエンティティ
 */
@Entity(tableName = "strings")
data class StringEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val value: String
)
