package ru.cpc.smartflatview.importing.model.retrofit.data;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GsonDate {
    @Expose
    @SerializedName("count")
    private Integer count;

    @Expose
    @SerializedName("items")
    private List<String> items = null;

    public List<String> getList(){
        return items;
    }

    @NonNull
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Получили кол-во = ")
                .append(count)
                .append("\n");
        items.forEach(s -> builder.append(s).append("\n"));
        return builder.toString();
    }


}
