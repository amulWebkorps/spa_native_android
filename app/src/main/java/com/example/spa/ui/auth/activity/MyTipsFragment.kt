package com.example.spa.ui.auth.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.spa.R
import com.example.spa.base.BaseFragment
import com.example.spa.base.listener.TutorialScreen
import com.example.spa.databinding.FragmentMyTipsBinding
import com.example.spa.ui.home.activity.HomeActivity
import com.example.spa.utilities.setAppLocale

class MyTipsFragment : BaseFragment() {

    private lateinit var binding: FragmentMyTipsBinding
    private lateinit var update: Button
    private lateinit var skip:Button

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentMyTipsBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        handler()

        binding.textViewLanguage.setOnClickListener {
            tutorialListener?.replaceFragment(TutorialScreen.SELECT_LANGUAGE)
        }
        when (session.language) {
            getString(R.string.english) -> {
                setAppLocale(requireContext(), "en")
                session.language  =  getString(R.string.english)
            }
            getString(R.string.french) -> {
                setAppLocale(requireContext(), "fr")
                binding.textViewLanguage.text = session.language
            }
            else -> {
                setAppLocale(requireContext(), "en")
                session.language  =  getString(R.string.english)

            }
        }

    }

    private fun handler(){
        binding.layoutBottom.setOnClickListener {
            session.isNotFirstTime = true
            if (session.isLogin){
                val intent= Intent(requireContext(), HomeActivity::class.java)
                startActivity(intent)
                requireActivity().finishAffinity()
            }
            else {
                val intent = Intent(requireContext(), AuthActivity::class.java)
                startActivity(intent)
                requireActivity().finishAffinity()
            }
        }
    }
}