package com.example.spa.ui.home.activitiy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import com.example.spa.R
import com.example.spa.base.listener.Listener
import com.example.spa.base.listener.Screen
import com.example.spa.databinding.ActivityHomeBinding
import com.example.spa.ui.home.fragment.SendFragment
import com.example.spa.ui.home.fragment.SettingsFragment
import com.example.spa.ui.home.fragment.TransactionFragment
import com.example.spa.utilities.Constants
import com.example.spa.utilities.GlideUtils
import com.example.spa.utilities.core.Session
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

        if (session.user!!.image != null) {
            GlideUtils.loadImage(
                this,
                session.user!!.image,
                R.drawable.place_holder,
                0,
                binding.includeToolbar.imageViewProfile
            )
        }
        binding.includeToolbar.textViewName.text = session.user!!.first_name

        binding.includeToolbar.imageViewWallet.setOnClickListener {
            openActivity(Constants.WALLET)
        }
        setFragment()
    }


    override fun onResume() {
        super.onResume()
      }
    private fun setFragment() {

        val firstFragment=SendFragment(this)
        val secondFragment=TransactionFragment()
        val thirdFragment=SettingsFragment()
//
        if (intent.extras?.get(Constants.SCREEN_NAME) == Constants.EDIT_PROFILE){
            setCurrentFragment(thirdFragment)
            binding.bottomNavigationView.selectedItemId=R.id.settings
        }else {
            setCurrentFragment(firstFragment)
        }

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