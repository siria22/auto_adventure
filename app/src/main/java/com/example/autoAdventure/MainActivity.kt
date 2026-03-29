package com.example.autoAdventure

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.example.autoAdventure.common.nav.AppNavGraph
import com.example.presentation.component.theme.AutoAdventureTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope  // 여기부터
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject                // 여기까지 테스트용

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var debugDataInitializer: DebugDataInitializer // 테스트용

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        CoroutineScope(Dispatchers.IO).launch { // 테스트용
            debugDataInitializer.init()
        }

        enableEdgeToEdge()
        setContent {
            AutoAdventureTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    AppNavGraph(modifier = Modifier.padding(innerPadding))
                }
            }
        }
    }
}
