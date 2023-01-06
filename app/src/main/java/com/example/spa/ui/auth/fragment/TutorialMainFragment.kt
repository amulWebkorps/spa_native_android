package com.example.spa.ui.auth.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.spa.R
import com.example.spa.base.BaseFragment
import com.example.spa.base.listener.Screen
import com.example.spa.base.listener.TutorialScreen
import com.example.spa.databinding.FragmentTutorialBinding
import com.example.spa.ui.auth.adapter.TutorialAdapter
import com.example.spa.ui.auth.dataClass.Tutorial
import com.example.spa.utilities.hideView
import com.example.spa.utilities.showView
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_tutorial.*
import kotlinx.android.synthetic.main.fragment_tutorial.view.*

@AndroidEntryPoint
class TutorialMainFragment :  BaseFragment() {

    var  adapter:TutorialAdapter? = null
    lateinit var list : ArrayList<Tutorial>

    private lateinit var binding: FragmentTutorialBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentTutorialBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
         if (session.language == ""){
             session.language  =  getString(R.string.english)
         }else {
             binding.textViewLanguage.text = session.language
         }

        list = ArrayList<Tutorial>()
        list.add(Tutorial(
            R.drawable.ic_splash_image , getString(R.string.tutorial_1),
        ))
        list.add(Tutorial(
            R.drawable.rocket , getString(R.string.tutorial_2),
            ))
        list.add(Tutorial(
            R.drawable.ic_splash_image , getString(R.string.tutorial_1),
        ))
        list.add(Tutorial(
            R.drawable.ic_splash_image , getString(R.string.tutorial_1),
        ))
        list.add(Tutorial(
            R.drawable.rocket , getString(R.string.tutorial_2),
        ))
        adapter = TutorialAdapter()
        binding.viewPager.adapter = adapter
        adapter?.setListItem(list)
        binding.dotIndicator.setViewPager2(binding.viewPager)

        binding.textViewLanguage.setOnClickListener {
            tutorialListener?.replaceFragment(TutorialScreen.SELECT_LANGUAGE)
        }
          binding.textViewSkip.setOnClickListener {
              tutorialListener?.replaceFragment(TutorialScreen.MY_TIPS)
          }

                binding.textViewNext.setOnClickListener {
                    if (binding.viewPager.currentItem+1<adapter!!.itemCount){
                    binding.viewPager.currentItem+= 1
                }else{
                        tutorialListener?.replaceFragment(TutorialScreen.MY_TIPS)
                    }

        }
    }

}