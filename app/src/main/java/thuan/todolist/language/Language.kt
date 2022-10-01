package thuan.todolist.language

import android.annotation.TargetApi
import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.os.LocaleList
import android.text.TextUtils

import java.util.Locale

enum class LanguageType(language: String?) {
    VIETNAM("vi"),
    ENGLISH("en");

    var language: String? = language
        get() {
            return field ?: ""
        }
}

@Suppress("unused", "DEPRECATION")
object LanguageUtil {
    private const val TAG = "LanguageUtil"
    var sharedPreferences: SharedPreferences? = null
    var editor: SharedPreferences.Editor? = null

    fun changeAppLanguage(context: Context, newLanguage: String) {
        if (TextUtils.isEmpty(newLanguage)) {
            return
        }
        val resources = context.resources
        val configuration = resources.configuration

        val locale = getLocaleByLanguage(newLanguage)
        configuration.setLocale(locale)
        // updateConfiguration
        val dm = resources.displayMetrics
        resources.updateConfiguration(configuration, dm)
    }

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
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            updateResources(context, language)
        } else {
            context
        }
    }

    @TargetApi(Build.VERSION_CODES.N)
    private fun updateResources(context: Context, language: String): Context {
        val resources = context.resources
        val locale = getLocaleByLanguage(language)
        val configuration = resources.configuration
        configuration.setLocale(locale)
        configuration.setLocales(LocaleList(locale))
        return context.createConfigurationContext(configuration)
    }
}
