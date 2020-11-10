package com.example.phpmysql_crud;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText user_name,user_password;
    private Button login;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if(SharedPrefManager.getInstance(this).isLoggedIn()){
            finish();
            startActivity(new Intent(this,ProfileActivity.class));
            return;
        }
        
        user_name = (EditText)findViewById(R.id.username);
        user_password = (EditText)findViewById(R.id.password);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait...");

        login = (Button)findViewById(R.id.login_btn);
        login.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        if(v==login){
            LoginUser();
        }
    }

    private void LoginUser() {
        final String username = user_name.getText().toString().trim();
        final String password = user_password.getText().toString().trim();

        progressDialog.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Constants.URL_LOGIN, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                progressDialog.dismiss();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    if(!jsonObject.getBoolean("error")){
                        SharedPrefManager.getInstance(getApplicationContext())
                                .userLogin(
                                        jsonObject.getInt("id"),
                                        jsonObject.getString("username"),
                                        jsonObject.getString("email"));
                        Toast.makeText(getApplicationContext(),"User login successful..",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(LoginActivity.this,ProfileActivity.class));
                        finish();
                    }
                    else {
                        Toast.makeText(getApplicationContext(),jsonObject.getString("message"),Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        progressDialog.hide();
                        Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<>();
                params.put("username",username);
                params.put("password",password);
                return params;
            }

        };

        Toast.makeText(getApplicationContext(),stringRequest.toString(),Toast.LENGTH_LONG).show();

        RequestHandler.getInstance(this).addToRequestQueue(stringRequest);
    }
}
