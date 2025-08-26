package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.example.myapplication.data.database.AppDatabase
import com.example.myapplication.data.repository.GreetingRepository
import com.example.myapplication.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    
    // データベースとリポジトリのインスタンス
    private lateinit var greetingRepository: GreetingRepository
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        // データベースとリポジトリの初期化
        val database = AppDatabase.getDatabase(this)
        greetingRepository = GreetingRepository(database.greetingDao())
        
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GreetingFromDatabase(
                        repository = greetingRepository,
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

/**
 * データベースから挨拶名を取得して表示するComposable関数
 */
@Composable
fun GreetingFromDatabase(
    repository: GreetingRepository,
    modifier: Modifier = Modifier
) {
    // 挨拶名の状態管理
    var greetingName by remember { mutableStateOf("") }
    
    // データベースから挨拶名を取得
    LaunchedEffect(Unit) {
        greetingName = repository.getGreetingName()
    }
    
    Greeting(
        name = greetingName,
        modifier = modifier
    )
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }
}
