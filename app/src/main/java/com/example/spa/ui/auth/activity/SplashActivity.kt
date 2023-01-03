package com.example.spa.ui.auth.activity

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.spa.R
import com.example.spa.databinding.ActivitySplashBinding
import com.example.spa.ui.auth.activity.SplashActivity_MembersInjector.create
import com.example.spa.ui.home.activitiy.HomeActivity
import com.example.spa.utilities.Constants
import com.example.spa.utilities.core.Session
import com.futuremind.recyclerviewfastscroll.viewprovider.VisibilityAnimationManager.Builder
import javax.inject.Inject

class SplashActivity : AppCompatActivity() {

    @Inject
    lateinit var session: Session
    private lateinit var binding: ActivitySplashBinding
    private lateinit var update: Button
    private lateinit var skip:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)
        session = Session(this)

        handler()
//
//        var dialog = Dialog(this)
//        dialog.setContentView(R.layout.layout_update_app);
//        dialog.window!!.setLayout(
//            ViewGroup.LayoutParams.MATCH_PARENT,
//            ViewGroup.LayoutParams.WRAP_CONTENT
//        );
//        dialog.setCancelable(true);
//        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
//        skip = dialog.findViewById(R.id.buttonSkip)
//        update = dialog.findViewById(R.id.buttonUpdate)
//        skip.setOnClickListener {
//            dialog.dismiss()
//            handler()
//        }
//        update.setOnClickListener { dialog.dismiss()
//            handler()
//        }
//        dialog.show();

              }

    private fun handler(){
        binding.layoutBottom.setOnClickListener {
            session.isNotFirstTime = true
            if (session.isLogin){
                val intent= Intent(this, HomeActivity::class.java)
                startActivity(intent)
                this.finishAffinity();
            }
            else {
                val intent = Intent(this, AuthActivity::class.java)
                startActivity(intent)
                this.finishAffinity();
            }
        }
    }
}