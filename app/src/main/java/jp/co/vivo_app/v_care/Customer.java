package jp.co.vivo_app.v_care;

import java.io.Serializable;
import java.util.ArrayList;

public class Customer implements Serializable {
    private String mId;

    private String mName;

    public ArrayList<Customer> mCustomerArrayList;

    public ArrayList<Busyo> mBusyoArrayList;



    public String getId(){
        return mId;
    }

    public String getName(){
        return mName;
    }


    public ArrayList<Busyo> getBusyo() {
        return mBusyoArrayList;
    }


    public Customer(String id,String name) {
        mId = id;
        mName = name;

    }
}
