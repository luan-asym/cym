package net.luan.cym.util

import android.content.Context
import android.graphics.ColorSpace
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.*
import androidx.recyclerview.widget.RecyclerView

import net.luan.cym.Contact
import net.luan.cym.R

class ContactListAdapter(private val mContext: Context) {
    private val mItems = ArrayList<Contact>()

    fun add(item: Contact){
        mItems.add(item)
    }

    fun getCount(): Int {
        return mItems.size
    }

    fun getItem(pos: Int): Any {
        return mItems[pos]
    }

    fun getItemId(pos: Int): Long {
        return pos.toLong()
    }

    fun getView(pos: Int, convertView: View?, parent: ViewGroup): View? {
        val viewHolder: ViewHolder

        if (null == convertView) {
            viewHolder = ViewHolder()

            val layoutInflater = LayoutInflater.from(mContext)
            val layout = layoutInflater.inflate(R.layout.contact_item, parent, false)
            layout.tag = viewHolder
            viewHolder.mItemLayout = layout as RelativeLayout

            viewHolder.mName = layout.findViewById(R.id.contact_name)
            viewHolder.mFavorite = layout.findViewById(R.id.favorite)
        } else {
            viewHolder = convertView.tag as ViewHolder
            viewHolder.mFavorite!!.isChecked = false
        }

        var name = mItems[pos].name

        viewHolder.mName?.text = name

        return viewHolder.mItemLayout

//        val view: View = layoutInflater.inflate(R.layout.contact_item, null)
//        var name = view.findViewById<TextView>(R.id.contact_name)
//        var favorite = view.findViewById<CheckBox>(R.id.favorite)
//
//        var contact: Contact = mItems[pos]
//
//        name.text = contact.name
//        favorite.isChecked = false
//
//        return view



//        val viewHolder: ViewHolder
//        if (convertView == null) {
//            viewHolder = ViewHolder()
//
//            val inflater = LayoutInflater.from(mContext)
//            val layout = inflater.inflate(R.layout.contact_item, parent, false)
//
//            layout.tag = viewHolder
//            viewHolder.mItemLayout = layout as RelativeLayout
//            viewHolder.mName = layout.findViewById(R.id.contact_name)
//            viewHolder.mFavorite = layout.findViewById(R.id.favorite)
//        } else {
//            viewHolder = convertView.tag as ViewHolder
//        }
//
//        var name = mItems[pos].name
//        var favorite = mItems[pos].whitelisted
//
//        viewHolder.mName?.text = name


    }

    internal class ViewHolder {
        var position: Int = 0
        var mItemLayout: RelativeLayout? = null
        var mName: TextView? = null
        var mFavorite: CheckBox? = null
    }
}