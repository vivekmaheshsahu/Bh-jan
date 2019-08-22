package com.rajendra.bhajanaarti.activities

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.rajendra.bhajanaarti.R
import com.google.android.gms.ads.AdView
import com.rajendra.bhajanaarti.constants.Constant
import com.rajendra.bhajanaarti.utils.UserInterfaceUtils
import com.rajendra.bhajanaarti.utils.Utilities

class MusicPlayerActivity : AppCompatActivity(), SeekBar.OnSeekBarChangeListener, View.OnClickListener, MediaPlayer.OnCompletionListener {

    private var mAdView: AdView? = null
    private var btnPlay: ImageButton? = null
    private var btnForward: ImageButton? = null
    private var btnBackward: ImageButton? = null
    private var btnNext: ImageButton? = null
    private var btnPrevious: ImageButton? = null
    private var songProgressBar: SeekBar? = null
    private var songCurrentDurationLabel: TextView? = null
    private var songTotalDurationLabel: TextView? = null
    private var utils: Utilities? = null
    private val seekForwardTime = 5000 // 5000 milliseconds
    private val seekBackwardTime = 5000
    private var isRepeat = false
    internal var indexOfSong: Int = 0
    private val mHandler = Handler()
    private var isActivityVisible: Boolean = false

    private val mUpdateTimeTask = object : Runnable {
        override fun run() {
            if (mp != null) {
                val totalDuration = mp?.duration?.toLong()
                val currentDuration = mp?.currentPosition?.toLong()

                // Displaying Total Duration time
                /*Log.d(TAG,"totalDuration_val " + utils.milliSecondsToTimer(totalDuration));
            songTotalDurationLabel.setText(""+utils.milliSecondsToTimer(totalDuration));*/

                // Displaying time completed playing
                Log.d(TAG, "Song current Duration: " + currentDuration?.let { utils?.milliSecondsToTimer(it) })
                songCurrentDurationLabel?.text = currentDuration?.let { utils?.milliSecondsToTimer(it) }

                if (isActivityVisible) {
                    if (mp != null) {
                        if (mp?.isPlaying!!) {
                            btnPlay?.setImageResource(R.drawable.btn_pause)
                        } else
                            btnPlay?.setImageResource(R.drawable.btn_play)
                    }
                }

                val progress = currentDuration?.let { totalDuration?.let { it1 -> utils?.getProgressPercentage(it, it1) } }
                //Log.d("Progress", ""+progress);
                if (progress != null) {
                    songProgressBar?.progress = progress
                }

                // Running this thread after 1000 milliseconds
                mHandler.postDelayed(this, 1000)
            } else {
                Log.d(TAG, "finish_called")
                finish()
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_music_player)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initializer()
    }

    private fun initializer() {
        mAdView = findViewById(R.id.adView)

        songTotalDurationLabel = findViewById(R.id.songTotalDurationLabel)
        songCurrentDurationLabel = findViewById(R.id.songCurrentDurationLabel)
        btnPlay = findViewById(R.id.btnPlay)
        btnPlay?.setOnClickListener(this)
        btnForward = findViewById(R.id.btnForward)
        btnForward?.setOnClickListener(this)
        btnBackward = findViewById(R.id.btnBackward)
        btnBackward?.setOnClickListener(this)
        btnNext = findViewById(R.id.btnNext)
        btnNext?.setOnClickListener(this)
        btnPrevious = findViewById(R.id.btnPrevious)
        btnPrevious?.setOnClickListener(this)
        songProgressBar = findViewById(R.id.songProgressBar)
        songProgressBar?.progress = 0
        songProgressBar?.setOnSeekBarChangeListener(this)

        val mIntent = intent
        if (mIntent != null) {
            indexOfSong = mIntent.getIntExtra("songindex", 0)
        }
        Log.d(TAG, "song_index $indexOfSong")
        utils = Utilities()
        playSongIndex(indexOfSong)
        mUpdateTimeTask.run()
    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart_called")
        isActivityVisible = true
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause_called")
        isActivityVisible = false
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume_called")
        UserInterfaceUtils.loadAd(mAdView)
        isActivityVisible = true
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy_called")
        isActivityVisible = false
    }

    fun updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 1000)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnPlay -> {
                UserInterfaceUtils.loadAd(mAdView)
                if (mp?.isPlaying!!) {
                    if (mp != null) {
                        mp?.pause()
                        btnPlay?.setImageResource(R.drawable.btn_play)
                    }
                } else {
                    if (mp != null) {
                        mp?.start()
                        btnPlay?.setImageResource(R.drawable.btn_pause)
                    }
                }
            }

