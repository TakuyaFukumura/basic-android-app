package com.example.myapplication.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.myapplication.data.dao.StringDao
import com.example.myapplication.data.entity.StringEntity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * アプリケーションのメインデータベース
 */
@Database(
    entities = [StringEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    
    abstract fun stringDao(): StringDao
    
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null
        
        fun getDatabase(
            context: Context,
            scope: CoroutineScope
        ): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database"
                )
                .addCallback(AppDatabaseCallback(scope))
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
    
    /**
     * データベース作成時のコールバック
     * "Android"文字列をプリロードする
     */
    private class AppDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {
        
        override fun onCreate(db: SupportSQLiteDatabase) {
            super.onCreate(db)
            INSTANCE?.let { database ->
                scope.launch {
                    val stringDao = database.stringDao()
                    
                    // 既存データを削除
                    stringDao.deleteAllStrings()
                    
                    // "Android"文字列を挿入
                    stringDao.insertString(StringEntity(value = "Android"))
                }
            }
        }
    }
}
