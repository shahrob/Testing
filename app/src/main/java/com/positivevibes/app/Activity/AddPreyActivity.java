package com.positivevibes.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.positivevibes.app.ApiStructure.ApiModelClass;
import com.positivevibes.app.ApiStructure.Constants;
import com.positivevibes.app.ApiStructure.ServerCallback;
import com.positivevibes.app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class AddPreyActivity extends AppCompatActivity {

    RelativeLayout back_rel, box_add_prey_rel, parent;
    TextView add_prey_btn;
    EditText add_prey_txt;
    ProgressBar progressBar;
    String pray_id;
    String activity;
    String api_pray_id;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_prey);

        initialization();

//        AnalyticsTrackers.initialize(this);
//
//        AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP, "UA-123157129-1");
//        AnalyticsTrackers.getInstance().trackScreenView("Add Pray Activity");

        // firebase analytics
         FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundlee = new Bundle();
        bundlee.putString(FirebaseAnalytics.Param.ITEM_NAME, "Add Pray Activity");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundlee);

        // firebase analytics

        Bundle bundle = getIntent().getExtras();
        activity = bundle.getString("ACTIVITY");
        if (activity.equals("praylist")){
            String text = bundle.getString("TEXT");
            add_prey_txt.setText(text);
            pray_id = bundle.getString("PRAY_ID");

        }

        add_prey_txt.setMovementMethod(new ScrollingMovementMethod());

        back_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        box_add_prey_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager) AddPreyActivity.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(add_prey_txt, InputMethodManager.SHOW_IMPLICIT);
            }
        });
        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
            }
        });

        add_prey_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (add_prey_txt.getText().toString().isEmpty()){
                    Toast.makeText(AddPreyActivity.this, "Please insert some thing", Toast.LENGTH_SHORT).show();
                }else {
                    progressBar.setVisibility(View.VISIBLE);
                    hideKeyboard(v);
                    if (activity.equals("praylist")){
                        add_prey_btn.setVisibility(View.GONE);
                        ApiEditPray(pray_id);
                    }else {
                        add_prey_btn.setVisibility(View.GONE);
                        hitApi();

                    }
                }


            }
        });



    }


    private void hitApi() {

        // hit api............


        SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String token = mPrefs.getString("USER_TOKEN", "");

        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("title",add_prey_txt.getText().toString());

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("x-sh-auth", token);
        headers.put("Content-Type", "application/json");

        ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.ADDPREY, AddPreyActivity.this, postParam, headers, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result, String ERROR) {

                if (ERROR.isEmpty()) {

                    try {

                        JSONObject jsonObject = new JSONObject(String.valueOf(result));

                        api_pray_id = jsonObject.getString("_id");

                        hitApifornotify(api_pray_id);

                        progressBar.setVisibility(View.GONE);
                        finish();
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {


                    if (ERROR.equals("401")) {

                        SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                        SharedPreferences.Editor mEditor = mPrefs.edit();
                        mEditor.putString("USER_EMAIL", "");
                        mEditor.putString("USER_PASSWORD", "");
                        mEditor.putString("USER_TOKEN", "");
                        mEditor.putString("USER_PIC", "");
                        mEditor.apply();
                        Intent intt = new Intent(AddPreyActivity.this, FirstActivity.class);
                        startActivity(intt);
                        finish();

                    } else {

                        Toast.makeText(AddPreyActivity.this, ERROR, Toast.LENGTH_SHORT).show();

                    }

                }

            }
        });
        // hit api--------------------

    }

    private void hitApifornotify(String id) {

        // hit api............


        SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String token = mPrefs.getString("USER_TOKEN", "");


        Map<String, String> postParam = new HashMap<String, String>();

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("x-sh-auth", token);
        headers.put("Content-Type", "application/json");

        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.NOTIFYPRAY + id, AddPreyActivity.this, postParam, headers, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result, String ERROR) {

                if (ERROR.isEmpty()) {

                    try {


                        JSONObject jsonObject = new JSONObject(String.valueOf(result));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {


                    if (ERROR.equals("401")) {

                        SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                        SharedPreferences.Editor mEditor = mPrefs.edit();
                        mEditor.putString("USER_EMAIL", "");
                        mEditor.putString("USER_PASSWORD", "");
                        mEditor.putString("USER_TOKEN", "");
                        mEditor.putString("USER_PIC", "");
                        mEditor.apply();
                        Intent intt = new Intent(AddPreyActivity.this, FirstActivity.class);
                        startActivity(intt);
                        finish();
                    } else {

                        Toast.makeText(AddPreyActivity.this, ERROR, Toast.LENGTH_SHORT).show();

                    }

                }

            }
        });
        // hit api--------------------

    }


    private void ApiEditPray(String id){

        // hit api--------------------

        SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String token = mPrefs.getString("USER_TOKEN", "");

        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("title",add_prey_txt.getText().toString() );

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("x-sh-auth", token);
        headers.put("Content-Type", "application/json");

        ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.EDITPRAY + id, AddPreyActivity.this, postParam, headers, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result, String ERROR) {

                if (ERROR.isEmpty()) {

                    try {


                        JSONObject jsonObject = new JSONObject(String.valueOf(result));

                        progressBar.setVisibility(View.GONE);
                        finish();
                        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {


                    if (ERROR.equals("401")) {

                        SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                        SharedPreferences.Editor mEditor = mPrefs.edit();
                        mEditor.putString("USER_EMAIL", "");
                        mEditor.putString("USER_PASSWORD", "");
                        mEditor.putString("USER_TOKEN", "");
                        mEditor.putString("USER_PIC", "");
                        mEditor.apply();
                        Intent intt = new Intent(AddPreyActivity.this, FirstActivity.class);
                        startActivity(intt);
                        finish();
                    } else {

                        Toast.makeText(AddPreyActivity.this, ERROR, Toast.LENGTH_SHORT).show();

                    }

                }

            }
        });
        // hit api--------------------


    }



    protected void hideKeyboard(View view) {

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }

    private void initialization(){
        back_rel = findViewById(R.id.back_rel_prey);
        box_add_prey_rel = findViewById(R.id.box_add_prey_rel);
        add_prey_btn = findViewById(R.id.add_prey_btn);
        add_prey_txt = findViewById(R.id.add_prey_txt);
        parent = findViewById(R.id.parent_add_prey);
        progressBar = findViewById(R.id.progress_prey);
    }

}
