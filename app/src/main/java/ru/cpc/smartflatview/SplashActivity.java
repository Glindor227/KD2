package ru.cpc.smartflatview;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.Locale;
import java.util.Objects;

public class SplashActivity extends AppCompatActivity
{
    private static final int SPLASH_TIME = 500;

    private Uri importData = Uri.EMPTY;
    private String idUser = "";

    private class BackgroundTask extends AsyncTask<Uri, Integer, Config> {
        Intent intent;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            Log.d("LaunchScreen", "onPreExecute()");
            Log.d("Glindor3","BackgroundTask onPreExecute");

            intent = new Intent(SplashActivity.this, MainActivity.class);
        }
        @Override
        protected Config doInBackground(Uri... params) {
                /*  Use this method to load background
                * data that your app needs. */
            Log.d("Glindor3","BackgroundTask doInBackground 1");

            Config pConfig;
            Log.i("LaunchScreen", "Starting task with url: "+params[0]);
            //if(importData != Uri.EMPTY)
            //    throw new InvalidParameterException();
            Log.d("Glindor3","BackgroundTask doInBackground 2");

            Log.d("Glindor3!3","BackgroundTask orientation = "+getResources().getConfiguration().orientation);

            if(Config.DEMO || idUser.equals("")){
                pConfig = new Config(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);
            }
            else
            {
//                if(importData != Uri.EMPTY)
//                {
//                    throw new InvalidParameterException();
//                        LaunchScreenActivity.this.runOnUiThread(new Runnable()
//                        {
//                            public void run()
//                            {
//                                showDialog(MainActivity.DIALOG_REALLY_UPDATE_CONFIG_ID);
//                            }
//                        });
//                }
//                else
//                {
                    pConfig = Config.LoadXml( getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT,importData, SplashActivity.this);
                    Config.portOrientation = getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT;
                    if(pConfig == null || pConfig.m_cRooms.size() == 0)
                    {
                        pConfig = new Config(getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT);
//                      LaunchScreenActivity.this.runOnUiThread(new Runnable()
//                      {
//                          public void run()
//                          {
//                             showDialog(MainActivity.DIALOG_NO_CONFIG_ID);
//                          }
//                      });
                    }
//                  else
                    Config.SaveXml(pConfig, SplashActivity.this);
//                }
            }
            Log.d("Glindor3","BackgroundTask doInBackground 3");

            Log.d("LaunchScreen", "doInBackground()");
            try {
                Thread.sleep(SPLASH_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            Log.d("Glindor3","BackgroundTask doInBackground 4");

            return pConfig;
        }
        @Override
        protected void onPostExecute(Config config) {
            Log.d("Glindor3","BackgroundTask onPostExecute 1");

            super.onPostExecute(config);
            Log.d("LaunchScreen", "onPostExecute()");

            Config.Instance = config;
            TextView pTextName = findViewById(R.id.summary_name);
            TextView pTextDesc = findViewById(R.id.summary_description);
            Log.d("Glindor3","BackgroundTask onPostExecute 2");

            if (pTextName != null)
            {
                pTextName.setText(config.m_sSummaryName);
            }
            if (pTextDesc != null)
            {
                pTextDesc.setText(config.m_sSummaryText);
            }
            Log.d("Glindor3","BackgroundTask onPostExecute 3");

            try
            {
                Thread.sleep(200);
            }
            catch (InterruptedException e)
            {
                Log.v("Glindor",e.getMessage());
                e.printStackTrace();
            }
            Log.d("Glindor3","BackgroundTask onPostExecute 4");

            //            Pass your loaded data here using Intent
            //            intent.putExtra("data_key", "");
            String loginCode = Prefs.getLogin(SplashActivity.this);
            if(loginCode.isEmpty())
                startActivity(intent);
            else {
                AuthorizationActivity.s_sTrueCode = loginCode;
                AuthorizationActivity.s_pUnlocker = new AuthorizationActivity.SFUnlocker() {
                    @Override
                    public void Unlock(boolean bUnlock) {
                        if (bUnlock)
                            startActivity(intent);
                    }
                };
                Intent intentLogin = new Intent(SplashActivity.this, AuthorizationActivity.class);
                startActivity(intentLogin);
            }
            Log.d("Glindor3","BackgroundTask onPostExecute 5");

            finish();
        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        idUser = Objects.requireNonNull(Objects.requireNonNull(getIntent().getExtras()).getCharSequence("id")).toString();
        Log.d(LoginActivity.TAG,"LaunchScreenActivity onCreate start id("+ idUser+")");


        //        Transparent Status Bar
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.d("Glindor55", "LaunchScreenActivity onCreate ORIENTATION_LANDSCAPE");
        }
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.d("Glindor55", "LaunchScreenActivity onCreate ORIENTATION_PORTRAIT");
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
            getWindow().setStatusBarColor(Color.TRANSPARENT);
        }

        setContentView(R.layout.activity_splash);

        TextView pTextName = findViewById(R.id.summary_name);
        TextView pTextDesc = findViewById(R.id.summary_description);
        Log.d("Glindor3","LaunchScreenActivity onCreate 1");

        if (pTextName != null)
        {
            pTextName.setText(R.string.app_name);
        }
        if (pTextDesc != null)
        {
            Context context = getApplicationContext(); // or activity.getApplicationContext()
            PackageManager packageManager = context.getPackageManager();
            String packageName = context.getPackageName();

            String myVersionName = "not available"; // initialize String

            try
            {
                myVersionName = packageManager.getPackageInfo(packageName, 0).versionName;
            }
            catch (PackageManager.NameNotFoundException e)
            {
                e.printStackTrace();
            }
            pTextDesc.setText(String.format(Locale.US, "Version %s", myVersionName));
        }
        Log.d("Glindor3","LaunchScreenActivity onCreate 2");

        if(!Config.DEMO)
        {
            Intent i = getIntent();
            if(i != null)
            {
                Uri u = i.getData();
                if(u != null)
                {
                    importData = u;
//                    name.setText("Импорт конфигурации");
//                    text.setText(u.getEncodedPath());
                }
            }
        }
        Log.d("Glindor3","LaunchScreenActivity onCreate 3");


        //TODO надо чтобы эти параметры вступалив силу без перезагрузки
        Indicator.typeDez = Prefs.getDis(this);
//        Indicator.delta = Prefs.getDelta(this);

//        Indicator.newDez = Prefs.getNewDis(this);
//        Indicator.posDez = Prefs.getPostDis(this);
//        Indicator.pos2Dez = Prefs.getPost2Dis(this);
//        Indicator.pos3Dez = Prefs.getPost3Dis(this);
//        Indicator.pos4Dez = Prefs.getPost4Dis(this);
//        Log.d("Glindor56", "INIT newDez = "+Indicator.newDez);
//        Log.d("Glindor56", "INIT posDez = "+Indicator.posDez);


        new BackgroundTask().execute(importData);
        Log.d("Glindor3","LaunchScreenActivity onCreate end");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("Glindor3","LaunchScreenActivity onCreate start");
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.d("Glindor55", "LaunchScreenActivity onStart ORIENTATION_LANDSCAPE");
        }
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.d("Glindor55", "LaunchScreenActivity onStart ORIENTATION_PORTRAIT");
        }

    }
}
