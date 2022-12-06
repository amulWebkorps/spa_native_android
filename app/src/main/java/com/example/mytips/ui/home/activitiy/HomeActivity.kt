package com.example.mytips.ui.home.activitiy

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.mytips.R
import com.example.mytips.base.listener.Listener
import com.example.mytips.base.listener.Screen
import com.example.mytips.databinding.ActivityHomeBinding
import com.example.mytips.databinding.ActivitySplashBinding
import com.example.mytips.ui.auth.activity.AuthActivity
import com.example.mytips.ui.auth.fragment.LoginFragment
import com.example.mytips.ui.auth.fragment.SignUpFragment
import com.example.mytips.ui.auth.fragment.VerificationFragment
import com.example.mytips.ui.home.fragment.SendFragment
import com.example.mytips.ui.home.fragment.SettingsFragment
import com.example.mytips.ui.home.fragment.TransactionFragment
import com.example.mytips.utilities.Constants
import com.example.mytips.utilities.core.Session
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_home.*
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() , Listener {

    @Inject
    lateinit var session: Session
    private lateinit var binding: ActivityHomeBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        session = Session(this)

        binding.includeToolbar.imageViewWallet.setOnClickListener {
            openActivity(Constants.WALLET)
        }

        setFragment()
    }

    private fun setFragment() {
        val firstFragment=SendFragment(this)
        val secondFragment=TransactionFragment()
        val thirdFragment=SettingsFragment()

        setCurrentFragment(firstFragment)

        bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.send->setCurrentFragment(firstFragment)
                R.id.transaction->setCurrentFragment(secondFragment)
                R.id.settings->setCurrentFragment(thirdFragment)

            }
            true
        }
    }


    private fun openActivity(fragment: String){
        val intent= Intent(this, IsolatedActivity::class.java)
        intent.putExtra(Constants.SCREEN_NAME,fragment)
        startActivity(intent)
    }
    private fun setCurrentFragment(fragment: Fragment)=
        supportFragmentManager.beginTransaction().apply {
            replace(R.id.flFragment,fragment)
            commit()
        }

    override fun replaceFragment(screen: Screen, value: String?) {

    }

    override fun goBack() {
    }
}