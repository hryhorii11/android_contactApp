package com.example.myapplication.presentation.ui.fragments

import com.example.myapplication.presentation.ui.fragments.interfaces.ItemClickListener
import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.database.Cursor
import android.os.Bundle
import android.os.Handler
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts.RequestPermission
import androidx.fragment.app.Fragment
import androidx.navigation.Navigator
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.myapplication.presentation.ui.fragments.adapters.ContactAdapter
import com.example.myapplication.R
import com.example.myapplication.data.LocalUsers
import com.example.myapplication.presentation.utils.SwipeToDeleteCallback
import com.example.myapplication.databinding.FragmentContactListBinding

import com.example.myapplication.data.model.Contact
import com.google.android.material.snackbar.Snackbar


class ContactListFragment : Fragment(), ItemClickListener {
    private lateinit var binding: FragmentContactListBinding
    private lateinit var adapter: ContactAdapter
    private var isNavigating=false
    private var itemTouchHelper: ItemTouchHelper? = null
    private val permissionLauncher = registerForActivityResult(
        RequestPermission(),
        ::onGotPermissionResult
    )

    private val viewModel: ContactListViewModel by navGraphViewModels(R.id.nav_graph)

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
        permissionLauncher.launch(Manifest.permission.READ_CONTACTS)

        viewModel.contacts.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        return binding.root
    }

    private fun setTouchHelper() {
        val callback: SwipeToDeleteCallback // TODO inline
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
        binding.buttonAddContact.setOnClickListener { showAddContactDialog() }
        binding.buttonDeleteSelectMode.setOnClickListener {
            viewModel.deleteSelectedContacts()
            onContactChangeMode()
        }
    }

    private fun showAddContactDialog() {
        if(!isNavigating) {
            isNavigating = true
            findNavController().navigate(ViewPagerFragmentDirections.actionViewPagerFragmentToAddContactDialogFragment())
            Handler().postDelayed(
                { isNavigating = false },
                1000
            )
        }
    }

    override fun onContactClick(contact: Contact, extras: Navigator.Extras) {
        when (adapter.isSelectionMode) {
            false -> onContactDetail(contact, extras)
            true -> selectContact(contact)
        }
    }

    override fun onCheckBoxClick(contact: Contact) {
        selectContact(contact)
    }

    private fun selectContact(contact: Contact) {
        adapter.toggleSelection(adapter.currentList.indexOf(contact))
        if (!adapter.isSelectionMode) {
            binding.buttonDeleteSelectMode.visibility = View.GONE
            attachItemTouchHelper()
        }
    }

    private fun onContactDetail(contact: Contact, extras: Navigator.Extras) {
        val action = ViewPagerFragmentDirections.actionViewPagerFragmentToDetailViewFragment(
            contact.photo, contact.name, contact.career, contact.address
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
        snackBar.setAction("cancel") {
            viewModel.returnContact()
        }
        snackBar.show()
    }

    private fun onGotPermissionResult(granted: Boolean) {
        if (granted)
            viewModel.setUsers(getContactsFromPhone())
        else
            viewModel.setUsers(LocalUsers().getUsers())
    }

    @SuppressLint("Range")  // TODO to repository
    fun getContactsFromPhone(): List<Contact> {
        val contactsList = mutableListOf<Contact>()
        val contentResolver: ContentResolver = requireContext().contentResolver
        val cursor: Cursor? = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        cursor?.let {
            while (it.moveToNext()) {
                val name = it.getString(it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                contactsList.add(
                    Contact(
                        name,
                        "career",
                        "address",
                        R.drawable.baseline_person_2_24.toString()
                    )
                )
            }
            it.close()
        }
        return contactsList
    }
}