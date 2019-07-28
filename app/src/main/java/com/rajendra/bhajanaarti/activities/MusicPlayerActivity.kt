package com.rajendra.bhajanaarti.activities

import android.app.Activity
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast
import com.rajendra.bhajanaarti.R
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.rajendra.bhajanaarti.constants.Constant
import com.rajendra.bhajanaarti.utils.Utilities

class MusicPlayerActivity : Activity(), SeekBar.OnSeekBarChangeListener, View.OnClickListener, MediaPlayer.OnCompletionListener {

    private var mAdView: AdView? = null
    private var tvSongTitle: TextView? = null
    private var btnPlay: ImageButton? = null
    private var btnForward: ImageButton? = null
    private var btnBackward: ImageButton? = null
    private var btnNext: ImageButton? = null
    private var btnPrevious: ImageButton? = null
    private var btnRepeat: ImageButton? = null
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
                val totalDuration = mp!!.duration.toLong()
                val currentDuration = mp!!.currentPosition.toLong()

                // Displaying Total Duration time
                /*Log.d(TAG,"totalDuration_val " + utils.milliSecondsToTimer(totalDuration));
            songTotalDurationLabel.setText(""+utils.milliSecondsToTimer(totalDuration));*/

                // Displaying time completed playing
                Log.d(TAG, "Song current Duration: " + utils!!.milliSecondsToTimer(currentDuration))
                songCurrentDurationLabel!!.text = utils!!.milliSecondsToTimer(currentDuration)

                if (isActivityVisible) {
                    if (mp != null) {
                        if (mp!!.isPlaying) {
                            btnPlay!!.setImageResource(R.drawable.btn_pause)
                        } else
                            btnPlay!!.setImageResource(R.drawable.btn_play)
                    }
                }

                val progress = utils!!.getProgressPercentage(currentDuration, totalDuration)
                //Log.d("Progress", ""+progress);
                songProgressBar!!.progress = progress

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

