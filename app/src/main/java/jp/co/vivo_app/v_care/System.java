package jp.co.vivo_app.v_care;

import java.io.Serializable;
import java.util.ArrayList;

public class System implements Serializable{
    private String mId;

    private String mName;

    private String mCustomerId;

    public ArrayList<System> mSystemArrayList;

    public String getSystemId(){
        return mId;
    }

    public String getName(){
        return mName;
    }


    public String getCustomerId() {
        return mCustomerId;
    }


    public System(String systemId,String name,String customerId) {
        mId = systemId;
        mName = name;
        mCustomerId = customerId;
    }
}
