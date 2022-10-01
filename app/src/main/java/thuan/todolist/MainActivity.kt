package thuan.todolist

import android.content.SharedPreferences
import android.content.res.Resources
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


class MainActivity : AppCompatActivity(), SharedPreferences.OnSharedPreferenceChangeListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding

    companion object {
        lateinit var RESOURCES_INSTANCE: Resources
        lateinit var SP_INSTANCE: SharedPreferences
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.i("MainActivity", "onCreate")
        RESOURCES_INSTANCE = resources
        SP_INSTANCE = PreferenceManager.getDefaultSharedPreferences(this)

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
        val language = SP_INSTANCE.getString(APP_LANGUAGE, "en")
        LanguageUtil.changeAppLanguage(this, language!!)
        val isNightMode = SP_INSTANCE.getString(APP_THEME_MODE, "light")
        if (AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_YES && isNightMode == "dark") {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else if (AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_NO && isNightMode == "light") {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
        PreferenceManager.getDefaultSharedPreferences(this)
            .registerOnSharedPreferenceChangeListener(this)
    }

    override fun onSharedPreferenceChanged(sharedPreferences: SharedPreferences?, key: String?) {
        if (key == APP_THEME_MODE) {
            val isNightMode = sharedPreferences?.getString(APP_THEME_MODE, "light")
            if (AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_YES && isNightMode == "dark") {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else if (AppCompatDelegate.getDefaultNightMode() != AppCompatDelegate.MODE_NIGHT_NO && isNightMode == "light") {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        } else if (key == APP_LANGUAGE) {
            Log.i("MainActivity", "onSharedPreferenceChanged: todo_language")
            val language = sharedPreferences?.getString(APP_LANGUAGE, "en")
            LanguageUtil.changeAppLanguage(this, language!!)
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