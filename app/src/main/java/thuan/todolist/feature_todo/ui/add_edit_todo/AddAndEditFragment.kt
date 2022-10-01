package thuan.todolist.feature_todo.ui.add_edit_todo

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ListPopupWindow
import androidx.activity.OnBackPressedCallback
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.material.snackbar.Snackbar
import thuan.todolist.databinding.FragmentAddEditBinding
import thuan.todolist.di.Injection
import thuan.todolist.feature_todo.domain.service.toDoScheduleNotification
import thuan.todolist.feature_todo.domain.util.ToDoUtils
import thuan.todolist.feature_todo.ui.add_edit_todo.components.AlertDeleteBottomSheet
import thuan.todolist.feature_todo.ui.add_edit_todo.components.DateAndTimePickerBottomSheet
import thuan.todolist.feature_todo.ui.add_edit_todo.components.DialogAddGroup
import thuan.todolist.feature_todo.ui.add_edit_todo.components.DialogQuitWithOutSaving
import thuan.todolist.feature_todo.ui.add_edit_todo.utils.ActionDeleteToDo
import thuan.todolist.feature_todo.ui.add_edit_todo.utils.ActionSetTime
import java.util.*

const val TAG = "AddAndEditFragment"

/**
 * ChildFragmentManager and parentFragmentManager
 *  - ChildFragmentManager: Fragment in Fragment
 *  - ParentFragmentManager: Fragment in Activity
 *  - FragmentManager: Fragment in Fragment or Activity
 *  - SupportFragmentManager: Fragment in Fragment or Activity
 */

