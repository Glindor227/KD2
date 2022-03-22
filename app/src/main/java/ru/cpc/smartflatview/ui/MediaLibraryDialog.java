package ru.cpc.smartflatview.ui;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import ru.cpc.smartflatview.R;


public class MediaLibraryDialog extends DialogFragment {
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle(R.string.titleMediaLibrary);

        return inflater.inflate(R.layout.medialibrary_dialog, null); //TODO разобраться, что делать с желтым  null. Если не null - падает.
    }
}
