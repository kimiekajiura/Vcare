package jp.co.vivo_app.v_care;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.TimePicker;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import static android.content.Context.ALARM_SERVICE;

public class CalenderEdit extends DialogFragment {

    private int mYear;
    private int mMonth;
    private int mDate;
    private String mId;
    private String mStime;
    private String mEtime;
    private String mTitle;
    private String mDetail;
    private String mYTdate;

    private Date mSDdate;

    private int mSYear,mMonthofyear,mDayOfMonth;

    private Date mstime,metime;

    private int mCyear;
    private int mCmonth;
    private int mCdate;
    private int mChour;
    private int mCminute;

    private String mYID;
    private int data;

    private AlertDialog.Builder alert;

    ArrayAdapter<String> yadapter;
    ArrayAdapter<String> madapter;
    ArrayAdapter<String> dadapter;

    private Spinner mYearspinner;
    private Spinner mMonthspinner;
    private Spinner mDayspinner;
    private Spinner mSyaincodespinner;
    private Spinner mIdSpinner;
    private Spinner mStimespinner;
    private Spinner mEtimespinner;

    private Toolbar mToolbar;

    private EditText mYoteiEditText;
    private EditText mTitleEditText;

    private Button mCreateButton;
    private Button mDeleteButton;

    private CheckBox mAlermCh;

    ArrayAdapter<String> adapter;
    private ArrayList<Syain> mSyainArrayList;
    private ArrayList<Calender> mCalenderArrayList;

    DatabaseReference mDatabaseReference;

    private DatabaseReference mCalenderRef;
    private CalenderListAdapter mAdapter;
    private ListView mListView;

    private int mAEFlg;

    private String mHourOfDay;
    private String mMinuteDay;
    private Date mSdate;

    private boolean mAlertch;
    private String mEcdate;

    private DialogFragment dialogFragment;
    private FragmentManager flagmentManager;

    private int mCYear, mCMonth, mCDay, mCHour, mCMinute;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View newDialog = getActivity().getLayoutInflater().inflate(R.layout.calender_edit, null);

        mListView = (ListView) newDialog.findViewById(R.id.listview);
        mAdapter = new CalenderListAdapter(getActivity());
        mCalenderArrayList= new ArrayList<Calender>();

        mSyainArrayList = new ArrayList<Syain>();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Spinner spinner = (Spinner) view.findViewById(R.id.syaincodespinner);
        mSyaincodespinner = (Spinner) newDialog.findViewById(R.id.syaincodespinner);
        mYearspinner = (Spinner) newDialog.findViewById(R.id.yearspinner);
        mMonthspinner = (Spinner) newDialog.findViewById(R.id.monthspinner);
        mDayspinner = (Spinner) newDialog.findViewById(R.id.dayspinner);
        mStimespinner = (Spinner) newDialog.findViewById(R.id.stimespinner);
        mEtimespinner = (Spinner) newDialog.findViewById(R.id.etimespinner);

        mYoteiEditText= (EditText) newDialog.findViewById(R.id.yoteiedittext);
        mTitleEditText = (EditText) newDialog.findViewById(R.id.titleedittext);

        mCreateButton = (Button) newDialog.findViewById(R.id.createbutton);
        mDeleteButton = (Button) newDialog.findViewById(R.id.deletebutton);

        mAlermCh = (CheckBox) newDialog.findViewById(R.id.alarmch);

        Calendar cal = Calendar.getInstance();
        mCyear =cal.get(Calendar.YEAR);
        mCmonth =cal.get(Calendar.MONTH);
        mCdate =cal.get(Calendar.DATE);
        mChour =cal.get(Calendar.HOUR);
        mCminute = cal.get(Calendar.MINUTE);

        alert = new AlertDialog.Builder(getActivity());
        Bundle bundle = getArguments();
        mId = bundle.getString("mId");
        mYear = bundle.getInt("mLyear");
        mMonth = bundle.getInt("mLmonth");
        mDate = bundle.getInt("mLdate");
        mStime = bundle.getString("mLstime");
        mEtime = bundle.getString("mLetime");
        mTitle = bundle.getString("mLtitle");
        mDetail = bundle.getString("mLdetail");
        mYID = bundle.getString("mYID");
        mAlertch = bundle.getBoolean("mAlertch");

        //新規登録
        if (mYID == null){
            mAEFlg = 1;
            mCreateButton.setText("登録");
            mCreateButton.setVisibility(View.VISIBLE);
            mDeleteButton.setVisibility(View.INVISIBLE);
        //修正
        }else{
            mAEFlg = 2;
            mCreateButton.setText("修正登録");
            mCreateButton.setVisibility(View.VISIBLE);
            mDeleteButton.setVisibility(View.VISIBLE);

            mTitleEditText = (EditText) newDialog.findViewById(R.id.titleedittext);
            mTitleEditText.setText(mTitle);

            mYoteiEditText = (EditText) newDialog.findViewById(R.id.yoteiedittext);
            mYoteiEditText.setText(mDetail);

            mAlermCh = (CheckBox) newDialog.findViewById(R.id.alarmch);
            mAlermCh.setChecked(mAlertch);
        }

