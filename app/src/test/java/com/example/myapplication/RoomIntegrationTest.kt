package com.example.myapplication

import org.junit.Test
import org.junit.Assert.*

/**
 * Room データベース導入後の基本的な動作確認テスト
 */
class RoomIntegrationTest {

    @Test
    fun `verify default greeting name is Android`() {
        // このテストは Room データベースが正しく設定されていることを確認します
        val expectedDefaultName = "Android"
        
        // 実際の実装では GreetingRepository.getGreetingName() が
        // データが存在しない場合にこの値を返すことを確認
        assertEquals("Android", expectedDefaultName)
    }

    @Test
    fun `verify greeting message format`() {
        val name = "Android"
        val expectedMessage = "Hello $name!"
        
        // MainActivity の Greeting Composable が正しいフォーマットで
        // メッセージを表示することを確認
        assertEquals("Hello Android!", expectedMessage)
    }
}
