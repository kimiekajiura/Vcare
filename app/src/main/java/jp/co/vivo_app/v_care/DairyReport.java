package jp.co.vivo_app.v_care;

import android.content.Intent;
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
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
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
import java.util.Map;

public class DairyReport extends AppCompatActivity {

    private ArrayList<Syain> mSyainArrayList;
    private ArrayList<Customer> mCustomerArrayList;
    private ArrayList<Busyo> mBusyoArrayList;
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
    private DatabaseReference mDeiryReportRef;

    DatabaseReference mDatabaseReference;

    private Spinner mIdSpinner;
    private Spinner mYearspinner;
    private Spinner mMonthspinner;
    private Spinner mDayspinner;
    private Spinner mSyuukeibunrui;
    private Spinner mSagyoukubunn;

    private String mId;
    private String mPassword;
    private String mName;
    private String mGroup;
    private boolean mAdminkengen;
    private String mBikou;

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
    ArrayAdapter<String> badapter;

    private int mYear;
    private int mMonth;
    private int mDate;

    private int mCyear;
    private int mCmonth;
    private int mCdate;
    private int mChour;
    private int mCminute;

    private String mItem;
    private String mSyain;
    private String mCust;
    private String mCustBusyo;
    private int mPro;
    private int mSys;
    private String mSbunrui;
    private String mSagyou;
    private String mNaiyou;
    private int mHosyu;
    private int mJisseki;

