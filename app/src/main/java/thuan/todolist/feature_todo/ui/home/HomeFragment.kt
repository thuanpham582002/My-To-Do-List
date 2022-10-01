package thuan.todolist.feature_todo.ui.home

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.view.ActionMode
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.appbar.MaterialToolbar
import thuan.todolist.MainActivity
import thuan.todolist.R
import thuan.todolist.databinding.FragmentHomeBinding
import thuan.todolist.di.Injection
import thuan.todolist.feature_todo.domain.util.*
import thuan.todolist.feature_todo.ui.home.components.adapter.ToDoAdapter
import thuan.todolist.feature_todo.ui.home.components.adapter.todo_selectiontracker.ToDosDetailsLookup
import thuan.todolist.feature_todo.ui.home.components.adapter.todo_selectiontracker.ToDosKeyProvider

const val TAG = "HomeFragment"

class HomeFragment : Fragment(), ActionMode.Callback, SearchView.OnQueryTextListener,
    AdapterView.OnItemSelectedListener {
    private lateinit var binding: FragmentHomeBinding
    private lateinit var toDoAdapter: ToDoAdapter
    private lateinit var spinner: Spinner
    lateinit var selectionTracker: SelectionTracker<Long>
    private var actionMode: ActionMode? = null
    private var isViewCreated = false
    private lateinit var viewModel: ToDoViewModel

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
        subscribeToObservers()
    }

    private fun setupViewModel() {
        val toDoRepositoryImpl = Injection.provideToDoRepository(requireContext())
        val viewModelFactory =
            ToDoViewModelFactory(Injection.provideToDoUseCases(toDoRepositoryImpl))
        viewModel = ViewModelProvider(this, viewModelFactory)[ToDoViewModel::class.java]
    }

    private fun subscribeToObservers() {
        viewModel.isFilterVisible.observe(viewLifecycleOwner) {
            if (it) {
                binding.rlOrder.visibility = View.VISIBLE
            } else {
                binding.rlOrder.visibility = View.GONE
            }
        }

        viewModel.listTodo.observe(viewLifecycleOwner) {
            toDoAdapter.setData(it)
            viewModel.notifiItemPos.observe(viewLifecycleOwner) { pos ->
                Log.i(TAG, "subscribeToObservers: $pos")
                toDoAdapter.notifyItemChanged(pos)
            }
        }
    }

    private fun onUIClick() {
        binding.fabAdd.setOnClickListener {
            onDestroyActionMode(actionMode)
            it.findNavController()
                .navigate(HomeFragmentDirections.actionHomeFragmentToAddFragment(viewModel.defaultTodo))
        }

        binding.btnOrder.setOnClickListener {
            viewModel.isFilterVisible.value = viewModel.isFilterVisible.value != true
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

    private fun setupSpinnerGroup() {
        viewModel.getGroupToDo().observe(viewLifecycleOwner) {
            val group = listOf(resources.getString(R.string.all)) + it.map { group -> group.name }
            spinner = binding.spinnerGroup
            val adapter =
                ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, group)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinner.adapter = adapter
            spinner.onItemSelectedListener = this
        }
    }

    override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
        if (p2 == 0)
            viewModel.onEvent(ToDosEvent.Order(GroupType.All))
        else
            viewModel.onEvent(ToDosEvent.Order(GroupType.Custom(spinner.selectedItem.toString())))
    }

    override fun onNothingSelected(p0: AdapterView<*>?) {
        TODO("Not yet implemented")
    }

    private fun setupToolbar() {
        binding.apply {
            toolbar.toolbar.inflateMenu(R.menu.menu_main)
            setActionSearchToDo(toolbar.toolbar.menu)
            setOnMenuItemClickListener(toolbar.toolbar)
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
                    findNavController().navigate(HomeFragmentDirections.actionHomeFragmentToSettingsFragment())
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
        }
    }

    private fun setActionSearchToDo(menu: Menu) {
        val searchItem = menu.findItem(R.id.action_search)
        val searchView = searchItem.actionView as SearchView

        // set text color
        val editText = searchView.findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
        AppCompatDelegate.getDefaultNightMode().let { mode ->
            if (mode == AppCompatDelegate.MODE_NIGHT_YES) {
                editText.setHintTextColor(Color.GRAY)
                editText.setTextColor(Color.BLACK)
            } else {
                editText.setHintTextColor(Color.DKGRAY)
                editText.setTextColor(Color.WHITE)
            }
        }
        searchView.queryHint = resources.getString(R.string.search_to_do)
        Log.i(TAG, "setActionSearchToDo: ${searchView.query}")
        searchView.setOnQueryTextListener(this@HomeFragment)
    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        viewModel.onEvent(ToDosEvent.Order(SearchOrder(newText)))
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
                        actionMode?.title = "$items ${resources.getString(R.string.selected)}"
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
                "${selectionTracker.selection.size()} ${resources.getString(R.string.selected)}"
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
                    selectionTracker.selection.contains(it.id)
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