package com.rajendra.bhajanaarti.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemClickListener
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.TextView

import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.rajendra.bhajanaarti.R


class Written_Aarti : AppCompatActivity() {
    internal var mobileArray = arrayOf("गणेश आरती-(Ganesh Aarti)", "कृष्ण आरती- (Krishna Aarti)",
            "संतोषी माता की आरती- (Santoshi Mata Aarti)", "श्री साईं बाबा की आरती- (Sai Baba Aarti)",
            "शनि देवजी की आरती- (Shani Dev Aarti)", "दुर्गा जी की आरती- (Durga Aarti)",
            "कालीमाता आरती- (Kali Mata Aarti)", "लक्ष्मीजी की आरती- (Laxmi Mata Aarti)", "शिव आरती- (Shiv Aarti)",
            "बृहस्पति देवता की आरती- (Brihaspati Aarti)", "जय अम्बे गौरी मैया जय श्यामा गौरी- (Jai ambe gauri maiya jai shyama gauri)",
            "दुर्गे दुर्घट भारी- (Durge Durghat Bhari)", "प्रार्थना- (Prayer)", "पपुष्पांजली- (Mantra pushpanjali)")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_written__aarti)

        val mAdView = findViewById<View>(R.id.adView) as AdView
        val adRequest = AdRequest.Builder().build()
        mAdView.loadAd(adRequest)

        val adapter = ArrayAdapter(this,
                R.layout.activity_written__aarti__adapter, mobileArray)


        val listView = findViewById<View>(R.id.Aarti_list) as ListView

        listView.adapter = adapter
        listView.onItemClickListener = OnItemClickListener { parent, view, position, id ->
            // selected item
            val product = (view as TextView).text.toString()
            // Launching new Activity on selecting single List Item
            val i = Intent(applicationContext, Written_Aarti_Text::class.java)
            // sending data to new activity
            i.putExtra("product", product)

            startActivity(i)
        }
    }
}
