package com.rajendra.bhajanaarti.Adapters

import android.content.Context
import android.content.Intent
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView

import com.rajendra.bhajanaarti.Pojo.SongInfo
import com.rajendra.bhajanaarti.R
import com.rajendra.bhajanaarti.activities.MusicPlayerActivity

class SongInfoAdapter(private val mCtx: Context, private val items: ArrayList<SongInfo>,
                      private val progressBarInterface: ProgressBarInterface) :
        RecyclerView.Adapter<SongInfoAdapter.MyViewHolder>() {

    internal var handler = Handler()
    private var songIndex: Int? = null

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView), View.OnClickListener {
        var ivDeviFace: ImageView
        var tvSongName: TextView

        init {
            itemView.setOnClickListener(this)
            this.ivDeviFace = itemView.findViewById<View>(R.id.ivDeviFace) as ImageView
            this.tvSongName = itemView.findViewById<View>(R.id.tvSongName) as TextView
        }

        override fun onClick(itemview: View) {
            progressBarInterface.showProgressBar()
            if (MusicPlayerActivity.mp != null) {
                MusicPlayerActivity.mp!!.stop()
                MusicPlayerActivity.mp = null
            }
            songIndex = adapterPosition
            Log.d("test", "position_index $adapterPosition")
            Log.d("Test", "SongInfo_name " + items[adapterPosition].songname!!)
            handler.postDelayed(r, 900)
        }
    }

    internal var r: Runnable = Runnable {
        progressBarInterface.hideProgressBar()
        val intent = Intent(mCtx, MusicPlayerActivity::class.java)
        intent.putExtra("songindex", songIndex)
        mCtx.startActivity(intent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context)
                .inflate(R.layout.home_list_view, parent, false)

        return MyViewHolder(view)
    }


    override fun onBindViewHolder(holder: MyViewHolder, listPosition: Int) {
        val imageView = holder.ivDeviFace
        val textViewName = holder.tvSongName

        imageView.setImageResource(items[listPosition].imageid)
        textViewName.setText(items[listPosition].songname)
    }


    override fun getItemCount(): Int {
        return items.size
    }

    interface ProgressBarInterface{
        fun showProgressBar()
        fun hideProgressBar()
    }
}
