package ru.cpc.smartflatview.Import.Model.Retrofit;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.cpc.smartflatview.Import.Model.Retrofit.data.ConfigFile;
import ru.cpc.smartflatview.Import.Model.Retrofit.data.GsonDate;

public interface RetrofitApiService {
    @GET("/api")
    Single<GsonDate> getFileList();

    @GET("/api")
    Single<ConfigFile> getFile(@Query("name") String name);

}
