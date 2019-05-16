package com.rajendra.bhajanaarti.activities;

import android.app.Activity;
import android.content.Intent;
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
import com.rajendra.bhajanaarti.constants.Constant;
import com.rajendra.bhajanaarti.utils.Utilities;

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
    private static final String TAG = "MusicPlayerActivity";
    int indexOfSong;
    private Handler mHandler = new Handler();
    private boolean isActivityVisible;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_music_player);

        initializer();
    }

    private void initializer(){
        mAdView = findViewById(R.id.adView);
        loadAd();

        tvSongTitle = findViewById(R.id.tvSongTitle);
        songTotalDurationLabel = findViewById(R.id.songTotalDurationLabel);
        songCurrentDurationLabel = findViewById(R.id.songCurrentDurationLabel);
        btnPlay = findViewById(R.id.btnPlay);
        btnPlay.setOnClickListener(this);
        btnForward = findViewById(R.id.btnForward);
        btnForward.setOnClickListener(this);
        btnBackward = findViewById(R.id.btnBackward);
        btnBackward.setOnClickListener(this);
        btnNext = findViewById(R.id.btnNext);
        btnNext.setOnClickListener(this);
        btnPrevious = findViewById(R.id.btnPrevious);
        btnPrevious.setOnClickListener(this);
        btnRepeat = findViewById(R.id.btnRepeat);
        btnRepeat.setOnClickListener(this);
        songProgressBar = findViewById(R.id.songProgressBar);
        songProgressBar.setProgress(0);
        songProgressBar.setOnSeekBarChangeListener(this);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            songIndex = bundle.getString("songindex");
        }
        Log.d(TAG, "song_index " + songIndex);
        indexOfSong = Integer.parseInt(songIndex);
        utils = new Utilities();
        playSongIndex(indexOfSong);
        mUpdateTimeTask.run();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart_called");
        isActivityVisible = true;
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG, "onPause_called");
        isActivityVisible = false;
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d(TAG, "onResume_called");
        isActivityVisible = true;
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy_called");
        isActivityVisible = false;
    }

    private Runnable mUpdateTimeTask = new Runnable() {
        public void run() {
            if (mp != null){
                long totalDuration = mp.getDuration();
                long currentDuration = mp.getCurrentPosition();

                // Displaying Total Duration time
            /*Log.d(TAG,"totalDuration_val " + utils.milliSecondsToTimer(totalDuration));
            songTotalDurationLabel.setText(""+utils.milliSecondsToTimer(totalDuration));*/

                // Displaying time completed playing
                Log.d(TAG, "Song current Duration: " + utils.milliSecondsToTimer(currentDuration));
                songCurrentDurationLabel.setText(utils.milliSecondsToTimer(currentDuration));

                if (isActivityVisible){
                    if (mp != null){
                        if (!mp.isPlaying()){
                            btnPlay.setImageResource(R.drawable.btn_play);
                        }
                    }
                }

                int progress = (int)(utils.getProgressPercentage(currentDuration, totalDuration));
                //Log.d("Progress", ""+progress);
                songProgressBar.setProgress(progress);

                // Running this thread after 1000 milliseconds
                mHandler.postDelayed(this, 1000);
            }
            else {
                Log.d(TAG, "finish_called");
                finish();
            }
        }
    };
    public void updateProgressBar()
    {
        mHandler.postDelayed(mUpdateTimeTask, 1000);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnPlay:
                loadAd();
                if(mp.isPlaying()) {
                    if(mp != null) {
                        mp.pause();
                        btnPlay.setImageResource(R.drawable.btn_play);
                    }
                }
                else {
                    if (mp != null) {
                        mp.start();
                        btnPlay.setImageResource(R.drawable.btn_pause);
                    }
                }
                break;

            case R.id.btnForward:
                int currentPosition = mp.getCurrentPosition();
                // check if seekForward time is lesser than song duration
                if(currentPosition + seekForwardTime <= mp.getDuration()) {
                    // forward song
                    mp.seekTo(currentPosition + seekForwardTime);
                }
                else {
                    // forward to end position
                    mp.seekTo(mp.getDuration());
                }
                break;

            case R.id.btnBackward:
                int currentPosition1 = mp.getCurrentPosition();
                // check if seekBackward time is greater than 0 sec
                if(currentPosition1 - seekBackwardTime >= 0) {
                    // forward song
                    mp.seekTo(currentPosition1 - seekBackwardTime);
                }
                else {
                    // backward to starting position
                    mp.seekTo(0);
                }
                break;

            case R.id.btnNext:
                if (mp != null)
                    mp.stop();
                loadAd();
                playNextSong();
                break;

            case R.id.btnPrevious:
                if (mp != null)
                    mp.stop();
                loadAd();
                playPrevSong();
                break;

            case R.id.btnRepeat:
                if(isRepeat) {
                    isRepeat = false;
                    Toast.makeText(getApplicationContext(), "Repeat is OFF", Toast.LENGTH_SHORT).show();
                    btnRepeat.setImageResource(R.drawable.btn_repeat);
                }
                else {
                    // make repeat to true
                    isRepeat = true;
                    Toast.makeText(getApplicationContext(), "Repeat is ON", Toast.LENGTH_SHORT).show();
                    btnRepeat.setImageResource(R.drawable.img_btn_repeat_pressed);
                }
                break;
        }
    }

    public void loadAd(){
        if (mAdView != null)
            mAdView.loadAd(new AdRequest.Builder().build());
    }

    public void playNextSong(){
        if (indexOfSong < 14) {
            int nextValue = 1 + indexOfSong;
            indexOfSong++;
            Log.d(TAG, "nextSongIndex: " + nextValue);
            playSongIndex(nextValue);
            btnPlay.setImageResource(R.drawable.btn_pause);
        }
        else {
            playSongIndex(0);
            btnPlay.setImageResource(R.drawable.btn_pause);
            indexOfSong = 0;
        }
    }

    public void playPrevSong(){
        if (indexOfSong > 0) {
            int preValue = indexOfSong - 1;
            indexOfSong--;
            Log.d(TAG, "prevSongIndex: " + preValue);
            playSongIndex(preValue);
            btnPlay.setImageResource(R.drawable.btn_pause);
        }
        else {
            playSongIndex(14);
            btnPlay.setImageResource(R.drawable.btn_pause);
            indexOfSong = 14;
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
                tvSongTitle.setText("1. Ambey Tu Hai Jagdambey Kali");
                mp = MediaPlayer.create(this, R.raw.ambe_tu_hai_jagdambe_kali);
                break;
            case 1:
                tvSongTitle.setText("2. Bheja Hai Bulava Tune Sherawaliye");
                mp = MediaPlayer.create(this, R.raw.bheja_hai_bulava_tune_sherawaliye);
                break;
            case 2:
                tvSongTitle.setText("3. Bhor Bhai Din Char Gaya Meri Ambe");
                mp = MediaPlayer.create(this, R.raw.bhor_bhai_din_char_gaya_meri_ambe);
                break;
            case 3:
                tvSongTitle.setText("4. Bigdi Meri Bana De");
                mp = MediaPlayer.create(this, R.raw.bigdi_meri_bana_de);
                break;
            case 4:
                tvSongTitle.setText("5. Durga Hai Meri Maa");
                mp = MediaPlayer.create(this, R.raw.durga_hai_meri_maa);
                break;
            case 5:
                tvSongTitle.setText("6. Hey Naam Re Sabse Bada Tera Naam");
                mp = MediaPlayer.create(this, R.raw.hey_naam_re_sabse_bada_tera_naam);
                break;
            case 6:
                tvSongTitle.setText("7. Kabse Khadi Hoon");
                mp = MediaPlayer.create(this, R.raw.kabse_khadi_hoon);
                break;
            case 7:
                tvSongTitle.setText("8. Maa Sun Le Pukar");
                mp = MediaPlayer.create(this, R.raw.maa_sun_le_pukar);
                break;
            case 8:
                tvSongTitle.setText("9. Maiya Ka Chola Hai Rangla");
                mp = MediaPlayer.create(this, R.raw.maiya_ka_chola_rangla);
                break;
            case 9:
                tvSongTitle.setText("10. Maiya Main Nihaal Ho Gaya");
                mp = MediaPlayer.create(this, R.raw.maiya_main_nihaal_ho_gaya);
                break;
            case 10:
                tvSongTitle.setText("11. Meri Akhiyon Ke Samne Hi Rehna");
                mp = MediaPlayer.create(this, R.raw.meri_akhiyon_ke_samne_hi_rehna);
                break;
            case 11:
                tvSongTitle.setText("12. Meri Jholi Chhoti Pad Gayee Re");
                mp = MediaPlayer.create(this, R.raw.meri_jholi_chhoti_pa_gayee_re);
                break;
            case 12:
                tvSongTitle.setText("13. Na Main Mangu Sona Devi Bhajan");
                mp = MediaPlayer.create(this, R.raw.na_main_mangu_sona_devi);
                break;
            case 13:
                tvSongTitle.setText("14. Pyara Saja Hai Tera Dwar");
                mp = MediaPlayer.create(this, R.raw.pyara_saja_hai_tera_dwar);
                break;
            case 14:
                tvSongTitle.setText("15. Suno Suno Ek Kahani");
                mp = MediaPlayer.create(this, R.raw.suno_suno_ek_kahani);
                break;
        }

        if (mp != null){
            mp.start();
            Utilities.playing = true;
            mp.setOnCompletionListener(this);
            long totalDuration = mp.getDuration();

            songTotalDurationLabel.setText(utils.milliSecondsToTimer(totalDuration));
            Log.d(TAG, "Song_total_duration " + utils.milliSecondsToTimer(totalDuration));
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        Constant.NOW_PLAYING_SONG_NAME = tvSongTitle.getText().toString();
        startActivity(intent);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        //Log.d(TAG, "onProgressChanged");
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {
        Log.d(TAG, "onStartTrackingTouch");
        mHandler.removeCallbacks(mUpdateTimeTask);
    }
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        Log.d(TAG, "onStopTrackingTouch");
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
        Log.d(TAG,"Completed_Song_index " + indexOfSong);
        if(isRepeat) {
            playSongIndex(indexOfSong);
        }
        else {
            playNextSong();
        }
    }
}
