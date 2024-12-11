package com.example.pestbusters

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Fyi(
    val name: String,
    val description: String,
    val photo: Int
) : Parcelable
