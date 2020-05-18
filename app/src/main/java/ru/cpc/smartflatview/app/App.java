package ru.cpc.smartflatview.app;

import android.app.Application;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class App extends Application {
private static Map<String, Long> timeLabel;
private static Long beginTime;
    @Override
    public void onCreate() {
        super.onCreate();
        beginTime=0L;
        timeLabel = new HashMap<>();
    }
    public static void addTime(String label, Date time){
        timeLabel.put(label,time.getTime()-beginTime);
    }
    public static void beginTime(String label, Date time){
        timeLabel.clear();
        beginTime = time.getTime();
        addTime(label,time);
    }
    public static Map<String, Long> getTimeLabel() {
        return timeLabel;
    }
}
