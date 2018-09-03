package jp.co.vivo_app.v_care;

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

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class TaskAlarmReceiver extends BroadcastReceiver {

    DatabaseReference mDatabaseReference;
    private DatabaseReference mCalenderRef;

    private String mStime;
    private String mEtime;
    private String mTitle;
    private String mDetail;
    private String mKey;

    private String mItitle;
    private String mIdetail;


    @Override
    public void onReceive(Context context, Intent intent) {
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

        //アラーム準備
        mDatabaseReference=FirebaseDatabase.getInstance().getReference();
        mCalenderRef=mDatabaseReference.child(Const.CalenderPATH).child(id).child(String.valueOf(year)).child(String.valueOf(month)).child(String.valueOf(date)).child(mKey);
        mCalenderRef.addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        HashMap map = (HashMap) dataSnapshot.getValue();

                        mStime = (String) map.get("開始時間");
                        mEtime = (String) map.get("終了時間");
                        mTitle = (String) map.get("タイトル");
                        mDetail = (String) map.get("詳細");
                        boolean alertch = (boolean)map.get("通知on");

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }

                }

        );

        // タスクの情報を設定する
        builder.setTicker(mTitle); // 5.0以降は表示されない
        builder.setContentTitle(mItitle);
        builder.setContentText(mIdetail);


        // 通知をタップしたらアプリを起動するようにする
        Intent startAppIntent = new Intent(context, MainActivity.class);
        startAppIntent.addFlags(Intent.FLAG_ACTIVITY_BROUGHT_TO_FRONT);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, startAppIntent, 0);
        builder.setContentIntent(pendingIntent);

        // 通知を表示する
        notificationManager.notify(1, builder.build());

    }
}
