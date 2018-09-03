package jp.co.vivo_app.v_care;

import android.os.Bundle;
//import android.support.v4.app.DialogFragment;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

public class DairyReportEdit extends DialogFragment {

    private ArrayList<Syain> mSyainArrayList;
    private ArrayList<Customer> mCustomerArrayList;
    private ArrayList<Project> mProjectArrayList;
    private ArrayList<System> mSystemArrayList;
    private ArrayList<Syuukeibunrui> mSyuukeibunruiArrayList;
    private ArrayList<Sagyoukubun> mSagyoukubunArrayList;

    private DatabaseReference mUserRef;
    private DatabaseReference mCustemorRef;
    private DatabaseReference mProjectRef;
    private DatabaseReference mSystemRef;
    private DatabaseReference mBunruiRef;
    private DatabaseReference mSagyouRef;

    DatabaseReference mDatabaseReference;

    private Spinner mIdSpinner;
    private Spinner mYearspinner;
    private Spinner mMonthspinner;
    private Spinner mDayspinner;
    private Spinner mSyuukeibunrui;
    private Spinner mSagyoukubunn;

    private Spinner mTorihikisaki;
    private Spinner mTorihikisakibusyo;
    private Spinner mProject;
    private Spinner mSystem;

    ArrayAdapter<String> adapter;
    ArrayAdapter<String> yadapter;
    ArrayAdapter<String> madapter;
    ArrayAdapter<String> dadapter;
    ArrayAdapter<String> cadapter;
    ArrayAdapter<String> padapter;
    ArrayAdapter<String> sadapter;
    ArrayAdapter<String> sbadapter;
    ArrayAdapter<String> skadapter;

    private int mYear;
    private int mMonth;
    private int mDate;

    private int mCyear;
    private int mCmonth;
    private int mCdate;
    private int mChour;
    private int mCminute;

    private String mItem;
    private int mSyain;
    private int mCust;
    private int mCustBusyo;

    TextView mNameTextView;
    TextView mYearTextView;
    TextView mMonthTextView;
    TextView mDayTextView;
    TextView mTorihikisakiTextView;
    TextView mTorihikisakiBusyoTextView;
    TextView mProjectTextView;
    TextView mSystemTextView;
    TextView mSyuukeibunruiTextView;
    TextView mSagyoukubunTextView;
    TextView mJissekikousuuextView;
    TextView mHosyukousuuTextView;
    TextView mNaiyouTextView;

    //社員
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

    //取引先
    ChildEventListener mCustomerListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            HashMap map = (HashMap) dataSnapshot.getValue();
            String Id = dataSnapshot.getKey();

            String name = (String) map.get("name");

            ArrayList<Busyo> busyoArrayList = new ArrayList<Busyo>();
            HashMap busyoMap = (HashMap) map.get("busyo");
            if (busyoMap != null){
                for (Object key : busyoMap.keySet()){
                    HashMap temp =(HashMap) busyoMap.get((String) key);
                    String bid = (String) temp.get("bid");
                    String bname = (String) temp.get("name");

                    Busyo busyo = new Busyo(bid,bname,(String)key);
                    busyoArrayList.add(busyo);

                }
            }

            Customer customer = new Customer(Id, name);
            mCustomerArrayList.add(customer);
            cadapter.add(Id);
            cadapter.notifyDataSetChanged();
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

    //プロジェクト
    ChildEventListener mProjectListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            HashMap map = (HashMap) dataSnapshot.getValue();
            String projectId = dataSnapshot.getKey();

            String name = (String) map.get("name");
            String customerId = (String) map.get("customer");

            Project project = new Project(projectId,name,customerId);
            mProjectArrayList.add(project);
            padapter.add(projectId);
            padapter.notifyDataSetChanged();
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

    //システム
    ChildEventListener mSystemListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            HashMap map = (HashMap) dataSnapshot.getValue();
            String systemId = dataSnapshot.getKey();

            String name = (String) map.get("name");
            String customerId = (String) map.get("customer");

            System system = new System(systemId,name,customerId);
            mSystemArrayList.add(system);
            sadapter.add(systemId);
            sadapter.notifyDataSetChanged();
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


    //集計分類
    ChildEventListener mSyuukeibunruiListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            HashMap map = (HashMap) dataSnapshot.getValue();
            String syuukeibunruiid = dataSnapshot.getKey();
            String name = (String) map.get("name");

