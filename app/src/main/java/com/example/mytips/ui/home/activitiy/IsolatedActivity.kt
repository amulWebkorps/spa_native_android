package com.example.mytips.ui.home.activitiy

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.example.mytips.R
import com.example.mytips.base.listener.BankDetailScreen
import com.example.mytips.base.listener.IsolatedListener
import com.example.mytips.databinding.ActivityIsolatedBinding
import com.example.mytips.ui.home.fragment.*
import com.example.mytips.utilities.Constants

class IsolatedActivity : AppCompatActivity() , IsolatedListener {

    private lateinit var binding: ActivityIsolatedBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)
        binding = ActivityIsolatedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        when (intent.extras?.get(Constants.SCREEN_NAME)) {
            Constants.EDIT_PROFILE -> {
                loadFragment(EditProfileFragment())
            }
            Constants.BANK_DETAIL -> {
                loadFragment(BankDetailsFragment())
            }
            Constants.WALLET -> {
                loadFragment(WalletFragment())
            }
            Constants.SHARE_QR -> {
                loadFragment(ShareQRFragment())
            }
        }
    }

     private fun loadFragment(fragment: Fragment){
         supportFragmentManager.beginTransaction().add(R.id.placeHolder, fragment).commit()
     }


    override fun replaceFragment(screen: BankDetailScreen, value: String?) {
        val fragment = when (screen) {
            BankDetailScreen.BANK_DETAIL -> BankDetailsFragment()
            BankDetailScreen.ADD_BANK_DETAIL -> AddBankDetailFragment()
        }
        val args = Bundle()
        args.putString(Constants.AUTH, value)
        fragment.arguments = args

        supportFragmentManager.beginTransaction().replace(R.id.placeHolder, fragment)
            .addToBackStack(null).commit()

    }

    override fun goBack() {
        val fm: FragmentManager = this.getSupportFragmentManager()
        fm.popBackStack()
    }
}