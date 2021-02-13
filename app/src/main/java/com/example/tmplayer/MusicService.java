 package com.example.tmplayer;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentUris;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.tmplayer.Models.MusicSong;
import com.example.tmplayer.Tools.DownloadCenter;
import com.example.tmplayer.Tools.DownloadParameters;
import com.example.tmplayer.Tools.MusicPlayer;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Random;

public class MusicService extends Service {

    String text = "inital";
    MediaPlayer player;

    LinkedList<MusicSong> playList;
    int currentIndex;

    MusicSong currentSong = null;

    boolean ended = true;
    boolean playing = false;

    private final IBinder musicBind = new MusicBinder();

    public void onCreate(){
        //create the service
        super.onCreate();
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public class MusicBinder extends Binder {
        MusicService getService() {
            return MusicService.this;
        }
    }

    public MusicSong getCurrentSong() {
        return currentSong;
    }

    public void setCurrentSong(MusicSong currentSong) {
        this.currentSong = currentSong;
    }

    @Override
    public IBinder onBind(Intent intent) {
        return musicBind;
    }


    @Override
    public boolean onUnbind(Intent intent){
//        player.stop();
//        player.release();
        return false;
    }

    public String getDuration() {
        if (currentSong != null) {
            int duration = player.getDuration() /1000;
            int min = duration / 60;
            int sec = duration % 60;

            return String.valueOf(min) + ":" + (sec < 10?"0"+String.valueOf(sec):String.valueOf(sec));
        }

        return "0:00";
    }

    public int getDurationInt() {
        return player.getDuration() / 1000;
    }

    public int getCurrentInt() {
        return player.getCurrentPosition() / 1000;
    }

    public boolean isEnded() {
        return ended;
    }

    public String getCurrentTime() {
        if (currentSong != null) {
            int duration = player.getCurrentPosition() /1000;
            int min = duration / 60;
            int sec = duration % 60;



            return String.valueOf(min) + ":" + (sec < 10?"0"+String.valueOf(sec):String.valueOf(sec));
        }

        return "0:00";
    }

    public void seekTo(int time) {
        player.seekTo(time * 1000);
    }

    public void play() {
        if (player != null) {
            playing = true;
            player.start();
        }
    }

    public void pause() {
        if (player != null) {
            playing = false;
            player.pause();
        }
    }

    public void setEnded(boolean ended) {
        this.ended = ended;
    }

    public boolean isPlaying() {
        return playing;
    }

    public void setPlaying(boolean playing) {
        this.playing = playing;
    }

    @Override
    public void onDestroy() {
        stopForeground(true);
    }

    public void setSource(MusicSong song) {
        currentSong = song;
        ended = false;

        if (player != null)
            player.reset();
        player = MediaPlayer.create(this, Uri.parse(song.getPath().toString()));
        player.start();
        playing = true;

        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                ended = true;
                playing = false;

            }
        });
    }

    public void nextSong() {
        if (!isLastSong()) {
            currentIndex++;
            setSource(playList.get(currentIndex));
        }
    }

    public void previousSong() {
        if (getCurrentInt() > 2) {
            player.seekTo(0);
        } else {
            if (currentIndex != 0) {
                currentIndex--;
                setSource(playList.get(currentIndex));
            }
        }
    }

    public boolean isLastSong() {
        return playList.size() == (currentIndex + 1);
    }

    public void setSource(LinkedList<MusicSong> songs, int position) {
        playList = songs;
        currentIndex = position;
        setSource(songs.get(position));
    }

}
