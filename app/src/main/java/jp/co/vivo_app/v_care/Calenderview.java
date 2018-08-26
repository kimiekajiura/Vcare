package jp.co.vivo_app.v_care;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.support.v7.widget.Toolbar;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import jp.co.vivo_app.v_care.R;

public class Calenderview extends DialogFragment {

    private int mYear;
    private int mMonth;
    private int mDate;
    private String mId;

    private int mCyear;
    private int mCmonth;
    private int mCdate;
    private int mChour;
    private int mCminute;

    private Toolbar mToolbar;

    DatabaseReference mDatabaseReference;

    private AlertDialog.Builder alert;

    private DatabaseReference mCalenderRef;
    private CalenderListAdapter mAdapter;
    private ListView mListView;
    private ArrayList<Calender> mCalenderArrayList;

    private DialogFragment dialogFragment;
    private FragmentManager flagmentManager;

    private String mLid;
    private int mLyear;
    private int mLmonth;
    private int mLdate;
    private String mLstime;
    private String mLetime;
    private String mLtitle;
    private String mLdetail;

    private ArrayList<Attendance> mAttendanceArrayList;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View mainTabView = getActivity().getLayoutInflater().inflate(R.layout.calender_view, null);

        alert = new AlertDialog.Builder(getActivity());
        Bundle bundle = getArguments();

        mId = bundle.getString("mId");
        mYear = bundle.getInt("mLyear");
        mMonth = bundle.getInt("mLmonth");
        mDate = bundle.getInt("mLdate");

        mToolbar = (android.support.v7.widget.Toolbar) mainTabView.findViewById(R.id.toolbar);
        mToolbar.setTitle(mYear + "/" + mMonth + "/" + mDate );

        mListView = (ListView) mainTabView.findViewById(R.id.listview);
        mAdapter = new CalenderListAdapter(getActivity());
        mCalenderArrayList= new ArrayList<Calender>();

        Calendar cal = Calendar.getInstance();

        mCyear =cal.get(Calendar.YEAR);
        mCmonth =cal.get(Calendar.MONTH);
        mCdate =cal.get(Calendar.DATE);
        mChour =cal.get(Calendar.HOUR);
        mCminute = cal.get(Calendar.MINUTE);

        //一覧表示
        mDatabaseReference= FirebaseDatabase.getInstance().getReference();
        mDatabaseReference.child(Const.CalenderPATH).child(mId).child(String.valueOf(mYear)).child(String.valueOf(mMonth)).child(String.valueOf(mDate)).addChildEventListener(
                new ChildEventListener() {
                    @Override
                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                        HashMap map = (HashMap) dataSnapshot.getValue();
                        Map<String,Object> data = new HashMap<>();

                        String stime = (String) map.get("開始時間");
                        String etime = (String) map.get("終了時間");
                        String title = (String) map.get("タイトル");
                        String detail = (String) map.get("詳細");
                        String calenderid = (dataSnapshot.getKey());

                        Calender calender = new Calender(mYear,mMonth,mDate,stime,etime,title,detail,mId,calenderid);

                        mCalenderArrayList.add(calender);

                        mAdapter.setCalenderArrayList(mCalenderArrayList);
                        if (mListView == null){
                            View newDialog = getActivity().getLayoutInflater().inflate(R.layout.calender_view, null);
                            mListView = (ListView) newDialog.findViewById(R.id.listview);
                        }
                        mListView.setAdapter(mAdapter);
                        mAdapter.notifyDataSetChanged();
                        return;
                    }

                    @Override
                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                        HashMap map = (HashMap) dataSnapshot.getValue();
                        Map<String,Object> data = new HashMap<>();

                        for (Calender calender : mCalenderArrayList){
                            if (String.valueOf(dataSnapshot.getKey()).equals(String.valueOf(calender.getYid()))) {
                                String stime = (String) map.get("開始時間");
                                String etime = (String) map.get("終了時間");
                                String title = (String) map.get("タイトル");
                                String detail = (String) map.get("詳細");
                                String calenderid = (dataSnapshot.getKey());

                                calender.setYear(mYear);
                                calender.setMonth(mMonth);
                                calender.setDate(mDate);
                                calender.setStime(stime);
                                calender.setEtime(etime);
                                calender.setTitle(title);
                                calender.setDetail(detail);
                                calender.setYid(calenderid);
                                mAdapter.notifyDataSetChanged();
                            }
                        }

                    }

