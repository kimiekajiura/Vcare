package jp.co.vivo_app.v_care;

import java.io.Serializable;
import java.util.ArrayList;

public class Syain implements Serializable{
    //Firebaseから取得した管理者権限
    private boolean mAdminkengen;

    //Firebaseから取得したグループ
    private String mGroup;

    //Firebaseから取得した氏名
    private String mName;

    //Firebaseから取得したパスワード
    private String mPassword;

    //ID
    private String mId;

    private ArrayList<Syain> mSyainArrayList;



    public boolean getAdminkengen(){
        return mAdminkengen;
    }

    public String getGroup(){
        return mGroup;
    }

    public String getName() {
        return mName;
    }

    public String getPassword() {
        return mPassword;
    }

    public String getid(){
        return mId;
    }

    public Syain(String userId,boolean adminkengen,String group,String name,String password){
        mAdminkengen = adminkengen;
        mGroup = group;
        mName = name;
        mPassword = password;
        mId = userId;
    }
}
