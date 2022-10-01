package thuan.todolist.feature_setting.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.preference.PreferenceFragmentCompat
import com.google.android.material.appbar.MaterialToolbar

class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(thuan.todolist.R.xml.todo_preference, rootKey)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        val toolbar = LayoutInflater.from(context).inflate(
            thuan.todolist.R.layout.layout_toolbar,
            view as ViewGroup, false
        ) as MaterialToolbar
        toolbar.title = resources.getString(thuan.todolist.R.string.settings)

        toolbar.setNavigationIcon(thuan.todolist.R.drawable.ic_arrow_back)
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
    }


}