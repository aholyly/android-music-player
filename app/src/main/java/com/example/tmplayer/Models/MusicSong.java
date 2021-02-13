package com.example.tmplayer.Models;

import java.io.File;

public class MusicSong {

    String name;
    String artist;
    File path;

    public MusicSong(String name, String artist, File path) {
        this.name = name;
        this.artist = artist;
        this.path = path;
    }

    public String getArtist() {
        return artist;
    }

    public void setArtist(String artist) {
        this.artist = artist;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public File getPath() {
        return path;
    }

    public void setPath(File path) {
        this.path = path;
    }
}
