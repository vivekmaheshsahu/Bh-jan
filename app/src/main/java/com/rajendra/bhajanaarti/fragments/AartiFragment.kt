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
        val covers = intArrayOf(R.drawable.ganesh1, R.drawable.ganesh2,
                R.drawable.shankar1, R.drawable.durga1,
                R.drawable.vitthal2, R.drawable.vitthal1,
                R.drawable.ganesh3, R.drawable.vishnu,
                R.drawable.ganesh4, R.drawable.ganesh1,
                R.drawable.datta, R.drawable.durga1,
                R.drawable.ganesh3, R.drawable.pushpanjali)

        var a = Album("सुखकर्ता दुखहर्ता", 13, covers[0])
        albumList?.add(a)

        a = Album("शेंदूर लाल चढायो", 8, covers[1])
        albumList?.add(a)

        a = Album("लवथवती विक्राळा", 12, covers[2])
        albumList?.add(a)

        a = Album("दुर्गे दुर्घट भारी", 14, covers[3])
        albumList?.add(a)

        a = Album("युगें अठ्ठावीस", 1, covers[4])
        albumList?.add(a)

        a = Album("येई हो विठ्ठले माझे माऊली ये", 11, covers[5])
        albumList?.add(a)

        a = Album("जय गणेश जय गणेश देवा", 11, covers[6])
        albumList?.add(a)

        a = Album("ओम जय जगदीश हरे", 17, covers[7])
        albumList?.add(a)
        a = Album("श्रीज्ञानदेवाची आरती ज्ञानराजा", 17, covers[8])
        albumList?.add(a)
        a = Album("घालीन लोटांगण", 17, covers[9])
        albumList?.add(a)
        a = Album("श्री दत्त आरती", 17, covers[10])
        albumList?.add(a)
        a = Album("जय अम्बे गौरी मैया जय श्यामा गौरी", 17, covers[11])
        albumList?.add(a)
        a = Album("प्रार्थना", 17, covers[12])
        albumList?.add(a)
        a = Album("पुष्पांजली", 17, covers[13])
        albumList?.add(a)
        adapter?.notifyDataSetChanged()
    }
}
