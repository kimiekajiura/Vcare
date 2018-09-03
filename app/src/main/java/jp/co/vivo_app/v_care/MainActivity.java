package jp.co.vivo_app.v_care;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class MainActivity extends AppCompatActivity{

    ArrayAdapter<String> adapter;

    Spinner mIdSpinner;
    EditText mPasswordEditText;
    TextView mNameTextView;
    ProgressDialog mProgress;

    private ArrayList<Syain> mSyainArrayList;
    private DatabaseReference mUserRef;
    FirebaseAuth mAuth;
    DatabaseReference mDatabaseReference;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mSyainArrayList = new ArrayList<Syain>();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        Spinner spinner = (Spinner) findViewById(R.id.syaincodespinner);
        spinner.setAdapter(adapter);

        mUserRef = mDatabaseReference.child(Const.UserPATH);
        mUserRef.addChildEventListener(mUserListener);
        //return;

        spinner.setPrompt("社員CODE");
        TextView textView = (TextView) findViewById(R.id.syainnametextview);
        textView.setText("");

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








        //インスタンスをメンバ変数に保持
        mPasswordEditText = (EditText) findViewById(R.id.passwordedittext);
        mNameTextView = (TextView) findViewById(R.id.syainnametextview);

        mProgress = new ProgressDialog(this);
        mProgress.setMessage("処理中・・・");

        Button loginButton = (Button) findViewById(R.id.loginbutton);
        loginButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                InputMethodManager im =(InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                im.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

                String name = mNameTextView.getText().toString();
                String password = mPasswordEditText.getText().toString();

                if (name.equals("")){
                    Snackbar.make(v,"社員名を入力してください",Snackbar.LENGTH_LONG).show();
                }else if(password.equals("")){
                    Snackbar.make(v,"パスワードを入力してください",Snackbar.LENGTH_LONG).show();
                }else if (name != "" && password != ""){

                    String item = (String) mIdSpinner.getSelectedItem();

                        mDatabaseReference.child(Const.UserPATH).child(item).addListenerForSingleValueEvent(
                                new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        HashMap map = (HashMap) dataSnapshot.getValue();
                                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

                                        String item = (String) mIdSpinner.getSelectedItem();

                                        String password = (String) map.get("password");
                                        String name = (String) map.get("name");
                                        String group = (String) map.get("group");
                                        boolean adminkengen = (boolean) map.get("adminkengen");

                                        String pass = mPasswordEditText.getText().toString();
                                        if (Objects.equals(pass,password)){
                                            Intent intent = new Intent(getApplicationContext(),TimeandAttendance.class);
                                            intent.putExtra("id",item);
                                            intent.putExtra("password", password);
                                            intent.putExtra("name", name);
                                            intent.putExtra("group", group);
                                            intent.putExtra("adminkengen", adminkengen);
                                            startActivity(intent);
                                            return;
                                        }else{
                                            showAlertDialog();
                                            return;
                                        }

                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                }
                        );

                    }

            }
        });

    }

    private void showAlertDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setTitle("入力内容を確認してください。");
        alertDialogBuilder.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

}
