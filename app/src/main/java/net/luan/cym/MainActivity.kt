package net.luan.cym

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import android.os.Bundle
import android.provider.CallLog
import android.provider.ContactsContract
import android.util.Log
import android.widget.*
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonParser
import net.luan.cym.ui.StatsFragment
import java.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

class MainActivity : AppCompatActivity() {
    private lateinit var sharedPref: SharedPreferences

    private lateinit var statsFragment: StatsFragment

    private lateinit var monthMap: HashMap<String, Int>
    private var phoneNumberToName =  HashMap<String, String>()
    private var contactNameToIndex =  HashMap<String, Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // UI Components
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        val title = findViewById<TextView>(R.id.title)

        // SharedPreference handler
        sharedPref = applicationContext.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE)
        editor = sharedPref.edit()
        Log.d(TAG, "----- Main Activity -----")
        Log.d(TAG, "--- SHARED PREFERENCES ---")
        Log.d(TAG, "MODE: \t${sharedPref.getBoolean("WHITELISTING_MODE", false)}")
        Log.d(TAG, "FIRST: \t${sharedPref.getBoolean("FIRST", true)}")
        Log.d(TAG, "FREQ: \t${sharedPref.getInt("REMINDER_FREQ", 0)}")
        Log.d(TAG, "FRAGMENT: ${sharedPref.getInt("FRAGMENT", 0)}")
        Log.d(TAG, "--------------------------")

        // changes the fragment to the currently selected item
        bottomNav.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.stats -> {
                    statsFragment = StatsFragment()
                    supportFragmentManager.beginTransaction().replace(R.id.main_frame, statsFragment)
                        .commit()
                    title.text = resources.getString(R.string.statistics)
                }
                R.id.call -> {
                    val intent = Intent(this, ContactManagerActivity::class.java)
                    startActivity(intent)
                    bottomNav.selectedItemId = sharedPref.getInt("FRAGMENT", 0)
                }
                R.id.settings -> {
                    supportFragmentManager.beginTransaction().replace(R.id.main_frame, SettingsFragment())
                        .commit()
                    title.text = resources.getString(R.string.settings)
                }
            }
            true
        }

        // default page is call page
        if (savedInstanceState == null) {
            bottomNav.selectedItemId = sharedPref.getInt("FRAGMENT", R.id.call)
        }

        callLogContacts = readCallLog(this)
