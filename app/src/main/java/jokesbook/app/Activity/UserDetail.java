package jokesbook.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;
import jokesbook.app.Adapters.UserDetailAdapter;
import jokesbook.app.ApiStructure.ApiModelClass;
import jokesbook.app.ApiStructure.Constants;
import jokesbook.app.ApiStructure.ServerCallback;
import jokesbook.app.Models.PrayListModel.Prays;
import jokesbook.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class UserDetail extends AppCompatActivity {

    private RecyclerView recyclerView;
    RelativeLayout back_rel;
    private TextView author_title_name;

    private UserDetailAdapter adapter;
    public static List<Prays> prayList;
    ArrayList<String> pray_remain_array;
    LinearLayoutManager mLayoutManager;
    boolean isLoading =true ;
    ProgressBar progressBar,progress;
    SharedPreferences mPrefs;
    private SwipeRefreshLayout swipeContainer;
    public static String positionForRefresh="";
    public static String current_add="one";
    public static int show_add_after=0;

    public static String p_user_full_name;
    public static String p_user_achievements;
    public static String p_user_country;
    public static String p_user_DOB;
    public static String p_user_about;
    public static String p_user_quotes_count;
    public static String p_user_age;
    public static String p_user_id;
    public static String p_user_pray_count;
    public static String p_user_prayed_count;
    public static String p_user_dp;
    public static String refreshposition = "";


    @Override
    protected void onResume() {
        super.onResume();
        if(!prayList.isEmpty() && prayList != null) {

//            adapter.notifyDataSetChanged();
//            adapter.refresh();
            if (!refreshposition.equals("")) {
                getPrayApi(Constants.URL.GETUSER);

                adapter.notifyItemChanged(Integer.parseInt(refreshposition));
                refreshposition = "";
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);

        AdView adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);

        adView.setAdUnitId(String.valueOf(R.string.unitid));

        MobileAds.initialize(this,String.valueOf(R.string.add_app_id));
        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

//        AnalyticsTrackers.initialize(this);
//
//        AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP, "UA-123157129-1");
//        AnalyticsTrackers.getInstance().trackScreenView("User Detail Activity");
//

        // firebase analytics
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundlee = new Bundle();
        bundlee.putString(FirebaseAnalytics.Param.ITEM_NAME, "User Deatil Activity");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundlee);

        // firebase analytics



        back_rel = findViewById(R.id.back_rel_user);
        back_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        author_title_name = findViewById(R.id.user_name_title);

        p_user_id = getIntent().getStringExtra("AUTH_ID");

        prayList = new ArrayList<>();

        pray_remain_array = new ArrayList<>();
        progressBar=findViewById(R.id.progressBar_user);
        progress=findViewById(R.id.progress_user);
        progress.setVisibility(View.VISIBLE);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.user_swipeContainer);
        swipeContainer.setColorSchemeColors(Color.parseColor("#9F0030"));

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
//                fetchTimelineAsync(0);
//                Toast.makeText(getActivity(), " Swipe To Refresh ", Toast.LENGTH_SHORT).show();
                getPrayApi(Constants.URL.GETUSER);
            }
        });



        recyclerView = (RecyclerView) findViewById(R.id.user_rv);

        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.getItemAnimator().endAnimations();

        progressBar.setVisibility(View.VISIBLE);
        getPrayApi(Constants.URL.GETUSER);


    }

    private void getPrayApi(String url) {


        // hit api............
//        final ProgressDialog ringProgressDialog;
//
//        ringProgressDialog = ProgressDialog.show(getActivity(), "", "please wait", true);
//        ringProgressDialog.setCancelable(false);
//        ringProgressDialog.show();

        mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String token = mPrefs.getString("USER_TOKEN", "");

//        if(token.equals(InProfileSettingChangePassword.updated_token)){
//            Toast.makeText(getActivity(), "token same", Toast.LENGTH_SHORT).show();
//
//
//        }
//
//        else {
//            Toast.makeText(getActivity(), "token changed............", Toast.LENGTH_SHORT).show();
//
//
//        }

        Map<String, String> postParam = new HashMap<String, String>();


        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("x-sh-auth", token);

        ApiModelClass.GetApiResponse(Request.Method.GET, url+p_user_id+"/0/10", UserDetail.this, postParam, headers, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result, String ERROR) {

                if (ERROR.isEmpty()) {

                    try {

                        prayList.clear();
                        Prays empty_feed_list_obj = new Prays();
                        prayList.add(empty_feed_list_obj);


                        JSONObject jsonObject = new JSONObject(String.valueOf(result));

                        String code = jsonObject.getString("code");
                        JSONObject userObj = jsonObject.getJSONObject("user");
                        String user_id = userObj.getString("_id");
                        String user_name = userObj.getString("full_name");
                        String user_country = userObj.getString("country");
                        String user_DOB = userObj.getString("date_of_birth");
                        String user_age = userObj.getString("age");
                        String user_about = userObj.getString("about");
                        String user_achievements = userObj.getString("achievements");
                        int user_pray_count = userObj.getInt("pray_requests_count");
                        int user_prayed_count = userObj.getInt("prayed_count");
                        int user_quotes_count = userObj.getInt("quotes_count");
                        String user_dp = userObj.getString("dp_active_file");

                        p_user_full_name = user_name;
                        p_user_achievements = user_achievements;
                        p_user_dp = user_dp;
                        p_user_country = user_country;
                        p_user_DOB = user_DOB;
                        p_user_age = user_age;
                        p_user_about = user_about;
                        p_user_quotes_count = String.valueOf(user_quotes_count);
                        p_user_pray_count = String.valueOf(user_pray_count);
                        p_user_prayed_count = String.valueOf(user_prayed_count);
                        author_title_name.setText(p_user_full_name);



                        JSONArray files = jsonObject.getJSONArray("pray_requests");

//                        ArrayList<Feeds> feeds_arr=new ArrayList<>();
//                        isLoading=true;
                        if (files.length() > 0) {



                            for (int a = 0; a < files.length(); a++) {


                                Prays prays_obj = new Prays();
                                JSONObject jsonObject1 = files.getJSONObject(a);

                                String pray_id = jsonObject1.getString("_id");
                                String title = jsonObject1.getString("title");
                                String approved = jsonObject1.getString("approved");

                                boolean liked_by_me=jsonObject1.getBoolean("liked_by_me");
                                boolean admin_quote=jsonObject1.getBoolean("admin_quote");
                                int liked_count = jsonObject1.getInt("liked_count");
                                int comments_count = jsonObject1.getInt("comment_count");

                                JSONObject user_Obj = new JSONObject(jsonObject1.getString("user"));

                                String userinner_id = user_Obj.getString("_id");
                                String user_full_name = user_Obj.getString("full_name");
                                String user_dp_active_file = user_Obj.getString("dp_active_file");




                                prays_obj.setPrayed_by_me(liked_by_me);
                                prays_obj.setAdmin_quote(admin_quote);
                                prays_obj.setUser_id(userinner_id);
                                prays_obj.setUser_full_name(user_full_name);
                                prays_obj.setUser_dp(user_dp_active_file);
                                prays_obj.setText(title);
                                prays_obj.setPray_id(pray_id);
                                prays_obj.setComments_count(comments_count);
                                prays_obj.setLiked_count(liked_count);



                                prayList.add(prays_obj);
                            }

                        } else {


                        }

                        String load_more_url = jsonObject.getString("load_more_url");

                        pray_remain_array.clear();
                        pray_remain_array.add(load_more_url);

                        adapter = new UserDetailAdapter(UserDetail.this, prayList);

//                        recyclerView.setHasFixedSize(true);
                        recyclerView.setItemViewCacheSize(20);
                        recyclerView.setDrawingCacheEnabled(true);
                        recyclerView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);



                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(mLayoutManager);

                        recyclerView.setOnScrollListener(recyclerViewOnScrollListener);


//                        ringProgressDialog.dismiss();

                        progressBar.setVisibility(View.GONE);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    swipeContainer.setRefreshing(false);
                    progress.setVisibility(View.GONE);

                } else {
                    progress.setVisibility(View.GONE);
                    swipeContainer.setRefreshing(false);

                    progressBar.setVisibility(View.GONE);

                    if(ERROR.equals("401")){

//                        SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                        SharedPreferences.Editor mEditor = mPrefs.edit();
                        mEditor.putString("USER_EMAIL", "");
                        mEditor.putString("USER_PASSWORD", "");
                        mEditor.putString("USER_TOKEN", "");
                        mEditor.putString("USER_PIC", "");
                        mEditor.apply();
                        Intent intt = new Intent(UserDetail.this, FirstActivity.class);
                        startActivity(intt);
                        finish();
                    }
                    else {

//                    ringProgressDialog.dismiss();
//                        Toast.makeText(getActivity(), ERROR, Toast.LENGTH_SHORT).show();

                    }

                }

            }
        });
        // hit api--------------------


    }

    private RecyclerView.OnScrollListener recyclerViewOnScrollListener = new RecyclerView.OnScrollListener() {
        @Override
        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {

            super.onScrollStateChanged(recyclerView, newState);
        }

        @Override
        public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
            super.onScrolled(recyclerView, dx, dy);

            int visibleItemCount ,totalItemCount,pastVisiblesItems,lastVisibleItem,threshhold=1;
            if(dy > 0) {

                visibleItemCount = mLayoutManager.getChildCount();
                totalItemCount = mLayoutManager.getItemCount();
                pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();


//            int pastVisiblesItems = mLayoutManager.findFirstVisibleItemPosition();
                int a = 10;
                int b = a;

//                if (!isLoading && !isLastPage) {
//                    if (visibleItemCount >= totalItemCount
//
//                            && totalItemCount >= PAGE_SIZE) {
//                        loadMoreItems();
//                    }
//                }

                if (isLoading) {

                    if ( (visibleItemCount + pastVisiblesItems) >= totalItemCount)

//                    if ( totalItemCount  <= (lastVisibleItem + threshhold))

                    {
                        isLoading = false;

                        loadMoreItems();
                    }

                }
            }
        }
    };

    private void getPrayApi2(String url) {

//        feedList.clear();
//        isLoading=true;
        // hit api............
//        final ProgressDialog ringProgressDialog;
//
//        ringProgressDialog = ProgressDialog.show(getActivity(), "", "please wait", true);
//        ringProgressDialog.setCancelable(false);
//        ringProgressDialog.show();

        SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String token = mPrefs.getString("USER_TOKEN", "");

        Map<String, String> postParam = new HashMap<String, String>();


        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("x-sh-auth", token);

        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.BASE_URL + url, this, postParam, headers, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result, String ERROR) {

                if (ERROR.isEmpty()) {

                    try {

//                        Feeds feed_list_obj = new Feeds();
                        isLoading=true;

                        ArrayList<Prays> test=new ArrayList<>();

                        JSONObject jsonObject = new JSONObject(String.valueOf(result));

                        String code = jsonObject.getString("code");
                        JSONObject userObj = jsonObject.getJSONObject("user");
                        String user_id = userObj.getString("_id");
                        String user_name = userObj.getString("full_name");
                        String user_country = userObj.getString("country");
                        String user_DOB = userObj.getString("date_of_birth");
                        String user_age = userObj.getString("age");
                        String user_about = userObj.getString("about");
                        String user_achievements = userObj.getString("achievements");
                        int user_pray_count = userObj.getInt("pray_requests_count");
                        int user_prayed_count = userObj.getInt("prayed_count");
                        int user_quotes_count = userObj.getInt("quotes_count");
                        String user_dp = userObj.getString("dp_active_file");

                        JSONArray files = jsonObject.getJSONArray("pray_requests");

//                        ArrayList<Feeds> feeds_arr=new ArrayList<>();
//                        isLoading=true;
                        if (files.length() > 0) {



                            for (int a = 0; a < files.length(); a++) {


                                Prays prays_obj = new Prays();
                                JSONObject jsonObject1 = files.getJSONObject(a);

                                String pray_id = jsonObject1.getString("_id");
                                String title = jsonObject1.getString("title");
                                String approved = jsonObject1.getString("approved");

                                boolean liked_by_me=jsonObject1.getBoolean("liked_by_me");
                                boolean admin_quote=jsonObject1.getBoolean("admin_quote");
                                int liked_count = jsonObject1.getInt("liked_count");
                                int comments_count = jsonObject1.getInt("comment_count");

                                JSONObject user_Obj = new JSONObject(jsonObject1.getString("user"));

                                String userinner_id = user_Obj.getString("_id");
                                String user_full_name = user_Obj.getString("full_name");
                                String user_dp_active_file = user_Obj.getString("dp_active_file");




                                prays_obj.setPrayed_by_me(liked_by_me);
                                prays_obj.setAdmin_quote(admin_quote);
                                prays_obj.setUser_id(userinner_id);
                                prays_obj.setUser_full_name(user_full_name);
                                prays_obj.setUser_dp(user_dp_active_file);
                                prays_obj.setText(title);
                                prays_obj.setPray_id(pray_id);
                                prays_obj.setComments_count(comments_count);
                                prays_obj.setLiked_count(liked_count);

                                test.add(prays_obj);
                            }


                            String load_more_url = jsonObject.getString("load_more_url");

                            pray_remain_array.clear();
                            pray_remain_array.add(load_more_url);
//
//

                            progressBar.setVisibility(View.GONE);


                            adapter.addfeed(test);



                        } else {

                            progressBar.setVisibility(View.GONE);

                        }

//                            ringProgressDialog.dismiss();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    progressBar.setVisibility(View.GONE);

//                    ringProgressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), ERROR, Toast.LENGTH_SHORT).show();


                }

            }
        });
        // hit api--------------------


    }



    private void loadMoreItems() {
        isLoading = false;


        getPrayApi2(pray_remain_array.get(0));
    }

}
