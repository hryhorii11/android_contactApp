package com.example.level2.presentation.ui.activities.contacts


import android.Manifest
import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.pm.PackageManager
import android.database.Cursor
import android.os.Bundle
import android.provider.ContactsContract
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.level2.R
import com.example.level2.data.LocalUsers
import com.example.level2.data.model.Contact
import com.example.level2.databinding.ActivityContactsBinding
import com.example.level2.presentation.ui.activities.contacts.adapter.ContactAdapter
import com.example.level2.presentation.ui.activities.contacts.intrefaces.ItemClickListener
import com.example.level2.presentation.ui.fragments.addcontactdialog.AddContactDialogFragment
import com.example.level2.presentation.utils.Constants.ADD_CONTACT_TAG
import com.example.level2.presentation.utils.Constants.CONTACTS_REQUEST_CODE
import com.google.android.material.snackbar.Snackbar


class ContactsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityContactsBinding
    private val adapter: ContactAdapter = ContactAdapter(object : ItemClickListener {
        override fun onUserDelete(contact: Contact) {
            viewModel.deleteContact(contact)
            showSnackBar()
        }
    })
    private var contactsList = mutableListOf<Contact>()
    private val viewModel: UsersViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContactsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        getContactsFromPhone()
        viewModel.setUsers(contactsList)
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
                viewModel.deleteContact(adapter.currentList[viewHolder.adapterPosition])
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
        dialog.show(supportFragmentManager, ADD_CONTACT_TAG)
    }

    private fun showSnackBar() {
        Snackbar.make(
            binding.root,
            getString(R.string.remove_contact), Snackbar.LENGTH_LONG
        ).setAction("cancel") {
            viewModel.returnContact()
        }.show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CONTACTS_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                readContact()
                viewModel.setUsers(contactsList)
            } else {
                contactsList = LocalUsers().getUsers().toMutableList()
            }
        }
    }


    @SuppressLint("Range")
    private fun readContact() {
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
    }

    private fun getContactsFromPhone() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            readContact()
            viewModel.setUsers(contactsList)
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_CONTACTS), CONTACTS_REQUEST_CODE)
        }
    }

}
