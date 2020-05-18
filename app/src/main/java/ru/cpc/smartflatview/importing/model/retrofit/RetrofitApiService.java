package ru.cpc.smartflatview.importing.model.retrofit;

import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Path;
import ru.cpc.smartflatview.importing.model.retrofit.data.GsonDate;

public interface RetrofitApiService {
    @GET("/api")
    Single<GsonDate> getFileList();

    @GET("/api/files/{fileName}")
    Single<ResponseBody> getFile2(@Path("fileName") String fileName);
}
