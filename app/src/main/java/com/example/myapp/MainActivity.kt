package com.example.myapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.myapp.ui.theme.MyAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyAppTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CopySaverScreen()
                }
            }
        }
    }
}

@Composable
fun CopySaverScreen(modifier: Modifier = Modifier) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "CopySaver App",
            style = MaterialTheme.typography.headlineLarge
        )
        Text(
            text = "Your clipboard history, saved.",
            style = MaterialTheme.typography.bodyLarge
        )
        // Future implementation for displaying saved clips would go here
    }
}

@Preview(showBackground = true)
@Composable
fun CopySaverScreenPreview() {
    MyAppTheme {
        CopySaverScreen()
    }
}