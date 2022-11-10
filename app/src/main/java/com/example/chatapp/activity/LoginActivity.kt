package com.example.chatapp.activity

import android.os.Bundle
import com.example.chatapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity: BaseActivity() {
    private val binding by lazy { ActivityLoginBinding.inflate(layoutInflater) }
    private val auth by lazy { FirebaseAuth.getInstance() }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.hide()
        binding.registerButton.click {
            intent(this, RegisterActivity())
        }

        binding.loginButton.click {
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            if (validate(email, password, binding.emailInput, binding.passwordInput)) {
                binding.loadingProgressBar.isIndeterminate = true
                login(email, password)
            }
        }
    }
    private fun login(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    binding.loadingProgressBar.isIndeterminate = false
                    toast("Logged in")
                    intent(this, MainActivity())
                    finish()
                } else {
                    binding.loadingProgressBar.isIndeterminate = false
                    toast("User doesn't exists")
                }
            }
    }
}