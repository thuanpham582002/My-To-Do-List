package com.example.todoapp_cleanarch_koin.ui.setting.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.todoapp_cleanarch_koin.R
import com.example.todoapp_cleanarch_koin.databinding.FragmentAboutMeBinding

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
        binding.toolbar.toolbar.setNavigationIcon(R.drawable.ic_arrow_back)
    }

    private fun onUIClick() {
        binding.toolbar.toolbar.setNavigationOnClickListener {
            findNavController().popBackStack()
        }
        binding.tvFbAboutMe.setOnClickListener {
            openFacebookIntent(binding.tvFbAboutMe.text.split("/")[3])
        }

        binding.tvFbAboutProptit.setOnClickListener {
            openFacebookIntent(binding.tvFbAboutProptit.text.split("/")[3])
        }
    }

    private fun openFacebookIntent(fbID: String) {
        Intent(
            Intent.ACTION_VIEW,
            Uri.parse("https://www.facebook.com/$fbID")
        ).apply {
            startActivity(this)
        }
    }
}