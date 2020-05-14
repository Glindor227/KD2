package ru.cpc.smartflatview.importing.model.netIO.common;

public class FileRequest extends AbstractMessage {
    private String filename;

    public FileRequest(String filename) {
        this.filename = filename;
    }
}
