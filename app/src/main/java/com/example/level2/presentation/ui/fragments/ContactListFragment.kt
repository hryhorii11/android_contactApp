package com.example.level2.presentation.ui.fragments

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
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.example.level2.presentation.ui.fragments.adapters.ContactAdapter
import com.example.level2.R
import com.example.level2.data.LocalUsers
import com.example.level2.databinding.FragmentContactListBinding
import com.example.level2.data.model.Contact
import com.example.level2.presentation.utils.ext.swipeToDelete
import com.google.android.material.snackbar.Snackbar

interface ItemClickListener {
    fun onContactDelete(contact: Contact)
    fun onContactDetail(contact: Contact, extras: Navigator.Extras)
}
class ContactListFragment : Fragment() {
    private val binding by lazy { FragmentContactListBinding.inflate(layoutInflater) }
    private lateinit var adapter: ContactAdapter
    private var isNavigating=false
    private val permissionLauncher = registerForActivityResult(
        RequestPermission(),
        ::onGotPermissionResult
    )
    private val viewModel:ContactListViewModel by navGraphViewModels(R.id.nav)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        setRecyclerView()
        setListeners()
        setTouchHelper()
        setObserver()
        getContactsFromPhone()
        return binding.root
    }

    private fun setObserver() {
        viewModel.contacts.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }

    private fun setRecyclerView() {
        adapter = ContactAdapter(object :ItemClickListener{
            override fun onContactDelete(contact: Contact) {
                contactDelete(contact)
            }
            override fun onContactDetail(contact: Contact, extras: Navigator.Extras) {
               contactDetail(contact,extras)
            }
        })
        binding.recycler.adapter = adapter
    }

    private fun setTouchHelper() {
        binding.recycler.swipeToDelete { contactDelete(viewModel.getContactFromPosition(it)) }
    }

    private fun setListeners() {
        binding.buttonAddContact.setOnClickListener { showAddContactDialog() }
    }

    private fun showAddContactDialog() {
        if(!isNavigating) {
            isNavigating = true
            findNavController().navigate(ContactListFragmentDirections.actionContactListFragmentToAddContactDialogFragment2())
            Handler().postDelayed(
                { isNavigating = false },
                1000
            )
        }
    }

     fun contactDetail(contact: Contact, extras: Navigator.Extras) {

        val action = ContactListFragmentDirections.actionContactListFragmentToDetailViewFragment(
            contact.photo, contact.name, contact.career, contact.address
        )
        findNavController().navigate(action, extras)
    }

     private fun contactDelete(contact: Contact) {
         if (adapter.currentList.contains(contact))
             showSnackBar()
        viewModel.deleteContact(contact)
    }

    private fun showSnackBar() {
        Snackbar.make(
            binding.root,
            getString(R.string.remove_contact), Snackbar.LENGTH_LONG
        ).setAction(getString(R.string.cancel)) {
            viewModel.returnContact()
        }.show()
    }

    private fun onGotPermissionResult(granted: Boolean) {
        if (granted)
            viewModel.setUsers(readContact())
        else
            viewModel.setUsers(LocalUsers().getUsers())
    }

    @SuppressLint("Range")
    private fun readContact(): MutableList<Contact> {
        val contactsList = mutableListOf<Contact>()
        val contentResolver: ContentResolver = requireContext().contentResolver
        val cursor: Cursor? = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        cursor?.use {
            while (it.moveToNext()) {
                val name = it.getString(it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                contactsList.add(
                    Contact(
                        name,
                        "career",
                        "",
                        "address"
                    )
                )
            }
            it.close()
        }
        return contactsList
    }

    private fun getContactsFromPhone() {
        permissionLauncher.launch(Manifest.permission.READ_CONTACTS)
    }
}