package net.luan.cym.ui

import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import kotlinx.android.synthetic.main.fragment_stats.*
import net.luan.cym.Contact
import net.luan.cym.MainActivity.Companion.callLogContacts
import net.luan.cym.MainActivity.Companion.gson
import net.luan.cym.MainActivity.Companion.saveAndReturnContactList

import net.luan.cym.R
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter


// Charts used are found on the following github: https://github.com/PhilJay/MPAndroidChart


class StatsFragment : Fragment() {

    // This hashMap is of type Float because it's what the PieChart Library requires... this
    // MIGHT cause issues when we get all the call log data as I assume those will be ints.
    // We definitely will want to figure that out and prevent type issues
    var hashMap : HashMap<String, Float>
            = HashMap()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val inflated = inflater.inflate(R.layout.fragment_stats, container, false)
        //val spin = inflated.findViewById<View>(R.id.spinnerChart) as Spinner
        return inflated
    }

    // Had to make this function and move all the hashMap additions and what not to here, before
    // I was doing it in onCreateView() and my Ids (like piechart) were returning null because they
    // it is apparently not type-safe to do all your work in there.
    
    override fun onActivityCreated(savedInstanceState: Bundle?) {

        // ---- This part was from Hamid
        super.onActivityCreated(savedInstanceState)
        val temp = saveAndReturnContactList("Contacts")
        var i = 0
        var contact: Contact

        // Populate the hashMap for statistics
        while (i < temp.size() && i < callLogContacts.size) {
            contact =  gson.fromJson(temp.get(i), Contact::class.java)
            Log.i(TAG, contact.toString())
            hashMap.put(contact.name.toString(), contact.freq.toFloat())
            i++
        }

        // ---- End part from Hamid

        Log.i("Spinner value", spinnerChart.selectedItem.toString())


        // TODO: This is supposed to let us choose between what type of chart we want... Try to
        // work on the "invisible" stuff and see if that works. Also figure out the barchart BS

        // initialize the spinner options
        val chartNames = arrayOf("Pie", "Bar")
        val arrayAdapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, chartNames)
        spinnerChart.adapter = arrayAdapter

        // Do constant checks to verify which spinner selection is chosen
        spinnerChart.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {

                Toast.makeText(context!!, chartNames[position], Toast.LENGTH_SHORT).show()
                if (chartNames[position]=="Pie") {
                    setupPieChart(hashMap)
                    Log.i("In Spinner", "Setting up Pie Chart")
                    // Set the pichart visible and the barchart to invisible
                    piechart.visibility = View.VISIBLE
                    barchart.visibility = View.INVISIBLE
                } else {
                    Log.i("In spinner selector", "Bar was chosen")
                    piechart.visibility = View.INVISIBLE
                    barchart.visibility = View.VISIBLE
                    setupBarChart(hashMap)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                Log.i("Spinner", "Nothing was selected")
            }
        }


    }

    // https://www.youtube.com/watch?v=iS7EgKnyDeY <-- This video was how I learned to do this
    // I suggest you watch it if you want to learn more about the setup and all

    private fun setupPieChart(map : HashMap<String, Float>) {
        val pieEntries = arrayListOf<PieEntry>()

        // PieCharts with this library require data entries with the following typing (float, string)
        // for each contact in the Hashmap add the frequency of contact (Value) and the name (Contact)
        // to pieEntries
        map.forEach{
            (contact, value) -> pieEntries.add( PieEntry (value, contact))
        }

        // Initalize the piechart dataset
        val dataSet = PieDataSet(pieEntries, "Phone calls made")
        // set the piechart to use a non-monotonous display
        dataSet.setColors(*ColorTemplate.COLORFUL_COLORS)
        val data = PieData(dataSet)
        data.setValueTextSize(10f)

        if (piechart == null) {
            Log.i("In SetupPieChart", "NULL")
        } else {
            // Actually setup the piechart now
            Log.i("In SetupPieChart", "Not NULL")
            piechart.setData(data)
            piechart.setNoDataTextColor(0)
            piechart.setCenterText("Call frequency")
            piechart.setCenterTextSize(20.0f)
            piechart.invalidate()
        }

    }

    // TODO: Figure out how to do this stupid bar chart. What the heck is this crap
    // TODO: https://github.com/PhilJay/MPAndroidChart/wiki/Setting-Data <-- Use this link

    private fun setupBarChart(map : HashMap<String, Float>) {
        val barEntry = arrayListOf<BarEntry>();
        val axis = arrayListOf<String>()
        var count : Float = 0f

        // BarCharts with this library require data entries with the following typing (float, float)
        // for each contact in the Hashmap add the position for the barchart (count) and the times contacted (value)
        // to pieEntries
        map.forEach{ (contact, value) -> barEntry.add( BarEntry (count++, value))}

        // Get a list of contacts for the X-axis
        map.forEach{ (contact, _) -> axis.add(contact) }

        // Initalize the barchart dataset
        val dataSet = BarDataSet(barEntry, "BarDataSet")

        // set the barchart to use a non-monotonous display
        dataSet.setColors(*ColorTemplate.COLORFUL_COLORS)
        val data = BarData(dataSet)


        if (barchart == null) {
            Log.i("In SetupBarChart", "NULL")
        } else {
            Log.i("In SetupBarChart", "Not NULL")
            barchart.setData(data)
            barchart.setFitBars(true)
            barchart.xAxis.valueFormatter = IndexAxisValueFormatter(axis)
            barchart.xAxis.position = XAxis.XAxisPosition.BOTTOM
            barchart.invalidate()
        }
    }

    companion object {
        private val TAG = "CYM-Debug"
    }
}
