package com.example.tmplayer.Models;

import android.app.DownloadManager;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.tmplayer.R;
import com.example.tmplayer.Tools.DownloadCenter;
import com.example.tmplayer.Tools.DownloadParameters;
import com.example.tmplayer.Tools.FileOperation;
import com.example.tmplayer.Tools.Unzip;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class MusicCategory {

    File musicFolder;

    String id;
    String categoryUri;
    String zipUri;
    String displayName;
    boolean downloaded;

    public boolean isDownloaded() {
        return downloaded;
    }

    public void setDownloaded(boolean downloaded) {
        this.downloaded = downloaded;
    }

    public MusicCategory(File appFolder, String id, String categoryUri, String zipUri, String displayName) {
        this.id = id;
        this.categoryUri = categoryUri;
        this.displayName = displayName;
        this.zipUri = zipUri;
        this.downloaded = false;
        check(appFolder);
    }



    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategoryUri() {
        return categoryUri;
    }

    public void setCategoryUri(String categoryUri) {
        this.categoryUri = categoryUri;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public boolean download(Context ctx, File appFolder, ImageView v) {

        musicFolder = new File(appFolder + "/music");

        File categoryFolder = new File(musicFolder, id);
        categoryFolder.mkdir();

        File categoryZipFile = new File(categoryFolder + File.separator + id + ".zip");

        categoryZipFile.delete();

        try {
            AsyncTask downloader = new DownloadCenter().execute(
                    new DownloadParameters(this, categoryZipFile, zipUri, categoryFolder, categoryZipFile, v));

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return false;
    }

    public void check(File appFolder) {
        File zipFile = new File(appFolder + "/music/" + id + "/" + id + ".zip");

        if (zipFile.exists()) {
            downloaded = true;
        }
    }

    @Override
    public String toString() {
        return "MusicCategory{" +
                "id='" + id + '\'' +
                ", categoryUri='" + categoryUri + '\'' +
                ", diplayName='" + displayName + '\'' +
                '}';
    }


}
