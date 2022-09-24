package thuan.todolist.feature_todo.ui.add_edit_todo

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListPopupWindow
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import thuan.todolist.databinding.FragmentAddEditBinding
import thuan.todolist.di.Injection
import thuan.todolist.feature_todo.domain.service.toDoScheduleNotification
import thuan.todolist.feature_todo.ui.add_edit_todo.components.DialogAddGroup
import thuan.todolist.feature_todo.ui.add_edit_todo.components.DialogQuitWithOutSaving
import thuan.todolist.feature_todo.ui.add_edit_todo.components.time_date_picker.DateAndTimePickerBottomSheet

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
        setData()
        onUIClick()
        subscribeToObservers()
    }

    private fun setupViewModel() {
        val toDoRepositoryImpl = Injection.provideToDoRepository(requireContext())
        val viewModelFactory = AddEditToDoViewModelFactory(
            Injection.provideToDoUseCases(toDoRepositoryImpl),
            AddAndEditFragmentArgs.fromBundle(requireArguments()).todo
        )
        viewModel = ViewModelProvider(this, viewModelFactory)[AddEditToDoViewModel::class.java]
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun subscribeToObservers() {
        viewModel.latestState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is AddEditToDoViewModel.UIEvent.ShowSnackBar -> {
                    showSnackBar(state.message)
                    viewModel.latestState.value = AddEditToDoViewModel.UIEvent.None
                }
                is AddEditToDoViewModel.UIEvent.SaveToDoSuccess -> {
                    showSnackBar(state.message)
                    toDoScheduleNotification(
                        requireContext(),
                        viewModel.toDoId.value!!,
                        viewModel.todoTitle.value!!,
                        viewModel.todoDescription.value!!,
                        viewModel.todoDateAndTime.value!!,
                    )
                    requireActivity().onBackPressed()
                }
                AddEditToDoViewModel.UIEvent.None -> {}
            }
        }
        viewModel.isTimeSet.observe(viewLifecycleOwner) { isTimeSet ->
            if (isTimeSet) {
                binding.deleteDateAndTime.visibility = View.VISIBLE
                val img = requireContext().getDrawable(thuan.todolist.R.drawable.ic_timer)
                img?.setBounds(0, 0, 60, 60)
                binding.tvTimeAndDate.setCompoundDrawables(img, null, null, null)
            } else {
                binding.deleteDateAndTime.visibility = View.GONE
                val img = requireContext().getDrawable(thuan.todolist.R.drawable.ic_timer_off)
                img?.setBounds(0, 0, 60, 60)
                binding.tvTimeAndDate.setCompoundDrawables(img, null, null, null)
            }
        }
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
                    deleteDateAndTime.visibility = View.VISIBLE
                    viewModel.isTimeSet.value = true
                    viewModel.onEvent(AddEditToDoEvent.EnteredDateAndTime(it))
                }.show(
                    childFragmentManager,
                    "DateAndTimePickerBottomSheet"
                )
                Log.i(TAG, "onUIClick: tvTimeAndDate")
            }

            deleteDateAndTime.setOnClickListener {
                viewModel.isTimeSet.value = false
                tvTimeAndDate.text = String.format("Time not set")
                viewModel.onEvent(AddEditToDoEvent.EnteredDateAndTime("Time not set"))
                it.visibility = View.GONE
            }

            btnAddGroup.setOnClickListener {
                DialogAddGroup.show(requireContext()) { groupName ->
                    viewModel.onEvent(AddEditToDoEvent.SaveGroup(groupName))
                }
                Log.i(TAG, "onUIClick: btnAddGroup")
            }

            onClickToolBar()
        }
    }


    private fun setData() {
        binding.etTitle.setText(viewModel.todoTitle.value)
        binding.etDescription.setText(viewModel.todoDescription.value)
        binding.tvTimeAndDate.text = viewModel.todoDateAndTime.value
        binding.tvGroup.text = viewModel.groupName.value
    }

    private fun setupToolbar() {
        binding.apply {
            toolbar.title = "My To Do"
            binding.toolbar.inflateMenu(thuan.todolist.R.menu.menu_save)
            // display back button
            toolbar.setNavigationIcon(thuan.todolist.R.drawable.ic_close)
        }
    }

    private fun onClickToolBar() {
        binding.apply {
            toolbar.setNavigationOnClickListener {
                if (viewModel.getCurrentToDo() != AddAndEditFragmentArgs.fromBundle(requireArguments()).todo) {
                    DialogQuitWithOutSaving.show(requireContext()) {
                        viewModel.onEvent(AddEditToDoEvent.SaveToDo)
                    }
                } else {
                    requireActivity().onBackPressed()
                }
            }
            toolbar.setOnMenuItemClickListener {
                when (it.itemId) {
                    thuan.todolist.R.id.action_save -> {
                        viewModel.onEvent(AddEditToDoEvent.SaveToDo)
                        true
                    }
                    else -> false
                }
            }
        }
    }

    private fun setupListPopUpGroup() {
        viewModel.getGroupToDo().observe(viewLifecycleOwner) {
            listGroup = it.map { groupToDo -> groupToDo.name }
            if ("Default" !in listGroup) {
                Log.i(TAG, "setupListPopUpGroup:  Default not in listGroup")
                listGroup = listOf("Default") + listGroup
                viewModel.onEvent(AddEditToDoEvent.SaveGroup("Default"))
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

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }
}