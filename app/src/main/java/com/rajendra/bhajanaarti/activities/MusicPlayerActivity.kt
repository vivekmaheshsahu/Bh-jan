package com.rajendra.bhajanaarti.activities

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
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.rajendra.bhajanaarti.R
import com.google.android.gms.ads.AdView
import com.rajendra.bhajanaarti.base.BaseActivity
import com.rajendra.bhajanaarti.constants.Constant
import com.rajendra.bhajanaarti.utils.UserInterfaceUtils
import com.rajendra.bhajanaarti.utils.Utilities
import java.lang.Exception

class MusicPlayerActivity : BaseActivity(), SeekBar.OnSeekBarChangeListener, View.OnClickListener, MediaPlayer.OnCompletionListener {

    //private var mAdView: AdView? = null
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
    internal var indexOfSong: Int = 0
    private val mHandler = Handler()
    private var isActivityVisible: Boolean = false
    private var songListSize: Int = 30

    private val mUpdateTimeTask = object : Runnable {
        override fun run() {
            try {
                if (mp != null) {
                    val totalDuration = mp?.duration?.toLong()
                    val currentDuration = mp?.currentPosition?.toLong()

                    // Displaying time completed playing
                    Log.d(TAG, "Song current Duration: " + currentDuration?.let { utils?.milliSecondsToTimer(it) })
                    songCurrentDurationLabel?.text = currentDuration?.let { utils?.milliSecondsToTimer(it) }

                    if (isActivityVisible) {
                        if (mp != null) {
                            if (mp?.isPlaying!!) {
                                btnPlay?.setImageResource(R.drawable.ic_pause_white_64dp)
                            } else
                                btnPlay?.setImageResource(R.drawable.ic_play_white_64dp)
                        }
                    }

                    val progress = currentDuration?.let { totalDuration?.let { it1 -> utils?.getProgressPercentage(it, it1) } }
                    //Log.d("Progress", ""+progress);
                    if (progress != null) {
                        songProgressBar?.progress = progress
                    }

                    // Running this thread after 500 milliseconds
                    mHandler.postDelayed(this, 1000)
                } else {
                    Log.d(TAG, "finish_called")
                    finish()
                }
            }
            catch (e: Exception){
                e.printStackTrace()
                Log.d(TAG, "exception_mUpdateTimeTask " + e.message)
            }
        }
    }

    override fun provideLayoutId(): Int {
        return R.layout.activity_music_player
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        initializer()
    }

    private fun initializer() {
        //mAdView = findViewById(R.id.adView)
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
        //UserInterfaceUtils.loadAd(mAdView)
        isActivityVisible = true
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy_called")
        isActivityVisible = false
        /*if (mAdView != null)
            mAdView?.destroy()*/
    }

