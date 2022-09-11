package thuan.todolist.feature_todo.ui.add

import android.R
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import thuan.todolist.MainActivity
import thuan.todolist.databinding.FragmentAddBinding
import thuan.todolist.feature_todo.adapter.case_todo.ToDoAdapter
import thuan.todolist.feature_todo.di.Injection
import thuan.todolist.feature_todo.domain.model.GroupToDo
import thuan.todolist.feature_todo.domain.model.ToDo
import thuan.todolist.feature_todo.ui.time_date_picker.DateAndTimePickerBottomSheet

/**
 * ChildFragmentManager and parentFragmentManager
 *  - ChildFragmentManager: Fragment in Fragment
 *  - ParentFragmentManager: Fragment in Activity
 *  - FragmentManager: Fragment in Fragment or Activity
 *  - SupportFragmentManager: Fragment in Fragment or Activity
 */
class AddFragment : Fragment() {
    lateinit var binding: FragmentAddBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddBinding.inflate(inflater, container, false)
        return binding.root
    }
    //        (requireContext() as AppCompatActivity).supportActionBar?.apply {
//            setDisplayHomeAsUpEnabled(true)
////            setHomeAsUpIndicator(R.drawable.ic_close)
//            title = "Add Task"
//        }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val dateAndTimePickerBottomSheet = DateAndTimePickerBottomSheet()
        dateAndTimePickerBottomSheet.show(childFragmentManager, "dateAndTimePickerBottomSheet")
        setOptionsMenu()
        setUpUi()
        (requireActivity() as MainActivity).toDoViewModel.savedStateHandle.getLiveData<String>("timeAndDate")
            .observe(viewLifecycleOwner) {
                binding.tvTimeAndDate.text = it
            }
    }

    private fun setUpUi() {
        val listGroup = mutableListOf<String>()
        listGroup.add("Default")
        (requireActivity() as MainActivity).toDoViewModel.getAllGroup()
            .observe(viewLifecycleOwner) {
                Toast.makeText(requireContext(), "size: ${it.size}", Toast.LENGTH_SHORT).show()
                if (GroupToDo(name = "Default") !in it) {
                    (requireActivity() as MainActivity).toDoViewModel.insertGroup(GroupToDo(name = "Default"))
                }

                it.forEach { group ->
                    if (group.name != "Default") {
                        listGroup.add(group.name)
                    }
                }
                // add all group to list except finish group
            }

        val spinner = binding.spinnerGroup
        val adapter =
            ArrayAdapter(requireContext(), R.layout.simple_spinner_item, listGroup)
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)


        spinner.adapter = adapter
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                when (position) {
                    0 -> {
                        spinner.setSelection(0)
                    }
                    1 -> {
                        // work
                    }
                    2 -> {
                        // home
                    }
                    3 -> {
                        // school
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                Log.d("TAG", "onNothingSelected: ")
            }
        }
    }


    private fun setOptionsMenu() {
        // The usage of an interface lets you inject your own implementation

        val menuHost: MenuHost = requireActivity()

        // Add menu items without using the Fragment Menu APIs
        // Note how we can tie the MenuProvider to the viewLifecycleOwner
        // and an optional Lifecycle.State (here, RESUMED) to indicate when
        // the menu should be visible
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(thuan.todolist.R.menu.menu_save, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.home -> {
                        Log.i("AddFragment", "onMenuItemSelected: home")
//                        (requireContext() as AppCompatActivity).supportFragmentManager.popBackStack()
                        // press back button
                        if (binding.etTitle.text.toString().isNotEmpty())
                            showDialogQuitWithoutSaving()
                        else
                            requireActivity().onBackPressed()
                        true
                    }

                    thuan.todolist.R.id.action_save -> {
                        Log.i("AddFragment", "onMenuItemSelected: save")
                        if (binding.etTitle.text.toString().isNotEmpty()) {
                            (requireActivity() as MainActivity).toDoViewModel.insertToDo(
                                ToDo(
                                    0,
                                    binding.etTitle.text.toString(),
                                    binding.etDescription.text.toString(),
                                    binding.tvTimeAndDate.text.toString(),
                                    false,
                                    binding.spinnerGroup.selectedItem.toString()
                                )
                            )
                            requireActivity().onBackPressed()
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Please enter task",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun showDialogQuitWithoutSaving() {
        AlertDialog.Builder(requireContext())
            .setTitle("Quit without saving?")
            .setMessage("Are you sure you want to quit without saving?")
            .setPositiveButton("Yes") { _, _ ->
                requireActivity().onBackPressed()
            }
            .setNegativeButton("No") { dialog, _ ->
                dialog.dismiss()
            }
            .show()
    }
}