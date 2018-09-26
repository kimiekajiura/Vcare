package jp.co.vivo_app.v_care;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.sql.Time;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class TimeandAttendance extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,DatabaseReference.CompletionListener{

    private String mId;
    private String mPassword;
    private String mName;
    private String mGroup;
    private boolean mAdminkengen;

    private Button mSyukkinButton;
    private Button mTaikinButton;
    private Button mListopenbutton;
    private Button mBikouopenButton;
    private Button mLastmonthButton;
    private Button mNextmonthButton;

    private ListView mListView;
    private TextView mSyukkinTextview;
    private TextView mTaikinTextView;
    private TextView mTextView;

    private DatabaseReference mAttendanceRef;
    private DatabaseReference mAttendanceListRef;
    private DatabaseReference mSyukkinCHRef;
    DatabaseReference mDatabaseReference;
    private DatabaseReference mAdminRef;
    private DatabaseReference mUserRef;

    private int mState = 0;

    private int mYear;
    private int mMonth;
    private int mDate;
    private int mHour;
    private int mMinute;
    private int mDay;
    private Calendar mGcalendar;
    private String mHourOfDay;
    private String mMinuteDay;

    private String mStime;
    private String mEtime;
    private Date mSdate;
    private Date mEdate;
    private String mSYear;
    private String mMonthofyear;
    private String mDayOfMonth;
    private Date mSDdate;
    private Date mSTdate;
    private Date mSStime;
    private Date mEStime;
    private String mTSYear;
    private String mTMonthofyear;
    private String mTDayOfMonth;
    private String mBikou;

    private int mThisYear;
    private int mThisMonth;
    private int mDiff;

    private String mLid,mLbikou;
    private int mLyear,mLmonth,mLdate;

    private WebView webView;

    private NavigationView mNavigationView;
    private DialogFragment dialogFragment;
    private FragmentManager flagmentManager;
    private BottomNavigationView mBottomavigation;
    private ArrayList<Attendance> mAttendanceArrayList;
    private AttendanceListAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_timeand_attendance);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        webView = new WebView(this);

        Calendar cal = Calendar.getInstance();

        mYear =cal.get(Calendar.YEAR);
        mMonth =cal.get(Calendar.MONTH) + 1;
        mDate =cal.get(Calendar.DATE);
        mHour =cal.get(Calendar.HOUR);
        mMinute = cal.get(Calendar.MINUTE);

        TextView tv = (TextView)findViewById(R.id.nowltextview);
        tv.setText("現在      " + mYear + "/" + mMonth + "/" + mDate + "　　 " + mHour + "：" + mMinute);

        Bundle extras = getIntent().getExtras();
        mId = extras.getString("mId");
        mPassword = extras.getString("mPassword");
        mName = extras.getString("mName");
        mGroup = extras.getString("mGroup");
        mAdminkengen = extras.getBoolean("mAdminkengen");
        mBikou = extras.getString("bikou");

        mBottomavigation  = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        BottomNavigationViewHelper.disableShiftMode(mBottomavigation );

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        if (mId != null){
            mDatabaseReference.child(Const.UserPATH).child(mId).child(String.valueOf(mAdminkengen)).addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Menu menu = mBottomavigation.getMenu();
                            MenuItem menuItem = menu.findItem(R.id.createsyain);
                            if (mAdminkengen == true){
                                menuItem.setEnabled(true);
                            }else {
                                mBottomavigation.getMenu().removeItem(R.id.createsyain);
                            }


                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    }

            );
        }


        mBottomavigation .setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                // 各選択したときの処理
                switch (item.getItemId()) {
                    case R.id.nav_attendance:

                        return true;
                    case R.id.dayreport:
                        Intent intent = new Intent(getApplicationContext(),ChartMain.class);
                        intent.putExtra("mId",mId);
                        intent.putExtra("mPassword",mPassword);
                        intent.putExtra("mName",mName);
                        intent.putExtra("mGroup",mGroup);
                        intent.putExtra("mAdminkengen",mAdminkengen);
                        intent.putExtra("mBikou",mBikou);

                        startActivity(intent);
                        return true;
                    case R.id.calendar:

                        flagmentManager = getFragmentManager();
                        dialogFragment = new Calendershow();

                        Bundle bundle = new Bundle();
                        bundle.putString("mId",mId);
                        bundle.putInt("mYear",mYear);
                        bundle.putInt("mMonth",mMonth);
                        bundle.putInt("mDate",mDate);

                        dialogFragment.setArguments(bundle);
                        dialogFragment.show(flagmentManager,"calendar");
                        return true;

                    case R.id.createsyain:
                            flagmentManager = getFragmentManager();
                            dialogFragment = new AlertDialogshow();
                            dialogFragment.show(flagmentManager,"test");
                }
                return false;
            }
        });

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        TextView textview = (TextView) findViewById(R.id.syukkinview);

        mYear =cal.get(Calendar.YEAR);
        mMonth =cal.get(Calendar.MONTH) + 1;
        mDate =cal.get(Calendar.DATE);
        mHour =cal.get(Calendar.HOUR);
        mMinute = cal.get(Calendar.MINUTE);


        //出勤ボタンタップ処理
        mSyukkinButton = (Button) findViewById(R.id.syukkinbutton);
        mSyukkinButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                DateTimeSet(1);
                new AlertDialog.Builder(TimeandAttendance.this)
                        .setMessage(mYear + "/" + mMonth + "/" + mDate + "   " + mHour + ":" + mMinute + "出勤登録しますか？")
                        .setPositiveButton(
                                "OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
                                        mAttendanceRef = mDatabaseReference.child(Const.AttendancePATH).child(mId).child(String.valueOf(mYear)).child(String.valueOf(mMonth)).child(String.valueOf(mDate));

                                        Map<String, String> data = new HashMap<String, String>();

                                        (mSyukkinTextview = (TextView) findViewById(R.id.syukkinview)).setText(mYear + "/" + mMonth + "/" + mDate + "  " + mHour + ":" + mMinute);

                                        String stime = mHour + ":" + mMinute;

                                        data.put("出勤時刻",String.valueOf(mHour) + ":" +String.valueOf(mMinute));
                                        //data.put("備考",String.valueOf(mBikou));
                                        mAttendanceRef.setValue(data);

                                    }
                                }
                        )
                        .setNegativeButton(
                                "CANCEL",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.cancel();
                                    }
                                }
                        )
                        .setOnCancelListener(new DialogInterface.OnCancelListener() {
                            @Override
                            public void onCancel(DialogInterface dialog) {
                                dialog.cancel();
                            }
                        })
                        .show();
            }
        });

        //退勤ボタンタップ処理
        mTaikinButton = (Button) findViewById(R.id.taikinbutton);
        mTaikinButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                DateTimeSet(2);
                mState = 2;
                mDatabaseReference = FirebaseDatabase.getInstance().getReference();
                mSyukkinCHRef = mDatabaseReference.child(Const.AttendancePATH).child(mId).child(String.valueOf(mYear)).child(String.valueOf(mMonth)).child(String.valueOf(mDate));
                mSyukkinCHRef.addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                HashMap map = (HashMap) dataSnapshot.getValue();
                                if (map == null){
                                    showAlertDialog(1);
                                }else{
                                    new AlertDialog.Builder(TimeandAttendance.this)
                                            .setMessage(mYear + "/" + mMonth + "/" + mDate + "   " + mHour + ":" + mMinute + "退勤登録しますか？")
                                            .setPositiveButton(
                                                    "OK", new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            mDatabaseReference = FirebaseDatabase.getInstance().getReference();
                                                            mDatabaseReference.child(Const.AttendancePATH).child(mId).child(String.valueOf(mYear)).child(String.valueOf(mMonth)).child(String.valueOf(mDate)).addListenerForSingleValueEvent(
                                                                    new ValueEventListener() {
                                                                        @Override
                                                                        public void onDataChange(DataSnapshot dataSnapshot) {

                                                                            HashMap map = (HashMap) dataSnapshot.getValue();

                                                                            Map<String, String> data = new HashMap<>();

                                                                            String stime = (String) map.get("出勤時刻");
                                                                            String bikou = (String) map.get("備考");
                                                                            String bikoutouroku = (String) map.get("備考登録時間");

                                                                            (mTaikinTextView = (TextView) findViewById(R.id.taikinview)).setText(mYear + "/" + mMonth + "/" + mDate + "  " + mHour + ":" + mMinute);

                                                                            data.put("退勤時刻",String.valueOf(mHour) + ":" +String.valueOf(mMinute));
                                                                            data.put("出勤時刻",stime);
                                                                            data.put("備考",bikou);
                                                                            data.put("備考登録時間",bikoutouroku);

                                                                            mSyukkinCHRef.setValue(data);
                                                                            mAttendanceArrayList.clear();

                                                                            //画面更新
                                                                            mDatabaseReference.child(Const.AttendancePATH).child(mId).child(String.valueOf(mYear)).child(String.valueOf(mMonth)).addChildEventListener(
                                                                                    new ChildEventListener() {
                                                                                        @Override
                                                                                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                                                                                            HashMap map = (HashMap) dataSnapshot.getValue();
                                                                                            Map<String,Object> data = new HashMap<>();

                                                                                            mDay = Integer.parseInt(dataSnapshot.getKey().toString());
                                                                                            String stime = (String) map.get("出勤時刻");
                                                                                            String etime = (String) map.get("退勤時刻");
                                                                                            String bikou = (String) map.get("備考");

                                                                                            TextView tv = (TextView)findViewById(R.id.thismonthtextview);
                                                                                            tv.setText(mYear + "/" + mMonth);

                                                                                            Attendance attendance = new Attendance(mYear,mMonth,mDay,stime,etime,mId,bikou);

                                                                                            mThisMonth = mMonth;
                                                                                            mThisYear = mYear;

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

                                                                        }

                                                                        @Override
                                                                        public void onCancelled(DatabaseError databaseError) {

                                                                        }
                                                                    }
                                                            );

                                                        }


                                                    }


                                            )


                                            .setNegativeButton(
                                                    "CANCEL",
                                                    new DialogInterface.OnClickListener() {
                                                        @Override
                                                        public void onClick(DialogInterface dialog, int which) {
                                                            dialog.cancel();
                                                        }
                                                    }

                                            )
                                            .setOnCancelListener(new DialogInterface.OnCancelListener() {
                                                @Override
                                                public void onCancel(DialogInterface dialog) {
                                                    dialog.cancel();
                                                }
                                            })
                                            .show();
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }

                        }

                );

            }
        });

        mAdapter = new AttendanceListAdapter(this);
        mListView = (ListView) findViewById(R.id.listView);
        mAttendanceArrayList= new ArrayList<Attendance>();

        //リストビュータップ
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                mListView = (ListView) findViewById(R.id.listView);
                flagmentManager = getFragmentManager();
                dialogFragment = new BikouEdit();

                mLid =mId;
                mLyear =mAttendanceArrayList.get(position).getYear();
                mLmonth =mAttendanceArrayList.get(position).getMonth();
                mLdate =mAttendanceArrayList.get(position).getDay();
                mLbikou = mAttendanceArrayList.get(position).getBikou();

                Bundle bundle = new Bundle();

                bundle.putString("mId",mId);
                bundle.putInt("mLyear",mLyear);
                bundle.putInt("mLmonth",mLmonth);
                bundle.putInt("mLdate",mLdate);
                bundle.putString("mLbikou",mLbikou);

                dialogFragment.setArguments(bundle);
                dialogFragment.show(flagmentManager,"test");

            }
        });

        //前の月表示
        mLastmonthButton = (Button) findViewById(R.id.lastmonthbutton);
        mLastmonthButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mAttendanceArrayList.clear();
                mListView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                //前月
                mDiff = (mThisMonth) -1;
                if (mDiff == 0){
                    mThisYear = mThisYear - 1;
                    mDiff = 12;
                    mThisMonth = mDiff;
                }else{
                    mThisMonth = mDiff;
                }

                mThisMonth = mDiff;
                TextView tv = (TextView)findViewById(R.id.thismonthtextview);
                tv.setText(mThisYear + "/" + mThisMonth);


                mDatabaseReference.child(Const.AttendancePATH).child(mId).child(String.valueOf(mThisYear)).child(String.valueOf(mDiff)).addChildEventListener(
                        new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                HashMap map = (HashMap) dataSnapshot.getValue();

                                    Map<String, Object> data = new HashMap<>();

                                    mDay = Integer.parseInt(dataSnapshot.getKey().toString());
                                    String stime = (String) map.get("出勤時刻");
                                    String etime = (String) map.get("退勤時刻");
                                    String bikou = (String) map.get("備考");

                                    Attendance attendance = new Attendance(mThisYear,mDiff,mDay,stime,etime,mId,bikou);

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
            }
        });

        //次の月表示
        mNextmonthButton = (Button) findViewById(R.id.nextmonthbutton);
        mNextmonthButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                mAttendanceArrayList.clear();
                mListView.setAdapter(mAdapter);
                mAdapter.notifyDataSetChanged();
                //翌月
                mDiff = (mThisMonth) + 1;

                if (mDiff == 13){
                    mThisYear = mThisYear + 1;
                    mDiff = 1;
                    mThisMonth = mDiff;
                }else{
                    mThisMonth = mDiff;
                }


                TextView tv = (TextView)findViewById(R.id.thismonthtextview);
                tv.setText(mThisYear + "/" + mThisMonth);
                mDatabaseReference.child(Const.AttendancePATH).child(mId).child(String.valueOf(mThisYear)).child(String.valueOf(mDiff)).addChildEventListener(
                        new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                HashMap map = (HashMap) dataSnapshot.getValue();

                                    Map<String, Object> data = new HashMap<>();

                                    mDay = Integer.parseInt(dataSnapshot.getKey().toString());
                                    String stime = (String) map.get("出勤時刻");
                                    String etime = (String) map.get("退勤時刻");
                                    String bikou = (String) map.get("備考");

                                    Attendance attendance = new Attendance(mThisYear,mDiff,mDay,stime,etime,mId,bikou);

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
            }

        });

        mTextView = (TextView)findViewById(R.id.thismonthtextview);
        mTextView.setText(mYear + "/" + mMonth);

        //一覧表示
        mDatabaseReference.child(Const.AttendancePATH).child(mId).child(String.valueOf(mYear)).child(String.valueOf(mMonth)).addChildEventListener(
                new ChildEventListener() {

                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                        HashMap map = (HashMap) dataSnapshot.getValue();
                        Map<String,Object> data = new HashMap<>();

                        mDay = Integer.parseInt(dataSnapshot.getKey().toString());
                        String stime = (String) map.get("出勤時刻");
                        String etime = (String) map.get("退勤時刻");
                        String bikou = (String) map.get("備考");

                        TextView tv = (TextView)findViewById(R.id.thismonthtextview);
                        tv.setText(mYear + "/" + mMonth);

                        //今日の登録があったら、表示
                        if (mDay == mDate){
                            if (stime != null) {
                                (mSyukkinTextview = (TextView) findViewById(R.id.syukkinview)).setText(mYear + "/" + mMonth + "/" + mDate + "  "+ stime);
                                mSyukkinButton.setEnabled(false);
                            }
                            if (etime != null){
                                (mTaikinTextView = (TextView) findViewById(R.id.taikinview)).setText(mYear + "/" + mMonth + "/" + mDate + "  " + etime);
                                mTaikinButton.setEnabled(false);
                            }
                        }else {

                        }

                        Attendance attendance = new Attendance(mYear,mMonth,mDay,stime,etime,mId,bikou);

                        mThisMonth = mMonth;
                        mThisYear = mYear;

                        mAttendanceArrayList.add(attendance);
                        mAdapter.setAttendanceArrayList(mAttendanceArrayList);
                        mListView.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        HashMap map = (HashMap) dataSnapshot.getValue();
                        for (Attendance attendance : mAttendanceArrayList){
                            if (String.valueOf(dataSnapshot.getKey()).equals(String.valueOf(attendance.getDay()))) {
                                String bikou = (String) map.get("備考");
                                attendance.setBikou(bikou);
                                mAdapter.notifyDataSetChanged();
                            }
                        }
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

        /*
        //勤怠一覧表を開く
        mListopenbutton = (Button) findViewById(R.id.listopenbutton);
        mListopenbutton.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        flagmentManager = getFragmentManager();
                        dialogFragment = new AttendanceListDialogshow();
                        dialogFragment.show(flagmentManager,"test");

                    }
                }
        );
        */

    }


    private void showAlertDialog(int z) {
        if (z == 1){
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("次の画面で先に出勤登録をしてください。");
            alertDialogBuilder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            showDatePickerDialog(1);
                        }
                    });
            android.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            return;
        }else if (z == 2){
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("未来の登録はできません。");
            alertDialogBuilder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            showTimePickerDialog(1);
                        }
                    });
            android.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            return;
        }else if (z == 3){
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("未来の登録はできません。");
            alertDialogBuilder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            showDatePickerDialog(1);
                        }
                    });
            android.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            return;
        }else if (z == 4){
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("今の日時を退勤に登録しますか？");
            alertDialogBuilder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            DateTimeSet(2);
                            mState = 2;
                            mDatabaseReference = FirebaseDatabase.getInstance().getReference();
                            mDatabaseReference.child(Const.AttendancePATH).child(mId).child(String.valueOf(mYear)).child(String.valueOf(mMonth)).child(String.valueOf(mDate)).child(Const.StateKey).addListenerForSingleValueEvent(
                                    new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            HashMap map = (HashMap) dataSnapshot.getValue();

                                            if (map == null){
                                                mDatabaseReference = FirebaseDatabase.getInstance().getReference();
                                                mAttendanceRef = mDatabaseReference.child(Const.AttendancePATH).child(mId).child(String.valueOf(mYear)).child(String.valueOf(mMonth)).child(String.valueOf(mDate));

                                                Map<String, String> data = new HashMap<String, String>();

                                                (mTaikinTextView = (TextView) findViewById(R.id.taikinview)).setText(mYear + "/" + mMonth + "/" + mDate + "  " + mHour + ":" + mMinute);

                                                //data.put("退勤",String.valueOf(mState));
                                                data.put("退勤時刻",String.valueOf(mHour) + ":" +String.valueOf(mMinute));
                                                data.put("備考",String.valueOf(mBikou));
                                                mAttendanceRef.setValue(data);
                                                mTaikinButton.setEnabled(false);

                                            }else{
                                                Map<String, String> data = new HashMap<>();

                                                String stime = (String) map.get("出勤時刻");

                                                (mTaikinTextView = (TextView) findViewById(R.id.taikinview)).setText(mYear + "/" + mMonth + "/" + mDate + "  " + mHour + ":" + mMinute);

                                                data.put("退勤時刻",String.valueOf(mHour) + ":" +String.valueOf(mMinute));
                                                data.put("出勤時刻",stime);
                                                data.put("備考",String.valueOf(mBikou));
                                                mAttendanceRef.setValue(data);
                                                mTaikinButton.setEnabled(false);
                                            }


                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    }
                            );
                        }
                    });
            android.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            return;
        }else if (z == 5){
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("出勤日より前には登録できません。");
            alertDialogBuilder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            showDatePickerDialog(2);
                        }
                    });
            android.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            return;
        }else if (z == 6){
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("現在よりの後の出勤登録はできません。");
            alertDialogBuilder.setPositiveButton("OK",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            showTimePickerDialog(1);
                        }
                    });
            android.app.AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            return;
        }else if (z == 7){
            android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
            alertDialogBuilder.setTitle("データはありません。");
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

    private void showDatePickerDialog(int m){
        if (m == 1){
            DatePickerDialog dataPickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            mSYear = String.valueOf(year);
                            mMonthofyear = String.valueOf(month + 1);
                            mDayOfMonth = String.valueOf(dayOfMonth);
                            try{
                                String ecdate = mSYear + "/" + mMonthofyear + "/" + mDayOfMonth;
                                String scdate = mYear + "/" + mMonth + "/" + mDate;

                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                                mSDdate = sdf.parse(ecdate);
                                mSTdate = sdf.parse(scdate);

                            }catch (ParseException e){

                            }
                            if (mSDdate.after(mSTdate)){
                                showAlertDialog(3);
                            }else{
                                showTimePickerDialog(1);
                            }

                        }
                    },mYear,mMonth,mDate);
            dataPickerDialog.show();
            //手動で出勤登録後、続けて退勤登録。出勤日より先にならないように。
        }else if (m == 2){
            DatePickerDialog datepickerDialog = new DatePickerDialog(this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                            mTSYear = String.valueOf(year);
                            mTMonthofyear = String.valueOf(month + 1);
                            mTDayOfMonth = String.valueOf(dayOfMonth);
                            try{
                                String ecdate = mSYear + "/" + mMonthofyear + "/" + mDayOfMonth;
                                String scdate = mTSYear + "/" + mTMonthofyear + "/" + mTDayOfMonth;

                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                                mSDdate = sdf.parse(ecdate);
                                mSTdate = sdf.parse(scdate);

                            }catch (ParseException e){

                            }
                            if (mSDdate.after(mSTdate)){
                                showAlertDialog(5);
                            }else{
                                showTimePickerDialog(2);
                            }
                        }
                    },mYear,mMonth,mDate);
            datepickerDialog.show();
        }

    }

    private void showTimePickerDialog(int n){
        //手動で出勤登録
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    new TimePickerDialog.OnTimeSetListener() {
                        @Override
                        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                            mHourOfDay=String.valueOf(hourOfDay);
                            mMinuteDay=String.valueOf(minute);
                            DateTimeSet(1);
                            mState = 1;
                            try{
                                String scdate = mSYear + "/" + mMonthofyear + "/" + mDayOfMonth;
                                String ecdate = mYear + "/" + mMonth + "/" + mDate;

                                String sctime = mHourOfDay + ":" + mMinuteDay;
                                String ectime = mHour + ":" + mMinute;

                                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                                mSdate = sdf.parse(scdate);
                                mEdate = sdf.parse(ecdate);

                                SimpleDateFormat sdft = new SimpleDateFormat("hh:ss");
                                mSStime = sdft.parse(sctime);
                                mEStime = sdft.parse(ectime);
                            }catch(ParseException e) {

                            }
                            //選択日がきょうと同じか
                            if (mSdate.equals(mEdate)){
                                //同じなら、時間に不正はないか
                                if (mSStime.after(mEStime)){
                                    showAlertDialog(6);
                                }else{
                                    SyukkinTouroku();
                                }
                            }else{
                                SyukkinTouroku();
                            }

                        }
                    },8,30,true);
            timePickerDialog.setTitle("出勤時間登録");
            timePickerDialog.show();


    }


    private void SyukkinTouroku() {
        new AlertDialog.Builder(TimeandAttendance.this)
                .setMessage(mSYear + "/" + mMonthofyear + "/" + mDayOfMonth + "   " + mHourOfDay + ":" + mMinuteDay + "出勤登録しますか？")
                .setPositiveButton(
                        "OK", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mDatabaseReference = FirebaseDatabase.getInstance().getReference();
                                mAttendanceRef = mDatabaseReference.child(Const.AttendancePATH).child(mId).child(String.valueOf(mSYear)).child(String.valueOf(mMonthofyear)).child(String.valueOf(mDayOfMonth));

                                Map<String, String> data = new HashMap<String, String>();

                                (mSyukkinTextview = (TextView) findViewById(R.id.syukkinview)).setText(mSYear + "/" + mMonthofyear + "/" + mDayOfMonth + "  " + mHourOfDay + ":" + mMinuteDay);

                                data.put("出勤時刻",String.valueOf(mHourOfDay) + ":" +String.valueOf(mMinuteDay));
                                data.put("備考",String.valueOf(mBikou));

                                mAttendanceRef.setValue(data);
                                showAlertDialog(4);
                                mSyukkinButton.setEnabled(false);
                                return;
                            }
                        }
                )
                .setNegativeButton(
                        "CANCEL",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        }
                )
                .setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        dialog.cancel();
                    }
                })
                .show();
    }

    private void DateTimeSet(int i) {
        Calendar cal = Calendar.getInstance();

        mYear =cal.get(Calendar.YEAR);
        mMonth =cal.get(Calendar.MONTH) + 1;
        mDate =cal.get(Calendar.DATE);
        mHour =cal.get(Calendar.HOUR);
        mMinute = cal.get(Calendar.MINUTE);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.timeand_attendance, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
        if (databaseError == null) {

        }else {

        }

    }
