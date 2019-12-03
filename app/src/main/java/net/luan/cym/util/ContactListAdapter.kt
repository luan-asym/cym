package net.luan.cym.util

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.TextView
import net.luan.cym.Contact
import net.luan.cym.MainActivity.Companion.allContacts
import net.luan.cym.MainActivity.Companion.returnContactList
import net.luan.cym.R

class ContactListAdapter(private val context: Context,
                         private val data: ArrayList<Contact>) : BaseAdapter() {
    private val inflater: LayoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

    override fun getCount(): Int {
        return data.size
    }

    override fun getItem(pos: Int): Any {
        return data[pos]
    }

    override fun getItemId(pos: Int): Long {
        return pos.toLong()
    }

    override fun getView(pos: Int, convertView: View?, parent: ViewGroup): View {
        // UI Components
        val item = inflater.inflate(R.layout.contact_item, parent, false)
        val nameView = item.findViewById<TextView>(R.id.contact_name)
        val lastContactedView = item.findViewById<TextView>(R.id.last_contacted)
        val whitelistView = item.findViewById<CheckBox>(R.id.favorite)

        val contact = getItem(pos) as Contact
        nameView.text = contact.name
        lastContactedView.text = "Last contacted: ${contact.last_contacted.toString()}"
        whitelistView.isChecked = contact.whitelisted

        // changes icon to whitelist status
        if (contact.whitelisted) {
            whitelistView.setButtonDrawable(R.drawable.ic_checked)
        } else {
            whitelistView.setButtonDrawable(R.drawable.ic_star)
        }

        // whitelist onClickListener
        whitelistView.setOnClickListener {
            allContacts.remove(contact)
            contact.changeWhitelist(whitelistView.isChecked)
            Log.d(TAG, "${contact.name} whitelist has changed to ${contact.whitelisted}")
            allContacts.add(contact)

            if (contact.whitelisted) {
                whitelistView.setButtonDrawable(R.drawable.ic_checked)
            } else {
                whitelistView.setButtonDrawable(R.drawable.ic_star)
            }
        }

        return item
    }

    companion object {
        private val TAG = "CYM-Debug"

        private val PREF_FILE = "net.luan.cym.prefs"
    }
}