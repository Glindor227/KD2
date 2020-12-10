package ru.cpc.smartflatview;

import java.util.ArrayList;


public class Room
{
    public int m_iIndex;

    public String m_sName;

    String m_sID;

    public ArrayList<Subsystem> m_cSubsystems = new ArrayList<Subsystem>();

    public Room(int index, String sName, String sID)
    {
        m_iIndex = index;

        m_sName = sName;

        m_sID = sID;
    }

    public Room AddSubsystem(Subsystem pSubsystem)
    {
        if(pSubsystem != null)
        {
            m_cSubsystems.add(pSubsystem);
            pSubsystem.m_pRoom = this;
        }

        return this;
    }
    public AddressString GetQueryString()
    {
        AddressString pAddr = new AddressString();
        for(Subsystem pSS : m_cSubsystems) {
            for (Indicator pInd : pSS.m_cIndicators)
                pInd.GetAddresses(pAddr);
        }
        return pAddr;
    }

}
