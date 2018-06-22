package com.example.apple.musicbox;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.apple.musicbox.Tools.Item;
import com.example.apple.musicbox.Tools.SongsAdapter;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PlayerActivity extends AppCompatActivity {
    public static final String INDEX_TAG = "songIndex";

    public static final float HALF_VOLUME = 0.5f;
    public static final int HALF_VOLUME_POSITION = 50;

    public static final float ZERO_VOLUME = 0.0f;
    private static final int ZERO_VOLUME_POSITION = 0;

    private static int buffer_position;
    private static float buffer_volume;

    @BindView(R.id.control_button)
    ImageView controlButton;

    @BindView(R.id.volume_button)
    ImageView volumeButton;

    @BindView(R.id.track_seek_bar)
    SeekBar trackSeekBar;

    @BindView(R.id.volume_seek_bar)
    SeekBar volumeSeekBar;

    @BindView(R.id.current_time_view)
    TextView currentTimeView;

    @BindView(R.id.max_time_view)
    TextView maxTimeView;

    Item song;
    MediaPlayer audio;
    Handler positionHandler;
    PositionUpdater positionUpdater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_player);
        ButterKnife.bind(this);

        song = getValue();

        setData();
        setEventsListener();
        setPositionHandler();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        audio.stop();
    }

    private Item getValue() {
        Intent intent = getIntent();
        int index = intent.getIntExtra(INDEX_TAG,0);
        Item item = SongsAdapter.SONGS_ADAPTER.get(index);

        return item;
    }

    private void setData() {
        maxTimeView.setText(song.getTime(song.getDuration()));
        audio = song.getSong();

        trackSeekBar.setMax(audio.getDuration());
        volumeSeekBar.setProgress(HALF_VOLUME_POSITION);

        audio.setVolume(HALF_VOLUME,HALF_VOLUME);
        audio.setLooping(true);
    }

    private void setEventsListener() {
        trackSeekBar.setOnSeekBarChangeListener(new TrackChangeListener());
        volumeSeekBar.setOnSeekBarChangeListener(new VolumeChangeListener());

        controlButton.setOnClickListener(new ControlButtonOnClick());
        volumeButton.setOnClickListener(new VolumeButtonOnClick());
    }

    private void setPositionHandler() {
      positionHandler = new PositionHandler();
      positionUpdater = new PositionUpdater();

      positionUpdater.start();
    }

    private class ControlButtonOnClick implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (audio.isPlaying()){
                controlButton.setImageResource(R.drawable.play);
                audio.pause();
            }else{
                controlButton.setImageResource(R.drawable.pause);
                audio.start();
            }
        }
    }

    private class VolumeButtonOnClick implements View.OnClickListener {

        @Override
        public void onClick(View view) {
            if (volumeSeekBar.getProgress() > 0) {
                buffer_position = volumeSeekBar.getProgress();
                buffer_volume = buffer_position / 100f;

                volumeButton.setImageResource(R.drawable.disable_volume);
                volumeSeekBar.setProgress(ZERO_VOLUME_POSITION);
                audio.setVolume(ZERO_VOLUME,ZERO_VOLUME);
            }else {
                volumeButton.setImageResource(R.drawable.volume);
                volumeSeekBar.setProgress(buffer_position);
                audio.setVolume(buffer_volume,buffer_volume);
            }
        }
    }

    private class TrackChangeListener implements SeekBar.OnSeekBarChangeListener {

        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            if (fromUser){
                audio.seekTo(progress);
                currentTimeView.setText(song.getTime(audio.getCurrentPosition()));
            }
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

    private class VolumeChangeListener implements SeekBar.OnSeekBarChangeListener {
        @Override
        public void onProgressChanged(SeekBar seekBar, int progress, boolean b) {
            float volume = progress / 100f;
            audio.setVolume(volume,volume);
        }

        @Override
        public void onStartTrackingTouch(SeekBar seekBar) {

        }

        @Override
        public void onStopTrackingTouch(SeekBar seekBar) {

        }
    }

    private class PositionHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            int currentPosition = msg.what;

            trackSeekBar.setProgress(currentPosition);
            currentTimeView.setText(song.getTime(currentPosition));
        }
    }

    private class PositionUpdater extends Thread {
        @Override
        public void run() {
            while (true) {
                int currentPosition = audio.getCurrentPosition();
                positionHandler.sendEmptyMessage(currentPosition);
            }
        }
    }

}
