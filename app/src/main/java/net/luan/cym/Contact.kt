package net.luan.cym

import android.util.Log
import java.time.LocalDate
import java.util.*

// Contact class takes in multiple variables
// name:            contact's name
// phone:           contact's phone number
// freq:            how often they have contacted the person within the last __ weeks
// last_contacted:  the date they were last contacted
//                  how to create a LocalDate: LocalDate.of(2020, Month.January, 1)
// alert_pref:      the frequency to remind the user to contact their contact
// whitelisted:     shows if contact in the contact list was whitelisted
//
// Note: the internal keyword was removed so that the Contact ArrayList could be stored in sharedPref
// Note: phone number was changed to long to avoid NumberFormatException
class Contact (name: String, phone: Long, freq: Int, last_contacted: LocalDate,
                        alert_pref: Int, whitelisted: Boolean){

    // variables (ignore the warnings, this is redundant for now
    var name: String = name
    var phone: Long = phone
    var freq: Int = freq
    var last_contacted: LocalDate = last_contacted
    var alert_pref: Int = alert_pref
    var whitelisted: Boolean = whitelisted

    // initializer
    init {
        /*
        Log.i(TAG, "Contact created: ${this.name} (${this.phone})" +
                "\n\t Freq: ${this.freq}, Last contacted: ${this.last_contacted}" +
                "\n\t Alerts to be sent every: ${this.alert_pref} days" +
                "\t [${this.whitelisted}]")
         */
    }

    override fun toString(): String {
        // last_contacted should produce in this format: YYYY-MM-DD (e.g. 2020-01-01)
        return "${this.name} (${this.phone}) has been contacted ${this.freq} times." +
                "\n\tLast contacted: ${this.last_contacted}. Alerts sent every: ${this.alert_pref} days" +
                "\t [${this.whitelisted}]"
    }

    companion object {
        private val TAG = "CYM-Debug"
    }
}