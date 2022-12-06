package com.example.mytips.utilities

import androidx.fragment.app.Fragment
import com.example.mytips.base.listener.Screen

interface Communicator  {
    fun replaceFragment(fragment: Fragment)
}