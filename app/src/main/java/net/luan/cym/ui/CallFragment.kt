package net.luan.cym.ui


import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.get
import net.luan.cym.Contact
import net.luan.cym.MainActivity.Companion.callLogContacts
import net.luan.cym.MainActivity.Companion.gson
import net.luan.cym.MainActivity.Companion.saveAndReturnContactList

import net.luan.cym.R
import net.luan.cym.util.ContactListAdapter

class CallFragment : Fragment() {

    private lateinit var contactsListView: ListView
    internal lateinit var mAdapter: ContactListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        // -------
        val inflated = inflater.inflate(R.layout.fragment_call, container, false)

        return inflated
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        readContacts()

        mAdapter = ContactListAdapter(context!!)
        val layoutInflater = LayoutInflater.from(context!!)

    }






    private fun readContacts() {

        // read in contacts
        val rawContacts = saveAndReturnContactList("Contacts")
        val contactsList = arrayOf(rawContacts.size())
        contactsListView = getView()!!.findViewById(R.id.contact_list)
        var i = 0

        Log.d(TAG, "----- CONTACTS -----")
        for (contacts in rawContacts) {
            Log.d(TAG, contacts.toString())
        }



    }


    //TODO:: ADD REFERENCES TO More Credits

//    private fun readAndDisplayContacts() {
//        //enables the read of all contacts on a user's device
//        val cursor : Cursor = context!!.contentResolver.query(
//                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null)!!
//
//        //used to store each contact read in by the cursor
//        val contactInfo = arrayOf(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
//            ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone._ID)
//
//        //creates a 2 item array representing the two textView fields to be populated in the listView
//        val textViews = intArrayOf(android.R.id.text1, android.R.id.text2)
//
//        //adapter which holds all contacts read from the contacts list and transfers that data to be
//        //adapted onto the listView
//        val contactsCursorAdapter = SimpleCursorAdapter(context, android.R.layout.simple_list_item_2,
//            cursor, contactInfo, textViews)
//
//        //list.adapter = contactsCursorAdapter
//
//        //retrieves the selected item from the listView and opens the dialer app to call
//        //list.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
//
//            val temp = contactsCursorAdapter.getView(position, view, parent)
//            val phoneNumberTextView = temp.get(1) as TextView
//            val phoneNumber = phoneNumberTextView.text.toString()
//
//            val intent = Intent(Intent.ACTION_CALL)
//            intent.data = Uri.parse("tel:$phoneNumber")
//            startActivity(intent)
//        }
//
//    }

    /** HOW TO MAKE A CONTACT
     *      var last_contacted: LocalDate = LocalDate.of(2020, Month.January, 1)
     *      Contact("name", 1234567890, 1, last_contacted, 14)
     *
     *      look at the Contact.kt for more info
     */

    companion object {
        private val TAG = "CYM-Debug"
    }
}
