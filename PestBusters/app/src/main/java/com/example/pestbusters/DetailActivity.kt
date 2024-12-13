package com.example.pestbusters

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.util.Base64
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.pestbusters.databinding.ActivityDetailBinding
import com.example.pestbusters.response.PestData
import com.example.pestbusters.retrofit.ApiConfig
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.CoroutineStart
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val apiServiceTreatments = ApiConfig.apiServiceTreatments

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val classLabel = intent.getStringExtra(CLASS_LABEL)
        binding.tvClassLabel.text = classLabel ?: "No class label found."

//        fetchPestDetails(classLabel.toString())
        fetchAllPestDetails(classLabel.toString())
    }

    private fun fetchAllPestDetails(classLabel: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = ApiConfig.apiServiceTreatments.getAllPestTreatments()
                if (response.isSuccessful && response.body() != null) {
                    val pestList = response.body()?.data
                    val pestData = pestList?.firstOrNull { it.Pest_name.equals(classLabel, ignoreCase = true) }

                    withContext(Dispatchers.Main) {
                        if (pestData != null) {
                            displayPestDetails(pestData)
                        } else {
                            showToast("Pest not found for class label: $classLabel")
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Log.e("DetailActivity", "Error: ${response.errorBody()?.string()}")
                        showToast("Error: ${response.code()}")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("DetailActivity", "Exception: ${e.message}")
                    showToast("Failed: ${e.message}")
                }
            }
        }
    }

    private fun fetchPestDetails(classLabel: String) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                Log.d("DetailActivity", "Class label sent: $classLabel")
                val response = apiServiceTreatments.getPestTreatments(classLabel)
                if (response.isSuccessful && response.body() != null) {
                    val pestDataList = response.body()?.data
                    withContext(Dispatchers.Main) {
                        Log.d("DetailActivity", "Response: ${response.body()}")
                        if (!pestDataList.isNullOrEmpty()) {
                            displayPestDetails(pestDataList[0])
                        } else {
                            showToast("No pest data found.")
                        }
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Log.e("DetailActivity", "Error: ${response.errorBody()?.string()}")
                        showToast("Error: ${response.code()}")
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("DetailActivity", "Exception: ${e.message}")
                    showToast("Failed: ${e.message}")
                }
            }
        }
    }



    private fun displayPestDetails(pestData: PestData?) {
        if (pestData != null) {
            binding.tvClassLabel.text = pestData.Pest_name
            binding.tvMaterial.text = pestData.Treat_material
            binding.tvTreatment.text = pestData.Treatment

            val bitmap = decodeBase64ToBitmap(pestData.Image)
            if (bitmap != null) {
                binding.ivImageDetail.setImageBitmap(bitmap)
            } else {
                showToast("Failed to load image.")
            }
        } else {
            showToast("No details available for this pest.")
        }
    }

    private fun decodeBase64ToBitmap(base64Str: String): Bitmap? {
        return try {
            val decodedBytes = Base64.decode(base64Str, Base64.DEFAULT)
            BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    companion object {
        const val CLASS_LABEL = "CLASS_LABEL"
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

}