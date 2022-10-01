package thuan.todolist

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.preference.PreferenceManager
import thuan.todolist.databinding.ActivityMainBinding
import thuan.todolist.feature_setting.constants.APP_LANGUAGE
import thuan.todolist.feature_setting.constants.APP_THEME_MODE
import thuan.todolist.language.LanguageUtil
import java.util.*


class MainActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun attachBaseContext(newBase: Context) {
        val isNightMode = PreferenceManager.getDefaultSharedPreferences(newBase)
            .getString(APP_THEME_MODE, "light")
        if (AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_YES && isNightMode == "dark") {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else if (AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_NO && isNightMode == "light") {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }

        super.attachBaseContext(
            LanguageUtil.attachBaseContext(
                newBase,
                PreferenceManager.getDefaultSharedPreferences(newBase)
                    .getString(APP_LANGUAGE, "en")!!
            )
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("MainActivity", "onCreate")
        WindowCompat.setDecorFitsSystemWindows(window, false)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navController =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main)!!
                .findNavController()
        appBarConfiguration = AppBarConfiguration(navController.graph)
        observerSettings()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    private fun observerSettings() {
        PreferenceManager.getDefaultSharedPreferences(this)
            .registerOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == APP_THEME_MODE) {
            val isNightMode = sharedPreferences?.getString(APP_THEME_MODE, "light")
            Log.i("MainActivity", "onSharedPreferenceChanged: $isNightMode")
            Log.i("MainActivity", "onSharedPreferenceChanged: ${Locale.getDefault().language}")
            if (isNightMode == "dark" && (AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_YES)) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else if (isNightMode == "light" && (AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_NO)) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        } else if (key == APP_LANGUAGE) {
            val language = sharedPreferences?.getString(APP_LANGUAGE, "en")
            Log.i("MainActivity", "onSharedPreferenceChanged: $language")
            LanguageUtil.changeAppLanguage(applicationContext, language!!)
            Log.i("MainActivity", "onSharedPreferenceChanged: ${Locale.getDefault().language}")
            recreate()
        }
    }

    override fun onDestroy() {
        Log.i("MainActivity", "onDestroy")
        PreferenceManager.getDefaultSharedPreferences(this)
            .unregisterOnSharedPreferenceChangeListener(this)
        super.onDestroy()
    }
}