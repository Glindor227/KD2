package ru.cpc.smartflatview.importing.model.retrofit;

import java.util.Map;

import io.reactivex.Single;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.QueryMap;
import ru.cpc.smartflatview.importing.model.retrofit.data.GsonDate;

public interface RetrofitApiService {
/*    @GET("/api/idUser/{idUser}")
    Single<GsonDate> getFileList(@Path("idUser") String idUser);
*/
    @GET("/api/list")
    Single<GsonDate> getFileList(@QueryMap Map<String, String> parameter);

    @GET("/api/file")
    Single<ResponseBody> getFile2(@QueryMap Map<String, String> parameter);
}
