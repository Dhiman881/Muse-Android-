package com.example.dhiman.muse;


import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;

import com.example.dhiman.muse.Helpers.WaveHelper;
import com.example.dhiman.muse.NetworkRequest.NetworkResponseListener;
import com.example.dhiman.muse.NetworkRequest.VollyClass;
import com.example.dhiman.muse.app.ApplicationVariable;
import com.example.dhiman.muse.app.CommonMethods;
import com.gelitenight.waveview.library.WaveView;

import org.json.JSONObject;

import java.util.HashMap;


public class Loading extends AppCompatActivity implements NetworkResponseListener {

    private WaveHelper mWaveHelper;
    private static final String GET_ACCOUNT_URL ="getaccountdata";

    private int mBorderColor = Color.parseColor("#44FFFFFF");
    private int mBorderWidth = 10;
    private VollyClass request;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        final WaveView waveView = (WaveView) findViewById(R.id.wave_view);
        mWaveHelper = new WaveHelper(waveView);
        waveView.setBorder(mBorderWidth, mBorderColor);
        final String token = CommonMethods.getSavedToken(this);
        mWaveHelper.start();
        if(token == null){

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    mWaveHelper.cancel();
                    startActivity(new Intent(Loading.this,LoginActivity.class));
                    finish();
                }
            },1200);
        }
        else{
            ApplicationVariable.accoundData.token = token;
            request = new VollyClass(this,getApplicationContext());
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    HashMap<String,String> params = new HashMap<String, String>();
                    params.put("authorization",token);
                    mWaveHelper.pause();
                    request.makeRequestGet(getResources().getString(R.string.muse_base_url)+GET_ACCOUNT_URL,params);

                }
            },1000);

        }

    }
    @Override
    protected void onPause() {
        super.onPause();
        mWaveHelper.cancel();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mWaveHelper.start();
    }

    @Override
    public void onResponseReceived(String result) {
        try {
            JSONObject jsonResponse = new JSONObject(result);
            if(jsonResponse.optBoolean("success")== true ){
               if(jsonResponse.optJSONObject("userDetails")!=null){
                   JSONObject jsonObject = jsonResponse.optJSONObject("userDetails");
                   ApplicationVariable.accoundData.regId = jsonObject.optString("regId");
                   ApplicationVariable.accoundData.userName = jsonObject.optString("username");
                   mWaveHelper.resume();
                   new Handler().postDelayed(new Runnable() {
                       @Override
                       public void run() {
                           mWaveHelper.cancel();
                           startActivity(new Intent(Loading.this,MainActivity.class));
                           finish();
                       }
                   },1000);
               }
            }
            else{
                Toast.makeText(this,"Token Invalid",Toast.LENGTH_SHORT).show();
                mWaveHelper.resume();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mWaveHelper.cancel();
                        CommonMethods.logout(Loading.this);
                        startActivity(new Intent(Loading.this,LoginActivity.class));
                        finish();
                    }
                },1000);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
