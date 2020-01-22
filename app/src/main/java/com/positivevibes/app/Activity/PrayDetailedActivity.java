package com.positivevibes.app.Activity;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSmoothScroller;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.positivevibes.app.Adapters.PrayDetailedAdapterwithHeader;
import com.positivevibes.app.ApiStructure.ApiModelClass;
import com.positivevibes.app.ApiStructure.Constants;
import com.positivevibes.app.ApiStructure.ServerCallback;
import com.positivevibes.app.Fragments.AllFragment;
import com.positivevibes.app.Fragments.PrayFragment;
import com.positivevibes.app.Models.FeedListModel.Comments;
import com.positivevibes.app.Models.PrayListModel.PrayDetail;
import com.positivevibes.app.R;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import me.leolin.shortcutbadger.ShortcutBadger;

public class PrayDetailedActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private PrayDetailedAdapterwithHeader adapter;
    private List<Comments> commentList;
    public static PrayDetail pray_detail_obj;
    RecyclerView.LayoutManager mLayoutManager;

    RelativeLayout back_rel,post_comment_rel, feed_profile_img_rel;
    EditText comment_text;
    CircleImageView feed_profile_img;
    ProgressBar progress_post,progress;
    TextView post;

    public static String keyboard_status,Pray_Id,notification, coming;

    @Override
    protected void onResume() {
        super.onResume();
//        PrayDetailApi(Pray_Id);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        comment_text.setCursorVisible(false);



        ActivityManager mngr = (ActivityManager) getSystemService( ACTIVITY_SERVICE );

        List<ActivityManager.RunningTaskInfo> taskList = mngr.getRunningTasks(10);

        if(taskList.get(0).numActivities == 1) {

            Intent inn = new Intent(PrayDetailedActivity.this, NavigationDrawerActivity.class);
            inn.putExtra("FromNotification",false);

            startActivity(inn);
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        }
        else {

            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);


        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pray_detailed);

        Initialization();

