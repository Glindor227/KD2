package ru.cpc.smartflatview.importing.model.retrofit;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.cpc.smartflatview.importing.model.retrofit.data.GsonDate;

public class RetrofitApi {
    private RetrofitApiService apiServiceJson;
    private RetrofitApiService apiService;

    public RetrofitApi(String ip,String port) {
        String url_base = "http://" + ip + ":" + port;// = "http://86.62.78.53:8189";
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        GsonConverterFactory gsonConverterFactory = GsonConverterFactory.create(gson);
        apiServiceJson = new Retrofit.Builder()
                .baseUrl(url_base)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(gsonConverterFactory)
                .build()
                .create(RetrofitApiService.class);
        apiService = new Retrofit.Builder()
                .baseUrl(url_base)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
                .create(RetrofitApiService.class);
    }

    public Single<GsonDate> requestFilesList(){
        return apiServiceJson.getFileList().subscribeOn(Schedulers.io());
    }
    public Single<ResponseBody> requestFileByName2(String name){
        return apiService.getFile2(name).subscribeOn(Schedulers.io());
    }


}
