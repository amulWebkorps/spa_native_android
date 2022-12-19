package com.example.spa.utilities

import android.content.Context
import android.content.SharedPreferences
import android.preference.PreferenceManager
import javax.inject.Inject

class Preferences @Inject
constructor(context: Context) {
    private val sharedPreferences: SharedPreferences =PreferenceManager.getDefaultSharedPreferences(context)
    private fun setString(key: String,value:String){
          with(sharedPreferences.edit()){
              putString(key, value)
              commit()
          }
    }
     fun setBoolean(key: String,value:Int){
        with(sharedPreferences.edit()){
            putInt(key, value)
            commit()
        }
    }
     fun setInt(key: String,value:Boolean){
        with(sharedPreferences.edit()){
            putBoolean(key, value)
            commit()
        }
    }
     fun remove(key: String) {
         with(sharedPreferences.edit()){
             remove(key)
             commit()
         }
    }
    private fun getBoolean(key:String):Boolean{
      return sharedPreferences.getBoolean(key,false)?:false
    }
    private fun getInt(key:String):Int{
        return sharedPreferences.getInt(key,0)
    }


}