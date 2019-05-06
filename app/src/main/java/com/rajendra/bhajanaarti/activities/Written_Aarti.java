package com.rajendra.bhajanaarti.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.rajendra.bhajanaarti.R;


public class Written_Aarti extends AppCompatActivity {
    String[] mobileArray = {"गणेश आरती-(Ganesh Aarti)","कृष्ण आरती- (Krishna Aarti)","संतोषी माता की आरती- (Santoshi Mata Aarti)","श्री साईं बाबा की आरती- (Sai Baba Aarti)",
           "शनि देवजी की आरती- (Shani Dev Aarti)","दुर्गा जी की आरती- (Durga Aarti)","कालीमाता आरती- (Kali Mata Aarti)","लक्ष्मीजी की आरती- (Laxmi Mata Aarti)","शिव आरती- (Shiv Aarti)","बृहस्पति देवता की आरती- (Brihaspati Aarti)","जय अम्बे गौरी मैया जय श्यामा गौरी- (Jai ambe gauri maiya jai shyama gauri)","दुर्गे दुर्घट भारी- (Durge Durghat Bhari)","प्रार्थना- (Prayer)","पपुष्पांजली- (Mantra pushpanjali)"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_written__aarti);

        AdView mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        ArrayAdapter adapter = new ArrayAdapter<String>(this,
                R.layout.activity_written__aarti__adapter, mobileArray);


            final ListView listView = (ListView) findViewById(R.id.Aarti_list);

            listView.setAdapter(adapter);
        listView.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


                // selected item
                String product = ((TextView) view).getText().toString();
                // Launching new Activity on selecting single List Item
                Intent i = new Intent(getApplicationContext(), Written_Aarti_Text.class);
                // sending data to new activity
                i.putExtra("product", product);

                startActivity(i);

            }
        });
    }
}
