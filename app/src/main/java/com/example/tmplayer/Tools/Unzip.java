package com.example.tmplayer.Tools;

import android.os.AsyncTask;
import android.util.Log;

import com.example.tmplayer.R;
import com.github.junrar.Junrar;
import com.github.junrar.exception.RarException;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.LinkedList;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Unzip extends AsyncTask<ZipParameters, String, ZipParameters> {

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
    protected ZipParameters doInBackground(ZipParameters... params) {

        boolean status = unzip(params[0].getDest(), params[0].getZip());

        params[0].setStatus(status);

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
    protected void onPostExecute(ZipParameters zipped) {
        // dismiss the dialog after the file was downloaded
//            dismissDialog(progress_bar_type);

        if (zipped.getStatus() == true)
        {
            zipped.imageView.setImageResource(R.drawable.ic_downloaded);
            zipped.musicCategory.setDownloaded(true);
        }

    }


    public static boolean unzip(File extractPath, File zipPath) {
        try {
            String fileZip = zipPath.toString();
            File destDir = extractPath;
            byte[] buffer = new byte[40960];
            ZipInputStream zis = new ZipInputStream(new FileInputStream(fileZip));
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                File newFile = newFile(destDir, zipEntry);
                if (zipEntry.isDirectory()) {
                    if (!newFile.isDirectory() && !newFile.mkdirs()) {
                        throw new Exception("Failed to create directory " + newFile);
                    }
                } else {
                    // fix for Windows-created archives
                    File parent = newFile.getParentFile();
                    if (!parent.isDirectory() && !parent.mkdirs()) {
                        throw new Exception("Failed to create directory " + parent);
                    }

                    // write file content
                    FileOutputStream fos = new FileOutputStream(newFile);
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        fos.write(buffer, 0, len);
                    }
                    fos.close();
                }
                zipEntry = zis.getNextEntry() ;
            }
            zis.closeEntry();
            zis.close();

            return true;


        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }

    }

    public static File newFile(File destinationDir, ZipEntry zipEntry) throws Exception {
        File destFile = new File(destinationDir, zipEntry.getName());

        String destDirPath = destinationDir.getCanonicalPath();
        String destFilePath = destFile.getCanonicalPath();

        if (!destFilePath.startsWith(destDirPath + File.separator)) {
            throw new Exception("Entry is outside of the target dir: " + zipEntry.getName());
        }

        return destFile;
    }
}
