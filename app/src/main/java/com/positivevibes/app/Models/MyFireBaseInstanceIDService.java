package com.positivevibes.app.Models;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.Request;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.positivevibes.app.ApiStructure.ApiModelClass;
import com.positivevibes.app.ApiStructure.Constants;
import com.positivevibes.app.ApiStructure.ServerCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

//
public class MyFireBaseInstanceIDService extends FirebaseInstanceIdService {
    String user_token="";
    String TAG="kk";
    @Override
    public void onTokenRefresh() {

        Log.e(TAG, "IDcall");

        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();



        Log.e(TAG, "Refreshed token: " + refreshedToken);

        user_token=refreshedToken;
        if(user_token != ""){
            SharedPreferences mPrefs_token = getSharedPreferences("Token", Context.MODE_PRIVATE );
            SharedPreferences.Editor mEditor = mPrefs_token.edit();
            mEditor.putString("TOKEN",user_token);
            mEditor.apply();

        }


        // hit api............

        SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String token = mPrefs.getString("USER_TOKEN", "");
        String session_id = mPrefs.getString("session_id", "");


        Map<String, String> postParam = new HashMap<String, String>();


        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("x-sh-auth", token);

        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.UPDATE_TOEKN+refreshedToken+"&session_id="+session_id, MyFireBaseInstanceIDService.this, postParam, headers, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result, String ERROR) {

                if (ERROR.isEmpty()) {

                    try {


                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        Log.e(TAG, "Refreshed token: jsonObject " + jsonObject );

                        int code = jsonObject.getInt("code");
                        if (code == 200) {
                            Log.e(TAG, "Refreshed token: saved " );

//                            String message = jsonObject.getString("message");
//                            Toast.makeText(MyFireBaseInstanceIDService.this, message+" TOEKN API", Toast.LENGTH_LONG).show();

                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {
                    Log.e(TAG, "Refreshed token: error " );

                }

            }
        });
        // hit api--------------------


        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }
    private  void sendRegistrationToServer(String token){

    }
}
