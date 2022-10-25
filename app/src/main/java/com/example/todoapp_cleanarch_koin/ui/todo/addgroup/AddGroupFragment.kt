package com.example.todoapp_cleanarch_koin.ui.todo.addgroup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.todoapp_cleanarch_koin.R
import com.example.todoapp_cleanarch_koin.databinding.FragmentAddGroupBinding
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel

class AddGroupFragment : Fragment() {
    val binding: FragmentAddGroupBinding by lazy {
        FragmentAddGroupBinding.inflate(layoutInflater)
    }

    private val addGroupViewModel: AddGroupViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar()
        onUIClick()
        observeState()
    }

    private fun observeState() {
        lifecycleScope.launchWhenStarted {
            addGroupViewModel.stateFlow.collectLatest {
                binding.apply {
                    etGroupName.setText(it.groupTodoEntity.name)
                    when (it.groupTodoEntity.color) {
                        R.color.black -> rgColorTag.check(rbColorBlack.id)
                        R.color.red -> rgColorTag.check(rbColorRed.id)
                        R.color.yellow -> rgColorTag.check(rbColorYellow.id)
                        R.color.green -> rgColorTag.check(rbColorGreen.id)
                        R.color.blue -> rgColorTag.check(rbColorBlue.id)
                        R.color.purple -> rgColorTag.check(rbColorPurple.id)
                        R.color.brown -> rgColorTag.check(rbColorBrown.id)
                        R.color.grey -> rgColorTag.check(rbColorGrey.id)
                        R.color.orange -> rgColorTag.check(rbColorOrange.id)
                        null -> rgColorTag.clearCheck()
                    }
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            addGroupViewModel.eventFlow.collectLatest { event ->
                when (event) {
                    is AddGroupViewModel.UIEvent.ChangeScreen -> TODO()
                    AddGroupViewModel.UIEvent.NavigateBack -> findNavController().popBackStack()
                    is AddGroupViewModel.UIEvent.ShowMessage -> showMassage(
                        resources.getString(
                            event.idMessage
                        )
                    )
                }
            }
        }
    }

    private fun onUIClick() {
        binding.apply {
            fabAddGroup.setOnClickListener {
                addGroupViewModel.onEvent(AddGroupEvent.SaveGroup)
            }
            rgColorTag.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    rbColorBlack.id -> addGroupViewModel.onEvent(AddGroupEvent.SelectColor(R.color.black))
                    rbColorRed.id -> addGroupViewModel.onEvent(AddGroupEvent.SelectColor(R.color.red))
                    rbColorYellow.id -> addGroupViewModel.onEvent(AddGroupEvent.SelectColor(R.color.yellow))
                    rbColorGreen.id -> addGroupViewModel.onEvent(AddGroupEvent.SelectColor(R.color.green))
                    rbColorBlue.id -> addGroupViewModel.onEvent(AddGroupEvent.SelectColor(R.color.blue))
                    rbColorPurple.id -> addGroupViewModel.onEvent(AddGroupEvent.SelectColor(R.color.purple))
                    rbColorBrown.id -> addGroupViewModel.onEvent(AddGroupEvent.SelectColor(R.color.brown))
                    rbColorGrey.id -> addGroupViewModel.onEvent(AddGroupEvent.SelectColor(R.color.grey))
                    rbColorOrange.id -> addGroupViewModel.onEvent(AddGroupEvent.SelectColor(R.color.orange))
                }
            }
            etGroupName.doAfterTextChanged {
                addGroupViewModel.onEvent(AddGroupEvent.EnteredGroupName(it.toString()))
            }
        }
    }

    private fun setUpToolbar() {
        binding.toolbar.toolbar.apply {
            title = getString(R.string.add_group)
            setNavigationIcon(R.drawable.ic_arrow_back)
            setNavigationOnClickListener {
                findNavController().popBackStack()
            }
        }
    }

    private fun showMassage(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }
}