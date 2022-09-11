package thuan.todolist.feature_todo.ui.home

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.view.ActionMode
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import thuan.todolist.MainActivity
import thuan.todolist.R
import thuan.todolist.databinding.FragmentHomeBinding
import thuan.todolist.feature_todo.adapter.case_todo.ToDoAdapter
import thuan.todolist.feature_todo.adapter.case_todo.todo_selectiontracker.ToDosDetailsLookup
import thuan.todolist.feature_todo.adapter.case_todo.todo_selectiontracker.ToDosKeyProvider

class HomeFragment : Fragment(), ActionMode.Callback {
    private var isOnDestroyView: Boolean = false
    lateinit var binding: FragmentHomeBinding
    lateinit var toDoAdapter: ToDoAdapter
    private var actionMode: ActionMode? = null
    private var searchQuery: String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.i("HomeFragment", "onViewCreated")
        isOnDestroyView = false
        toDoAdapter = ToDoAdapter((requireActivity() as MainActivity).toDoViewModel)
        setupRecyclerview()
        setOptionsMenu()
        setupUiComponents()

        val group = listOf("All", "Completed", "Uncompleted")
        val spinner = binding.spinnerGroup
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, group)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
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
                        // set text for spinner
                        spinner.setSelection(0)
                    }
                    1 -> {
                        // set text for spinner
                        spinner.setSelection(1)
                    }
                    2 -> {
                        // set text for spinner
                        spinner.setSelection(2)
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        if (!(requireActivity() as MainActivity).toDoViewModel.savedStateHandle.contains("search"))
            (requireActivity() as MainActivity).toDoViewModel.savedStateHandle["search"] = ""

        (requireActivity() as MainActivity).toDoViewModel.savedStateHandle.getLiveData<String>("search")
            .observe(viewLifecycleOwner) {
                if (it != "") {
                    searchQuery = it
                    (requireActivity() as MainActivity).toDoViewModel.getAllToDo()
                        .observe(viewLifecycleOwner) { list ->
                            toDoAdapter.setData(list.filter { toDo ->
                                toDo.title.lowercase().contains(it.toString().lowercase())
                            })
                        }
                } else {
                    (requireActivity() as MainActivity).toDoViewModel.getAllToDo()
                        .observe(viewLifecycleOwner) { list ->
                            toDoAdapter.setData(list)
                        }
                }
            }


//        (requireActivity() as MainActivity).toDoViewModel.getAllToDoWithGroup("First").observe(viewLifecycleOwner) {
//            toDoAdapter.setData(it[0].todos)
//            Log.i("HomeFragment", "onViewCreated: $it")
//        }


        binding.fabAdd.setOnClickListener {
            onDestroyActionMode(actionMode)
            (requireActivity() as MainActivity).toDoViewModel.goToAddToDoFragment(it)
        }
    }

    private fun setupRecyclerview() {
        with(binding.recyclerView) {
            adapter = toDoAdapter
            layoutManager = LinearLayoutManager(requireContext())
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
                // Add menu items here
                Log.i("HomeFragment", "onCreateMenu")
                menuInflater.inflate(R.menu.menu_main, menu)
                actionSearchToDo(menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_search -> {
                        true
                    }
                    R.id.action_settings -> {
                        true
                    }
                    androidx.appcompat.R.id.home -> {
                        Snackbar.make(
                            binding.root,
                            "Home button clicked",
                            Snackbar.LENGTH_SHORT
                        ).show()
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun actionSearchToDo(menu: Menu) {
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView
        if (searchQuery != "") {
            Log.i("HomeFragment", "actionSearchToDo: $searchQuery")
            searchItem.expandActionView()
            searchView.setQuery(searchQuery, false)
            searchView.clearFocus()
        }

/*        searchItem.setOnActionExpandListener(object : MenuItem.OnActionExpandListener {
            override fun onMenuItemActionExpand(item: MenuItem?): Boolean {
                Snackbar.make(
                    binding.root,
                    "Search1 button clicked",
                    Snackbar.LENGTH_SHORT
                ).show()
                return true
            }

            override fun onMenuItemActionCollapse(item: MenuItem?): Boolean {
                return true
            }
        })*/

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!isOnDestroyView)
                    (requireActivity() as MainActivity).toDoViewModel.savedStateHandle["search"] =
                        newText
                return false
            }
        })
    }

    private fun setupUiComponents() {
        (requireActivity() as MainActivity).toDoViewModel.trackerAdapterHomeFragment =
            SelectionTracker.Builder(
                "selectionItem",
                binding.recyclerView,
                ToDosKeyProvider(toDoAdapter),
                ToDosDetailsLookup(binding.recyclerView),
                StorageStrategy.createLongStorage()
            ).withSelectionPredicate(
                SelectionPredicates.createSelectAnything()
            ).build()

        (requireActivity() as MainActivity).toDoViewModel.trackerAdapterHomeFragment.addObserver(
            object : SelectionTracker.SelectionObserver<Long>() {
                override fun onSelectionChanged() {
                    super.onSelectionChanged()
                    if (actionMode == null) {
                        actionMode =
                            (requireActivity() as MainActivity).startSupportActionMode(this@HomeFragment)
                        Log.i("HomeFragment", "onSelectionChanged: ")
                    }

                    val items =
                        (requireActivity() as MainActivity).toDoViewModel.trackerAdapterHomeFragment.selection.size()
                    if (items > 0) {
                        actionMode?.title = "$items selected"
                    } else {
                        actionMode?.finish()
                    }
                }
            })

        toDoAdapter.tracker =
            (requireActivity() as MainActivity).toDoViewModel.trackerAdapterHomeFragment

    }


    // Save the state of the tracker if the activity is destroyed or recreated
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        Log.i("HomeFragment", "onSaveInstanceState:")
        (requireActivity() as MainActivity).toDoViewModel.trackerAdapterHomeFragment.onSaveInstanceState(
            outState
        )
    }

    // Restore the state of the tracker if the activity is destroyed or recreated
    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        Log.i("HomeFragment", "onViewStateRestored: ")
        (requireActivity() as MainActivity).toDoViewModel.trackerAdapterHomeFragment.onRestoreInstanceState(
            savedInstanceState
        )
        if ((requireActivity() as MainActivity).toDoViewModel.trackerAdapterHomeFragment.hasSelection()) {
            actionMode =
                (requireActivity() as MainActivity).startSupportActionMode(this@HomeFragment)
            actionMode?.title =
                "${(requireActivity() as MainActivity).toDoViewModel.trackerAdapterHomeFragment.selection.size()} selected"
        }
    }


    // ActionMode.Callback
    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        binding.fabAdd.hide()
        mode?.menuInflater?.inflate(R.menu.menu_delete, menu)
        return true
    }

    override fun onPrepareActionMode(mode: ActionMode?, menu: Menu?): Boolean {
        return true
    }

    override fun onActionItemClicked(mode: ActionMode?, item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.action_delete -> {
                val selected = toDoAdapter.dataList.filter {
                    (requireActivity() as MainActivity).toDoViewModel.trackerAdapterHomeFragment.selection.contains(
                        it.id.toLong()
                    )
                }.toMutableList()

                selected.forEach {
                    (requireActivity() as MainActivity).toDoViewModel.deleteToDo(it)
                }

                actionMode?.finish()
                true
            }
            else -> {
                false
            }
        }
    }

    override fun onDestroyActionMode(mode: ActionMode?) {
        Log.i("HomeFragment", "onDestroyActionMode: ")
        binding.fabAdd.show()
        (requireActivity() as MainActivity).toDoViewModel.trackerAdapterHomeFragment.clearSelection()
        actionMode = null
    }
}