package com.example.phpmysql_crud;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private TextView profile_username, profile_useremail, profile_userid;
    private Button logout_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        if(!SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }

        profile_useremail = (TextView)findViewById(R.id.profile_useremail);
        profile_username = (TextView)findViewById(R.id.profile_username);
        profile_userid = (TextView)findViewById(R.id.profile_userid);

        profile_useremail.setText(SharedPrefManager.getInstance(this).getUserEmail());
        profile_username.setText(SharedPrefManager.getInstance(this).getUserName());
        profile_userid.setText(String.valueOf(SharedPrefManager.getInstance(this).getUserId()));

        logout_btn = (Button)findViewById(R.id.profile_logout);
        logout_btn.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v==logout_btn){
            SharedPrefManager.getInstance(this).logout();
            Toast.makeText(this,"User Logged out..",Toast.LENGTH_LONG).show();
            finish();
            startActivity(new Intent(this,LoginActivity.class));

        }
    }
}
