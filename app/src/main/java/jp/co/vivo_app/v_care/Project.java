package jp.co.vivo_app.v_care;

import java.io.Serializable;
import java.util.ArrayList;

public class Project implements Serializable{
    private String mId;

    private String mName;

    private String mCustomerId;

    public ArrayList<Project> mProjectArrayList;

    public String getProjectId(){
        return mId;
    }

    public String getName(){
        return mName;
    }


    public String getCustomerId() {
        return mCustomerId;
    }


    public Project(String projectId,String name,String customerId) {
        mId = projectId;
        mName = name;
        mCustomerId = customerId;
    }
}
