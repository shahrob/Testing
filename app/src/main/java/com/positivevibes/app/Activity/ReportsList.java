package com.positivevibes.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.firebase.provider.FirebaseInitProvider;
import com.positivevibes.app.Adapters.ReportsAdapter;
import com.positivevibes.app.ApiStructure.ApiModelClass;
import com.positivevibes.app.ApiStructure.Constants;
import com.positivevibes.app.ApiStructure.ServerCallback;
import com.positivevibes.app.Models.ReportsModel.ReportsModel;
import com.positivevibes.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportsList extends AppCompatActivity {

    RelativeLayout back, submit_report_btn;
    RecyclerView reports_rv;
    ProgressBar progress;
    ReportsAdapter adapter;
    LinearLayoutManager mLayoutManager;
    String feed_id;

    List<ReportsModel> reportsModelList;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onResume() {
        super.onResume();

        ApiListReports();

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports_list);
        initialization();

        feed_id = getIntent().getStringExtra("feed_id");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        submit_report_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ReportsList.this, ReportToQuote.class);
                intent.putExtra("FEEDID", feed_id);
                startActivity(intent);
            }
        });

        progress.setVisibility(View.VISIBLE);


        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        reports_rv.getItemAnimator().endAnimations();

        reportsModelList = new ArrayList<>();
        ApiListReports();

    }

    private void ApiListReports(){
        SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String token = mPrefs.getString("USER_TOKEN", "");


        Map<String, String> postParam = new HashMap<String, String>();

        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("x-sh-auth", token);

        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.REPORTLIST, ReportsList.this, postParam, headers, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result, String ERROR) {

                if (ERROR.isEmpty()) {
                    try {

                        reportsModelList.clear();

                        JSONObject jsonObject = new JSONObject(String.valueOf(result));

                        int code = jsonObject.getInt("code");
                        JSONArray reports = jsonObject.getJSONArray("reports");

                        for (int i = 0; i < reports.length(); i++){
                            ReportsModel reportsModel = new ReportsModel();
                            JSONObject reportsObj = reports.getJSONObject(i);

                            String report_id = reportsObj.getString("_id");

                            JSONObject user = reportsObj.getJSONObject("user");
                            String user_dp = user.getString("dp_active_file");
                            String user_id = user.getString("_id");
                            String user_name = user.getString("full_name");

                            String report_text = reportsObj.getString("report");
                            String report_date = reportsObj.getString("report_date");
                            String reply_date = reportsObj.getString("reply_date");
                            String reply = reportsObj.getString("reply");
                            String feed_id = reportsObj.getString("feed");

                            reportsModel.setReport_id(report_id);
                            reportsModel.setUser_image(user_dp);
                            reportsModel.setUser_id(user_id);
                            reportsModel.setUser_name(user_name);
                            reportsModel.setReport_text(report_text);
                            reportsModel.setReply_date(reply_date);
                            reportsModel.setReport_date(report_date);
                            reportsModel.setAdmin_reply(reply);
                            reportsModel.setFeed_id(feed_id);

                            reportsModelList.add(reportsModel);
                        }

                        adapter = new ReportsAdapter(ReportsList.this, reportsModelList);

//                        reports_rv.setItemViewCacheSize(20);
                        reports_rv.setDrawingCacheEnabled(true);
                        reports_rv.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);

                        reports_rv.setAdapter(adapter);
                        reports_rv.setLayoutManager(mLayoutManager);



                        progress.setVisibility(View.GONE);
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
                        Intent intt = new Intent(ReportsList.this, FirstActivity.class);
                        startActivity(intt);
                        finish();
                    } else {
                        Toast.makeText(ReportsList.this, ERROR, Toast.LENGTH_SHORT).show();
                        progress.setVisibility(View.GONE);
                    }
                }
            }
        });
    }

    private void initialization(){
        back = findViewById(R.id.back_reports);
        reports_rv = findViewById(R.id.reports_rv);
        progress = findViewById(R.id.progress);
        submit_report_btn = findViewById(R.id.submit_report_btn);
    }
}
