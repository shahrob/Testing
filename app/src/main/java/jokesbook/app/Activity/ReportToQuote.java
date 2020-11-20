package jokesbook.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import jokesbook.app.ApiStructure.ApiModelClass;
import jokesbook.app.ApiStructure.Constants;
import jokesbook.app.ApiStructure.ServerCallback;
import jokesbook.app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ReportToQuote extends AppCompatActivity {

    EditText problem;
    RelativeLayout back_rel;
    RelativeLayout send_rel,parent,box_rel;
    ProgressBar progress;
    String quote_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_to_quote);
        Initialization();

//        AnalyticsTrackers.initialize(this);
//
//        AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP, "UA-123157129-1");
//        AnalyticsTrackers.getInstance().trackScreenView("Report to Quote Activity");

        Bundle bundle = getIntent().getExtras();

        quote_id = bundle.getString("FEEDID");

        box_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                InputMethodManager imm = (InputMethodManager) ReportToQuote.this.getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(problem, InputMethodManager.SHOW_IMPLICIT);
            }
        });

        parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(v);
            }
        });
        back_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });
        send_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(problem.getText().toString().isEmpty()){
                    Toast.makeText(ReportToQuote.this, "Please insert some thing", Toast.LENGTH_SHORT).show();

                }
                else {

                    progress.setVisibility(View.VISIBLE);
                    hideKeyboard(v);
                    hitApi();
                }


            }
        });



    }

    private void hitApi() {

        // hit api............


        SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String token = mPrefs.getString("USER_TOKEN", "");


        Map<String, String> postParam = new HashMap<String, String>();
        postParam.put("message",problem.getText().toString() );



        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("x-sh-auth", token);
        headers.put("Content-Type", "application/json");

        ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.QUOTEREPORT + quote_id, ReportToQuote.this, postParam, headers, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result, String ERROR) {

                if (ERROR.isEmpty()) {

                    try {


                        JSONObject jsonObject = new JSONObject(String.valueOf(result));

                        progress.setVisibility(View.GONE);
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
                        Intent intt = new Intent(ReportToQuote.this, FirstActivity.class);
                        startActivity(intt);
                        finish();
                    } else {

                        Toast.makeText(ReportToQuote.this, ERROR, Toast.LENGTH_SHORT).show();

                    }

                }

            }
        });
        // hit api--------------------



    }

    protected void hideKeyboard(View view)
    {

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }

    private void Initialization(){
        problem = findViewById(R.id.problem_quote);
        back_rel = findViewById(R.id.back_rel);
        progress = findViewById(R.id.progress_quote);
        send_rel = findViewById(R.id.send_quote_rel);
        parent = findViewById(R.id.parent_quote);
        box_rel = findViewById(R.id.box_quote_rel);
    }

}
