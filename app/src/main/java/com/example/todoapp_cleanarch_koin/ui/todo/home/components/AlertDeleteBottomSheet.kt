package com.example.todoapp_cleanarch_koin.ui.todo.home.components

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.todoapp_cleanarch_koin.databinding.BottomsheetDeleteAlertBinding
import com.example.todoapp_cleanarch_koin.ui.todo.addedittodo.utils.ActionDeleteToDo
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.example.todoapp_cleanarch_koin.ui.todo.addedittodo.constants.ACTION_DELETE

class AlertDeleteBottomSheet : BottomSheetDialogFragment() {
    private lateinit var binding: BottomsheetDeleteAlertBinding
    private lateinit var actionDeleteToDo: ActionDeleteToDo

    companion object {
        fun newInstance(action_delete: ActionDeleteToDo) =
            AlertDeleteBottomSheet().apply {
                arguments = Bundle().apply {
                    putSerializable(ACTION_DELETE, action_delete)
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = BottomsheetDeleteAlertBinding.inflate(inflater, container, false)
        actionDeleteToDo = arguments?.getSerializable(ACTION_DELETE) as ActionDeleteToDo
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.apply {
            btnDelete.setOnClickListener {
                actionDeleteToDo.deleteToDo()
                this@AlertDeleteBottomSheet.dismiss()
            }
            btnCancel.setOnClickListener {
                this@AlertDeleteBottomSheet.dismiss()
            }
        }
    }
}