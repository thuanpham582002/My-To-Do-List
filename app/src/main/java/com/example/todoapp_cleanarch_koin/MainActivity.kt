package com.example.todoapp_cleanarch_koin

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import androidx.core.view.WindowCompat
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.preference.PreferenceManager
import com.example.todoapp_cleanarch_koin.databinding.ActivityMainBinding
import com.example.todoapp_cleanarch_koin.ui.setting.constants.APP_LANGUAGE
import com.example.todoapp_cleanarch_koin.ui.setting.constants.APP_THEME_MODE

class MainActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val navController =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment_content_main)!!
                .findNavController()
        appBarConfiguration = AppBarConfiguration(navController.graph)

        PreferenceManager.getDefaultSharedPreferences(this)
            .registerOnSharedPreferenceChangeListener(this)
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences, key: String?) {
        if (key == APP_THEME_MODE) {
            val isNightMode = sharedPreferences.getString(APP_THEME_MODE, "light")
            if (isNightMode == "dark" && (AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_YES)) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else if (isNightMode == "light" && (AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_NO)) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        } else if (key == APP_LANGUAGE) {
            val language = sharedPreferences.getString(APP_LANGUAGE, "en")
            val locales = LocaleListCompat.forLanguageTags(language)
            AppCompatDelegate.setApplicationLocales(locales)
        }
    }

    override fun onDestroy() {
        PreferenceManager.getDefaultSharedPreferences(this)
            .unregisterOnSharedPreferenceChangeListener(this)
        super.onDestroy()
    }
}