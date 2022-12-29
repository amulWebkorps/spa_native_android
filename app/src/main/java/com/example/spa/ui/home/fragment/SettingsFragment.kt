package com.example.spa.ui.home.fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.spa.R
import com.example.spa.base.BaseFragment
import com.example.spa.base.DialogUtils
import com.example.spa.databinding.FragmentSettingsBinding
import com.example.spa.ui.auth.activity.AuthActivity
import com.example.spa.ui.home.activitiy.IsolatedActivity
import com.example.spa.utilities.Constants

class SettingsFragment:BaseFragment() {

    private lateinit var binding: FragmentSettingsBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSettingsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setView()
        setClick()
    }

    private fun setClick() {
        binding.apply {
            includeProfile.layoutMain.setOnClickListener {
               openActivity(Constants.EDIT_PROFILE)
                requireActivity().finish()
            }
            includeBankDetail.layoutMain.setOnClickListener {
                openActivity(Constants.BANK_DETAIL)
            }
            includeWallet.layoutMain.setOnClickListener {
                openActivity(Constants.WALLET)
            }
            buttonLogout.setOnClickListener {
                    DialogUtils().showGeneralDialog(
                        requireContext(),
                        message = getString(R.string.message_logout),
                        positiveText = getString(R.string.button_no),
                        negativeText = getString(R.string.button_yes),
                        onClick = {},
                        onNoClick = {
                            session.isLogin = false
                            val intent= Intent(requireContext(), AuthActivity::class.java)
                            requireActivity().finish()
                            startActivity(intent)
                        })

            }
        }
    }
    private fun openActivity(fragment: String){
        val intent= Intent(requireContext(), IsolatedActivity::class.java)
        intent.putExtra(Constants.SCREEN_NAME,fragment)
        startActivity(intent)
    }

    private fun setView() {
        binding.apply {
            includeWallet.textViewHead.text = getString(R.string.wallet)
            includeBankDetail.textViewHead.text = getString(R.string.bank_detail)
//            includeLogOut.textViewHead.text = getString(R.string.label_logout)

        }
    }
}