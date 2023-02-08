package com.example.spa.ui.auth.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.spa.R
import com.example.spa.base.BaseFragment
import com.example.spa.base.listener.Screen
import com.example.spa.base.listener.TutorialScreen
import com.example.spa.databinding.FragmentSelectLanguageBinding
import com.example.spa.databinding.FragmentTutorialBinding
import com.example.spa.ui.auth.adapter.LanguageAdapter
import com.example.spa.ui.auth.adapter.TutorialAdapter
import com.example.spa.ui.auth.dataClass.Language
import com.example.spa.ui.auth.dataClass.Tutorial
import com.example.spa.utilities.hideView
import com.example.spa.utilities.setAppLocale
import com.example.spa.utilities.showView
import dagger.hilt.android.AndroidEntryPoint
//import kotlinx.android.synthetic.main.fragment_tutorial.*
//import kotlinx.android.synthetic.main.fragment_tutorial.view.*

@AndroidEntryPoint
class SelectLanguageFragment :  BaseFragment(), LanguageAdapter.OnClick {


    var  adapter:LanguageAdapter? = null
    lateinit var list : ArrayList<Language>

    private lateinit var binding: FragmentSelectLanguageBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSelectLanguageBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.layoutToolBar.imageViewBack.setOnClickListener {
            tutorialListener?.goBack()
        }

        list = ArrayList<Language>()
        list.add(
            Language(
            getString(R.string.english),
            )
        )

        list.add(Language(
            getString(R.string.french),
        ))

        adapter = LanguageAdapter(this,requireContext(),session.language)
        binding.recycleViewLanguage.adapter = adapter
        adapter?.setListItem(list)
    }

    override fun onClick(selectLanguage: String) {

        if (selectLanguage == getString(R.string.french)){
            setAppLocale(requireContext(), "fr")
        }else{
            setAppLocale(requireContext(), "en")
        }
        session.language=selectLanguage
        tutorialListener?.goBack()
    }


}