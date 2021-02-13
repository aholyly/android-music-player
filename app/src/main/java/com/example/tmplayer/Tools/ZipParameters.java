package com.example.tmplayer.Tools;

import android.widget.ImageView;

import com.example.tmplayer.Models.MusicCategory;

import java.io.File;

public class ZipParameters {

    File dest;
    File zip;
    ImageView imageView;
    boolean status;

    MusicCategory musicCategory;

    public ZipParameters(File dest, File zip, ImageView imageView, MusicCategory musicCategory) {
        this.dest = dest;
        this.zip = zip;
        this.imageView = imageView;
        this.musicCategory = musicCategory;
    }

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public ImageView getImageView() {
        return imageView;
    }

    public void setImageView(ImageView imageView) {
        this.imageView = imageView;
    }

    public File getDest() {
        return dest;
    }

    public void setDest(File dest) {
        this.dest = dest;
    }

    public File getZip() {
        return zip;
    }

    public void setZip(File zip) {
        this.zip = zip;
    }
}
