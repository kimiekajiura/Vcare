package jp.co.vivo_app.v_care;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class AttendanceListAdapter extends BaseAdapter {

    private LayoutInflater mLayoutInflater;
    private ArrayList<Attendance> mAttendanceArrayList;
    private Attendance mAttendance;

    public AttendanceListAdapter(Context context){
        mLayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mAttendanceArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return mAttendanceArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.list_attendance,parent,false);
        }

        TextView dateText = (TextView) convertView.findViewById(R.id.datetextView);
        dateText.setText(String.valueOf(mAttendanceArrayList.get(position).getYear()) + "/" + String.valueOf(mAttendanceArrayList.get(position).getMonth()) + "/" +String.valueOf(mAttendanceArrayList.get(position).getDay()));

        TextView stimeText = (TextView) convertView.findViewById(R.id.stimetextView);
        stimeText.setText(mAttendanceArrayList.get(position).getStime());

        TextView etimeText = (TextView) convertView.findViewById(R.id.etimetextView);
        etimeText.setText(mAttendanceArrayList.get(position).getEtime());

        TextView bikouText = (TextView) convertView.findViewById(R.id.bikoutextView);
        bikouText.setText(mAttendanceArrayList.get(position).getBikou());

        return convertView;
    }

    public void setAttendanceArrayList(ArrayList<Attendance> attendanceArrayList){
        mAttendanceArrayList = attendanceArrayList;
    }


}
