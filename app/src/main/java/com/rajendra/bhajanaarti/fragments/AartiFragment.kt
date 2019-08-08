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

class AartiFragment : Fragment() {

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
        val covers = intArrayOf(R.drawable.ganesh2,R.drawable.ganesh3, R.drawable.durga_devi,
                R.drawable.shankar1, R.drawable.vishnu_dev, R.drawable.krishna,
                R.drawable.shani_dev, R.drawable.lakshmi, R.drawable.santoshi_mata,
                R.drawable.kalimata, R.drawable.brihaspati_dev, R.drawable.pushpanjali)

        var a = Album("शेंदूर लाल चढायो", covers[0])
        albumList?.add(a)
        a = Album("जय गणेश जय गणेश देवा", covers[1])
        albumList?.add(a)
        a = Album("जय अम्बे गौरी मैया जय श्यामा गौरी", covers[2])
        albumList?.add(a)
        a = Album("शिव आरती",covers[3])
        albumList?.add(a)
        a = Album("ओम जय जगदीश हरे", covers[4])
        albumList?.add(a)
        a = Album("कृष्ण आरती",covers[5])
        albumList?.add(a)
        a = Album("शनि देवजी की आरती",covers[6])
        albumList?.add(a)
        a = Album("लक्ष्मीजी की आरती",covers[7])
        albumList?.add(a)
        a = Album("संतोषी माता की आरती",covers[8])
        albumList?.add(a)
        a = Album("कालीमाता आरती",covers[9])
        albumList?.add(a)
        a = Album("बृहस्पति देवता की आरती",covers[10])
        albumList?.add(a)
        a = Album("घालीन लोटांगण", covers[1])
        albumList?.add(a)
        a = Album("पुष्पांजली", covers[11])
        albumList?.add(a)

        adapter?.notifyDataSetChanged()
    }
}
