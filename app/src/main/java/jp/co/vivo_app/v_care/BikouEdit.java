package jp.co.vivo_app.v_care;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class BikouEdit extends DialogFragment {

    private int mYear;
    private int mMonth;
    private int mDate;

    private int mDay;
    private int mThisMonth;
    private int mThisYear;

    private int mLyear;
    private int mLmonth;
    private int mLdate;

    private int mCyear;
    private int mCmonth;
    private int mCdate;
    private int mChour;
    private int mCminute;

    private String mId;

    ArrayAdapter<String> adapter;
    private ArrayList<Attendance> mAttendanceArrayList;
    private AttendanceListAdapter mAdapter;
    private ArrayList<Syain> mSyainArrayList;
    DatabaseReference mDatabaseReference;
    private DatabaseReference mUserRef;
    private DatabaseReference mAttendanceRef;

    ArrayAdapter<String> yadapter;
    ArrayAdapter<String> madapter;
    ArrayAdapter<String> dadapter;

    private Spinner mYearspinner;
    private Spinner mMonthspinner;
    private Spinner mDayspinner;
    private Spinner mSyaincodespinner;
    private Spinner mIdSpinner;

    private Button mTourokuButton;

    private TextView mBikouTextView;

    private AlertDialog.Builder alert;
    private ListView mListView;

    EditText bikouedittext;


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
            adapter.add(mId);
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

        alert = new AlertDialog.Builder(getActivity());
        Bundle bundle = getArguments();

        mId = bundle.getString("mId");
        mYear = bundle.getInt("mLyear");
        mMonth = bundle.getInt("mLmonth");
        mDate = bundle.getInt("mLdate");

        Calendar cal = Calendar.getInstance();

        mCyear =cal.get(Calendar.YEAR);
        mCmonth =cal.get(Calendar.MONTH);
        mCdate =cal.get(Calendar.DATE);
        mChour =cal.get(Calendar.HOUR);
        mCminute = cal.get(Calendar.MINUTE);

        View view = getActivity().getLayoutInflater().inflate(R.layout.bikou_edit, null);

        mSyaincodespinner = (Spinner) view.findViewById(R.id.syaincodespinner);
        mYearspinner = (Spinner) view.findViewById(R.id.yearspinner);
        mMonthspinner = (Spinner) view.findViewById(R.id.monthspinner);
        mDayspinner = (Spinner) view.findViewById(R.id.dayspinner);

        //年スピナー
        yadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item);
        yadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner yspinner = (Spinner) view.findViewById(R.id.yearspinner);
        yspinner.setAdapter(adapter);

        //月スピナー
        madapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item);
        madapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner mspinner = (Spinner) view.findViewById(R.id.monthspinner);
        mspinner.setAdapter(adapter);

        dadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item);
        dadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner dspinner = (Spinner) view.findViewById(R.id.dayspinner);
        dspinner.setAdapter(adapter);


        //return;
        for(int i = mYear - 1; i <= mYear; i++){
            yadapter.add(String.valueOf(i));
        }
        for(int i = 1; i <= 12; i++){
            madapter.add(String.valueOf(i));
        }

        for(int i = 1; i <= 31; i++){
            dadapter.add(String.valueOf(i));
        }

        //アダプターをセットする
        yspinner.setAdapter(yadapter);
        mspinner.setAdapter(madapter);
        dspinner.setAdapter(dadapter);

        //スピナーの初期値を現在の日付にする
        yspinner.setSelection(1);
        mspinner.setSelection(mMonth-1);
        dspinner.setSelection(mDate-1);

        mSyainArrayList = new ArrayList<Syain>();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = (Spinner) view.findViewById(R.id.syaincodespinner);
        spinner.setAdapter(adapter);

        mUserRef = mDatabaseReference.child(Const.UserPATH);
        mUserRef.addChildEventListener(mUserListener);
        //return;

        spinner.setPrompt("社員CODE");
        TextView textView = (TextView) view.findViewById(R.id.syainnametextview);
        textView.setText("");

        /*
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mIdSpinner = (Spinner) view.findViewById(R.id.syaincodespinner);
                String item = (String) mIdSpinner.getSelectedItem();

                mDatabaseReference.child(Const.UserPATH).child(item).addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                HashMap map = (HashMap)dataSnapshot.getValue();

                                boolean adminkengen = (boolean) map.get("adminkengen");
                                String group = (String) map.get("group");
                                String name = (String) map.get("name");
                                String password = (String) map.get("password");

                                View view = getActivity().getLayoutInflater().inflate(R.layout.bikou_edit, null);
                                TextView textView = view.findViewById(R.id.syainnametextview);
                                textView.setText(name);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        }
                );
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        */


        /*

        mSyainArrayList = new ArrayList<Syain>();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        //社員スピナー
        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner spinner = (Spinner) view.findViewById(R.id.syaincodespinner);
        spinner.setAdapter(adapter);
        mUserRef = mDatabaseReference.child(Const.UserPATH);
        mUserRef.addChildEventListener(mUserListener);


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mIdSpinner = (Spinner) view.findViewById(R.id.syaincodespinner);
                String item = (String) mIdSpinner.getSelectedItem();

                mDatabaseReference.child(Const.UserPATH).child(item).addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                View view = getActivity().getLayoutInflater().inflate(R.layout.bikou_edit, null);
                                HashMap map = (HashMap)dataSnapshot.getValue();

                                boolean adminkengen = (boolean) map.get("adminkengen");
                                String group = (String) map.get("group");
                                String name = (String) map.get("name");
                                String password = (String) map.get("password");

                                TextView textView = view.findViewById(R.id.syainnametextview);
                                textView.setText(name);
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        }
                );
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        */



        mBikouTextView = (EditText) view.findViewById(R.id.bikouedittext);
        mTourokuButton = (Button) view.findViewById(R.id.exbutton);
        mTourokuButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mId = (String) mSyaincodespinner.getSelectedItem();
                mYear = Integer.parseInt(mYearspinner.getSelectedItem().toString());
                mMonth = Integer.parseInt(mMonthspinner.getSelectedItem().toString());
                mDate=Integer.parseInt(mDayspinner.getSelectedItem().toString());

                //String bikou = mBikouTextView.getText().toString();

                mDatabaseReference = FirebaseDatabase.getInstance().getReference();
                mAttendanceRef = mDatabaseReference.child(Const.AttendancePATH).child(mId).child(String.valueOf(mYear)).child(String.valueOf(mMonth)).child(String.valueOf(mDate));
                mAttendanceRef.addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String bikou = mBikouTextView.getText().toString();

                                HashMap map = (HashMap) dataSnapshot.getValue();
                                Map<String, String> data = new HashMap<>();

                                if (map != null){
                                    String stime = (String) map.get("出勤時刻");
                                    String etime = (String) map.get("退勤時刻");

                                    data.put("退勤時刻",etime);
                                    data.put("出勤時刻",stime);
                                    data.put("備考",bikou);
                                    data.put("備考登録時間",mCyear + "/" + mCmonth + "/" + mCdate + "  " + mChour + ":" + mCminute);

                                    mAttendanceRef.setValue(data);
                                    mAttendanceArrayList= new ArrayList<Attendance>();
                                    mAttendanceArrayList.clear();

                                    //画面更新
                                    mDatabaseReference.child(Const.AttendancePATH).child(mId).child(String.valueOf(mYear)).child(String.valueOf(mMonth)).addChildEventListener(
                                            new ChildEventListener() {
                                                @Override
                                                public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                                                    View view = getActivity().getLayoutInflater().inflate(R.layout.content_timeand_attendance, null);

                                                    HashMap map = (HashMap) dataSnapshot.getValue();
                                                    Map<String,Object> data = new HashMap<>();

                                                    mDay = Integer.parseInt(dataSnapshot.getKey().toString());
                                                    String stime = (String) map.get("出勤時刻");
                                                    String etime = (String) map.get("退勤時刻");
                                                    String bikou = (String) map.get("備考");

                                                    Attendance attendance = new Attendance(mYear,mMonth,mDay,stime,etime,mId,bikou);

                                                    mAttendanceArrayList= new ArrayList<Attendance>();
                                                    mAdapter = new AttendanceListAdapter(getActivity());
                                                    mListView = (ListView) view.findViewById(R.id.listView);

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

                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(getActivity());
                                    alertDialogBuilder.setTitle("登録しました。");
                                    alertDialogBuilder.setPositiveButton("OK",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dismiss();
                                                    //Bundle bundle = new Bundle();
                                                    //bundle.putString("mId",mId);

                                                    //TimeandAttendance timeandAttendance = new TimeandAttendance();
                                                    //timeandAttendance.onResume();

                                                }
                                            });
                                    android.app.AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                                    return;
                                }else {
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(getActivity());
                                    alertDialogBuilder.setTitle("出退勤の登録はありません。");
                                    alertDialogBuilder.setPositiveButton("OK",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                }
                                            });
                                    android.app.AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                                    return;
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }

                        }

                );

            }
        });

        return new android.app.AlertDialog.Builder(getActivity(),R.style.MyAlertDialogTheme)
                .setView(view)
                .setTitle("備考登録")
                .show();

    }

    public BikouEdit(){

    }

}
