package com.example.pestbusters

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class DetailActivityFyi : AppCompatActivity() {

    private lateinit var imgPhoto: ImageView
    private lateinit var tvName: TextView
    private lateinit var tvDescription: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        enableEdgeToEdge()
        setContentView(R.layout.activity_detail_fyi)

        imgPhoto = findViewById(R.id.iv_Image_Detail)
        tvName = findViewById(R.id.tv_Class_Label)
        tvDescription = findViewById(R.id.tv_how_to)

        val placeName = intent.getStringExtra(EXTRA_NAME)
        val placeDescription = intent.getStringExtra(EXTRA_DESCRIPTION)
        val placePhoto = intent.getIntExtra(EXTRA_PHOTO, -1)

        tvName.text = placeName
        tvDescription.text = placeDescription
        imgPhoto.setImageResource(placePhoto)
    }

    companion object {
        const val EXTRA_NAME = "extra_name"
        const val EXTRA_DESCRIPTION = "extra_description"
        const val EXTRA_PHOTO = "extra_photo"
    }
}