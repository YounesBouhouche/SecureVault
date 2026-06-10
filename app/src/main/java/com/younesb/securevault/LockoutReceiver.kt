package com.younesb.securevault

import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.younesb.securevault.core.data.util.AuthManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.java.KoinJavaComponent.inject

class LockoutReceiver: BroadcastReceiver() {
    private val authManager by inject<AuthManager>(AuthManager::class.java)

    override fun onReceive(p0: Context?, p1: Intent?) {
        CoroutineScope(Dispatchers.IO).launch {
            authManager.unlock()
        }
    }

    companion object {
        private const val UNIQUE_ID = 0
        fun pendingIntent(context: Context): PendingIntent {
            return PendingIntent.getBroadcast(
                context,
                UNIQUE_ID,
                Intent(context, LockoutReceiver::class.java),
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            )
        }
    }
}