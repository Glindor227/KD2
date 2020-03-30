package ru.cpc.smartflatview.Import.Common;

import java.util.ArrayList;
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
