package ru.cpc.smartflatview;

import android.util.Log;

import java.util.ArrayList;

class Logger
{
    static Logger Instance = new Logger();

    ArrayList<String> m_cDebugLines = new ArrayList<>();

    void AddDebugInfo(String message)
    {
        Log.d("LOGGER", message);

    }
}
