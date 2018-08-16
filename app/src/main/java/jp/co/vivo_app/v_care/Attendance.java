package jp.co.vivo_app.v_care;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

public class Attendance implements Serializable{
    //Firebaseから取得した年
    private int mYear;

    //Firebaseから取得した月
    private int mMonth;
    //日
    private int mDay;

    //Firebaseから取得した時間
    private String mStime;

    //Firebaseから取得した時間
    private String mEtime;

    //ID
    private String mId;

    //備考
    private String mBikou;

    private ArrayList<Attendance> mAttendanceArrayList;

    public int getYear() {
        return mYear;
    }

    public int getMonth(){
        return mMonth;
    }

    public int getDay() {
        return mDay;
    }

    public String getStime() {
        return mStime;
    }

    public String getEtime() {
        return mEtime;
    }

    public String getId() {
        return mId;
    }

    public String getBikou() {
        return mBikou;
    }

    public void setBikou(String bikou){
        mBikou = bikou;
    }

    public ArrayList<Attendance> getAttendance() {
        return mAttendanceArrayList;
    }

    public Attendance(int year, int month,int day,String stime,String etime,String id,String bikou){
        mYear = year;
        mMonth = month;
        mDay = day;
        mStime = stime;
        mEtime = etime;
        mId = id;
        mBikou = bikou;
    }





}
