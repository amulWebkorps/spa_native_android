package com.example.spa.ui.auth.activity

import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.gif.GifDrawable
import com.bumptech.glide.request.target.ImageViewTarget
import com.example.spa.R
import com.example.spa.data.request.VersionRequest
import com.example.spa.databinding.ActivitySplashMainBinding
import com.example.spa.ui.home.activity.HomeActivity
import com.example.spa.utilities.Constants
import com.example.spa.utilities.Resource
import com.example.spa.utilities.core.Session
import com.example.spa.utilities.hideView
import com.example.spa.utilities.setAppLocale
import com.example.spa.utilities.showMessage
import com.example.spa.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashMainActivity : AppCompatActivity() {

    @Inject
    lateinit var session: Session
    private lateinit var binding: ActivitySplashMainBinding
    private lateinit var update: Button
    private lateinit var skip: Button

    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash_main)

        binding = ActivitySplashMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        session = Session(this)
        handler()
//        binding.gifImageView.setImageResource(R.drawable.splash_animation)
//
//        val animation: Animation =
//            AnimationUtils.loadAnimation(applicationContext, R.anim.animation_left_right)
//        binding.textViewM.startAnimation(animation)
//        binding.textViewTi.startAnimation(animation)
//
//
//        val animation2: Animation =
//            AnimationUtils.loadAnimation(applicationContext, R.anim.animation_right_left)
//        binding.textViewY.startAnimation(animation2)
//        binding.textViewPs.startAnimation(animation2)

        when (session.language) {
            getString(R.string.french) -> {
                setAppLocale(this, "fr")
            }
            getString(R.string.arabic) -> {
                setAppLocale(this, "ar")
            }
            else -> {
                setAppLocale(this, "en")
                session.language ==getString(R.string.english)
            }
        }

        Glide.with(this).asGif().load(R.drawable.splash_anim)
            .into(object : ImageViewTarget<GifDrawable>(binding.splash) {
                override fun setResource(resource: GifDrawable?) {
                    binding.splash.setImageDrawable(resource)
                }
            })
    }


    private fun handler() {
        Handler(Looper.myLooper()!!).postDelayed({
            when {
                session.isLogin -> {
                    val intent = Intent(this, AuthActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    val bundle = Bundle()
                    bundle.putBoolean(Constants.COME_FROM_SPLASH, true)
                    intent.putExtras(bundle)
                    startActivity(intent)
                    this.finishAffinity();

                    //val intent= Intent(this, HomeActivity::class.java)
                    //startActivity(intent)
                    //this.finishAffinity();
                }

                else -> {
                    val intent = Intent(this, TutorialActivity::class.java)
                    startActivity(intent)
                    this.finishAffinity();
                }
            }
            /*when {
                 !session.isNotFirstTime -> {
                    val intent= Intent(this, TutorialActivity::class.java)
                    startActivity(intent)
                    this.finishAffinity();
                }
                else -> {
                    when {
                        session.isLogin -> {
                            val intent= Intent(this, HomeActivity::class.java)
                            startActivity(intent)
                            this.finishAffinity();
                        }
                        else -> {
                            val intent = Intent(this, AuthActivity::class.java)
                            startActivity(intent)
                            this.finishAffinity();
                        }
                    }
                }
            }*/
        }, 1500)
    }


}
