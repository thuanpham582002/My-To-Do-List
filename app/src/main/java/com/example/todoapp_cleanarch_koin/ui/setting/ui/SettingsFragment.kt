package com.example.todoapp_cleanarch_koin.ui.setting.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import com.example.todoapp_cleanarch_koin.R
import com.google.android.material.appbar.MaterialToolbar
import java.util.*

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.todo_preference, rootKey)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        val toolbar = LayoutInflater.from(context).inflate(
            R.layout.layout_toolbar,
            view as ViewGroup, false
        ) as MaterialToolbar
        toolbar.title = resources.getString(R.string.settings)

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
        toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
            /**
             * different from findNavController().navigateUp() vs findNavController().popBackStack()
             * navigateUp() will navigate to the previous fragment
             * popBackStack() will pop the current fragment
             * navigateUp() didn't pop fragment out of the backstack but popBackStack() did
             * https://stackoverflow.com/questions/67245601/difference-between-navigateup-and-popbackstack
             */
        }
        view.addView(toolbar, 0)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // inflate toolbar
        this.findPreference<Preference>("todo_about_me")?.setOnPreferenceClickListener {
            findNavController().navigate(R.id.action_settingsFragment_to_aboutMeFragment)
            true
        }
        Log.i("SettingsFragment", "onViewCreated: ${Locale.getDefault().language}")
    }

}