package com.example.pestbusters

import android.os.Bundle
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.appcompat.app.AppCompatActivity
import com.example.pestbusters.databinding.ActivityChatBotBinding

class ChatBotActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatBotBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        binding = ActivityChatBotBinding.inflate(layoutInflater)
        setContentView(binding.root)

        WebView.setWebContentsDebuggingEnabled(true)

        val myWebView = binding.webview
        myWebView.settings.javaScriptEnabled = true
        myWebView.settings.domStorageEnabled = true
        myWebView.settings.mixedContentMode = WebSettings.MIXED_CONTENT_COMPATIBILITY_MODE
        myWebView.loadUrl("file:///android_asset/chatbot.html")
    }
}