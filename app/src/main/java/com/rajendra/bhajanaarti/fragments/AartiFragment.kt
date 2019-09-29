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
import com.rajendra.bhajanaarti.constants.Constant
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

    override fun onResume() {
        super.onResume()
        UserInterfaceUtils.loadAd(mAdView)
    }

    fun initializer(v: View){
        mAdView = v.findViewById(R.id.adView)
        recyclerView = v.findViewById(R.id.recycler_view) as RecyclerView
        albumList = ArrayList()
        adapter = AartiAdapter(albumList as ArrayList<Album>)
        recyclerView?.setAdapter(adapter)
        prepareAartis()
    }

    private fun prepareAartis(){
        val rawArrayHindi = intArrayOf(R.raw.shendur_lal_chhadayo_hin,
                R.raw.jai_ganesh_deva_hin, R.raw.jai_ambe_gauri_hin,
                R.raw.shiv_ji_aarti_hin, R.raw.om_jai_jagadish_hin, R.raw.krishna_aarti_hin,
                R.raw.shani_aarti_hin, R.raw.lakshmi_aarti_hin, R.raw.santoshi_mata_aarti_hin,
                R.raw.kali_mata_aarti_hin, R.raw.brihaspati_aarti_hin)

        val rawArrayHinEnglish = intArrayOf(R.raw.shendur_lal_chhadayo_eng,
                R.raw.jai_ganesh_deva_eng, R.raw.jai_ambe_gauri_eng,
                R.raw.shiv_ji_aarti_eng, R.raw.om_jai_jagadish_eng, R.raw.krishna_aarti_eng,
                R.raw.shani_aarti_eng, R.raw.lakshmi_aarti_eng, R.raw.santoshi_mata_aarti_eng,
                R.raw.kali_mata_aarti_eng, R.raw.brihaspati_aarti_eng)

        val coversHindi = intArrayOf(R.drawable.ganesh2,R.drawable.ganesh3, R.drawable.durga_devi,
                R.drawable.shankar1, R.drawable.vishnu_dev, R.drawable.krishna,
                R.drawable.shani_dev, R.drawable.lakshmi, R.drawable.santoshi_mata,
                R.drawable.kalimata, R.drawable.brihaspati_dev)

        albumList?.clear()
        adapter?.notifyDataSetChanged()

        if (Constant.LANGUAGE == "हिंदी"){
            var a = Album("शेंदूर लाल चढायो",rawArrayHindi[0],  coversHindi[0])
            albumList?.add(a)
            a = Album("जय गणेश जय गणेश देवा",rawArrayHindi[1], coversHindi[1])
            albumList?.add(a)
            a = Album("जय अम्बे गौरी मैया", rawArrayHindi[2],coversHindi[2])
            albumList?.add(a)
            a = Album("शिव आरती",rawArrayHindi[3], coversHindi[3])
            albumList?.add(a)
            a = Album("ओम जय जगदीश हरे", rawArrayHindi[4],coversHindi[4])
            albumList?.add(a)
            a = Album("कृष्ण आरती",rawArrayHindi[5], coversHindi[5])
            albumList?.add(a)
            a = Album("शनि देवजी की आरती",rawArrayHindi[6], coversHindi[6])
            albumList?.add(a)
            a = Album("लक्ष्मीजी की आरती",rawArrayHindi[7], coversHindi[7])
            albumList?.add(a)
            a = Album("संतोषी माता की आरती",rawArrayHindi[8], coversHindi[8])
            albumList?.add(a)
            a = Album("कालीमाता आरती",rawArrayHindi[9], coversHindi[9])
            albumList?.add(a)
            a = Album("बृहस्पति देवता की आरती",rawArrayHindi[10], coversHindi[10])
            albumList?.add(a)
        }
        else if (Constant.LANGUAGE == "hindi"){
            var a = Album("Shendur Lal Chadayo",rawArrayHinEnglish[0],  coversHindi[0])
            albumList?.add(a)
            a = Album("Jai Ganesh Jai Ganesh Deva",rawArrayHinEnglish[1], coversHindi[1])
            albumList?.add(a)
            a = Album("Jai Ambe Gauri Maiya", rawArrayHinEnglish[2],coversHindi[2])
            albumList?.add(a)
            a = Album("Shiv Ji Ki Aarti",rawArrayHinEnglish[3], coversHindi[3])
            albumList?.add(a)
            a = Album("Om Jai Jagadish Hare", rawArrayHinEnglish[4],coversHindi[4])
            albumList?.add(a)
            a = Album("Shri Krishna Aarti",rawArrayHinEnglish[5], coversHindi[5])
            albumList?.add(a)
            a = Album("Shani Dev Ji Ki Aarti",rawArrayHinEnglish[6], coversHindi[6])
            albumList?.add(a)
            a = Album("Lakshmi Ji Ki Aarti",rawArrayHinEnglish[7], coversHindi[7])
            albumList?.add(a)
            a = Album("Santoshi Mata Ki Aarti",rawArrayHinEnglish[8], coversHindi[8])
            albumList?.add(a)
            a = Album("Kalimata Aarti",rawArrayHinEnglish[9], coversHindi[9])
            albumList?.add(a)
            a = Album("Brihaspati devata ki aarti",rawArrayHinEnglish[10], coversHindi[10])
            albumList?.add(a)
        }
        adapter?.notifyDataSetChanged()
    }
}