            Syuukeibunrui syuukeibunrui = new Syuukeibunrui(syuukeibunruiid,name);
            mSyuukeibunruiArrayList.add(syuukeibunrui);
            sbadapter.add(name);
            sbadapter.notifyDataSetChanged();
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

    //作業区分
    ChildEventListener mSagyoukubunnListener = new ChildEventListener() {
        @Override
        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
            HashMap map = (HashMap) dataSnapshot.getValue();
            String sagyoukubunid = dataSnapshot.getKey();
            String name = (String) map.get("name");

            Syuukeibunrui syuukeibunrui = new Syuukeibunrui(sagyoukubunid,name);
            mSyuukeibunruiArrayList.add(syuukeibunrui);
            skadapter.add(name);
            skadapter.notifyDataSetChanged();
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
    public Dialog onCreateDialog(Bundle savedInstanceState){

        View mainTabView = getActivity().getLayoutInflater().inflate(R.layout.dairy_report_edit, null);

        Calendar cal = Calendar.getInstance();

        mCyear =cal.get(Calendar.YEAR);
        mCmonth =cal.get(Calendar.MONTH);
        mCdate =cal.get(Calendar.DATE);
        mChour =cal.get(Calendar.HOUR);
        mCminute = cal.get(Calendar.MINUTE);



        //年月日スピナー
        mYearspinner = (Spinner) mainTabView.findViewById(R.id.yearspinner);
        mMonthspinner = (Spinner) mainTabView.findViewById(R.id.monthspinner);
        mDayspinner = (Spinner) mainTabView.findViewById(R.id.dayspinner);

        //年スピナー
        yadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item);
        yadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mYearspinner.setAdapter(yadapter);

        //月スピナー
        madapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item);
        madapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mMonthspinner.setAdapter(madapter);

        dadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item);
        dadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mDayspinner.setAdapter(dadapter);

        for(int i = mCyear - 1; i <= mCyear; i++){
            yadapter.add(String.valueOf(i));
        }
        for(int i = 1; i <= 12; i++){
            madapter.add(String.valueOf(i));
        }

        for(int i = 1; i <= 31; i++){
            dadapter.add(String.valueOf(i));
        }

        //アダプターをセットする
        mYearspinner.setAdapter(yadapter);
        mMonthspinner.setAdapter(madapter);
        mDayspinner.setAdapter(dadapter);

        //スピナーの初期値を現在の日付にする
        mYearspinner.setSelection(1);
        mMonthspinner.setSelection(mCmonth-1);
        mDayspinner.setSelection(mCdate);

        //社員スピナー
        mSyainArrayList = new ArrayList<Syain>();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = (Spinner) mainTabView.findViewById(R.id.syaincodespinner);
        spinner.setAdapter(adapter);

        mUserRef = mDatabaseReference.child(Const.UserPATH);
        mUserRef.addChildEventListener(mUserListener);
        //return;

        spinner.setPrompt("社員CODE");
        TextView textView = (TextView) mainTabView.findViewById(R.id.syainnametextview);
        textView.setText("");

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                View mainTabView = getActivity().getLayoutInflater().inflate(R.layout.dairy_report_edit, null);
                mIdSpinner = (Spinner) mainTabView.findViewById(R.id.syaincodespinner);
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

                                View mainTabView = getActivity().getLayoutInflater().inflate(R.layout.dairy_report_edit, null);
                                TextView textView = mainTabView.findViewById(R.id.syainnametextview);
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

        //取引先
        cadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item);
        cadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mCustomerArrayList = new ArrayList<Customer>();
        mTorihikisaki = (Spinner) mainTabView.findViewById(R.id.torihikisakispinner);
        mTorihikisaki.setAdapter(cadapter);

        mCustemorRef = mDatabaseReference.child(Const.CustomerPATH);
        mCustemorRef.addChildEventListener(mCustomerListener);

        mTorihikisakiTextView = (TextView) mainTabView.findViewById(R.id.torihikisakitextview);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mTorihikisaki.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                View mainTabView = getActivity().getLayoutInflater().inflate(R.layout.dairy_report_edit, null);
                mTorihikisaki = (Spinner) mainTabView.findViewById(R.id.torihikisakispinner);
                String item = (String) mTorihikisaki.getSelectedItem();
                if (item == null){
                    mTorihikisaki.setAdapter(cadapter);
                    mDatabaseReference.child(Const.CustomerPATH).addListenerForSingleValueEvent(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    HashMap map = (HashMap)dataSnapshot.getValue();
                                    String Id = dataSnapshot.getKey();

                                    String name = (String) map.get("name");

                                    mTorihikisaki.setAdapter(cadapter);
                                    mTorihikisakiTextView.setText(name);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            }
                    );
                }else{
                    mDatabaseReference.child(Const.CustomerPATH).child(item).addListenerForSingleValueEvent(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    HashMap map = (HashMap)dataSnapshot.getValue();

                                    boolean adminkengen = (boolean) map.get("adminkengen");
                                    String group = (String) map.get("group");
                                    String name = (String) map.get("name");
                                    String password = (String) map.get("password");

                                    mTorihikisakiTextView.setText(name);
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            }
                    );
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //取引先部署
//        mTorihikisakibusyo = (Spinner)mainTabView.findViewById(R.id.torihikisakibusyospinner);
//        mCustemorRef = mDatabaseReference.child(Const.CustomerPATH).child(String.valueOf(mTorihikisaki));
//        mCustemorRef.addChildEventListener(mCustomerListener);

        //プロジェクト
        padapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item);
        padapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mProjectArrayList = new ArrayList<Project>();
        mProject = (Spinner)mainTabView.findViewById(R.id.projectspinner);
        mProject.setAdapter(padapter);

        mProjectRef = mDatabaseReference.child(Const.ProjectPATH);
        mProjectRef.addChildEventListener(mProjectListener);

        //システム名
        sadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item);
        sadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSystemArrayList = new ArrayList<System>();
        mSystem = (Spinner)mainTabView.findViewById(R.id.systemspinner);
        mSystem.setAdapter(sadapter);

        mSagyouRef = mDatabaseReference.child(Const.SystemPATH);
        mSagyouRef.addChildEventListener(mSystemListener);



        //集計分類
        sbadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item);
        sbadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSyuukeibunruiArrayList = new ArrayList<Syuukeibunrui>();
        mSyuukeibunrui = (Spinner)mainTabView.findViewById(R.id.syuukeibunruispinner);
        mSyuukeibunrui.setAdapter(sbadapter);

        mSagyouRef = mDatabaseReference.child(Const.BunruiPATH);
        mSagyouRef.addChildEventListener(mSyuukeibunruiListener);

        //作業区分
        skadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item);
        skadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSagyoukubunArrayList = new ArrayList<Sagyoukubun>();
        mSagyoukubunn = (Spinner)mainTabView.findViewById(R.id.sagyoukubunnspinner);
        mSagyoukubunn.setAdapter(skadapter);

        mSagyouRef = mDatabaseReference.child(Const.SagyouPATH);
        mSagyouRef.addChildEventListener(mSagyoukubunnListener);

        mNaiyouTextView = (EditText) mainTabView.findViewById(R.id.naiyouedittext);
        //登録処理
        Button createButton = (Button) mainTabView.findViewById(R.id.createbutton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View mainTabView = getActivity().getLayoutInflater().inflate(R.layout.dairy_report_edit, null);
                mYear = Integer.parseInt(mYearspinner.getSelectedItem().toString());
                mMonth = Integer.parseInt(mMonthspinner.getSelectedItem().toString());
                mDate=Integer.parseInt(mDayspinner.getSelectedItem().toString());

                mSyain = Integer.parseInt(mIdSpinner.getSelectedItem().toString());
                mCust = Integer.parseInt(mTorihikisaki.getSelectedItem().toString());
                mCustBusyo = Integer.parseInt(mTorihikisakibusyo.getSelectedItem().toString());
                mSyain = Integer.parseInt(mIdSpinner.getSelectedItem().toString());
                mCust = Integer.parseInt(mTorihikisaki.getSelectedItem().toString());
                mCustBusyo = Integer.parseInt(mTorihikisakibusyo.getSelectedItem().toString());


                String item = mNaiyouTextView.getText().toString();

            }
        });

        return new android.app.AlertDialog.Builder(getActivity(),R.style.MyAlertDialogTheme)
                .setView(mainTabView)
                .show();

    }
}
