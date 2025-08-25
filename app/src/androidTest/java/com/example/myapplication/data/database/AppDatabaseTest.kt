package com.example.myapplication.data.database

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.myapplication.data.entity.Greeting
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.Assert.*

/**
 * Room データベースの統合テスト
 */
@RunWith(AndroidJUnit4::class)
class AppDatabaseTest {
    
    private lateinit var database: AppDatabase
    
    @Before
    fun createDb() {
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            AppDatabase::class.java
        ).build()
    }
    
    @After
    fun closeDb() {
        database.close()
    }
    
    @Test
    fun insertAndRetrieveGreeting() = runBlocking {
        val greeting = Greeting(name = "Test")
        val greetingDao = database.greetingDao()
        
        val id = greetingDao.insertGreeting(greeting)
        assertTrue(id > 0)
        
        val retrievedGreeting = greetingDao.getGreetingById(id)
        assertNotNull(retrievedGreeting)
        assertEquals("Test", retrievedGreeting?.name)
    }
    
    @Test
    fun getFirstGreeting() = runBlocking {
        val greetingDao = database.greetingDao()
        
        // 初期状態では null
        val firstGreeting = greetingDao.getFirstGreeting()
        assertNull(firstGreeting)
        
        // データを挿入
        greetingDao.insertGreeting(Greeting(name = "First"))
        greetingDao.insertGreeting(Greeting(name = "Second"))
        
        // 最初のデータを取得
        val retrieved = greetingDao.getFirstGreeting()
        assertNotNull(retrieved)
        assertEquals("First", retrieved?.name)
    }
}
