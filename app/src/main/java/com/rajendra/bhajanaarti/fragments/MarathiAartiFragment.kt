package com.rajendra.bhajanaarti.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.ads.AdView
import com.rajendra.bhajanaarti.Adapters.AartiAdapter
import com.rajendra.bhajanaarti.Pojo.Album

import com.rajendra.bhajanaarti.R
import com.rajendra.bhajanaarti.utils.UserInterfaceUtils
import java.util.ArrayList

class MarathiAartiFragment : Fragment() {
    private var recyclerView: RecyclerView? = null
    private var adapter: AartiAdapter? = null
    private var albumList: MutableList<Album>? = null
    private var mAdView: AdView? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_aarti, container, false)
        initializer(view)
        return view
    }
    fun initializer(v: View){
        mAdView = v.findViewById(R.id.adView)
        UserInterfaceUtils.loadAd(mAdView)
        recyclerView = v.findViewById(R.id.recycler_view) as RecyclerView
        albumList = ArrayList()
        adapter = AartiAdapter(albumList as ArrayList<Album>)
        recyclerView?.setAdapter(adapter)
        prepareAlbums()
    }

    private fun prepareAlbums() {
        val covers = intArrayOf(R.drawable.ganesh1, R.drawable.durga_devi, R.drawable.shankar1,
                R.drawable.datta, R.drawable.vitthal2, R.drawable.vitthal1,
                R.drawable.dnyan_raja, R.drawable.ganesh3, R.drawable.pushpanjali)

        var a = Album("सुखकर्ता दुखहर्ता", covers[0])
        albumList?.add(a)
        a = Album("दुर्गे दुर्घट भारी", covers[1])
        albumList?.add(a)
        a = Album("लवथवती विक्राळा", covers[2])
        albumList?.add(a)
        a = Album("त्रिगुणात्मक त्रैमूर्ती दत्त", covers[3])
        albumList?.add(a)
        a = Album("युगें अठ्ठावीस", covers[4])
        albumList?.add(a)
        a = Album("येई हो विठ्ठले माझे माऊली ये", covers[5])
        albumList?.add(a)
        a = Album("आरती ज्ञानराजा", covers[6])
        albumList?.add(a)
        a = Album("घालीन लोटांगण", covers[7])
        albumList?.add(a)
        a = Album("प्रार्थना", covers[7])
        albumList?.add(a)
        a = Album("पुष्पांजली", covers[8])
        albumList?.add(a)

        adapter?.notifyDataSetChanged()
    }
}
