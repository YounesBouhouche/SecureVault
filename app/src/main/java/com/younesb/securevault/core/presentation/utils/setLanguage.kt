package com.younesb.securevault.core.presentation.utils

import android.app.Activity
import android.app.LocaleManager
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.LocaleManagerCompat
import androidx.core.os.LocaleListCompat
import com.younesb.securevault.core.domain.models.preferences.Language
import kotlin.jvm.java

fun Activity.setLanguage(language: Language) {
    runOnUiThread {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getSystemService(LocaleManager::class.java)
                .applicationLocales =
                LocaleList.forLanguageTags(
                    if (language == Language.SYSTEM) {
                        LocaleManagerCompat.getSystemLocales(this)[0]!!.language
                    } else {
                        language.tag
                    },
                )
        } else {
            AppCompatDelegate.setApplicationLocales(
                LocaleListCompat.forLanguageTags(language.tag),
            )
        }
    }
}
