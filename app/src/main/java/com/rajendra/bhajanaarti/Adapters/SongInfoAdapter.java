package com.rajendra.bhajanaarti.Adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.rajendra.bhajanaarti.Pojo.SongInfo;
import com.rajendra.bhajanaarti.R;

import java.util.List;


public class  SongInfoAdapter extends ArrayAdapter<SongInfo>
{
    Context context;

    public SongInfoAdapter(Context context, int resourceId, List<SongInfo> items)
    {
        super(context, resourceId, items);
        this.context = context;
    }

    @Override
    public View getView(final int position, View view, ViewGroup parent) {

        ViewHolder holder = null;
        final SongInfo songInfo = getItem(position);

        LayoutInflater mInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (view == null)
        {
            view = mInflater.inflate(R.layout.home_list_view, null);
            holder = new ViewHolder();
            holder.ivDeviFace = (ImageView) view.findViewById(R.id.ivDeviFace);
            holder.tvSongName = (TextView) view.findViewById(R.id.tvSongName);

            view.setTag(holder);
        }
        else
            holder = (ViewHolder) view.getTag();
            holder.ivDeviFace.setImageResource(songInfo.getImageid());
            holder.tvSongName.setText(songInfo.getSongname());

        return view;
    }

    /*private view holder class*/
    private class ViewHolder
    {
        ImageView ivDeviFace;
        TextView tvSongName;
    }
}
