package jp.co.vivo_app.v_care;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;

import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import static android.content.Context.ALARM_SERVICE;
import static android.content.Context.HARDWARE_PROPERTIES_SERVICE;

public class TaskAlarmReceiver extends BroadcastReceiver {

    DatabaseReference mDatabaseReference;
    private DatabaseReference mCalenderRef;

    private String mStime;
    private String mEtime;
    private String mTitle;
    private String mDetail;
    private String mKey;
    private String mEcdate;

    private String mItitle;
    private String mIdetail;

    private Date mTdate,mTmdate;
    private int mCYear, mCMonth, mCDay, mCHour, mCMinute;
    private int mTyear,mTmonth,mTddate;

    private int mAlermCH = 0;

    private String mPath,mId,mYear,mMonth,mDate;

    @Override
    public void onReceive(final Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= 26) {
            NotificationChannel channel = new NotificationChannel("default",
                    "Channel name",
                            NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(("channel description"));
            notificationManager.createNotificationChannel(channel);
        }

        // 通知の設定を行う
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default");
        //ステータスバー表示のアイコンリソース
        builder.setSmallIcon(R.drawable.droid);

        //通知に表示される大きなアイコンをbitmapで指定。
        //指定が無い場合はsetSmalliconメソッドで指定したリソース
        builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.droid_large));

        //いつ
        builder.setWhen(SystemClock.uptimeMillis());

        //通知時の音・バイブ・ライトの指定
        builder.setDefaults(Notification.DEFAULT_ALL);

        //true：ユーザーがタップしたら消える。falseの時は、コードで消す必要あり
        builder.setAutoCancel(true);

        mKey = intent.getStringExtra("alarm");
        int year = intent.getIntExtra("year",0);
        int month = intent.getIntExtra("month",0);
        int date = intent.getIntExtra("date",0);
        String id = intent.getStringExtra("id");
        mItitle = intent.getStringExtra("title");
        mIdetail = intent.getStringExtra("detail");

        //再起動時のアラーム準備
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction())) {
            //GregorianCalendar calendar = new GregorianCalendar(mCYear,mCMonth - 1,mCDay,mCHour,mCMinute);
            mDatabaseReference= FirebaseDatabase.getInstance().getReference();
            mCalenderRef=mDatabaseReference.child(Const.CalenderPATH);
            mCalenderRef.addListenerForSingleValueEvent(
                    new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            Log.d("tuuti","tuuti3");
                            HashMap<String,HashMap<String,String>> map = (HashMap) dataSnapshot.getValue();
                            //mapの中身全て見える
                            //keyに社員IDを取得
                            for (String mid : map.keySet()){
                                mId = mid;
                                Log.d("tuuti",String.valueOf(mId));
                                mCalenderRef=mDatabaseReference.child(Const.CalenderPATH).child(mId);
                                mCalenderRef.addListenerForSingleValueEvent(
                                    new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            HashMap<String,HashMap<String,String>> map = (HashMap) dataSnapshot.getValue();
                                            //keyに年を取得
                                            for (String year : map.keySet()){
                                                mYear = year;
                                                Log.d("tuuti",String.valueOf(mYear));
                                                mCalenderRef=mDatabaseReference.child(Const.CalenderPATH).child(mId).child(mYear);
                                                mCalenderRef.addListenerForSingleValueEvent(
                                                        new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(DataSnapshot dataSnapshot) {
                                                                HashMap<String,HashMap<String,String>> map = (HashMap) dataSnapshot.getValue();
                                                                //keyに月を取得
                                                                for (String month : map.keySet()){
                                                                    mMonth = month;
                                                                    Log.d("tuuti",String.valueOf(mMonth));
                                                                    mCalenderRef=mDatabaseReference.child(Const.CalenderPATH).child(mId).child(mYear).child(mMonth);
                                                                    mCalenderRef.addListenerForSingleValueEvent(
                                                                            new ValueEventListener() {
                                                                                @Override
                                                                                public void onDataChange(DataSnapshot dataSnapshot) {
                                                                                    HashMap<String,HashMap<String,String>> map = (HashMap) dataSnapshot.getValue();
                                                                                    for (String date : map.keySet()){
                                                                                        mDate = date;
                                                                                        Log.d("tuuti",String.valueOf(mDate));
                                                                                        mCalenderRef = mDatabaseReference.child(Const.CalenderPATH).child(mId).child(mYear).child(mMonth).child(mDate);
                                                                                        mCalenderRef.addChildEventListener(
                                                                                                new ChildEventListener() {
                                                                                                    @Override
                                                                                                    public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                                                                                                        HashMap map = (HashMap) dataSnapshot.getValue();
                                                                                                        mStime = (String) map.get("開始時間");
                                                                                                        mEtime = (String) map.get("終了時間");
                                                                                                        mItitle = (String) map.get("タイトル");
                                                                                                        mIdetail = (String) map.get("詳細");
                                                                                                        boolean alertch = (boolean)map.get("通知on");
                                                                                                        mEcdate = (String) map.get("通知時刻");
                                                                                                        if (alertch == true) {
                                                                                                            try{
                                                                                                                SimpleDateFormat sdfy = new SimpleDateFormat("yyyy/mm/dd");
                                                                                                                mTdate = sdfy.parse(mEcdate);
                                                                                                                Log.d("tuuti",String.valueOf(mTdate));

                                                                                                                mTyear = mTdate.getYear();
                                                                                                                mTmonth = mTdate.getMonth();
                                                                                                                mTddate = mTdate.getDate();

//                                                                                                                Calendar cal = Calendar.getInstance();
//                                                                                                                mCYear =cal.get(Calendar.YEAR);
//                                                                                                                mCMonth =cal.get(Calendar.MONTH);
//                                                                                                                mCDay =cal.get(Calendar.DATE);
//                                                                                                                mCHour =cal.get(Calendar.HOUR);
//                                                                                                                mCMinute = cal.get(Calendar.MINUTE);

                                                                                                                GregorianCalendar calendar = new GregorianCalendar(mCYear,mCMonth,mCDay,mCHour,mCMinute);
                                                                                                                calendar.setTimeInMillis(0);
                                                                                                                calendar.set(mTyear,mTmonth,mTddate,0,0,0);
                                                                                                                Log.d("tuuti",String.valueOf(mTyear));
                                                                                                                Log.d("tuuti",String.valueOf(mTmonth));
                                                                                                                Log.d("tuuti",String.valueOf(mTddate));

                                                                            //                                    Intent resultIntent  = new Intent(context,TaskAlarmReceiver.class);
                                                                            //                                    resultIntent.putExtra("title",mItitle);
                                                                            //                                    resultIntent.putExtra("detail",mIdetail);


                                                                                                                PendingIntent contentIntent  = PendingIntent.getActivity(
                                                                                                                        context,
                                                                                                                        1,
                                                                                                                        new Intent() ,
                                                                                                                        PendingIntent.FLAG_UPDATE_CURRENT
                                                                                                                );

                                                                                                                AlarmManager alarmManager = (AlarmManager)context.getSystemService(ALARM_SERVICE);
                                                                                                                alarmManager.set(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(), contentIntent);

                                                                                                                NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "default");
                                                                                                                //ステータスバー表示のアイコンリソース
                                                                                                                builder.setSmallIcon(R.drawable.droid);

                                                                                                                //通知に表示される大きなアイコンをbitmapで指定。
                                                                                                                //指定が無い場合はsetSmalliconメソッドで指定したリソース
                                                                                                                builder.setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.droid_large));

                                                                                                                //いつ
                                                                                                                builder.setWhen(SystemClock.uptimeMillis());

                                                                                                                //通知時の音・バイブ・ライトの指定
                                                                                                                builder.setDefaults(Notification.DEFAULT_ALL);

                                                                                                                //true：ユーザーがタップしたら消える。falseの時は、コードで消す必要あり
                                                                                                                builder.setAutoCancel(true);

                                                                                                                // タスクの情報を設定する
                                                                                                                builder.setTicker(mItitle); // 5.0以降は表示されない
                                                                                                                builder.setContentTitle(mItitle);
                                                                                                                builder.setContentText(mIdetail);


                                                                                                                // 通知をタップしたらアプリを起動するようにする
                                                                                                                Intent startAppIntent = new Intent(context, MainActivity.class);
                                                                                                                startAppIntent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
                                                                                                                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, startAppIntent, 0);
                                                                                                                builder.setContentIntent(pendingIntent);

                                                                                                                // 通知を表示する
                                                                                                                NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

                                                                                                                notificationManager.notify(1, builder.build());

                                                                                                            }catch (Exception e){

                                                                                                            }

                                                                                                        }else{

                                                                                                        }
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

                                                                                @Override
                                                                                public void onCancelled(DatabaseError databaseError) {

                                                                                }
                                                                            }
                                                                    );

                                                                }
                                                            }

                                                            @Override
                                                            public void onCancelled(DatabaseError databaseError) {

                                                            }
                                                        }
                                                );
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    }
                            );
                            }



                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    }
            );

        }else{
            // タスクの情報を設定する
            builder.setTicker(mTitle); // 5.0以降は表示されない
            builder.setContentTitle(mItitle);
            builder.setContentText(mIdetail);


            // 通知をタップしたらアプリを起動するようにする
            Intent startAppIntent = new Intent(context, MainActivity.class);
            startAppIntent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, startAppIntent, 0);
            builder.setContentIntent(pendingIntent);


            notificationManager.notify(1, builder.build());

        }

    }
}