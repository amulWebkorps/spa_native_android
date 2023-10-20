package com.example.spa.ui.auth.activity

import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import androidx.lifecycle.lifecycleScope
import com.example.spa.App
import com.example.spa.R
import com.example.spa.Url.SUPPORT_MAIL
import com.example.spa.base.DialogUtils
import com.example.spa.base.listener.Listener
import com.example.spa.base.listener.Screen
import com.example.spa.data.request.VersionRequest
import com.example.spa.databinding.ActivityAuthBinding
import com.example.spa.ui.auth.fragment.*
import com.example.spa.utilities.Constants
import com.example.spa.utilities.Resource
import com.example.spa.utilities.core.Session
import com.example.spa.utilities.hideView
import com.example.spa.utilities.showMessage
import com.example.spa.viewmodel.AuthViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : AppCompatActivity(), Listener {

    private val authViewModel: AuthViewModel by viewModels()

    private lateinit var binding: ActivityAuthBinding
    private lateinit var update: Button
    private lateinit var skip: Button
    private lateinit var session: Session

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        Log.e("error", "rohit test")
        binding = ActivityAuthBinding.inflate(layoutInflater)
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
        session.appVersion = ver

        response()
        lifecycleScope.launchWhenCreated {
            authViewModel.checkVersion(
                VersionRequest(
                    device_type = "android",
                    version = ver
                )
            )
        }
        if (intent.extras?.get(Constants.COME_FROM_SPLASH) == true) {
            supportFragmentManager.beginTransaction()
                .add(R.id.placeHolder, AfterSignUpWelcomeScreen()).commit()
        } else {
            supportFragmentManager.beginTransaction().add(R.id.placeHolder, LoginFragment())
                .commit()

        }
        if (intent.extras?.get(Constants.COME_FROM_SUSPEND) == true) {
            DialogUtils().showGeneralDialog(
                this,
                message = getString(R.string.suspended_msg) + " " + SUPPORT_MAIL,
                negativeText = getString(R.string.button_ok),
                onNoClick = {

                })
        }

        if (intent.extras?.get(Constants.SESSION_EXPIRE) == true) {
            showMessage(binding.root, getString(R.string.session_expire))
        }

        if (intent.extras?.get(Constants.SCREEN_NAME) == Constants.RESET_PASSWORD) {
            showMessage(binding.root, getString(R.string.reset_password_successfully))
        }
    }

    override fun replaceFragment(screen: Screen, value: String?) {
        val fragment = when (screen) {
            Screen.LOGIN -> LoginFragment()
            Screen.SIGN_UP -> SignUpFragment()
            Screen.AFTER_SIGN_IN -> AfterSignUpWelcomeScreen()
            Screen.VERIFICATION -> VerificationFragment()
            Screen.FORGOT_PASSWORD -> ForgotPasswordFragment()
            Screen.RESET_PASSWORD -> ResetPasswordFragment()
        }

        val args = Bundle()
        args.putString(Constants.AUTH, value)
        fragment.arguments = args
        val fm = supportFragmentManager.beginTransaction()
        fm.replace(R.id.placeHolder, fragment)
            .addToBackStack(null)
        val bsc = supportFragmentManager.backStackEntryCount
        if (bsc > 1) {
            supportFragmentManager.popBackStack()
        }
        fm.commit()
    }

    override fun goBack() {
        val fm: FragmentManager = this.supportFragmentManager
        fm.popBackStack()
    }


    private fun showDialog(hideSkip: Boolean) {
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
        if (hideSkip) {
            skip.hideView()
        }
        skip.setOnClickListener {
            dialog.dismiss()
        }
        update.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show();
    }

    private fun response() {
        lifecycleScope.launchWhenCreated {
            authViewModel.versionManager.collect { result ->
                when (result) {
                    is Resource.Error -> {
                        result.message?.let { it ->
                            showMessage(binding.root, it)
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

                                }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
    }
}