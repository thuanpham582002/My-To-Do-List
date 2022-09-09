package thuan.todolist.feature_todo.ui.home

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.view.ActionMode
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import thuan.todolist.MainActivity
import thuan.todolist.R
import thuan.todolist.databinding.FragmentHomeBinding
import thuan.todolist.feature_todo.adapter.ItemsDetailsLookup
import thuan.todolist.feature_todo.adapter.ItemsKeyProvider
import thuan.todolist.feature_todo.adapter.ToDoAdapter
import thuan.todolist.feature_todo.di.Injection
import thuan.todolist.feature_todo.domain.model.ToDo
import thuan.todolist.feature_todo.viewmodel.ToDoViewModel
import thuan.todolist.feature_todo.viewmodel.ToDoViewModelFactory

class HomeFragment : Fragment(), ActionMode.Callback {

    lateinit var binding: FragmentHomeBinding
    lateinit var toDoViewModel: ToDoViewModel
    lateinit var toDoAdapter: ToDoAdapter
    private lateinit var tracker: SelectionTracker<Long>
    private var actionMode: ActionMode? = null

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
        val toDoRepositoryImpl = Injection.provideToDoRepository(requireContext())
        val viewModelFactory = ToDoViewModelFactory.getInstance(toDoRepositoryImpl)
        toDoViewModel = ViewModelProvider(this, viewModelFactory)[ToDoViewModel::class.java]
        toDoAdapter = ToDoAdapter(toDoViewModel)

        setupRecyclerview()
        setOptionsMenu()
        setupUiComponents()

        toDoViewModel.getAllToDo().observe(viewLifecycleOwner) {
            toDoAdapter.setData(it)
        }

        binding.fabAdd.setOnClickListener {
            toDoViewModel.insertToDo(
                ToDo(
                    id = 0,
                    title = "Firs",
                    date = "7/9/2022",
                    time = "First",
                    isCompleted = false,
                    groupName = "First"
                )
            )
            findNavController().navigate(R.id.action_homeFragment_to_addFragment)
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
                menuInflater.inflate(R.menu.menu_main, menu)
                val searchItem = menu.findItem(R.id.action_search)
                val searchView = searchItem.actionView as SearchView
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        toDoViewModel.getAllToDo().observe(viewLifecycleOwner) { list ->
                            toDoAdapter.setData(list.filter {
                                it.title.lowercase().contains(newText.toString().lowercase())
                            })
                        }
                        return false
                    }
                })
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_search -> {
                        true
                    }
                    R.id.action_settings -> {
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun setupUiComponents() {
        tracker = SelectionTracker.Builder(
            "selectionItem",
            binding.recyclerView,
            ItemsKeyProvider(toDoAdapter),
            ItemsDetailsLookup(binding.recyclerView),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
            SelectionPredicates.createSelectAnything()
        ).build()

        tracker.addObserver(
            object : SelectionTracker.SelectionObserver<Long>() {
                override fun onSelectionChanged() {
                    super.onSelectionChanged()
                    if (actionMode == null) {
                        actionMode =
                            (requireActivity() as MainActivity).startSupportActionMode(this@HomeFragment)
                        Log.i("HomeFragment", "onSelectionChanged: ")
                    }

                    val items = tracker.selection.size()
                    if (items > 0) {
                        actionMode?.title = "$items selected"
                    } else {
                        actionMode?.finish()
                    }
                }
            })

        toDoAdapter.tracker = tracker

    }


    // Save the state of the tracker if the activity is destroyed or recreated
    override fun onSaveInstanceState(outState: Bundle) {
        Log.i("HomeFragment", "onSaveInstanceState: ")
        tracker.onSaveInstanceState(outState)
        super.onSaveInstanceState(outState)
    }
    // Restore the state of the tracker if the activity is destroyed or recreated
    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        Log.i("HomeFragment", "onViewStateRestored: ")
        tracker.onRestoreInstanceState(savedInstanceState)
        if (tracker.hasSelection()) {
            actionMode =
                (requireActivity() as MainActivity).startSupportActionMode(this@HomeFragment)
            actionMode?.title = "${tracker.selection.size()} selected"
        }

        super.onViewStateRestored(savedInstanceState)
    }


    // ActionMode.Callback
    override fun onCreateActionMode(mode: ActionMode?, menu: Menu?): Boolean {
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
                    tracker.selection.contains(it.id.toLong())
                }.toMutableList()

                selected.forEach {
                    toDoViewModel.deleteToDo(it)
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
        tracker.clearSelection()
        actionMode = null
    }

}