package jp.co.vivo_app.v_care;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

public class CalenderListAdapter extends BaseAdapter{

    private LayoutInflater mLayoutInflater = null;
    private ArrayList<Calender> mCalenderArrayList;
    private Calender mCalender;

    public CalenderListAdapter(Context context){
        mLayoutInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return mCalenderArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return mCalenderArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = mLayoutInflater.inflate(R.layout.list_calender,parent,false);
        }

        TextView timeText = (TextView) convertView.findViewById(R.id.timetextView);
        timeText.setText(String.valueOf(String.valueOf(mCalenderArrayList.get(position).getStime()) + "～" +String.valueOf(mCalenderArrayList.get(position).getEtime())));

        TextView titleText = (TextView) convertView.findViewById(R.id.titletextView);
        titleText.setText(mCalenderArrayList.get(position).getTitle());

        TextView detailext = (TextView) convertView.findViewById(R.id.detailtextView);
        detailext.setText(mCalenderArrayList.get(position).getDetail());

        return convertView;
    }

    public void setCalenderArrayList(ArrayList<Calender> calenderArrayList) {
        mCalenderArrayList = calenderArrayList;
    }
}
