/**
 *
 */
package com.example.spa.base.navigation

import android.os.Bundle
import androidx.fragment.app.Fragment

interface FragmentNavigationListener {
   // fun <B : Fragment?> navigateTo(fragmentClass: Class<B>?, addToBackstack: Boolean, addOnTop: Boolean, removeCurrentFragment: Boolean, bundle: Bundle?)
    fun <B : Fragment?> replaceLastFragment(fragmentClass: Class<B>?, bundle: Bundle?)
    fun <B : Fragment?> replaceFragment(fragmentClass: Class<B>?, bundle: Bundle?)
    fun navigateBack(bundle: Bundle?)
    fun onFragmentDetached(clazz: Class<*>?)
    fun setHeaderMenuVisibility(visibility: Boolean)
    fun setFooterMenuVisibility(visibility: Boolean)
    fun logoutUser()
    fun handleBackPress()
    fun onBackPressed()
    val currentFragment: Fragment?
}