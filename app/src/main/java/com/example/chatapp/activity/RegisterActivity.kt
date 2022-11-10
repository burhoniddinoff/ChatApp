package com.example.chatapp.activity

import android.net.Uri
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import com.example.chatapp.databinding.ActivityRegisterBinding
import com.example.chatapp.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class RegisterActivity : BaseActivity() {
    private val binding by lazy { ActivityRegisterBinding.inflate(layoutInflater) }
    private val auth by lazy { FirebaseAuth.getInstance() }
    private val dbRef by lazy { FirebaseDatabase.getInstance().reference }
    private var photoUri: Uri? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.backButton.click {
            finish()
        }
        binding.imageView.click {
            pickImage.launch("image/*")
        }
        binding.registerButton.click {
            binding.loadingProgressBar.isIndeterminate = true
            val name = binding.nameEditText.text.toString().trim()
            val email = binding.emailEditText.text.toString().trim()
            val password = binding.passwordEditText.text.toString().trim()
            if (name.isNotBlank() && validate(
                    email,
                    password,
                    binding.emailInput,
                    binding.passwordInput
                ) && photoUri != null
            ) {
                register(name, email, password)
            }
        }
    }

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) {
        it?.let { imageUri ->
            photoUri = imageUri
            binding.imageView.setImageURI(imageUri)
        }
    }

    private fun register(name: String, email: String, password: String) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    binding.loadingProgressBar.isIndeterminate = false
                    toast("Sign up success")
                    addToDatabase(name, email, password, auth.currentUser?.uid!!)
                    intent(this, MainActivity())
                    finish()
                } else {
                    toast(it.exception?.message.toString())
                    binding.loadingProgressBar.isIndeterminate = false
                }
            }
    }

    private fun addToDatabase(name: String, email: String, password: String, uid: String) {
        val fileName = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("images/$fileName")
        ref.putFile(photoUri!!)
            .addOnSuccessListener {
                ref.downloadUrl.addOnSuccessListener {
                    dbRef.child("users/$uid")
                        .setValue(User(name, email, password, uid, it.toString()))
                }
            }
            .addOnFailureListener {
                toast(it.message.toString())
            }
    }
}