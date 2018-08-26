package jp.co.vivo_app.v_care;

import java.io.Serializable;
import java.util.ArrayList;

public class Calender implements Serializable {
    private int mYear;

    private int mMonth;

    private int mDate;

    private String mStime;

    private String mEtime;

    private String mTitle;

    private String mDetail;

    private String mId;

    private String mYID;


    private ArrayList<Calender> mCalenderArrayList;

    public int getYear(){
        return mYear;
    }
    public int getMonth() {
        return mMonth;
    }
    public int getDate(){
        return mDate;
    }
    public String getStime(){
        return mStime;
    }
    public String getEtime(){
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
    public String getYid() {
        return mYID;
    }


    public void setYear(int year){
        mYear = year;
    }
    public void setMonth(int month){
        mMonth = month;
    }
    public void setDate(int dete){
        mDate = dete;
    }
    public void setStime(String stime){
        mStime = stime;
    }
    public void setEtime(String etime){
        mEtime = etime;
    }
    public void setTitle(String title){
        mTitle = title;
    }
    public void setDetail(String detail){
        mDetail = detail;
    }
    public void setId(String id){
        mId = id;
    }
    public void setYid(String yid){
        mYID = yid;
    }



    public ArrayList<Calender> getCalender(){
        return mCalenderArrayList;
    }

    public Calender (int year,int month,int date,String stime,String etime,String title,String detail,String mid,String mYID) {
        mYear = year;
        mMonth = month;
        mDate = date;
        mStime = stime;
        mEtime = etime;
        mTitle = title;
        mDetail = detail;
        mId = mid;
        this.mYID = mYID;
    }
}