            R.id.btnForward -> {
                val currentPosition = mp?.currentPosition
                // check if seekForward time is lesser than song duration
                if (currentPosition != null) {
                    if (currentPosition + seekForwardTime <= mp!!.duration) {
                        // forward song
                        mp?.seekTo(currentPosition + seekForwardTime)
                    } else {
                        // forward to end position
                        mp?.duration?.let { mp?.seekTo(it) }
                    }
                }
            }

            R.id.btnBackward -> {
                val currentPosition1 = mp?.currentPosition
                // check if seekBackward time is greater than 0 sec
                if (currentPosition1 != null) {
                    if (currentPosition1 - seekBackwardTime >= 0) {
                        // forward song
                        mp?.seekTo(currentPosition1 - seekBackwardTime)
                    } else {
                        // backward to starting position
                        mp?.seekTo(0)
                    }
                }
            }

            R.id.btnNext -> {
                if (mp != null)
                    mp?.stop()
                UserInterfaceUtils.loadAd(mAdView)
                playNextSong()
            }

            R.id.btnPrevious -> {
                if (mp != null)
                    mp?.stop()
                UserInterfaceUtils.loadAd(mAdView)
                playPrevSong()
            }
        }
    }

    fun playNextSong() {
        if (indexOfSong < 19) {
            val nextValue = 1 + indexOfSong
            indexOfSong++
            Log.d(TAG, "nextSongIndex: $nextValue")
            playSongIndex(nextValue)
            btnPlay?.setImageResource(R.drawable.btn_pause)
        } else {
            playSongIndex(0)
            btnPlay?.setImageResource(R.drawable.btn_pause)
            indexOfSong = 0
        }
    }

    fun playPrevSong() {
        if (indexOfSong > 0) {
            val preValue = indexOfSong - 1
            indexOfSong--
            Log.d(TAG, "prevSongIndex: $preValue")
            playSongIndex(preValue)
            btnPlay?.setImageResource(R.drawable.btn_pause)
        } else {
            playSongIndex(19)
            btnPlay?.setImageResource(R.drawable.btn_pause)
            indexOfSong = 19
        }
    }

    fun playSongIndex(index: Int) {
        if (mp != null) {
            mp?.stop()
            mp?.release()
        }

        when (index) {
            0 -> {
                supportActionBar?.title = "Ambe Tu Hai Jagdambe Kali"
                mp = MediaPlayer.create(this, R.raw.ambe_tu_hai_jagdambe_kali)
            }
            1 -> {
                supportActionBar?.title = "Bheja Hai Bulava Tune Sherawaliye"
                mp = MediaPlayer.create(this, R.raw.bheja_hai_bulava_tune_sherawaliye)
            }
            2 -> {
                supportActionBar?.title = "Bhor Bhai Din Char Gaya Meri Ambe"
                mp = MediaPlayer.create(this, R.raw.bhor_bhai_din_char_gaya_meri_ambe)
            }
            3 -> {
                supportActionBar?.title = "Bigdi Meri Bana De"
                mp = MediaPlayer.create(this, R.raw.bigdi_meri_bana_de)
            }
            4 -> {
                supportActionBar?.title = "Chalo Bulava Aaya Hai"
                mp = MediaPlayer.create(this, R.raw.chalo_bulava_aaya_hai)
            }
            5 -> {
                supportActionBar?.title = "Durga Hai Meri Maa"
                mp = MediaPlayer.create(this, R.raw.durga_hai_meri_maa)
            }
            6 -> {
                supportActionBar?.title = "Hey Naam Re Sabse Bada Tera Naam"
                mp = MediaPlayer.create(this, R.raw.hey_naam_re_sabse_bada_tera_naam)
            }
            7 -> {
                supportActionBar?.title = "Maa Sun Le Pukar"
                mp = MediaPlayer.create(this, R.raw.maa_sun_le_pukar)
            }
            8 -> {
                supportActionBar?.title = "Maiya Ka Chola Hai Rangla"
                mp = MediaPlayer.create(this, R.raw.maiya_ka_chola_rangla)
            }
            9 -> {
                supportActionBar?.title = "Maiya Main Nihaal Ho Gaya"
                mp = MediaPlayer.create(this, R.raw.maiya_main_nihaal_ho_gaya)
            }
            10 -> {
                supportActionBar?.title = "Man Tera Mandir Aankhe Diya"
                mp = MediaPlayer.create(this, R.raw.man_tera_mandir_aakhe_diya)
            }
            11 -> {
                supportActionBar?.title = "Main Toh Aarti Utaru Re Santoshi Mata Ki"
                mp = MediaPlayer.create(this, R.raw.main_toh_aarti_utaru)
            }
            12 -> {
                supportActionBar?.title = "Meri Akhiyon Ke Samne Hi Rehna"
                mp = MediaPlayer.create(this, R.raw.meri_akhiyon_ke_samne_hi_rehna)
            }
            13 -> {
                supportActionBar?.title = "Meri Jholi Chhoti Pad Gayee Re"
                mp = MediaPlayer.create(this, R.raw.meri_jholi_chhoti_pa_gayee_re)
            }
            14 -> {
                supportActionBar?.title = "Na Main Mangu Sona Devi Bhajan"
                mp = MediaPlayer.create(this, R.raw.na_main_mangu_sona_devi)
            }
            15 -> {
                supportActionBar?.title = "Pyara Saja Hai Tera Dwar"
                mp = MediaPlayer.create(this, R.raw.pyara_saja_hai_tera_dwar)
            }
            16 -> {
                supportActionBar?.title = "Sher Pe Sawar Hoke"
                mp = MediaPlayer.create(this, R.raw.sher_pe_sawar_hoke)
            }
            17 -> {
                supportActionBar?.title = "Suno Suno Ek Kahani"
                mp = MediaPlayer.create(this, R.raw.suno_suno_ek_kahani)
            }
            18 -> {
                supportActionBar?.title = "Tune Mujhe Bulaya Sherawaliye"
                mp = MediaPlayer.create(this, R.raw.tune_mujhe_bulaya_sherawaliye)
            }
            19 -> {
                supportActionBar?.title = "Yahaan Wahaan Apni Santoshi Maa"
                mp = MediaPlayer.create(this, R.raw.yahaan_wahaan_apani_santoshi_maa)
            }
        }

        if (mp != null) {
            mp?.start()
            mp?.setOnCompletionListener(this)
            val totalDuration = mp?.duration?.toLong()

            songTotalDurationLabel?.text = totalDuration?.let { utils?.milliSecondsToTimer(it) }
            Log.d(TAG, "Song_total_duration " + totalDuration?.let { utils?.milliSecondsToTimer(it) })
        }
    }

    override fun onBackPressed() {
        backPressed()
    }

    fun backPressed() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        Constant.NOW_PLAYING_SONG_NAME = supportActionBar?.title.toString()
        startActivity(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home ->{
                backPressed()
                return true
            }
            else ->
                return super.onOptionsItemSelected(item)
        }
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        //Log.d(TAG, "onProgressChanged");
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {
        Log.d(TAG, "onStartTrackingTouch")
        mHandler.removeCallbacks(mUpdateTimeTask)
    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
        Log.d(TAG, "onStopTrackingTouch")
        mHandler.removeCallbacks(mUpdateTimeTask)
        val totalDuration = mp?.duration
        val currentPosition = totalDuration?.let { utils?.progressToTimer(seekBar.progress, it) }
        // forward or backward to certain seconds
        if (currentPosition != null) {
            mp?.seekTo(currentPosition)
        }
        // update timer progress again
        updateProgressBar()
    }

    override fun onCompletion(mp: MediaPlayer) {
        Log.d(TAG, "Completed_Song_index $indexOfSong")
        if (isRepeat) {
            playSongIndex(indexOfSong)
        } else {
            playNextSong()
        }
    }

    companion object {
        var mp: MediaPlayer? = null
        private val TAG = "MusicPlayerActivity"
    }
}
