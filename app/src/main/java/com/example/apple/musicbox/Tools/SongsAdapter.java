package com.example.apple.musicbox.Tools;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.apple.musicbox.PlayerActivity;
import com.example.apple.musicbox.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.SongsHolder> {
    //Вставки большого количества елементов не будет, но много запросов елемента по индексу
    //Потому юзаем ArrayList
    private static final List<Item> songsList = new ArrayList<>();

    public static final SongsAdapter SONGS_ADAPTER = new SongsAdapter();

    public void add(Item song){
        songsList.add(song);
        notifyDataSetChanged();
    }

    public Item get(int index){
        return songsList.get(index);
    }

    @NonNull
    @Override
    public SongsHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemLayout = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item,null);
        SongsHolder songsHolder = new SongsHolder(itemLayout);

        return songsHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SongsHolder songsHolder, int i) {
        songsHolder.bind(i);
    }

    @Override
    public int getItemCount() {
        return songsList.size();
    }

    public class SongsHolder extends RecyclerView.ViewHolder{
        @BindView(R.id.title_view)
        TextView titleView;

        @BindView(R.id.time_view)
        TextView timeView;

        @BindView(R.id.start_button)
        ImageView startButton;

        public SongsHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        private void bind(int index){
            Item song = songsList.get(index);

            titleView.setText(song.getTitle());
            timeView.setText(song.getTime(song.getDuration()));

            OnStartClick onStartClick = new OnStartClick(index);
            startButton.setOnClickListener(onStartClick);
        }

        private class OnStartClick implements View.OnClickListener{
            int index;
            Context context = itemView.getContext();

            public OnStartClick(int index) {
                this.index = index;
            }

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, PlayerActivity.class);
                intent.putExtra(PlayerActivity.INDEX_TAG,index);

                context.startActivity(intent);
            }
        }
    }

}
