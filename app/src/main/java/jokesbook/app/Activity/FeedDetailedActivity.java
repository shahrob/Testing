package jokesbook.app.Activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Bundle;
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
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.firebase.analytics.FirebaseAnalytics;
import jokesbook.app.Adapters.FeedDetailAdapterWithHeader;
import jokesbook.app.ApiStructure.ApiModelClass;
import jokesbook.app.ApiStructure.Constants;
import jokesbook.app.ApiStructure.ServerCallback;
import jokesbook.app.Fragments.AllFragment;
import jokesbook.app.Fragments.RandomFeedFragment;
import jokesbook.app.Models.FeedDetailModel.FeedDetail;
import jokesbook.app.Models.FeedListModel.Comments;
import jokesbook.app.R;
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

public class FeedDetailedActivity extends Activity {



    private RecyclerView recyclerView;
    private FeedDetailAdapterWithHeader adapter;
    private List<Comments> commentList;
    public static FeedDetail feed_detail_obj;
    RecyclerView.LayoutManager mLayoutManager;

    RelativeLayout back_rel,post_comment_rel, feed_profile_img_rel;
    EditText comment_text;
    CircleImageView feed_profile_img;
    ProgressBar progress_post,progress;
    TextView post;

    public static String keyboard_status,Feed_Id,notification, coming;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_detailed);

        Initialization();
        AdView adView;
        adView = new AdView(this);
        adView.setAdSize(AdSize.BANNER);

        adView.setAdUnitId(String.valueOf(R.string.unitid));

        MobileAds.initialize(this,String.valueOf(R.string.add_app_id));
        adView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        adView.loadAd(adRequest);

//        AnalyticsTrackers.initialize(this);
//
//        AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP, "UA-123157129-1");
//        AnalyticsTrackers.getInstance().trackScreenView("Feed Detail Activity");



        // firebase analytics
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundlee = new Bundle();
        bundlee.putString(FirebaseAnalytics.Param.ITEM_NAME, "Feed Detail Activity");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundlee);

        // firebase analytics


        progress.setVisibility(View.VISIBLE);
        SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        final String USER_PIC = mPrefs.getString("USER_DP", "");

         keyboard_status = getIntent().getStringExtra("KEYBOADR_STATUS");
         Feed_Id = getIntent().getStringExtra("Feed_id");
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
         else
             {

             }

//         feed_profile_img.setImageURI(Uri.parse(Constants.URL.BASE_URL+USER_PIC));

        feed_profile_img_rel.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        Picasso.with(FeedDetailedActivity.this).load(Constants.URL.BASE_URL+USER_PIC)
                .error(R.drawable.profile_icon)
                .into(feed_profile_img);



        commentList= new ArrayList<Comments>();
        Comments enter_empty_obj_first=new Comments();
        commentList.add(enter_empty_obj_first);

       FeedDetailApi(Feed_Id);




//
//        adapter = new FeedDetailAdapterWithHeader(FeedDetailedActivity.this, commentList, "");
//
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(FeedDetailedActivity.this);
//
//        recyclerView.setLayoutManager(mLayoutManager);
//
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//
//        recyclerView.setAdapter(adapter);
//
//        // for smooth scrool
//        RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(FeedDetailedActivity.this) {
//            @Override
//            protected int getVerticalSnapPreference() {
//                return LinearSmoothScroller.SNAP_TO_END;
//            }
//
//            @Override
//            protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
//                return 0.0625f;
//            }
//        };
//
//        smoothScroller.setTargetPosition(commentList.size() - 1);
//        mLayoutManager.startSmoothScroll(smoothScroller);
//        // for smooth scrool end
//
//
//        adapter.notifyDataSetChanged();




        back_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment_text.setCursorVisible(false);



                ActivityManager mngr = (ActivityManager) getSystemService( ACTIVITY_SERVICE );

                List<ActivityManager.RunningTaskInfo> taskList = mngr.getRunningTasks(10);

                if(taskList.get(0).numActivities == 1) {

                    Intent inn = new Intent(FeedDetailedActivity.this, NavigationDrawerActivity.class);
                    inn.putExtra("FromNotification",false);

                    startActivity(inn);
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);



//                    Toast.makeText(FeedDetailedActivity.this,"This is last activity in the stack",Toast.LENGTH_LONG).show();
                }
                else {

                    hideKeyboard(v);
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);


                }




//                hideKeyboard(v);
//                finish();
//                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            }
        });



