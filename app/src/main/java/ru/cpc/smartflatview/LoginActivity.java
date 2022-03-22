package ru.cpc.smartflatview;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import ru.cpc.smartflatview.ui.SplashActivity;

public class LoginActivity extends AppCompatActivity {

    public static final String TAG = "LoginActivityDebug";
    EditText editText1;
    EditText editText2;
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        String strId = Prefs.getId(this);
        Log.d(LoginActivity.TAG, "old id = "+strId);
        if(!strId.isEmpty()) {
            startSplashActivity(strId);
        }
        initView();
    }

    private void startSplashActivity(String strId) {
        startActivity(new Intent(this, SplashActivity.class).putExtra("id", strId));
    }

    private void initView() {
        editText1 = findViewById(R.id.id_set);
        editText2 = findViewById(R.id.id_repid);
        button= findViewById(R.id.btn_set_id);
        button.setOnClickListener(v -> {
            String id1 =editText1.getText().toString();
            String id2 =editText2.getText().toString();
            if(id1.isEmpty()||id2.isEmpty()||!id1.equals(id2)){
                Snackbar snackbar = Snackbar.make(v, "Идентификатор пользователя не корректен", Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("Demo", v2 -> startSplashActivity("")).show();
                Log.d(LoginActivity.TAG, "id fail");

            }else{
                Log.d(LoginActivity.TAG, "id ok");
                Prefs.setId(id1,this);
                startSplashActivity(id1);
            }

        });
    }
}