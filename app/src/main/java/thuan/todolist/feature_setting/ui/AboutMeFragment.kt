package thuan.todolist.feature_setting.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import thuan.todolist.databinding.FragmentAboutMeBinding

class AboutMeFragment : Fragment() {
    private lateinit var binding: FragmentAboutMeBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAboutMeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUI()
        onUIClick()
    }

    private fun setUpUI() {
        binding.toolbar.toolbar.setNavigationIcon(thuan.todolist.R.drawable.ic_arrow_back)
    }

    private fun onUIClick() {
        binding.toolbar.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
    }
}