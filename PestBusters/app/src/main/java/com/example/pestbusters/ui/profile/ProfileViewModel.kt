package com.example.pestbusters.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {

    // Contoh data yang akan ditampilkan dalam ProfileFragment
    private val _username = MutableLiveData<String>().apply {
        value = "arkaan24135"
    }
    private val _email = MutableLiveData<String>().apply {
        value = "arkaan2003@gmail.com"
    }

    // Getter untuk data agar dapat diamati oleh fragment
    val username: LiveData<String> get() = _username
    val email: LiveData<String> get() = _email

    // Fungsi untuk memperbarui data (contoh interaksi user)
    fun updateUsername(newUsername: String) {
        _username.value = newUsername
    }

    fun updateEmail(newEmail: String) {
        _email.value = newEmail
    }
}
