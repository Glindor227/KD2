package ru.cpc.smartflatview;

import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Objects;

public class LogActivity extends DialogFragment// implements DialogInterface.OnClickListener
{
    public static LogActivity newInstance(ArrayList<String> cLogLines, String title)
    {
        LogActivity f = new LogActivity();

        // Supply num input as an argument.
        Bundle b = new Bundle();
        b.putStringArrayList("log", cLogLines);
        b.putString("title", title);
        f.setArguments(b);

        return f;
    }
    ArrayList<String> cLog = null;
    String title = "Состояние связи с оборудованием:";

    @Override
    public void onStart()
    {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            Objects.requireNonNull(dialog.getWindow()).setLayout(width, height);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        cLog = getArguments().getStringArrayList("log");
        title = getArguments().getString("title");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.activity_log, null); //TODO разобраться, что делать с желтым  null. Если не null - падает.

        TextView pHeader = v.findViewById(R.id.logHeader);
        pHeader.setText(title);

        TextView pText = v.findViewById(R.id.log_text);
        pText.setText("");

        if (cLog!= null)
        {
            for(String sLine : cLog)
                pText.append(sLine + "\n\r");
        }
        return v;
    }
}
