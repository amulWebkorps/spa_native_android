package com.example.spa.ui.auth.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import com.example.spa.R
import com.example.spa.base.listener.Screen
import com.example.spa.base.listener.TutorialListener
import com.example.spa.base.listener.TutorialScreen
import com.example.spa.databinding.ActivityTutorialBinding
import com.example.spa.ui.auth.fragment.SelectLanguageFragment
import com.example.spa.ui.auth.fragment.TutorialMainFragment
import com.example.spa.utilities.Constants
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TutorialActivity : AppCompatActivity(),TutorialListener {


    private lateinit var binding: ActivityTutorialBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tutorial)
        binding = ActivityTutorialBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportFragmentManager.beginTransaction().add(R.id.placeHolder, MyTipsFragment()).commit()
    }

    override fun replaceFragment(screen: TutorialScreen, value: String?) {

        val fragment = when (screen) {
            TutorialScreen.TUTORIAL -> TutorialMainFragment()
            TutorialScreen.MY_TIPS -> MyTipsFragment()
            TutorialScreen.SELECT_LANGUAGE -> SelectLanguageFragment()
        }

        val args = Bundle()
        args.putString(Constants.AUTH, value)
        fragment.arguments = args
        val fm=supportFragmentManager.beginTransaction()
        fm.replace(R.id.placeHolder, fragment)
            .addToBackStack(null)
        val bsc = supportFragmentManager.backStackEntryCount
        supportFragmentManager.popBackStack()
        fm.commit()
    }

    override fun goBack() {
        val fm: FragmentManager = this.supportFragmentManager
        fm.popBackStack()
    }
}