//    public boolean onKeyDown(int keyCode,KeyEvent event){
//        Intent intent = new Intent(getApplicationContext(),MainActivity.class);
//        intent.putExtra("mId",mId);
//        intent.putExtra("mPassword",mPassword);
//        intent.putExtra("mName",mName);
//        intent.putExtra("mGroup",mGroup);
//        intent.putExtra("mAdminkengen",mAdminkengen);
//        intent.putExtra("mBikou",mBikou);
//        startActivity(intent);
//        finish();
//        return false;
//    }


/*
    public void listset(){
        //mListView = (ListView) findViewById(R.id.listView);
        mAttendanceArrayList= new ArrayList<Attendance>();
        mAdapter = new AttendanceListAdapter(this);
        mAdapter.setAttendanceArrayList(mAttendanceArrayList);
        mAdapter.notifyDataSetChanged();


        Calendar cal = Calendar.getInstance();
        mYear =cal.get(Calendar.YEAR);
        mMonth =cal.get(Calendar.MONTH);
        mDate =cal.get(Calendar.DATE);

        //this.mId=mId;
        mId = "0001";
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mDatabaseReference.child(Const.AttendancePATH).child(mId).child(String.valueOf(mYear)).child(String.valueOf(mMonth)).addChildEventListener(
                new ChildEventListener() {

                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        //View view = getLayoutInflater().inflate(R.layout.content_timeand_attendance, null);

                        HashMap map = (HashMap) dataSnapshot.getValue();
                        Map<String,Object> data = new HashMap<>();

                        mDay = Integer.parseInt(dataSnapshot.getKey().toString());
                        String stime = (String) map.get("出勤時刻");
                        String etime = (String) map.get("退勤時刻");
                        String bikou = (String) map.get("備考");


                        TextView tv = (TextView)findViewById(R.id.thismonthtextview);
                        tv.setText(mYear + "/" + mMonth);

                        //今日の登録があったら、表示
                        if (mDay == mDate){
                            if (stime != null) {
                                (mSyukkinTextview = (TextView) findViewById(R.id.syukkinview)).setText(mYear + "/" + mMonth + "/" + mDate + "  "+ stime);
                                mSyukkinButton.setEnabled(false);
                            }
                            if (etime != null){
                                (mTaikinTextView = (TextView) findViewById(R.id.taikinview)).setText(mYear + "/" + mMonth + "/" + mDate + "  " + etime);
                                mTaikinButton.setEnabled(false);
                            }
                        }else {

                        }

                        Attendance attendance = new Attendance(mYear,mMonth,mDay,stime,etime,mId,bikou);

                        mThisMonth = mMonth;
                        mThisYear = mYear;


                        mAdapter = new AttendanceListAdapter(getParent());
                        mListView = (ListView) findViewById(R.id.listView);
                        mAttendanceArrayList= new ArrayList<Attendance>();

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


    }
*/


}
