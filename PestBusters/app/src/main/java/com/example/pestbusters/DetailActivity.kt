package com.example.pestbusters

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val classLabel = intent.getStringExtra("CLASS_LABEL")
        val classLabelTextView = findViewById<TextView>(R.id.tvClassLabel)
        classLabelTextView.text = classLabel ?: "No class label found."
    }
}