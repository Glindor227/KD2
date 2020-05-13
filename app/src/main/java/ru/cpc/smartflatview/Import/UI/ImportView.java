package ru.cpc.smartflatview.Import.UI;

import java.util.List;

import moxy.MvpView;
import moxy.viewstate.strategy.alias.AddToEndSingle;

public interface ImportView<T> extends MvpView {
    @AddToEndSingle
    void callbackGo(T o);

    @AddToEndSingle
    void callbackImage(List<Byte> list);

}
