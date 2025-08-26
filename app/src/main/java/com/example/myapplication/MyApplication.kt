package com.example.myapplication

import android.app.Application
import com.example.myapplication.data.database.AppDatabase
import com.example.myapplication.data.repository.StringRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

/**
 * アプリケーションクラス
 * データベースとリポジトリを初期化する
 */
class MyApplication : Application() {
    
    // アプリケーションスコープのCoroutineScope
    private val applicationScope = CoroutineScope(SupervisorJob())
    
    // データベースのインスタンス（遅延初期化）
    val database by lazy { 
        AppDatabase.getDatabase(this, applicationScope) 
    }
    
    // リポジトリのインスタンス（遅延初期化）
    val repository by lazy { 
        StringRepository(database.stringDao()) 
    }
}
