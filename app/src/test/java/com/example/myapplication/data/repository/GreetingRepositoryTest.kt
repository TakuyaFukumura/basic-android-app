package com.example.myapplication.data.repository

import com.example.myapplication.data.dao.GreetingDao
import com.example.myapplication.data.entity.GreetingEntity
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Test

/**
 * GreetingRepositoryのユニットテスト
 * Room データベース機能の基本的な動作確認
 */
class GreetingRepositoryTest {

    // テスト用のシンプルなDAOモック
    private class TestGreetingDao : GreetingDao {
        private var greeting: GreetingEntity? = null

        override suspend fun getGreeting(): GreetingEntity? = greeting

        override suspend fun insertGreeting(greeting: GreetingEntity) {
            this.greeting = greeting
        }
    }

    @Test
    fun `getGreetingName returns default Android when no data exists`() = runTest {
        // Arrange
        val testDao = TestGreetingDao()
        val repository = GreetingRepository(testDao)
        
        // Act
        val result = repository.getGreetingName()
        
        // Assert
        assertEquals("Android", result)
    }

    @Test
    fun `getGreetingName returns existing name when data exists`() = runTest {
        // Arrange
        val testDao = TestGreetingDao()
        testDao.insertGreeting(GreetingEntity(id = 1, name = "TestName"))
        val repository = GreetingRepository(testDao)
        
        // Act
        val result = repository.getGreetingName()
        
        // Assert
        assertEquals("TestName", result)
    }

    @Test
    fun `updateGreetingName updates the greeting correctly`() = runTest {
        // Arrange
        val testDao = TestGreetingDao()
        val repository = GreetingRepository(testDao)
        val newName = "NewTestName"
        
        // Act
        repository.updateGreetingName(newName)
        val result = repository.getGreetingName()
        
        // Assert
        assertEquals(newName, result)
    }
}
