package com.example.mytips.base.listener

import android.os.Bundle

enum class Screen {
    LOGIN ,SIGN_UP,VERIFICATION,FORGOT_PASSWORD,RESET_PASSWORD
}

interface Listener {
    fun replaceFragment(screen: Screen, value:String? = "")
    fun goBack()
}

/*
interface SsoResponseListner{
    fun SsoResponse(response: SsoResponse)
}*/
