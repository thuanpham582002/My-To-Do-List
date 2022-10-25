package com.example.todoapp_cleanarch_koin.ui.notification.utils

import android.content.Context
import android.util.Log
import androidx.preference.PreferenceManager
import com.example.todoapp_cleanarch_koin.ui.setting.constants.APP_LANGUAGE

object NotificationUtils {
    fun getStringByLocal(context: Context, stringId: Int): String {
        val locale =
            PreferenceManager.getDefaultSharedPreferences(context).getString(APP_LANGUAGE, "en")
        Log.i("NotificationUtils", "getStringByLocal:  $locale")
        val contextConfig = LanguageUtil.attachBaseContext(context, locale!!)
        return contextConfig.resources.getString(stringId)
    }
}