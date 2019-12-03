package net.luan.cym

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    private lateinit var sharedPref: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor

    private var mDelay: Handler? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        mDelay = Handler()
        mDelay!!.postDelayed(mRunnable, DELAY)
    }

    public override fun onDestroy() {
        if (mDelay != null) {
            mDelay!!.removeCallbacks(mRunnable)
        }

        super.onDestroy()
    }

    private val mRunnable = Runnable {
        if (!isFinishing) {
            var intent: Intent

            // SharedPreference handler
            sharedPref = applicationContext.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE)
            editor = sharedPref.edit()

            if (sharedPref.getBoolean("FIRST", true)) {
                Log.d(TAG, "Going to SetupActivity")
                intent = Intent(applicationContext, SetupActivity::class.java)
            } else {
                Log.d(TAG, "Going to ContactManagerActivity")
                intent = Intent(applicationContext, MainActivity::class.java)
                editor.putInt("FRAGMENT", R.id.call)
            }

            // --- DEBUG ---
            // uncomment this to always show setup
            // intent = Intent(applicationContext, SetupActivity::class.java)

            startActivity(intent)
            finish()
        }
    }

    companion object {
        private val TAG = "CYM-Debug"

        private val PREF_FILE = "net.luan.cym.prefs"
        private val DELAY: Long = 1000
    }
}