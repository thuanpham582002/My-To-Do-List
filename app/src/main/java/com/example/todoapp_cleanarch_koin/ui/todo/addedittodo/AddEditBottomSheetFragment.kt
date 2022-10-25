package com.example.todoapp_cleanarch_koin.ui.todo.addedittodo

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.lifecycle.lifecycleScope
import com.example.todoapp_cleanarch_koin.R
import com.example.todoapp_cleanarch_koin.databinding.BottomsheetAddEditTodoBinding
import com.example.todoapp_cleanarch_koin.ui.todo.addedittodo.components.PopupChooseColor
import com.example.todoapp_cleanarch_koin.ui.todo.util.toFormattedString
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*


class AddEditBottomSheetFragment : BottomSheetDialogFragment() {
    private val binding by lazy { BottomsheetAddEditTodoBinding.inflate(layoutInflater) }
    private val addEditViewModel: AddEditViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        setStyle(STYLE_NORMAL, R.style.DialogStyle)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding.etTodo.requestFocus()
        return binding.root
    }

    @SuppressLint("InflateParams")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeState()
        onUIClick()
    }

    private fun observeState() {
        lifecycleScope.launchWhenStarted {
            addEditViewModel.stateFlow.collectLatest { state ->
                binding.etTodo.setText(state.toDo.title)
                if (state.toDo.dateAndTime != null) {
                    binding.tvSelectedDate.text =
                        state.toDo.dateAndTime.toFormattedString(requireContext())
                    binding.llSelectedDate.visibility = View.VISIBLE
                    binding.llChooseNearDate.visibility = View.GONE
                } else {
                    binding.llSelectedDate.visibility = View.GONE
                    binding.llChooseNearDate.visibility = View.VISIBLE
                }
            }
        }
        lifecycleScope.launchWhenStarted {
            addEditViewModel.eventFlow.collectLatest { event ->
                when (event) {
                    is AddEditViewModel.UiEvent.ChangeUi -> {
                        dismiss()
                    }
                    is AddEditViewModel.UiEvent.Message -> {
                        showMessages(resources.getString(event.idMessage))
                    }
                }
            }
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables", "ResourceType")
    private fun onUIClick() {
        binding.apply {
            btnChooseTag.setOnClickListener {
                PopupChooseColor.Builder(requireContext())
                    .setAnchorView(btnChooseTag)
                    .setCurrentColor(addEditViewModel.stateFlow.value.toDo.color)
                    .build()
                    .setOnColorSelectedListener { color ->
                        addEditViewModel.onEvent(AddEditEvent.EnteredColor(color))


                        // mutate() is needed to avoid the drawable being shared between views
                        resources.getDrawable(R.drawable.ic_tag, null).apply {
                            mutate()
                            if (color != null) {
                                setTint(resources.getColor(color))
                            }
                            btnChooseTag.setImageDrawable(this)
                        }
                    }
                    .setOutSideTouchable(true)
                    .show()
            }
            btnAddTodo.setOnClickListener {
                addEditViewModel.onEvent(AddEditEvent.SaveToDo)
            }

            etTodo.doAfterTextChanged { text ->
                addEditViewModel.onEvent(AddEditEvent.EnteredTitle(text.toString()))
            }

            btnChooseDate.setOnClickListener {
                DatePickerDialog(
                    requireContext(),
                    { _, year, month, dayOfMonth ->
                        addEditViewModel.onEvent(
                            AddEditEvent.EnteredDate( // todo
                                Calendar.getInstance().apply {
                                    set(Calendar.YEAR, year)
                                    set(Calendar.MONTH, month)
                                    set(Calendar.DAY_OF_MONTH, dayOfMonth)
                                }.time
                            )
                        )
                    },
                    Calendar.getInstance().get(Calendar.YEAR),
                    Calendar.getInstance().get(Calendar.MONTH),
                    Calendar.getInstance().get(
                        Calendar.DAY_OF_MONTH
                    )
                ).show()
            }
            btnClearDate.setOnClickListener {
                addEditViewModel.onEvent(AddEditEvent.EnteredDate(null))
            }

            btnToday.setOnClickListener {
                addEditViewModel.onEvent(AddEditEvent.EnteredDate(Calendar.getInstance().time))
            }

            btnTomorrow.setOnClickListener {
                addEditViewModel.onEvent(
                    AddEditEvent.EnteredDate(
                        Calendar.getInstance().apply {
                            add(Calendar.DAY_OF_MONTH, 1)
                        }.time
                    )
                )
            }
        }
    }

    private fun showMessages(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}