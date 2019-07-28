package com.rajendra.bhajanaarti.Adapters

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.TextView

import com.rajendra.bhajanaarti.Pojo.SongInfo
import com.rajendra.bhajanaarti.R


class SongInfoAdapter(internal var context: Context, resourceId: Int, items: List<SongInfo>) :
        ArrayAdapter<SongInfo>(context, resourceId, items) {

    override fun getView(position: Int, view: View?, parent: ViewGroup): View {
        var view = view

        var holder: ViewHolder? = null
        val songInfo = getItem(position)

        val mInflater = context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        if (view == null) {
            view = mInflater.inflate(R.layout.home_list_view, null)
            holder = ViewHolder()
            holder.ivDeviFace = view!!.findViewById<View>(R.id.ivDeviFace) as ImageView
            holder.tvSongName = view.findViewById<View>(R.id.tvSongName) as TextView

            view.tag = holder
        } else
            holder = view.tag as ViewHolder
        holder.ivDeviFace!!.setImageResource(songInfo!!.imageid)
        holder.tvSongName!!.text = songInfo.songname

        return view
    }

    /*private view holder class*/
    private inner class ViewHolder {
        internal var ivDeviFace: ImageView? = null
        internal var tvSongName: TextView? = null
    }
}
