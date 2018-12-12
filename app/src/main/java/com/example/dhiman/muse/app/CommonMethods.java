package com.example.dhiman.muse.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.dhiman.muse.NetworkRequest.VollyClass;
import com.example.dhiman.muse.R;

import java.util.HashMap;
import java.util.Map;

public class CommonMethods {
    private static final String LOGIN_URL = "api/signin";
    public static String getSavedToken(Context context){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        return sharedPreferences.getString("TOKEN", null);
    }
    public static void saveToken(Context context,String token){
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("TOKEN", token);
        editor.commit();

    }
    public static void login(Context activityContext,Context applicationContext,String username,String password){
        VollyClass req = new VollyClass(activityContext, applicationContext);
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        req.makeRequestPost(activityContext.getResources().getString(R.string.muse_base_url)+LOGIN_URL,params);
    }
    public static void signup(Context activityContext,Context applicationContext,String username,String password){
        VollyClass req = new VollyClass(activityContext, applicationContext);
        Map<String, String> params = new HashMap<>();
        params.put("username", username);
        params.put("password", password);
        req.makeRequestPost("SignUp URL",params);
    }
    public static void logout(Context context){
        ApplicationVariable.accoundData.token = null;
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("TOKEN", null);
        editor.commit();
        ApplicationVariable.accoundData.regId = null;
        ApplicationVariable.accoundData.token = null;
        ApplicationVariable.accoundData.userName = null;
    }
}
