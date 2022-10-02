package thuan.todolist.feature_todo.domain.service.utils

import android.content.Context
import android.util.Log
import androidx.preference.PreferenceManager
import thuan.todolist.feature_setting.constants.APP_LANGUAGE
import thuan.todolist.language.LanguageUtil

object NotificationUtils {
    fun getStringByLocal(context: Context, stringId: Int): String {
        val locale =
            PreferenceManager.getDefaultSharedPreferences(context).getString(APP_LANGUAGE, "en")
        Log.i("NotificationUtils", "getStringByLocal:  $locale")
        val contextConfig = LanguageUtil.attachBaseContext(context, locale!!)
        return contextConfig.resources.getString(stringId)
    }
}