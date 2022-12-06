package com.example.mytips.ui.auth.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import com.example.mytips.R
import com.example.mytips.databinding.ActivitySplashBinding
import com.example.mytips.ui.home.activitiy.HomeActivity
import com.example.mytips.utilities.core.Session
import javax.inject.Inject

class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var session: Session
    private lateinit var binding: ActivitySplashBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        session = Session(this)

                Handler(Looper.myLooper()!!).postDelayed({
                    if (session.isLogin){
                        val intent=Intent(this, HomeActivity::class.java)
                        startActivity(intent)
                        this.finishAffinity();
                    }
                        else {
                            val intent = Intent(this, AuthActivity::class.java)
                            startActivity(intent)
                            this.finishAffinity();
                        }
                }, 2000)
              }
}