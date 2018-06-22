package com.example.apple.musicbox.Tools;

import android.media.MediaPlayer;

public class Item {
    String title;
    MediaPlayer song;

    public Item(String title, MediaPlayer song) {
        this.title = title;
        this.song = song;
    }

    public String getTitle() {
        return title;
    }

    public MediaPlayer getSong() {
        return song;
    }

    public String getTime(int millis){
        StringBuilder time = new StringBuilder();
        int minutes = (int) millis / 1000 / 60;
        int seconds = (int) millis / 1000 % 60;

        if (minutes < 10)
            time.append("0" + minutes);
        else
            time.append(minutes);

        time.append(":");

        if (seconds < 10)
            time.append("0" + seconds);
        else
            time.append(seconds);

        return time.toString();
    }

    public int getDuration(){
        return song.getDuration();
    }
}
