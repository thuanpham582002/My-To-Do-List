package thuan.todolist.feature_todo.ui.home

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.view.ActionMode
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.MaterialToolbar
import thuan.todolist.MainActivity
import thuan.todolist.R
import thuan.todolist.databinding.FragmentHomeBinding
import thuan.todolist.di.Injection
import thuan.todolist.feature_todo.adapter.case_todo.ToDoAdapter
import thuan.todolist.feature_todo.adapter.case_todo.todo_selectiontracker.ToDosDetailsLookup
import thuan.todolist.feature_todo.adapter.case_todo.todo_selectiontracker.ToDosKeyProvider
import thuan.todolist.feature_todo.domain.use_case.GetToDos
import thuan.todolist.feature_todo.domain.util.GroupType
import thuan.todolist.feature_todo.domain.util.OrderType
import thuan.todolist.feature_todo.domain.util.ToDoTagType
import thuan.todolist.feature_todo.domain.util.ToDoType

const val TAG = "HomeFragment"

class HomeFragment : Fragment(), ActionMode.Callback, SearchView.OnQueryTextListener,
    AdapterView.OnItemSelectedListener {
    private lateinit var searchView: SearchView
    private lateinit var binding: FragmentHomeBinding
    private lateinit var toDoAdapter: ToDoAdapter
    lateinit var selectionTracker: SelectionTracker<Long>
    private var actionMode: ActionMode? = null
    private var isViewCreated = false
    private lateinit var viewModel: ToDoViewModel
    private lateinit var getToDos: GetToDos

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
        isViewCreated = true
        setupViewModel()
        setupToolbar()
        setupSpinnerGroup()
        setupRecyclerview()
        setupSelectionTrackerAdapter()
        onUIClick()
//        subscribeToObservers()

        viewModel.toDoOrder.observe(viewLifecycleOwner) { order ->
//            Transformations.map(getToDos(order)) {   // Transformations.map() is not working
//                viewModel.listTodo.value = it
//            }
            getToDos(order).observe(viewLifecycleOwner) {
                viewModel.listTodo.value = it
            }
        }

        viewModel.listTodo.observe(viewLifecycleOwner) {
            Log.i(TAG, "onViewCreated: ")
            toDoAdapter.setData(it)
        }
    }

    private fun onUIClick() {
        binding.fabAdd.setOnClickListener {
            onDestroyActionMode(actionMode)
            viewModel.apply {
                goToAddAndEditToDoFragment(it, defaultTodo)
            }
        }

        binding.btnOrder.setOnClickListener {
            viewModel.onEvent(ToDosEvent.Order(GroupType.Custom("Default")))
        }

        binding.rbDate.setOnClickListener {
            viewModel.onEvent(ToDosEvent.Order(ToDoTagType.Date))
        }

        binding.rbTitle.setOnClickListener {
            viewModel.onEvent(ToDosEvent.Order(ToDoTagType.Title))
        }

        binding.rbNone.setOnClickListener {
            viewModel.onEvent(ToDosEvent.Order(ToDoTagType.None))
        }

        binding.rbAsc.setOnClickListener {
            viewModel.onEvent(ToDosEvent.Order(OrderType.Ascending))
        }

        binding.rbDesc.setOnClickListener {
            viewModel.onEvent(ToDosEvent.Order(OrderType.Descending))
        }

        binding.rbTodoAll.setOnClickListener {
            viewModel.onEvent(ToDosEvent.Order(ToDoType.All))
        }

        binding.rbTodoCompleted.setOnClickListener {
            viewModel.onEvent(ToDosEvent.Order(ToDoType.Completed))
        }

        binding.rbTodoUncompleted.setOnClickListener {
            viewModel.onEvent(ToDosEvent.Order(ToDoType.Uncompleted))
        }
    }

    private fun setupViewModel() {
        val toDoRepositoryImpl = Injection.provideToDoRepository(requireContext())
        getToDos = GetToDos(toDoRepositoryImpl)
        val viewModelFactory = ToDoViewModelFactory(toDoRepositoryImpl)
        viewModel = ViewModelProvider(this, viewModelFactory)[ToDoViewModel::class.java]
    }

    private fun setupSpinnerGroup() {
        val group = listOf("All", "Completed", "Uncompleted")
        val spinner = binding.spinnerGroup
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, group)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter
        spinner.onItemSelectedListener = this
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
//        when (p2) {
//            0 -> {
//                viewModel.getAllToDo()
//                    .observe(viewLifecycleOwner) { list ->
//                        toDoAdapter.setData(list)
//                    }
//            }
//            1 -> {
//                viewModel.getAllCompletedToDo()
//                    .observe(viewLifecycleOwner) { list ->
//                        toDoAdapter.setData(list)
//                    }
//            }
//            2 -> {
//                viewModel.getAllUncompletedToDo()
//                    .observe(viewLifecycleOwner) { list ->
//                        toDoAdapter.setData(list)
//                    }
//            }
//
//    }
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    private fun setupToolbar() {
        binding.apply {
            toolbar.title = "My To Do List"
            toolbar.inflateMenu(R.menu.menu_main)
            setActionSearchToDo(toolbar.menu)
            setOnMenuItemClickListener(toolbar)
        }
    }

    private fun setOnMenuItemClickListener(toolbar: MaterialToolbar) {
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_search -> {
                    Log.i(TAG, "setOnMenuItemClickListener: search")
                    true
                }
                R.id.action_settings -> {
                    Log.i(TAG, "setOnMenuItemClickListener: setting")
                    true
                }
                else -> false
            }
        }
    }

    private fun setupRecyclerview() {
        toDoAdapter = ToDoAdapter(viewModel)
        with(binding.recyclerView) {
            adapter = toDoAdapter
            layoutManager = LinearLayoutManager(requireContext())
//            FastScrollerBuilder(this).build()
        }
    }

    private fun setActionSearchToDo(menu: Menu) {
        val searchItem = menu.findItem(R.id.action_search)
        searchView = searchItem.actionView as SearchView
        searchView.queryHint = "Search To Do"
        Log.i(TAG, "setActionSearchToDo: ${searchView.query}")
        searchView.setOnQueryTextListener(this@HomeFragment)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String?): Boolean {
        viewModel.listTodo.observe(viewLifecycleOwner) {
            toDoAdapter.setData(it.filter { toDo ->
                toDo.title.lowercase().contains(newText.toString().lowercase())
            })
        }
        return true
    }

    private fun setupSelectionTrackerAdapter() {
        selectionTracker =
            SelectionTracker.Builder(
                "selectionItem",
                binding.recyclerView,
                ToDosKeyProvider(toDoAdapter),
                ToDosDetailsLookup(binding.recyclerView),
                StorageStrategy.createLongStorage()
            ).withSelectionPredicate(
                SelectionPredicates.createSelectAnything()
            ).build()

        selectionTracker.addObserver(
            object : SelectionTracker.SelectionObserver<Long>() {
                override fun onSelectionChanged() {
                    super.onSelectionChanged()
                    if (actionMode == null) {
                        actionMode =
                            (requireActivity() as MainActivity).startSupportActionMode(this@HomeFragment)
                        Log.i("HomeFragment", "onSelectionChanged: ")
                    }

                    val items = selectionTracker.selection.size()
                    if (items > 0) {
                        actionMode?.title = "$items selected"
                    } else {
                        actionMode?.finish()
                    }
                }
            })
        toDoAdapter.tracker = selectionTracker
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Save the state of the tracker if the activity is destroyed or recreated
        if (isViewCreated) {
            selectionTracker.onSaveInstanceState(outState)
        }
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        Log.i("HomeFragment", "onViewStateRestored: ")
        super.onViewStateRestored(savedInstanceState)

        // Restore the state of the tracker if the activity is destroyed or recreated
        selectionTracker.onRestoreInstanceState(savedInstanceState)
        if (selectionTracker.hasSelection()) {
            actionMode =
                (requireActivity() as MainActivity).startSupportActionMode(this@HomeFragment)
            actionMode?.title =
                "${selectionTracker.selection.size()} selected"
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
                    selectionTracker.selection.contains(it.id.toLong())
                }.toMutableList()

                selected.forEach {
                    viewModel.onEvent(ToDosEvent.DeleteToDo(it))
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
        selectionTracker.clearSelection()
        actionMode = null
    }
}