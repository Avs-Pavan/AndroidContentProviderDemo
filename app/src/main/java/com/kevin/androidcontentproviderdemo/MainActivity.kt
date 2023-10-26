package com.kevin.androidcontentproviderdemo

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import com.kevin.androidcontentproviderdemo.databinding.ActivityMainBinding
import com.kevin.androidcontentproviderdemo.recyclerview.ContactAdapter

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy {
        ActivityMainBinding.inflate(layoutInflater)
    }

    private val adapter by lazy {
        ContactAdapter(contacts)
    }

    private val contacts = mutableListOf<Contact>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


        initView()

    }

    private fun initView() {

        binding.recyclerView.apply {
            adapter = this@MainActivity.adapter
        }

        binding.fab.setOnClickListener {
            // ask permission to read contacts
            getContactsPermission()
        }


    }

    private fun getContactsPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
            && checkSelfPermission(android.Manifest.permission.READ_CONTACTS)
            != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(
                arrayOf(android.Manifest.permission.READ_CONTACTS),
                101
            )
        } else {
            getContacts()
            adapter.notifyDataSetChanged()
        }
    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
            getContacts()
            adapter.notifyDataSetChanged()
        }
    }


    @SuppressLint("Range")
    fun getContacts(): MutableList<Contact> {
        contacts.clear()

        val cursor = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        cursor?.let {
            if (cursor.count > 0)
                while (cursor.moveToNext()) {

                    val id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID))
                    val name =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                    val phoneNum =
                        cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))
                            .toInt()
                    val contact = Contact(name)
                    if (phoneNum > 0) {

                        val numCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Phone.CONTACT_ID + "=?",
                            arrayOf(id),
                            null
                        )

                        numCursor?.let {
                            if (numCursor.count > 0) {
                                while (numCursor.moveToNext()) {
                                    val phoneNumber = numCursor.getString(
                                        numCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                                    )
                                    contact.numbers.add(phoneNumber)
                                }
                            }
                        }
                        numCursor?.close()
                    }
                    contacts.add(contact)
                }

        }
        cursor?.close()

        return contacts
    }


}