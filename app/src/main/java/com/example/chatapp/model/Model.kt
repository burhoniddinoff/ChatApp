package com.example.chatapp.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val name: String = "",
    val email: String = "",
    val password: String = "",
    val uid: String = "",
    val photoUri: String = ""
): Parcelable