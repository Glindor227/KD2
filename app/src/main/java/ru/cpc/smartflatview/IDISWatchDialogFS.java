package ru.cpc.smartflatview;

/*

public class IDISWatchDialogFS extends DialogFragment
        implements
        RWatcherListener,RSearcherListener,View.OnClickListener
{
    public int mMode=0; //режим функционирования. 0= просмотр. 1-архив
    public int mPlay=-1;//идет просмотр архива(1), остановлен просмотр(0), не архив(-1)
    public RTime lastTime=null;//
    private static final String TAG = "IDISWatchDialogFS";
    // Debug Log
    private static final boolean DEBUG_LOG = false;

    private ScreenLayout mScreenLayout;

    private RStatus _status = null;
    private REvent[] mEvents;
    private RSearcher mSearcher = null;
    private RWatcher mRWatcher = null;

    View viewBig=null;

    private static final boolean IPS_LOG = false;
    private Ips _ips = IPS_LOG ? new Ips() : null;

    private int mWatchSelectedCamera = -1;
    private int mSearchSelectedCamera = -1;
    private int mCountOfCamera = 64;

    IDISWatchDialogFullScreenExitListener mParent=null;

    public void Init(RWatcher watcher, IDISWatchDialogFullScreenExitListener parent)
    {
        mMode = 0;

        mRWatcher = watcher;
        mParent = parent;
    }

    public void Init(RSearcher searcher, IDISWatchDialogFullScreenExitListener parent)
    {
        mMode = 1;

        mSearcher = searcher;
        mParent = parent;
    }

    private VerticalTextView mDateTimeTextView;
    private VerticalTextView mNoCarrierTextView;
    private TextView tv1;

    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.base_watch_container:
            case R.id.noCarrierTextView:
                //IDISWatchDialogFullScreenExitListener listener = (IDISWatchDialogFullScreenExitListener)getParentFragment();
                //listener.OnFullScreenExit();
                mParent.OnFullScreenExit();

                this.dismiss();

                break;
        }
    }
    private void changeConstraints1(ConstraintSet set)
    {

    }

    @Override
    public void onStart()
    {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getDialog().setTitle("Видео");

        VideoBitmapPool.initialize(null);

        viewBig = inflater.inflate(R.layout.idis_watch_fullscreen, null);

        mNoCarrierTextView = (VerticalTextView) viewBig.findViewById(R.id.noCarrierTextView);

        mDateTimeTextView = (VerticalTextView) viewBig.findViewById(R.id.dateTimeTextView);
        setDateTime(0, 0, 0, 0, 0, 0);
        //       tv1 = (TextView) viewBig.findViewById(R.id.textViewLOG);
        lastTime = new RTime(0,0,0);

        FrameLayout contentHolder = (FrameLayout) viewBig.findViewById(
                R.id.base_watch_container);
        ;
        contentHolder.setOnClickListener(this);
        mScreenLayout = new ScreenLayout(contentHolder.getContext());

        contentHolder.removeAllViews();
        contentHolder.addView(mScreenLayout);

        if(mMode == 0)
        {
            mRWatcher.setListener(this);
        }
        else
        {
            mSearcher.setListener(this);
        }

        //onConnected();

        return viewBig;
    }

    @Override
    public void onDestroyView() {
//        if (misFenCleanupRequired) {
//            onAppBackground();
//        }

        super.onDestroyView();
    }

    @Override
    public void onConnected() {

        if (IPS_LOG) {
            _ips.reset();
        }

        //RCore.getInstance().setResolution(new RSize(mScreenLayout.getWidth(), mScreenLayout.getHeight()));
        //RCore.getInstance().setResolution(new RSize());
        //RCore.getInstance().setResolution(new RSize(VideoBitmapPool.DEFAULT_ELEMENT_WIDTH*2, VideoBitmapPool.DEFAULT_ELEMENT_HEIGHT*2));
//        RCore.getInstance().setResolution(new RSize());


        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
//                mNoCarrierTextView.setText("НЕТ СВЯЗИ");
                mNoCarrierTextView.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void onDisconnected(final int whyDisconnect, int invalidLoginCount, RDisconnectInfo attachment) {
        if (DEBUG_LOG) {
            Log.v(TAG, "onDisconnected");
            Log.v(TAG, "Disconnected Reason = " + whyDisconnect);
            Log.v(TAG, "invalidLoginCount = " + invalidLoginCount);
        }

        if (IPS_LOG) {
            _ips.reset();
        }

        mWatchSelectedCamera = -1;

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (whyDisconnect != WhyDisconnected.LOGOUT) {
                    mNoCarrierTextView.setVisibility(View.VISIBLE);
                    mNoCarrierTextView.setText(R.string.idisNoCarrier);
                    mNoCarrierTextView.invalidate();
//                    Toast.makeText(getActivity(), "Watch : connection failed " + whyDisconnect, Toast.LENGTH_SHORT).show();
                }
//                if (!isFinishing()) {
//                    finish();
//                }
            }
        });
    }

    @Override
    public void onStatusLoaded(RStatus status) {
        if (_status != null) {
            synchronized (_status) {
                _status = status;
            }
        }
        else {
            _status = status;
        }
    }

    public void setDateTime(int YYYY, int MM, int DD, int hh, int mm, int ss) {
        String datetime = String.format(Locale.US,"%04d-%02d-%02d %02d:%02d:%02d", YYYY, MM, DD, hh, mm, ss);
        if(mMode==1)
            datetime = String.format(Locale.US,"%04d-%02d-%02d %02d:%02d:%02d", YYYY, MM, DD, hh, mm, ss);
        mDateTimeTextView.setText(datetime);
        mDateTimeTextView.invalidate();
    }

    private boolean mFrameBusy = false;
    @Override
    public void onFrameLoaded(final int camera, final RString title, final RDateTime dateTime, final long peerBuffer, final RSize cvtSize, final RSize originSize) {
        if(mMode==1) {
            if (DEBUG_LOG) {
                Log.v(TAG, "onFrameLoaded ARCH");
            }
        }
        else
        if (DEBUG_LOG) {
            Log.v(TAG, "onFrameLoaded");
        }

        if (IPS_LOG) {
            if (!_ips.isStarted()) {
                _ips.start();
            }
            _ips.increment();
            float ips = _ips.getIps();
            Log.v(TAG, "onFrameLoaded " + cvtSize.toString() + "(" + ips + "ips)");
        }



        if (title != null) {
            if (DEBUG_LOG) {
                Log.v(TAG, "frame title - " + title.toString() + (title.isUTF8() ? "(UTF-8)" : "(Windows)"));
            }
        }

        // waiting for ScreenLayout instantiation.
        // this wait routine is applied only to the first frame.
        while (mScreenLayout == null) {
            try {
                if (DEBUG_LOG) {
                    Log.v(TAG, "waiting 500ms...");
                }

                Thread.sleep(500);
            }
            catch (InterruptedException e) {
                if (DEBUG_LOG) {
                    Log.e(TAG, "screenLayout is missing");
                }

                e.printStackTrace();
            }
        }

        if (DEBUG_LOG) {
            Log.v(TAG, "Shoot to ScreenView.drawImage");
        }

        if (DEBUG_LOG) {
            Log.v(TAG, "converted image");
        }

        if(mFrameBusy)
            return;

        mFrameBusy = true;

        if (mScreenLayout != null) {
            if (DEBUG_LOG) {
                Log.v(TAG, "Shoot to ScreenView.drawImage");
            }

            Log.v(TAG, "onFrameLoaded " + cvtSize.toString());

            final byte[] image_to_draw = PeerMemory.allocateByteArray(peerBuffer, cvtSize.width() * cvtSize.height() * 2);

            try
            {
                getActivity().runOnUiThread(new Runnable()
                {
                    @Override
                    public void run() {
                        try {

                            if (DEBUG_LOG) {
                                Log.v(TAG, "started posting...");
                            }
                            mNoCarrierTextView.setVisibility(View.GONE);



                            mScreenLayout.drawImage(image_to_draw, cvtSize.width(), cvtSize.height());
                            setDateTime(dateTime.date().year(), dateTime.date().month(),
                                    dateTime.date().day(), dateTime.time().hour(),
                                    dateTime.time().minute(), dateTime.time().second());

                            if (DEBUG_LOG) {
                                Log.v(TAG, "finished posting...");
                            }

                            mFrameBusy = false;
                        }
                        catch (Exception e){
                            Log.v("Glindor","Exception = "+e.getMessage());
                        }

                    }
                });
            }
            catch (Exception ex)
            {
                Log.e(TAG, ex.getMessage());
                mFrameBusy = false;
            }
        }
    }

    @Override public void onFrameStopped(int var1){}
    @Override public void onReceiveDateList(RDate[] dates)
    {
        if (DEBUG_LOG) {
            Log.v(TAG, "onReceiveDateList");
        }
        //mRecordedDates = dates;
    }
    @Override public void onReceiveEventList(REvent[] var1){}
    @Override public void onNoFrameLoaded(){}
    @Override public void onReceiveTimeList(boolean[] times)
    {
        if (DEBUG_LOG) {
            Log.v(TAG, "onReceiveTimeList");
        }
        //mRecordedTimes = times;

    }
    @Override public void onReceiveMinuteList(RDateTime var1, byte[] var2){}

    @Override public void onEventLoaded(REvent event) {}
    @Override public void onReceivePtzPreset(int camera, RPtzPreset[] presets) {}
    @Override public void onReceivePtzAutoFocusBegin(int camera) {}
    @Override public void onReceivePtzAutoFocusEnd(int camera) {}
    @Override public void onReceivePushEnabledResult(int pushEnabledResult) {}
    @Override public void onReceivePushEnabledStatus(boolean enabled) {}
    @Override public void onReceivePasswordReissueStartResponse(int result, RString operationId, int authRemainTime) {}
    @Override public void onReceivePasswordReissueStatusResponse(int result, RString operationId, int authRemainTime) {}
    @Override public void onReceivePasswordReissueAuthConfirmResponse(int result, RString operationId, RString securityCode) {}
    @Override public void onReceivePasswordReissueResponse(int result, RString operationId, RString info) {}
    @Override public void onReceivePasswordReissueCancelResponse(int result, RString operationId) {}
    @Override public void onReceiveRemoteUpgradeDeviceCheck(int status, RUpgradeDevice[] devices) {}
    @Override public void onReceiveRemoteUpgradeDeviceFileInfos(RUpgradeFile[] fileInfos) {}
    @Override public void onReceiveRemoteUpgradeStatus(int deviceId, int status, boolean done, int percentage) {}
    @Override public void onReceiveRemoteUpgradeDeviceCancel(int deviceId, int result) {}
    @Override public void onReceiveRawProtocol(long buf, int size) {}
    @Override public void onReceiveResponseGpbSetup(long buf, int size) {}
    @Override public void onReceiveResponseModifyGpbSetupResult(long buf, int size) {}
    @Override public void onReceivePasswordResetStartResponse(RString publicKey) {}
    @Override public void onReceivePasswordResetResponse(int result, int[] failedReason) {}
    @Override public void onReceiveUwbCommandResponse(long buf, int size) {}
    @Override public void onReceiveFormatResult(int result) {}

    @Override
    public void onAudioConnected() {
        getActivity().runOnUiThread(new Runnable() {

            @Override
            public void run() {

            }

        });
    }

    @Override
    public void onAudioDisconnected(int reason) {

    }

    @Override
    public void onReceiveReceivingAudioFormat(RAudioFormat recvInfo) {
        if (!recvInfo.isValid()) {
            Log.v(TAG, "onReceiveReceivingAudioFormat invalid");
            return;
        }

        Log.v(TAG, "onReceiveReceivingAudioFormat");

//        AudioPlayer.getInstance().initialize(recvInfo);
    }

    @Override
    public void onReceiveSendingAudioFormat(RAudioFormat sendInfo) {
        if (!sendInfo.isValid()) {
            Log.v(TAG, "onReceiveSendingAudioFormat invalid");
            return;
        }
        Log.v(TAG, "onReceiveSendingAudioFormat");


    }

    @Override
    public void onReceiveAudioData(byte[] data) {
        Log.v(TAG, "onReceiveAudioData " + data.length);

//        AudioPlayer.getInstance().play(data);
    }
}
*/