//        post_comment_rel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(final View v) {
//
//
//                post.setText("");
//                progress_post.setVisibility(View.VISIBLE);
//
//                final String comment = comment_text.getText().toString();
//
//                if (comment.isEmpty()) {
//                    Toast.makeText(FeedDetailedActivity.this, "Enter some thing first", Toast.LENGTH_SHORT).show();
//
//                    progress_post.setVisibility(View.GONE);
//                    post.setText("Post");
//
//                } else {
//
//
////                     hit api............
////                    final ProgressDialog ringProgressDialog;
////
////                    ringProgressDialog = ProgressDialog.show(FeedDetailedActivity.this, "", "please wait", true);
////                    ringProgressDialog.setCancelable(false);
////
////                    ringProgressDialog.show();
//
//
//                    final SharedPreferences mPrefs = FeedDetailedActivity.this.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
//                    String token = mPrefs.getString("USER_TOKEN", "");
//                    final String USER_PIC = mPrefs.getString("USER_PIC", "");
//
//
//
//                    final Map<String, String> postParam = new HashMap<String, String>();
//                    postParam.put("comment", comment);
//
//
//                    HashMap<String, String> headers = new HashMap<String, String>();
//                    headers.put("x-sh-auth", token);
//
//                    ApiModelClass.GetApiResponse(Request.Method.POST, Constants.URL.ADD_COMMENT+Feed_Id, FeedDetailedActivity.this, postParam, headers, new ServerCallback() {
//                        @Override
//                        public void onSuccess(JSONObject result, String ERROR) {
//
//                            if (ERROR.isEmpty()) {
//
//                                try {
//
////                        Feeds feed_list_obj = new Feeds();
//
//                                    InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
//                                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
//
//                                    JSONObject jsonObject = new JSONObject(String.valueOf(result));
//
//
//                                    JSONObject media = jsonObject.getJSONObject("media");
//
//                                    String feed_id = media.getString("_id");
//                                    int comments_count = media.getInt("comments_count");
//
//                                    // set counts to  subscribed feed list
//
////                                    for(int a = 0; a< SubscribedFeedFragment.feedList.size(); a++){
////
////                                        String id=SubscribedFeedFragment.feedList.get(a).get_id();
////                                        if(id.equals(feed_id)){
////
////
////                                            SubscribedFeedFragment.feedList.get(a).setComments_count(comments_count);
////                                            break;
////                                        }
////
////                                    }
//
//                                    // set counts to  subscribed feed list
//
//                                    if( RandomFeedFragment.feedList == null || RandomFeedFragment.feedList.isEmpty() ){}
//                                    else {
//                                        for (int a = 0; a < RandomFeedFragment.feedList.size(); a++) {
//
//                                            String id = RandomFeedFragment.feedList.get(a).get_id();
//                                            if (id.equals(feed_id)) {
//
//                                                RandomFeedFragment.feedList.get(a).setComments_count(comments_count);
//
//                                                break;
//                                            }
//                                        }
//                                    }
//                                    if( AllFragment.feedList == null || AllFragment.feedList.isEmpty() ){}
//                                    else {
//                                        for (int a = 0; a < AllFragment.feedList.size(); a++) {
//
//                                            String id = AllFragment.feedList.get(a).get_id();
//                                            if (id.equals(feed_id)) {
//
//                                                AllFragment.feedList.get(a).setComments_count(comments_count);
//
//                                                break;
//                                            }
//                                        }
//                                    }
//
//                                    // set counts to  subscribed feed list
//
//                                    feed_detail_obj.setComments_count(comments_count);
//
//                                    commentList.clear();
//                                    Comments enter_empty_obj_first=new Comments();
//                                    commentList.add(enter_empty_obj_first);
//                                    JSONArray comments_array = media.getJSONArray("comments");
//
//                                    for (int b = 0; b < comments_array.length(); b++) {
//
//                                        Comments comment_obj = new Comments();
//                                        JSONObject jsonObject2 = comments_array.getJSONObject(b);
//
//                                        String comment_id = jsonObject2.getString("_id");
//
//                                        try {
//
//                                            JSONObject comment_user = new JSONObject(jsonObject2.getString("user"));
//                                            String comment_full_name = comment_user.getString("full_name");
//                                            String comment_user_id = comment_user.getString("_id");
//                                            String dp_active_file = comment_user.getString("dp_active_file");
//                                            comment_obj.setFull_name(comment_full_name);
//                                            comment_obj.setUser_id(comment_user_id);
//                                            comment_obj.setActive_dp(dp_active_file);
//
//                                        }
//                                        catch (Exception e){
//                                            comment_obj.setFull_name("api ma masla ha ");
//                                            comment_obj.setUser_id("api ma masla ha ");
//
//                                        }
//                                        String comment = jsonObject2.getString("comment");
//                                        String created_at = jsonObject2.getString("created_at");
//                                        int liked_count = jsonObject2.getInt("liked_count");
//                                        boolean liked_by_me = jsonObject2.getBoolean("liked_by_me");
//
//                                        comment_obj.setComment_id(comment_id);
//                                        comment_obj.setLiked_count(liked_count);
//                                        comment_obj.setComment(comment);
//                                        comment_obj.setLiked_by_me(liked_by_me);
//                                        comment_obj.setCreated_at(created_at);
//                                        comment_obj.setFeed_id(feed_id);
//
//                                        commentList.add(comment_obj);
//
//                                    }
//
//                                    recyclerView.scrollToPosition(commentList.size()-1);
//
//                                    adapter.notifyDataSetChanged();
//
//                                    comment_text.setText("");
//                                        comment_text.setCursorVisible(false);
//
//                                    progress_post.setVisibility(View.GONE);
//                                    post.setText("Post");
//                                }
//                                catch (JSONException e) {
//                                    progress_post.setVisibility(View.GONE);
//                                    post.setText("Post");
//                                    e.printStackTrace();
//                                }
//                            } else {
//                                progress_post.setVisibility(View.GONE);
//                                post.setText("Post");
//                                Toast.makeText(FeedDetailedActivity.this, ERROR, Toast.LENGTH_SHORT).show();
//
//
//                            }
//                        }
//                    });
//
//                    // hit api--------------------
//
//                }
//            }
//        });
    }

    private void FeedDetailApi(String Feed_Id) {


        SharedPreferences mPrefs = FeedDetailedActivity.this.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String token = mPrefs.getString("USER_TOKEN", "");

        Map<String, String> postParam = new HashMap<String, String>();


        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("x-sh-auth", token);

        ApiModelClass.GetApiResponse(Request.Method.GET,  Constants.URL.GET+Feed_Id, FeedDetailedActivity.this, postParam, headers, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result, String ERROR) {

                if (ERROR.isEmpty()) {

                    try {

                        JSONObject jsonObject = new JSONObject(String.valueOf(result));

                         feed_detail_obj=new FeedDetail();
                        JSONObject media = jsonObject.getJSONObject("media");

                        JSONObject author = media.getJSONObject("author");
                        String auther_id=author.getString("_id");
                        String auther_name=author.getString("name");
                        String auther_img=author.getString("img");
                        boolean author_check = author.getBoolean("author");
                        String feed_id=media.getString("_id");
                        String title=media.getString("title");
                        boolean feed_liked_by_me=media.getBoolean("liked_by_me");
                        String large_image=media.getString("large_image");
                        String small_image=media.getString("small_image");
                        String createdAt=media.getString("createdAt");
                        int liked_countt=media.getInt("liked_count");
                        int comments_count=media.getInt("comments_count");
                        String feed_blog_url="https://pk.zapmeta.ws/ws?q=blogs%20on%20websites&asid=zm_pk_gb_1_cg1_05&abt=1&mt=b&nw=g&de=c&ap=&kid=kwd-11480526309&aid=35417532236&ac=469&cid=686767819&aid=35417532236&kid=kwd-11480526309&locale=en_PK&gclid=Cj0KCQiAhs79BRD0ARIsAC6XpaWWdElwtrj-aSEJMNpbXYMrEVj2HGO-qAlNLVJfyJ-GOsjxvMzTXbkaAq1rEALw_wcB";

                        feed_detail_obj.setFeed_id(feed_id);
                        feed_detail_obj.setTitle(title);
                        feed_detail_obj.setLiked_by_me(feed_liked_by_me);
                        feed_detail_obj.setLarge_image(large_image);
                        feed_detail_obj.setSmall_image(small_image);
                        feed_detail_obj.setCreatedAt(createdAt);
                        feed_detail_obj.setAuther_name(auther_name);
                        feed_detail_obj.setAuther_id(auther_id);
                        feed_detail_obj.setAuther_img(auther_img);
                        feed_detail_obj.setLiked_count(liked_countt);
                        feed_detail_obj.setComments_count(comments_count);
                        feed_detail_obj.setAuthor_check(author_check);
                        feed_detail_obj.setFeed_blog_url(feed_blog_url);



                        JSONArray comments_array = media.getJSONArray("comments");

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
                            comment_obj.setFeed_id(feed_id);





                            commentList.add(comment_obj);

                        }







                        // for going next screen work////////////////////////////////////


                        if(keyboard_status.equals("open_keyboard")) {

                            comment_text.requestFocus();
//                            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);

                            // Request focus and show soft keyboard automatically
                            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,0);

                            adapter = new FeedDetailAdapterWithHeader(FeedDetailedActivity.this, commentList, "");

                            mLayoutManager = new LinearLayoutManager(FeedDetailedActivity.this);

                            recyclerView.setLayoutManager(mLayoutManager);

                            recyclerView.setItemAnimator(new DefaultItemAnimator());

                            recyclerView.setAdapter(adapter);

                            adapter.notifyDataSetChanged();

                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    // for smooth scrool
                                    RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(FeedDetailedActivity.this) {
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
                        if(keyboard_status.equals("feed_img")) {



                            adapter = new FeedDetailAdapterWithHeader(FeedDetailedActivity.this, commentList, "");

                            mLayoutManager = new LinearLayoutManager(FeedDetailedActivity.this);

                            recyclerView.setLayoutManager(mLayoutManager);


                            recyclerView.setItemAnimator(new DefaultItemAnimator());

                            recyclerView.setAdapter(adapter);




                            adapter.notifyDataSetChanged();

                        }
                        if(keyboard_status.equals("view_all_comment")) {



                            adapter = new FeedDetailAdapterWithHeader(FeedDetailedActivity.this, commentList, "");

                            mLayoutManager = new LinearLayoutManager(FeedDetailedActivity.this);

                            recyclerView.setLayoutManager(mLayoutManager);

                            recyclerView.setItemAnimator(new DefaultItemAnimator());

                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();


                            Handler handler = new Handler();
                            handler.postDelayed(new Runnable() {
                                @Override
                                public void run() {

                                    // for smooth scrool
                                    RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(FeedDetailedActivity.this) {
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


                            adapter = new FeedDetailAdapterWithHeader(FeedDetailedActivity.this, commentList, "");

                            mLayoutManager = new LinearLayoutManager(FeedDetailedActivity.this);

                            recyclerView.setLayoutManager(mLayoutManager);

                            recyclerView.setItemAnimator(new DefaultItemAnimator());

                            recyclerView.setAdapter(adapter);
                            adapter.notifyDataSetChanged();


//                            Handler handler = new Handler();
//                            handler.postDelayed(new Runnable() {
//                                @Override
//                                public void run() {
//
//                                    // for smooth scrool
//                                    RecyclerView.SmoothScroller smoothScroller = new LinearSmoothScroller(FeedDetailedActivity.this) {
//                                        @Override
//                                        protected int getVerticalSnapPreference() {
//                                            return LinearSmoothScroller.SNAP_TO_END;
//                                        }
//
//                                        @Override
//                                        protected float calculateSpeedPerPixel(DisplayMetrics displayMetrics) {
//                                            return 0.0625f;
//                                        }
//                                    };
//
//                                    smoothScroller.setTargetPosition(commentList.size()-1);
//                                    mLayoutManager.startSmoothScroll(smoothScroller);
//                                    // for smooth scrool end
//
//                                }
//                            }, 600);


                        }
                        if(keyboard_status.equals("update_comment")) {



                            adapter = new FeedDetailAdapterWithHeader(FeedDetailedActivity.this, commentList, "");

                            mLayoutManager = new LinearLayoutManager(FeedDetailedActivity.this);

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
        post_comment_rel=findViewById(R.id.post_comment_rel);
        comment_text=findViewById(R.id.comment_text);
        recyclerView = (RecyclerView) findViewById(R.id.rv);
        feed_profile_img =  findViewById(R.id.feed_profile_img);
        feed_profile_img_rel =  findViewById(R.id.feed_profile_img_rel);
        progress_post=findViewById(R.id.progress_post);
        post=findViewById(R.id.post);
        progress=findViewById(R.id.progress);

    }

    @Override
    protected void onResume() {
        super.onResume();
        FeedDetailApi(Feed_Id);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        FeedDetailApi(Feed_Id);
    }
}