    fun updateProgressBar() {
        mHandler.postDelayed(mUpdateTimeTask, 10)
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.btnPlay -> {
                if (mp?.isPlaying!!) {
                    if (mp != null) {
                        mp?.pause()
                        btnPlay?.setImageResource(R.drawable.ic_play_white_64dp)
                    }
                } else {
                    if (mp != null) {
                        mp?.start()
                        btnPlay?.setImageResource(R.drawable.ic_pause_white_64dp)
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
                playNextSong(false)
            }

            R.id.btnPrevious -> {
                if (mp != null)
                    mp?.stop()
                playPrevSong()
            }
        }
    }

    fun playNextSong(onCompleteSong: Boolean) {
        if (indexOfSong < songListSize) {
            val nextValue = 1 + indexOfSong
            indexOfSong++
            Log.d(TAG, "nextSongIndex: $nextValue")
            playSongIndex(nextValue)
            btnPlay?.setImageResource(R.drawable.ic_pause_white_64dp)
        } else {
            playSongIndex(0)
            btnPlay?.setImageResource(R.drawable.ic_pause_white_64dp)
            indexOfSong = 0
        }

        if (onCompleteSong){
            Thread.sleep(1000)
            val intent = Intent("NowPlayingEvent")
            Constant.NOW_PLAYING_SONG_NAME = supportActionBar?.title.toString()
            LocalBroadcastManager.getInstance(this).sendBroadcast(intent)
        }
    }

    fun playPrevSong() {
        if (indexOfSong > 0) {
            val preValue = indexOfSong - 1
            indexOfSong--
            Log.d(TAG, "prevSongIndex: $preValue")
            playSongIndex(preValue)
            btnPlay?.setImageResource(R.drawable.ic_pause_white_64dp)
        } else {
            playSongIndex(songListSize)
            btnPlay?.setImageResource(R.drawable.ic_pause_white_64dp)
            indexOfSong = songListSize
        }
    }

    fun playSongIndex(index: Int) {
        if (mp != null) {
            mp?.stop()
            mp?.release()
        }

        when (index) {
            0 -> {
                supportActionBar?.title = Constant.APP_CONTEXT?.resources?.getString(R.string.aa_maa_aa_tujhe)
                mp = MediaPlayer.create(this, R.raw.aa_maa_aa_tujhe_dil)
            }
            1 -> {
                supportActionBar?.title = Constant.APP_CONTEXT?.resources?.getString(R.string.ambe_tu_hai)
                mp = MediaPlayer.create(this, R.raw.ambe_tu_hai_jagdambe_kali)
            }
            2 -> {
                supportActionBar?.title = Constant.APP_CONTEXT?.resources?.getString(R.string.bheja_hai_bulava_tune)
                mp = MediaPlayer.create(this, R.raw.bheja_hai_bulava_tune_sherawaliye)
            }
            3 -> {
                supportActionBar?.title = Constant.APP_CONTEXT?.resources?.getString(R.string.bhor_bhai_din_char)
                mp = MediaPlayer.create(this, R.raw.bhor_bhai_din_char_gaya_meri_ambe)
            }
            4 -> {
                supportActionBar?.title = Constant.APP_CONTEXT?.resources?.getString(R.string.bigdi_meri_bana)
                mp = MediaPlayer.create(this, R.raw.bigdi_meri_bana_de)
            }
            5 -> {
                supportActionBar?.title = Constant.APP_CONTEXT?.resources?.getString(R.string.chalo_bulava_aaya)
                mp = MediaPlayer.create(this, R.raw.chalo_bulava_aaya_hai)
            }
            6 -> {
                supportActionBar?.title = Constant.APP_CONTEXT?.resources?.getString(R.string.durga_hai_meri)
                mp = MediaPlayer.create(this, R.raw.durga_hai_meri_maa)
            }
            7 -> {
                supportActionBar?.title = Constant.APP_CONTEXT?.resources?.getString(R.string.hey_naam_re_sabse)
                mp = MediaPlayer.create(this, R.raw.hey_naam_re_sabse_bada_tera_naam)
            }
            8 -> {
                supportActionBar?.title = Constant.APP_CONTEXT?.resources?.getString(R.string.maa_sun_le_pukar)
                mp = MediaPlayer.create(this, R.raw.maa_sun_le_pukar)
            }
            9 -> {
                supportActionBar?.title = Constant.APP_CONTEXT?.resources?.getString(R.string.maiya_ka_chola)
                mp = MediaPlayer.create(this, R.raw.maiya_ka_chola_rangla)
            }
            10 -> {
                supportActionBar?.title = Constant.APP_CONTEXT?.resources?.getString(R.string.maiya_main_nihal)
                mp = MediaPlayer.create(this, R.raw.maiya_main_nihaal_ho_gaya)
            }
            11 -> {
                supportActionBar?.title = Constant.APP_CONTEXT?.resources?.getString(R.string.man_tera_mandir)
                mp = MediaPlayer.create(this, R.raw.man_tera_mandir_aakhe_diya)
            }
            12 -> {
                supportActionBar?.title = Constant.APP_CONTEXT?.resources?.getString(R.string.main_toh_aarti_utaru)
                mp = MediaPlayer.create(this, R.raw.main_toh_aarti_utaru)
            }
            13 -> {
                supportActionBar?.title = Constant.APP_CONTEXT?.resources?.getString(R.string.main_balak_tu_mata)
                mp = MediaPlayer.create(this, R.raw.main_balak_tu_mata)
            }
            14 -> {
                supportActionBar?.title = Constant.APP_CONTEXT?.resources?.getString(R.string.meri_akhiyon_ke_samne)
                mp = MediaPlayer.create(this, R.raw.meri_akhiyon_ke_samne_hi_rehna)
            }
            15 -> {
                supportActionBar?.title = Constant.APP_CONTEXT?.resources?.getString(R.string.meri_jholi_chhoti)
                mp = MediaPlayer.create(this, R.raw.meri_jholi_chhoti_pa_gayee_re)
            }
            16 -> {
                supportActionBar?.title = Constant.APP_CONTEXT?.resources?.getString(R.string.na_main_mangu)
                mp = MediaPlayer.create(this, R.raw.na_main_mangu_sona_devi)
            }
            17 -> {
                supportActionBar?.title = Constant.APP_CONTEXT?.resources?.getString(R.string.pyara_saja_hai)
                mp = MediaPlayer.create(this, R.raw.pyara_saja_hai_tera_dwar)
            }
            18 -> {
                supportActionBar?.title = Constant.APP_CONTEXT?.resources?.getString(R.string.sher_pe_sawar)
                mp = MediaPlayer.create(this, R.raw.sher_pe_sawar_hoke)
            }
            19 -> {
                supportActionBar?.title = Constant.APP_CONTEXT?.resources?.getString(R.string.suno_suno_ek)
                mp = MediaPlayer.create(this, R.raw.suno_suno_ek_kahani)
            }
            20 -> {
                supportActionBar?.title = Constant.APP_CONTEXT?.resources?.getString(R.string.tune_mujhe_bulaya)
                mp = MediaPlayer.create(this, R.raw.tune_mujhe_bulaya_sherawaliye)
            }
            21 -> {
                supportActionBar?.title = Constant.APP_CONTEXT?.resources?.getString(R.string.yahaan_wahaan_apni)
                mp = MediaPlayer.create(this, R.raw.yahaan_wahaan_apani_santoshi_maa)
            }
            22 -> {
                supportActionBar?.title = Constant.APP_CONTEXT?.resources?.getString(R.string.shailputri_aarti)
                mp = MediaPlayer.create(this, R.raw.shailputri_aarti)
            }
            23 -> {
                supportActionBar?.title = Constant.APP_CONTEXT?.resources?.getString(R.string.bhrahmcharini_aart)
                mp = MediaPlayer.create(this, R.raw.brahmacharini_aarti)
            }
            24 -> {
                supportActionBar?.title = Constant.APP_CONTEXT?.resources?.getString(R.string.chandraghata_aarti)
                mp = MediaPlayer.create(this, R.raw.chandraghanta_aarti)
            }
            25 -> {
                supportActionBar?.title = Constant.APP_CONTEXT?.resources?.getString(R.string.kushmanda_aarti)
                mp = MediaPlayer.create(this, R.raw.kusmanda_aarti)
            }
            26 -> {
                supportActionBar?.title = Constant.APP_CONTEXT?.resources?.getString(R.string.skandmata_aarti)
                mp = MediaPlayer.create(this, R.raw.skandamata_aarti)
            }
            27 -> {
                supportActionBar?.title = Constant.APP_CONTEXT?.resources?.getString(R.string.katyayani_aarti)
                mp = MediaPlayer.create(this, R.raw.katyayani_aarti)
            }
            28 -> {
                supportActionBar?.title = Constant.APP_CONTEXT?.resources?.getString(R.string.kalratri_aarti)
                mp = MediaPlayer.create(this, R.raw.kaalratri_aarti)
            }
            29 -> {
                supportActionBar?.title = Constant.APP_CONTEXT?.resources?.getString(R.string.mahagauri_aarti)
                mp = MediaPlayer.create(this, R.raw.mahagauri_aarti)
            }
            30 -> {
                supportActionBar?.title = Constant.APP_CONTEXT?.resources?.getString(R.string.sidhhidatri_aarti)
                mp = MediaPlayer.create(this, R.raw.siddhidatri_aarti)
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
        playNextSong(true)
    }

    companion object {
        var mp: MediaPlayer? = null
        private val TAG = "MusicPlayerActivity"
    }
}
