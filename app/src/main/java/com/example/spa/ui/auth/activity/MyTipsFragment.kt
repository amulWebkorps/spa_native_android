package com.example.spa.ui.auth.activity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
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
    private var startX = 0
    private var endX = 0
    private var MIN_SWIPE_DISTANCE = 150 // Minimum distance for a swipe

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
        binding.finalMain.setOnTouchListener { v, event ->
            when (event.action) {
                MotionEvent.ACTION_DOWN -> startX = event.x.toInt()
                MotionEvent.ACTION_UP -> {
                    endX = event.x.toInt()
                    val deltaX = (endX - startX)
                    //Toast.makeText(requireContext(),"xxxxx"+deltaX, Toast.LENGTH_LONG).show()
                    // Check if the swipe distance is greater than the minimum threshold
                    if (Math.abs(deltaX) >= MIN_SWIPE_DISTANCE) {

                        //Toast.makeText(requireContext(),"hope", Toast.LENGTH_LONG).show()
                        if (deltaX > 0) {
                            goToNext()
                            //Toast.makeText(requireContext(),"Right to left swipe", Toast.LENGTH_LONG).show()
                            // Right to left swipe
                            // Handle your action for this swipe direction
                        } else {
                            //Toast.makeText(requireContext(),"Left to right swipe", Toast.LENGTH_LONG).show()
                            // Left to right swipe
                            // Handle your action for this swipe direction
                        }
                    }
                }
            }
            true
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
            getString(R.string.arabic) -> {
                setAppLocale(requireContext(), "ar")
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
            goToNext()

        }
    }

    private fun goToNext() {
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