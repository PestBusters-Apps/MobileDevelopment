package com.example.pestbusters

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.pestbusters.databinding.ActivityMainBinding
import com.example.pestbusters.retrofit.ApiConfig
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import android.Manifest
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val apiService = ApiConfig.apiService
    private lateinit var rvFyi: RecyclerView
    private val list = ArrayList<Fyi>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_gallery, R.id.navigation_history, R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

        val imagePicker = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let { uploadImage(it) }
        }

        binding.fabPickImage.setOnClickListener {
            if (hasReadMediaPermission()) {
                imagePicker.launch("image/*")
            } else {
                requestReadMediaPermission()
            }
        }

        rvFyi = findViewById(R.id.rv_fyi)
        rvFyi.setHasFixedSize(true)

        list.addAll(getListfyi())
        showRecyclerList()

//        binding.fabPickImage.setOnClickListener {
//            val intent = Intent(this, DetailActivity::class.java)
//            startActivity(intent)
//        }
    }

    private fun getListfyi(): ArrayList<Fyi> {
        val dataName = resources.getStringArray(R.array.data_name)
        val dataDescription = resources.getStringArray(R.array.data_description)
        val dataPhoto = resources.obtainTypedArray(R.array.data_photo)
        val listFyi = ArrayList<Fyi>()
        for (i in dataName.indices) {
            val fyi = Fyi(dataName[i], dataDescription[i], dataPhoto.getResourceId(i, -1))
            listFyi.add(fyi)
        }
        return listFyi
    }

    private fun showRecyclerList() {
        rvFyi.layoutManager = LinearLayoutManager(this)
        val listFyiAdapter = ListFyiAdapter(list)
        rvFyi.adapter = listFyiAdapter
        listFyiAdapter.setOnItemClickCallback(object : ListFyiAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Fyi) {
                showSelectedPlace(data)
            }
        })
    }

    private fun showSelectedPlace(place: Fyi) {
        val intent = Intent(this, DetailActivityFyi::class.java)
        intent.putExtra(DetailActivityFyi.EXTRA_NAME, place.name)
        intent.putExtra(DetailActivityFyi.EXTRA_DESCRIPTION, place.description)
        intent.putExtra(DetailActivityFyi.EXTRA_PHOTO, place.photo)
        startActivity(intent)
    }

    private fun hasReadMediaPermission(): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_MEDIA_IMAGES
            ) == PackageManager.PERMISSION_GRANTED
        } else {
            ContextCompat.checkSelfPermission(
                this, Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestReadMediaPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_MEDIA_IMAGES),
                REQUEST_READ_MEDIA_IMAGES
            )
        } else {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                REQUEST_READ_EXTERNAL_STORAGE
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_READ_MEDIA_IMAGES || requestCode == REQUEST_READ_EXTERNAL_STORAGE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission Granted!", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Permission Denied!", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun uploadImage(imageUri: Uri) {
        val file = uriToFile(imageUri)
        file?.let {
            Log.d("ImageUpload", "File created: ${it.absolutePath}")

            val requestBody = RequestBody.create("image/*".toMediaTypeOrNull(), it)
            val multipartBody = MultipartBody.Part.createFormData("image", it.name, requestBody)

            lifecycleScope.launch {
                try {
                    val response = apiService.uploadImage(multipartBody)
                    if (response.isSuccessful) {
                        Log.d("ImageUpload", "Upload successful: ${response.body()}")

                        val predictions = response.body()?.predictions
                        if (!predictions.isNullOrEmpty()) {
                            val firstClassLabel = predictions[0].class_label
                            navigateToDetail(firstClassLabel)
                        } else {
                            showToast("No predictions found.")
                        }
                    } else {
                        Log.e("ImageUpload", "Upload failed: ${response.errorBody()?.string()}")

                        showToast("Upload failed.")
                    }
                } catch (e: Exception) {
                    Log.e("ImageUpload", "An error occurred: ${e.message}")

                    showToast("An error occurred: ${e.message}")
                }
            }
        } ?: run {
            showToast("Failed to convert image.")
        }
    }

    private fun uriToFile(uri: Uri): File? {
        val contentResolver = contentResolver
        val fileName = getFileName(uri)
        val file = File(cacheDir, fileName)

        return try {
            val inputStream: InputStream? = contentResolver.openInputStream(uri)
            val outputStream = FileOutputStream(file)
            inputStream?.copyTo(outputStream)
            inputStream?.close()
            outputStream.close()

            Log.d("ImageUpload", "File copied to: ${file.absolutePath}")

            file
        } catch (e: Exception) {
            Log.e("ImageUpload", "Error converting URI to file: ${e.message}")

            null
        }
    }

    private fun getFileName(uri: Uri): String {
        var fileName = "temp_image"
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            val nameIndex = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (nameIndex >= 0 && it.moveToFirst()) {
                fileName = it.getString(nameIndex)
            }
        }
        return fileName
    }

    private fun navigateToDetail(classLabel: String) {
        val intent = Intent(this, DetailActivity::class.java)
        intent.putExtra("CLASS_LABEL", classLabel)
        startActivity(intent)
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    companion object {
        private const val REQUEST_READ_EXTERNAL_STORAGE = 101
        private const val REQUEST_READ_MEDIA_IMAGES = 102
    }

}