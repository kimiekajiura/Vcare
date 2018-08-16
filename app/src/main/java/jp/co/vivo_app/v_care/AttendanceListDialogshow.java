package jp.co.vivo_app.v_care;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class
AttendanceListDialogshow extends DialogFragment {

    private ListView mListView;
    private AttendanceListAdapter mAdapter;
    private ArrayList<Attendance> mAttendanceArrayList;
    private ArrayList<Syain> mSyainArrayList;
    DatabaseReference mDatabaseReference;
    private DatabaseReference mAttendanceRef;
    private DatabaseReference mUserRef;

    private Attendance mAttendance;

    private FragmentManager flagmentManager;
    private DialogFragment dialogFragment;

    private Button mChoiceButton;
    private Spinner mSyaincodespinner;
    private Spinner mYearspinner;
    private Spinner mMonthspinner;
    private Spinner mDayspinner;
    private Spinner mIdSpinner;
    private TextView mDateText;
    private TextView mStimeText;
    private TextView mEtimeText;
    private TextView mBikou;

    private int mYear;
    private int mMonth;
    private int mDate;
    private int mHour;
    private int mMinute;
    private int mDay;
    private String mStime;
    private String mEtime;
    private String mId;

    ArrayAdapter<String> adapter;
    ArrayAdapter<String> yadapter;
    ArrayAdapter<String> madapter;
    ArrayAdapter<String> dadapter;

    //社員spinnaer設定用
    ChildEventListener mUserListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

            HashMap map = (HashMap) dataSnapshot.getValue();
            String userId = dataSnapshot.getKey();

            boolean adminkengen = (boolean) map.get("adminkengen");
            String group = (String) map.get("group");
            String name = (String) map.get("name");
            String password = (String) map.get("password");

            Syain syain = new Syain(userId,adminkengen,group,name,password);
            mSyainArrayList.add(syain);
            adapter.add(userId);
            adapter.notifyDataSetChanged();

        }

        @Override
        public void onChildChanged(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onChildRemoved(DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(DataSnapshot dataSnapshot, String s) {

        }

        @Override
        public void onCancelled(DatabaseError databaseError) {

        }

    };

    @Override
    public Dialog onCreateDialog (Bundle savedInstanceState) {

        Calendar cal = Calendar.getInstance();

        mYear =cal.get(Calendar.YEAR);
        mMonth =cal.get(Calendar.MONTH);
        mDate =cal.get(Calendar.DATE);

        View view = getActivity().getLayoutInflater().inflate(R.layout.attendance_dialoglist, null);

        mSyainArrayList = new ArrayList<Syain>();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        //社員スピナー
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = (Spinner) view.findViewById(R.id.syaincodespinner);
        spinner.setAdapter(adapter);
        mUserRef = mDatabaseReference.child(Const.UserPATH);
        mUserRef.addChildEventListener(mUserListener);

        //年スピナー
        yadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item);
        yadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner yspinner = (Spinner) view.findViewById(R.id.yearspinner);
        spinner.setAdapter(adapter);

        //月スピナー
        madapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item);
        madapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner mspinner = (Spinner) view.findViewById(R.id.monthspinner);
        spinner.setAdapter(adapter);


        //return;
        for(int i = mYear - 1; i <= mYear; i++){
            yadapter.add(String.valueOf(i));
        }
        for(int i = 1; i <= 12; i++){
            madapter.add(String.valueOf(i));
        }

        //アダプターをセットする
        yspinner.setAdapter(yadapter);
        mspinner.setAdapter(madapter);

        //スピナーの初期値を現在の日付にする
        yspinner.setSelection(1);
        mspinner.setSelection(mMonth-1);

        TextView textView = (TextView) view.findViewById(R.id.syainnametextview);
        textView.setText("");

        mSyaincodespinner = (Spinner) view.findViewById(R.id.syaincodespinner);
        mYearspinner = (Spinner) view.findViewById(R.id.yearspinner);
        mMonthspinner = (Spinner) view.findViewById(R.id.monthspinner);

        mStimeText = (TextView) view.findViewById(R.id.stimetextView);
        mEtimeText = (TextView) view.findViewById(R.id.etimetextView);
        mBikou = (TextView) view.findViewById(R.id.bikouTextView);
        mDateText = (TextView) view.findViewById(R.id.datetextView);

        mAttendanceArrayList = new ArrayList<Attendance>();

        mAdapter = new AttendanceListAdapter(getActivity());
        mListView = (ListView) view.findViewById(R.id.listView);
        mAttendanceArrayList= new ArrayList<Attendance>();

        //表示ボタンで一覧表示
        mChoiceButton = (Button) view.findViewById(R.id.choicebutton);
        mChoiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAttendanceArrayList.clear();

                mId = (String) mSyaincodespinner.getSelectedItem();
                mYear = Integer.parseInt(mYearspinner.getSelectedItem().toString());
                mMonth = Integer.parseInt(mMonthspinner.getSelectedItem().toString());

                mDatabaseReference = FirebaseDatabase.getInstance().getReference();
                mDatabaseReference.child(Const.AttendancePATH).child(mId).child(String.valueOf(mYear)).child(String.valueOf(mMonth)).addChildEventListener(
                        new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                HashMap map = (HashMap) dataSnapshot.getValue();
                                Map<String,Object> data = new HashMap<>();

                                mDay = Integer.parseInt(dataSnapshot.getKey().toString());
                                String stime = (String) map.get("出勤時刻");
                                String etime = (String) map.get("退勤時刻");
                                mId = (String) mSyaincodespinner.getSelectedItem();
                                String bikou = (String) map.get("備考");

                                Attendance attendance = new Attendance(mYear,mMonth,mDay,stime,etime,mId,bikou);

                                mAttendanceArrayList.add(attendance);
                                mAdapter.setAttendanceArrayList(mAttendanceArrayList);
                                mListView.setAdapter(mAdapter);
                                mAdapter.notifyDataSetChanged();


                            }

                            @Override
                            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onChildRemoved(DataSnapshot dataSnapshot) {

                            }

                            @Override
                            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        }

                );
                mAttendanceArrayList.clear();
                mAdapter.notifyDataSetChanged();
            }


        });


        /*
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                flagmentManager = getFragmentManager();
                dialogFragment = new BikouEdit(mId,mYear,mMonth,mDate);
                dialogFragment.show(flagmentManager,"test");
            }
        });
        */

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        return new android.app.AlertDialog.Builder(getActivity(),R.style.MyAlertDialogTheme)
                .setView(view)
                .setTitle("出退勤一覧")
                .show();


    }

    public static class ViewPagerAdapter extends FragmentStatePagerAdapter {

        CharSequence titles[];
        int numbOfTabs;

        public ViewPagerAdapter(android.support.v4.app.FragmentManager fm, CharSequence titles[], int mNumbOfTabs) {
            super(fm);
            this.titles = titles;
            this.numbOfTabs = mNumbOfTabs;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                CompactCalendarTab compactCalendarTab = new CompactCalendarTab();
                return compactCalendarTab;
            } else {
                Tab2 tab2 = new Tab2();
                return tab2;
            }
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @Override
        public int getCount() {
            return numbOfTabs;
        }
    }
}
