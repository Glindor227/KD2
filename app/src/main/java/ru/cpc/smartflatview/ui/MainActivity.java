package ru.cpc.smartflatview.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.StrictMode;
import android.os.SystemClock;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextWatcher;
import android.text.style.ImageSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.EditText;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import io.reactivex.annotations.NonNull;
import ru.cpc.smartflatview.AlarmReceiver;
import ru.cpc.smartflatview.Config;
import ru.cpc.smartflatview.Indicator;
import ru.cpc.smartflatview.LogActivity;
import ru.cpc.smartflatview.Prefs;
import ru.cpc.smartflatview.R;
import ru.cpc.smartflatview.RoboErrorReporter;
import ru.cpc.smartflatview.Room;
import ru.cpc.smartflatview.SFServer;
import ru.cpc.smartflatview.Subsystem;
import ru.cpc.smartflatview.SubsystemUI;
import ru.cpc.smartflatview.app.App;
import ru.cpc.smartflatview.importing.ui.ImportActivity;
import ru.cpc.smartflatview.uiView.ExpandedMenuModel;
import ru.cpc.smartflatview.uiView.HouseExpandableListAdapter;
import ru.cpc.smartflatview.utils.LogUtils;
import ru.cpc.smartflatview.voice.IVoiceResult;
import ru.cpc.smartflatview.voice.VoiceToText;
import ru.cpc.smartflatview.voice.VoiceUtils;

//import com.idis.android.redx.RSize;
//import com.idis.android.redx.core.RCore;

public class MainActivity extends AppCompatActivity implements IVoiceResult {

    private static Context mAppContext = null;

//    public  static final boolean USE_ORIGINAL_RESOLUTION = true;

    public static Context getAppContext() {return mAppContext;}

    public static boolean canUseGL() {return false;}

    private static final int REQUEST_READWRITE_STORAGE = 100;

    private static final String PREF_INDICATORS = "indicators" ;
/*
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        //No call for super(). Bug on API Level > 11.
    }
*/
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    private SectionsPagerAdapter mSectionsPagerAdapter;

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    private ViewPager mViewPager;
    private TabLayout tabLayout;


    private HashMap<Integer, List<ExpandedMenuModel>> m_cMenu = new HashMap<>();
    List<ExpandedMenuModel> m_cListData = new ArrayList<>();

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode == REQUEST_READWRITE_STORAGE) {
            if ((grantResults.length <= 0) || (grantResults[0] != PackageManager.PERMISSION_GRANTED))
            {
                AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
                dlgAlert.setMessage(R.string.error_FileAccess);
                dlgAlert.setTitle(R.string.app_name);
                dlgAlert.setPositiveButton(R.string.ok,
                        (dialog, which) -> {
//                                SafeExit();
//                                finish();
//                                System.exit(0);
                        });
                dlgAlert.setCancelable(false);
                dlgAlert.create().show();
            }
        }
    }

    private void StartCheckAlarmShedule()
    {
        Intent alarm = new Intent(this, AlarmReceiver.class);
        String sIP = Prefs.getExternalIP(this);
        alarm.putExtra("ip1", Config.Instance.m_sIP);
        alarm.putExtra("ip2", sIP);
        alarm.putExtra("port", Config.Instance.m_iPort);
        alarm.putExtra("mobile", Prefs.getMobile(this));
        alarm.putExtra("sound", Prefs.getSound(this));
        alarm.putExtra("wakeup", Prefs.getWakeup(this));
        alarm.putExtra("connection", Prefs.getConnectionNotify(this));

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarm,
                                                                 PendingIntent.FLAG_UPDATE_CURRENT);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null)
        {
            alarmManager.cancel(pendingIntent);
            Log.d("SFV-Service", "AlarmManager.cancel() - OK");

            if(Prefs.getBackround(this))
            {
                //alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                //                          SystemClock.elapsedRealtime(), 60000, pendingIntent);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                {
                    alarmManager.setAndAllowWhileIdle(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 60000, pendingIntent);
                    Log.d("SFV-Service", "AlarmManager.setAndAllowWhileIdle() - OK");
                }
                else
                {
                    alarmManager.set(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 60000, pendingIntent);
                    Log.d("SFV-Service", "AlarmManager.set() - OK");
                }
            }
            else
            {
                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                if (mNotificationManager != null)
                {
                    mNotificationManager.cancelAll();
                    Log.d("SFV-Service", "NotificationManager.cancelAll() - OK");
                }
            }
        }
    }

    private void StopCheckAlarmShedule()
    {
        Intent alarm = new Intent(this, AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 0, alarm, PendingIntent.FLAG_NO_CREATE);
        if(pendingIntent != null)
        {
            pendingIntent = PendingIntent.getBroadcast(this, 0, alarm, 0);
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            alarmManager.cancel(pendingIntent);
            Log.d("SFV-Service", "AlarmManager.cancel() - OK");
        }
    }

    @Override
    protected void onStart() {
        App.addTime("4 MainActivity onStart", new Date());
        Log.d("ImportTime",App.getTimeLabel().toString());


//        Indicator.typeDez = Prefs.getDis(this);
        Indicator.delta = Prefs.getDelta(this);

        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.d("Glindor3!3", "MainActivity onStart ORIENTATION_LANDSCAPE");
        }
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.d("Glindor3!3", "MainActivity onStart ORIENTATION_PORTRAIT");
        }
        super.onStart();
    }

    @SuppressLint("SourceLockedOrientationActivity")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        App.addTime("5 onCreate", new Date());
        initOrientation();
        initPolice();
        Log.d("Glindor3","MainActivity onCreate 1");
        Log.d("Glindor3!3","MainActivity orientation = "+getResources().getConfiguration().orientation);
        initPermission();

        super.onCreate(savedInstanceState);
