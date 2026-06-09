package com.younesb.securevault.core.domain.models.preferences

import android.content.Context
import com.younesb.securevault.R
import com.younesb.securevault.core.domain.utils.LocaleUtils

enum class Language(
    val tag: String,
    val label: Int,
) {
    SYSTEM("system", R.string.follow_system),
    ENGLISH("en", R.string.english),
    FRENCH("fr", R.string.french),
    ARABIC("ar", R.string.arabic),
    ;

    /**
     * Get the language name in its native form (e.g., "Français" for French)
     *
     * @param context The context to use for resource resolution
     * @return The language name in its native script
     */
    fun getLocalizedName(context: Context): String? =
        when (this) {
            SYSTEM -> null
            else -> LocaleUtils.getStringInLanguage(context, label, tag)
        }

    companion object {
        fun fromString(value: String): Language = entries.find { it.name.equals(value, ignoreCase = true) } ?: SYSTEM
    }
}
