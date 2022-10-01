package thuan.todolist.feature_todo.domain.service.utils

import android.content.Context
import android.content.res.Configuration
import android.util.Log
import androidx.preference.PreferenceManager
import thuan.todolist.feature_setting.constants.APP_LANGUAGE
import thuan.todolist.language.LanguageType
import java.util.*

object NotificationUtils {
    @Suppress("DEPRECATION")
    fun getStringByLocal(context: Context, stringId: Int): String {
        val locale =
            PreferenceManager.getDefaultSharedPreferences(context).getString(APP_LANGUAGE, "en")
        Log.i("NotificationUtils", "getStringByLocal:  $locale")
        val configuration = Configuration(context.resources.configuration)
        when (locale) {
            LanguageType.VIETNAM.language -> configuration.setLocale(Locale("vi", "VN"))
            LanguageType.ENGLISH.language -> configuration.setLocale(Locale("en", "US"))
        }

        val dm = context.resources.displayMetrics
        context.resources.updateConfiguration(configuration, dm)
        return context.resources.getString(stringId)
    }
}