package jp.co.vivo_app.v_care;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ListFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v4.app.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.lang.String;
import java.lang.Boolean;

import static android.content.Context.LAYOUT_INFLATER_SERVICE;

public class AlertDialogshow extends DialogFragment {

    private Button mCreatebutton;
    private Button mEditbutton;
    private Button mExbutton;
    private EditText mIdText;
    private EditText mNameText;
    private EditText mPasswordText;
    private CheckBox mKengenCh;
    private EditText mGroup;

    private int cid;

    private ArrayList<Syain> mSyainArrayList;

    private DatabaseReference mUserRef;
    private DatabaseReference mDeleteRef;
    DatabaseReference mDatabaseReference;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View view = getActivity().getLayoutInflater().inflate(R.layout.activity_alert_dialogshow, null);

        mIdText = (EditText) view.findViewById(R.id.idedittext);
        mNameText = (EditText) view.findViewById(R.id.nameedittext);
        mPasswordText = (EditText) view.findViewById(R.id.passedittext);
        mKengenCh = (CheckBox) view.findViewById(R.id.adminkengen);
        mGroup = (EditText) view.findViewById(R.id.grouptext);

        //社員新規登録
        mCreatebutton = (Button) view.findViewById(R.id.createbutton);
        mCreatebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id = mIdText.getText().toString();
                String name = mNameText.getText().toString();
                String password = mPasswordText.getText().toString();
                String group = mGroup.getText().toString();
                boolean adminkengen = mKengenCh.isChecked();

                if (id.length() == 0){
                    Snackbar.make(v,"社員IDが未入力です。",Snackbar.LENGTH_LONG).show();
                    return;
                }

                if (name.length() == 0) {
                    Snackbar.make(v, "社員名が未入力です。", Snackbar.LENGTH_LONG).show();
                    return;
                }

                if (password.length() == 0) {
                    Snackbar.make(v, "パスワードが未入力です。", Snackbar.LENGTH_LONG).show();
                    return;
                }

                mDatabaseReference = FirebaseDatabase.getInstance().getReference();
                mUserRef = mDatabaseReference.child(Const.UserPATH).child(id);
                mUserRef.addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                String id = mIdText.getText().toString();
                                String name = mNameText.getText().toString();
                                String password = mPasswordText.getText().toString();
                                String group = mGroup.getText().toString();
                                boolean adminkengen = mKengenCh.isChecked();

