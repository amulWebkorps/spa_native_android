package com.example.spa.ui.home.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.spa.R
import com.example.spa.base.listener.BankDetailScreen
import com.example.spa.base.listener.IsolatedListener
import com.example.spa.databinding.ActivityIsolatedBinding
import com.example.spa.ui.auth.fragment.SelectLanguageFragment
import com.example.spa.ui.home.fragment.*
import com.example.spa.utilities.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IsolatedActivity : AppCompatActivity() , IsolatedListener {

    private lateinit var binding: ActivityIsolatedBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        binding = ActivityIsolatedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        when (intent.extras?.get(Constants.SCREEN_NAME)) {
            Constants.EDIT_PROFILE -> {
                loadFragment(EditProfileFragment(),"")
            }
            Constants.BANK_DETAIL -> {
                loadFragment(BankDetailsFragment(),"")
            }
            Constants.WALLET -> {
                loadFragment(WalletFragment(),"")
            }
            Constants.SHARE_QR -> {
                loadFragment(ShareQRFragment(),"")
            }
            Constants.SHARE_QR_ONE_TIME -> {
                loadFragment(ShareQROneTimeFragment(),"")
            }
            Constants.CHANGE_LANGUAGE -> {
                loadFragment(SelectLanguageFragment(),Constants.SETTING)
            }

        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if (intent.extras?.get(Constants.SCREEN_NAME) == Constants.EDIT_PROFILE) {
            val intent = Intent(this, HomeActivity::class.java)
            intent.putExtra(Constants.SCREEN_NAME, Constants.EDIT_PROFILE)
            startActivity(intent)
            this.finish()
        }
    }
     private fun loadFragment(fragment: Fragment,bundleValue: String){
         val args = Bundle()
         args.putString(Constants.ISOLATED,bundleValue)
         fragment.arguments = args
         supportFragmentManager.beginTransaction().add(R.id.placeHolder, fragment).commit()
     }


    override fun replaceFragment(screen: BankDetailScreen, value: String?) {
        val fragment = when (screen) {
            BankDetailScreen.BANK_DETAIL -> BankDetailsFragment()
            BankDetailScreen.ADD_BANK_DETAIL -> AddBankDetailFragment()
            BankDetailScreen.PAYMENT_SUCCESS -> TransactionSuccessfulFragment()
        }
        val args = Bundle()
        args.putString(Constants.AUTH, value)
        fragment.arguments = args

        val fm=supportFragmentManager.beginTransaction()
        fm.replace(R.id.placeHolder, fragment)
            .addToBackStack(null)
        val bsc = supportFragmentManager.backStackEntryCount
//        if (bsc > 1) {
            supportFragmentManager.popBackStack()
//        }
        fm.commit()

//        supportFragmentManager.beginTransaction().replace(R.id.placeHolder, fragment)
//            .addToBackStack(null).commit()

    }

    override fun goBack() {
        val fm: FragmentManager = this.supportFragmentManager
        fm.popBackStack()
    }
}