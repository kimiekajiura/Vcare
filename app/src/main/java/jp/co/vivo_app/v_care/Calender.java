package jp.co.vivo_app.v_care;

import java.io.Serializable;
import java.util.ArrayList;

public class Calender implements Serializable {
    private int mDate;

    private int mStime;

    private int mEtime;

    private String mTitle;

    private String mDetail;

    private String mId;

    private int mYID;


    private ArrayList<Calender> mCalenderArrayList;

    public int getDate(){
        return mDate;
    }

    public int getStime(){
        return mStime;
    }
    public int getEtime(){
        return mEtime;
    }
    public String getTitle(){
        return mTitle;
    }
    public String getDetail(){
        return mDetail;
    }

    public String getId() {
        return mId;
    }

    public int getYid() {
        return mYID;
    }

    public ArrayList<Calender> getCalender(){
        return mCalenderArrayList;
    }

    public Calender (int date,int stime,int etime,String title,String detail,int yid) {
        mDate = date;
        mStime = stime;
        mEtime = etime;
        mTitle = title;
        mDetail = detail;
        mYID = yid;
    }
}


