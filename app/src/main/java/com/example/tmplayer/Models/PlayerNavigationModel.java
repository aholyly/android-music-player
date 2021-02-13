package com.example.tmplayer.Models;

import android.util.Log;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.tmplayer.MusicService;
import com.example.tmplayer.R;

public class PlayerNavigationModel {

    TextView name;
    TextView artist;
    TextView current;
    TextView duration;
    SeekBar seekBar;
    ImageView playButton;

    public PlayerNavigationModel(TextView name, TextView artist, TextView current, TextView duration, SeekBar seekBar, ImageView playButton) {
        this.name = name;
        this.artist = artist;
        this.current = current;
        this.duration = duration;
        this.seekBar = seekBar;
        this.playButton = playButton;
    }

    public void newSong(MusicService srv) {

        if (srv.getCurrentSong() != null) {
            this.name.setText(srv.getCurrentSong().getName());
            this.artist.setText(srv.getCurrentSong().getArtist());
            this.current.setText(srv.getCurrentTime());
            this.duration.setText(srv.getDuration());
            this.seekBar.setMax(srv.getDurationInt());
            this.seekBar.setProgress(srv.getCurrentInt());
        }

    }

    public TextView getName() {
        return name;
    }

    public void setName(TextView name) {
        this.name = name;
    }

    public TextView getArtist() {
        return artist;
    }

    public void setArtist(TextView artist) {
        this.artist = artist;
    }

    public TextView getCurrent() {
        return current;
    }

    public void setCurrent(TextView current) {
        this.current = current;
    }

    public TextView getDuration() {
        return duration;
    }

    public void setDuration(TextView duration) {
        this.duration = duration;
    }

    public SeekBar getSeekBar() {
        return seekBar;
    }

    public void setSeekBar(SeekBar seekBar) {
        this.seekBar = seekBar;
    }

    public ImageView getPlayButton() {
        return playButton;
    }

    public void setPlayButton(ImageView playButton) {
        this.playButton = playButton;
    }
}