//        for (i in callLogContacts.indices) {
//            Log.i(TAG, callLogContacts.get(i).toString())
//        }

        var allContacts = readCallLog(this)


        // ----- HAMID -----
        gson = GsonBuilder().create()
        // ------ END ------
    }

    // ----- Settings Fragment -----
    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            addPreferencesFromResource(R.xml.preferences)
        }
        // HOW TO DO SETTINGS
        // val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        // val allowPush = sharedPreferences.getBoolean("allow_push", false)
        // val defaultTime = sharedPreferences.getInt("default_time", 14)
    }

    override fun onResume() {
        super.onResume()

        Log.d(TAG, "--- MainActivity.onResume() ---")
        // changing bottomNav to selected activity
        val bottomNav = findViewById<BottomNavigationView>(R.id.bottom_nav)
        bottomNav.selectedItemId = sharedPref.getInt("FRAGMENT", 0)
    }

    // ----- HAMID -----
    // no one knows what any of his code does... missing comments

    private fun createMap(): HashMap<String, Int> {
        monthMap = HashMap()
        monthMap[getString(R.string.Jan)] = 1; monthMap[getString(R.string.Feb)] = 2; monthMap[getString(R.string.Mar)] = 3
        monthMap[getString(R.string.Apr)] = 4; monthMap[getString(R.string.May)] = 5; monthMap[getString(R.string.Jun)] = 6
        monthMap[getString(R.string.Jul)] = 7; monthMap[getString(R.string.Aug)] = 8; monthMap[getString(R.string.Sep)] = 9
        monthMap[getString(R.string.Oct)] = 10; monthMap[getString(R.string.Nov)] = 11; monthMap[getString(R.string.Dec)] = 12

        return monthMap
    }

    private fun readCallLog(context: Context) : ArrayList<Contact>{
        val allProcessedContacts = ArrayList<Contact>()
        val contentUri = CallLog.Calls.CONTENT_URI

        try {
            val cursor = context.contentResolver.query(contentUri, null, null, null, null)
            val nameUri = cursor!!.getColumnIndex(CallLog.Calls.CACHED_LOOKUP_URI)
            val number = cursor.getColumnIndex(CallLog.Calls.NUMBER)
            val date = cursor.getColumnIndex(CallLog.Calls.DATE)
            //testing whether the query result is empty
            val isNonEmptyQueryResult = cursor.moveToFirst()
            contactNameToIndex.clear()
            createMap()

            if (isNonEmptyQueryResult) {
                do {
                    var isPreviouslySeenContact = false
                    val callerNameUriFormat = cursor.getString(nameUri)
                    val phoneNumber = cursor.getString(number).toLong()
                    val callDate = cursor.getString(date)
                    val dateString = Date(callDate.toLong()).toString()
                    val year = dateString.substring(dateString.length - 4).toInt()
                    val month = monthMap.get(dateString.substring(4, 7))
                    val day = dateString.substring(8, 10).toInt()
                    val lastContacted: LocalDate = LocalDate.of(year, month!!, day)
                    val name: String

                    if (phoneNumberToName[phoneNumber.toString()] == null) {
                        name = getContactName(phoneNumber.toString(), context)
                    } else if (callerNameUriFormat == null) {
                        name = phoneNumberToName[phoneNumber.toString()]!!
                    } else {
                        name = getNameFromUriFormat(callerNameUriFormat)
                    }

                    //Node: the default alert_pref is set to 7
                    val contact = Contact(name, phoneNumber, 1, lastContacted, 7, false)

                    //if we've seen this contact before
                    if (contactNameToIndex[name] != null) {
                        val indexOfContact = contactNameToIndex[name]
                        allProcessedContacts[indexOfContact!!].freq += 1
                        allProcessedContacts[indexOfContact!!].last_contacted = lastContacted
                        isPreviouslySeenContact = true
                    } else {
                        contactNameToIndex[name] = allProcessedContacts.size
                    }

                    if (!isPreviouslySeenContact) {
                        allProcessedContacts.add(contact)
                        phoneNumberToName[phoneNumber.toString()] = name
                    }
                } while (cursor.moveToNext())
            }
            cursor.close()
        } catch (e : SecurityException) {
            Log.w(TAG, e.toString())
        }

        return allProcessedContacts
    }

    private fun getNameFromUriFormat(nameInUri : String?) : String {
        return if (nameInUri != null) {
            val cursor = contentResolver.query(Uri.parse(nameInUri), null, null, null, null)

            var name = ""
            if ((cursor?.count ?: 0) > 0) {
                while (cursor != null && cursor.moveToNext()) {
                    name = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                }
            }
            cursor?.close()

            return name
        } else {
            "Unknown Number"
        }
    }

    fun getContactName(phoneNumber: String, context: Context): String {
        val uri = Uri.withAppendedPath(
            ContactsContract.PhoneLookup.CONTENT_FILTER_URI,
            Uri.encode(phoneNumber)
        )

        val projection = arrayOf(ContactsContract.PhoneLookup.DISPLAY_NAME)
        var contactName = ""
        val cursor = context.contentResolver.query(uri, projection, null, null, null)

        if (cursor != null) {
            if (cursor.moveToFirst()) {
                contactName = cursor.getString(0)
            }
            cursor.close()
        }

        return contactName
    }
    // ------ END ------

    companion object {
        private val TAG = "CYM-Debug"

        private val PREF_FILE = "net.luan.cym.prefs"

        fun returnContactList(): ArrayList<Contact> {
            return callLogContacts
        }

        // ----- HAMID -----
        lateinit var gson: Gson
        private lateinit var editor: SharedPreferences.Editor
        lateinit var callLogContacts: ArrayList<Contact>
        fun saveAndReturnContactList(key: String): JsonArray {
            val listJSON = gson.toJson(callLogContacts)
            Log.d(TAG, "----------------------")
            Log.d(TAG, callLogContacts.toString())
            Log.d(TAG, "----------------------")
            editor.putString(key, listJSON)
            editor.apply()
            val parser = JsonParser()

            return parser.parse(listJSON).asJsonArray
        }
        // ------ END ------
    }
}