package com.example.myapplication.data.entity

import org.junit.Test
import org.junit.Assert.*

/**
 * Greetingエンティティのユニットテスト
 */
class GreetingTest {
    
    @Test
    fun greeting_creation_isCorrect() {
        val greeting = Greeting(id = 1, name = "Test")
        assertEquals(1, greeting.id)
        assertEquals("Test", greeting.name)
    }
    
    @Test
    fun greeting_default_id_isZero() {
        val greeting = Greeting(name = "Test")
        assertEquals(0, greeting.id)
        assertEquals("Test", greeting.name)
    }
}