package com.example.spa.base.listener

enum class BankDetailScreen{
    ADD_BANK_DETAIL,BANK_DETAIL
}

interface IsolatedListener {
    fun replaceFragment(screen: BankDetailScreen, value:String? = "")
    fun goBack()
}

/*
interface SsoResponseListner{
    fun SsoResponse(response: SsoResponse)
}*/
