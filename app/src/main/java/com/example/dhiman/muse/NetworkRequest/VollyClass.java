package com.example.dhiman.muse.NetworkRequest;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.Map;

public class VollyClass {
    private Context activityContext;
    private NetworkResponseListener listenerInterface;
    Map<String, String> params;
    RequestQueue queue;
    public VollyClass(Context c1, Context c2){
        activityContext = c1;
        //applicationContext = c2;
        this.listenerInterface = (NetworkResponseListener) c1;
    }
    public VollyClass(Fragment f, Context c1, Context c2){
        activityContext = c1;
        //applicationContext = c2;
        this.listenerInterface = (NetworkResponseListener) f;
    }
    public void makeRequestPost(String url, Map<String, String> parameters) {
        params = parameters;
        // Instantiate the RequestQueue.
        Log.v("DEBUG","Network-Request : " + params.toString());
        queue = Volley.newRequestQueue(activityContext);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.d("DEBUG","Response:" + response);
                        // Display the first 500 characters of the response string.
                        listenerInterface.onResponseReceived(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("DEBUG", "ERROR: " + error.getMessage());
                        listenerInterface.onResponseReceived("Unexpected Error!" + error.toString());
                        error.printStackTrace();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                return params;
            }
        };
        stringRequest.setTag("request");
        int socketTimeout = 45000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, -1,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
    public void makeRequestGet(String url, Map<String, String> parameters) {
        params = parameters;
        Log.v("DEBUG","Request : " + params.toString());
        // Instantiate the RequestQueue.
        queue = Volley.newRequestQueue(activityContext);
        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Log.d("DEBUG","Response:" + response);
                        listenerInterface.onResponseReceived(response);
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("DEBUG", "ERROR: " + error.getMessage());
                        listenerInterface.onResponseReceived("Unexpected Error!" + error.toString());
                        error.printStackTrace();
                    }
                }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                params.put("User-Agent", "Muse-Android_HiDnAm");
                return params;
            }
        };
        stringRequest.setTag("request");
        // Add the request to the RequestQueue.
        int socketTimeout = 45000;//30 seconds - change to what you want
        RetryPolicy policy = new DefaultRetryPolicy(socketTimeout, 2,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(policy);
        queue.add(stringRequest);
    }

    public void cancelAllRequest(){
        if(queue != null)
            queue.cancelAll("request");
    }

}
