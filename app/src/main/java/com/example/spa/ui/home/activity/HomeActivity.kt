package com.example.spa.ui.home.activity

import android.app.Dialog
import android.content.Intent
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.example.spa.App
import com.example.spa.R
import com.example.spa.base.listener.Listener
import com.example.spa.base.listener.Screen
import com.example.spa.data.request.VersionRequest
import com.example.spa.databinding.ActivityHomeBinding
import com.example.spa.ui.home.fragment.SendFragment
import com.example.spa.ui.home.fragment.SettingsFragment
import com.example.spa.ui.home.fragment.TransactionFragment
import com.example.spa.utilities.Constants
import com.example.spa.utilities.Resource
import com.example.spa.utilities.core.Session
import com.example.spa.utilities.hideView
import com.example.spa.utilities.showMessage
import com.example.spa.viewmodel.AuthViewModel
import com.squareup.picasso.Picasso

import dagger.hilt.android.AndroidEntryPoint
import java.util.*
//import kotlinx.android.synthetic.main.activity_home.*
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() , Listener {

    @Inject
    lateinit var session: Session
    private lateinit var binding: ActivityHomeBinding
    private val authViewModel: AuthViewModel by viewModels()
    private lateinit var update: Button
    private lateinit var skip: Button
    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        if (newConfig.locale != Locale.getDefault()) { recreate() }
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        session = Session(this)

        if ((this.application as App).session.user!!.image != null && session.user!!.image.isNotEmpty()) {
            Log.e("TAG", "onCreate: ${session.user!!.image}", )
//            GlideUtils.loadImage(
//                this,
//                session.user!!.image,
//                R.drawable.place_holder,
//                0,
//                binding.includeToolbar.imageViewProfile
//            )


//            Glide.with(this)
//                .load(session.user!!.image)
//                .placeholder(R.drawable.place_holder)
//                .into(binding.includeToolbar.imageViewProfile)

            Picasso.with(this)
                .load((this.application as App).session.user!!.image)
                .placeholder(R.drawable.place_holder)
                .into(binding.includeToolbar.imageViewProfile);
        }

        binding.includeToolbar.textViewName.text = session.user!!.first_name

        binding.includeToolbar.imageViewWallet.setOnClickListener {
            openActivity(Constants.WALLET)
        }
        setFragment()
        versionApi()


    }
    private fun versionApi()
    {

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

        binding.bottomNavigationView.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.send->{setCurrentFragment(firstFragment)
                    binding.includeToolbar.imageViewWallet.visibility = View.VISIBLE}
                R.id.transaction->{setCurrentFragment(secondFragment)
                    binding.includeToolbar.imageViewWallet.visibility = View.VISIBLE}
                R.id.settings->{setCurrentFragment(thirdFragment)
                        binding.includeToolbar.imageViewWallet.visibility = View.INVISIBLE
                   }
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
            replace(R.id.flFragment, fragment)
            addToBackStack(null)
            commit()

    }

    override fun replaceFragment(screen: Screen, value: String?) {

    }

    override fun goBack() {
    }

    override fun onBackPressed() {
        super.onBackPressed()
        finishAffinity()
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
        }
        update.setOnClickListener {
            dialog.dismiss()
        }
        dialog.show();
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

                                }
                            }
                        }
                    }
                }
            }
        }
    }
}