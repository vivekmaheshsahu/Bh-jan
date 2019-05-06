package com.rajendra.bhajanaarti.activities;

import android.app.Activity;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import com.rajendra.bhajanaarti.R;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.rajendra.bhajanaarti.Utility.Utilities;

public class MusicPlayerActivity extends Activity implements SeekBar.OnSeekBarChangeListener, View.OnClickListener, MediaPlayer.OnCompletionListener {

    private AdView mAdView;
    private TextView tvSongTitle;
    private ImageButton btnPlay;
    private ImageButton btnForward;
    private ImageButton btnBackward;
    private ImageButton btnNext;
    private ImageButton btnPrevious;
    private ImageButton btnRepeat;
    private SeekBar songProgressBar;
    private TextView songCurrentDurationLabel;
    private TextView songTotalDurationLabel;
    private Utilities utils;
    private int seekForwardTime = 5000; // 5000 milliseconds
    private int seekBackwardTime = 5000;
    private boolean isRepeat = false;
    public static MediaPlayer mp;
    String songIndex;
    private static final String TAG = "MainActivity";
    int indexOfSong;
    private Handler mHandler = new Handler();;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        tvSongTitle = (TextView) findViewById(R.id.tvSongTitle);
        songTotalDurationLabel = (TextView) findViewById(R.id.songTotalDurationLabel);
        songCurrentDurationLabel = (TextView) findViewById(R.id.songCurrentDurationLabel);
        utils = new Utilities();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null)
        {
            songIndex = bundle.getString("songindex");
        }
        Log.d(TAG, "song_index " + songIndex);

        indexOfSong = Integer.parseInt(songIndex);

        setSongTitle(indexOfSong);
        playSongIndex(indexOfSong);

        mp.setOnCompletionListener(this);


        btnPlay = (ImageButton) findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(this);

        btnForward = (ImageButton) findViewById(R.id.btnForward);
        btnForward.setOnClickListener(this);

        btnBackward = (ImageButton) findViewById(R.id.btnBackward);
        btnBackward.setOnClickListener(this);

        btnNext = (ImageButton) findViewById(R.id.btnNext);
        btnNext.setOnClickListener(this);

        btnPrevious = (ImageButton) findViewById(R.id.btnPrevious);
        btnPrevious.setOnClickListener(this);

        btnRepeat = (ImageButton) findViewById(R.id.btnRepeat);
        btnRepeat.setOnClickListener(this);

        songProgressBar = (SeekBar) findViewById(R.id.songProgressBar);
        songProgressBar.setOnSeekBarChangeListener(this);

        songCurrentDurationLabel = (TextView) findViewById(R.id.songCurrentDurationLabel);
        songTotalDurationLabel = (TextView) findViewById(R.id.songTotalDurationLabel);

        mUpdateTimeTask.run();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private Runnable mUpdateTimeTask = new Runnable()
    {
        public void run()
        {
            if (mp != null){
                long totalDuration = mp.getDuration();
                long currentDuration = mp.getCurrentPosition();

                // Displaying Total Duration time
            /*Log.d(TAG,"totalDuration_val " + utils.milliSecondsToTimer(totalDuration));
            songTotalDurationLabel.setText(""+utils.milliSecondsToTimer(totalDuration));*/

                // Displaying time completed playing
                Log.d(TAG, "currentDuration_val " + utils.milliSecondsToTimer(currentDuration));
                songCurrentDurationLabel.setText(utils.milliSecondsToTimer(currentDuration));

                int progress = (int)(utils.getProgressPercentage(currentDuration, totalDuration));
                //Log.d("Progress", ""+progress);
                songProgressBar.setProgress(progress);

                // Running this thread after 1000 milliseconds
                mHandler.postDelayed(this, 1000);
            }
            else
            {
                finish();
            }
        }
    };

    public void updateProgressBar()
    {
        mHandler.postDelayed(mUpdateTimeTask, 1000);
    }



    @Override
    public void onClick(View v)
    {
        if (v == btnPlay)
        {
            mAdView.loadAd(new AdRequest.Builder().build());

            if(mp.isPlaying())
            {
                if(mp != null)
                {
                    mp.pause();
                    btnPlay.setImageResource(R.drawable.btn_play);
                }
            }
            else
            {
                if (mp != null)
                {
                    mp.start();
                    btnPlay.setImageResource(R.drawable.btn_pause);
                }
            }
        }

        if (v == btnForward)
        {
            int currentPosition = mp.getCurrentPosition();
            // check if seekForward time is lesser than song duration
            if(currentPosition + seekForwardTime <= mp.getDuration())
            {
                // forward song
                mp.seekTo(currentPosition + seekForwardTime);
            }
            else
            {
                // forward to end position
                mp.seekTo(mp.getDuration());
            }
        }
        if (v == btnBackward)
        {
            int currentPosition = mp.getCurrentPosition();
            // check if seekBackward time is greater than 0 sec
            if(currentPosition - seekBackwardTime >= 0)
            {
                // forward song
                mp.seekTo(currentPosition - seekBackwardTime);
            }
            else
            {
                // backward to starting position
                mp.seekTo(0);
            }
        }
        if (v == btnNext)
        {
            if (mp != null)
                mp.stop();

            Log.d("btnNext","indexOfSong_val " + indexOfSong);

            mAdView.loadAd(new AdRequest.Builder().build());

            if (indexOfSong < 15)
            {
                int addSongIndex = 1;
                int nextValue = addSongIndex + indexOfSong;
                Log.d("btnNext", "nextValue_val " + nextValue );

                indexOfSong++;
                Log.d("btnNext","indexOfSong_new_val " + indexOfSong);

                setSongTitle(nextValue);
                playSongIndex(nextValue);
                btnPlay.setImageResource(R.drawable.btn_pause);
            }
            else
            {
                setSongTitle(0);
                playSongIndex(0);
                btnPlay.setImageResource(R.drawable.btn_pause);
                indexOfSong = 0;
            }
        }
        if (v == btnPrevious)
        {
            if (mp != null)
                mp.stop();

            Log.d("btnPrevious","indexOfSong_val " + indexOfSong);

            mAdView.loadAd(new AdRequest.Builder().build());

            if (indexOfSong > 0)
            {
                int subtractSongIndex = 1;
                int preValue = indexOfSong - subtractSongIndex;
                Log.d("btnPrevious", "nextValue_val " + preValue);

                indexOfSong--;
                Log.d("btnPrevious","indexOfSong_new_val " + indexOfSong);

                setSongTitle(preValue);
                playSongIndex(preValue);
                btnPlay.setImageResource(R.drawable.btn_pause);
            }
            else
            {
                setSongTitle(15);
                playSongIndex(15);
                btnPlay.setImageResource(R.drawable.btn_pause);
                indexOfSong = 15;
            }
        }
        if (v == btnRepeat)
        {
            if(isRepeat)
            {
                isRepeat = false;
                Toast.makeText(getApplicationContext(), "Repeat is OFF", Toast.LENGTH_SHORT).show();
                btnRepeat.setImageResource(R.drawable.btn_repeat);
            }
            else
            {
                // make repeat to true
                isRepeat = true;
                Toast.makeText(getApplicationContext(), "Repeat is ON", Toast.LENGTH_SHORT).show();

                btnRepeat.setImageResource(R.drawable.img_btn_repeat_pressed);
            }
            //Toast.makeText(this, "We will add this feature in next version", Toast.LENGTH_SHORT).show();

        }
    }



    public void setSongTitle(int titleIndex)
    {
        if (titleIndex == 0)
        {
            tvSongTitle.setText("Aarti Ambe Tu Hai Jagdambe Kali");
        }
        if (titleIndex == 1)
        {
            tvSongTitle.setText("Bhajan Ambey Tu Hai Jagdambey Kali");
        }
        if (titleIndex == 2)
        {
            tvSongTitle.setText("Bheja Hai Bulava Tune Sherawaliye");
        }
        if (titleIndex == 3)
        {
            tvSongTitle.setText("Bhor Bhai Din Char Gaya Meri Ambe");
        }
        if (titleIndex == 4)
        {
            tvSongTitle.setText("Bigdi Meri Bana De");
        }
        if (titleIndex == 5)
        {
            tvSongTitle.setText("Durga Hai Meri Maa");
        }
        if (titleIndex == 6)
        {
            tvSongTitle.setText("Hey Naam Re Sabse Bada Tera Naam");
        }
        if (titleIndex == 7)
        {
            tvSongTitle.setText("Kabse Khadi Hoon");
        }
        if (titleIndex == 8)
        {
            tvSongTitle.setText("Maa Sun Le Pukar By Gulshan Kumar");
        }
        if (titleIndex == 9)
        {
            tvSongTitle.setText("Maiya Ka Chola Hai Rangla");
        }
        if (titleIndex == 10)
        {
            tvSongTitle.setText("Maiya Main Nihaal Ho Gaya");
        }
        if (titleIndex == 11)
        {
            tvSongTitle.setText("Meri Akhiyon Ke Samne Hi Rehna");
        }
        if (titleIndex == 12)
        {
            tvSongTitle.setText("Meri Jholi Chhoti Pad Gayee Re");
        }
        if (titleIndex == 13)
        {
            tvSongTitle.setText("Na Main Mangu Sona Devi Bhajan");
        }
        if (titleIndex == 14)
        {
            tvSongTitle.setText("Pyara Saja Hai Tera Dwar");
        }
        if (titleIndex == 15)
        {
            tvSongTitle.setText("Suno Suno Ek Kahani");
        }
    }

    public void playSongIndex(int index)
    {
        if (mp != null){
            mp.stop();
            mp.release();
        }

        switch (index){
            case 0:
                mp = MediaPlayer.create(this, R.raw.aarti_ambe_tu_hai_jagdambe_kali);
                break;
            case 1:
                mp = MediaPlayer.create(this, R.raw.bhajan_ambey_tu_hai_jagdambey_kali);
                break;
            case 2:
                mp = MediaPlayer.create(this, R.raw.bheja_hai_bulava_tune_sherawaliye);
                break;
            case 3:
                mp = MediaPlayer.create(this, R.raw.bhor_bhai_din_char_gaya_meri_ambe);
                break;
            case 4:
                mp = MediaPlayer.create(this, R.raw.bigdi_meri_bana_de);
                break;
            case 5:
                mp = MediaPlayer.create(this, R.raw.durga_hai_meri_maa);
                break;
            case 6:
                mp = MediaPlayer.create(this, R.raw.hey_naam_re_sabse_bada_tera_naam);
                break;
            case 7:
                mp = MediaPlayer.create(this, R.raw.kabse_khadi_hoon);
                break;
            case 8:
                mp = MediaPlayer.create(this, R.raw.maa_sun_le_pukar);
                break;
            case 9:
                mp = MediaPlayer.create(this, R.raw.maiya_ka_chola_rangla);
                break;
            case 10:
                mp = MediaPlayer.create(this, R.raw.maiya_main_nihaal_ho_gaya);
                break;
            case 11:
                mp = MediaPlayer.create(this, R.raw.meri_akhiyon_ke_samne_hi_rehna);
                break;
            case 12:
                mp = MediaPlayer.create(this, R.raw.meri_jholi_chhoti_pa_gayee_re);
                break;
            case 13:
                mp = MediaPlayer.create(this, R.raw.na_main_mangu_sona_devi);
                break;
            case 14:
                mp = MediaPlayer.create(this, R.raw.pyara_saja_hai_tera_dwar);
                break;
            case 15:
                mp = MediaPlayer.create(this, R.raw.suno_suno_ek_kahani);
                break;
        }

        if (mp != null){
            mp.start();
            mp.setOnCompletionListener(this);
            long totalDuration = mp.getDuration();

            songTotalDurationLabel.setText(utils.milliSecondsToTimer(totalDuration));
            Log.d(TAG, "total_val " + utils.milliSecondsToTimer(totalDuration));
        }
    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser)
    {

    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar)
    {
        mHandler.removeCallbacks(mUpdateTimeTask);
    }
    @Override
    public void onStopTrackingTouch(SeekBar seekBar)
    {
        mHandler.removeCallbacks(mUpdateTimeTask);
        int totalDuration = mp.getDuration();
        int currentPosition = utils.progressToTimer(seekBar.getProgress(), totalDuration);

        // forward or backward to certain seconds
        mp.seekTo(currentPosition);
        // update timer progress again
        updateProgressBar();
    }

    @Override
    public void onCompletion(MediaPlayer mp)
    {
        Log.d("onCompletion","Completed_Song_index " + indexOfSong);

        // check for repeat is ON or OFF
        if(isRepeat)
        {
            // repeat is on play same song again
            playSongIndex(indexOfSong);
        }
        else
        {
            if (indexOfSong < 15)
            {
                int addSongIndex = 1;
                int nextValue = addSongIndex + indexOfSong;
                Log.d("onCompletion", "nextValue_val " + nextValue );

                indexOfSong++;
                Log.d("onCompletion","indexOfSong_new_val " + indexOfSong);

                setSongTitle(nextValue);
                playSongIndex(nextValue);
                btnPlay.setImageResource(R.drawable.btn_pause);
            }
            else
            {
                setSongTitle(0);
                playSongIndex(0);
                btnPlay.setImageResource(R.drawable.btn_pause);
                indexOfSong = 0;
            }
        }
    }


}
