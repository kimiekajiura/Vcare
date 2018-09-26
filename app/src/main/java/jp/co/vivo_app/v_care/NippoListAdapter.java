package jp.co.vivo_app.v_care;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class NippoListAdapter extends BaseAdapter{

    private LayoutInflater mLayoutInflater = null;
    private ArrayList<Nippou> mNippouArrayList;

    public NippoListAdapter(Context context){
        mLayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public int getCount() {
        return mNippouArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return mNippouArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.list_nippou,parent,false);
        }

        TextView projectText = (TextView) convertView.findViewById(R.id.projecttextView);
        projectText.setText(mNippouArrayList.get(position).getProjectName());


        TextView jissekikousuulext = (TextView) convertView.findViewById(R.id.jissekikousuutextView);
        jissekikousuulext.setText(String.valueOf(mNippouArrayList.get(position).getJissekikousuu()));

        return convertView;
    }


    public void setNippouArrayList(ArrayList<Nippou> nippouArrayList) {
        mNippouArrayList = nippouArrayList;
    }
}
