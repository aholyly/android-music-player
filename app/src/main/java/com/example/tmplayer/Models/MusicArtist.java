package com.example.tmplayer.Models;

import org.json.JSONArray;

import java.util.LinkedList;

public class MusicArtist {

    String name;
    String id;
    JSONArray songs;
    boolean downloaded = false;

    public MusicArtist(String name, JSONArray songs, String id) {
        this.name = name;
        this.songs = songs;
        this.id = id;
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public JSONArray getSongs() {
        return songs;
    }

    public void setSongs(JSONArray songs) {
        this.songs = songs;
    }
}
