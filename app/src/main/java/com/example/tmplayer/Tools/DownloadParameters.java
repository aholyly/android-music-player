package com.example.tmplayer.Tools;

import android.content.Context;
import android.net.Uri;
import android.widget.ImageView;

import com.example.tmplayer.Models.MusicCategory;

import java.io.File;

public class DownloadParameters {
    File downloadPath;
    String url;

    int downloaded;
    int totalSize;

    File extractFolder;
    File zipFile;
    ImageView imageView;

    MusicCategory musicCategory;

    public DownloadParameters(File downloadPath, String url) {
        this.downloadPath = downloadPath;
        this.url = url;
        extractFolder = null;
        zipFile = null;
    }

    public DownloadParameters(File downloadPath, String url, File extractFolder, File zipFile, ImageView view) {
        this.downloadPath = downloadPath;
        this.url = url;
        this.extractFolder = extractFolder;
        this.zipFile = zipFile;
        this.imageView = view;
    }

    public DownloadParameters(MusicCategory musicCategory,File downloadPath, String url, File extractFolder, File zipFile, ImageView view) {
        this.downloadPath = downloadPath;
        this.url = url;
        this.extractFolder = extractFolder;
        this.zipFile = zipFile;
        this.imageView = view;
        this.musicCategory = musicCategory;
    }





    public File getDownloadPath() {
        return downloadPath;
    }

    public void setDownloadPath(File downloadPath) {
        this.downloadPath = downloadPath;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getDownloaded() {
        return downloaded;
    }

    public void setDownloaded(int downloaded) {
        this.downloaded = downloaded;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }
}
