package com.example.tmplayer.Tools;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;

import java.io.File;

public class MusicPlayer
{
    // static variable single_instance of type Singleton
    static MusicPlayer single_instance = null;
    private static Context ctx;
    private MediaPlayer mediaPlayer;

    // variable of type String
    public String s;

    // private constructor restricted to this class itself
    private MusicPlayer(Context ctx)
    {
        this.ctx = ctx;
    }

    // static method to create instance of Singleton class
    public static MusicPlayer getInstance()
    {
        if (single_instance == null)
            single_instance = new MusicPlayer(ctx);

        return single_instance;
    }

    public void setSource(File musicPath) {
        mediaPlayer = MediaPlayer.create(ctx, Uri.parse(musicPath.toString()));
        play();
    }

    public void play() {
        mediaPlayer.start();
    }

    public void pause() {
        mediaPlayer.pause();
    }
}