        initializer()
    }

    private fun initializer() {
        mAdView = findViewById(R.id.adView)
        loadAd()

        tvSongTitle = findViewById(R.id.tvSongTitle)
        songTotalDurationLabel = findViewById(R.id.songTotalDurationLabel)
        songCurrentDurationLabel = findViewById(R.id.songCurrentDurationLabel)
        btnPlay = findViewById(R.id.btnPlay)
        btnPlay!!.setOnClickListener(this)
        btnForward = findViewById(R.id.btnForward)
        btnForward!!.setOnClickListener(this)
        btnBackward = findViewById(R.id.btnBackward)
        btnBackward!!.setOnClickListener(this)
        btnNext = findViewById(R.id.btnNext)
        btnNext!!.setOnClickListener(this)
        btnPrevious = findViewById(R.id.btnPrevious)
        btnPrevious!!.setOnClickListener(this)
        btnRepeat = findViewById(R.id.btnRepeat)
        btnRepeat!!.setOnClickListener(this)
        songProgressBar = findViewById(R.id.songProgressBar)
        songProgressBar!!.progress = 0
        songProgressBar!!.setOnSeekBarChangeListener(this)

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
                loadAd()
                if (mp!!.isPlaying) {
                    if (mp != null) {
                        mp!!.pause()
                        btnPlay!!.setImageResource(R.drawable.btn_play)
                    }
                } else {
                    if (mp != null) {
                        mp!!.start()
                        btnPlay!!.setImageResource(R.drawable.btn_pause)
                    }
                }
            }

            R.id.btnForward -> {
                val currentPosition = mp!!.currentPosition
                // check if seekForward time is lesser than song duration
                if (currentPosition + seekForwardTime <= mp!!.duration) {
                    // forward song
                    mp!!.seekTo(currentPosition + seekForwardTime)
                } else {
                    // forward to end position
                    mp!!.seekTo(mp!!.duration)
                }
            }

            R.id.btnBackward -> {
                val currentPosition1 = mp!!.currentPosition
                // check if seekBackward time is greater than 0 sec
                if (currentPosition1 - seekBackwardTime >= 0) {
                    // forward song
                    mp!!.seekTo(currentPosition1 - seekBackwardTime)
                } else {
                    // backward to starting position
                    mp!!.seekTo(0)
                }
            }

            R.id.btnNext -> {
                if (mp != null)
                    mp!!.stop()
                loadAd()
                playNextSong()
            }

            R.id.btnPrevious -> {
                if (mp != null)
                    mp!!.stop()
                loadAd()
                playPrevSong()
            }

            R.id.btnRepeat -> if (isRepeat) {
                isRepeat = false
                Toast.makeText(applicationContext, "Repeat is OFF", Toast.LENGTH_SHORT).show()
                btnRepeat!!.setImageResource(R.drawable.btn_repeat)
            } else {
                // make repeat to true
                isRepeat = true
                Toast.makeText(applicationContext, "Repeat is ON", Toast.LENGTH_SHORT).show()
                btnRepeat!!.setImageResource(R.drawable.img_btn_repeat_pressed)
            }
        }
    }

    fun loadAd() {
        if (mAdView != null)
            mAdView!!.loadAd(AdRequest.Builder().build())
    }

    fun playNextSong() {
        if (indexOfSong < 14) {
            val nextValue = 1 + indexOfSong
            indexOfSong++
            Log.d(TAG, "nextSongIndex: $nextValue")
            playSongIndex(nextValue)
            btnPlay!!.setImageResource(R.drawable.btn_pause)
        } else {
            playSongIndex(0)
            btnPlay!!.setImageResource(R.drawable.btn_pause)
            indexOfSong = 0
        }
    }

    fun playPrevSong() {
        if (indexOfSong > 0) {
            val preValue = indexOfSong - 1
            indexOfSong--
            Log.d(TAG, "prevSongIndex: $preValue")
            playSongIndex(preValue)
            btnPlay!!.setImageResource(R.drawable.btn_pause)
        } else {
            playSongIndex(14)
            btnPlay!!.setImageResource(R.drawable.btn_pause)
            indexOfSong = 14
        }
    }

    fun playSongIndex(index: Int) {
        if (mp != null) {
            mp!!.stop()
            mp!!.release()
        }

        when (index) {
            0 -> {
                tvSongTitle!!.text = "1. Ambey Tu Hai Jagdambey Kali"
                mp = MediaPlayer.create(this, R.raw.ambe_tu_hai_jagdambe_kali)
            }
            1 -> {
                tvSongTitle!!.text = "2. Bheja Hai Bulava Tune Sherawaliye"
                mp = MediaPlayer.create(this, R.raw.bheja_hai_bulava_tune_sherawaliye)
            }
            2 -> {
                tvSongTitle!!.text = "3. Bhor Bhai Din Char Gaya Meri Ambe"
                mp = MediaPlayer.create(this, R.raw.bhor_bhai_din_char_gaya_meri_ambe)
            }
            3 -> {
                tvSongTitle!!.text = "4. Bigdi Meri Bana De"
                mp = MediaPlayer.create(this, R.raw.bigdi_meri_bana_de)
            }
            4 -> {
                tvSongTitle!!.text = "5. Durga Hai Meri Maa"
                mp = MediaPlayer.create(this, R.raw.durga_hai_meri_maa)
            }
            5 -> {
                tvSongTitle!!.text = "6. Hey Naam Re Sabse Bada Tera Naam"
                mp = MediaPlayer.create(this, R.raw.hey_naam_re_sabse_bada_tera_naam)
            }
            6 -> {
                tvSongTitle!!.text = "7. Kabse Khadi Hoon"
                mp = MediaPlayer.create(this, R.raw.kabse_khadi_hoon)
            }
            7 -> {
                tvSongTitle!!.text = "8. Maa Sun Le Pukar"
                mp = MediaPlayer.create(this, R.raw.maa_sun_le_pukar)
            }
            8 -> {
                tvSongTitle!!.text = "9. Maiya Ka Chola Hai Rangla"
                mp = MediaPlayer.create(this, R.raw.maiya_ka_chola_rangla)
            }
            9 -> {
                tvSongTitle!!.text = "10. Maiya Main Nihaal Ho Gaya"
                mp = MediaPlayer.create(this, R.raw.maiya_main_nihaal_ho_gaya)
            }
            10 -> {
                tvSongTitle!!.text = "11. Meri Akhiyon Ke Samne Hi Rehna"
                mp = MediaPlayer.create(this, R.raw.meri_akhiyon_ke_samne_hi_rehna)
            }
            11 -> {
                tvSongTitle!!.text = "12. Meri Jholi Chhoti Pad Gayee Re"
                mp = MediaPlayer.create(this, R.raw.meri_jholi_chhoti_pa_gayee_re)
            }
            12 -> {
                tvSongTitle!!.text = "13. Na Main Mangu Sona Devi Bhajan"
                mp = MediaPlayer.create(this, R.raw.na_main_mangu_sona_devi)
            }
            13 -> {
                tvSongTitle!!.text = "14. Pyara Saja Hai Tera Dwar"
                mp = MediaPlayer.create(this, R.raw.pyara_saja_hai_tera_dwar)
            }
            14 -> {
                tvSongTitle!!.text = "15. Suno Suno Ek Kahani"
                mp = MediaPlayer.create(this, R.raw.suno_suno_ek_kahani)
            }
        }

        if (mp != null) {
            mp!!.start()
            mp!!.setOnCompletionListener(this)
            val totalDuration = mp!!.duration.toLong()

            songTotalDurationLabel!!.text = utils!!.milliSecondsToTimer(totalDuration)
            Log.d(TAG, "Song_total_duration " + utils!!.milliSecondsToTimer(totalDuration))
        }
    }

    override fun onBackPressed() {
        val intent = Intent(this, HomeActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
        Constant.NOW_PLAYING_SONG_NAME = tvSongTitle!!.text.toString().substring(3)
        startActivity(intent)
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
        val totalDuration = mp!!.duration
        val currentPosition = utils!!.progressToTimer(seekBar.progress, totalDuration)
        // forward or backward to certain seconds
        mp!!.seekTo(currentPosition)
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
