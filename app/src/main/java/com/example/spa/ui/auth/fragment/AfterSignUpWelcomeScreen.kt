package com.example.spa.ui.auth.fragment

import android.R
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import com.example.spa.base.BaseFragment
import com.example.spa.databinding.FragmentAfterSignUpScreenBinding
import com.example.spa.ui.home.activity.HomeActivity


class AfterSignUpWelcomeScreen :BaseFragment() {

    private lateinit var binding: FragmentAfterSignUpScreenBinding
    lateinit var value:String
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAfterSignUpScreenBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val animation: Animation =
            AnimationUtils.loadAnimation(context, com.example.spa.R.anim.animation_fade_in)
        binding.textViewSubHead.startAnimation(animation)
        binding.textViewWelcomeToMyTips.startAnimation(animation)


        val animation2: Animation =
            AnimationUtils.loadAnimation(context, com.example.spa.R.anim.animation_fade_in)
        binding.textViewSubHead.startAnimation(animation2)
        binding.textViewWelcomeToMyTips.startAnimation(animation2)
        Handler(Looper.myLooper()!!).postDelayed({
            val intent = Intent(requireContext(), HomeActivity::class.java)
            startActivity(intent)
            requireActivity().finish()
        }, 3000)

    }
}