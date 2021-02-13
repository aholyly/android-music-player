package com.example.tmplayer.Tools;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.tmplayer.R;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;


public class DownloadCenter extends AsyncTask<DownloadParameters, String, DownloadParameters> {

    /**
     * Before starting background thread Show Progress Bar Dialog
     **/
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
//            showDialog(progress_bar_type);
    }

    /**
     * Downloading file in background thread
     **/
    @Override
    protected DownloadParameters doInBackground(DownloadParameters... params) {
        int count;

        try {
            String downloadPath = params[0].downloadPath.toString();
            URL url = new URL(params[0].getUrl());
            URLConnection conection = url.openConnection();
            conection.connect();

            // this will be useful so that you can show a tipical 0-100%
            // progress bar
            int lenghtOfFile = conection.getContentLength();
            params[0].setTotalSize(lenghtOfFile);

            // download the file
            InputStream input = new BufferedInputStream(url.openStream(),
                    8192);

            // Output stream
            File old = new File(downloadPath);
            if (old.exists())
                old.delete();
            OutputStream output = new FileOutputStream(downloadPath);

            byte data[] = new byte[1024];

            long total = 0;

            while ((count = input.read(data)) != -1) {
                total += count;
                // publishing the progress....
                // After this onProgressUpdate will be called
//                publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                // writing data to file
                output.write(data, 0, count);
            }

            params[0].setDownloaded((int)total);

            // flushing output
            output.flush();

            // closing streams
            output.close();
            input.close();
        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }

        return params[0];
    }

    /**
     * Updating progress bar
     **/
    protected void onProgressUpdate(String... progress) {
        // setting progress percentage
//            pDialog.setProgress(Integer.parseInt(progress[0]));
    }

    /**
     * After completing background task Dismiss the progress dialog
     **/
    @Override
    protected void onPostExecute(DownloadParameters downloaded) {
        // dismiss the dialog after the file was downloaded
//            dismissDialog(progress_bar_type);

        if (downloaded.getTotalSize() == downloaded.getDownloaded())
        {
            if (downloaded.zipFile != null) {
                AsyncTask unzipper = new Unzip().execute(new ZipParameters(downloaded.extractFolder, downloaded.zipFile, downloaded.imageView, downloaded.musicCategory));
            }

        }
    }
}
