package net.luan.cym

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {
    private var mDelay: Handler? = null
    private lateinit var sharedPref: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        sharedPref = applicationContext.getSharedPreferences(PREF_FILE, Context.MODE_PRIVATE)

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

            if (sharedPref.getBoolean("FIRST", true)) {
                Log.d(TAG, "Going to SetupActivity")
                intent = Intent(applicationContext, SetupActivity::class.java)
            } else {
                Log.d(TAG, "Going to MainActivity")
                intent = Intent(applicationContext, MainActivity::class.java)
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
        private val DELAY: Long = 1000

        private val PREF_FILE = "net.luan.cym.prefs"
    }
}