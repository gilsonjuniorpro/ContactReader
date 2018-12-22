package com.contactreader.ca.ui

import android.Manifest
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.MediaStore
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import com.contactreader.ca.R
import com.contactreader.ca.adapter.ContactAdapter
import com.contactreader.ca.dto.ContactDTO
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    private val CONTACTS_PERMISSION_CODE = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        contactList.layoutManager = LinearLayoutManager(this)

        btReadContact.setOnClickListener{

            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_CONTACTS) == PackageManager.PERMISSION_GRANTED) {

                val list: MutableList<ContactDTO> = ArrayList()
                val contacts = contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    null, null, null, null)

                while(contacts.moveToNext()){
                    val contactDTO = ContactDTO()

                    val name = contacts.getString(contacts.getColumnIndex(
                        ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME))

                    val number = contacts.getString(contacts.getColumnIndex(
                        ContactsContract.CommonDataKinds.Phone.NUMBER))

                    val uriPhoto = contacts.getString(contacts.getColumnIndex(
                        ContactsContract.CommonDataKinds.Phone.PHOTO_URI))

                    contactDTO.name = name
                    contactDTO.number = number

                    if(uriPhoto != null)
                        contactDTO.image = MediaStore.Images.Media.getBitmap(
                            contentResolver, Uri.parse(uriPhoto))

                    list.add(contactDTO)
                }

                contactList.adapter = ContactAdapter(list,this)
                contacts.close()
            } else {
                requestPermission()
            }
        }
    }

    private fun requestPermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.READ_CONTACTS)) {

            AlertDialog.Builder(this)
                .setTitle("Permission needed")
                .setMessage("This permission is needed to access the phone contacts")
                .setPositiveButton("ok", DialogInterface.OnClickListener { dialog, which ->
                    ActivityCompat.requestPermissions(this,
                        arrayOf(Manifest.permission.READ_CONTACTS), CONTACTS_PERMISSION_CODE)
                })
                .setNegativeButton("cancel", DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
                .create().show()

        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            CONTACTS_PERMISSION_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

}