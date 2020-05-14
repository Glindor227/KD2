package ru.cpc.smartflatview.importing.model.netIO.common;

public class MyError extends AbstractMessage {
    private String message;

    public String getMessage() {
        return message;
    }

    public MyError(String message) {
        this.message = message;
    }
}
