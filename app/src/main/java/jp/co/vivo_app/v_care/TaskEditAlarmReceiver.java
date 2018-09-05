package jp.co.vivo_app.v_care;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.SystemClock;
import android.support.v4.app.NotificationCompat;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;

import static android.content.Context.ALARM_SERVICE;

public class TaskEditAlarmReceiver extends BroadcastReceiver {
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




    @Override
    public void onReceive(final Context context, Intent intent) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);




    }
}
