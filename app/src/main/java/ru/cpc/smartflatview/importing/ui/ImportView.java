package ru.cpc.smartflatview.importing.ui;

import java.io.InputStream;
import java.util.List;

import moxy.MvpView;
import moxy.viewstate.strategy.alias.AddToEndSingle;

public interface ImportView extends MvpView {
    @AddToEndSingle
    void callbackListFiles(List<String> list);
    @AddToEndSingle
    void callbackOneFile(InputStream file);

}
