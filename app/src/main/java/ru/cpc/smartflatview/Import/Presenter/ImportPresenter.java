package ru.cpc.smartflatview.Import.Presenter;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import moxy.MvpPresenter;
import ru.cpc.smartflatview.Import.Model.Retrofit.RetrofitApi;
import ru.cpc.smartflatview.Import.Model.Retrofit.data.ConfigFile;
import ru.cpc.smartflatview.Import.Model.Retrofit.data.GsonDate;
import ru.cpc.smartflatview.Import.UI.ImportView;

public class ImportPresenter<Tin> extends MvpPresenter<ImportView> {
//    private MainModel<Tin, GsonDate> model;
    private Single<GsonDate> single;
    private Single<ConfigFile> oneFile;
    private Disposable disposable;
    private RetrofitApi myApi;

    public ImportPresenter() {
        myApi = new RetrofitApi();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void presenterSubscribe(Tin obj){
        if(disposable!=null && !disposable.isDisposed())
            return;
//        single = myApi.requestFilesList();
        oneFile = myApi.requestFileByName("tessss");

        disposable = oneFile.observeOn(AndroidSchedulers.mainThread()).subscribe(
                s -> {
                    getViewState().callbackGo(s.name+" "+s.size);
                    getViewState().callbackImage(s.file);
                },
                e -> {
                    toLog("Ошибка:"+e.getMessage());
                    getViewState().callbackGo(e.getMessage());
                });
    }

    public void presenterUnSubscribe(){
        if(disposable!=null)
            disposable.dispose();
    }

    private static void toLog(String s){
        Log.d("Lesson4_2","Поток["+ Thread.currentThread().getName()+"] "+s);
    }

}
