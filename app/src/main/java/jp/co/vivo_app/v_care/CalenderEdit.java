package jp.co.vivo_app.v_care;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class CalenderEdit extends DialogFragment {

    private int mYear;
    private int mMonth;
    private int mDate;
    private String mId;
    private String mStime;
    private String mEtime;
    private String mTitle;
    private String mDetail;

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

    ArrayAdapter<String> adapter;
    private ArrayList<Syain> mSyainArrayList;
    private ArrayList<Calender> mCalenderArrayList;

    DatabaseReference mDatabaseReference;

    private DatabaseReference mCalenderRef;
    private CalenderListAdapter mAdapter;
    private ListView mListView;

    private int mAEFlg;

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

        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //新規登録
                if (mAEFlg == 1){
                    mId = mId;
                    mDatabaseReference=FirebaseDatabase.getInstance().getReference();
                    mCalenderRef=mDatabaseReference.child(Const.CalenderPATH).child(mId).child(String.valueOf(mYear)).child(String.valueOf(mMonth)).child(String.valueOf(mDate));
                    //HashMap map = (HashMap) dataSnapshot.getValue();
                    Map<String,Object> data = new HashMap<>();

                    String stime = (String) mStimespinner.getSelectedItem();
                    String etime = (String) mEtimespinner.getSelectedItem();
                    String title = mTitleEditText.getText().toString();
                    String detail = mYoteiEditText.getText().toString();

                    data.put("開始時間",stime);
                    data.put("終了時間",etime);
                    data.put("タイトル",title);
                    data.put("詳細",detail);
                    data.put("予定登録時間",mCyear + "/" + mCmonth + "/" + mCdate + "  " + mChour + ":" + mCminute);

                    mCalenderRef.push().setValue(data);

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

                    mCalenderRef.setValue(data);

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
                                mCalenderRef.removeValue();

                                android.app.AlertDialog.Builder alertdialog = new android.app.AlertDialog.Builder(getActivity());
                                alertdialog.setTitle("削除しました。");
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
