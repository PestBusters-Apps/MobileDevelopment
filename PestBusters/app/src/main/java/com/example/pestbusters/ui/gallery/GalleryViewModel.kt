package com.example.pestbusters.ui.gallery

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pestbusters.response.PestData
import com.example.pestbusters.retrofit.ApiConfig
import kotlinx.coroutines.launch

class GalleryViewModel : ViewModel() {
    private val _pests = MutableLiveData<List<PestData>>()
    val pests: LiveData<List<PestData>> get() = _pests

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    fun fetchAllPests() {
        viewModelScope.launch {
            try {
                val response = ApiConfig.apiServiceTreatments.getAllPestTreatments()
                if (response.isSuccessful && response.body() != null) {
                    _pests.postValue(response.body()?.data ?: emptyList())
                } else {
                    _errorMessage.postValue("Error: ${response.code()}")
                }
            } catch (e: Exception) {
                _errorMessage.postValue("Failed: ${e.message}")
            }
        }
    }
}
