package ru.cpc.smartflatview.Import.Model.Retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.cpc.smartflatview.Import.Model.Retrofit.data.ConfigFile;
import ru.cpc.smartflatview.Import.Model.Retrofit.data.GsonDate;

public class RetrofitApi {
    public Single<GsonDate> requestFilesList(){
        return initApi().getFileList().subscribeOn(Schedulers.io());
    }
    public Single<ConfigFile> requestFileByName(String name){
        return initApi().getFile(name).subscribeOn(Schedulers.io());
    }

    private RetrofitApiService initApi() {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create(gson);
        return new Retrofit.Builder()
                .baseUrl("http://192.168.1.65:8001")
//                .baseUrl("https://api.github.com")
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(gsonConverterFactory)
                .build()
                .create(RetrofitApiService.class);
    }

}
