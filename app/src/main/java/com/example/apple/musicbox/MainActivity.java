package com.example.apple.musicbox;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;

import com.example.apple.musicbox.Tools.Item;
import com.example.apple.musicbox.Tools.SongsAdapter;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {
    public static final String DEBUG_TAG = "Mydebug";

    @BindView(R.id.songsRecycler)
    RecyclerView songsRecycler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initSongsRecycler();
        new GetSongsList().execute();
    }

    private void initSongsRecycler(){
        LinearLayoutManager layoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);

        songsRecycler.setLayoutManager(layoutManager);
        songsRecycler.setAdapter(SongsAdapter.SONGS_ADAPTER);
    }

    private class GetSongsList extends AsyncTask <Void,Void,List<Item>> {

        @Override
        protected List<Item> doInBackground(Void... voids) {
            List<Item> songs = new LinkedList<>();
            Field[] mp3s = R.raw.class.getFields();

            for (int i = 0; i < mp3s.length; i++){
                String title = mp3s[i].getName();
                int id = 0;
                try {
                    id = mp3s[i].getInt(mp3s[i]);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
                Log.d(DEBUG_TAG,title);

                try {
                    songs.add(new Item(title,MediaPlayer.create(MainActivity.this,id)));
                }catch (Exception e){
                    Log.d(DEBUG_TAG,e.getMessage());
                }

            }

            return songs;
        }


        @Override
        protected void onPostExecute(List<Item> songs) {
            super.onPostExecute(songs);
            for (Item song : songs){
                SongsAdapter.SONGS_ADAPTER.add(song);
            }
        }
    }
}
