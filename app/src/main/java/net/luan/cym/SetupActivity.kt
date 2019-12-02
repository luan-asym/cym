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
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat

class SetupActivity : AppCompatActivity() {
    private lateinit var editor: SharedPreferences.Editor
    private lateinit var sharedPref: SharedPreferences

    // define permissions needed
    val permissionsNeeded = arrayOf(Manifest.permission.READ_CALL_LOG,
        Manifest.permission.READ_CONTACTS, Manifest.permission.CALL_PHONE)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_setup)
        Log.i(TAG, "--- Entered first time setup ---")

        sharedPref = applicationContext.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE)
        editor = sharedPref.edit()

        // UI components
        val skip = findViewById<Button>(R.id.skip)
        val progress = findViewById<ProgressBar>(R.id.setup_progress)

        // start the initial setup questions
        //  0% -> welcome screen asking for permissions
        // 25% -> requesting permissions
        // 50% -> How often do you want to be reminded to contact your contact
        // 75% ->
        progress.progress = 0
        when (progress.progress) {
            0 -> {
                Log.d(TAG, "Instructions")
                progress.progress = 25
            }
            25 -> {

            }
        }



        // skip button changes shared preferences to skip first-time setup next time
        skip.setOnClickListener {
            Log.d(TAG, "skipped setup")
            progress.progress = 100
            editor.putBoolean("FIRST", false)
            editor.apply()
        }

        Log.d(TAG, "done")
    }

    // --- Permission Handling ---
    private fun setupPermissions() {
        for (permission in permissionsNeeded) {
            var granted = ContextCompat.checkSelfPermission(this, permission)

            if (granted != PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "$permission not granted")
            }
        }
    }

    private fun makeRequest() {
        ActivityCompat.requestPermissions(this, permissionsNeeded, REQUEST_CODE)
    }


    public override fun onDestroy() {
        super.onDestroy()
    }


    companion object {
        private val TAG = "CYM-Debug"

        private val PREF_FILE = "net.luan.cym.prefs"
        private val REQUEST_CODE = 10
    }
}