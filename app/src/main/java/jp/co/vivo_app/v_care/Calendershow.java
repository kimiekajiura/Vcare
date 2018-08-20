package jp.co.vivo_app.v_care;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.FragmentManager;
import android.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

public class Calendershow extends DialogFragment{

    private static final String TAG = "CalenderMainActivity";
    private Calendar currentCalender = Calendar.getInstance(Locale.getDefault());
    private SimpleDateFormat dateFormatForDisplaying = new SimpleDateFormat("dd-M-yyyy hh:mm:ss a", Locale.getDefault());
    private SimpleDateFormat dateFormatForMonth = new SimpleDateFormat("MMM - yyyy", Locale.getDefault());
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MM", Locale.getDefault());
    private SimpleDateFormat dateFormatYear = new SimpleDateFormat("yyyy", Locale.getDefault());
    private SimpleDateFormat dateFormatDate = new SimpleDateFormat("dd",Locale.getDefault());
    private boolean shouldShow = false;
    private CompactCalendarView compactCalendarView;
    private ActionBar toolbar;
    DatabaseReference mDatabaseReference;
    private DatabaseReference mUserRef;
    private DatabaseReference mCalenderRef;

    private AlertDialog.Builder alert;

    private String mId;
    private int mYear;
    private int mMonth;
    private int mDate;
    private int mDiff;

    private Spinner mYearspinner;
    private Spinner mMonthspinner;
    private Spinner mDayspinner;
    private Spinner mSyaincodespinner;
    private Spinner mIdSpinner;

    private Button mEditbutton;
    private Button mDeletebutton;

    private ListView mListView;

    ArrayAdapter<String> yadapter;
    ArrayAdapter<String> madapter;
    ArrayAdapter<String> dadapter;
    ArrayAdapter<String> adapter;
    ArrayList<Syain> mSyainArrayList;

    private DialogFragment dialogFragment;
    private FragmentManager flagmentManager;

    private int mTargetMonth;
    private int mTargetYear;
    private int mTargetDate;

    private CalenderListAdapter mAdapter;
    private ArrayList<Calender> mCalenderArrayList;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        //View mainTabView = getActivity().getLayoutInflater().inflate(R.layout.main_tab, null);
        View mainTabView = inflater.inflate(R.layout.main_tab,container,false);

        mListView = (ListView) mainTabView.findViewById(R.id.bookings_listview);
        mAdapter = new CalenderListAdapter(getActivity());
        mCalenderArrayList= new ArrayList<Calender>();

        mEditbutton = (Button) mainTabView.findViewById(R.id.edit_button);
        mDeletebutton = (Button) mainTabView.findViewById(R.id.delete_button);

        mEditbutton.setVisibility(mainTabView.INVISIBLE);
        mDeletebutton.setVisibility(mainTabView.INVISIBLE);

        alert = new AlertDialog.Builder(getActivity());
        Bundle bundle = getArguments();

        mId = bundle.getString("mId");
        mYear = bundle.getInt("mYear");
        mMonth = bundle.getInt("mMonth");
        mDate = bundle.getInt("mDate");

        mSyaincodespinner = (Spinner) mainTabView.findViewById(R.id.syainchoicespinner);
        mYearspinner = (Spinner) mainTabView.findViewById(R.id.yearspinner);
        mMonthspinner = (Spinner) mainTabView.findViewById(R.id.monthspinner);

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

