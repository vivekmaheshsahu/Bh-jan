package com.rajendra.bhajanaarti.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import com.rajendra.bhajanaarti.R
import kotlinx.android.synthetic.main.fragment_disclaimer.*

class DisclaimerFragment : Fragment() {

    private var tvDisclaimer: TextView? = null;

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_disclaimer, container, false)

        tvDisclaimer = view.findViewById(R.id.tvDisclaimer)
        tvDisclaimer?.text = getAartiFromRaw(R.raw.disclaimer)

        return view
    }

    fun getAartiFromRaw(raw: Int?): String{
        try {
            val res = resources
            val in_s = res.openRawResource(raw!!)

            val b = ByteArray(in_s.available())
            in_s.read(b)
            return String(b)
        } catch (e: Exception) {
            e.printStackTrace();
        }
        return ""
    }

}
