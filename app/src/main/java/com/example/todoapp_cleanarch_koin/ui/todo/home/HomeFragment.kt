package com.example.todoapp_cleanarch_koin.ui.todo.home

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.EditText
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.view.ActionMode
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.selection.SelectionPredicates
import androidx.recyclerview.selection.SelectionTracker
import androidx.recyclerview.selection.StorageStrategy
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.core.data.source.local.model.todo.TodoEntity
import com.example.core.domain.util.TodoOrder
import com.example.todoapp_cleanarch_koin.MainActivity
import com.example.todoapp_cleanarch_koin.R
import com.example.todoapp_cleanarch_koin.databinding.FragmentHomeBinding
import com.example.todoapp_cleanarch_koin.ui.todo.addedittodo.AddEditBottomSheetFragment
import com.example.todoapp_cleanarch_koin.ui.todo.home.components.adapter.TodoAdapter
import com.example.todoapp_cleanarch_koin.ui.todo.home.components.adapter.selectiontracker.TodosDetailsLookup
import com.example.todoapp_cleanarch_koin.ui.todo.home.components.adapter.selectiontracker.TodosKeyProvider
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.tabs.TabLayout
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.viewmodel.ext.android.viewModel

class HomeFragment : Fragment(), ActionMode.Callback, SearchView.OnQueryTextListener {
    private lateinit var binding: FragmentHomeBinding
    private val homeViewModel: HomeViewModel by viewModel()
    private lateinit var todoAdapter: TodoAdapter
    private var actionMode: ActionMode? = null
    lateinit var selectionTracker: SelectionTracker<Long>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar()
        setupTabLayout()
        setupRecyclerview()
        setupSelectionTrackerAdapter()
        onUIClick()
        subscribeToObservers()
    }

    private fun subscribeToObservers() {
        lifecycleScope.launchWhenStarted {
            homeViewModel.state.collectLatest {
                Log.i("HomeFragment", "HomeState: $it")

                todoAdapter.setData(it.listTodo.filter { todo -> todo.title.contains(it.searchQuery) })
                binding.rlOrder.visibility =
                    if (!it.isOrderSectionVisible) View.GONE else View.VISIBLE
            }
        }
        lifecycleScope.launchWhenStarted {
            homeViewModel.stateListGroupTodoEntity.collect {
                Log.i("HomeFragment", "stateListGroupTodoEntity: $it")
                val listGroupName =
                    listOf(resources.getString(R.string.all)) + listOf(resources.getString(R.string.default_group)) + it.map { group -> group.name } + listOf(
                        ""
                    )

                binding.tabLayout.apply {
                    removeAllTabs()
                    val currentIndex = homeViewModel.currentGroupIndex
                    for (element in listGroupName) {
                        addTab(binding.tabLayout.newTab().setText(element))
                    }
                    getTabAt(listGroupName.size - 1)?.setIcon(R.drawable.ic_add)
                    getTabAt(currentIndex)?.select()
                }
            }
        }
    }

    private fun onUIClick() {
        binding.apply {
            fabAdd.setOnClickListener {
                onDestroyActionMode(actionMode)
                homeViewModel.saveStateTodo(TodoEntity())
                AddEditBottomSheetFragment().show(childFragmentManager, "addEdit")
            }
            btnOrder.setOnClickListener {
                homeViewModel.onEvent(HomeEvent.ToggleOrderSection)
            }
            rbDate.setOnClickListener {
                homeViewModel.onEvent(HomeEvent.Order(TodoOrder.OrderBy.Date))
            }
            rbTitle.setOnClickListener {
                homeViewModel.onEvent(HomeEvent.Order(TodoOrder.OrderBy.Title))
            }
            rbNone.setOnClickListener {
                homeViewModel.onEvent(HomeEvent.Order(TodoOrder.OrderBy.None))
            }
            rbAsc.setOnClickListener {
                homeViewModel.onEvent(HomeEvent.Order(TodoOrder.OrderType.Ascending))
            }
            rbDesc.setOnClickListener {
                homeViewModel.onEvent(HomeEvent.Order(TodoOrder.OrderType.Descending))
            }
            rbTodoAll.setOnClickListener {
                homeViewModel.onEvent(HomeEvent.Order(TodoOrder.TodoType.All))
            }
            rbTodoCompleted.setOnClickListener {
                homeViewModel.onEvent(HomeEvent.Order(TodoOrder.TodoType.Completed))
            }
            rbTodoUncompleted.setOnClickListener {
                homeViewModel.onEvent(HomeEvent.Order(TodoOrder.TodoType.Uncompleted))
            }
        }
    }

    private fun setupSelectionTrackerAdapter() {
        selectionTracker = SelectionTracker.Builder(
            "selectionItem",
            binding.recyclerView,
            TodosKeyProvider(todoAdapter),
            TodosDetailsLookup(binding.recyclerView),
            StorageStrategy.createLongStorage()
        ).withSelectionPredicate(
            SelectionPredicates.createSelectAnything()
        ).build()

        selectionTracker.addObserver(object : SelectionTracker.SelectionObserver<Long>() {
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
        todoAdapter.tracker = selectionTracker
    }

    private fun setupRecyclerview() {
        todoAdapter = TodoAdapter(homeViewModel)
        with(binding.recyclerView) {
            adapter = todoAdapter
            layoutManager = LinearLayoutManager(requireContext())
            addItemDecoration(
                DividerItemDecoration(
                    requireContext(), DividerItemDecoration.VERTICAL
                )
            )
        }
    }

    private fun setupTabLayout() {
        binding.tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                if (tab.text == "") {
                    findNavController().navigate(R.id.action_homeFragment_to_addGroupFragment)
                    return
                }
                Log.i("HomeFragment", "onTabSelected: ${tab.position}")
                when (tab.position) {
                    0 -> {
                        homeViewModel.onEvent(HomeEvent.Order(TodoOrder.GroupType.All))
                    }
                    1 -> {
                        homeViewModel.onEvent(HomeEvent.Order(TodoOrder.GroupType.Default))
                    }
                    else -> {
                        homeViewModel.onEvent(
                            HomeEvent.Order(
                                TodoOrder.GroupType.Custom(
                                    homeViewModel.stateListGroupTodoEntity.value[tab.position - 2].id
                                )
                            )
                        )
                    }
                }

                if (homeViewModel.currentGroupIndex != tab.position) homeViewModel.onEvent(
                    HomeEvent.CurrentGroupIndex(tab.position)
                )
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })
    }

    private fun setupToolbar() {
        binding.apply {
            toolbar.toolbar.title = resources.getString(R.string.my_to_do_list)
            toolbar.toolbar.inflateMenu(R.menu.menu_main)
            setActionSearchToDo(toolbar.toolbar.menu)
            setOnMenuItemClickListener(toolbar.toolbar)
        }
    }

    private fun setOnMenuItemClickListener(toolbar: MaterialToolbar) {
        toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_search -> {
                    true
                }
                R.id.action_settings -> {
                    findNavController().navigate(R.id.action_homeFragment_to_settingsFragment)
                    true
                }
                else -> false
            }
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
        searchView.setOnQueryTextListener(this@HomeFragment)

    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        return false
    }

    override fun onQueryTextChange(newText: String): Boolean {
        Log.i("onQueryTextChange", "onQueryTextChange: $newText")
        homeViewModel.onEvent(HomeEvent.SearchQueryChange(newText))
        return true
    }

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
                val selected = todoAdapter.dataList.filter {
                    selectionTracker.selection.contains(it.id)
                }.toMutableList()

                selected.forEach {
                    homeViewModel.onEvent(HomeEvent.DeleteToDo(it))
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