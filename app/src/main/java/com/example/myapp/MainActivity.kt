package com.example.myapp

import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class MainActivity : AppCompatActivity() {

    // Data class to hold copied content and its timestamp
    data class ClipboardItem(
        val content: String,
        val timestamp: String // Store as formatted string
    )

    private lateinit var clipboardManager: ClipboardManager
    private lateinit var lastClipTextView: TextView // TextView to display the last copied item
    private val savedClips = mutableListOf<ClipboardItem>() // List to store ClipboardItem objects

    // Formatter for timestamps, using default locale
    private val dateFormatter = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

    // Listener to detect changes in the primary clip (clipboard)
    private val onPrimaryClipChangedListener = ClipboardManager.OnPrimaryClipChangedListener {
        handleClipboardChange()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Sets the content view to the layout defined in activity_main.xml
        // Ensure activity_main.xml contains a TextView with ID `lastClipTextView`
        setContentView(R.layout.activity_main)

        // Initialize ClipboardManager
        clipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        // Link the TextView from the layout
        lastClipTextView = findViewById(R.id.lastClipTextView)

        // Display the current clipboard content when the activity is first created
        displayCurrentClipboardContent()
    }

    override fun onResume() {
        super.onResume()
        // Register the clipboard listener when the activity becomes active
        clipboardManager.addPrimaryClipChangedListener(onPrimaryClipChangedListener)
        // Also check clipboard content on resume, in case something was copied while the app was in the background
        displayCurrentClipboardContent()
    }

    override fun onPause() {
        super.onPause()
        // Unregister the clipboard listener when the activity is paused to prevent memory leaks
        clipboardManager.removePrimaryClipChangedListener(onPrimaryClipChangedListener)
    }

    /**
     * Handles changes detected in the system clipboard.
     * Updates the UI and simulates saving the copied item.
     */
    private fun handleClipboardChange() {
        displayCurrentClipboardContent() // Update the UI with the new clipboard content
    }

    /**
     * Retrieves and displays the current primary clip content.
     * Also simulates saving the content to an in-memory list with a timestamp.
     */
    private fun displayCurrentClipboardContent() {
        if (clipboardManager.hasPrimaryClip()) {
            val clip = clipboardManager.primaryClip
            if (clip != null && clip.itemCount > 0) {
                // Try to get the text from the first item in the clip data
                // coerceToText handles various clip types and converts them to text
                val copiedText = clip.getItemAt(0).coerceToText(this).toString().trim()

                if (copiedText.isNotBlank()) {
                    val currentTimestamp = dateFormatter.format(Date())
                    val newClipItem = ClipboardItem(copiedText, currentTimestamp)

                    // Simulate saving the copied item to our history
                    // Only add if it's new or different from the last saved item to avoid duplicates
                    // (comparing content only, assuming new timestamp for same content might be desired)
                    if (savedClips.isEmpty() || savedClips.last().content != newClipItem.content) {
                        savedClips.add(newClipItem)
                        // In a real application, you would save this to a persistent storage
                        // (e.g., a database like Room, SharedPreferences)
                        // Log.d("MainActivity", "Saved clip: ${newClipItem.content} at ${newClipItem.timestamp}")
                    }

                    // Always display the most recently added or recognized clip
                    lastClipTextView.text = "Last copied (${newClipItem.timestamp}): ${newClipItem.content}"
                    Toast.makeText(this, "Copied: ${newClipItem.content}", Toast.LENGTH_SHORT).show()
                } else {
                    lastClipTextView.text = "No accessible text copied."
                }
            } else {
                lastClipTextView.text = "Clipboard is empty or contains non-text data."
            }
        } else {
            lastClipTextView.text = "Clipboard is empty."
        }
    }
}