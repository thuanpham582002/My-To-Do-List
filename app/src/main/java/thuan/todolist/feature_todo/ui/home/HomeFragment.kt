package thuan.todolist.feature_todo.ui.home

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import thuan.todolist.R
import thuan.todolist.databinding.FragmentHomeBinding
import thuan.todolist.feature_todo.adapter.ToDoAdapter
import thuan.todolist.feature_todo.di.Injection
import thuan.todolist.feature_todo.domain.model.ToDo
import thuan.todolist.feature_todo.viewmodel.ToDoViewModel
import thuan.todolist.feature_todo.viewmodel.ToDoViewModelFactory

/**
 * A simple [Fragment] subclass as the default destination in the navigation.
 */
class HomeFragment : Fragment() {

    lateinit var binding: FragmentHomeBinding
    lateinit var toDoViewModel: ToDoViewModel
    lateinit var toDoAdapter: ToDoAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater)
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
                        // show back button when search is clicked
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

    private fun setOptionsMenuDelete(list: List<ToDo>) {
        // The usage of an interface lets you inject your own implementation
        requireActivity().title = list.size.toString()

        val menuHost: MenuHost = requireActivity()

        // Add menu items without using the Fragment Menu APIs
        // Note how we can tie the MenuProvider to the viewLifecycleOwner
        // and an optional Lifecycle.State (here, RESUMED) to indicate when
        // the menu should be visible
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // Add menu items here
                menuInflater.inflate(R.menu.menu_delete, menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_delete -> {
                        // show back button when search is clicked
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

}