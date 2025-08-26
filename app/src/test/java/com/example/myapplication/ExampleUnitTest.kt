package com.example.myapplication

import org.junit.Test
import org.junit.Assert.*
import com.example.myapplication.data.entity.StringEntity

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
    
    @Test
    fun stringEntity_creation_isCorrect() {
        val entity = StringEntity(id = 1, value = "Android")
        assertEquals(1, entity.id)
        assertEquals("Android", entity.value)
    }
    
    @Test
    fun stringEntity_defaultId_isZero() {
        val entity = StringEntity(value = "Test")
        assertEquals(0, entity.id)
        assertEquals("Test", entity.value)
    }
}
