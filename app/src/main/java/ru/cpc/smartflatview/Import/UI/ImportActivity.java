package ru.cpc.smartflatview.Import.UI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.List;
import java.util.Objects;

import ru.cpc.smartflatview.Config;
import ru.cpc.smartflatview.FileChooser;
import ru.cpc.smartflatview.Import.Model.NetIO.FileRepository.ServerFile;
import ru.cpc.smartflatview.MainActivity;
import ru.cpc.smartflatview.R;

public class ImportActivity extends AppCompatActivity {

    private static final String IMPORT_PREFERENCES = "srsimport";
    private static final String IMPORT_PREFERENCES_IP = "ip";
    private static final String IMPORT_PREFERENCES_PORT = "port";
    public static final String TAG = "Import";
    InputStream input_IS = null;
    //File input_f;
    ServerFile serverFile;
    EditText et_ip;
    EditText et_port;
    private SharedPreferences importPref;
    Boolean localImport;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import);
        Toolbar toolbar = findViewById(R.id.toolbar_import);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        InitUserView();
        InitSharedPref();
        et_ip = findViewById(R.id.et_ip);
        et_port = findViewById(R.id.et_port);

        try{
            Integer port = Integer.parseInt(et_port.getText().toString());
            serverFile = new ServerFile(this,et_ip.getText().toString(),port);
        }
        catch (Exception e){
            inputError("Ошибка в конструкторе активити");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = importPref.edit();
        editor.putString(IMPORT_PREFERENCES_IP, et_ip.getText().toString());
        editor.putString(IMPORT_PREFERENCES_PORT, et_port.getText().toString());
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (importPref.contains(IMPORT_PREFERENCES_IP)) {
            et_ip.setText(importPref.getString(IMPORT_PREFERENCES_IP, "192.168.1.68"));
        }
        if (importPref.contains(IMPORT_PREFERENCES_PORT)) {
            et_port.setText(importPref.getString(IMPORT_PREFERENCES_PORT, "8189"));
        }
    }

    private void InitSharedPref() {
        importPref = getSharedPreferences(IMPORT_PREFERENCES, Context.MODE_PRIVATE);

    }

    public void inputError(String text){
        Toast.makeText(this,text,Toast.LENGTH_LONG).show();
        Log.d(ImportActivity.TAG, "text");
    }

    public void setInput_IS(InputStream input_IS) {
        this.input_IS = input_IS;
        ImportConfig(false);

//        btn_import.setEnabled(true);

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void InitUserView() {
        et_ip = findViewById(R.id.et_ip);
        et_port = findViewById(R.id.et_port);

        Button btn_local = findViewById(R.id.btn_local);
        Button btn_link = findViewById(R.id.btn_link);

        btn_local.setOnClickListener(v -> new FileChooser(
                ImportActivity.this).setFileListener(file -> {
            try{
                input_IS = new FileInputStream(file);
                ImportConfig(true);
                localImport = true;
                Log.d("localImport", "true");
            }
            catch(FileNotFoundException e){
                Log.e("FileNotFoundException", "can't create FileInputStream");
            }


        }).showDialog());
        btn_link.setOnClickListener(v -> {
            try {

                serverFile.setNewLinkParams(et_ip.getText().toString(),Integer.parseInt(et_port.getText().toString()));
            }
            catch (Exception e){
                inputError("Ошибка в установке параметров соединения");
            }
            Log.d(ImportActivity.TAG, "ImportActivity getList");
//            serverFile.startSever();
            serverFile.initList();
            localImport = false;
            Log.d("localImport", "false");


        });


    }

    public void InitRV(List<String> inFilesList) {
        RecyclerView recyclerView = findViewById(R.id.if_rv_notes);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL, false);
        RVAdapter adapter = new RVAdapter(serverFile,inFilesList);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }

    private void ImportConfig(Boolean local) {
        Config pConfig = new Config(input_IS);
        if(pConfig.m_cRooms.size() != 0)
        {
//            SafeExit();
            Config.Instance = pConfig;
            Config.SaveXml(pConfig, this);
            Toast.makeText(getApplicationContext(), R.string.importSuccess, Toast.LENGTH_LONG).show();
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if(!local) serverFile.stop();

            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);

//                    Intent mStartActivity = new Intent(getApplicationContext(), LaunchScreenActivity.class);
//                    int mPendingIntentId = 123456;
//                    PendingIntent mPendingIntent = PendingIntent.getActivity(getApplicationContext(), mPendingIntentId,    mStartActivity, PendingIntent.FLAG_CANCEL_CURRENT);
//                    AlarmManager mgr = (AlarmManager)getApplicationContext().getSystemService(Context.ALARM_SERVICE);
//                    mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);

            finish();
            //System.exit(0);
        }
    }
}
