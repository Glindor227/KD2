package ru.cpc.smartflatview.importing.model.netIO.fileRepository;


import android.os.Build;
import android.support.annotation.RequiresApi;
import android.util.Log;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import ru.cpc.smartflatview.importing.model.netIO.common.AbstractMessage;
import ru.cpc.smartflatview.importing.model.netIO.common.FileMessage;
import ru.cpc.smartflatview.importing.model.netIO.common.FileRequest;
import ru.cpc.smartflatview.importing.model.netIO.common.FilesListRequest;
import ru.cpc.smartflatview.importing.model.netIO.common.FilesListRezult;
import ru.cpc.smartflatview.importing.model.netIO.common.MyError;
import ru.cpc.smartflatview.importing.ui.ImportActivity;

public class ServerFile {
    private ImportActivity importActivity;
    private String ip;
    private Integer port;

    public void setNewLinkParams(String ip,Integer port){
        this.ip = ip;
        this.port = port;
    }

    public ServerFile(ImportActivity importActivity,String ip,Integer port) {
        this.importActivity = importActivity;
        setNewLinkParams(ip,port);
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    public void initList(){
//        testList();
        Log.d(ImportActivity.TAG, "Стартовали    getList()");

        Thread t = new Thread(() -> {
            try {
                Log.d(ImportActivity.TAG, "Стартовали поток");
                if(!Network.start(ip,port)){
                    Log.d(ImportActivity.TAG, "НЕстартовали Network");
                    return;
                }
                Log.d(ImportActivity.TAG, "Стартовали Network.start");
                Network.sendMsg(new FilesListRequest());
                Log.d(ImportActivity.TAG, "Послали FilesListRequest");

                while (true) {
                    AbstractMessage am = Network.readObject();
                    Log.d(ImportActivity.TAG, "Пришло чтото");

                    if (am instanceof FileMessage) {
                        Log.d(ImportActivity.TAG, "Пришло FileMessage");
                        FileMessage fm = (FileMessage) am;
                        if (fm.getFirstPart() && fm.getEndPart()){
                            InputStream targetStream = new ByteArrayInputStream(fm.getData());
                            importActivity.runOnUiThread(() -> {
                                importActivity.setInput_IS(targetStream);
                                Log.d(ImportActivity.TAG, "Считали нужный InputStream");
                            });
                        }
                    }
                    if(am instanceof FilesListRezult) {
                        Log.d(ImportActivity.TAG, "Пришло FilesListRezult");

                        FilesListRezult filesListRezult = (FilesListRezult) am;
                        Log.d(ImportActivity.TAG, "Получили список файлов(количество="+filesListRezult.getFileList().size()+")");
                        importActivity.runOnUiThread(() -> importActivity.InitRV(filesListRezult.getFileList()));
                    }

                    if(am instanceof MyError) {
                        importActivity.runOnUiThread(() -> importActivity.inputError("из сети пришла ошибка"));
                        Log.d(ImportActivity.TAG, "Пришло Error");
                    }


                }
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            } finally {
                Network.stop();
            }
        });
        t.setDaemon(true);
        t.start();

    }
    public void initFile(String fileName){
        Network.sendMsg(new FileRequest(fileName));
    }
    public void stop(){
        Network.stop();
    }



}