                    @Override
                    public void onChildRemoved(DataSnapshot dataSnapshot) {
                        mCalenderArrayList.clear();
                        mDatabaseReference= FirebaseDatabase.getInstance().getReference();
                        mDatabaseReference.child(Const.CalenderPATH).child(mId).child(String.valueOf(mYear)).child(String.valueOf(mMonth)).child(String.valueOf(mDate)).addChildEventListener(
                                new ChildEventListener() {
                                    @Override
                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                        HashMap map = (HashMap) dataSnapshot.getValue();
                                        Map<String,Object> data = new HashMap<>();


                                        String stime = (String) map.get("開始時間");
                                        String etime = (String) map.get("終了時間");
                                        String title = (String) map.get("タイトル");
                                        String detail = (String) map.get("詳細");
                                        String calenderid = (dataSnapshot.getKey());

                                        Calender calender = new Calender(mYear,mMonth,mDate,stime,etime,title,detail,mId,calenderid);
                                        mCalenderArrayList.add(calender);

                                        mAdapter.setCalenderArrayList(mCalenderArrayList);
                                        if (mListView == null){
                                            View newDialog = getActivity().getLayoutInflater().inflate(R.layout.calender_view, null);
                                            mListView = (ListView) newDialog.findViewById(R.id.listview);
                                        }
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
                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );

        //リストビュータップ
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                View mainTabView = getActivity().getLayoutInflater().inflate(R.layout.calender_view, null);
                mListView = (ListView) mainTabView.findViewById(R.id.listView);
                flagmentManager = getFragmentManager();
                dialogFragment = new CalenderEdit();

                mLid =mId;
                mLyear =mCalenderArrayList.get(position).getYear();
                mLmonth =mCalenderArrayList.get(position).getMonth();
                mLdate =mCalenderArrayList.get(position).getDate();
                mLstime =mCalenderArrayList.get(position).getStime();
                mLetime =mCalenderArrayList.get(position).getEtime();
                mLtitle =mCalenderArrayList.get(position).getTitle();
                mLdetail =mCalenderArrayList.get(position).getDetail();

                Bundle bundle = new Bundle();

                bundle.putString("mId",mId);
                bundle.putInt("mLyear",mLyear);
                bundle.putInt("mLmonth",mLmonth);
                bundle.putInt("mLdate",mLdate);
                bundle.putString("mLstime",mLstime);
                bundle.putString("mLetime",mLetime);
                bundle.putString("mLtitle",mLtitle);
                bundle.putString("mLdetail",mLdetail);
                bundle.putString("mYID",mCalenderArrayList.get(position).getYid());

                dialogFragment.setArguments(bundle);
                dialogFragment.show(flagmentManager,"calender");

            }
        });

        //新規登録画面開く
        FloatingActionButton fab = (FloatingActionButton) mainTabView.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flagmentManager = getFragmentManager();
                dialogFragment = new CalenderEdit();

                Bundle bundle = new Bundle();
                bundle.putString("mId",mId);
                bundle.putInt("mLyear",mYear);
                bundle.putInt("mLmonth",mMonth);
                bundle.putInt("mLdate",mDate);

                dialogFragment.setArguments(bundle);
                dialogFragment.show(flagmentManager,"calendaredit");
                return;
            }
        });

        return new android.app.AlertDialog.Builder(getActivity(), R.style.MyAlertDialogTheme)
                .setView(mainTabView)
                .show();
    }
}
