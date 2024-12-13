package com.example.pestbusters

import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.example.pestbusters.databinding.ActivityChatBotBinding
import com.example.pestbusters.localserver.LocalServer
import java.io.File

class ChatBotActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBotBinding
    private lateinit var server: LocalServer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        binding = ActivityChatBotBinding.inflate(layoutInflater)
        setContentView(binding.root)

        WebView.setWebContentsDebuggingEnabled(true)

        // Copy chatbot.html from assets to a readable path
        val htmlFilePath = "${filesDir.absolutePath}/chatbot.html"
        assets.open("chatbot.html").use { input ->
            File(htmlFilePath).outputStream().use { output ->
                input.copyTo(output)
            }
        }

        // Start local HTTP server
        server = LocalServer(8080, htmlFilePath)
        server.start()

        // Configure WebView
        binding.webview.settings.javaScriptEnabled = true
        binding.webview.settings.domStorageEnabled = true
        binding.webview.loadUrl("http://localhost:8080")
    }

    override fun onDestroy() {
        super.onDestroy()
        server.stop()
    }
}