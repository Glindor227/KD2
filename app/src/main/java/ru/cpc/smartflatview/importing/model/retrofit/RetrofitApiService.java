package ru.cpc.smartflatview.importing.model.retrofit;

import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import ru.cpc.smartflatview.importing.model.retrofit.data.ConfigFile;
import ru.cpc.smartflatview.importing.model.retrofit.data.GsonDate;

public interface RetrofitApiService {
    @GET("/api")
    Single<GsonDate> getFileList();

    @GET("/api")
    Single<ConfigFile> getFile(@Query("name") String name);

    @GET("/api/files/{fileName}")
    Single<ResponseBody> getFile2(@Path("fileName") String fileName);


}
