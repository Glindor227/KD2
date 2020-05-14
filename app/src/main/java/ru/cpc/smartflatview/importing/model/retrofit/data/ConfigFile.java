package ru.cpc.smartflatview.importing.model.retrofit.data;

import android.support.annotation.NonNull;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ConfigFile {
    @Expose
    @SerializedName("name")
    public String name;

    @Expose
    @SerializedName("size")
    public Integer size;

    @Expose
    @SerializedName("file")
    public List<Byte> file;

    @NonNull
    @Override
    public String toString() {
        return name+" "+size;
    }

    public byte[] bytes(){
        byte[] byteArray = new byte[file.size()];
        for (int index = 0; index < file.size(); index++) {
            byteArray[index] = file.get(index);
        }
        return byteArray;

    }
}