        //年スピナー
        yadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item);
        yadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner yspinner = (Spinner) mainTabView.findViewById(R.id.yearspinner);
        yspinner.setAdapter(yadapter);

        //月スピナー
        madapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item);
        madapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        Spinner mspinner = (Spinner) mainTabView.findViewById(R.id.monthspinner);
        mspinner.setAdapter(madapter);

        //return;
        for(int i = mYear - 1; i <= mYear; i++){
            yadapter.add(String.valueOf(i));
        }

        for(int i = 1; i <= 12; i++){
            madapter.add(String.valueOf(i));
        }

        //アダプターをセットする
        yspinner.setAdapter(yadapter);
        mspinner.setAdapter(madapter);

        //スピナーの初期値を現在の日付にする
        yspinner.setSelection(1);
        mspinner.setSelection(mMonth);

        mSyainArrayList = new ArrayList<Syain>();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = (Spinner) mainTabView.findViewById(R.id.syaincodespinner);
        spinner.setAdapter(adapter);

        mUserRef = mDatabaseReference.child(Const.UserPATH);
        mUserRef.addChildEventListener(mUserListener);

        final List<String> mutableBookings = new ArrayList<>();

        final ListView bookingsListView = mainTabView.findViewById(R.id.bookings_listview);
        final Button showPreviousMonthBut = mainTabView.findViewById(R.id.prev_button);
        final Button showNextMonthBut = mainTabView.findViewById(R.id.next_button);

        //予定登録画面
        final Button create_button = mainTabView.findViewById(R.id.create_button);

        //final ArrayAdapter adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, mutableBookings);
        //bookingsListView.setAdapter(adapter);
        compactCalendarView = mainTabView.findViewById(R.id.compactcalendar_view);

        // below allows you to configure color for the current day in the month
        // compactCalendarView.setCurrentDayBackgroundColor(getResources().getColor(R.color.black));
        // below allows you to configure colors for the current day the user has selected
        // compactCalendarView.setCurrentSelectedDayBackgroundColor(getResources().getColor(R.color.dark_red));
        compactCalendarView.setUseThreeLetterAbbreviation(false);
        compactCalendarView.setFirstDayOfWeek(Calendar.MONDAY);
        compactCalendarView.setIsRtl(false);
        compactCalendarView.displayOtherMonthDays(false);
        //compactCalendarView.setIsRtl(true);
        loadEvents();
        loadEventsForYear(2018);
        compactCalendarView.invalidate();

        logEventsByMonth(compactCalendarView);

        // below line will display Sunday as the first day of the week
        // compactCalendarView.setShouldShowMondayAsFirstDay(false);

        // disable scrolling calendar
        // compactCalendarView.shouldScrollMonth(false);

        // show days from other months as greyed out days
        // compactCalendarView.displayOtherMonthDays(true);

        // show Sunday as first day of month
        // compactCalendarView.setShouldShowMondayAsFirstDay(false);

        //set initial title
        //toolbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        //toolbar.setTitle(dateFormatForMonth.format(compactCalendarView.getFirstDayOfCurrentMonth()));

        //set title on calendar scroll
        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                //toolbar.setTitle(dateFormatForMonth.format(dateClicked));
                //List<Event> bookingsFromMap = compactCalendarView.getEvents(dateClicked);
                //月の特定：dateFormatMonth.format(dateClicked)
                //年の特定：dateFormatYear.format(dateClicked)
                mTargetMonth=Integer.parseInt(dateFormatMonth.format(dateClicked));
                mTargetYear=Integer.parseInt(dateFormatYear.format(dateClicked));
                mTargetDate =Integer.parseInt(dateFormatDate.format(dateClicked));

                //一覧表示
                mDatabaseReference=FirebaseDatabase.getInstance().getReference();
                mCalenderRef=mDatabaseReference.child(Const.CalenderPATH).child(String.valueOf(mTargetYear)).child(String.valueOf(mTargetMonth)).child(String.valueOf(mTargetDate)).child(mId);
                mCalenderRef.addChildEventListener(
                        new ChildEventListener() {
                            @Override
                            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                HashMap map = (HashMap) dataSnapshot.getValue();
                                Map<String,Object> data = new HashMap<>();

                                String  stime = (String) map.get("開始時間");
                                String etime = (String) map.get("終了時間");
                                String title = (String) map.get("タイトル");
                                String detail = (String) map.get("詳細");
                                String ttime = (String) map.get("予定登録時間");

                                View view = getActivity().getLayoutInflater().inflate(R.layout.main_tab, null);

                                mListView = (ListView) view.findViewById(R.id.bookings_listview);
                                mAdapter = new CalenderListAdapter(getActivity());
                                mCalenderArrayList= new ArrayList<Calender>();

                                Calender calender = new Calender(mTargetYear,mTargetMonth,mTargetDate,stime,etime,title,detail,mId);

                                mCalenderArrayList.add(calender);
                                mAdapter.setCalenderArrayList(mCalenderArrayList);
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

                /*
                if (bookingsFromMap != null) {
                    Log.d(TAG, bookingsFromMap.toString());
                    mutableBookings.clear();
                    for (Event booking : bookingsFromMap) {
                        mutableBookings.add((String) booking.getData());
                    }
                    adapter.notifyDataSetChanged();
                }
                */

            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                //toolbar.setTitle(dateFormatForMonth.format(firstDayOfNewMonth));

            }
        });

        showPreviousMonthBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compactCalendarView.scrollLeft();

                View mainTabView = getActivity().getLayoutInflater().inflate(R.layout.main_tab, null);
                Spinner ys = (Spinner)mainTabView.findViewById(R.id.yearspinner);
                Spinner ms = (Spinner)mainTabView.findViewById(R.id.monthspinner);

                //年スピナー
                yadapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item);
                yadapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ys.setAdapter(yadapter);

                //月スピナー
                madapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item);
                madapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                ms.setAdapter(madapter);

                //return;
                for(int i = mYear - 1 ; i <= mYear; i++){
                    yadapter.add(String.valueOf(i));
                }

                for(int i = 1; i <= 12; i++){
                    madapter.add(String.valueOf(i));
                }



                //前月
                mDiff = (mMonth) -1;
                if (mDiff == 0){
                    //ys.setSelection(-1);
                    mDiff = 12;
                    mMonth = mDiff;
                }else{
                    mMonth = mDiff;
                }

                ys.setAdapter(yadapter);
                ms.setAdapter(madapter);

                ys.setSelection(yadapter.getPosition(String.valueOf(mYear)));
                ms.setSelection(madapter.getPosition(String.valueOf(mMonth)));
            }
        });


        showNextMonthBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                compactCalendarView.scrollRight();

                //翌月
                mDiff = (mMonth) + 1;

                if (mDiff == 13){
                    mYear = mYear + 1;
                    mDiff = 1;
                    mMonth = mDiff;
                }else{
                    mMonth = mDiff;
                }
                View mainTabView = getActivity().getLayoutInflater().inflate(R.layout.main_tab, null);
                Spinner ys = (Spinner)mainTabView.findViewById(R.id.yearspinner);
                Spinner ms = (Spinner)mainTabView.findViewById(R.id.monthspinner);

                ys.setSelection(mYear);
                ms.setSelection(mMonth);
            }
        });

        create_button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                flagmentManager = getFragmentManager();
                dialogFragment = new CalenderEdit();

                Bundle bundle = new Bundle();
                bundle.putString("mId",mId);
                bundle.putInt("mLyear",mTargetYear);
                bundle.putInt("mLmonth",mTargetMonth);
                bundle.putInt("mLdate",mTargetDate);

                dialogFragment.setArguments(bundle);
                dialogFragment.show(flagmentManager,"calendaredit");
                return;


            }
        });

        compactCalendarView.setAnimationListener(new CompactCalendarView.CompactCalendarAnimationListener() {
            @Override
            public void onOpened() {
            }

            @Override
            public void onClosed() {
            }
        });

        // uncomment below to show indicators above small indicator events
        // compactCalendarView.shouldDrawIndicatorsBelowSelectedDays(true);

        // uncomment below to open onCreate
        //openCalendarOnCreate(v);


        return mainTabView;
    }

    @NonNull
    private View.OnClickListener getCalendarShowLis() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!compactCalendarView.isAnimating()) {
                    if (shouldShow) {
                        compactCalendarView.showCalendar();
                    } else {
                        compactCalendarView.hideCalendar();
                    }
                    shouldShow = !shouldShow;
                }
            }
        };
    }

    @NonNull
    private View.OnClickListener getCalendarExposeLis() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!compactCalendarView.isAnimating()) {
                    if (shouldShow) {
                        compactCalendarView.showCalendarWithAnimation();
                    } else {
                        compactCalendarView.hideCalendarWithAnimation();
                    }
                    shouldShow = !shouldShow;
                }
            }
        };
    }

    private void openCalendarOnCreate(View v) {
        final RelativeLayout layout = v.findViewById(R.id.main_content);
        ViewTreeObserver vto = layout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (Build.VERSION.SDK_INT < 16) {
                    layout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                } else {
                    layout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                compactCalendarView.showCalendarWithAnimation();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        //toolbar.setTitle(dateFormatForMonth.format(compactCalendarView.getFirstDayOfCurrentMonth()));
        // Set to current day on resume to set calendar to latest day
        // toolbar.setTitle(dateFormatForMonth.format(new Date()));
    }

    private void loadEvents() {
        addEvents(-1, -1);
        addEvents(Calendar.DECEMBER, -1);
        addEvents(Calendar.AUGUST, -1);
    }

    private void loadEventsForYear(int year) {
        addEvents(Calendar.DECEMBER, year);
        addEvents(Calendar.AUGUST, year);
    }

    private void logEventsByMonth(CompactCalendarView compactCalendarView) {
        currentCalender.setTime(new Date());
        currentCalender.set(Calendar.DAY_OF_MONTH, 1);
        currentCalender.set(Calendar.MONTH, Calendar.AUGUST);
        List<String> dates = new ArrayList<>();
        for (Event e : compactCalendarView.getEventsForMonth(new Date())) {
            dates.add(dateFormatForDisplaying.format(e.getTimeInMillis()));
        }
        Log.d(TAG, "Events for Aug with simple date formatter: " + dates);
        Log.d(TAG, "Events for Aug month using default local and timezone: " + compactCalendarView.getEventsForMonth(currentCalender.getTime()));
    }

    private void addEvents(int month, int year) {
        currentCalender.setTime(new Date());
        currentCalender.set(Calendar.DAY_OF_MONTH, 1);
        Date firstDayOfMonth = currentCalender.getTime();
        for (int i = 0; i < 6; i++) {
            currentCalender.setTime(firstDayOfMonth);
            if (month > -1) {
                currentCalender.set(Calendar.MONTH, month);
            }
            if (year > -1) {
                currentCalender.set(Calendar.ERA, GregorianCalendar.AD);
                currentCalender.set(Calendar.YEAR, year);
            }
            currentCalender.add(Calendar.DATE, i);
            setToMidnight(currentCalender);
            long timeInMillis = currentCalender.getTimeInMillis();

            List<Event> events = getEvents(timeInMillis, i);

            compactCalendarView.addEvents(events);
        }
    }


    private List<Event> getEvents(long timeInMillis, int day) {
        if (day < 2) {
            return Arrays.asList(new Event(Color.argb(255, 169, 68, 65), timeInMillis, "Event at " + new Date(timeInMillis)));
        } else if ( day > 2 && day <= 4) {
            return Arrays.asList(
                    new Event(Color.argb(255, 169, 68, 65), timeInMillis, "Event at " + new Date(timeInMillis)),
                    new Event(Color.argb(255, 100, 68, 65), timeInMillis, "Event 2 at " + new Date(timeInMillis)));
        } else {
            return Arrays.asList(
                    new Event(Color.argb(255, 169, 68, 65), timeInMillis, "Event at " + new Date(timeInMillis) ),
                    new Event(Color.argb(255, 100, 68, 65), timeInMillis, "Event 2 at " + new Date(timeInMillis)),
                    new Event(Color.argb(255, 70, 68, 65), timeInMillis, "Event 3 at " + new Date(timeInMillis)));
        }
    }

    private void setToMidnight(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
    }
}