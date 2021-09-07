package ru.cpc.smartflatview;

import android.util.Log;

public class RoomViewPollThread extends Thread
{
    private SFServer m_pServer;
    private boolean m_bRun = false;
    private int pollCounter = 0;
    
    public RoomViewPollThread(SFServer pRoomView)
    {
        m_pServer = pRoomView;
    }
 
    public void setRunning(boolean run) 
    {
        Log.d("SFServer", "RoomViewPollThread setRunning = " + run);
        if(run)
        {
            m_pServer.Poll(false);
            pollCounter = 1;
        }

        //Log.d("SFServer", "C: RoomViewPollThread pollCounter = " + pollCounter);
        m_bRun = run;

        if(!m_bRun)
            interrupt();
    }
    
    @Override
    public void run() 
    {
        Log.d("SFServer", "RoomViewPollThread::run ");
        while (m_bRun)
        {
            try 
            {
                Log.d("SFServer", "RoomViewPollThread::run pollCounter = " + pollCounter);
            	m_pServer.Poll(pollCounter > 0);
                Log.d("SFServer", "RoomViewPollThread::run 1");
            	m_pServer.Imitate();
                Log.d("SFServer", "RoomViewPollThread::run 2");
//            	if(m_pServer.post(new Runnable()
//					            	{
//										public void run()
//										{
//							            	//m_pServer.Poll();
//							            	m_pServer.Imitate();
//										}
//									}))
                Log.d("SFServer", "RoomViewPollThread::run 3 " + Config.Instance.m_iPollPeriod);

                sleep(Config.Instance.m_iPollPeriod/120);
                Log.d("SFServer", "RoomViewPollThread::run 4");

                if(pollCounter >= 120)
                {
                    pollCounter = 0;
                }
                else
                {
                    pollCounter++;
                }
                Log.d("SFServer", "RoomViewPollThread::run 5");

                //Log.d("SFServer", "C: RoomViewPollThread pollCounter = " + pollCounter);
            }
            catch (InterruptedException e) 
            {
				Log.d("SFServer","Фигня:"+ e.getMessage());
				//e.printStackTrace();
			} 
            finally 
            {
                Log.d("SFServer","RoomViewPollThread::run вышли");
            }
        }
    }    
}
