package net.luan.cym

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.TextView
import androidx.core.app.ActivityCompat

class SetupActivity : AppCompatActivity() {
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var sharedPref: SharedPreferences

    // define permissions needed
    private val permissionsNeeded = arrayOf(Manifest.permission.READ_CALL_LOG,
        Manifest.permission.READ_CONTACTS, Manifest.permission.CALL_PHONE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup)
        Log.i(TAG, "--- Entered first time setup ---")

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

        Log.d(TAG, "done")
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

                Log.d(TAG, "Suggesting contacts")
                p.text = resources.getString(R.string.q3)
            }
            99 -> {
                Log.d(TAG, "Completed")
                p.text = resources.getString(R.string.q4)
            }
            else -> {
                val intent = Intent(applicationContext, MainActivity::class.java)
                startActivity(intent)
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

        private val PREF_FILE = "net.luan.cym.prefs"
        private val REQUEST_CODE = 69
    }
}