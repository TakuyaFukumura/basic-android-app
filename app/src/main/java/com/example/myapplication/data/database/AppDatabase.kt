package com.example.myapplication.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.myapplication.data.dao.GreetingDao
import com.example.myapplication.data.entity.GreetingEntity

/**
 * アプリケーションのメインデータベース
 * 挨拶データを管理するRoomデータベース
 */
@Database(
    entities = [GreetingEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    /**
     * 挨拶データにアクセスするためのDAO
     */
    abstract fun greetingDao(): GreetingDao

    companion object {
        // データベースのシングルトンインスタンス
        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * データベースインスタンスを取得する
         * @param context アプリケーションコンテキスト
         * @return データベースインスタンス
         */
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}