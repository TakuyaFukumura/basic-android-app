package com.example.myapplication.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import android.content.Context
import com.example.myapplication.data.dao.GreetingDao
import com.example.myapplication.data.entity.Greeting
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

/**
 * アプリケーションのメインデータベース
 */
@Database(
    entities = [Greeting::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    
    abstract fun greetingDao(): GreetingDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                    .addCallback(DatabaseCallback())
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
    
    /**
     * データベース作成時のコールバック
     * 初期データを挿入する
     */
    private class DatabaseCallback : RoomDatabase.Callback() {
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            // 初期データを挿入
            INSTANCE?.let { database ->
                CoroutineScope(Dispatchers.IO).launch {
                    database.greetingDao().insertGreeting(Greeting(name = "Android"))
                }
            }
        }
    }
}