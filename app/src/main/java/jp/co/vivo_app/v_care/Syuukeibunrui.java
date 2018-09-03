package jp.co.vivo_app.v_care;

import java.io.Serializable;
import java.util.ArrayList;

public class Syuukeibunrui implements Serializable{
    private String mId;

    private String mName;

    public ArrayList<Syuukeibunrui> mSyuukeibunruiArrayList;

    public String getSyuukeibunruiId(){
        return mId;
    }

    public String getName(){
        return mName;
    }

    public Syuukeibunrui(String syuukeibunruiid,String name) {
        mId = syuukeibunruiid;
        mName = name;
    }
}
