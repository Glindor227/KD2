package ru.cpc.smartflatview;

import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.Locale;

import ru.cpc.smartflatview.ui.MediaLibraryDialog;


public class PlaylistDialog extends DialogFragment{

    protected Button firstButton;
    protected Button prevButton;
    protected Button nextButton;
    protected Button lastButton;
    protected Button clearButton;
    protected Button addButton;
    protected Button loadButton;

    protected TextView line01;
    protected TextView line02;
    protected TextView line03;
    protected TextView line04;
    protected TextView line05;
    protected TextView line06;
    protected TextView line07;
    protected TextView line08;
    protected TextView line09;
    protected TextView line10;

    protected TextView firstLine;
    protected TextView linesCount;

    public String m_sTitle;
    private TextView m_sLastSelected = null;

    private OnPlaylistDialogListener callback = null;

    public interface OnPlaylistDialogListener {
        void onPlaylistDialogSubmit(String friendEmail);
    }

    public void SetCallback(OnPlaylistDialogListener _callback){
        callback = _callback;
    }

    protected class LineClickListener implements View.OnClickListener
    {
        int m_iLine;
        private TextView m_pView;

        LineClickListener(int iLine, TextView pView)
        {
            m_iLine = iLine;
            m_pView = pView;
        }
        @Override
        public void onClick(View v) {
            m_sTitle = (String)m_pView.getText();
            Log.d("111111111111", "Selected '" + m_sTitle + "'/'" + m_pView.getText() + "'");
            if(m_sLastSelected != null)
            {
                m_sLastSelected.setBackgroundColor(getResources().getColor(R.color.colorAccent));
            }
            m_pView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            m_sLastSelected = m_pView;

            if(callback != null)
                callback.onPlaylistDialogSubmit(m_sTitle);
            //PlaylistDialog.this.dismiss();
        }
    }

    private int m_iFirst = 0;
    private int m_iTotal = 40;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle(R.string.titlePlaylist);
        View v = inflater.inflate(R.layout.playlist_dialog, null);//TODO разобраться, что делать с желтым  null. Если не null - падает.
        //Toolbar toolbar = (Toolbar)v.findViewById(R.id.toolbar);
        //v.setSupportActionBar(toolbar);

        firstButton =v.findViewById(R.id.PL_start);
        prevButton = v.findViewById(R.id.PL_back);
        nextButton = v.findViewById(R.id.PL_next);
        lastButton = v.findViewById(R.id.PL_finish);

        firstButton.setOnClickListener(v12 -> {
            m_iFirst = 0;
            Update();
        });

        nextButton.setOnClickListener(v13 -> {
            if(m_iFirst + 10 < m_iTotal)
            {
                m_iFirst += 10;
                Update();
            }
        });

        prevButton.setOnClickListener(v14 -> {
            if(m_iFirst - 10 >= 0)
            {
                m_iFirst -= 10;
                Update();
            }
        });

        lastButton.setOnClickListener(v15 -> {
            m_iFirst = m_iTotal-11;
            Update();
        });

        clearButton = v.findViewById(R.id.PL_clear);
        addButton = v.findViewById(R.id.PL_add);
        loadButton = v.findViewById(R.id.PL_load);

        addButton.setOnClickListener(v1 -> {
            MediaLibraryDialog dlg = new MediaLibraryDialog();
            dlg.show(getFragmentManager(), "dlg");

        });

        firstLine = v.findViewById(R.id.PL_first);
        linesCount = v.findViewById(R.id.PL_total);

        line01 = v.findViewById(R.id.PL_line01);
        line02 = v.findViewById(R.id.PL_line02);
        line03 = v.findViewById(R.id.PL_line03);
        line04 = v.findViewById(R.id.PL_line04);
        line05 = v.findViewById(R.id.PL_line05);
        line06 = v.findViewById(R.id.PL_line06);
        line07 = v.findViewById(R.id.PL_line07);
        line08 = v.findViewById(R.id.PL_line08);
        line09 = v.findViewById(R.id.PL_line09);
        line10 = v.findViewById(R.id.PL_line10);

        line01.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
        m_sLastSelected = line01;

        line01.setOnClickListener(new LineClickListener(1, line01));
        line02.setOnClickListener(new LineClickListener(2, line02));
        line03.setOnClickListener(new LineClickListener(3, line03));
        line04.setOnClickListener(new LineClickListener(4, line04));
        line05.setOnClickListener(new LineClickListener(5, line05));
        line06.setOnClickListener(new LineClickListener(6, line06));
        line07.setOnClickListener(new LineClickListener(7, line07));
        line08.setOnClickListener(new LineClickListener(8, line08));
        line09.setOnClickListener(new LineClickListener(9, line09));
        line10.setOnClickListener(new LineClickListener(10, line10));

        return v;
    }

    private void Update()
    {
        firstLine.setText(String.valueOf(m_iFirst+1));
        linesCount.setText(String.valueOf(m_iTotal));

        line01.setText(String.format(Locale.US, "%s%d", getString(R.string.mediaTrack), m_iFirst + 1));
        line02.setText(String.format(Locale.US, "%s%d", getString(R.string.mediaTrack), m_iFirst + 2));
        line03.setText(String.format(Locale.US, "%s%d", getString(R.string.mediaTrack), m_iFirst + 3));
        line04.setText(String.format(Locale.US, "%s%d", getString(R.string.mediaTrack), m_iFirst + 4));
        line05.setText(String.format(Locale.US, "%s%d", getString(R.string.mediaTrack), m_iFirst + 5));
        line06.setText(String.format(Locale.US, "%s%d", getString(R.string.mediaTrack), m_iFirst + 6));
        line07.setText(String.format(Locale.US, "%s%d", getString(R.string.mediaTrack), m_iFirst + 7));
        line08.setText(String.format(Locale.US, "%s%d", getString(R.string.mediaTrack), m_iFirst + 8));
        line09.setText(String.format(Locale.US, "%s%d", getString(R.string.mediaTrack), m_iFirst + 9));
        line10.setText(String.format(Locale.US, "%s%d", getString(R.string.mediaTrack), m_iFirst + 10));
    }
}
