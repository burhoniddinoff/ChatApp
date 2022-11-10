package com.example.chatapp.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import com.example.chatapp.R
import com.google.firebase.auth.FirebaseAuth

class IntroActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        supportActionBar?.hide()
        val auth = FirebaseAuth.getInstance()
        object : CountDownTimer(2000, 1000) {
            override fun onTick(millisUntilFinished: Long) {}

            override fun onFinish() {
                if (auth.currentUser != null) {
                    intent(this@IntroActivity, MainActivity())
                    finish()
                } else {
                    intent(this@IntroActivity, LoginActivity())
                    finish()
                }
            }
        }.start()
    }
}