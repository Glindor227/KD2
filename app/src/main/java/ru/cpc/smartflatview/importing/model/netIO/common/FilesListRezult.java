package ru.cpc.smartflatview.importing.model.netIO.common;

import java.util.List;

public class FilesListRezult extends AbstractMessage {
    private List<String> fileList;

    public FilesListRezult(List<String> fileList) {
        this.fileList = fileList;
    }

    public List<String> getFileList() {
        return fileList;
    }
}
