package com.example.spa.ui.auth.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.commit
import com.example.spa.R
import com.example.spa.base.listener.Listener
import com.example.spa.base.listener.Screen
import com.example.spa.databinding.ActivityAuthBinding
import com.example.spa.ui.auth.fragment.*
import com.example.spa.utilities.Constants
import com.example.spa.utilities.showMessage
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AuthActivity : AppCompatActivity() ,Listener {

    private lateinit var binding: ActivityAuthBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        binding = ActivityAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction().add(R.id.placeHolder, LoginFragment()).commit()

        if (intent.extras?.get(Constants.SESSION_EXPIRE) == true){
            showMessage(binding.root , getString(R.string.session_expire))
        }

        if (intent.extras?.get(Constants.SCREEN_NAME) == Constants.RESET_PASSWORD) {
            showMessage(binding.root,getString(R.string.reset_password_successfully))
          }
        }

    override fun replaceFragment(screen: Screen,value:String?) {

        val fragment = when (screen) {
            Screen.LOGIN -> LoginFragment()
            Screen.SIGN_UP -> SignUpFragment()
            Screen.VERIFICATION -> VerificationFragment()
            Screen.FORGOT_PASSWORD -> ForgotPasswordFragment()
            Screen.RESET_PASSWORD -> ResetPasswordFragment()
        }
        val args = Bundle()
        args.putString(Constants.AUTH, value)
        fragment.arguments = args
        val fm=supportFragmentManager.beginTransaction()
        fm.replace(R.id.placeHolder, fragment)
            .addToBackStack(null)
        val bsc = supportFragmentManager.backStackEntryCount
      //  if (bsc > 1) {
            supportFragmentManager.popBackStack()
       // }
        fm.commit()
    }
    override fun goBack() {
        val fm: FragmentManager = this.supportFragmentManager
        fm.popBackStack()
    }

}