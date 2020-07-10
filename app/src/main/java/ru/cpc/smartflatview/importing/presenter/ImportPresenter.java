package ru.cpc.smartflatview.importing.presenter;

import android.os.Build;
import android.support.annotation.RequiresApi;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;
import java.util.HashMap;
import java.util.stream.Collectors;

import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import moxy.MvpPresenter;
import okhttp3.ResponseBody;
import ru.cpc.smartflatview.app.App;
import ru.cpc.smartflatview.importing.model.retrofit.RetrofitApi;
import ru.cpc.smartflatview.importing.model.retrofit.data.GsonDate;
import ru.cpc.smartflatview.importing.ui.ImportView;

public class ImportPresenter extends MvpPresenter<ImportView> {
    private Single<GsonDate> filesList;
    private Disposable disposable;
    private RetrofitApi myApi;
    private HashMap<String,String> mapKey;
    private String idUser;
    public ImportPresenter() {
    }

    public void initParam(String ip,String port,String idUser){
        mapKey = new HashMap<>();
        myApi = new RetrofitApi(ip,port);
        this.idUser =  idUser;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void getList(){
        if(disposable!=null && !disposable.isDisposed())
            return;
        mapKey.clear();
        mapKey.put("id",idUser);
        filesList = myApi.requestFilesList(mapKey);

        disposable = filesList.observeOn(AndroidSchedulers.mainThread()).subscribe(
                s -> getViewState().callbackListFiles(
                        s.getList().stream()
                                .map(s1 -> {
                                    try {
                                        return URLDecoder.decode(s1,"UTF-8");
                                    } catch (UnsupportedEncodingException e) {
                                        e.printStackTrace();
                                    }
                                    return s1;
                                })
                                .collect(Collectors.toList())),
                e -> getViewState().callbackError(e.getMessage()));
    }

    public void getOneFile(String name){
        if(disposable!=null && !disposable.isDisposed())
            return;
        App.beginTime("1 Запросили " + name, new Date());
//        filesList = myApi.requestFilesList();

        mapKey.clear();
        mapKey.put("id",idUser);
        mapKey.put("name",name);
        Single<ResponseBody> oneFile2 = myApi.requestFileByName2(mapKey);

        disposable = oneFile2.observeOn(AndroidSchedulers.mainThread()).subscribe(
                s -> getViewState().callbackOneFile(s.byteStream()),
                e -> getViewState().callbackError(e.getMessage()));
    }

}
