package ru.cpc.smartflatview.Import.Model.Retrofit.data;

import android.os.Build;
import android.support.annotation.RequiresApi;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class GsonDate {
    @Expose
    @SerializedName("id")
    public Integer id;

    @Expose
    @SerializedName("items")
    private List<String> items = null;

    public List<String> getList(){
        return items;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder("Получили id =")
                .append(id.toString())
                .append(", кол-во = ")
                .append(items.size())
                .append("\n");
        items.forEach(s -> builder.append(s).append("\n"));
        return builder.toString();
    }


}
