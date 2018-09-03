package jp.co.vivo_app.v_care;

import java.io.Serializable;
import java.util.ArrayList;

public class Sagyoukubun implements Serializable{
    private String mId;

    private String mName;

    public ArrayList<Sagyoukubun> mSagyoukubunArrayList;

    public String getSyuukeibunruiId(){
        return mId;
    }

    public String getName(){
        return mName;
    }

    public Sagyoukubun(String sagyoukubunid,String name) {
        mId = sagyoukubunid;
        mName = name;
    }
}
