package com.example.myapplication.presentation.ui.main.fragments.contactList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigator
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.myapplication.R
import com.example.myapplication.presentation.utils.SwipeToDeleteCallback
import com.example.myapplication.databinding.FragmentContactListBinding
import com.example.myapplication.domain.model.Contact
import com.example.myapplication.notification.Data
import com.example.myapplication.notification.NotificationModel
import com.example.myapplication.presentation.ui.BaseFragment
import com.example.myapplication.presentation.ui.main.fragments.viewPager.ViewPagerFragmentDirections
import com.example.myapplication.presentation.utils.Constants.DEEP_LINK_URI
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.messaging.FirebaseMessaging
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContactListFragment :
    BaseFragment<ContactListViewModel>(R.layout.fragment_contact_list),
    ItemClickListener {

    private lateinit var adapter: ContactAdapter
    private var itemTouchHelper: ItemTouchHelper? = null
    override val viewModel: ContactListViewModel by viewModels()
    private val binding: FragmentContactListBinding by lazy {
        FragmentContactListBinding.inflate(
            layoutInflater
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        super.onCreate(savedInstanceState)

        setListeners()
        adapter = ContactAdapter(this)
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.adapter = adapter
        setTouchHelper()
        setObservers()
        setUsers()
        return binding.root
    }

    private fun setUsers() {
        viewModel.setUsers()
    }

    private fun setObservers() {

        viewModel.contacts.collectUIState(
            onError = {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            },
            onSuccess = {
                adapter.submitList(it)
            }
        )


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
                viewModel.deleteSelectedContacts()
                onContactChangeMode()
            }
            buttonSearch.setOnClickListener {
                sendSearchNotification()

            }
            buttonBack.setOnClickListener{
                val viewPager = activity?.findViewById<ViewPager2>(R.id.viewPager)
                viewPager?.currentItem = 0
            }

        }
    }


    private fun sendSearchNotification() {
        FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
            viewModel
                .sendNotification(
                    NotificationModel(
                        token,
                        Data(
                            getString(R.string.search_notification_title),
                            getString(R.string.search_notification_message),
                            DEEP_LINK_URI
                        )
                    )
                )
        }
    }

    private fun showAddContactDialog() {
        findNavController().navigate(ViewPagerFragmentDirections.actionViewPagerFragmentToAddContactFragment())

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
        if (adapter.currentList.contains(contact))
            showSnackBar()
        viewModel.deleteContact(contact)
    }

    private fun showSnackBar() {
        val snackBar =
            Snackbar.make(
                binding.root,
                getString(R.string.remove_contact), Snackbar.LENGTH_LONG
            )
        snackBar.setAction(R.string.cancel) {
            viewModel.returnDeletedContact()
        }
        snackBar.show()
    }


}