package ru.cpc.smartflatview.Import.Model.NetIO.Common;

public class MyError extends AbstractMessage {
    private String message;

    public String getMessage() {
        return message;
    }

    public MyError(String message) {
        this.message = message;
    }
}
