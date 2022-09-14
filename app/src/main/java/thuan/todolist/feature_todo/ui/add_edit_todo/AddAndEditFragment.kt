package thuan.todolist.feature_todo.ui.add_edit_todo

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListPopupWindow
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.appbar.MaterialToolbar
import thuan.todolist.databinding.FragmentAddEditBinding
import thuan.todolist.di.Injection
import thuan.todolist.feature_todo.domain.model.GroupToDo
import thuan.todolist.feature_todo.ui.add_edit_todo.components.DialogAddGroup
import thuan.todolist.feature_todo.ui.add_edit_todo.components.DialogQuitWithOutSaving
import thuan.todolist.feature_todo.ui.time_date_picker.DateAndTimePickerBottomSheet

const val TAG = "AddAndEditFragment"

/**
 * ChildFragmentManager and parentFragmentManager
 *  - ChildFragmentManager: Fragment in Fragment
 *  - ParentFragmentManager: Fragment in Activity
 *  - FragmentManager: Fragment in Fragment or Activity
 *  - SupportFragmentManager: Fragment in Fragment or Activity
 */

class AddAndEditFragment : Fragment() {
    private lateinit var binding: FragmentAddEditBinding
    private lateinit var viewModel: AddEditToDoViewModel
    private lateinit var listPopUpGroup: ListPopupWindow
    private var listGroup: List<String> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddEditBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViewModel()
        setupToolbar()
        setupListPopUpGroup()
        setDefaultData()
        onUIClick()
    }

    private fun setupViewModel() {
        val toDoRepositoryImpl = Injection.provideToDoRepository(requireContext())
        val viewModelFactory = AddEditToDoViewModelFactory.getInstance(
            toDoRepositoryImpl,
            AddAndEditFragmentArgs.fromBundle(requireArguments()).todo
        )
        viewModel = ViewModelProvider(this, viewModelFactory)[AddEditToDoViewModel::class.java]
    }

    private fun onUIClick() {
        binding.apply {
            etTitle.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    viewModel.onEvent(AddEditToDoEvent.EnteredTitle(s.toString()))
                }
            })

            etDescription.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(
                    s: CharSequence?,
                    start: Int,
                    count: Int,
                    after: Int
                ) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                }

                override fun afterTextChanged(s: Editable?) {
                    if (s.toString().isNotEmpty()) {
                        viewModel.onEvent(AddEditToDoEvent.EnteredDescription(s.toString()))
                    }
                }
            })

            tvGroup.setOnClickListener {
                listPopUpGroup.show()
            }
            tvTimeAndDate.setOnClickListener {
                DateAndTimePickerBottomSheet {
                    tvTimeAndDate.text = it
                    viewModel.onEvent(AddEditToDoEvent.EnteredDateAndTime(it))
                }.show(
                    childFragmentManager,
                    "DateAndTimePickerBottomSheet"
                )
                Log.i(TAG, "onUIClick: tvTimeAndDate")
            }

            btnAddGroup.setOnClickListener {
                DialogAddGroup.show(requireContext()) { groupName ->
                    viewModel.insertGroup(GroupToDo(name = groupName))
                }
                Log.i(TAG, "onUIClick: btnAddGroup")
            }
        }
    }


    private fun setDefaultData() {
        Log.i(TAG, "setDefaultData: data changed")
        binding.etTitle.setText(viewModel.todoTitle.value)
        binding.etDescription.setText(viewModel.todoDescription.value)
        binding.tvTimeAndDate.text = viewModel.todoDateAndTime.value
        binding.tvGroup.text = viewModel.groupName.value
    }

    private fun setupToolbar() {
        binding.apply {
            toolbar.title = "My To Do"
            binding.toolbar.inflateMenu(thuan.todolist.R.menu.menu_save)
            setOnMenuItemClickListener(toolbar)
            // display back button
            toolbar.setNavigationIcon(thuan.todolist.R.drawable.ic_close)
            setNavigationOnClickListener(toolbar)
        }
    }

    private fun setNavigationOnClickListener(toolbar: MaterialToolbar) {
        toolbar.setNavigationOnClickListener {
            if (viewModel.getCurrentToDo() != AddAndEditFragmentArgs.fromBundle(requireArguments()).todo) {
                DialogQuitWithOutSaving.show(requireContext()) {
                    viewModel.onEvent(
                        AddEditToDoEvent.SaveToDo(
                            { message ->
                                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                            }, {
                                requireActivity().onBackPressed()
                            })
                    )
                }
            } else {
                requireActivity().onBackPressed()
            }
        }
    }

    private fun setOnMenuItemClickListener(toolbar: MaterialToolbar) {
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                thuan.todolist.R.id.action_save -> {
                    viewModel.onEvent(
                        AddEditToDoEvent.SaveToDo(
                            { message ->
                                Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
                            }, {
                                requireActivity().onBackPressed()
                            })
                    )
                    true
                }
                else -> false
            }
        }
    }

    private fun setupListPopUpGroup() {
        viewModel.getGroupToDo().observe(viewLifecycleOwner) {
            listGroup = it.map { groupToDo -> groupToDo.name }
            if ("Default" !in listGroup) {
                Log.i(TAG, "setupListPopUpGroup:  Default not in listGroup")
                listGroup = listOf("Default") + listGroup
                viewModel.insertGroup(GroupToDo(name = "Default"))
            }

            listPopUpGroup = ListPopupWindow(requireContext())
            listPopUpGroup.setAdapter(
                ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_list_item_1,
                    listGroup
                )
            )
            listPopUpGroup.anchorView = binding.tvGroup
            listPopUpGroup.setOnItemClickListener { _, _, position, _ ->
                binding.tvGroup.text = listGroup[position]
                viewModel.onEvent(AddEditToDoEvent.EnteredGroupName(listGroup[position]))
                listPopUpGroup.dismiss()
            }
        }
    }
}