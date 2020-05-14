package ru.cpc.smartflatview.importing.presenter;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import moxy.MvpPresenter;
import okhttp3.ResponseBody;
import ru.cpc.smartflatview.importing.model.retrofit.RetrofitApi;
import ru.cpc.smartflatview.importing.model.retrofit.data.GsonDate;
import ru.cpc.smartflatview.importing.ui.ImportActivity;
import ru.cpc.smartflatview.importing.ui.ImportView;

public class ImportPresenter extends MvpPresenter<ImportView> {
    private Single<GsonDate> filesList;
    private Disposable disposable;
    private RetrofitApi myApi;

    public ImportPresenter() {
        myApi = new RetrofitApi();
    }

    public void initParam(String ip,String port){
        myApi.initParam(ip,port);
    }
    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getList(){
        if(disposable!=null && !disposable.isDisposed())
            return;
        filesList = myApi.requestFilesList();

        disposable = filesList.observeOn(AndroidSchedulers.mainThread()).subscribe(
                s -> getViewState().callbackListFiles(s.getList()),
                e -> toLog("Ошибка:"+e.getMessage()));
    }

    public void getOneFile(String name){
        if(disposable!=null && !disposable.isDisposed())
            return;
        filesList = myApi.requestFilesList();
        Single<ResponseBody> oneFile2 = myApi.requestFileByName2(name);

        disposable = oneFile2.observeOn(AndroidSchedulers.mainThread()).subscribe(
                s -> getViewState().callbackOneFile(s.byteStream()),
                e -> toLog("Ошибка:"+e.getMessage()));
    }


    private static void toLog(String s){
        Log.d(ImportActivity.TAG,"Поток["+Thread.currentThread().getName()+"] "+s);
    }

}
