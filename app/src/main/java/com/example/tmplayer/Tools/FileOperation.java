package com.example.tmplayer.Tools;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class FileOperation {

    public static String readFile(File file) throws Exception {
        FileReader fileReader = new FileReader(file);
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        StringBuilder stringBuilder = new StringBuilder();
        String line = bufferedReader.readLine();
        while (line != null){
            stringBuilder.append(line).append("\n");
            line = bufferedReader.readLine();
        }
        bufferedReader.close();

        return stringBuilder.toString();
    }

}