//        AnalyticsTrackers.initialize(this);
//
//        AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP, "UA-123157129-1");
//        AnalyticsTrackers.getInstance().trackScreenView("Pray Detail Activity");


        // firebase analytics
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundlee = new Bundle();
        bundlee.putString(FirebaseAnalytics.Param.ITEM_NAME, "Pray Detailed Activity");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundlee);

        // firebase analytics

        progress.setVisibility(View.VISIBLE);
        SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        final String USER_PIC = mPrefs.getString("USER_DP", "");

        keyboard_status = getIntent().getStringExtra("KEYBOADR_STATUS");
        Pray_Id = getIntent().getStringExtra("Feed_id");
        notification = getIntent().getStringExtra("Notification");
        coming = getIntent().getStringExtra("COMING");

        progress_post.setVisibility(View.GONE);

        if(notification.equals("true")){

            SharedPreferences mPrefs1=getSharedPreferences("NotificationBadgeCount",Context.MODE_PRIVATE);


            SharedPreferences.Editor editor=mPrefs1.edit();
            editor.putInt("count",0);
            editor.apply();

            ShortcutBadger.removeCount(this);

            // clear badge count
        }
        else {

        }

        feed_profile_img_rel.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        Picasso.with(PrayDetailedActivity.this).load(Constants.URL.BASE_URL+USER_PIC)
                .error(R.drawable.profile_icon)
                .into(feed_profile_img);

        commentList= new ArrayList<Comments>();
        Comments enter_empty_obj_first=new Comments();
        commentList.add(enter_empty_obj_first);

        PrayDetailApi(Pray_Id);


        back_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment_text.setCursorVisible(false);



                ActivityManager mngr = (ActivityManager) getSystemService( ACTIVITY_SERVICE );

                List<ActivityManager.RunningTaskInfo> taskList = mngr.getRunningTasks(10);

                if(taskList.get(0).numActivities == 1) {

                    Intent inn = new Intent(PrayDetailedActivity.this, NavigationDrawerActivity.class);
                    inn.putExtra("FromNotification",false);

                    startActivity(inn);
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

                }
                else {

                    hideKeyboard(v);
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);


                }

            }
        });

        post_comment_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {


                post.setText("");
                progress_post.setVisibility(View.VISIBLE);

                final String comment = comment_text.getText().toString();

                if (comment.isEmpty()) {
                    Toast.makeText(PrayDetailedActivity.this, "Enter some thing first", Toast.LENGTH_SHORT).show();

                    progress_post.setVisibility(View.GONE);
                    post.setText("Post");

                } else {

                    final SharedPreferences mPrefs = PrayDetailedActivity.this.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                    String token = mPrefs.getString("USER_TOKEN", "");
                    final String USER_PIC = mPrefs.getString("USER_PIC", "");



                    final Map<String, String> postParam = new HashMap<String, String>();
                    postParam.put("comment", comment);


                    HashMap<String, String> headers = new HashMap<String, String>();
                    headers.put("x-sh-auth", token);

                    ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.ADD_COMMENT+Pray_Id, PrayDetailedActivity.this, postParam, headers, new ServerCallback() {
                        @Override
                        public void onSuccess(JSONObject result, String ERROR) {

                            if (ERROR.isEmpty()) {

                                try {

//                        Feeds feed_list_obj = new Feeds();

                                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);

                                    JSONObject jsonObject = new JSONObject(String.valueOf(result));


                                    JSONObject media = jsonObject.getJSONObject("media");

                                    String feed_id = media.getString("_id");
                                    int comments_count = media.getInt("comments_count");

                                    if( AllFragment.feedList == null || AllFragment.feedList.isEmpty() ){}
                                    else {
                                        for (int a = 0; a < AllFragment.feedList.size(); a++) {

                                            String id = AllFragment.feedList.get(a).get_id();
                                            if (id.equals(feed_id)) {

                                                AllFragment.feedList.get(a).setComments_count(comments_count);

                                                break;
                                            }
                                        }
                                    }
                                    if( PrayFragment.praysList == null || PrayFragment.praysList.isEmpty() ){}
                                    else {
                                        for (int a = 0; a < PrayFragment.praysList.size(); a++) {

                                            String id = PrayFragment.praysList.get(a).getPray_id();
                                            if (id.equals(feed_id)) {

                                                PrayFragment.praysList.get(a).setComments_count(comments_count);

                                                break;
                                            }
                                        }
                                    }

                                    // set counts to  subscribed feed list

                                    pray_detail_obj.setComments_count(comments_count);

                                    commentList.clear();
                                    Comments enter_empty_obj_first=new Comments();
                                    commentList.add(enter_empty_obj_first);
                                    JSONArray comments_array = media.getJSONArray("comments");

                                    for (int b = 0; b < comments_array.length(); b++) {

                                        Comments comment_obj = new Comments();
                                        JSONObject jsonObject2 = comments_array.getJSONObject(b);

                                        String comment_id = jsonObject2.getString("_id");

                                        try {

                                            JSONObject comment_user = new JSONObject(jsonObject2.getString("user"));
                                            String comment_full_name = comment_user.getString("full_name");
                                            String comment_user_id = comment_user.getString("_id");
                                            String dp_active_file = comment_user.getString("dp_active_file");
                                            comment_obj.setFull_name(comment_full_name);
                                            comment_obj.setUser_id(comment_user_id);
                                            comment_obj.setActive_dp(dp_active_file);

                                        }
                                        catch (Exception e){
                                            comment_obj.setFull_name("api ma masla ha ");
                                            comment_obj.setUser_id("api ma masla ha ");

                                        }
                                        String comment = jsonObject2.getString("comment");
                                        String created_at = jsonObject2.getString("created_at");
                                        int liked_count = jsonObject2.getInt("liked_count");
                                        boolean liked_by_me = jsonObject2.getBoolean("liked_by_me");

                                        comment_obj.setComment_id(comment_id);
                                        comment_obj.setLiked_count(liked_count);
                                        comment_obj.setComment(comment);
                                        comment_obj.setLiked_by_me(liked_by_me);
                                        comment_obj.setCreated_at(created_at);
                                        comment_obj.setFeed_id(feed_id);

                                        commentList.add(comment_obj);

                                    }

                                    recyclerView.scrollToPosition(commentList.size()-1);

                                    adapter.notifyDataSetChanged();

                                    comment_text.setText("");
                                    comment_text.setCursorVisible(false);

                                    progress_post.setVisibility(View.GONE);
                                    post.setText("Post");
                                }
                                catch (JSONException e) {
                                    progress_post.setVisibility(View.GONE);
                                    post.setText("Post");
                                    e.printStackTrace();
                                }
                            } else {
                                progress_post.setVisibility(View.GONE);
                                post.setText("Post");
                                Toast.makeText(PrayDetailedActivity.this, ERROR, Toast.LENGTH_SHORT).show();


                            }
                        }
                    });

                    // hit api--------------------

                }
            }
        });

    }

    private void PrayDetailApi(String Pray_Id) {


        SharedPreferences mPrefs = PrayDetailedActivity.this.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String token = mPrefs.getString("USER_TOKEN", "");

        Map<String, String> postParam = new HashMap<String, String>();


        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("x-sh-auth", token);

        ApiModelClass.GetApiResponse(Request.Method.GET,  Constants.URL.GETPRAY+Pray_Id, PrayDetailedActivity.this, postParam, headers, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result, String ERROR) {

                if (ERROR.isEmpty()) {

                    try {


                        JSONObject jsonObject = new JSONObject(String.valueOf(result));

                        pray_detail_obj=new PrayDetail();
                        JSONObject pray_res = jsonObject.getJSONObject("pray_res");

                        JSONObject user = pray_res.getJSONObject("user");

                        String user_id=user.getString("_id");
                        String user_name=user.getString("full_name");
                        String user_dp=user.getString("dp_active_file");



                        String pray_id=pray_res.getString("_id");
                        String title=pray_res.getString("title");
                        boolean feed_liked_by_me=pray_res.getBoolean("liked_by_me");
                        String approved=pray_res.getString("approved");
                        int liked_countt=pray_res.getInt("liked_count");
                        int comments_count=pray_res.getInt("comment_count");



                        pray_detail_obj.setPray_id(pray_id);
                        pray_detail_obj.setTitle(title);
                        pray_detail_obj.setLiked_by_me(feed_liked_by_me);
                        pray_detail_obj.setApproved(approved);
                        pray_detail_obj.setUser_name(user_name);
                        pray_detail_obj.setUser_id(user_id);
                        pray_detail_obj.setUser_dp(user_dp);
                        pray_detail_obj.setLiked_count(liked_countt);
                        pray_detail_obj.setComments_count(comments_count);



                        JSONArray comments_array = pray_res.getJSONArray("comments");

//                        ArrayList<Comments> comment_arr = new ArrayList<>();

                        for (int b = 0; b < comments_array.length(); b++) {

                            Comments comment_obj = new Comments();
                            JSONObject jsonObject2 = comments_array.getJSONObject(b);

                            String created_at = jsonObject2.getString("created_at");
                            int liked_count = jsonObject2.getInt("liked_count");
                            boolean liked_by_me = jsonObject2.getBoolean("liked_by_me");
                            String comment_id = jsonObject2.getString("_id");
                            String comment = jsonObject2.getString("comment");



                            JSONObject comment_user = new JSONObject(jsonObject2.getString("user"));
                            String comment_user_id = comment_user.getString("_id");
                            String comment_full_name = comment_user.getString("full_name");
                            String dp_active_file = comment_user.getString("dp_active_file");



                            comment_obj.setCreated_at(created_at);
                            comment_obj.setLiked_count(liked_count);
                            comment_obj.setLiked_by_me(liked_by_me);
                            comment_obj.setComment_id(comment_id);
                            comment_obj.setComment(comment);
                            comment_obj.setUser_id(comment_user_id);
                            comment_obj.setFull_name(comment_full_name);
                            comment_obj.setActive_dp(dp_active_file);
                            comment_obj.setFeed_id(pray_id);

                            commentList.add(comment_obj);

                        }

                        // for going next screen work////////////////////////////////////


                        if(keyboard_status.equals("open_keyboard")) {

                            comment_text.requestFocus();
//                            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

                            // Request focus and show soft keyboard automatically
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);

                            adapter = new PrayDetailedAdapterwithHeader(PrayDetailedActivity.this, commentList, "");

                            mLayoutManager = new LinearLayoutManager(PrayDetailedActivity.this);

                            recyclerView.setLayoutManager(mLayoutManager);

                            recyclerView.setItemAnimator(new DefaultItemAnimator());

                            recyclerView.setAdapter(adapter);

                            adapter.notifyDataSetChanged();

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    // for smooth scrool
                                    RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(PrayDetailedActivity.this) {
                                        @Override
                                        protected int getVerticalSnapPreference() {
                                            return LinearSmoothScroller.SNAP_TO_END;
                                        }

                                        @Override
                                        protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                                            return 0.0625f;
                                        }
                                    };

                                    smoothScroller.setTargetPosition(commentList.size() - 1);
                                    mLayoutManager.startSmoothScroll(smoothScroller);
                                    // for smooth scrool end

                                }
                            }, 600);

                        }
                        if(keyboard_status.equals("PrayText")) {



                            adapter = new PrayDetailedAdapterwithHeader(PrayDetailedActivity.this, commentList, "");

                            mLayoutManager = new LinearLayoutManager(PrayDetailedActivity.this);

                            recyclerView.setLayoutManager(mLayoutManager);


                            recyclerView.setItemAnimator(new DefaultItemAnimator());

                            recyclerView.setAdapter(adapter);




                            adapter.notifyDataSetChanged();

                        }
                        if(keyboard_status.equals("view_all_comment")) {



                            adapter = new PrayDetailedAdapterwithHeader(PrayDetailedActivity.this, commentList, "");

                            mLayoutManager = new LinearLayoutManager(PrayDetailedActivity.this);

                            recyclerView.setLayoutManager(mLayoutManager);

                            recyclerView.setItemAnimator(new DefaultItemAnimator());

                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();


                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    // for smooth scrool
                                    RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(PrayDetailedActivity.this) {
                                        @Override
                                        protected int getVerticalSnapPreference() {
                                            return LinearSmoothScroller.SNAP_TO_END;
                                        }

                                        @Override
                                        protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
                                            return 0.0625f;
                                        }
                                    };

                                    smoothScroller.setTargetPosition(commentList.size() -  ( commentList.size() - 8 ));
                                    mLayoutManager.startSmoothScroll(smoothScroller);
                                    // for smooth scrool end

                                }
                            }, 500);




                        }



                        if(keyboard_status.equals("when_new_comment")) {

//                            comment_text.requestFocus();
//                            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);


                            adapter = new PrayDetailedAdapterwithHeader(PrayDetailedActivity.this, commentList, "");

                            mLayoutManager = new LinearLayoutManager(PrayDetailedActivity.this);

                            recyclerView.setLayoutManager(mLayoutManager);

                            recyclerView.setItemAnimator(new DefaultItemAnimator());

                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();

                        }
                        if(keyboard_status.equals("update_comment")) {

                            adapter = new PrayDetailedAdapterwithHeader(PrayDetailedActivity.this, commentList, "");

                            mLayoutManager = new LinearLayoutManager(PrayDetailedActivity.this);

                            recyclerView.setLayoutManager(mLayoutManager);

                            recyclerView.setItemAnimator(new DefaultItemAnimator());

                            recyclerView.setAdapter(adapter);
                            recyclerView.scrollToPosition(EditCommentActivity.position);
                            adapter.notifyDataSetChanged();




                        }


                        // for going next screen work////////////////////////////////////




                        progress.setVisibility(View.GONE);

                    } catch (JSONException e) {
                        progress.setVisibility(View.GONE);
                        e.printStackTrace();
                    }


                }
                progress.setVisibility(View.GONE);
            }
        });


    }

    protected void hideKeyboard(View view)
    {

//
//        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }


    private void Initialization() {

        back_rel=findViewById(R.id.back_rel);
        feed_profile_img_rel=findViewById(R.id.feed_profile_img_rel);
        post_comment_rel=findViewById(R.id.post_comment_rel);
        comment_text=findViewById(R.id.comment_text);
        recyclerView = findViewById(R.id.rv_praydetail);
        feed_profile_img =  findViewById(R.id.feed_profile_img);
        progress_post=findViewById(R.id.progress_post);
        post=findViewById(R.id.post);
        progress=findViewById(R.id.progress);

    }
}
