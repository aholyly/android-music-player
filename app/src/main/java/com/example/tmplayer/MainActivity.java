package com.example.tmplayer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.tmplayer.Models.MusicCategory;
import com.example.tmplayer.Models.MusicSong;
import com.example.tmplayer.Models.PlayerNavigationModel;
import com.example.tmplayer.Tools.MusicPlayer;

import java.io.File;
import java.util.LinkedList;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    RecyclerView categoryRecyclerView;
    LinkedList<MusicCategory> musicCategories;

    TextView currentSongTextView;
    TextView currentArtistTextView;
    TextView currentTimeTextView;
    TextView durationTextView;
    SeekBar seekBar;
    ImageView playButton;
    ImageView nextButton;
    ImageView previousButton;

    PlayerNavigationModel playerNavigationModel;

    private Handler mHandler;



    File appFolder;
    File jsonFolder;
    File dataFolder;
    File musicFolder;

    Intent playIntent;
    private MusicService musicSrv;
    private boolean musicBound=false;

    //connect to the service
    private ServiceConnection musicConnection = new ServiceConnection(){

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MusicService.MusicBinder binder = (MusicService.MusicBinder) service;
            //get service
            musicSrv = binder.getService();
            musicBound = true;

            mHandler = new Handler();
             MainActivity.this.runOnUiThread(new Runnable() {

                @Override
                public void run() {
                    playerNavigationModel.newSong(musicSrv);
                    if (musicSrv.ended) {
                        playerNavigationModel.getPlayButton().setImageResource(R.drawable.ic_play_icon);
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


    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        verifyStoragePermissions(this);

        ActivityCompat.requestPermissions(MainActivity.this,
                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                1);

        playIntent = new Intent(this, MusicService.class);
        bindService(playIntent, musicConnection, Context.BIND_AUTO_CREATE);
        startService(playIntent);

        currentSongTextView = findViewById(R.id.currentSongTextView);
        currentArtistTextView = findViewById(R.id.currentArtistTextView);
        currentTimeTextView = findViewById(R.id.currentTimeTextView);
        durationTextView = findViewById(R.id.durationTextView);
        seekBar = findViewById(R.id.musicSeekBar);
        playButton = findViewById(R.id.playPauseImageView);
        nextButton = findViewById(R.id.nextImageView);
        previousButton = findViewById(R.id.previousImageView);

        playerNavigationModel = new PlayerNavigationModel(currentSongTextView, currentArtistTextView, currentTimeTextView, durationTextView, seekBar, playButton);

        createFolders();
        createCategoriesUI();

    }

    public void createFolders() {
//        appFolder = getFilesDir();
        appFolder = new File(getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).toString());
//        appFolder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), "TurkishMusic");
//        appFolder.mkdirs();
        jsonFolder = new File(appFolder, "json");
        dataFolder = new File(appFolder, "data");
        musicFolder = new File(appFolder, "music");
        jsonFolder.mkdirs();
        dataFolder.mkdirs();
        musicFolder.mkdirs();
    }

    protected void createCategoriesUI(){

        CategoryInfo checkAlbumCover = new CategoryInfo(this, appFolder);
        musicCategories = checkAlbumCover.getCategories();

        categoryRecyclerView = findViewById(R.id.categoryRecyclerView);
        CategoryAdapter categoryAdapter = new CategoryAdapter(this, musicCategories, appFolder);
        categoryRecyclerView.setAdapter(categoryAdapter);
        categoryRecyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        stopService(playIntent);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (musicSrv != null) {
            if (musicSrv.getCurrentSong() != null) {
               playerNavigationModel.newSong(musicSrv);

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
                            musicSrv.play();
                            imageView.setImageResource(R.drawable.ic_pause_icon);
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
            }
        }
    }
}