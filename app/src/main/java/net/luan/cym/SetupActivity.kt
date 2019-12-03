package net.luan.cym

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat

class SetupActivity : AppCompatActivity() {
    private lateinit var sharedPref: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    // define permissions needed
    private val permissionsNeeded = arrayOf(Manifest.permission.READ_CALL_LOG,
        Manifest.permission.READ_CONTACTS, Manifest.permission.CALL_PHONE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup)
        Log.i(TAG, "--- Entered first time setup ---")

        // SharedPreference handler
        sharedPref = applicationContext.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE)
        editor = sharedPref.edit()

        // initialize UI components
        val next = findViewById<Button>(R.id.next)
        val progress = findViewById<ProgressBar>(R.id.setup_progress)
        val prompt = findViewById<TextView>(R.id.prompt)

        // start progress at zero and set onClick to increment by 25
        progress.progress = 0
        progressNext(progress.progress, prompt)
        next.setOnClickListener {
            progress.progress += 33
            progressNext(progress.progress, prompt)
        }

        editor.putBoolean("FIRST", false)
        editor.apply()
    }

    // --- Progress bar handling ---
    // start the initial setup questions
    //  0% -> welcome
    // 25% -> requesting permissions
    // 50% -> reminder frequency
    // 75% -> completed
    private fun progressNext(step: Int, p: TextView) {
        when (step) {
            0 -> {
                Log.d(TAG, "Instructions")
                p.text = resources.getString(R.string.q1)
            }
            33 -> {
                Log.d(TAG, "Permission request prompt")
                p.text = resources.getString(R.string.q2)
            }
            66 -> {
                setupPermissions()

                Log.d(TAG, "Reminder prompt")
                p.text = resources.getString(R.string.q3)
            }
            99 -> {
                // reminder frequency picker
                val items = arrayOf("Everyday", "Every other day", "Weekly",
                    "Bi-Weekly", "Monthly")
                val builder = AlertDialog.Builder(this)
                with (builder) {
                    setTitle("Pick reminder frequency")
                    setItems(items) { _, freq ->
                        Log.d(TAG, "Frequency picked: $freq")

                        when (freq) {
                            0 -> editor.putInt("REMINDER_FREQ", 1)
                            1 -> editor.putInt("REMINDER_FREQ", 2)
                            2 -> editor.putInt("REMINDER_FREQ", 7)
                            3 -> editor.putInt("REMINDER_FREQ", 14)
                            4 -> editor.putInt("REMINDER_FREQ", 30)
                            else -> {
                                Log.d(TAG, "FREQUENCY PICKER ERROR: Auto selecting 7 days")
                                editor.putInt("REMINDER_FREQ", 7)
                            }
                        }

                        editor.apply()
                    }
                    show()
                }

                // check if all permissions are granted
                var permissionError = false
                for (permission in permissionsNeeded) {
                    if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                        permissionError = true
                    }
                }

                // warn user if permission is not granted
                if (permissionError) {
                    Log.w(TAG, "user did not provide all permissions")
                    p.text = resources.getString(R.string.permission_error)
                } else {
                    Log.d(TAG, "setup completed")
                    p.text = resources.getString(R.string.q4)
                }

                editor.putBoolean("FIRST", false)
            }
            else -> {
                Log.d(TAG, "starting ContactManagerActivity in whitelist mode")
                editor.putBoolean("WHITELISTING_MODE", true)
                editor.putInt("FRAGMENT", R.id.call)
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    // --- Permission Handling ---
    private fun setupPermissions() {
        // ask the first time
        for (permission in permissionsNeeded) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, permissionsNeeded, REQUEST_CODE)
            }
        }
    }

    public override fun onDestroy() {
        super.onDestroy()
    }

    companion object {
        private val TAG = "CYM-Debug"
        private val REQUEST_CODE = 69

        private val PREF_FILE = "net.luan.cym.prefs"
    }
}