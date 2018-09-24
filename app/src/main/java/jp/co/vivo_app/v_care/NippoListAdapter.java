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

        TextView systemnameText = (TextView) convertView.findViewById(R.id.systemnametextview);
        systemnameText.setText(mNippouArrayList.get(position).getSystemName());

        TextView customername = (TextView) convertView.findViewById(R.id.customernametextView);
        customername.setText(mNippouArrayList.get(position).getCustomer());

        TextView customerBusyoNametext = (TextView) convertView.findViewById(R.id.customerbusyotextView);
        customerBusyoNametext.setText(mNippouArrayList.get(position).getCustomerBusyoName());

        TextView projectText = (TextView) convertView.findViewById(R.id.keikakutextView);
        projectText.setText(mNippouArrayList.get(position).getProjectName());

        TextView bunruiText = (TextView) convertView.findViewById(R.id.bunruitextView);
        bunruiText.setText(mNippouArrayList.get(position).getSagyoubunrui());

        TextView jissekikousuulext = (TextView) convertView.findViewById(R.id.jissekikousuutextView);
        jissekikousuulext.setText(String.valueOf(mNippouArrayList.get(position).getJissekikousuu()));

        TextView hosyukousuuText = (TextView) convertView.findViewById(R.id.hosyukousuutextView);
        hosyukousuuText.setText(String.valueOf(mNippouArrayList.get(position).getHosyukousuu()));

        TextView naiyouText = (TextView) convertView.findViewById(R.id.naiyoutextview);
        naiyouText.setText(mNippouArrayList.get(position).getNaiyou());

        return convertView;
    }


    public void setNippouArrayList(ArrayList<Nippou> nippouArrayList) {
        mNippouArrayList = nippouArrayList;
    }
}
