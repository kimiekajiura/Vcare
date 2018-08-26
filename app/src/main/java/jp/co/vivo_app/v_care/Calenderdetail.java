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
import android.widget.TextView;
import android.support.v7.widget.Toolbar;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

public class Calenderdetail extends DialogFragment{

    private AlertDialog.Builder alert;

    private String mId;
    private int mYear;
    private int mMonth;
    private int mDate;
    private String mStime;
    private String mEtime;
    private String mTitle;
    private String mDetail;

    private int mCyear;
    private int mCmonth;
    private int mCdate;
    private int mChour;
    private int mCminute;

    private DatabaseReference mCalenderRef;
    DatabaseReference mDatabaseReference;

    ArrayAdapter<String> adapter;

    private EditText mYoteiEditText;
    private EditText mTitleedittext;
    private Spinner mIdSpinner;
    private Spinner mStimespinner;
    private Spinner mEtimespinner;
    private Button mCreateButton;
    private Toolbar mToolbar;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View mainTabView = getActivity().getLayoutInflater().inflate(R.layout.calender_detail, null);

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

        TextView titleView = (TextView) mainTabView.findViewById(R.id.titleedittext);
        titleView.setText(mTitle);

        TextView detailView = (TextView) mainTabView.findViewById(R.id.yoteiedittext);
        detailView.setText(mDetail);

        mToolbar = (android.support.v7.widget.Toolbar) mainTabView.findViewById(R.id.toolbar);
        mToolbar.setTitle(mYear + "/" + mMonth + "/" + mDate );

        mStimespinner = (Spinner) mainTabView.findViewById(R.id.stimespinner);
        mEtimespinner = (Spinner) mainTabView.findViewById(R.id.etimespinner);
        mTitleedittext = (EditText) mainTabView.findViewById(R.id.titleedittext);
        mYoteiEditText = (EditText) mainTabView.findViewById(R.id.yoteiedittext);

        adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //Spinner stimespinner = (Spinner) mainTabView.findViewById(R.id.stimespinner);
        //stimespinner.setAdapter(adapter);
        //stimespinner.setSelection(mYear);

        //Spinner etimespinner = (Spinner) mainTabView.findViewById(R.id.etimespinner);
        //etimespinner.setAdapter(adapter);
        //etimespinner.setSelection(mMonth);

        Calendar cal = Calendar.getInstance();

        mCyear =cal.get(Calendar.YEAR);
        mCmonth =cal.get(Calendar.MONTH);
        mCdate =cal.get(Calendar.DATE);
        mChour =cal.get(Calendar.HOUR);
        mCminute = cal.get(Calendar.MINUTE);

        //登録
        mCreateButton=(Button) mainTabView.findViewById(R.id.createbutton);
        mCreateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mId = mId;

                mDatabaseReference= FirebaseDatabase.getInstance().getReference();
                mCalenderRef=mDatabaseReference.child(Const.CalenderPATH).child(mId).child(String.valueOf(mYear)).child(String.valueOf(mMonth)).child(String.valueOf(mDate));
                //HashMap map = (HashMap) dataSnapshot.getValue();

                Map<String,Object> data = new HashMap<>();

                String stime = (String) mStimespinner.getSelectedItem();
                String etime = (String) mEtimespinner.getSelectedItem();
                String title = mTitleedittext.getText().toString();
                String detail = mYoteiEditText.getText().toString();

                data.put("開始時間",stime);
                data.put("終了時間",etime);
                data.put("タイトル",title);
                data.put("詳細",detail);
                data.put("予定登録時間",mCyear + "/" + mCmonth + "/" + mCdate + "  " + mChour + ":" + mCminute);

                //Calender calender = new Calender(mYear,mMonth,mDate,stime,etime,title,detail,mId);

                mCalenderRef.push().setValue(data);

                android.app.AlertDialog.Builder alertdialog = new android.app.AlertDialog.Builder(getActivity());
                alertdialog.setTitle("登録しました。");
                alertdialog.setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                android.app.AlertDialog alertDialog = alertdialog.create();
                alertDialog.show();

            }

        });

        return new android.app.AlertDialog.Builder(getActivity(), R.style.MyAlertDialogTheme)
                .setView(mainTabView)
                .show();
    }
}
