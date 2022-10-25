package com.example.todoapp_cleanarch_koin.ui.notification.utils

import android.content.Context
import android.os.Build
import android.os.LocaleList
import android.util.Log
import java.util.*

enum class LanguageType(language: String?) {
    VIETNAM("vi"),
    ENGLISH("en");

    var language: String? = language
        get() {
            return field ?: ""
        }
}

object LanguageUtil {
    private const val TAG = "LanguageUtil"

    private fun getLocaleByLanguage(language: String): Locale {
        // default English
        var locale = Locale.ENGLISH
        when (language) {
            LanguageType.VIETNAM.language -> locale = Locale("vi", "VN")
            LanguageType.ENGLISH.language -> locale = Locale.ENGLISH
        }
        return locale
    }

    fun attachBaseContext(context: Context, language: String): Context {
        Log.i(TAG, "attachBaseContext: $language")
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateResources(context, language)
        } else {
            context
        }
    }

    private fun updateResources(context: Context, language: String): Context {
        Log.i("attachBaseContext", "attachBaseContext: ")
        val resources = context.resources
        val locale = getLocaleByLanguage(language)
        val configuration = resources.configuration
        configuration.setLocale(locale)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            configuration.setLocales(LocaleList(locale))
        }
        return context.createConfigurationContext(configuration)
    }
}
