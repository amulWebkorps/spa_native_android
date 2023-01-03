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
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.spa.R
import com.example.spa.data.request.VersionRequest
import com.example.spa.databinding.ActivitySplashMainBinding
import com.example.spa.ui.home.activitiy.HomeActivity
import com.example.spa.utilities.Resource
import com.example.spa.utilities.core.Session
import com.example.spa.utilities.hideView
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
    private lateinit var skip:Button

   private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        binding = ActivitySplashMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        session = Session(this)

        val pm: PackageManager = applicationContext.packageManager
        val pkgName = applicationContext.packageName
        var pkgInfo: PackageInfo? = null
        try {
            pkgInfo = pm.getPackageInfo(pkgName, 0)
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }
        val ver: String = pkgInfo!!.versionName

        response()
        lifecycleScope.launchWhenCreated {
            authViewModel.checkVersion(
            VersionRequest(
                device_type = "android",
                version = ver
            )
          )
        }
    }

    private fun showDialog(hideSkip : Boolean){
        var dialog = Dialog(this)
        dialog.setContentView(R.layout.layout_update_app);
        dialog.window!!.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        );
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(false)
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        skip = dialog.findViewById(R.id.buttonSkip)
        update = dialog.findViewById(R.id.buttonUpdate)
        if (hideSkip){
            skip.hideView()
        }
        skip.setOnClickListener {
            dialog.dismiss()
            handler()
        }
        update.setOnClickListener { dialog.dismiss()
            handler()
        }
        dialog.show();

    }
    private fun handler(){

        Handler(Looper.myLooper()!!).postDelayed({

            when {
                 !session.isNotFirstTime -> {
                    val intent= Intent(this, SplashActivity::class.java)
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
            }
       }, 2000)
    }


    private fun response(){

        lifecycleScope.launchWhenCreated {
            authViewModel.versionManager.collect { result ->
                when (result) {
                    is Resource.Error -> {
                        result.message?.let {it->
                            showMessage(binding.root,it)
                        }
                    }
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        result.data?.let { it ->
                            when {
                                it.isForceUpdate -> {
                                    showDialog(true)
                                }
                                it.isSoftUpdate && !it.isForceUpdate -> {
                                    showDialog(false)
                                }
                                else -> {
                                    handler()
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}