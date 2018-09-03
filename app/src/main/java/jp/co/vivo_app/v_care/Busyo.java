package jp.co.vivo_app.v_care;

import java.io.Serializable;
import java.util.ArrayList;

public class Busyo implements Serializable{
    private String mBid;

    private String mName;

    private String mId;

    public ArrayList<Busyo> mBusyoArrayList;



    public String getBid() {
        return mBid;
    }

    public String getName() {
        return mName;
    }

    public ArrayList<Busyo> getBusyo() {
        return mBusyoArrayList;
    }



    public Busyo(String bid, String name,String id) {
        mBid = bid;
        mName = name;
        mId = id;
    }

}
