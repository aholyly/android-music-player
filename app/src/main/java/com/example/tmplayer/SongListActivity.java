package com.example.tmplayer;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tmplayer.Models.MusicSong;
import com.example.tmplayer.Models.PlayerNavigationModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.LinkedList;

public class SongListActivity extends AppCompatActivity {

    String albumId;
    String artistName;
    String title;
    String titleId;
    String category;
    String appFolder;
    String songsJsonString;
    boolean downloaded = false;

    LinkedList<MusicSong> songsLinkedList;

    TextView titleTextView;
    TextView categoryTextView;
    ListView songsListView;

    Intent playIntent;
    private MusicService musicSrv;
    private boolean musicBound=false;

    TextView currentSongTextView;
    TextView currentArtistTextView;
    TextView currentTimeTextView;
    TextView durationTextView;
    SeekBar seekBar;
    ImageView playButton;
    ImageView previousButton;
    ImageView nextButton;

    PlayerNavigationModel playerNavigationModel;

    Handler mHandler;

    //connect to the service
    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            //get service
            musicSrv = binder.getService();
            musicBound = true;

            if (musicSrv.isPlaying()) {
                playButton.setImageResource(R.drawable.ic_pause_icon);
            } else {
                playButton.setImageResource(R.drawable.ic_play_icon);
            }

            playButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ImageView imageView = (ImageView) v;
                    if (musicSrv.isPlaying()) {
                        musicSrv.pause();
                        imageView.setImageResource(R.drawable.ic_play_icon);
                    }
                    else {
                        if (musicSrv.getCurrentSong() != null) {
                            musicSrv.play();
                            imageView.setImageResource(R.drawable.ic_pause_icon);
                        }
                    }
                }
            });

            nextButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    musicSrv.nextSong();
                }
            });

            previousButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    musicSrv.previousSong();
                }
            });

            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if (fromUser)
                        musicSrv.seekTo(progress);
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });

            mHandler = new Handler();
            SongListActivity.this.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    playerNavigationModel.newSong(musicSrv);
                    if (musicSrv.ended) {
//                        playerNavigationModel.getPlayButton().setImageResource(R.drawable.ic_play_icon);
                    }
                    mHandler.postDelayed(this, 1000);
                }
            });
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            musicBound = false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.album_page);

        playIntent = new Intent(this, MusicService.class);
        bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
        startService(playIntent);

        titleTextView = findViewById(R.id.songListTitleTextView);
        categoryTextView = findViewById(R.id.songListCategoryTextView);
        songsListView = findViewById(R.id.songsListView);

        albumId = getIntent().getStringExtra("albumId");
        title = getIntent().getStringExtra("title");
        artistName = getIntent().getStringExtra("artistName");
        titleId = getIntent().getStringExtra("titleId");
        category = getIntent().getStringExtra("category");
        appFolder = getIntent().getStringExtra("appFolder");
        songsJsonString = getIntent().getStringExtra("songs");
        downloaded = Boolean.parseBoolean(getIntent().getStringExtra("downloaded"));


        currentSongTextView = findViewById(R.id.currentSongTextView);
        currentArtistTextView = findViewById(R.id.currentArtistTextView);
        currentTimeTextView = findViewById(R.id.currentTimeTextView);
        durationTextView = findViewById(R.id.durationTextView);
        seekBar = findViewById(R.id.musicSeekBar);
        playButton = findViewById(R.id.playPauseImageView);
        previousButton = findViewById(R.id.previousImageView);
        nextButton = findViewById(R.id.nextImageView);



        playerNavigationModel = new PlayerNavigationModel(currentSongTextView, currentArtistTextView, currentTimeTextView, durationTextView, seekBar, playButton);


        songsLinkedList = new LinkedList<>();
        LinkedList<String> stringLinkedList = new LinkedList<>();

        try {
            JSONArray songsJsonArray = new JSONArray(songsJsonString);

            for (int i = 0; i < songsJsonArray.length(); i++) {
                JSONObject obj = songsJsonArray.getJSONObject(i);
                String fileName = parseFileName(obj.getString("fileName"));

                songsLinkedList.add( new MusicSong(obj.getString("name"), artistName,new File(appFolder + "/music/" + titleId  + "/" + fileName)) );
                stringLinkedList.add(obj.getString("name"));
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        titleTextView.setText(title);
        categoryTextView.setText(category);

        ArrayAdapter<String> itemsAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, stringLinkedList);

        songsListView.setAdapter(itemsAdapter);
        songsListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (downloaded) {
                    playButton.setImageResource(R.drawable.ic_pause_icon);
                    musicSrv.setSource(songsLinkedList, position);
                    playerNavigationModel.newSong(musicSrv);
                } else {
                    Toast.makeText(getApplicationContext(),"Download category first!", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    private String parseFileName(String path) {
//        int slash = path.lastIndexOf('/');
//        int dot = path.lastIndexOf('.');
//
//        return path.substring(slash + 1, dot) + ".mp3";


        int indexOfQuestion = path.indexOf('?');
        path = path.substring(indexOfQuestion+1);
        int firstIndexOfSlash = path.indexOf('/');
        path = path.substring(firstIndexOfSlash+1);
        int indexOfDot = path.lastIndexOf('.');
        path = path.substring(0, indexOfDot);
        String str = path + ".mp3";

        return str;
    }

}
