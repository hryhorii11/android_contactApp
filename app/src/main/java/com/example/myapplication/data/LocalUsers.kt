//package com.example.myapplication.data
//
//import android.annotation.SuppressLint
//import android.provider.ContactsContract
//import com.example.myapplication.App
//import com.example.myapplication.R
//import com.example.myapplication.data.model.Contact
//
//
//class LocalUsers {
//    @SuppressLint("Range")
//    fun getPhoneContacts(): MutableList<Contact> {
//
//        val contactList: MutableList<Contact> = ArrayList()
//
//        val contentResolver = App.contentResolverInstance
//        val uri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI
//        val sortOrder = ContactsContract.Contacts.DISPLAY_NAME
//
//        val cursor = contentResolver.query(
//            uri,
//            null,
//            null,
//            null,
//            sortOrder,
//            null
//        )
//
//        cursor?.let {
//            while (cursor.moveToNext()) {
//                val name = cursor.getString(
//                    cursor.getColumnIndex(
//                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
//                    )
//                )
//
//                val contact = Contact(
//                    name,
//                    "career",
//                    "address",
//                    R.drawable.baseline_person_2_24.toString()
//                )
//
//                contactList.add(contact)
//            }
//        }
//        cursor?.close()
//
//        return contactList
//    }
//    fun getUsers() :List<Contact> = listOf(
//        Contact("NAME1", "career1", "address","photo"),
//        Contact("NAME2", "career2", "address","photo"),
//        Contact("NAME3", "career3","address", "photo"),
//        Contact("NAME4", "career4","address", "photo"),
//        Contact("NAME5", "career5","address", "photo"),
//        Contact("NAME6", "career6","address", "photo"),
//        Contact("NAME7", "career7", "address","photo"),
//        Contact("NAME8", "career8","address", "photo"),
//        Contact("NAME9", "career9","address", "photo"),
//        Contact("NAME10", "career10", "address","photo"),
//    )
//}
