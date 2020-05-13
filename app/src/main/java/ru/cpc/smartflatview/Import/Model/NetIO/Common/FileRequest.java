package ru.cpc.smartflatview.Import.Model.NetIO.Common;

public class FileRequest extends AbstractMessage {
    private String filename;

    public FileRequest(String filename) {
        this.filename = filename;
    }
}
