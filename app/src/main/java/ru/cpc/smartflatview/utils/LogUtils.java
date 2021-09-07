package ru.cpc.smartflatview.utils;

import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class LogUtils {

    static public void goInfo(String tag,String text) {
        Log.d(tag,text);
        appendLog(getTime() + " " + text);
    }
    static public void goError(String tag,String text) {
        Log.e(tag,text);
        appendLog(getTime() + " " + text);
    }

    static private String getTime(){
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
    static public void appendLog(String text) {
        String logDirPath = "sdcard/SFViewer/";
        File logDir = new File(logDirPath);
        File logFile = new File(logDirPath + "log.file");
        if (!logDir.exists()) {
            try {
                logDir.mkdir();
                logFile.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        try {
            //BufferedWriter for performance, true to set append to file flag
            BufferedWriter buf = new BufferedWriter(new FileWriter(logFile, true));
            buf.append(text);
            buf.newLine();
            buf.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}