package jp.co.vivo_app.v_care;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;

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

    private int mCyear;
    private int mCmonth;
    private int mCdate;
    private int mChour;
    private int mCminute;

    private int mYID;

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

    private EditText mYoteiEditText;
    private EditText mKenmeiEditTet;

    private Button mCreateButton;

    ArrayAdapter<String> adapter;
    private ArrayList<Syain> mSyainArrayList;
    private ArrayList<Calender> mCalenderArrayList;
    DatabaseReference mDatabaseReference;

    private DatabaseReference mCalenderRef;

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
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = getActivity().getLayoutInflater().inflate(R.layout.calender_edit, null);

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

        mSyaincodespinner = (Spinner) view.findViewById(R.id.syaincodespinner);
        mYearspinner = (Spinner) view.findViewById(R.id.yearspinner);
        mMonthspinner = (Spinner) view.findViewById(R.id.monthspinner);
        mDayspinner = (Spinner) view.findViewById(R.id.dayspinner);
        mStimespinner = (Spinner) view.findViewById(R.id.stimespinner);
        mEtimespinner = (Spinner) view.findViewById(R.id.etimespinner);

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

        mYoteiEditText= (EditText) view.findViewById(R.id.yoteiedittext);
        mKenmeiEditTet = (EditText) view.findViewById(R.id.titleedittext);

        mCreateButton=(Button) view.findViewById(R.id.createbutton);
        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //mId = (String) mSyaincodespinner.getSelectedItem();
                mYear = Integer.parseInt(mYearspinner.getSelectedItem().toString());
                mMonth = Integer.parseInt(mMonthspinner.getSelectedItem().toString());
                mDate=Integer.parseInt(mDayspinner.getSelectedItem().toString());

                mDatabaseReference=FirebaseDatabase.getInstance().getReference();
                mCalenderRef=mDatabaseReference.child(Const.CalenderPATH).child(String.valueOf(mYear)).child(String.valueOf(mMonth)).child(String.valueOf(mDate)).child(mId);
                mCalenderRef.addChildEventListener(
                        new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                    String t = dataSnapshot.getKey();
                                    if (t != null){
                                        mYID = mYID + 1;
                                        HashMap map = (HashMap) dataSnapshot.getValue();

                                        if (map == null){
                                            mYID = 1;
                                        }else{
                                            int yid = (int)map.get("YID");
                                        }
                                        Map<String,Object> data = new HashMap<>();

                                        String stime = (String) mStimespinner.getSelectedItem();
                                        String etime = (String) mEtimespinner.getSelectedItem();
                                        String title = mKenmeiEditTet.getText().toString();
                                        String detail = mYoteiEditText.getText().toString();

                                        data.put("YID",mYID);
                                        data.put("開始時間",stime);
                                        data.put("終了時間",etime);
                                        data.put("タイトル",title);
                                        data.put("詳細",detail);
                                        data.put("予定登録時間",mCyear + "/" + mCmonth + "/" + mCdate + "  " + mChour + ":" + mCminute);

                                        mCalenderRef.setValue(data);
                                        android.app.AlertDialog.Builder alertdialog = new android.app.AlertDialog.Builder(getActivity());
                                        alertdialog.setTitle("登録しました。");
                                        alertdialog.setPositiveButton("OK",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dismiss();
                                                    }
                                                });
                                        android.app.AlertDialog alertDialog = alertdialog.create();
                                        alertDialog.show();
                                    }else{
                                        mYID = 1;
                                        mCalenderRef=mDatabaseReference.child(Const.CalenderPATH).child(String.valueOf(mYear)).child(String.valueOf(mMonth)).child(String.valueOf(mDate)).child(mId).child(String.valueOf(mYID));

                                        Map<String,Object> data = new HashMap<>();

                                        String stime = (String) mStimespinner.getSelectedItem();
                                        String etime = (String) mEtimespinner.getSelectedItem();
                                        String title = mKenmeiEditTet.getText().toString();
                                        String detail = mYoteiEditText.getText().toString();

                                        data.put("開始時間",stime);
                                        data.put("終了時間",etime);
                                        data.put("タイトル",title);
                                        data.put("詳細",detail);
                                        data.put("予定登録時間",mCyear + "/" + mCmonth + "/" + mCdate + "  " + mChour + ":" + mCminute);

                                        mCalenderRef.setValue(data);
                                        android.app.AlertDialog.Builder alertdialog = new android.app.AlertDialog.Builder(getActivity());
                                        alertdialog.setTitle("登録しました。");
                                        alertdialog.setPositiveButton("OK",
                                                new DialogInterface.OnClickListener() {
                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {
                                                        dismiss();
                                                    }
                                                });
                                        android.app.AlertDialog alertDialog = alertdialog.create();
                                        alertDialog.show();
                                    }
                                    mYID = mYID + 1;

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

                /*
                mCalenderRef.addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                HashMap map = (HashMap) dataSnapshot.getValue();
                                if (map == null){
                                    mYID = 1;
                                    Map<String,Object> data = new HashMap<>();

                                    String stime = (String) mStimespinner.getSelectedItem();
                                    String etime = (String) mEtimespinner.getSelectedItem();
                                    String title = mKenmeiEditTet.getText().toString();
                                    String detail = mYoteiEditText.getText().toString();

                                    data.put("開始時間",stime);
                                    data.put("終了時間",etime);
                                    data.put("タイトル",title);
                                    data.put("詳細",detail);
                                    data.put("予定登録時間",mCyear + "/" + mCmonth + "/" + mCdate + "  " + mChour + ":" + mCminute);

                                    mCalenderRef.setValue(data);
                                    android.app.AlertDialog.Builder alertdialog = new android.app.AlertDialog.Builder(getActivity());
                                    alertdialog.setTitle("登録しました。");
                                    alertdialog.setPositiveButton("OK",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    dismiss();
                                                }
                                            });
                                    android.app.AlertDialog alertDialog = alertdialog.create();
                                    alertDialog.show();
                                }else{

                                }

                                mYID = 1;
                                //mCalenderRef=mDatabaseReference.child(Const.CalenderPATH).child(String.valueOf(mYear)).child(String.valueOf(mMonth)).child(String.valueOf(mDate)).child(mId).child(String.valueOf(mYID));
                                //String yid = dataSnapshot.getKey();

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        }
                );
                */

            }

        });



        return new android.app.AlertDialog.Builder(getActivity(),R.style.MyAlertDialogTheme)
                .setView(view)
                .setTitle("予定登録")
                .show();

    }
}
