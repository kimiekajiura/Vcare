package jp.co.vivo_app.v_care;

import java.io.Serializable;
import java.util.ArrayList;

public class Nippou implements Serializable{
    //Firebaseから取得した年
    private int mYear;

    //Firebaseから取得した月
    private int mMonth;

    //日
    private int mDay;

    //ID
    private String mId;

    //システム名
    private String mSysId;
    private String mSystem;

    //取引先
    private String mCusId;
    private String mCustomer;

    //部署
    private String mCusBId;
    private String mCustomerBusyo;

    //プロジェクト名
    private String mProId;
    private String mProjectName;

    //分類
    private String mBunrui;

    //区分
    private String mKubun;

    //実績工数
    private int mJissekikousuu;

    //保守工数
    private int mHosyukousuu;

    //内容
    private String mNaiyou;


    private ArrayList<Nippou> mNippouArrayList;



    public int getYear() {
        return mYear;
    }

    public int getMonth(){
        return mMonth;
    }

    public int getDay() {
        return mDay;
    }

    public String getId() {
        return mId;
    }

    public String getSystemId() {
        return mSysId;
    }

    public String getSystemName() {
        return mSystem;
    }

    public String getCustomerid() {
        return mCusId;
    }

    public String getCustomer() {
        return mCustomer;
    }

    public String getCustomerBusyoId() {
        return mCusBId;
    }

    public String getCustomerBusyoName() {
        return mCustomerBusyo;
    }

    public String getProjectId() {
        return mProId;
    }

    public String getProjectName() {
        return mProjectName;
    }

    public String getSagyoubunrui() {
        return mBunrui;
    }

    public String getSagyoukubun() {
        return mKubun;
    }

    public int getJissekikousuu() {
        return mJissekikousuu;
    }

    public int getHosyukousuu() {
        return mHosyukousuu;
    }

    public String getNaiyou() {
        return mNaiyou;
    }





    public ArrayList<Nippou> getNippou() {
        return mNippouArrayList;
    }


    public Nippou(int year, int month,int day,String id,String systemid,String system,String customerid,String customer,String cbusyoid,String projectid,String project,String bunrui,String sagyoukubun,int jissekikousuu,int  hosyukousuu,String naiyou){
        mYear = year;
        mMonth = month;
        mDay = day;
        mId = id;
        mSysId = systemid;
        mSystem = system;
        mCusId = customerid;
        mCustomer = customer;
        mCusBId = cbusyoid;
        mProId = projectid;
        mProjectName = project;
        mBunrui = bunrui;
        mKubun = sagyoukubun;
        mJissekikousuu = jissekikousuu;
        mHosyukousuu = hosyukousuu;
        mNaiyou = naiyou;






    }
}
