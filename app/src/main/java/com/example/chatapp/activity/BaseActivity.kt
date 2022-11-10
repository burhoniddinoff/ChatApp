package com.example.chatapp.activity

import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.util.Patterns
import android.view.View
import android.widget.Toast
import com.example.chatapp.R
import com.google.android.material.textfield.TextInputLayout

abstract class BaseActivity: AppCompatActivity() {
    fun intent(from: Activity, to: Activity) {
        startActivity(Intent(from, to::class.java))
    }
    fun toast(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()
    }
    fun View.click(view: (View) -> Unit) {
        this.setOnClickListener {
            view(it)
        }
    }
    fun validate(email: String, password: String, etEmail: TextInputLayout, etPass: TextInputLayout): Boolean {
        etEmail.error = null
        etPass.error = null
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.error = getString(R.string.error_email_not_valid)
            return false
        }
        if (password.length < 8) {
            etPass.error = getString(R.string.error_password_not_valid)
            return false
        }
        return true
    }
}