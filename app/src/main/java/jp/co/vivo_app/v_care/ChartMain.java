package jp.co.vivo_app.v_care;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChartMain extends AppCompatActivity {

    private BarChart chart;
    private DialogFragment dialogFragment;
    private FragmentManager flagmentManager;

    DatabaseReference mDatabaseReference;
    private DatabaseReference mNippouRef;
    private TextView mHoshuText;

    private int mCyear;
    private int mCmonth;
    private int mCdate;
    private int mChour;
    private int mCminute;

    private int mThisYear,mThisMonth,mDiff;

    private String mId;
    private String mPassword;
    private String mName;
    private String mGroup;
    private boolean mAdminkengen;
    private String mBikou;

    private String mLabels;
    private String mLdate;

    private int mlabel,mHosyu;
    private String mHosyukousuu,mJissekikousuu,mCustomer;
    private String mPath,mYear,mMonth;
    private int mDate;
    private int mCYear, mCMonth, mCDay, mCHour, mCMinute;
    private int mThosyu = 0;
    private int mJisseki;
    private int mLastDayOfMonth;

    private int [] mKousuuArray;

    private Button mCreateButton,mLastmonthButton,mNextmonthButton,mDatechoiceButton;

    private ArrayList<Nippou> mNippouArrayList;
    private NippoListAdapter mAdapter;
    private ListView mListView;

    ArrayAdapter<String> dadapter;
    private Spinner mDatechiceSpinner;

    private static final int CHOOSER_REQUEST_CODE = 1;

    int a,z = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_main);

        chart = (BarChart) findViewById(R.id.bar_chart);
        mCreateButton = (Button) findViewById(R.id.createbutton);
        mDatechoiceButton = (Button) findViewById(R.id.datechoicebutton);
        mDatechiceSpinner = (Spinner) findViewById(R.id.thisdatespinner);

        Bundle extras = getIntent().getExtras();
        mId = extras.getString("mId");
        mPassword = extras.getString("mPassword");
        mName = extras.getString("mName");
        mGroup = extras.getString("mGroup");
        mAdminkengen = extras.getBoolean("mAdminkengen");
        mBikou = extras.getString("mBikou");

        Calendar cal = Calendar.getInstance();

        mCYear =cal.get(Calendar.YEAR);
        mCMonth =cal.get(Calendar.MONTH) + 1;
        mCDay =cal.get(Calendar.DATE);
        mCHour =cal.get(Calendar.HOUR);
        mCMinute = cal.get(Calendar.MINUTE);



        cal.set(Calendar.YEAR, mCyear);
        cal.set(Calendar.MONTH, mCmonth);
        int lastDayOfMonth = cal.getActualMaximum(Calendar.DATE);
        mKousuuArray = new int[lastDayOfMonth + 1];

        mThisMonth = mCMonth;
        mThisYear = mCYear;

        TextView tv = (TextView) findViewById(R.id.thismonthtextview);
        tv.setText(mThisYear + "/" + mThisMonth);



        dadapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        dadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner dspinner = (Spinner) findViewById(R.id.thisdatespinner);
        dspinner.setAdapter(dadapter);

        for(int i = 1; i <= 31; i++){
            dadapter.add(String.valueOf(i));
        }
        dspinner.setAdapter(dadapter);
        dspinner.setSelection(mDate-1);

        //前月
        mLastmonthButton = (Button) findViewById(R.id.lastmonthbutton);
        mLastmonthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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




                Calendar cal = Calendar.getInstance();

                cal.set(Calendar.YEAR, mThisYear);
                cal.set(Calendar.MONTH, mThisMonth);
                int lastDayOfMonth = cal.getActualMaximum(Calendar.DATE);

                //表示させるデータ
                ArrayList<BarEntry> entries = new ArrayList<>();

                for(int i = 0;i < lastDayOfMonth;i++){
                    entries.add(new BarEntry(i, 0));
                }

                List<IBarDataSet> bars = new ArrayList<>();
                BarDataSet dataSet = new BarDataSet(entries, "bar");


                //整数で表示
                dataSet.setValueFormatter(new IValueFormatter() {
                    @Override
                    public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                        return "" + (int) value;
                    }
                });
                //ハイライトさせない
                dataSet.setHighlightEnabled(false);

                //Barの色をセット
                dataSet.setColor(R.color.blueColor);
                bars.add(dataSet);

                darSet(2);
















                mDatabaseReference = FirebaseDatabase.getInstance().getReference();
                mNippouRef = mDatabaseReference.child(Const.NippouPath).child((mId)).child(String.valueOf(mThisYear)).child(String.valueOf(mThisMonth));
                mNippouRef.addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                //Map<String,Map<String,Map<String, String>>> map = (HashMap) dataSnapshot.getValue();
                                Map<String,Map<String,Map<String, String>>> map = (HashMap) dataSnapshot.getValue();

                                if(map != null){
                                    for (String date : map.keySet()) {
                                        mDate = Integer.parseInt(date);
                                        mThosyu = 0;
                                        mJissekikousuu = null;


                                        Calendar cal = Calendar.getInstance();

                                        cal.set(Calendar.YEAR, mThisYear);
                                        cal.set(Calendar.MONTH, mThisMonth);
                                        int lastDayOfMonth = cal.getActualMaximum(Calendar.DATE);
                                        Map<String,Map<String, String>> oneDayDataMap = (Map<String,Map<String, String>>) map.get(date);
                                        for (String oneDayDataKey: oneDayDataMap.keySet()){
                                            Map<String, String> oneValueMap = (Map<String, String>) oneDayDataMap.get(oneDayDataKey);
                                            mJissekikousuu = (String) oneValueMap.get("Jissekikousuu");
                                            mThosyu = Integer.parseInt(mJissekikousuu) + mThosyu;
                                            mKousuuArray[mDate] = mThosyu;
                                        }

                                    }

                                    darSet(1);
                                }


                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        }
                );




            }
        });

        //翌月
        mNextmonthButton = (Button) findViewById(R.id.nextmonthbutton);
        mNextmonthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
            }
        });

        mAdapter = new NippoListAdapter(this);
        mListView = (ListView) findViewById(R.id.listview);
        mNippouArrayList= new ArrayList<Nippou>();


        //工数ゲット
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mNippouRef = mDatabaseReference.child(Const.NippouPath).child(mId);
        mNippouRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        HashMap<String,HashMap<String,String>> map = (HashMap) dataSnapshot.getValue();
                        if (map != null){
                            for (String year : map.keySet()){
                                mYear = year;
                                mNippouRef = mDatabaseReference.child(Const.NippouPath).child(mId).child(String.valueOf(mThisYear));
                                mNippouRef.addListenerForSingleValueEvent(
                                        new ValueEventListener() {
                                            @Override
                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                HashMap<String,HashMap<String,String>> map = (HashMap) dataSnapshot.getValue();
                                                if (map != null){
                                                    for (String month :map.keySet()){
                                                        mMonth = month;
                                                        mNippouRef = mDatabaseReference.child(Const.NippouPath).child((mId)).child(String.valueOf(mThisYear)).child(String.valueOf(mThisMonth));
                                                        mNippouRef.addListenerForSingleValueEvent(
                                                                new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                                                        Map<String,Map<String,Map<String, String>>> map = (HashMap) dataSnapshot.getValue();

                                                                        if(map != null){
                                                                            for (String date : map.keySet()) {
                                                                                mDate = Integer.parseInt(date);
                                                                                mThosyu = 0;
                                                                                Map<String,Map<String, String>> oneDayDataMap = (Map<String,Map<String, String>>) map.get(date);
                                                                                for (String oneDayDataKey: oneDayDataMap.keySet()){
                                                                                    Map<String, String> oneValueMap = (Map<String, String>) oneDayDataMap.get(oneDayDataKey);
                                                                                    mJissekikousuu = (String) oneValueMap.get("Jissekikousuu");
                                                                                    mThosyu = Integer.parseInt(mJissekikousuu) + mThosyu;
                                                                                    mKousuuArray[mDate] = mThosyu;
                                                                                }

                                                                            }
                                                                            darSet(1);



                                                                        }


                                                                    }

                                                                    @Override
                                                                    public void onCancelled(DatabaseError databaseError) {

                                                                    }
                                                                }
                                                        );
                                                    }
                                                }

                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        }
                                );
                            }
                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                }
        );


        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),DairyReport.class);
                intent.putExtra("mId",mId);
                intent.putExtra("mPassword",mPassword);
                intent.putExtra("mName",mName);
                intent.putExtra("mGroup",mGroup);
                intent.putExtra("mAdminkengen",mAdminkengen);
                intent.putExtra("mBikou",mBikou);
                startActivity(intent);
                return;

            }
        });

        mDatechoiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mNippouArrayList.clear();
                mAdapter.setNippouArrayList(mNippouArrayList);
                mListView.setAdapter(mAdapter);

                mDate=Integer.parseInt(mDatechiceSpinner.getSelectedItem().toString());
                mDatabaseReference.child(Const.NippouPath).child(mId).child(String.valueOf(mThisYear)).child(String.valueOf(mThisMonth)).child(String.valueOf(mDate)).addChildEventListener(
                        new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                HashMap map = (HashMap) dataSnapshot.getValue();
                                Map<String,Object> data = new HashMap<>();

                                String systemid = (String) map.get("SystemId");
                                String system = (String) map.get("SystemName");
                                String customerid = (String) map.get("Customerid");
                                String customer = (String) map.get("Customer");
                                String cbusyoid = (String) map.get("CustomerBusyoId");
                                String projectid = (String) map.get("ProjectId");
                                String project = (String) map.get("ProjectName");
                                String bunrui = (String) map.get("Sagyoubunrui");
                                String sagyoukubun = (String) map.get("Sagyoukubun");
                                mHosyukousuu = (String) map.get("Hosyukousuu");
                                mHosyu = Integer.parseInt(mHosyukousuu);
                                mJissekikousuu = (String) map.get("Jissekikousuu");
                                mJisseki = Integer.parseInt(mJissekikousuu);
                                String naiyou = (String) map.get("Naiyou");

                                Nippou nippou = new Nippou(mThisYear,mThisMonth,mDate,mId,systemid,system,customerid,customer,cbusyoid,projectid,project,bunrui,sagyoukubun,mJisseki,mHosyu,naiyou);


                                mNippouArrayList.add(nippou);
                                mAdapter.setNippouArrayList(mNippouArrayList);
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



    }

        private void darSet(int a){

            if (a == 1){
                //表示データ取得
                BarData data = new BarData(getBarData(1));
                chart.setData(data);
            }else{
                BarData data = new BarData(getBarData(2));
                chart.setData(data);
            }


            Calendar cal = Calendar.getInstance();

            cal.set(Calendar.YEAR, mThisYear);
            cal.set(Calendar.MONTH, mThisMonth);
            int lastDayOfMonth = cal.getActualMaximum(Calendar.DATE);

            //Y軸(左)
            YAxis left = chart.getAxisLeft();
            left.setAxisMinimum(0);
            left.setAxisMaximum(30);
            left.setLabelCount(5);
            left.setDrawTopYLabelEntry(true);
            //整数表示に
            left.setValueFormatter(new IAxisValueFormatter() {
                @Override
                public String getFormattedValue(float value, AxisBase axis) {
                    return "" + (int)value;
                }
            });

            //Y軸(右)
            YAxis right = chart.getAxisRight();
            right.setDrawLabels(false);
            right.setDrawGridLines(false);
            right.setDrawZeroLine(true);
            right.setDrawTopYLabelEntry(true);

            cal.set(Calendar.YEAR, mThisYear);
            cal.set(Calendar.MONTH, mThisMonth);


            String[] mLabels = new String[lastDayOfMonth + 1];
            mLabels[0]= "";
            XAxis xAxis = chart.getXAxis();
            xAxis.setTextColor(Color.BLACK);
            //x軸のラベルをいくつ表示するか
            xAxis.setLabelCount(15);


            for(int i = 1; i <= lastDayOfMonth; i++) {
                mLabels[i] = String.valueOf(i + "日");
            }
            xAxis.setValueFormatter(new IndexAxisValueFormatter(mLabels));
            XAxis bottomAxis = chart.getXAxis();
            bottomAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            bottomAxis.setDrawLabels(true);
            bottomAxis.setDrawGridLines(false);
            bottomAxis.setDrawAxisLine(true);

            //グラフ上の表示
            chart.setDrawValueAboveBar(true);
            chart.getDescription().setEnabled(false);
            chart.setClickable(false);

            //凡例
            chart.getLegend().setEnabled(false);

            chart.setScaleEnabled(false);
            //アニメーション
            chart.animateY(500, Easing.EasingOption.Linear);

        }


    //棒グラフのデータを取得
    private List<IBarDataSet> getBarData(int z){

        Calendar cal = Calendar.getInstance();

        cal.set(Calendar.YEAR, mThisYear);
        cal.set(Calendar.MONTH, mThisMonth);
        int lastDayOfMonth = cal.getActualMaximum(Calendar.DATE);

        if (z == 1){
            //表示させるデータ
            ArrayList<BarEntry> entries = new ArrayList<>();


            for(int i = 0;i < lastDayOfMonth;i++){
                entries.add(new BarEntry(i, mKousuuArray[i]));
            }

            List<IBarDataSet> bars = new ArrayList<>();
            BarDataSet dataSet = new BarDataSet(entries, "bar");

            //整数で表示
            dataSet.setValueFormatter(new IValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    return "" + (int) value;
                }
            });
            //ハイライトさせない
            dataSet.setHighlightEnabled(false);

            //Barの色をセット
            dataSet.setColor(R.color.blueColor);
            bars.add(dataSet);

            return bars;

        }else{
            //表示させるデータ
            ArrayList<BarEntry> entries = new ArrayList<>();
            entries.clear();


            for(int i = 0;i < lastDayOfMonth;i++){
                entries.add(new BarEntry(i, mKousuuArray[0]));
            }

            List<IBarDataSet> bars = new ArrayList<>();
            BarDataSet dataSet = new BarDataSet(entries, "bar");

            //整数で表示
            dataSet.setValueFormatter(new IValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    return "" + (int) value;
                }
            });
            //ハイライトさせない
            dataSet.setHighlightEnabled(false);

            //Barの色をセット
            dataSet.setColor(R.color.blueColor);
            bars.add(dataSet);

            return bars;
        }








    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Intent intent = new Intent();

    }

}
