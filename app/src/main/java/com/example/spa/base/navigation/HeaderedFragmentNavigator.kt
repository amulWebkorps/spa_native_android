package com.example.spa.base.navigation

import android.view.View

interface HeaderedFragmentNavigator : FragmentNavigationListener {
   // fun setOnMultipleSelectionListener(selectionHandler: OnMultipleSelectionHandler?)
   // fun setOnFilterIconSelection(selectionHandler: FilterDialogBox?) //  show the dialog in EntityListBaseFragment


    fun setHeaderText(string: String?)
 //   fun setEnableControls(enable: Boolean)
    fun setHeaderConfiguration(configuration: Int, clickListener: View.OnClickListener?)
 //   fun setAuditProgress(text: String?)
 //   fun setHeaderVisibility(visibility: Boolean)
 //   fun dismissLoader()
 //    fun setRightBtnEnabled(enabled: Boolean)
 //   val header: HeaderMenu?
}