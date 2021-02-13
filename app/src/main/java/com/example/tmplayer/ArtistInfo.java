package com.example.tmplayer;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.tmplayer.Models.MusicArtist;
import com.example.tmplayer.Models.MusicCategory;
import com.example.tmplayer.Models.MusicSong;
import com.example.tmplayer.Tools.DownloadCenter;
import com.example.tmplayer.Tools.DownloadParameters;
import com.example.tmplayer.Tools.FileOperation;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.LinkedList;

public class ArtistInfo {

    Context ctx;
    File appFolder;
    MusicCategory musicCategory;
    File jsonFolder;

    String categoryJson;
    LinkedList<MusicArtist> musicArtistLinkedList;

    public ArtistInfo(Context ctx, File appFolder, MusicCategory category) {
        this.ctx = ctx;
        this.appFolder = appFolder;
        this.musicCategory = category;

        jsonFolder = new File(appFolder + "/json");
        musicArtistLinkedList = new LinkedList<>();
    }

    public boolean check() {

        File categoryJsonFile = new File(jsonFolder + "/" + musicCategory.getId() + ".json");

        try {
            if (!categoryJsonFile.exists()) {

                AsyncTask downloader = new DownloadCenter().execute(
                        new DownloadParameters(categoryJsonFile, musicCategory.getCategoryUri()));
//                AsyncTask downloader = new DownloadCenter().execute(musicCategory.getCategoryUri(), categoryJsonFile.toString());

                DownloadParameters downloaded = (DownloadParameters) downloader.get();
            }

            categoryJson = FileOperation.readFile(categoryJsonFile);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public LinkedList<MusicArtist> getArtists() {

        try {
            JSONObject jsonObject  = new JSONObject(categoryJson);

            JSONArray artists = jsonObject.getJSONArray("artists");

            for (int i = 0; i < artists.length(); i++) {
                JSONObject obj = artists.getJSONObject(i);
                MusicArtist musicArtist = new MusicArtist(obj.getString("name"),null, obj.getString("id"));

                JSONArray songs = obj.getJSONArray("songs");

                musicArtist.setSongs(songs);
                musicArtistLinkedList.add(musicArtist);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return musicArtistLinkedList;

    }

}
