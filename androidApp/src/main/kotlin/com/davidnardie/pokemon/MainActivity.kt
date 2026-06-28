package com.davidnardie.pokemon

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.davidnardie.pokemon.di.appModule
import com.davidnardie.pokemon.data.local.initSettingsContext
import com.davidnardie.pokemon.di.initDatabaseContext
import org.koin.core.context.GlobalContext
import org.koin.core.context.startKoin

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)
        initDatabaseContext(this)
        initSettingsContext(this)
        if (GlobalContext.getOrNull() == null) {
            startKoin {
                modules(appModule)
            }
        }

        setContent {
            App()
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}