class AddAndEditFragment : Fragment(), ActionDeleteToDo, ActionSetTime {
    private lateinit var binding: FragmentAddEditBinding
    private lateinit var viewModel: AddEditToDoViewModel
    private lateinit var listPopUpGroup: ListPopupWindow
    private lateinit var alertDeleteBottomSheet: AlertDeleteBottomSheet
    private lateinit var dateAndTimePickerBottomSheet: DateAndTimePickerBottomSheet
    private var listGroup: List<String> = listOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddEditBinding.inflate(inflater, container, false)
        setupViewModel()
        alertDeleteBottomSheet = AlertDeleteBottomSheet.newInstance(this)
        dateAndTimePickerBottomSheet = DateAndTimePickerBottomSheet.newInstance(this)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
        // Ger event from intent
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
                        viewModel.getCurrentToDo()
                    )
                    findNavController().popBackStack()
                }
                AddEditToDoViewModel.UIEvent.None -> {}
                is AddEditToDoViewModel.UIEvent.DeleteToDoSuccess -> {
                    showSnackBar(state.message)
                    findNavController().popBackStack()
                }
            }
        }
        viewModel.isTimeSet.observe(viewLifecycleOwner) { isTimeSet ->
            if (isTimeSet) {
                binding.deleteDateAndTime.visibility = View.VISIBLE
                val img = requireContext().getDrawable(thuan.todolist.R.drawable.ic_timer)
                binding.tvTimeAndDate.icon = img
            } else {
                binding.deleteDateAndTime.visibility = View.GONE
                val img = requireContext().getDrawable(thuan.todolist.R.drawable.ic_timer_off)
                binding.tvTimeAndDate.icon = img
            }
        }

        viewModel.todoDateAndTime.observe(viewLifecycleOwner) {
            binding.tvTimeAndDate.text = ToDoUtils.dateToString(requireContext(), it)
        }
    }

    private fun onUIClick() {
        binding.apply {
            etTitle.doAfterTextChanged { s ->
                viewModel.onEvent(AddEditToDoEvent.EnteredTitle(s.toString()))
            }
            etDescription.doAfterTextChanged { s ->
                viewModel.onEvent(AddEditToDoEvent.EnteredDescription(s.toString()))
            }

            tvGroup.setOnClickListener {
                listPopUpGroup.show()
            }

            tvTimeAndDate.setOnClickListener {
                if (dateAndTimePickerBottomSheet.isAdded) {
                    dateAndTimePickerBottomSheet.dismiss()
                } else {
                    dateAndTimePickerBottomSheet.show(
                        childFragmentManager,
                        "DateAndTimePickerBottomSheet"
                    )
                }
                Log.i(TAG, "onUIClick: tvTimeAndDate")
            }

            deleteDateAndTime.setOnClickListener {
                viewModel.isTimeSet.value = false
                tvTimeAndDate.text = resources.getString(thuan.todolist.R.string.time_not_set)
                viewModel.onEvent(AddEditToDoEvent.EnteredDateAndTime(null))
                it.visibility = View.GONE
            }

            switchIsDone.setOnCheckedChangeListener { _, isChecked ->
                viewModel.onEvent(AddEditToDoEvent.EnteredIsDone(isChecked))
            }

            btnAddGroup.setOnClickListener {
                DialogAddGroup.show(requireContext()) { groupName ->
                    viewModel.onEvent(AddEditToDoEvent.SaveGroup(groupName))
                }
                Log.i(TAG, "onUIClick: btnAddGroup")
            }
            onClickToolBar()

            btnDelete.setOnClickListener {
                if (alertDeleteBottomSheet.isAdded) {
                    alertDeleteBottomSheet.dismiss()
                } else {
                    alertDeleteBottomSheet.show(childFragmentManager, "AlertDeleteBottomSheet")
                }
                Log.i(TAG, "onUIClick: $alertDeleteBottomSheet.is")
            }
        }
    }


    private fun setData() {
        binding.apply {
            etTitle.setText(viewModel.todoTitle.value)
            etDescription.setText(viewModel.todoDescription.value)
            tvTimeAndDate.text =
                ToDoUtils.dateToString(requireContext(), viewModel.todoDateAndTime.value)
            tvGroup.text = viewModel.groupName.value
            if (AddAndEditFragmentArgs.fromBundle(requireArguments()).todo!!.title.isEmpty()) {
                btnDelete.visibility = View.GONE
            }
            switchIsDone.isChecked = viewModel.isDone.value!!
        }
    }

    private fun setupToolbar() {
        binding.apply {
            toolbar.toolbar.title = resources.getString(thuan.todolist.R.string.my_to_do)
            toolbar.toolbar.inflateMenu(thuan.todolist.R.menu.menu_save)
            // display back button
            toolbar.toolbar.setNavigationIcon(thuan.todolist.R.drawable.ic_arrow_back)
        }
    }

    private fun checkQuitWithoutSave() {
        if (viewModel.getCurrentToDo() != AddAndEditFragmentArgs.fromBundle(requireArguments()).todo) {
            DialogQuitWithOutSaving.show(requireContext()) {
                findNavController().popBackStack()
            }
        } else {
            findNavController().popBackStack()
        }
    }

    private fun onClickToolBar() {
        binding.apply {
            toolbar.toolbar.setNavigationOnClickListener {
                checkQuitWithoutSave()
            }
            toolbar.toolbar.setOnMenuItemClickListener {
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
                Log.i(TAG, "setupListPopUpGroup: ${listGroup[position]}")
                listPopUpGroup.dismiss()
            }
        }
    }

    private fun showSnackBar(message: String) {
        Snackbar.make(binding.root, message, Snackbar.LENGTH_SHORT).show()
    }


    //  handle event back press
    override fun onAttach(context: Context) {
        Log.i("TodoFrag", "onAttach")
        super.onAttach(context)
        val callback: OnBackPressedCallback = object :
            OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                checkQuitWithoutSave()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(this, callback)
    }

    override fun deleteToDo() {
        viewModel.onEvent(AddEditToDoEvent.DeleteToDo)
    }

    override fun setTime(dateAndTime: Date?) {
        Log.i(TAG, "setTime: $dateAndTime")
        viewModel.isTimeSet.value = true
        viewModel.onEvent(AddEditToDoEvent.EnteredDateAndTime(dateAndTime))
    }
}