package jp.co.vivo_app.v_care;

import android.app.DialogFragment;
import android.app.FragmentManager;
import android.content.Intent;
import android.graphics.Color;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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
    private Button mCreateButton;
    private DialogFragment dialogFragment;
    private FragmentManager flagmentManager;

    DatabaseReference mDatabaseReference;
    private DatabaseReference mNippouRef;

    private int mCyear;
    private int mCmonth;
    private int mCdate;
    private int mChour;
    private int mCminute;

    private String mId;
    private String mLabels;
    private String mLdate;

    private int mlabel,mHosyu;
    private String mHosyukousuu,mCustomer;
    private String mPath,mYear,mMonth;
    private int mDate;
    private int mCYear, mCMonth, mCDay, mCHour, mCMinute;
    private int mThosyu = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart_main);

        chart = (BarChart) findViewById(R.id.bar_chart);
        mCreateButton = (Button) findViewById(R.id.createbutton);


        Bundle extras = getIntent().getExtras();
        mId = extras.getString("mId");

        Calendar cal = Calendar.getInstance();

        mCYear =cal.get(Calendar.YEAR);
        mCMonth =cal.get(Calendar.MONTH) + 1;
        mCDay =cal.get(Calendar.DATE);
        mCHour =cal.get(Calendar.HOUR);
        mCMinute = cal.get(Calendar.MINUTE);

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
                                                    mNippouRef = mDatabaseReference.child(Const.NippouPath).child(mId).child(String.valueOf(mCYear));
                                                    mNippouRef.addListenerForSingleValueEvent(
                                                            new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                    HashMap<String,HashMap<String,String>> map = (HashMap) dataSnapshot.getValue();
                                                                    if (map != null){
                                                                        for (String month :map.keySet()){
                                                                            mMonth = month;
                                                                            mNippouRef = mDatabaseReference.child(Const.NippouPath).child((mId)).child(String.valueOf(mCYear)).child(String.valueOf(mCMonth));
                                                                            mNippouRef.addListenerForSingleValueEvent(
                                                                                    new ValueEventListener() {
                                                                                        @Override
                                                                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                            HashMap<String,HashMap<String,String>> map = (HashMap) dataSnapshot.getValue();

                                                                                            if(map != null){
                                                                                                for (String date : map.keySet()) {
                                                                                                    mDate = Integer.parseInt(date);

//                                                                                                    Calendar cal = Calendar.getInstance();
//                                                                                                    mCYear =cal.get(Calendar.YEAR);
//                                                                                                    mCMonth =cal.get(Calendar.MONTH) + 1;
//                                                                                                    mCDay =cal.get(Calendar.DATE);
//
//                                                                                                    cal.set(Calendar.YEAR, mCYear);
//                                                                                                    cal.set(Calendar.MONTH, mCMonth);
//                                                                                                    int lastDayOfMonth = cal.getActualMaximum(Calendar.DATE);
//                                                                                                    int[] kosuArray = new int[lastDayOfMonth + 1];
//
//                                                                                                    kosuArray[mDate] = kosuArray[mDate] + 10;
//


//                                                                                                    for (int i = 1;i < lastDayOfMonth;i++){
//                                                                                                        kosuArray[i] = i;
//                                                                                                        if (mDate == i){
//                                                                                                            mHosyukousuu = map.get(mDate).get("Hosyukousuu");
//                                                                                                        }
//
//                                                                                                    }

                                                                                                    mNippouRef = mDatabaseReference.child(Const.NippouPath).child(mId).child(String.valueOf(mCYear)).child(String.valueOf(mCMonth)).child(String.valueOf(mDate));
                                                                                                            mNippouRef.addChildEventListener(
                                                                                                                    new ChildEventListener() {
                                                                                                                        @Override
                                                                                                                        public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                                                                                                            HashMap map = (HashMap) dataSnapshot.getValue();
                                                                                                                            mCustomer = (String) map.get("Customer");
                                                                                                                            mHosyukousuu = (String) map.get("Hosyukousuu");
                                                                                                                            mThosyu = Integer.parseInt(mHosyukousuu) + mThosyu;
                                                                                                                            Log.d("vcare",String.valueOf(mDate));
                                                                                                                            Log.d("vcare",String.valueOf(mThosyu));
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
                startActivity(intent);
                return;

            }
        });


        //表示データ取得
        BarData data = new BarData(getBarData());
        chart.setData(data);

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

        cal.set(Calendar.YEAR, mCyear);
        cal.set(Calendar.MONTH, mCmonth);
        int lastDayOfMonth = cal.getActualMaximum(Calendar.DATE);

        String[] mLabels = new String[lastDayOfMonth + 1];
        mLabels[0]= "";
        XAxis xAxis = chart.getXAxis();

        for(int i = 1; i <= lastDayOfMonth; i++) {
            mLabels[i] = String.valueOf(i + "日");
        }
        xAxis.setValueFormatter(new IndexAxisValueFormatter(mLabels));
            XAxis bottomAxis = chart.getXAxis();
            bottomAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            bottomAxis.setDrawLabels(true);
            bottomAxis.setDrawGridLines(false);
            bottomAxis.setDrawAxisLine(true);



//        mHosyukousuu = 0;
//        mDatabaseReference= FirebaseDatabase.getInstance().getReference();
//        mDatabaseReference.child(Const.NippouPath).child(mId).child(String.valueOf(mCyear)).child(String.valueOf(mCmonth)).addChildEventListener(
//                new ChildEventListener() {
//                    @Override
//                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                        Calendar cal = Calendar.getInstance();
//
//                        mCyear =cal.get(Calendar.YEAR);
//                        mCmonth =cal.get(Calendar.MONTH) + 1;
//                        mCdate =cal.get(Calendar.DATE);
//                        mChour =cal.get(Calendar.HOUR);
//                        mCminute = cal.get(Calendar.MINUTE);
//
//                        cal.set(Calendar.YEAR, mCyear);
//                        cal.set(Calendar.MONTH, mCmonth);
//                        int lastDayOfMonth = cal.getActualMaximum(Calendar.DATE);
//
//                        for(int i = 1; i <= lastDayOfMonth; i++) {
//                            mlabel[ i - 1] = i;
//
//                            HashMap map = (HashMap) dataSnapshot.getValue();
//                            mLdate = dataSnapshot.getKey();
//
//                            //データがある
////                            if (String.valueOf(i).equals(mLdate)) {
////                                String hkousuu = (String) map.get("Hosyukousuu");
////                                if (hkousuu.equals(0)) {
////
////                                } else {
////                                    //mHosyukousuu = hkousuu + 1;
////                                }
////                            //データ無し
////                            } else {
////
////                            }
//
//                        }
//                        Log.d("vcare",mlabel);
//
//                    }
//
//                    @Override
//                    public void onChildChanged(DataSnapshot dataSnapshot, String s) {
//
//                    }
//
//                    @Override
//                    public void onChildRemoved(DataSnapshot dataSnapshot) {
//
//                    }
//
//                    @Override
//                    public void onChildMoved(DataSnapshot dataSnapshot, String s) {
//
//                    }
//
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//
//                    }
//                }
//        );



        //グラフ上の表示
        chart.setDrawValueAboveBar(true);
        chart.getDescription().setEnabled(false);
        chart.setClickable(false);

        //凡例
        chart.getLegend().setEnabled(false);

        chart.setScaleEnabled(false);
        //アニメーション
        chart.animateY(1200, Easing.EasingOption.Linear);
    }

    //棒グラフのデータを取得
    private List<IBarDataSet> getBarData(){
        //表示させるデータ
        ArrayList<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(1, 10));
        entries.add(new BarEntry(2, 15));
        entries.add(new BarEntry(3, 10));
        entries.add(new BarEntry(4, 12));
        entries.add(new BarEntry(5, 18));
        entries.add(new BarEntry(6, 8));
        entries.add(new BarEntry(7, 13));
        entries.add(new BarEntry(8, 10));
        entries.add(new BarEntry(9, 25));
        entries.add(new BarEntry(10, 22));
        entries.add(new BarEntry(11, 10));
        entries.add(new BarEntry(12, 24));
        entries.add(new BarEntry(13, 11));
        entries.add(new BarEntry(14, 8));
        entries.add(new BarEntry(15, 20));
        entries.add(new BarEntry(16, 22));
        entries.add(new BarEntry(17, 16));
        entries.add(new BarEntry(18, 11));
        entries.add(new BarEntry(19, 10));
        entries.add(new BarEntry(20, 15));
        entries.add(new BarEntry(21, 10));
        entries.add(new BarEntry(22, 12));
        entries.add(new BarEntry(23, 18));
        entries.add(new BarEntry(24, 8));
        entries.add(new BarEntry(25, 13));
        entries.add(new BarEntry(26, 10));
        entries.add(new BarEntry(27, 25));
        entries.add(new BarEntry(28, 22));
        entries.add(new BarEntry(29, 10));
        entries.add(new BarEntry(30, 24));
        entries.add(new BarEntry(31, 11));

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
        dataSet.setColors(new int[]{R.color.colorPrimaryDark, R.color.blueColor, R.color.colorPrimaryDark,R.color.blueColor, R.color.colorPrimaryDark, R.color.blueColor,R.color.colorPrimaryDark, R.color.blueColor, R.color.colorPrimaryDark,R.color.blueColor, R.color.colorPrimaryDark, R.color.blueColor,R.color.colorPrimaryDark, R.color.blueColor, R.color.colorPrimaryDark,R.color.blueColor, R.color.colorPrimaryDark, R.color.blueColor,R.color.colorPrimaryDark, R.color.blueColor, R.color.colorPrimaryDark,R.color.blueColor, R.color.colorPrimaryDark, R.color.blueColor,R.color.colorPrimaryDark, R.color.blueColor, R.color.colorPrimaryDark,R.color.blueColor, R.color.colorPrimaryDark, R.color.blueColor}, this);
        bars.add(dataSet);

        return bars;

    }
}
