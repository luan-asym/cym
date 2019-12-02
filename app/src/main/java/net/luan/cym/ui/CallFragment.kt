package net.luan.cym.ui

import android.content.Intent
import android.database.Cursor
import android.net.Uri
import android.os.Bundle
import android.provider.ContactsContract
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.view.get

class CallFragment : Fragment() {
    lateinit var list : ListView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val inflated = inflater.inflate(net.luan.cym.R.layout.fragment_call, container, false)

        list = inflated.findViewById(net.luan.cym.R.id.listView) as ListView

        readAndDisplayContacts()

        return inflated
    }

    //TODO:: ADD REFERENCES TO More Credits

    private fun readAndDisplayContacts() {
        //enables the read of all contacts on a user's device
        val cursor : Cursor = context!!.contentResolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null)!!

        //used to store each contact read in by the cursor
        val contactInfo = arrayOf(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME,
            ContactsContract.CommonDataKinds.Phone.NUMBER, ContactsContract.CommonDataKinds.Phone._ID)

        //creates a 2 item array representing the two textView fields to be populated in the listView
        val textViews = intArrayOf(android.R.id.text1, android.R.id.text2)

        //adapter which holds all contacts read from the contacts list and transfers that data to be
        //adapted onto the listView
        val contactsCursorAdapter = SimpleCursorAdapter(context, android.R.layout.simple_list_item_2,
            cursor, contactInfo, textViews)

        list.adapter = contactsCursorAdapter

        //retrieves the selected item from the listView and opens the dialer app to call
        list.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->

            val temp = contactsCursorAdapter.getView(position, view, parent) as TwoLineListItem
            val phoneNumberTextView = temp.get(1) as TextView
            val phoneNumber = phoneNumberTextView.text.toString()

            val intent = Intent(Intent.ACTION_CALL)
            intent.data = Uri.parse("tel:$phoneNumber")
            startActivity(intent)
        }

        /** HOW TO MAKE A CONTACT
         *      var last_contacted: LocalDate = LocalDate.of(2020, Month.January, 1)
         *      Contact("name", 1234567890, 1, last_contacted, 14)
         *
         *      look at the Contact.kt for more info
         */
    }

    companion object {
        private val TAG = "CYM-Debug"
    }
}
