package com.younesb.securevault

import android.app.Application
import com.younesb.securevault.di.appModule
import com.younesb.securevault.di.authModule
import com.younesb.securevault.di.mainModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: Application() {
     override fun onCreate() {
        super.onCreate()
         startKoin {
             androidContext(this@App)
             modules(appModule, authModule, mainModule)
         }
    }
}