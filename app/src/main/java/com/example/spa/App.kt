package com.example.spa

import android.app.Activity
import android.app.Application
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ProcessLifecycleOwner
import com.example.spa.data.remote.AuthApi
import com.example.spa.data.response.GetUser
import com.example.spa.ui.auth.activity.AuthActivity
import com.example.spa.utilities.Constants
import com.example.spa.utilities.core.Session
import dagger.hilt.android.HiltAndroidApp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Inject


@HiltAndroidApp
class App:Application()
{
    @Inject
    lateinit var session: Session



    override fun onCreate() {
        super.onCreate()
        session = Session(this)

        ProcessLifecycleOwner.get().lifecycle.addObserver(object : LifecycleEventObserver {


            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if (event == Lifecycle.Event.ON_PAUSE) {
                    Log.e("Lifecycle", "Background")
                } else if (event == Lifecycle.Event.ON_RESUME) {
                    CoroutineScope(Dispatchers.IO).launch {
                        checkSessionUser()
                    }

                }
            }
        })
    }

    private suspend fun checkSessionUser() {
        val retrofit = Retrofit.Builder()
            .baseUrl(Url.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(AuthApi::class.java)

            try {
                val call: Response<GetUser> = retrofit.getUser(
                    session.token
                )
                if (call.isSuccessful) {
                    Log.e("TAG", "upload: ${call.body()}")
                    session.user = call.body()!!
                 }
                else if (call.code() == 401){
                    if (session.isLogin) {
                        Log.e("TAG", "checkSessionUser: ")
                        session.editor.clear()
                        val intent = Intent(this, AuthActivity::class.java)
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        val bundle = Bundle()
                        bundle.putBoolean(Constants.SESSION_EXPIRE, true)
                        intent.putExtras(bundle)
                        this.startActivity(intent)
                        (this as Activity).finish()
                    }
                }
                else {
                   }
            } catch (e: Exception) {
                Log.e("TAG", "upload: ${e}")
            }

    }

}