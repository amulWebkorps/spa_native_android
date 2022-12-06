package com.example.mytips.base.navigation

import android.os.Bundle

interface SlidingFragmentNavigator : HeaderedFragmentNavigator {
    fun toggleSlidingMenu()
    fun refreshSlidingMenu()
    fun toggleSecondaryMenu()
    fun closeMenu()
    fun openMenu()
    fun setSlidingMenuEnabled(enable: Boolean)
    fun setListForStatus(status: Bundle?)
    /* fun selectMenuItem(action: String?, view: View?, entityObjectId: String?, needToUpload: Boolean, isMainMenu: Boolean)*/
    fun processBackPressed()
    fun processBackPressedSilently()

    /* fun <K> onMultipleItemSelected(arrayList: List<K>?)
     fun updateAvailableStorage()
     fun callDocumentUploadWS(doc_type: String?, filePath: String?, name: String?, entityType: String?, entityId: Int?)
     fun setUserName(name: String?)
     fun popBackStack()
     fun openScanner(scantype: Int)
     fun requestPermission(permission: String?)*/
}