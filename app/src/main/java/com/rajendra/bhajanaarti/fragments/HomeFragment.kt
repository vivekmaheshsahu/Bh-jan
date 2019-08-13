package com.rajendra.bhajanaarti.fragments

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdView
import com.rajendra.bhajanaarti.Adapters.SongInfoAdapter
import com.rajendra.bhajanaarti.Pojo.SongInfo

import com.rajendra.bhajanaarti.R
import com.rajendra.bhajanaarti.activities.MusicPlayerActivity
import com.rajendra.bhajanaarti.constants.Constant
import com.rajendra.bhajanaarti.utils.UserInterfaceUtils
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment(), View.OnClickListener, SongInfoAdapter.ProgressBarInterface {

    private val TAG = HomeFragment::class.java.simpleName
    private var mAdView: AdView? = null
    lateinit internal var adapter: SongInfoAdapter
    lateinit internal var rvListBhajan: RecyclerView
    lateinit internal var songInfo: ArrayList<SongInfo>
    private var progressBar: ProgressBar? = null
    internal var handler = Handler()
    internal var songIndex: Int = 0
    private var ivPlayHome: ImageView? = null
    private var ivPauseHome: ImageView? = null
    private lateinit var viewRoot: View

    companion object {
        val songName = arrayOf("Ambe Tu Hai Jagdambe Kali", "Bheja Hai Bulava Tune Sherawaliye",
                "Bhor Bhai Din Char Gaya Meri Ambe", "Bigdi Meri Bana De", "Durga Hai Meri Maa",
                "Hey Naam Re Sabse Bada Tera Naam", "Kabse Khadi Hoon", "Maa Sun Le Pukar",
                "Maiya Ka Chola Hai Rangla", "Maiya Main Nihaal Ho Gaya", "Meri Akhiyon Ke Samne Hi Rehna",
                "Meri Jholi Chhoti Pad Gayee Re", "Na Main Mangu Sona Devi Bhajan", "Pyara Saja Hai Tera Dwar",
                "Suno Suno Ek Kahani")
        val imageid = R.drawable.deviface_oldpic
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewRoot = inflater.inflate(R.layout.fragment_home, container, false)
        initialize(viewRoot)
        return viewRoot
    }

    fun initialize(v: View){
        mAdView = v.findViewById<View>(R.id.adView) as AdView
        ivPlayHome = v.findViewById(R.id.ivPlayHome)
        ivPauseHome = v.findViewById(R.id.ivPauseHome)

        songInfo = ArrayList()
        for (i in songName.indices) {
            val item = SongInfo(imageid, songName[i])
            songInfo.add(item)
        }

        rvListBhajan = v.findViewById<View>(R.id.rvListBhajan) as RecyclerView
        adapter = SongInfoAdapter(activity!!, songInfo, this)
        rvListBhajan.adapter = adapter
        progressBar = v.findViewById(R.id.progressBar)
    }

    fun showPlayButton(show: Boolean) {
        if (show) {
            ivPlayHome!!.visibility = View.VISIBLE
            ivPauseHome!!.visibility = View.INVISIBLE
        } else {
            ivPlayHome!!.visibility = View.INVISIBLE
            ivPauseHome!!.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        UserInterfaceUtils.loadAd(mAdView)

        if (Constant.NOW_PLAYING_SONG_NAME != null && Constant.NOW_PLAYING_SONG_NAME.length > 1) {
            val playingLayout = viewRoot.findViewById<RelativeLayout>(R.id.playingLayout)
            playingLayout.visibility = View.VISIBLE
            if (MusicPlayerActivity.mp!!.isPlaying) {
                showPlayButton(false)
            } else {
                showPlayButton(true)
            }
            playingLayout.setOnClickListener(this)

            val playingSongName = viewRoot.findViewById<TextView>(R.id.playingSongName)
            playingSongName.setText(String.format(Locale.US, "%s %s",
                    "Now playing: ", Constant.NOW_PLAYING_SONG_NAME))
            playingSongName.isSelected = true
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.playingLayout -> {
                val intent = Intent(activity, MusicPlayerActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT)
                startActivity(intent)
            }
        }
    }


    override fun showProgressBar() {
        progressBar!!.visibility = View.VISIBLE
        activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    override fun hideProgressBar() {
        progressBar!!.visibility = View.GONE
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }
}