                                HashMap map = (HashMap) dataSnapshot.getValue();
                                Map<String, Object> data = new HashMap<>();
                                if (map == null){
                                    data.put("name",name);
                                    data.put("password",password);
                                    data.put("group",group);
                                    data.put("adminkengen",adminkengen);

                                    mUserRef.setValue(data);
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(getActivity());
                                    alertDialogBuilder.setMessage("登録完了");
                                    alertDialogBuilder.setPositiveButton("OK",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            });
                                    android.app.AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                                    return;
                                }else{
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(getActivity());
                                    alertDialogBuilder.setTitle("すでに登録済のIDです。");
                                    alertDialogBuilder.setPositiveButton("OK",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                }
                                            });
                                    android.app.AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                                    return;
                                }



                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        }
                );
            }

        });

        //登録済社員修正
        mEditbutton = (Button) view.findViewById(R.id.editbutton);
        mEditbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String id = mIdText.getText().toString();
                String name = mNameText.getText().toString();
                String password = mPasswordText.getText().toString();
                String group = mGroup.getText().toString();
                boolean adminkengen = mKengenCh.isChecked();


                if (id.length() == 0){
                    Snackbar.make(v,"社員IDが未入力です。",Snackbar.LENGTH_LONG).show();
                    return;
                }

                if (name.length() == 0) {
                    Snackbar.make(v, "社員名が未入力です。", Snackbar.LENGTH_LONG).show();
                    return;
                }

                if (password.length() == 0) {
                    Snackbar.make(v, "パスワードが未入力です。", Snackbar.LENGTH_LONG).show();
                    return;
                }

                mDatabaseReference = FirebaseDatabase.getInstance().getReference();
                mUserRef = mDatabaseReference.child(Const.UserPATH).child(id);
                mUserRef.addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                String id = mIdText.getText().toString();
                                String name = mNameText.getText().toString();
                                String password = mPasswordText.getText().toString();
                                String group = mGroup.getText().toString();
                                boolean adminkengen = mKengenCh.isChecked();

                                HashMap map = (HashMap) dataSnapshot.getValue();
                                Map<String, Object> data = new HashMap<>();
                                //修正ボタンを押したが、登録がなかった
                                if (map == null){
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(getActivity());
                                    alertDialogBuilder.setTitle("IDの登録がありません。新規登録しますか？");
                                    alertDialogBuilder.setPositiveButton("OK",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    String id = mIdText.getText().toString();
                                                    String name = mNameText.getText().toString();
                                                    String password = mPasswordText.getText().toString();
                                                    String group = mGroup.getText().toString();
                                                    boolean adminkengen = mKengenCh.isChecked();

                                                    mUserRef = mDatabaseReference.child(Const.UserPATH).child(id);
                                                    Map<String,Object> data = new HashMap<>();

                                                    data.put("name",name);
                                                    data.put("password",password);
                                                    data.put("group",group);
                                                    data.put("adminkengen",adminkengen);

                                                    mUserRef.setValue(data);
                                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(getActivity());
                                                    alertDialogBuilder.setMessage("登録しました。");
                                                    alertDialogBuilder.setPositiveButton("OK",
                                                            new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {

                                                                }
                                                            });

                                                    android.app.AlertDialog alertDialog = alertDialogBuilder.create();
                                                    alertDialog.show();
                                                    return;
                                                }
                                            });
                                    alertDialogBuilder.setNegativeButton("CANCEL",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            });
                                    android.app.AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                                    return;
                                }else{
                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(getActivity());
                                    alertDialogBuilder.setMessage("社員ID：" +id + "の登録内容を修正しますか？");
                                    alertDialogBuilder.setPositiveButton("OK",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    String id = mIdText.getText().toString();
                                                    String name = mNameText.getText().toString();
                                                    String password = mPasswordText.getText().toString();
                                                    String group = mGroup.getText().toString();
                                                    boolean adminkengen = mKengenCh.isChecked();

                                                    Map<String,Object> data = new HashMap<>();
                                                    mUserRef = mDatabaseReference.child(Const.UserPATH).child(id);
                                                    data.put("name",name);
                                                    data.put("password",password);
                                                    data.put("group",group);
                                                    data.put("adminkengen",adminkengen);

                                                    mUserRef.setValue(data);
                                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(getActivity());
                                                    alertDialogBuilder.setMessage("修正しました。");
                                                    alertDialogBuilder.setPositiveButton("OK",
                                                            new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {

                                                                }
                                                            });

                                                    android.app.AlertDialog alertDialog = alertDialogBuilder.create();
                                                    alertDialog.show();
                                                    return;
                                                }
                                            });
                                    alertDialogBuilder.setNegativeButton("CANCEL",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            });
                                    android.app.AlertDialog alertDialog = alertDialogBuilder.create();
                                    alertDialog.show();
                                    return;
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        }
                );

            }

        });

        //社員削除
        mExbutton = (Button) view.findViewById(R.id.exbutton);
        mExbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id = mIdText.getText().toString();
                mDatabaseReference = FirebaseDatabase.getInstance().getReference();
                mUserRef = mDatabaseReference.child(Const.UserPATH).child(id);
                mUserRef.addListenerForSingleValueEvent(
                        new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                String id = mIdText.getText().toString();
                                HashMap map = (HashMap) dataSnapshot.getValue();

                                if (map != null){
                                    android.app.AlertDialog.Builder androidAlertDialogBuilder = new android.app.AlertDialog.Builder(getActivity());
                                    androidAlertDialogBuilder.setMessage("社員ID：" + id + "を削除しますか？");
                                    androidAlertDialogBuilder.setPositiveButton("OK",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {
                                                    String id = mIdText.getText().toString();
                                                    mDatabaseReference = FirebaseDatabase.getInstance().getReference();
                                                    mUserRef = mDatabaseReference.child(Const.UserPATH).child(id);
                                                    mUserRef.removeValue();

                                                    mDeleteRef = mDatabaseReference.child(Const.DeletePATH).child(id);

                                                    Map<String,Object> data = new HashMap<>();

                                                    String name = mNameText.getText().toString();
                                                    String password = mPasswordText.getText().toString();
                                                    String group = mGroup.getText().toString();
                                                    boolean adminkengen = mKengenCh.isChecked();


                                                    data.put("name",name);
                                                    data.put("password",password);
                                                    data.put("group",group);
                                                    data.put("adminkengen",adminkengen);

                                                    mDeleteRef.setValue(data);

                                                    android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(getActivity());
                                                    alertDialogBuilder.setMessage("削除しました。");
                                                    alertDialogBuilder.setPositiveButton("OK",
                                                            new DialogInterface.OnClickListener() {
                                                                @Override
                                                                public void onClick(DialogInterface dialog, int which) {

                                                                }
                                                            });

                                                    mIdText.setText("");
                                                    mNameText.setText("");
                                                    mGroup.setText("");
                                                    mKengenCh.setChecked(false);
                                                    mPasswordText.setText("");



                                                    android.app.AlertDialog alertDialog = alertDialogBuilder.create();
                                                    alertDialog.show();
                                                    return;

                                                }

                                            });
                                    androidAlertDialogBuilder.setNegativeButton("CANCEL",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            });

                                    android.app.AlertDialog alertDialog = androidAlertDialogBuilder.create();
                                    alertDialog.show();
                                    return;


                                }else{
                                    android.app.AlertDialog.Builder androidAlertDialogBuilder = new android.app.AlertDialog.Builder(getActivity());
                                    androidAlertDialogBuilder.setMessage("該当するIDの登録はありません。");
                                    androidAlertDialogBuilder.setPositiveButton("OK",
                                            new DialogInterface.OnClickListener() {
                                                @Override
                                                public void onClick(DialogInterface dialog, int which) {

                                                }
                                            });


                                    android.app.AlertDialog alertDialog = androidAlertDialogBuilder.create();
                                    alertDialog.show();
                                    return;
                                }

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        }
                );

            }
        });

        return new android.app.AlertDialog.Builder(getActivity(),R.style.MyAlertDialogTheme)
                .setView(view)
                .setTitle("社員登録")
                .show();



    }

    @Override
    public void onPause() {
        super.onPause();
        // onPause でダイアログを閉じる場合
        dismiss();
    }
}



