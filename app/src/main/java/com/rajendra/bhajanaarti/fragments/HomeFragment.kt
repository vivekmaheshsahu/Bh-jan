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

    private var mAdView: AdView? = null
    lateinit internal var adapter: SongInfoAdapter
    internal var rvListBhajan: RecyclerView? = null
    lateinit internal var songInfo: ArrayList<SongInfo>
    private var progressBar: ProgressBar? = null
    private var ivPlayHome: ImageView? = null
    private var ivPauseHome: ImageView? = null
    private var viewRoot: View? = null
    private val imageid = R.drawable.deviface_oldpic

    private var songName = arrayOf(Constant.APP_CONTEXT?.resources?.getString(R.string.ambe_tu_hai),
            Constant.APP_CONTEXT?.resources?.getString(R.string.bheja_hai_bulava_tune),
            Constant.APP_CONTEXT?.resources?.getString(R.string.bhor_bhai_din_char),
            Constant.APP_CONTEXT?.resources?.getString(R.string.bigdi_meri_bana),
            Constant.APP_CONTEXT?.resources?.getString(R.string.chalo_bulava_aaya),
            Constant.APP_CONTEXT?.resources?.getString(R.string.durga_hai_meri),
            Constant.APP_CONTEXT?.resources?.getString(R.string.hey_naam_re_sabse),
            Constant.APP_CONTEXT?.resources?.getString(R.string.maa_sun_le_pukar),
            Constant.APP_CONTEXT?.resources?.getString(R.string.maiya_ka_chola),
            Constant.APP_CONTEXT?.resources?.getString(R.string.maiya_main_nihal),
            Constant.APP_CONTEXT?.resources?.getString(R.string.man_tera_mandir),
            Constant.APP_CONTEXT?.resources?.getString(R.string.main_toh_aarti_utaru),
            Constant.APP_CONTEXT?.resources?.getString(R.string.meri_akhiyon_ke_samne),
            Constant.APP_CONTEXT?.resources?.getString(R.string.meri_jholi_chhoti),
            Constant.APP_CONTEXT?.resources?.getString(R.string.na_main_mangu),
            Constant.APP_CONTEXT?.resources?.getString(R.string.pyara_saja_hai),
            Constant.APP_CONTEXT?.resources?.getString(R.string.sher_pe_sawar),
            Constant.APP_CONTEXT?.resources?.getString(R.string.suno_suno_ek),
            Constant.APP_CONTEXT?.resources?.getString(R.string.tune_mujhe_bulaya),
            Constant.APP_CONTEXT?.resources?.getString(R.string.yahaan_wahaan_apni))

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        viewRoot = inflater.inflate(R.layout.fragment_home, container, false)
        initialize(viewRoot)
        return viewRoot
    }

    fun initialize(v: View?){
        mAdView = v?.findViewById<View>(R.id.adView) as AdView
        ivPlayHome = v.findViewById(R.id.ivPlayHome)
        ivPauseHome = v.findViewById(R.id.ivPauseHome)

        songInfo = ArrayList()
        for (i in songName.indices) {
            val item = SongInfo(imageid, songName[i])
            songInfo.add(item)
        }

        rvListBhajan = v.findViewById<View>(R.id.rvListBhajan) as RecyclerView
        adapter = SongInfoAdapter(activity, songInfo, this)
        rvListBhajan?.adapter = adapter
        progressBar = v.findViewById(R.id.progressBar)
    }

    fun showPlayButton(show: Boolean) {
        if (show) {
            ivPlayHome?.visibility = View.VISIBLE
            ivPauseHome?.visibility = View.INVISIBLE
        } else {
            ivPlayHome?.visibility = View.INVISIBLE
            ivPauseHome?.visibility = View.VISIBLE
        }
    }

    override fun onResume() {
        super.onResume()
        UserInterfaceUtils.loadAd(mAdView)

        if (!Constant.NOW_PLAYING_SONG_NAME.isEmpty() && Constant.NOW_PLAYING_SONG_NAME.length > 1) {
            val playingLayout = viewRoot?.findViewById<RelativeLayout>(R.id.playingLayout)
            playingLayout?.visibility = View.VISIBLE
            if (MusicPlayerActivity.mp != null){
                if (MusicPlayerActivity.mp?.isPlaying!!) {
                    showPlayButton(false)
                } else {
                    showPlayButton(true)
                }
                val playingSongName = viewRoot?.findViewById<TextView>(R.id.playingSongName)
                playingSongName?.setText(String.format(Locale.US, "%s: %s",
                        activity?.resources?.getString(R.string.now_playing), Constant.NOW_PLAYING_SONG_NAME))
                playingSongName?.isSelected = true
            }
            playingLayout?.setOnClickListener(this)
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
        progressBar?.visibility = View.VISIBLE
        activity?.window?.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    override fun hideProgressBar() {
        progressBar?.visibility = View.GONE
        activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }
}
