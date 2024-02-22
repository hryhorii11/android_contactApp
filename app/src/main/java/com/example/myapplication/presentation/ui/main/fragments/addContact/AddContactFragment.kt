package com.example.myapplication.presentation.ui.main.fragments.addContact

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.myapplication.R
import com.example.myapplication.domain.model.Contact
import com.example.myapplication.databinding.FragmentAddContactBinding
import com.example.myapplication.presentation.ui.BaseFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class AddContactFragment : BaseFragment<AddContactViewModel>(R.layout.fragment_add_contact) {
    private val binding: FragmentAddContactBinding by lazy {
        FragmentAddContactBinding.inflate(
            layoutInflater
        )
    }
    private lateinit var adapter: AddContactAdapter
    override val viewModel: AddContactViewModel by viewModels()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setListeners()
        setRecyclerAdapter()
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.adapter = adapter
        setObservers()
        viewModel.setContacts()
        return binding.root
    }

    private fun setObservers() {
        viewModel.contacts.collectUIState(
            onError = {
                binding.progressDialog.visibility = View.GONE
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            },
            onSuccess = {
                binding.progressDialog.visibility = View.GONE
                adapter.submitList(it)
            },
            onLoading = {
                binding.progressDialog.visibility = View.VISIBLE
            }
        )
    }

    private fun setRecyclerAdapter() {
        adapter = AddContactAdapter(object : AddClickListener {
            override fun addContact(id: Int) {
                viewModel.addContact(id)
            }

            override fun onItemClick(contact: Contact) {
                findNavController().navigate(
                    AddContactFragmentDirections.actionAddContactFragmentToDetailViewFragment(
                        contact
                    )
                )
            }
        })
    }

    private fun setListeners() {
        with(binding) {
            buttonBack.setOnClickListener {
                findNavController().popBackStack()
            }
            buttonSearch.setOnClickListener { toSearchMode() }
            buttonCloseSearch.setOnClickListener {
                closeSearchMode()
                viewModel.search("")
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
            textViewUsers.visibility = View.VISIBLE
            searchViewContact.visibility = View.GONE
            buttonCloseSearch.visibility = View.GONE
        }
    }

    private fun toSearchMode() {
        with(binding)
        {
            buttonSearch.visibility = View.GONE
            buttonBack.visibility = View.GONE
            textViewUsers.visibility = View.GONE
            searchViewContact.visibility = View.VISIBLE
            buttonCloseSearch.visibility = View.VISIBLE
        }
    }

}