package com.example.level2.presentation.ui.activities.contacts


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
import com.example.level2.presentation.ui.activities.contacts.adapter.ContactAdapter
import com.example.level2.presentation.ui.activities.contacts.intrefaces.ItemClickListener
import com.example.level2.R
import com.example.level2.databinding.ActivityContactsBinding
import com.example.level2.presentation.ui.fragments.addcontactdialog.AddContactDialogFragment
import com.example.level2.data.model.Contact
import com.google.android.material.snackbar.Snackbar
import factory


class ContactsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContactsBinding
    private val adapter: ContactAdapter = ContactAdapter(object : ItemClickListener {
        override fun onUserDelete(contact: Contact) {
            viewModel.deleteContact(contact)
            showSnackBar()
        }
    })
    private lateinit var viewModel: UsersViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel =
            ViewModelProvider(this, factory())[UsersViewModel::class.java] // TODO: by delegate
        viewModel.setUsers(getContactsFromPhone())

        setListeners()
        setRecyclerView()


        setTouchHelper()
        setObserver()

    }

    private fun setObserver() {
        viewModel.contacts.observe(this) {
            adapter.submitList(it)
        }
    }

    private fun setRecyclerView() {
        with(binding.recycler) {
            layoutManager = LinearLayoutManager(this@ContactsActivity)
            adapter = this@ContactsActivity.adapter
        }
    }

    private fun setTouchHelper() {
        val callback = object : SwipeToDeleteCallback() {
            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                viewModel.deleteContact(adapter.currentList[viewHolder.bindingAdapterPosition])
                showSnackBar()
            }
        }
        val itemTouchHelper = ItemTouchHelper(callback)
        itemTouchHelper.attachToRecyclerView(binding.recycler)
    }

    private fun setListeners() {
        binding.buttonAddContact.setOnClickListener { showAddContactDialog() }
    }

    private fun showAddContactDialog() {
        val dialog = AddContactDialogFragment(viewModel)
        dialog.show(supportFragmentManager, "AddContactDialog") // TODO: to constants
    }

    private fun showSnackBar() {
        // TODO: you can do this without variable
        Snackbar.make(
            binding.root,
            getString(R.string.remove_contact), Snackbar.LENGTH_LONG
        ).setAction("cancel") {
            viewModel.returnContact()
        }.show()
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
                val name = it.getString(it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                contactsList.add(
                    Contact(
                        name,
                        "career",
                        ""
                    )
                )
            }
            it.close()
        }

        return contactsList
    }

}