        mToolbar = (android.support.v7.widget.Toolbar) newDialog.findViewById(R.id.toolbar);
        mToolbar.setTitle(mYear + "/" + mMonth + "/" + mDate );

        mSyaincodespinner.setAdapter(adapter);
        //社員spinner設定用
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


        //チェックボックス
        mAlermCh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CheckBox checkBox = (CheckBox) v;
                boolean checked = checkBox.isChecked();
                if (checked == false){
                    //checkBox.setChecked(false);
                }else{
                    checkBox.setChecked(true);
                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(getActivity());
                    alertDialogBuilder.setTitle("通知日を登録してください。");
                    alertDialogBuilder.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    DatePickerDialog dataPickerDialog = new DatePickerDialog(getActivity(),
                                            new DatePickerDialog.OnDateSetListener() {
                                                @Override
                                                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                                    mSYear = year;
                                                    mMonthofyear = month;
                                                    mDayOfMonth = dayOfMonth;

                                                    try{
                                                        mEcdate = mSYear + "/" + mMonthofyear + "/" + mDayOfMonth;

                                                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                                                        mSDdate = sdf.parse(mEcdate);

                                                    }catch (ParseException e){

                                                    }

                                                }
                                            },mCyear,mCmonth,mCdate);
                                    dataPickerDialog.show();
                                }
                            });
                    android.app.AlertDialog alertDialog = alertDialogBuilder.create();
                    alertDialog.show();
                    return;

                }
            }
        });

        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //新規登録
                if (mAEFlg == 1){
                    mId = mId;
                    mDatabaseReference=FirebaseDatabase.getInstance().getReference();
                    //mCalenderRef=mDatabaseReference.child(Const.CalenderPATH).child(mId).child(String.valueOf(mYear)).child(String.valueOf(mMonth)).child(String.valueOf(mDate));
                    mCalenderRef=mDatabaseReference.child(Const.CalenderPATH).child(mId).child(String.valueOf(mYear)).child(String.valueOf(mMonth)).child(String.valueOf(mDate));
                    mCalenderRef.addListenerForSingleValueEvent(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    HashMap map = (HashMap) dataSnapshot.getValue();
                                    Map<String,Object> data = new HashMap<>();

                                    String stime = (String) mStimespinner.getSelectedItem();
                                    String etime = (String) mEtimespinner.getSelectedItem();
                                    String title = mTitleEditText.getText().toString();
                                    String detail = mYoteiEditText.getText().toString();
                                    boolean alermch = mAlermCh.isChecked();

                                    data.put("開始時間",stime);
                                    data.put("終了時間",etime);
                                    data.put("タイトル",title);
                                    data.put("詳細",detail);
                                    data.put("予定登録時間",mCyear + "/" + mCmonth + "/" + mCdate + "  " + mChour + ":" + mCminute);
                                    data.put("通知時刻",mEcdate);
                                    data.put("通知on",alermch);
                                    String key= mCalenderRef.push().getKey();

                                    //アラーム準備
                                    mCalenderRef=mDatabaseReference.child(Const.CalenderPATH).child(mId).child(String.valueOf(mYear)).child(String.valueOf(mMonth)).child(String.valueOf(mDate)).child(key);
                                    mCalenderRef.setValue(data);

                                    if (alermch == true) {

                                        GregorianCalendar calendar = new GregorianCalendar(mCYear,mCMonth - 1,mCDay,mCHour,mCMinute);
                                        calendar.setTimeInMillis(0);
                                        calendar.set(mSYear,mMonthofyear,mDayOfMonth,0,0,0);

                                        Intent resultIntent  = new Intent(getActivity(),TaskAlarmReceiver.class);
                                        resultIntent.putExtra("alarm",key);
                                        resultIntent.putExtra("year",mYear);
                                        resultIntent.putExtra("month",mMonth);
                                        resultIntent.putExtra("date",mDate);
                                        resultIntent.putExtra("id",mId);
                                        resultIntent.putExtra("title",title);
                                        resultIntent.putExtra("detail",detail);

                                        resultIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                        PendingIntent resultPendingIntent = PendingIntent.getBroadcast(
                                                getActivity(),
                                                1,
                                                resultIntent ,
                                                PendingIntent.FLAG_UPDATE_CURRENT
                                        );
                                        AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(ALARM_SERVICE);
                                        alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(), resultPendingIntent);

                                    }
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            }
                    );

                    android.app.AlertDialog.Builder alertdialog = new android.app.AlertDialog.Builder(getActivity());
                    alertdialog.setTitle("登録しました。");
                    alertdialog.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    android.app.AlertDialog alertDialog = alertdialog.create();
                    alertDialog.show();

                //修正登録
                }else{
                    String stime = (String) mStimespinner.getSelectedItem();
                    String etime = (String) mEtimespinner.getSelectedItem();
                    String title = mTitleEditText.getText().toString();
                    String detail = mYoteiEditText.getText().toString();
                    boolean alermch = mAlermCh.isChecked();
                    mId = mId;

                    mDatabaseReference=FirebaseDatabase.getInstance().getReference();
                    mCalenderRef=mDatabaseReference.child(Const.CalenderPATH).child(mId).child(String.valueOf(mYear)).child(String.valueOf(mMonth)).child(String.valueOf(mDate)).child(mYID);
                    //HashMap map = (HashMap) dataSnapshot.getValue();
                    Map<String,Object> data = new HashMap<>();

                    data.put("開始時間",stime);
                    data.put("終了時間",etime);
                    data.put("タイトル",title);
                    data.put("詳細",detail);
                    data.put("予定登録時間",mCyear + "/" + mCmonth + "/" + mCdate + "  " + mChour + ":" + mCminute);
                    data.put("通知on",alermch);
                    mCalenderRef.setValue(data);


                    if (alermch == true) {

                        GregorianCalendar calendar = new GregorianCalendar(mCYear,mCMonth - 1,mCDay,mCHour,mCMinute);
                        calendar.setTimeInMillis(0);
                        calendar.set(mSYear,mMonthofyear,mDayOfMonth,0,0,0);

                        Intent resultIntent  = new Intent(getActivity(),TaskAlarmReceiver.class);
                        resultIntent.putExtra("alarm",mYID);
                        resultIntent.putExtra("year",mYear);
                        resultIntent.putExtra("month",mMonth);
                        resultIntent.putExtra("date",mDate);
                        resultIntent.putExtra("id",mId);
                        resultIntent.putExtra("title",title);
                        resultIntent.putExtra("detail",detail);

                        PendingIntent resultPendingIntent = PendingIntent.getBroadcast(
                                getActivity(),
                                1,
                                resultIntent ,
                                PendingIntent.FLAG_UPDATE_CURRENT
                        );
                        AlarmManager alarmManager = (AlarmManager)getActivity().getSystemService(ALARM_SERVICE);
                        alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(), resultPendingIntent);

                    }


                    android.app.AlertDialog.Builder alertdialog = new android.app.AlertDialog.Builder(getActivity());
                    alertdialog.setTitle("修正しました。");
                    alertdialog.setPositiveButton("OK",
                            new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.cancel();
                                }
                            });
                    android.app.AlertDialog alertDialog = alertdialog.create();
                    alertDialog.show();

                }


            }


        });

        mDeleteButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                View view = getActivity().getLayoutInflater().inflate(R.layout.calender_edit, null);
                mId = mId;
                mDatabaseReference=FirebaseDatabase.getInstance().getReference();
                mCalenderRef=mDatabaseReference.child(Const.CalenderPATH).child(mId).child(String.valueOf(mYear)).child(String.valueOf(mMonth)).child(String.valueOf(mDate)).child(mYID);
                android.app.AlertDialog.Builder alertdialog = new android.app.AlertDialog.Builder(getActivity());
                alertdialog.setTitle("削除しますか？");
                alertdialog.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                android.app.AlertDialog.Builder alertdialog = new android.app.AlertDialog.Builder(getActivity());
                                alertdialog.setTitle("削除しました。");
                                alertdialog.setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                dialog.cancel();

                                                mDatabaseReference=FirebaseDatabase.getInstance().getReference();
                                                mCalenderRef=mDatabaseReference.child(Const.CalenderPATH).child(mId).child(String.valueOf(mYear)).child(String.valueOf(mMonth)).child(String.valueOf(mDate)).child(mYID);
                                                mCalenderRef.addListenerForSingleValueEvent(
                                                        new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                HashMap map = (HashMap) dataSnapshot.getValue();
                                                                Map<String,Object> data = new HashMap<>();

                                                                String title = (String) map.get("title");
                                                                String detail = (String) map.get("detail");

                                                                //タスク削除
                                                                Intent resultIntent  = new Intent(getActivity(),TaskAlarmReceiver.class);
                                                                resultIntent.putExtra("alarm",mYID);
                                                                resultIntent.putExtra("year",mYear);
                                                                resultIntent.putExtra("month",mMonth);
                                                                resultIntent.putExtra("date",mDate);
                                                                resultIntent.putExtra("id",mId);
                                                                resultIntent.putExtra("title",title);
                                                                resultIntent.putExtra("detail",detail);
                                                                PendingIntent resultPendingIntent = PendingIntent.getBroadcast(
                                                                        getActivity(),
                                                                        1,
                                                                        resultIntent,
                                                                        PendingIntent.FLAG_UPDATE_CURRENT
                                                                );

                                                                AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
                                                                alarmManager.cancel(resultPendingIntent);

                                                                mCalenderRef.removeValue();
                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {

                                                            }
                                                        }
                                                );



                                            }
                                        });
                                android.app.AlertDialog alertDialog = alertdialog.create();
                                alertDialog.show();
                            }
                        });
                android.app.AlertDialog alertDialog = alertdialog.create();
                alertDialog.show();
            }
        });

        return new android.app.AlertDialog.Builder(getActivity(),R.style.MyAlertDialogTheme)
                .setView(newDialog)
                .show();

    }

}