    private String mSyainname;
    private String mTorihikisakiname;
    private String mTorihikisakiBusyoname;
    private String mProjectname;
    private String mSystemname;

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

//            ArrayList<Busyo> busyoArrayList = new ArrayList<Busyo>();
//            HashMap busyoMap = (HashMap) map.get("busyo");
//            if (busyoMap != null){
//                for (Object key : busyoMap.keySet()){
//                    HashMap temp =(HashMap) busyoMap.get((String) key);
//                    String bid = (String) temp.get("bid");
//                    String bname = (String) temp.get("busyoname");
//
//                    Busyo busyo = new Busyo(bid,bname,(String)key);
//                    mBusyoArrayList.add(busyo);
//                    badapter.add(bid);
//                    badapter.notifyDataSetChanged();
//
//                }
//            }

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
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dairy_report);


        Calendar cal = Calendar.getInstance();

        mCyear =cal.get(Calendar.YEAR);
        mCmonth =cal.get(Calendar.MONTH);
        mCdate =cal.get(Calendar.DATE);
        mChour =cal.get(Calendar.HOUR);
        mCminute = cal.get(Calendar.MINUTE);

        Bundle extras = getIntent().getExtras();
        mId = extras.getString("mId");
        mPassword = extras.getString("mPassword");
        mName = extras.getString("mName");
        mGroup = extras.getString("mGroup");
        mAdminkengen = extras.getBoolean("mAdminkengen");
        mBikou = extras.getString("mBikou");




        //年月日スピナー
        mYearspinner = (Spinner) findViewById(R.id.yearspinner);
        mMonthspinner = (Spinner) findViewById(R.id.monthspinner);
        mDayspinner = (Spinner) findViewById(R.id.dayspinner);

        //年スピナー
        yadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        yadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mYearspinner.setAdapter(yadapter);

        //月スピナー
        madapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        madapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mMonthspinner.setAdapter(madapter);

        dadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
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
        mMonthspinner.setSelection(mCmonth);
        mDayspinner.setSelection(mCdate);

        //社員スピナー
        mSyainArrayList = new ArrayList<Syain>();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = (Spinner) findViewById(R.id.syaincodespinner);
        spinner.setAdapter(adapter);

        mUserRef = mDatabaseReference.child(Const.UserPATH);
        mUserRef.addChildEventListener(mUserListener);

        mNameTextView = (TextView) findViewById(R.id.syainnametextview);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mIdSpinner = (Spinner) findViewById(R.id.syaincodespinner);
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

                                TextView textView = findViewById(R.id.syainnametextview);
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
        cadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        cadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mCustomerArrayList = new ArrayList<Customer>();
        mTorihikisaki = (Spinner) findViewById(R.id.torihikisakispinner);
        mTorihikisaki.setAdapter(cadapter);

        mCustemorRef = mDatabaseReference.child(Const.CustomerPATH);
        mCustemorRef.addChildEventListener(mCustomerListener);

        mTorihikisakiTextView = (TextView) findViewById(R.id.torihikisakitextview);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mTorihikisaki.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                mTorihikisaki = (Spinner) findViewById(R.id.torihikisakispinner);
                String item = (String) mTorihikisaki.getSelectedItem();
                    mDatabaseReference.child(Const.CustomerPATH).child(item).addListenerForSingleValueEvent(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    HashMap map = (HashMap)dataSnapshot.getValue();

                                    String name = (String) map.get("name");

                                    TextView textView = findViewById(R.id.torihikisakitextview);
                                    textView.setText(name);

                                    mBusyoArrayList.clear();
                                    badapter.clear();

                                    ArrayList<Busyo> busyoArrayList = new ArrayList<Busyo>();
                                    HashMap busyomap = (HashMap) map.get("busyo");
                                    if (busyomap != null){
                                        for (Object key : busyomap.keySet()){
                                            HashMap temp = (HashMap) busyomap.get((String)key);
                                            String busyoname = (String) temp.get("busyoname");
                                            String busyoid = (String) temp.get("bid");
                                            Busyo busyo = new Busyo(busyoid,busyoname,(String)key);

                                            mBusyoArrayList.add(busyo);
                                            badapter.add(busyoid);
                                            badapter.notifyDataSetChanged();

                                            TextView btextView = findViewById(R.id.torihikisakibusyotextview);
                                            btextView.setText(busyoname);
                                        }
                                    }
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

        //取引先部署
        badapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        badapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mBusyoArrayList = new ArrayList<Busyo>();
        mTorihikisakibusyo = (Spinner) findViewById(R.id.torihikisakibusyospinner);
        mTorihikisakibusyo.setAdapter(badapter);


//        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
//        mTorihikisakibusyo.setOnItemSelectedListener(
//                new AdapterView.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                        mTorihikisaki = (Spinner) findViewById(R.id.torihikisakispinner);
//                        String item = (String) mTorihikisaki.getSelectedItem();
//
//                        mTorihikisakibusyo = (Spinner) findViewById(R.id.torihikisakibusyospinner);
//                        String busyoid = (String) mTorihikisakibusyo.getSelectedItem();
//                        mDatabaseReference.child(Const.CustomerPATH).child(item).child(busyoid).addListenerForSingleValueEvent(
//                                new ValueEventListener() {
//                                    @Override
//                                    public void onDataChange(DataSnapshot dataSnapshot) {
//                                        mTorihikisakibusyo = (Spinner) findViewById(R.id.torihikisakibusyospinner);
//                                        String busyoid = (String) mTorihikisakibusyo.getSelectedItem();
//
//                                        String id = dataSnapshot.getKey();
//                                        HashMap map = (HashMap)dataSnapshot.getValue();
//                                        ArrayList<Busyo> busyoArrayList = new ArrayList<Busyo>();
//                                        HashMap busyomap = (HashMap) map.get("busyo");
//
//                                                String busyoname = (String) map.get("busyoname");
//
//                                                badapter.add(busyoid);
//                                                badapter.notifyDataSetChanged();
//
//                                                TextView btextView = findViewById(R.id.torihikisakibusyotextview);
//                                                btextView.setText(busyoname);
//
//
//                                    }
//
//                                    @Override
//                                    public void onCancelled(DatabaseError databaseError) {
//
//                                    }
//                                }
//                        );
//
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> parent) {
//
//                    }
//                }
//        );


        //プロジェクト
        padapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        padapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mProjectArrayList = new ArrayList<Project>();
        mProject = (Spinner)findViewById(R.id.projectspinner);
        mProject.setAdapter(padapter);

        mProjectRef = mDatabaseReference.child(Const.ProjectPATH);
        mProjectRef.addChildEventListener(mProjectListener);

        mProjectTextView = (TextView)findViewById(R.id.projecttextview);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mProject.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) mProject.getSelectedItem();
                mDatabaseReference.child(Const.ProjectPATH).child(item).addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                HashMap map = (HashMap)dataSnapshot.getValue();

                                String busyo = (String) map.get("busyo");
                                String customer = (String) map.get("customer");
                                String name = (String) map.get("name");

                                TextView textView = findViewById(R.id.projecttextview);
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

        //システム名
        sadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        sadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSystemArrayList = new ArrayList<System>();
        mSystem = (Spinner)findViewById(R.id.systemspinner);
        mSystem.setAdapter(sadapter);

        mSystemTextView = (TextView) findViewById(R.id.systemtextview);

        mSagyouRef = mDatabaseReference.child(Const.SystemPATH);
        mSagyouRef.addChildEventListener(mSystemListener);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mSystem.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) mSystem.getSelectedItem();
                mDatabaseReference.child(Const.SystemPATH).child(item).addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                HashMap map = (HashMap)dataSnapshot.getValue();

                                String customer = (String) map.get("customer");
                                String name = (String) map.get("name");

                                TextView textView = findViewById(R.id.systemtextview);
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

        //集計分類
        sbadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        sbadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSyuukeibunruiArrayList = new ArrayList<Syuukeibunrui>();
        mSyuukeibunrui = (Spinner)findViewById(R.id.syuukeibunruispinner);
        mSyuukeibunrui.setAdapter(sbadapter);

        mSyuukeibunruiTextView = (TextView) findViewById(R.id.syuukeibunruitextview);

        mBunruiRef = mDatabaseReference.child(Const.BunruiPATH);
        mBunruiRef.addChildEventListener(mSyuukeibunruiListener);


        //作業区分
        skadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        skadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        mSagyoukubunArrayList = new ArrayList<Sagyoukubun>();
        mSagyoukubunn = (Spinner)findViewById(R.id.sagyoukubunnspinner);
        mSagyoukubunn.setAdapter(skadapter);

        mSagyoukubunTextView = (TextView) findViewById(R.id.sagyoukubunntextview);

        mSagyouRef = mDatabaseReference.child(Const.SagyouPATH);
        mSagyouRef.addChildEventListener(mSagyoukubunnListener);


        mJissekikousuuextView = (TextView) findViewById(R.id.jissekikousuuedittext);
        mJissekikousuuextView.setInputType(InputType.TYPE_CLASS_NUMBER);

        mHosyukousuuTextView = (TextView) findViewById(R.id.hosyukousuuedittext);
        mHosyukousuuTextView.setInputType(InputType.TYPE_CLASS_NUMBER);

        mNaiyouTextView = (EditText) findViewById(R.id.naiyouedittext);



        mYear = Integer.parseInt(mYearspinner.getSelectedItem().toString());
        mMonth = Integer.parseInt(mMonthspinner.getSelectedItem().toString());
        mDate=Integer.parseInt(mDayspinner.getSelectedItem().toString());



        //登録処理
        Button createButton = (Button) findViewById(R.id.createbutton);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mYear = Integer.parseInt(mYearspinner.getSelectedItem().toString());
                mMonth = Integer.parseInt(mMonthspinner.getSelectedItem().toString());
                mDate=Integer.parseInt(mDayspinner.getSelectedItem().toString());
                mIdSpinner = (Spinner) findViewById(R.id.syaincodespinner);
                String item = (String) mIdSpinner.getSelectedItem();
                mDatabaseReference = FirebaseDatabase.getInstance().getReference();
                mDeiryReportRef = mDatabaseReference.child(Const.NippouPath).child(item).child(String.valueOf(mYear)).child(String.valueOf(mMonth)).child(String.valueOf(mDate));

                mCust = mTorihikisaki.getSelectedItem().toString();
                mTorihikisakiname = mTorihikisakiTextView.getText().toString();

                mCustBusyo = mTorihikisakibusyo.getSelectedItem().toString();
                //mTorihikisakiBusyoname = mTorihikisakiBusyoTextView.getText().toString();

                mPro = Integer.parseInt(mProject.getSelectedItem().toString());
                mProjectname = mProjectTextView.getText().toString();

                mSys = Integer.parseInt(mSystem.getSelectedItem().toString());
                mSystemname = mSystemTextView.getText().toString();

                mSbunrui = mSyuukeibunrui.getSelectedItem().toString();
                mSagyou = mSagyoukubunn.getSelectedItem().toString();
                mHosyu = Integer.parseInt(mHosyukousuuTextView.getText().toString());
                mJisseki = Integer.parseInt(mJissekikousuuextView.getText().toString());
                mNaiyou = mNaiyouTextView.getText().toString();

                Map<String, String> data = new HashMap<String, String>();

                data.put("Customerid",String.valueOf(mCust));
                data.put("Customer",String.valueOf(mTorihikisakiname));

                data.put("CustomerBusyoId",String.valueOf(mCust));
                //data.put("CustomerBusyoname",String.valueOf(mCustBusyo));

                data.put("ProjectId",String.valueOf(mPro));
                data.put("ProjectName",String.valueOf(mProjectname));

                data.put("SystemId",String.valueOf(mSys));
                data.put("SystemName",String.valueOf(mSystemname));

                data.put("Sagyoubunrui",String.valueOf(mSbunrui));
                data.put("Sagyoukubun",String.valueOf(mSagyou));
                data.put("Hosyukousuu",String.valueOf(mHosyu));
                data.put("Jissekikousuu",String.valueOf(mJisseki));
                data.put("Naiyou",String.valueOf(mNaiyou));

                mDeiryReportRef.push().setValue(data);

                android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(DairyReport.this);
                alertDialogBuilder.setTitle("登録しました。");
                alertDialogBuilder.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent();
                                intent.putExtra("INPUT_STRING", mHosyukousuuTextView.toString());
                                setResult(RESULT_OK, intent);

                            }
                        });
                android.app.AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
                return;


            }
        });




    }

//    public boolean onKeyDown(int keyCode,KeyEvent event){
//        Intent intent = new Intent(getApplicationContext(),ChartMain.class);
//        mId = (String) mIdSpinner.getSelectedItem();
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

}