//        RCore.getInstance().setResolution(new RSize());
        Log.d("Glindor3","MainActivity onCreate 2");

        mAppContext = getApplicationContext();
        setContentView(R.layout.activity_main);
        initUserView();

        if(Config.Instance != null)
            prepareListData();

        initExpandableList();

        String indicators = "";
        try
        {
            indicators = getPreferences(MODE_PRIVATE).getString(PREF_INDICATORS, "");
        }
        catch(Exception ex)
        {
            Log.e("SF", "getPreferences exception : " + ex.getMessage() );
        }
        Log.d("SF", "onCreate, loading = '" + indicators + "'" );
        Log.d("Glindor3","MainActivity onCreate 8");
        Load(indicators);
        Log.d("Glindor3","MainActivity onCreate 8.1");
        App.addTime("11 onCreate", new Date());

        if(Config.Instance != null && Config.Instance.m_cSubsystems != null && Config.Instance.m_cSubsystems.size() > 0)
        {
            Log.d("Glindor3","MainActivity onCreate 8.1");
            SFServer.Instance = new SFServer(this);
            Log.d("Glindor3","MainActivity onCreate 8.2");
            App.addTime("12 onCreate", new Date());

            if(!Config.DEMO)
                SFServer.Instance.Connect();
            Log.d("Glindor3","MainActivity onCreate 8.3");
        }
        Log.d("Glindor3","MainActivity onCreate 9");
        App.addTime("13 onCreate exit", new Date());

    }


    private void initExpandableList() {
        ExpandableListView expandableList= findViewById(R.id.navigationmenu);
        if (expandableList != null)
        {
            ExpandableListView.OnChildClickListener listenerChild = (parent, v, groupPosition, childPosition, id) -> {
                Log.d("Menu", "MainActivity::OnChildClick("+groupPosition+","+childPosition+") ");
                menuItemOperation(m_cListData.get(groupPosition).m_cNestedMenu.get(childPosition).getRoom());
                return true;
            };
            ExpandableListView.OnGroupClickListener listenerGroup = (parent, v, groupPosition, id) -> {
                Log.d("Menu", "MainActivity::OnGroupClick("+groupPosition+") ");
                if(m_cListData.get(groupPosition).m_cNestedMenu.size() == 0) {// группа без помещений
                    Log.d("Menu", "MainActivity::OnGroupClick(EMPTY GROUP) ");
                    menuItemOperation(m_cListData.get(groupPosition).getRoom());
                    return true;
                }
                return false;
            };

            HouseExpandableListAdapter mMenuAdapter = new HouseExpandableListAdapter(this,
                    m_cListData,
                    listenerGroup,
                    listenerChild);
            expandableList.setAdapter(mMenuAdapter);
        }
    }

    private void menuItemOperation(int iMenuItem) {
        if(iMenuItem != -1) {
            showRoom(iMenuItem);
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            if (drawer != null) {
                drawer.closeDrawers();
            }
        }
    }

    private void initUserView() {
        Toolbar toolbar = initToolbar();
        Log.d("Glindor3","MainActivity onCreate 4");

        Log.d("SF", "MainActivity.onCreate()");

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = findViewById(R.id.container);
        if (mViewPager != null)
        {
            mViewPager.setAdapter(mSectionsPagerAdapter);
        }
        Log.d("Glindor3","MainActivity onCreate 5");

        tabLayout = findViewById(R.id.tabs);
        if (tabLayout != null)
        {
            tabLayout.setupWithViewPager(mViewPager);
            tabLayout.setVisibility(View.GONE);
        }

        //Настраиваем боковое меню
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            DrawerLayout drawer = findViewById(R.id.drawer_layout);
            ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                    this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
            if (drawer != null) {
                drawer.addDrawerListener(toggle);
            }
            toggle.syncState();
        }
        Log.d("Glindor3","MainActivity onCreate 6");

        NavigationView navigationView = findViewById(R.id.nav_view);
        initMenuFilter(navigationView.getHeaderView(0));


        if (navigationView != null)
        {
            navigationView.setItemIconTintList(null);
        }
        Log.d("Glindor3","MainActivity onCreate 7");
    }

    private void initMenuFilter(View header) {
        ImageView filterView = header.findViewById(R.id.filter_key);
         filterView.setOnClickListener(view -> {
            Log.d("GlindorFilter","Filter click");
            LinearLayout textFilterLayout = header.findViewById(R.id.filter_layout);
            if(textFilterLayout.getVisibility() == View.VISIBLE){
                Log.d("GlindorFilter","View.VISIBLE -> View.GONE");
                textFilterLayout.setVisibility(View.GONE);
            }else{
                Log.d("GlindorFilter","View.GONE -> View.VISIBLE");
                textFilterLayout.setVisibility(View.VISIBLE);
            }
        });
        EditText edit1 = header.findViewById(R.id.point_name);
        edit1.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {}
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if(s != Config.Instance.roomFilter) {
                    Log.d("GlindorFilter","Обновляем фильтр " + s);
                    Config.Instance.roomFilter = s.toString();
                    m_cMenu.clear();
                    m_cListData.clear();
                    prepareListData();
                    initExpandableList();
                }
            }
        });
    }

    @Nullable
    private Toolbar initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        Log.d("Glindor3","MainActivity onCreate 3");

        MenuItem item = null;
        if (toolbar != null)
        {
            item = toolbar.getMenu().findItem(R.id.action_alarm);
        }
        if(item != null)
            item.setIcon(R.drawable.ic_ok);
        return toolbar;
    }

    private void initPermission() {
        if (Build.VERSION.SDK_INT >= 23)
        {
            int permissionCheck1 = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);
            int permissionCheck2 = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
            if (permissionCheck1 != PackageManager.PERMISSION_GRANTED || permissionCheck2 != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_READWRITE_STORAGE);
            }
        }
    }

    private void initPolice() {
        RoboErrorReporter.bindReporter(this);
        StrictMode.ThreadPolicy policy =
                new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    private void initOrientation() {
        Log.d("Glindor3!3", "setRequestedOrientation");
        if(Config.portOrientation){
            Log.d("Glindor3!3", "setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT)");
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        else{
            Log.d("Glindor3!3", "setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)");
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        }

        Log.d("Glindor3!3", "onCreate");
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
            Log.d("Glindor3!3", "MainActivity onCreate ORIENTATION_LANDSCAPE");
        }
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            Log.d("Glindor3!3", "MainActivity onCreate ORIENTATION_PORTRAIT");
        }
    }


    private void showRoom(int iRoom) {
        Objects.requireNonNull(getSupportActionBar()).setTitle(Config.Instance.m_cRooms.get(iRoom).m_sName);

        mSectionsPagerAdapter.m_iRoom = iRoom;
        Objects.requireNonNull(mViewPager.getAdapter()).notifyDataSetChanged();

        tabLayout.removeAllTabs();
        for (Subsystem pSubsystem : Config.Instance.m_cRooms.get(iRoom).m_cSubsystems) {
            TabLayout.Tab newTab = tabLayout.newTab();
            if(pSubsystem.m_sName.equalsIgnoreCase("0"))
                newTab.setIcon(R.drawable.tab_light_indicator);
            else
                newTab.setIcon(R.drawable.tab_media_indicator);
            newTab.setText(pSubsystem.m_sName);
            Log.d("","Новая закладка "+pSubsystem.m_sName);
            tabLayout.addTab(newTab);
        }
        Objects.requireNonNull(tabLayout.getTabAt(0)).select();

        if(Config.Instance.m_cRooms.get(iRoom).m_cSubsystems.size() > 1)
            tabLayout.setVisibility(View.VISIBLE);
        else
            tabLayout.setVisibility(View.GONE);

        //mSectionsPagerAdapter = (SectionsPagerAdapter)mViewPager.getAdapter();
        mViewPager.getAdapter().notifyDataSetChanged();
    }


    private void prepareRoom(Room room, List<ExpandedMenuModel> list){
        String filter = Config.Instance.roomFilter;
        if((filter.length()!=0) && (!room.m_sName.contains(filter))) {
            Log.d("GlindorFilter","-("+filter+") "+room.m_sName);
            return;
        }
        Log.d("GlindorFilter","+("+filter+") "+room.m_sName);
        ExpandedMenuModel item0 = new ExpandedMenuModel(room.m_sName, R.drawable.ic_ok, room.m_iIndex);
        list.add(item0);
        List<ExpandedMenuModel> cList = m_cMenu.get(room.m_iIndex);
        if (cList == null) {
            cList = new ArrayList<>();
            m_cMenu.put(room.m_iIndex, cList);
        }
        cList.add(item0);
    }

    // формируем m_cMenu и m_cListData
    private void prepareListData()
    {
        m_cMenu.clear();

        if(Config.Instance == null)
            return;

        if(Config.Instance.m_cFavorites != null) // избранное
        {
            Log.d("SF", "prepareListData, favorites count = " + Config.Instance.m_cFavorites.size());
            for (Room pRoom : Config.Instance.m_cFavorites) {
                Log.d("SF", "prepareListData, favorites room[" + pRoom.m_iIndex + "] = '" + pRoom.m_sName + "'");
                prepareRoom(pRoom,m_cListData);
            }
        }

        if(Config.Instance.m_cGroups != null)
        {
            Log.d("SF", "prepareListData, groups count = " + Config.Instance.m_cGroups.size());
            for (String key : Config.Instance.m_cGroups.keySet())
            {
                List<Room> keyList = Config.Instance.m_cGroups.get(key);
                if (Objects.requireNonNull(keyList).size() <= 0) // пустая группа - не отображаем
                {
                    Log.e("SF", "ВНИМАНИЕ! Пустая группа "+key);
                    continue;
                }
                if (keyList.size() == 1 ) { // группа с одним помещением - отображаем как без группы
                    Room pRoom = Objects.requireNonNull(Config.Instance.m_cGroups.get(key)).get(0);
                    Log.d("SF", "prepareListData, room[" + pRoom.m_iIndex + "] = '" + pRoom.m_sName + "'");
                    prepareRoom(pRoom,m_cListData);
                }

                if (Objects.requireNonNull(keyList).size() > 1) // группа с вложениями
                {
                    ExpandedMenuModel group = new ExpandedMenuModel(key, -1, -1);
                    m_cListData.add(group);
                    for (Room pRoom : Objects.requireNonNull(Config.Instance.m_cGroups.get(key))) {
                        if(pRoom==null){
                            Log.e("SF", "ВНИМАНИЕ! В группе "+key+" существует пустая подсистема!");
                            continue;
                        }
                        Log.d("SF", "prepareListData, room[" + pRoom.m_iIndex + "] = '" + pRoom.m_sName + "'");
                        prepareRoom(pRoom,group.m_cNestedMenu);
                    }
                }
            }
        }

        if(m_cMenu.isEmpty())
        {
            for (Room pRoom : Config.Instance.m_cRooms)
            {
                Log.d("SF", "prepareListData, room[" + pRoom.m_iIndex + "] = '" + pRoom.m_sName + "'");
                prepareRoom(pRoom,m_cListData);
            }
        }

    }
    //По кнопке "Назад" закрываем боковое меню
    @Override
    public void onBackPressed() {
        Log.d("onBackPressed", "нажали +");
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer != null)
        {
            Log.d("onBackPressed", "drawer != null");
            if (drawer.isDrawerOpen(GravityCompat.START)) {
                Log.d("onBackPressed", "Боковое меню открыто");
                drawer.closeDrawer(GravityCompat.START);
            } else {
                Log.d("onBackPressed", "Боковое меню закрыто");
                if(doubleBackExit())
                    super.onBackPressed();
            }
        }
        Log.d("onBackPressed", "нажали -");
    }
    Boolean backPressedOne = false;
    private boolean doubleBackExit(){
        if (this.backPressedOne) {
            return true; }
        else {
            this.backPressedOne = true;
            Toast.makeText(this, "Нажмите ещё раз, чтобы выйти", Toast.LENGTH_SHORT).show();
            //Обнуление счётчика через 4 секунд
            new Handler().postDelayed(() -> backPressedOne = false, 4000);
            return false;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    public void mainImportConfig()
    {
        Intent intent = new Intent(this, ImportActivity.class);
        startActivity(intent);
        finish();
/*
        new FileChooser(this).setFileListener(new FileChooser.FileSelectedListener()
        {
            @Override
            public void fileSelected(final File file)
            {
                InputStream is = null;
                try
                {
                    is = new FileInputStream(file);
                }
                catch(FileNotFoundException e)
                {
                    Log.e("FileNotFoundException", "can't create FileInputStream");
                }

                Config pConfig = new Config(is);
                if(pConfig.m_cRooms.size() != 0)
                {
                    SafeExit();
                    Config.Instance = pConfig;
                    Config.SaveXml(pConfig, MainActivity.this);
                    Toast.makeText(getApplicationContext(), R.string.importSuccess, Toast.LENGTH_LONG).show();
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
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
        }).showDialog();
        */
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings)
        {
            String prefsCode = Prefs.getPrefs(this);
            if(prefsCode.isEmpty())
                startActivity(new Intent(MainActivity.this, Prefs.class));
            else {
                AuthorizationActivity.s_sTrueCode = prefsCode;
                AuthorizationActivity.s_pUnlocker = new AuthorizationActivity.SFUnlocker() {
                    @Override
                    public void Unlock(boolean bUnlock) {
                        if (bUnlock)
                            startActivity(new Intent(MainActivity.this, Prefs.class));
                    }
                };
                Intent intent = new Intent(this, AuthorizationActivity.class);
                startActivity(intent);
            }
            return true;
        }
        if (id == R.id.action_load)
        {
            String prefsCode = Prefs.getPrefs(this);
            if(prefsCode.isEmpty())
                mainImportConfig();
            else {
                AuthorizationActivity.s_sTrueCode = prefsCode;
                AuthorizationActivity.s_pUnlocker = new AuthorizationActivity.SFUnlocker() {
                    @Override
                    public void Unlock(boolean bUnlock) {
                        if (bUnlock)
                            mainImportConfig();
                    }
                };
                Intent intent = new Intent(this, AuthorizationActivity.class);
                startActivity(intent);
            }
            return true;
        }
        if(id == R.id.action_alarm)
        {
            int roomIndex = -1;
            int subsystemIndex = -1;
            for (Subsystem pSubsystem : Config.Instance.m_cSubsystems)
            {
                if (pSubsystem.GetAlarmedCount() > 0 && pSubsystem.m_pRoom.m_iIndex > mSectionsPagerAdapter.m_iRoom) {
                    roomIndex = pSubsystem.m_pRoom.m_iIndex;
                    subsystemIndex = pSubsystem.m_iIndex;
                    break;
                }
            }
            if(roomIndex == -1)
            {
                for (Subsystem pSubsystem : Config.Instance.m_cSubsystems)
                {
                    if (pSubsystem.GetAlarmedCount() > 0 && pSubsystem.m_pRoom.m_iIndex < mSectionsPagerAdapter.m_iRoom) {
                        roomIndex = pSubsystem.m_pRoom.m_iIndex;
                        subsystemIndex = pSubsystem.m_iIndex;
                        break;
                    }
                }
            }
            if(roomIndex != -1)
            {
                Objects.requireNonNull(getSupportActionBar()).setTitle(Config.Instance.m_cRooms.get(roomIndex).m_sName);

                Log.d("111111111111111", "Switching to room #" + roomIndex);

                //mSectionsPagerAdapter = (SectionsPagerAdapter)mViewPager.getAdapter();
                mSectionsPagerAdapter.m_iRoom = roomIndex;
                Objects.requireNonNull(mViewPager.getAdapter()).notifyDataSetChanged();

                tabLayout.removeAllTabs();
                for (Subsystem pSubsystem : Config.Instance.m_cRooms.get(roomIndex).m_cSubsystems) {
                    TabLayout.Tab newTab = tabLayout.newTab();
                    //if(pSubsystem.m_sName.equalsIgnoreCase("0"))
                    //    newTab.setIcon(R.drawable.tab_light_indicator);
                    //else
                    //    newTab.setIcon(R.drawable.tab_media_indicator);
                    newTab.setText(pSubsystem.m_sName);
                    tabLayout.addTab(newTab);
                }
                Objects.requireNonNull(tabLayout.getTabAt(subsystemIndex)).select();

                if(Config.Instance.m_cRooms.get(roomIndex).m_cSubsystems.size() > 1)
                    tabLayout.setVisibility(View.VISIBLE);
                else
                    tabLayout.setVisibility(View.GONE);

                mViewPager.getAdapter().notifyDataSetChanged();
            }
        }
        if(id == R.id.action_debug)
        {
            //Intent intent = new Intent(this, LogActivity.class);
            //Bundle b = new Bundle();
            //b.putStringArrayList("log", m_cLogLines);
            //intent.getExtras().putStringArrayList("log", m_cLogLines);
            //intent.putExtras(b);
            //startActivity(intent);
            LogActivity newFragment = LogActivity.newInstance(Logger.Instance.m_cDebugLines, "Отладочная информация:");
            newFragment.show(getFragmentManager(), "debugDialog");
        }
        if(id == R.id.action_connection)
        {
            //Intent intent = new Intent(this, LogActivity.class);
            //Bundle b = new Bundle();
            //b.putStringArrayList("log", m_cLogLines);
            //intent.getExtras().putStringArrayList("log", m_cLogLines);
            //intent.putExtras(b);
            //startActivity(intent);
            LogActivity newFragment = LogActivity.newInstance(m_cLogLines, "Состояние связи с оборудованием:");
            newFragment.show(getFragmentManager(), "logDialog");
        }
        if(id == R.id.action_voice)
        {
            LogUtils.goInfo("voice227", "Voice start");
            setVoiceIcon(true);
            VoiceToText voiceToText = new VoiceToText(this);
            voiceToText.start(this);
        }
        if (id == R.id.action_reset_exit)
        {
            Prefs.setId("",this);
            SafeExit();
            finish();
            System.exit(0);
            return true;
        }
        if (id == R.id.action_exit)
        {
            SafeExit();
            finish();
            System.exit(0);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void voiceText(String text) {
        VoiceUtils.debugOutput(getAppContext(),"Расшифровка звука: " + text);
        Integer goTo = VoiceUtils.getGoToLabel(text);
        if(goTo >= -1) {
            VoiceUtils.debugOutput(getAppContext(), "Это переход на: " + goTo);
            if(goTo >= 0)
                showRoom(goTo);
        }
        else {
            if(!VoiceUtils.execIndicator(getAppContext(), text)) {
                VoiceUtils.debugOutput(getAppContext(),"Общая неудача");
            }
        }
        setVoiceIcon(false);
    }

    void setVoiceIcon(Boolean onOff) {
        Toolbar toolbar = findViewById(R.id.toolbar);
        MenuItem item = null;
        if (toolbar != null) {
            item = toolbar.getMenu().findItem(R.id.action_voice);
        }
        if (item != null)
            item.setIcon(onOff ? R.drawable.ic_voice_on : R.drawable.ic_voice_off);
    }

    public void SwitchToSubsystem(String subsystemId)
    {
        for (Subsystem pSubsystem : Config.Instance.m_cSubsystems)
        {
            if (pSubsystem.m_sID.equals(subsystemId))
            {
                Objects.requireNonNull(getSupportActionBar()).setTitle(pSubsystem.m_pRoom.m_sName);

                Log.d("111111111111111", "Switching to room #" + pSubsystem.m_pRoom.m_iIndex);

                //mSectionsPagerAdapter = (SectionsPagerAdapter)mViewPager.getAdapter();
                mSectionsPagerAdapter.m_iRoom = pSubsystem.m_pRoom.m_iIndex;
                Objects.requireNonNull(mViewPager.getAdapter()).notifyDataSetChanged();

                tabLayout.removeAllTabs();
                for (Subsystem pRoomSubsystem : pSubsystem.m_pRoom.m_cSubsystems)
                {
                    TabLayout.Tab newTab = tabLayout.newTab();
                    //if(pSubsystem.m_sName.equalsIgnoreCase("0"))
                    //    newTab.setIcon(R.drawable.tab_light_indicator);
                    //else
                    //    newTab.setIcon(R.drawable.tab_media_indicator);

                    newTab.setText(pSubsystem.m_sName);//TODO тут кажется бага. По иде надо pRoomSubsystem.m_sName
                    tabLayout.addTab(newTab);
                }
                Objects.requireNonNull(tabLayout.getTabAt(pSubsystem.m_pRoom.m_cSubsystems.indexOf(pSubsystem))).select();

                if(pSubsystem.m_pRoom.m_cSubsystems.size() > 1)
                    tabLayout.setVisibility(View.VISIBLE);
                else
                    tabLayout.setVisibility(View.GONE);

                mViewPager.getAdapter().notifyDataSetChanged();
                break;
            }
        }

    }

    private boolean m_bStarted = false;

    @Override
    protected void onResume()
    {

        super.onResume();

        Log.d("SF", "onResume, calling StopCheckAlarmShedule()" );
        StopCheckAlarmShedule();

        if(SFServer.Instance != null)
            SFServer.Instance.Resume();

        m_bStarted = true;
        UpdateConnectionStatus();
    }

    public ArrayList<String> m_cLogLines = new ArrayList<>();

    private boolean m_bOldAlarm = false;

    private void UpdateAlarmStatus()
    {
        if(!m_bStarted)
            return;

//        NavigationView navigationView = findViewById(R.id.nav_view);
//        final Menu menu = navigationView.getMenu();

        boolean bAlarm = false;
        int alarmsCount = 0;
        if(Config.Instance != null)
        {
            for (Room pRoom : Config.Instance.m_cRooms)
            {
                boolean bRoomAlarm = false;
                for (Subsystem pSubsystem : pRoom.m_cSubsystems)
                {
                    int alarmedCount = pSubsystem.GetAlarmedCount();
                    if (alarmedCount > 0)
                    {
                        bRoomAlarm = true;
                        alarmsCount += alarmedCount;
                    }
                }

                List<ExpandedMenuModel> cList = m_cMenu.get(pRoom.m_iIndex);

                //Log.d("UpdateAlarmStatus", "Got item " + pRoom.m_iIndex + " = " + pItem);

                if (bRoomAlarm)
                {
                    bAlarm = true;
                }

                if (cList != null)
                {
                    for (ExpandedMenuModel pItem : cList)
                        pItem.setIcon(bRoomAlarm ? R.drawable.ic_alarm : R.drawable.ic_ok);
                }
                else
                    Log.d("UpdateAlarmStatus", "Can't get menu item " + pRoom.m_iIndex);
            }
        }
        else
            Log.e("UpdateAlarmStatus", "Config.Instance is NULL !!!!");

        Toolbar toolbar = findViewById(R.id.toolbar);
        final MenuItem alarmItem =
                toolbar != null ? toolbar.getMenu().findItem(R.id.action_alarm) : null;
        if(alarmItem != null) {
            //alarmItem.setIcon(bAlarm ? R.drawable.ic_alarm : R.drawable.ic_ok);

//            ImageView imageView = (ImageView)MenuItemCompat.getActionView(alarmItem);
            ImageView imageView = (ImageView)alarmItem.getActionView();

            if(imageView == null)
            {
                LayoutInflater inflater = (LayoutInflater) this.getSystemService(LAYOUT_INFLATER_SERVICE);
                if (inflater != null)
                {
                    imageView = (ImageView) inflater.inflate(R.layout.alarm_refresh, null);
                    imageView.setImageResource(R.drawable.ic_ok);

                    imageView.setOnTouchListener((v, event) -> {
                        onOptionsItemSelected(alarmItem);
                        return false;
                    });

                    alarmItem.setActionView(imageView);
                }
            }

            if(imageView != null && bAlarm != m_bOldAlarm)
            {
                if(bAlarm)
                {
                    imageView.setImageResource(R.drawable.ic_alarm);

                    Animation rotation = AnimationUtils.loadAnimation(this, R.anim.rotate);
                    rotation.setRepeatCount(Animation.INFINITE);
                    imageView.startAnimation(rotation);
                }
                else
                {
                    imageView.setImageResource(R.drawable.ic_ok);

                    imageView.clearAnimation();
                }
            }

            m_bOldAlarm = bAlarm;

            if(bAlarm)
                showForegroundNotification(1, R.drawable.ic_alarm, getString(R.string.notifyAlarms) + alarmsCount);
            else
            {
                //showForegroundNotification(R.drawable.ic_ok, "Тревог на объекте: " + iCount);
                NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                if (mNotificationManager != null)
                {
                    mNotificationManager.cancel(1);
                }
            }
        }
    }

    private void showForegroundNotification(int notifyId, int icon, String contentText) {
        // Create intent that will bring our app to the front, as if it was tapped in the app
        // launcher
        //Log.d("MainActivity", "showForegroundNotification");
        Intent showTaskIntent = new Intent(getApplicationContext(), SplashActivity.class);
        showTaskIntent.setAction(Intent.ACTION_MAIN);
        showTaskIntent.addCategory(Intent.CATEGORY_LAUNCHER);
        showTaskIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        PendingIntent contentIntent = PendingIntent.getActivity(
                this,
                0,
                showTaskIntent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        Bitmap iconBmp = BitmapFactory.decodeResource(getResources(),icon);

        Notification.Builder nBuilder = new Notification.Builder(getApplicationContext())
                .setContentTitle(contentText)//getString(R.string.app_name))
                //.setContentText(contentText)
                .setSmallIcon(icon)
                .setLargeIcon(iconBmp)
                .setWhen(System.currentTimeMillis())
//                .setAutoCancel(true)
                .setContentIntent(contentIntent);
        //startForeground(1, notification);

        if(Prefs.getSound(this))
        {
            nBuilder.setVibrate(new long[]{1000,
                                           1000,
                                           1000,
                                           1000,
                                           1000})
                    .setLights(Color.RED, 3000, 3000)
                    .setDefaults(Notification.DEFAULT_ALL)
                    .setOnlyAlertOnce(true);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            nBuilder.setColor(Color.RED);
        }

        Notification notification = nBuilder.build();
        NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (mNotificationManager != null)
        {
            mNotificationManager.notify(notifyId, notification);
        }
        //Log.d("MainActivity", "mNotificationManager.notify() - OK");
    }

    public void UpdateConnectionStatus()
    {
        if(!m_bStarted)
            return;

        Toolbar toolbar = findViewById(R.id.toolbar);
        MenuItem alarmItem = null;
        if (toolbar != null)
        {
            alarmItem = toolbar.getMenu().findItem(R.id.action_connection);
        }
        if(alarmItem != null)
            alarmItem.setIcon(SFServer.IsConnected() ? R.drawable.ic_connect : R.drawable.ic_disconnect);

        if(!SFServer.IsConnected() && Prefs.getConnectionNotify(this))
        {
            showForegroundNotification(2, R.drawable.ic_disconnect, getString(R.string.notiyNoCarrier));
        }
        else
        {
            NotificationManager mNotificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            if (mNotificationManager != null)
            {
                mNotificationManager.cancel(2);
            }
        }

        UpdateAlarmStatus();
    }

    private void SafeExit()
    {
        String str = Save();
        Log.d("SF", "onPause, saving = '" + str + "'");
        // Save the current puzzle
        getPreferences(MODE_PRIVATE).edit().putString(PREF_INDICATORS, str).apply();

        try
        {
            if (SFServer.Instance != null)
                SFServer.Instance.Stop();
        }
        catch (Exception e)
        {
            Log.e("SF", "SFServer.Instance.Stop() exception: ", e);
        }
        Log.d("SF", "onPause, StartCheckAlarmShedule()...");
        StartCheckAlarmShedule();
    }

    @Override
    protected void onPause()
    {
        m_bStarted = false;
        super.onPause();
        SafeExit();
    }

    public void Load(String indicators)
    {
        Log.d("Glindor3","MainActivity Load 0");

        if(Config.Instance == null)
            return;

        if(Config.Instance.m_cSubsystems == null || Config.Instance.m_cRooms == null)
            return;

        if(Config.Instance.m_cSubsystems.size() == 0)
            return;

        if(Config.Instance.m_cRooms.size() == 0)
            return;
        Log.d("Glindor3","MainActivity Load 1");

        int iIndCount = 0;
        for (Subsystem pSubsystem : Config.Instance.m_cSubsystems)
            iIndCount += pSubsystem.getIndicatorsCount();
        Log.d("Glindor3","MainActivity Load 2");

        if(indicators.length() < iIndCount + 1)
        {
            mSectionsPagerAdapter.m_iRoom = 0;
            Objects.requireNonNull(mViewPager.getAdapter()).notifyDataSetChanged();

            tabLayout.removeAllTabs();
            for (Subsystem pSubsystem : Config.Instance.m_cRooms.get(mSectionsPagerAdapter.m_iRoom).m_cSubsystems) {
                TabLayout.Tab newTab = tabLayout.newTab();
                //if(pSubsystem.m_sName.equalsIgnoreCase("0"))
                //    newTab.setIcon(R.drawable.tab_light_indicator);
                //else
                //    newTab.setIcon(R.drawable.tab_media_indicator);
                newTab.setText(pSubsystem.m_sName);
                tabLayout.addTab(newTab);
            }
            Objects.requireNonNull(tabLayout.getTabAt(0)).select();

            if(Config.Instance.m_cRooms.get(mSectionsPagerAdapter.m_iRoom).m_cSubsystems.size() > 1) {
                tabLayout.setVisibility(View.VISIBLE);
            }
            else
                tabLayout.setVisibility(View.GONE);

            Objects.requireNonNull(getSupportActionBar()).setTitle(Config.Instance.m_cRooms.get(mSectionsPagerAdapter.m_iRoom).m_sName);
            return;
        }
        Log.d("Glindor3","MainActivity Load 3");

        if(Prefs.getStart(this) == 2)
            mSectionsPagerAdapter.m_iRoom = indicators.charAt(0);
        else
            mSectionsPagerAdapter.m_iRoom = 0;

        if(mSectionsPagerAdapter.m_iRoom >= Config.Instance.m_cRooms.size())
            mSectionsPagerAdapter.m_iRoom = 0;
        Log.d("Glindor3","MainActivity Load 4");

        Objects.requireNonNull(mViewPager.getAdapter()).notifyDataSetChanged();
        Log.d("Glindor3","MainActivity Load 5");

        tabLayout.removeAllTabs();
        for (Subsystem pSubsystem : Config.Instance.m_cRooms.get(mSectionsPagerAdapter.m_iRoom).m_cSubsystems) {
            TabLayout.Tab newTab = tabLayout.newTab();
            //if(pSubsystem.m_sName.equalsIgnoreCase("0"))
            //    newTab.setIcon(R.drawable.tab_light_indicator);
            //else
            //    newTab.setIcon(R.drawable.tab_media_indicator);
            newTab.setText(pSubsystem.m_sName);
            tabLayout.addTab(newTab);
        }
        Log.d("Glindor3","MainActivity Load 6");

        Objects.requireNonNull(tabLayout.getTabAt(0)).select();
        Log.d("Glindor3","MainActivity Load 7");

        if(Config.Instance.m_cRooms.get(mSectionsPagerAdapter.m_iRoom).m_cSubsystems.size() > 1) {
            tabLayout.setVisibility(View.VISIBLE);
        }
        else
            tabLayout.setVisibility(View.GONE);
        Log.d("Glindor3","MainActivity Load 9");

        Objects.requireNonNull(getSupportActionBar()).setTitle(Config.Instance.m_cRooms.get(mSectionsPagerAdapter.m_iRoom).m_sName);
        Log.d("Glindor3","MainActivity Load 10");

        int iPos = 1;
        for (Subsystem pSubsystem : Config.Instance.m_cSubsystems)
            iPos = pSubsystem.Load(indicators, iPos);
        Log.d("Glindor3","MainActivity Load 11");

    }

    public String Save()
    {
        StringBuilder sRes = new StringBuilder();

        sRes.append((char) mSectionsPagerAdapter.m_iRoom);

        for (Subsystem pSubsystem : Config.Instance.m_cSubsystems)
        {
            if(pSubsystem != null)
                sRes.append(pSubsystem.Save());
        }

        return sRes.toString();
    }

    public void Imitate()
    {
        UpdateAlarmStatus();
        if(Config.DEMO)
        {
            for (Subsystem pSubsystem : Config.Instance.m_cSubsystems)
            {
                pSubsystem.Imitate();
            }
        }
    }


    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentStatePagerAdapter
    {
        int m_iRoom;

        SectionsPagerAdapter(FragmentManager fm)
        {
            super(fm);
            m_iRoom = 0;
        }


        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            Log.d("Regul2222", "Creating fragment #" + position + " for room #" + m_iRoom);
            return PlaceholderFragment.newInstance(m_iRoom, position);
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            if(Config.Instance == null || Config.Instance.m_cRooms.size() <= m_iRoom)
                return 0;
            return Config.Instance.m_cRooms.get(m_iRoom).m_cSubsystems.size();
        }

        @Override
        public int getItemPosition(@NonNull Object object) {
            return POSITION_NONE;
        }

        @Override
        public CharSequence getPageTitle(int position) {
//            int[] imageResId = {R.drawable.light2, R.drawable.media1};

            String text = Config.Instance.m_cRooms.get(m_iRoom).m_cSubsystems.get(position).m_sName;

// генерируем название в зависимости от позиции
//            Drawable image = getResources().getDrawable(imageResId[position<2?position:1]);
            Drawable image = getResources().getDrawable(R.drawable.ic_ok);
            image.setBounds(0, 0, image.getIntrinsicWidth(), image.getIntrinsicHeight());
            // заменяем пробел иконкой
            SpannableString sb = new SpannableString("      " + text);
            ImageSpan imageSpan = new ImageSpan(image, ImageSpan.ALIGN_BOTTOM);
            sb.setSpan(imageSpan, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            return sb;

        }
    }
    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment
    {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_ROOM_NUMBER = "room_number";
        private static final String ARG_SUBSYSTEM_NUMBER = "subsystem_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int room, int subsystem) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_ROOM_NUMBER, room);
            args.putInt(ARG_SUBSYSTEM_NUMBER, subsystem);
            fragment.setArguments(args);
            Log.d("Regul2222", "setArguments(sys:"+subsystem+" room:"+ room+")");
            return fragment;
        }

        public PlaceholderFragment() {
        }

//        private float lastX;
  //      private ViewFlipper viewFlipper;

        @Override
        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState)
        {
            int roodId = Objects.requireNonNull(getArguments()).getInt(ARG_ROOM_NUMBER);
            int ssNum = getArguments().getInt(ARG_SUBSYSTEM_NUMBER);
            Log.d("Regul2222", "Create View for room #" + roodId + ", subsystem #" + ssNum);
            Room room = Config.Instance.m_cRooms.get(roodId);
            Subsystem s1 =  room.m_cSubsystems.get(ssNum);
            View rootView = new SubsystemUI(SFServer.Instance, getContext(),s1);

//            по этой формуле мы запоминаем список опрашиваемых переменых не для всей комнаты, а только для подсистемы. А так как этот конструктор вызвается для каждой подсистемы - непоследний затирается
//            SFServer.Instance.m_pRoomQuery = Config.Instance.m_cRooms.get(getArguments().getInt(ARG_ROOM_NUMBER)).m_cSubsystems.get(getArguments().getInt(ARG_SUBSYSTEM_NUMBER)).GetQueryString();

            // а так мы при каждом вызове каждой посистемы формируем список сразу на все помещение! И ПУСТЬ ПЕРЕЗАТИРАЕТСЯ!!!
            SFServer.Instance.m_pRoomQuery = Config.Instance.m_cRooms.get(getArguments().getInt(ARG_ROOM_NUMBER)).GetQueryString();

            Log.d("ISTRA", "Adding rooms..."+SFServer.Instance);

            for (Subsystem pSubsystem : Config.Instance.m_cSubsystems)
            {
                SFServer.Instance.m_pRoomQuery.Add(pSubsystem.m_sAlarm, pSubsystem);
            }

            Log.d("ISTRA","SFServer.Instance.m_pRoomQuery.Build(getContext()) room="+roodId+" sub="+getArguments().getInt(ARG_SUBSYSTEM_NUMBER));
/*            if(AddressString.roomId!=roodId){
                Log.d("ISTRA","Новая комната. Сделали pool");
            }
            else
                Log.d("ISTRA","Старая комната");
*/

//            SFServer.Instance.m_pRoomQuery.Build(roodId,getContext());
//            SFServer.Instance.Poll(false);
            SFServer.Instance.m_pRoomQuery.Build(roodId,getContext());
            SFServer.Instance.Poll(false);


            //View rootView = Config.Instance.m_cSubsystems.get(getArguments().getInt(ARG_ROOM_NUMBER)).m_pUI;
            //TextView textView = (TextView) rootView.findViewById(R.id.section_label);
            //textView.setText(getString(R.string.section_format, ));
/*            switch (getArguments().getInt(ARG_SECTION_NUMBER))
            {
                case 1:
                    rootView = inflater.inflate(R.layout.content3_main_view, container, false);
                    viewFlipper = (ViewFlipper) rootView.findViewById(R.id.viewflipper);
                    rootView.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent touchevent) {
                            switch (touchevent.getAction()) {

                                case MotionEvent.ACTION_DOWN:
                                    lastX = touchevent.getX();
                                    break;
                                case MotionEvent.ACTION_UP:
                                    float currentX = touchevent.getX();

                                    // Handling left to right screen swap.
                                    if (lastX < currentX) {

                                        // If there aren't any other children, just break.
                                        if (viewFlipper.getDisplayedChild() == 0)
                                            break;

                                        // Next screen comes in from left.
                                        viewFlipper.setInAnimation(this, R.anim.slide_in_from_left);
                                        // Current screen goes out from right.
                                        viewFlipper.setOutAnimation(this, R.anim.slide_out_to_right);

                                        // Display next screen.
                                        viewFlipper.showNext();
                                    }

                                    // Handling right to left screen swap.
                                    if (lastX > currentX) {

                                        // If there is a child (to the left), kust break.
                                        if (viewFlipper.getDisplayedChild() == 1)
                                            break;

                                        // Next screen comes in from right.
                                        viewFlipper.setInAnimation(this, R.anim.slide_in_from_right);
                                        // Current screen goes out from left.
                                        viewFlipper.setOutAnimation(this, R.anim.slide_out_to_left);

                                        // Display previous screen.
                                        viewFlipper.showPrevious();
                                    }
                                    break;
                            }
                            return false;
                        }
                    });

//                    ImageView image4 = (ImageView) rootView.findViewById(R.id.imageView14);
//                    image4.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            startActivity(new Intent(getActivity(), LoginActivity.class));
//                        }
//                    });
//                    ImageView image5 = (ImageView) rootView.findViewById(R.id.imageView15);
//                    image5.setOnClickListener(new View.OnClickListener() {
//                        @Override
//                        public void onClick(View view) {
//                            startActivity(new Intent(getActivity(), ScrollingActivity.class));
//                        }
//                    });

                    break;
                case 2:
                    rootView = inflater.inflate(R.layout.content_main_view, container, false);
                    break;
            }*/
            return rootView;
        }
    }
}