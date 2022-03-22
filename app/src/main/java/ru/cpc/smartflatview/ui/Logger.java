package ru.cpc.smartflatview.ui;

import android.util.Log;

import java.util.ArrayList;

public class Logger
{
    public static Logger Instance = new Logger();

    public ArrayList<String> m_cDebugLines = new ArrayList<>();

    public void AddDebugInfo(String message)
    {
        Log.d("LOGGER", message);

    }
}
