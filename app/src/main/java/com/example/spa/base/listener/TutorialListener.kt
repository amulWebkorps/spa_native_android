package com.example.spa.base.listener

enum class TutorialScreen {
    TUTORIAL,MY_TIPS,SELECT_LANGUAGE
}

interface TutorialListener {
    fun replaceFragment(screen: TutorialScreen, value:String? = "")
    fun goBack()
}

