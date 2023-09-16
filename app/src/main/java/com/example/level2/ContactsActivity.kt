package com.example.level2


import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.level2.databinding.ActivityContactsBinding
import com.example.level2.fragments.AddContactDialogFragment
import com.example.level2.model.Contact
import com.google.android.material.snackbar.Snackbar
import factory


interface ItemClickListener {
    fun onUserDelete(contact: Contact)
}

class ContactsActivity : AppCompatActivity(), ItemClickListener {
    private lateinit var binding: ActivityContactsBinding
    private lateinit var adapter: ContactAdapter
    private lateinit var viewModel: UsersViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this, factory())[UsersViewModel::class.java]
        viewModel.setUsers(getContactsFromPhone())

        setListeners()

        adapter = ContactAdapter(this)
        binding.recycler.layoutManager = LinearLayoutManager(this)
        binding.recycler.adapter = adapter

        setTouchHelper()

        viewModel.contacts.observe(this) {
            adapter.submitList(it)
        }
    }

    private fun setTouchHelper() {
        val callback=object:SwipeToDeleteCallback()
        {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                onUserDelete(adapter.currentList[viewHolder.bindingAdapterPosition])
            }
        }
        val itemTouchHelper=ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(binding.recycler)
    }

    private fun setListeners() {
        binding.buttonAddContact.setOnClickListener { showAddContactDialog() }
    }

    private fun showAddContactDialog() {
        val dialog = AddContactDialogFragment(viewModel)
        dialog.show(supportFragmentManager, "AddContactDialog")
    }

    override fun onUserDelete(contact:Contact) {
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
                this,
                Manifest.permission.READ_CONTACTS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), 0)
        }
        val contactsList = mutableListOf<Contact>()
        val contentResolver: ContentResolver = this.contentResolver
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
                        R.drawable.baseline_person_24.toString()
                    )
                )
            }
            it.close()
        }
        return contactsList
    }

}