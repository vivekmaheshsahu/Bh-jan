package com.rajendra.bhajanaarti.Pojo;

public class SongInfo
{
    private int imageid;
    private String songname;


    public SongInfo(int imageid, String songname)
    {
        this.imageid = imageid;
        this.songname = songname;
    }

    public int getImageid() {
        return imageid;
    }

    public void setImageid(int imageid) {
        this.imageid = imageid;
    }

    public String getSongname() {
        return songname;
    }

    public void setSongname(String songname) {
        this.songname = songname;
    }
}
