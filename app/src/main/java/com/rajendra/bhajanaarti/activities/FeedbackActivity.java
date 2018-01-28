package com.rajendra.bhajanaarti.activities;



import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.VisibleForTesting;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.rajendra.bhajanaarti.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

public class FeedbackActivity extends AppCompatActivity implements View.OnClickListener {


    Button btn_feedback;
    EditText etMail_feedback_message;
    String emailMessege;
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);


        etMail_feedback_message = (EditText) findViewById(R.id.etMail_feedback_message);

        btn_feedback = (Button) findViewById(R.id.btn_feedback);
        btn_feedback.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        if (v == btn_feedback)
        {
            emailMessege = etMail_feedback_message.getText().toString();

            if (emailMessege.equalsIgnoreCase(""))
            {
                Toast.makeText(this, "Please Enter Feedback", Toast.LENGTH_SHORT).show();
            }
            else
            {
                sendFeedback();
            }
        }
    }

    private void sendFeedback()
    {
        final Intent feedbckIntent = new Intent(android.content.Intent.ACTION_SEND);
        feedbckIntent.setType("text/html");
        feedbckIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{ getString(R.string.mail_feedback_email) });
        feedbckIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, getString(R.string.mail_feedback_subject));
        feedbckIntent.putExtra(android.content.Intent.EXTRA_TEXT, emailMessege);
        startActivity(Intent.createChooser(feedbckIntent, getString(R.string.title_send_feedback)));
    }


}
