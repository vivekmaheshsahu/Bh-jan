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

    fun initializer(v: View){
        mAdView = v.findViewById(R.id.adView)
        UserInterfaceUtils.loadAd(mAdView)
        recyclerView = v.findViewById(R.id.recycler_view) as RecyclerView
        albumList = ArrayList()
        adapter = AartiAdapter(albumList as ArrayList<Album>)
        recyclerView?.setAdapter(adapter)
        prepareAartis()
    }

    private fun prepareAartis(){
        val coversHindi = intArrayOf(R.drawable.ganesh2,R.drawable.ganesh3, R.drawable.durga_devi,
                R.drawable.shankar1, R.drawable.vishnu_dev, R.drawable.krishna,
                R.drawable.shani_dev, R.drawable.lakshmi, R.drawable.santoshi_mata,
                R.drawable.kalimata, R.drawable.brihaspati_dev, R.drawable.pushpanjali)

        val coversMarathi = intArrayOf(R.drawable.ganesh1, R.drawable.durga_devi, R.drawable.shankar1,
                R.drawable.datta, R.drawable.vitthal2, R.drawable.vitthal1,
                R.drawable.dnyan_raja, R.drawable.ganesh3, R.drawable.pushpanjali)

        albumList?.clear()
        adapter?.notifyDataSetChanged()

        if (Constant.LANGUAGE == "हिंदी"){
            var a = Album("शेंदूर लाल चढायो", coversHindi[0])
            albumList?.add(a)
            a = Album("जय गणेश जय गणेश देवा", coversHindi[1])
            albumList?.add(a)
            a = Album("जय अम्बे गौरी मैया जय श्यामा गौरी", coversHindi[2])
            albumList?.add(a)
            a = Album("शिव आरती",coversHindi[3])
            albumList?.add(a)
            a = Album("ओम जय जगदीश हरे", coversHindi[4])
            albumList?.add(a)
            a = Album("कृष्ण आरती",coversHindi[5])
            albumList?.add(a)
            a = Album("शनि देवजी की आरती",coversHindi[6])
            albumList?.add(a)
            a = Album("लक्ष्मीजी की आरती",coversHindi[7])
            albumList?.add(a)
            a = Album("संतोषी माता की आरती",coversHindi[8])
            albumList?.add(a)
            a = Album("कालीमाता आरती",coversHindi[9])
            albumList?.add(a)
            a = Album("बृहस्पति देवता की आरती",coversHindi[10])
            albumList?.add(a)
            a = Album("घालीन लोटांगण", coversHindi[1])
            albumList?.add(a)
            a = Album("पुष्पांजली", coversHindi[11])
            albumList?.add(a)
        }
        else if (Constant.LANGUAGE == "मराठी"){
            var a = Album("सुखकर्ता दुखहर्ता", coversMarathi[0])
            albumList?.add(a)
            a = Album("दुर्गे दुर्घट भारी", coversMarathi[1])
            albumList?.add(a)
            a = Album("लवथवती विक्राळा", coversMarathi[2])
            albumList?.add(a)
            a = Album("त्रिगुणात्मक त्रैमूर्ती दत्त", coversMarathi[3])
            albumList?.add(a)
            a = Album("युगें अठ्ठावीस", coversMarathi[4])
            albumList?.add(a)
            a = Album("येई हो विठ्ठले माझे माऊली ये", coversMarathi[5])
            albumList?.add(a)
            a = Album("आरती ज्ञानराजा", coversMarathi[6])
            albumList?.add(a)
            a = Album("घालीन लोटांगण", coversMarathi[7])
            albumList?.add(a)
            a = Album("प्रार्थना", coversMarathi[7])
            albumList?.add(a)
            a = Album("पुष्पांजली", coversMarathi[8])
            albumList?.add(a)
        }
        else if (Constant.LANGUAGE == "hindi"){

        }
        else if (Constant.LANGUAGE == "marathi"){

        }
        adapter?.notifyDataSetChanged()
    }
}
