package net.luan.cym

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.GsonBuilder
import net.luan.cym.MainActivity.Companion.gson
import net.luan.cym.MainActivity.Companion.returnContactList
import net.luan.cym.MainActivity.Companion.saveAndReturnContactList
import net.luan.cym.util.ContactListAdapter
import java.time.LocalDate


class ContactManagerActivity : AppCompatActivity() {
    private lateinit var sharedPref: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private lateinit var listView: ListView

    private var whitelistMap: HashMap<String, Boolean> = HashMap()
    private var lastContactedMap: HashMap<String, String> = HashMap()

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_manager)

        // UI Components
        val bottomNav = findViewById<BottomNavigationView>(R.id.contact_manager_bottom_nav)
        listView = findViewById(R.id.contact_manager_list)

        // SharedPreference handler
        sharedPref = applicationContext.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE)
        editor = sharedPref.edit()
        Log.d(TAG, "----- Contact Manager Activity -----")
        Log.d(TAG, "--- SHARED PREFERENCES ---")
        Log.d(TAG, "MODE: \t${sharedPref.getBoolean("WHITELISTING_MODE", false)}")
        Log.d(TAG, "FIRST: \t${sharedPref.getBoolean("FIRST", true)}")
        Log.d(TAG, "FREQ: \t${sharedPref.getInt("REMINDER_FREQ", 0)}")
        Log.d(TAG, "FRAGMENT: ${sharedPref.getInt("FRAGMENT", 0)}")
        Log.d(TAG, "--------------------------")

        // bottom bar navigation to MainActivity
        bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.stats -> {
                    Log.d(TAG, "Contact Manager selected: stats")
                    editor.putInt("FRAGMENT", item.itemId)
                    editor.apply()
                    finish()
                }
                R.id.call -> {
                    // do nothing
                }
                R.id.settings -> {
                    Log.d(TAG, "Contact Manager selected: settings")
                    editor.putInt("FRAGMENT", item.itemId)
                    editor.apply()
                    finish()
                }
            }

            true
        }

        // attaching adapter to display custom listview
        val adapter = ContactListAdapter(this, returnContactList())
        listView.adapter = adapter
    }

    companion object {
        private val TAG = "CYM-Debug"

        private val PREF_FILE = "net.luan.cym.prefs"
    }
}