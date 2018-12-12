package com.example.dhiman.muse;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.dhiman.muse.NetworkRequest.NetworkResponseListener;
import com.example.dhiman.muse.app.ApplicationVariable;
import com.example.dhiman.muse.app.CommonMethods;
import com.google.android.exoplayer2.ExoPlayer;

import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity implements NetworkResponseListener{
    private EditText mUserNameText;
    private EditText mPasswordText;
    private Button mLoginButton;
    private Button mSignUpButton;
    private ProgressDialog dialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();

        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               if(TextUtils.isEmpty(mUserNameText.getText().toString().trim())|| TextUtils.isEmpty(mPasswordText.getText().toString().trim())){
                   Snackbar.make(view,"Fill username and Password ..",Snackbar.LENGTH_LONG).show();
               }
               else{
                   String username = mUserNameText.getText().toString();
                   String password = mUserNameText.getText().toString();
                   CommonMethods.login(LoginActivity.this,getApplicationContext(),username,password);
                   dialog.show();

               }
            }
        });
        mSignUpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this,SignUpActivity.class));
            }
        });
    }

    private void initView() {
        mUserNameText = (EditText) findViewById(R.id.username_edit_text);
        mPasswordText = (EditText) findViewById(R.id.password_edit_text);
        mLoginButton = (Button) findViewById(R.id.btn_login);
        mSignUpButton = (Button) findViewById(R.id.go_to_SignUp_btn);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Logging in ..");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
    }

    @Override
    public void onResponseReceived(String result) {
        dialog.dismiss();
        try {
            JSONObject jsonResponse = new JSONObject(result);
            if(jsonResponse.optBoolean("success")== true){
                if(!TextUtils.isEmpty(jsonResponse.optString("token"))){
                    String token = jsonResponse.optString("token");
                    ApplicationVariable.accoundData.token = token;
                    CommonMethods.saveToken(this,token);
                    startActivity(new Intent(this,Loading.class));
                }
                else{
                    Toast.makeText(this,"Error While Login",Toast.LENGTH_SHORT).show();
                }
            }
            else{
                Toast.makeText(this,"Username or Password Incorrect",Toast.LENGTH_SHORT).show();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
