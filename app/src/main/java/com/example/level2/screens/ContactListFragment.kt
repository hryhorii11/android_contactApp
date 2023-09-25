package com.example.level2.screens

import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigator
import androidx.navigation.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.level2.ContactAdapter
import com.example.level2.R
import com.example.level2.SwipeToDeleteCallback
import com.example.level2.databinding.FragmentContactListBinding
import com.example.level2.fragments.AddContactDialogFragment
import com.example.level2.model.Contact
import com.google.android.material.snackbar.Snackbar

interface ItemClickListener {
    fun onContactDelete(contact: Contact)
    fun onContactDetail(contact: Contact,extras:Navigator.Extras)
}

class ContactListFragment: Fragment(),ItemClickListener  {
    private lateinit var binding: FragmentContactListBinding
    private lateinit var adapter: ContactAdapter
    private  val viewModel: ContactListViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        super.onCreate(savedInstanceState)
        binding = FragmentContactListBinding.inflate(layoutInflater)
        setListeners()
        setTouchHelper()

        adapter = ContactAdapter(this)
        binding.recycler.layoutManager = LinearLayoutManager(requireContext())
        binding.recycler.adapter = adapter
        viewModel.setUsers(getContactsFromPhone())
        viewModel.contacts.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
        return binding.root
    }

    private fun setTouchHelper() {
        val callback=object: SwipeToDeleteCallback()
        {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                onContactDelete(adapter.currentList[viewHolder.bindingAdapterPosition])
            }
        }
        val itemTouchHelper= ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(binding.recycler)
    }

    private fun setListeners() {
        binding.buttonAddContact.setOnClickListener { showAddContactDialog() }
    }

    private fun showAddContactDialog() {
        val dialog = AddContactDialogFragment(viewModel)
        dialog.show(childFragmentManager, "AddContactDialog")
    }

    override fun onContactDetail(contact: Contact,extras: Navigator.Extras) {

        val action=ContactListFragmentDirections.actionContactListFragmentToDetailViewFragment(
            contact.photo,contact.name,contact.career,contact.address
        )
        view?.findNavController()?.navigate(action,extras)

    }
    override fun onContactDelete(contact:Contact) {
        viewModel.deleteContact(contact)
        showSnackBar()

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
    @SuppressLint("Range")
    fun getContactsFromPhone(): List<Contact> {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.READ_CONTACTS), 0)
        }
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
                val id = it.getString(it.getColumnIndex(ContactsContract.Contacts._ID))
                val name = it.getString(it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                contactsList.add(
                    Contact(
                        id.toInt(),
                        name,
                        "career",
                         "address",
                        R.drawable.baseline_android_24.toString()
                    )
                )
            }
            it.close()
        }
        return contactsList
    }
}