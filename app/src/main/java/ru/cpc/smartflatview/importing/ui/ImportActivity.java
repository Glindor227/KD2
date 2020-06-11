package ru.cpc.smartflatview.importing.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import moxy.MvpAppCompatActivity;
import moxy.presenter.InjectPresenter;
import ru.cpc.smartflatview.Config;
import ru.cpc.smartflatview.MainActivity;
import ru.cpc.smartflatview.R;
import ru.cpc.smartflatview.app.App;
import ru.cpc.smartflatview.importing.model.localfile.FileChooser;
import ru.cpc.smartflatview.importing.presenter.ImportPresenter;

public class ImportActivity extends MvpAppCompatActivity implements ImportView {
    private static final String IMPORT_PREFERENCES = "srsimport";
    private static final String IMPORT_PREFERENCES_IP = "ip";
    private static final String IMPORT_PREFERENCES_PORT = "port";
    public static final String TAG = "Import";
    EditText et_ip;
    EditText et_port;
    Toolbar toolbar;
    ProgressBar progressBar;
    private SharedPreferences importPref;

    @InjectPresenter
    ImportPresenter presenter;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import);
        toolbar = findViewById(R.id.toolbar_import);
        setSupportActionBar(toolbar);
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        InitUserView();
        InitSharedPref();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * записываем параметры в SharedPreferences
     */
    @Override
    protected void onPause() {
        super.onPause();
        SharedPreferences.Editor editor = importPref.edit();
        editor.putString(IMPORT_PREFERENCES_IP, et_ip.getText().toString());
        editor.putString(IMPORT_PREFERENCES_PORT, et_port.getText().toString());
        editor.apply();
    }

    /**
     * считываем параметры из SharedPreferences
     */
    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onResume() {
        super.onResume();
        if (importPref.contains(IMPORT_PREFERENCES_IP)) {
            et_ip.setText(importPref.getString(IMPORT_PREFERENCES_IP, "192.168.1.68"));
        }
        if (importPref.contains(IMPORT_PREFERENCES_PORT)) {
            et_port.setText(importPref.getString(IMPORT_PREFERENCES_PORT, "8189"));
        }
        presenterStart();
    }

    private void InitSharedPref() {
        importPref = getSharedPreferences(IMPORT_PREFERENCES, Context.MODE_PRIVATE);
    }

    public void inputError(String text){
        Snackbar snackbar = Snackbar.make(toolbar, text, Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction("ОК", v -> snackbar.dismiss()).show();
        Log.d(ImportActivity.TAG, text);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void InitUserView() {
        et_ip = findViewById(R.id.et_ip);
        et_port = findViewById(R.id.et_port);
        progressBar= findViewById(R.id.prog_bar_test);
        Button btn_local = findViewById(R.id.btn_local);
        Button btn_link = findViewById(R.id.btn_link);

        btn_local.setOnClickListener(v -> new FileChooser(
                ImportActivity.this).setFileListener(file -> {
            try{
                ImportConfig(new FileInputStream(file));
            }
            catch(FileNotFoundException e){
                Log.e("FileNotFoundException", "can't create FileInputStream");
            }
        }).showDialog());
        btn_link.setOnClickListener(v -> {
            try {
                presenterStart();
            }
            catch (Exception e){
                inputError(e.getMessage());
            }
            Log.d(ImportActivity.TAG, "ImportActivity getList");
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void presenterStart() {
        presenter.initParam(et_ip.getText().toString(),et_port.getText().toString());
        presenter.getList();
    }

    public void InitRV(List<String> inFilesList) {
        RecyclerView recyclerView = findViewById(R.id.if_rv_notes);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getApplicationContext(),
                LinearLayoutManager.VERTICAL, false);
        RVAdapter adapter = new RVAdapter(inFilesList, item -> presenter.getOneFile(item));
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

    //TODO По хорошему этот метод должен быть частично во view(передача управления во main, а частично на уровне модели(старт конфига). Просто пока Config это легаси код)
    private void ImportConfig(InputStream input_IS) {
        progressBar.setVisibility(ProgressBar.VISIBLE);
        Config pConfig = new Config(input_IS);
        if(pConfig.m_cRooms.size() != 0)
        {
            Config.Instance = pConfig;
            Config.SaveXml(pConfig, this);
            Toast.makeText(getApplicationContext(), R.string.importSuccess, Toast.LENGTH_LONG).show();
            Intent intent = new Intent(this, MainActivity.class);
            App.addTime("3 go to MainActivity", new Date());
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void callbackListFiles(List<String> list) {
        InitRV(list);
    }

    @Override
    public void callbackOneFile(InputStream file) {
        App.addTime("2 Получили из сети ", new Date());
        ImportConfig(file);
    }

    @Override
    public void callbackError(String error) {
        inputError(error);
        InitRV(new ArrayList<>());
    }
}
