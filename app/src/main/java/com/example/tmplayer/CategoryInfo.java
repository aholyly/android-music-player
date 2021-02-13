package com.example.tmplayer;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.example.tmplayer.Models.MusicCategory;
import com.example.tmplayer.Tools.DownloadCenter;
import com.example.tmplayer.Tools.DownloadParameters;
import com.example.tmplayer.Tools.FileOperation;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.LinkedList;

public class CategoryInfo {

    Context ctx;
    File appFolder;
    File jsonFolder;
    File coverFolder;

    String categoriesJson;
    LinkedList<MusicCategory> musicCategories;

    public CategoryInfo(Context ctx, File appFolder) {
        this.ctx = ctx;
        this.appFolder = appFolder;
        jsonFolder = new File(appFolder + "/json");
        coverFolder = new File(appFolder + "/cover");
        musicCategories = new LinkedList<>();
    }

    public boolean check() {

        File categoriesJsonFile = new File(jsonFolder + "/categories.json");

        try {

            if (!categoriesJsonFile.exists()) {
                AsyncTask downloader = new DownloadCenter().execute(
                        new DownloadParameters(categoriesJsonFile, "https://saattek.tk/contents/mert/json/categories.json"));
//            AsyncTask downloader = new DownloadCenter().execute("https://saattek.tk/contents/mert/json/categories.json", categoriesJsonFile.toString());

                DownloadParameters downloaded = (DownloadParameters) downloader.get();
            }

            categoriesJson = FileOperation.readFile(categoriesJsonFile);


        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public LinkedList<MusicCategory> getCategories() {

        check();

        try {
            JSONObject jsonObject  = new JSONObject(categoriesJson);

            JSONArray categories = jsonObject.getJSONArray("categories");


            for (int i = 0; i < categories.length(); i++) {
                JSONObject obj = categories.getJSONObject(i);

                MusicCategory musicCategory = new MusicCategory(appFolder,obj.getString("id"), obj.getString("categoryUri"), obj.getString("zipUri"),obj.getString("displayName"));
                musicCategories.add(musicCategory);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return musicCategories;
    }


}
