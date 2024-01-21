package com.example.myapplication.presentation.ui.main.fragments.contactList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigator
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.R
import com.example.myapplication.presentation.utils.SwipeToDeleteCallback
import com.example.myapplication.databinding.FragmentContactListBinding
import com.example.myapplication.data.model.Contact
import com.example.myapplication.data.model.UserFromLogin
import com.example.myapplication.presentation.ui.main.fragments.viewPager.ViewPagerFragmentDirections
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactListFragment : Fragment(), ItemClickListener {
    private lateinit var binding: FragmentContactListBinding
    private lateinit var adapter: ContactAdapter
    private var itemTouchHelper: ItemTouchHelper? = null


    private val viewModel: ContactListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)
        binding = FragmentContactListBinding.inflate(layoutInflater)
        setListeners()

        adapter = ContactAdapter(this)
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.adapter = adapter
        setTouchHelper()


        viewModel.contacts.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        viewModel.errorMessage.observe(viewLifecycleOwner) {
            Toast.makeText(this@ContactListFragment.context, it, Toast.LENGTH_SHORT).show()
        }

        val u: UserFromLogin? = requireActivity().intent.getParcelableExtra("user")
        if (u != null) {
            viewModel.setUsers(
                requireActivity().intent.getStringExtra("token"), u.id
            )
        }
        return binding.root
    }

    private fun setTouchHelper() {
        val callback: SwipeToDeleteCallback
        callback = object : SwipeToDeleteCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                onContactDelete(adapter.currentList[viewHolder.adapterPosition])
            }
        }
        itemTouchHelper = ItemTouchHelper(callback)
        attachItemTouchHelper()


    }

    private fun attachItemTouchHelper() {
        if (!adapter.isSelectionMode)
            itemTouchHelper?.attachToRecyclerView(binding.recycler)
        else itemTouchHelper?.attachToRecyclerView(null)
    }


    private fun setListeners() {
        with(binding) {
            buttonAddContact.setOnClickListener { showAddContactDialog() }
            buttonDeleteSelectMode.setOnClickListener {
                val u: UserFromLogin? = requireActivity().intent.getParcelableExtra("user")
                requireActivity().intent.getStringExtra("token")
                    ?.let {
                        if (u != null) {
                            viewModel.deleteSelectedContacts(it, u.id)
                        }
                    }
                onContactChangeMode()
            }
            buttonSearch.setOnClickListener {
                toSearchMode()
            }
            buttonCloseSearch.setOnClickListener {
                closeSearchMode()
            }
            searchViewContact.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return true
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (newText != null) {
                        viewModel.search(newText)
                    }
                    return true
                }
            })
        }
    }

    private fun closeSearchMode() {
        with(binding)
        {
            buttonSearch.visibility = View.VISIBLE
            buttonBack.visibility = View.VISIBLE
            textViewContacts.visibility = View.VISIBLE
            searchViewContact.visibility = View.GONE
            buttonCloseSearch.visibility = View.GONE
        }
    }

    private fun toSearchMode() {
        with(binding)
        {
            buttonSearch.visibility = View.GONE
            buttonBack.visibility = View.GONE
            textViewContacts.visibility = View.GONE
            searchViewContact.visibility = View.VISIBLE
            buttonCloseSearch.visibility = View.VISIBLE
        }
    }

    private fun showAddContactDialog() {
//        if (!isNavigating) {
//            isNavigating = true
        findNavController().navigate(ViewPagerFragmentDirections.actionViewPagerFragmentToAddContactFragment())
//            Handler().postDelayed(
//                { isNavigating = false },
//                1000
//            )
//        }
    }

    override fun onContactClick(contact: Contact, extras: Navigator.Extras) {
        when (adapter.isSelectionMode) {
            false -> onContactDetail(contact, extras)
            true -> selectContact(contact)
        }
    }

    override fun toggleSelect(contact: Contact) {
        viewModel.toggleSelect(contact)
    }

    override fun onCheckBoxClick(contact: Contact) {
        selectContact(contact)
    }

    private fun selectContact(contact: Contact) {
        if (viewModel.toggleSelect(contact)) {
            adapter.changeMode()
        }
        if (!adapter.isSelectionMode) {
            binding.buttonDeleteSelectMode.visibility = View.GONE
            attachItemTouchHelper()
        }
    }

    private fun onContactDetail(contact: Contact, extras: Navigator.Extras) {
        val action = ViewPagerFragmentDirections.actionViewPagerFragmentToDetailViewFragment(
            contact
        )
        view?.findNavController()?.navigate(action, extras)
    }

    override fun onContactChangeMode() {
        adapter.changeMode()
        attachItemTouchHelper()
        binding.buttonDeleteSelectMode.visibility =
            if (adapter.isSelectionMode) View.VISIBLE else View.GONE
    }

    override fun onContactDelete(contact: Contact) {
        val u: UserFromLogin? = requireActivity().intent.getParcelableExtra("user")
        if (adapter.currentList.contains(contact))
            showSnackBar()
        requireActivity().intent.getStringExtra("token")
            ?.let {
                if (u != null) {
                    viewModel.deleteContact(contact, u.id, it)
                }
            }
    }

    private fun showSnackBar() {
        val snackBar =
            Snackbar.make(
                binding.root,
                getString(R.string.remove_contact), Snackbar.LENGTH_LONG
            )
        snackBar.setAction("cancel") {
            val u: UserFromLogin? = requireActivity().intent.getParcelableExtra("user")
            requireActivity().intent.getStringExtra("token")
                ?.let {
                    if (u != null) {
                        viewModel.returnContact(u.id, it)
                    }
                }
        }
        snackBar.show()
    }


}