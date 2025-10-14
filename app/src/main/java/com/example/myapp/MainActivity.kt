package com.example.myapp

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.myapp.ui.theme.MyAppTheme
import org.json.JSONArray // Added import for JSONArray

// Constants for SharedPreferences
const val PREFS_NAME = "CopySaverPrefs"
const val KEY_SAVED_COPIES = "saved_copies"

/**
 * Saves the list of copied strings to SharedPreferences.
 * To preserve order, the list is serialized to a JSON string.
 * @param context The application context.
 * @param copies The list of strings to save.
 */
fun saveCopies(context: Context, copies: List<String>) {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    val jsonArray = JSONArray()
    for (copy in copies) {
        jsonArray.put(copy)
    }
    // Store the JSON array as a single string
    prefs.edit().putString(KEY_SAVED_COPIES, jsonArray.toString()).apply()
}

/**
 * Loads the list of copied strings from SharedPreferences.
 * The stored JSON string is deserialized back into a List<String>, preserving order.
 * @param context The application context.
 * @return A list of saved strings, or an empty list if none are found or parsing fails.
 */
fun loadCopies(context: Context): List<String> {
    val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    // Retrieve the JSON string
    val jsonString = prefs.getString(KEY_SAVED_COPIES, null)

    if (jsonString.isNullOrEmpty()) {
        return emptyList()
    }

    val copies = mutableListOf<String>()
    try {
        val jsonArray = JSONArray(jsonString)
        for (i in 0 until jsonArray.length()) {
            copies.add(jsonArray.getString(i))
        }
    } catch (e: Exception) {
        // Handle potential JSON parsing errors (e.g., malformed JSON due to previous data structure)
        e.printStackTrace()
        // If parsing fails, return an empty list to prevent app crashes
        return emptyList()
    }
    return copies
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyAppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    CopySaverApp()
                }
            }
        }
    }
}

@Composable
fun CopySaverApp() {
    val context = LocalContext.current
    val clipboardManager = remember { context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager }

    // Initialize savedCopies by loading from SharedPreferences
    val savedCopies = remember {
        mutableStateListOf<String>().apply {
            addAll(loadCopies(context))
        }
    }
    var currentClipboardContent by remember { mutableStateOf("") }

    // Function to update current clipboard content from the system clipboard
    val updateCurrentClipboardContent = {
        val clipData = clipboardManager.primaryClip
        if (clipData != null && clipData.itemCount > 0) {
            // coerceToText handles various types (text, uri, intent) by converting them to a text representation
            currentClipboardContent = clipData.getItemAt(0).coerceToText(context).toString()
        } else {
            currentClipboardContent = ""
        }
    }

    // Listen for clipboard changes while the composable is active
    DisposableEffect(Unit) {
        val listener = ClipboardManager.OnPrimaryClipChangedListener {
            updateCurrentClipboardContent()
        }
        clipboardManager.addPrimaryClipChangedListener(listener)

        // Initialize current clipboard content when the app starts
        updateCurrentClipboardContent()

        onDispose {
            clipboardManager.removePrimaryClipChangedListener(listener)
        }
    }

    Scaffold(
        topBar = {
            // Using SmallTopAppBar for Material3 consistency for a basic app bar
            SmallTopAppBar(title = { Text("CopySaver") })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Current Clipboard:",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.Start)
            )
            OutlinedTextField(
                value = currentClipboardContent,
                onValueChange = { /* Read-only, clipboard content is external */ },
                label = { Text("Clipboard content") },
                readOnly = true,
                modifier = Modifier.fillMaxWidth(),
                minLines = 3,
                maxLines = 5
            )

            Button(
                onClick = {
                    if (currentClipboardContent.isNotBlank() && !savedCopies.contains(currentClipboardContent)) {
                        savedCopies.add(0, currentClipboardContent) // Add to the top of the list
                        saveCopies(context, savedCopies) // Persist the updated list
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                enabled = currentClipboardContent.isNotBlank()
            ) {
                Text("Save Current Clipboard")
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Saved Copies:",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.align(Alignment.Start)
            )

            if (savedCopies.isEmpty()) {
                Text("No items saved yet.", modifier = Modifier.padding(vertical = 16.dp))
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(savedCopies) { item ->
                        Card(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = item,
                                    modifier = Modifier.weight(1f)
                                )
                                IconButton(
                                    onClick = {
                                        savedCopies.remove(item)
                                        saveCopies(context, savedCopies) // Persist the updated list
                                    },
                                    modifier = Modifier.align(Alignment.Top)
                                ) {
                                    Icon(Icons.Filled.Delete, contentDescription = "Delete saved item")
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}