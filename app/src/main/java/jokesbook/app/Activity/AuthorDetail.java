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
import jokesbook.app.Adapters.AuthorDetailAdapter;
import jokesbook.app.ApiStructure.ApiModelClass;
import jokesbook.app.ApiStructure.Constants;
import jokesbook.app.ApiStructure.ServerCallback;
import jokesbook.app.Models.FeedListModel.CategoriesList;
import jokesbook.app.Models.FeedListModel.Feeds;
import jokesbook.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AuthorDetail extends AppCompatActivity {

    private RecyclerView recyclerView;
    RelativeLayout back_rel;
    private TextView author_title_name, author_name_title_fix;

    private AuthorDetailAdapter adapter;
    public static List<Feeds> feedList;
    ArrayList<String> feed_remain_array;
    LinearLayoutManager mLayoutManager;
    boolean isLoading =true ;
    ProgressBar progressBar,progress;
    SharedPreferences mPrefs;
    private SwipeRefreshLayout swipeContainer;
    public static String positionForRefresh="";
    public static String current_add="one";
    public static int show_add_after=0;

    public static String p_author_detail_image;
    public static String p_author_detail_name;
    public static String p_author_achievements;
    public static String p_author_country;
    public static String p_author_born;
    public static String p_author_dismissed;
    public static String p_author_bio;
    public static String p_author_quotes_count;
    public static String p_author_age;
    public static String p_author_id;

    AdView banner_add;

    ArrayList<CategoriesList> categoriesarrayList;

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_author_detail);
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
//        AnalyticsTrackers.getInstance().trackScreenView("Author Detail Activity");



        // firebase analytics
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundlee = new Bundle();
        bundlee.putString(FirebaseAnalytics.Param.ITEM_NAME, "Auther Detail Activity");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundlee);

        // firebase analytics



        back_rel = findViewById(R.id.back_rel);
        back_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        author_title_name = findViewById(R.id.author_name_title);
        author_name_title_fix = findViewById(R.id.author_name_title_fix);

        Bundle bundle = getIntent().getExtras();
        p_author_id = bundle.getString("AUTH_ID");

        feedList = new ArrayList<>();

        feed_remain_array = new ArrayList<>();
        progressBar=findViewById(R.id.progressBar5);
        progress=findViewById(R.id.progress);
        progress.setVisibility(View.VISIBLE);
        swipeContainer = (SwipeRefreshLayout) findViewById(R.id.author_swipeContainer);
        swipeContainer.setColorSchemeColors(Color.parseColor("#9F0030"));

        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
//                fetchTimelineAsync(0);
//                Toast.makeText(getActivity(), " Swipe To Refresh ", Toast.LENGTH_SHORT).show();
                getFeedApi(Constants.URL.AUTHORFEEDS);
            }
        });



        recyclerView = (RecyclerView) findViewById(R.id.author_rv);

        mLayoutManager = new LinearLayoutManager(this);
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerView.getItemAnimator().endAnimations();

        progressBar.setVisibility(View.VISIBLE);
        getFeedApi(Constants.URL.AUTHORFEEDS);


    }

    private void getFeedApi(String url) {


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

        ApiModelClass.GetApiResponse(Request.Method.GET, url+p_author_id, AuthorDetail.this, postParam, headers, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result, String ERROR) {

                if (ERROR.isEmpty()) {

                    try {

                        feedList.clear();
                        Feeds empty_feed_list_obj = new Feeds();
                        feedList.add(empty_feed_list_obj);


                        JSONObject jsonObject = new JSONObject(String.valueOf(result));

                        String code = jsonObject.getString("code");
                        JSONObject authorObj = jsonObject.getJSONObject("author");
                        String author_id = authorObj.getString("_id");
                        String author_name = authorObj.getString("name");
                        String author_img = authorObj.getString("img");
                        String author_country = authorObj.getString("country");
                        String author_born = authorObj.getString("born");
                        String author_demised = authorObj.getString("demised");
                        String author_bio = authorObj.getString("bio");
                        String author_age = authorObj.getString("age");
                        int author_quotes_count = authorObj.getInt("quotes_count");
                        String author_achievements = authorObj.getString("achievements");

                        p_author_detail_name = author_name;
                        p_author_achievements = author_achievements;
                        p_author_detail_image = author_img;
                        p_author_country = author_country;
                        p_author_born = author_born;
                        p_author_dismissed = author_demised;
                        p_author_bio = author_bio;
                        p_author_quotes_count = String.valueOf(author_quotes_count);
                        p_author_age = author_age;
                        author_title_name.setText(author_name);
                        author_name_title_fix.setText("Quotes by");



                        JSONArray files = jsonObject.getJSONArray("media_files");

//                        ArrayList<Feeds> feeds_arr=new ArrayList<>();
//                        isLoading=true;
                        if (files.length() > 0) {



                            for (int a = 0; a < files.length(); a++) {


                                Feeds feed_obj = new Feeds();
                                JSONObject jsonObject1 = files.getJSONObject(a);
                                String feed_id = jsonObject1.getString("_id");
                                String title = jsonObject1.getString("title");
                                String large_image = jsonObject1.getString("large_image");
                                String small_image = jsonObject1.getString("small_image");
                                String createdAt = jsonObject1.getString("createdAt");
                                String feed_blog_url="https://pk.zapmeta.ws/ws?q=blogs%20on%20websites&asid=zm_pk_gb_1_cg1_05&abt=1&mt=b&nw=g&de=c&ap=&kid=kwd-11480526309&aid=35417532236&ac=469&cid=686767819&aid=35417532236&kid=kwd-11480526309&locale=en_PK&gclid=Cj0KCQiAhs79BRD0ARIsAC6XpaWWdElwtrj-aSEJMNpbXYMrEVj2HGO-qAlNLVJfyJ-GOsjxvMzTXbkaAq1rEALw_wcB";
                                boolean liked_by_me=jsonObject1.getBoolean("liked_by_me");
                                int liked_count = jsonObject1.getInt("liked_count");
                                int comments_count = jsonObject1.getInt("comments_count");


                                JSONObject author = new JSONObject(jsonObject1.getString("author"));

                                String a_id = author.getString("_id");
                                String a_name = author.getString("name");
                                String a_img = author.getString("img");

                                JSONArray categories_ref = jsonObject1.getJSONArray("categories_ref");

                                categoriesarrayList = new ArrayList<>();

                                for (int i = 0; i < categories_ref.length(); i++){


                                    JSONObject categories_obj =categories_ref.getJSONObject(i);
                                    JSONObject categories = categories_obj.getJSONObject("category");
                                    CategoriesList categoriesList = new CategoriesList();

                                    String cate_id = categories.getString("_id");
                                    String cate_title = categories.getString("title");
                                    String cate_image = categories.getString("cat_img");
                                    categoriesList.setCate_id(cate_id);
                                    categoriesList.setCate_title(cate_title);
                                    categoriesList.setCate_img(cate_image);
                                    categoriesarrayList.add(categoriesList);

                                }


//                                String a_id = "_id";
//                                String a_name = "name";
//                                String a_img = "/media/large_img/serve/20143fa0-958f-11e8-8889-9d6eb51cd902.png";





                                feed_obj.set_id(feed_id);
                                feed_obj.setTitle(title);
                                feed_obj.setLarge_image(large_image);
                                feed_obj.setSmall_image(small_image);
                                feed_obj.setLiked_by_me(liked_by_me);
                                feed_obj.setLiked_count(liked_count);
                                feed_obj.setComments_count(comments_count);
                                feed_obj.setAuther_id(a_id);
                                feed_obj.setAuther_name(a_name);
                                feed_obj.setAuther_img(a_img);
                                feed_obj.setCreatedAt(createdAt);
                                feed_obj.setCategoriesArrayList(categoriesarrayList);
                                feed_obj.setFeed_blog_url(feed_blog_url);
                                feedList.add(feed_obj);

                            }

                        } else {


                        }

                        String load_more_url = jsonObject.getString("load_more_url");

                        feed_remain_array.clear();
                        feed_remain_array.add(load_more_url);

                        adapter = new AuthorDetailAdapter(AuthorDetail.this, feedList);

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
                        Intent intt = new Intent(AuthorDetail.this, FirstActivity.class);
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

    private void getFeedApi2(String url) {

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

                        ArrayList<Feeds> test=new ArrayList<>();

                        JSONObject jsonObject = new JSONObject(String.valueOf(result));

                        JSONObject authorObj = jsonObject.getJSONObject("author");
                        String author_id = authorObj.getString("_id");
                        String author_name = authorObj.getString("name");
                        String author_img = authorObj.getString("img");
                        String author_country = authorObj.getString("country");
                        String author_born = authorObj.getString("born");
                        String author_demised = authorObj.getString("demised");
                        String author_bio = authorObj.getString("bio");

                        JSONArray media_files = jsonObject.getJSONArray("media_files");

//                        ArrayList<Feeds> feeds_arr=new ArrayList<>();
//                        isLoading=true;
                        if (media_files.length() > 0) {

                            for (int a = 0; a < media_files.length(); a++) {


                                Feeds feed_obj = new Feeds();
                                JSONObject jsonObject1 = media_files.getJSONObject(a);

                                String feed_id = jsonObject1.getString("_id");
                                String title = jsonObject1.getString("title");
                                String large_image = jsonObject1.getString("large_image");
                                String small_image = jsonObject1.getString("small_image");
                                String createdAt = jsonObject1.getString("createdAt");
                                int liked_count = jsonObject1.getInt("liked_count");
                                int comments_count = jsonObject1.getInt("comments_count");
                                boolean liked_by_me=jsonObject1.getBoolean("liked_by_me");

                                JSONObject author = new JSONObject(jsonObject1.getString("author"));

                                String a_id = author.getString("_id");
                                String a_name = author.getString("name");
                                String a_img = author.getString("img");

                                JSONArray categories_ref = jsonObject1.getJSONArray("categories_ref");
                                categoriesarrayList = new ArrayList<>();
                                for (int i = 0; i < categories_ref.length(); i++){

                                    JSONObject categories_obj =categories_ref.getJSONObject(i);
                                    JSONObject categories = categories_obj.getJSONObject("category");
                                    CategoriesList categoriesList = new CategoriesList();

                                    String cate_id = categories.getString("_id");
                                    String cate_title = categories.getString("title");
                                    String cate_image = categories.getString("cat_img");

                                    categoriesList.setCate_id(cate_id);
                                    categoriesList.setCate_title(cate_title);
                                    categoriesList.setCate_img(cate_image);
                                    categoriesarrayList.add(categoriesList);
                                }

                                feed_obj.set_id(feed_id);
                                feed_obj.setTitle(title);
                                feed_obj.setLarge_image(large_image);
                                feed_obj.setSmall_image(small_image);
                                feed_obj.setLiked_by_me(liked_by_me);
                                feed_obj.setLiked_count(liked_count);
                                feed_obj.setComments_count(comments_count);
                                feed_obj.setAuther_id(a_id);
                                feed_obj.setAuther_name(a_name);
                                feed_obj.setAuther_img(a_img);
                                feed_obj.setCreatedAt(createdAt);
                                feed_obj.setCategoriesArrayList(categoriesarrayList);


                                test.add(feed_obj);




                            }


                            String load_more_url = jsonObject.getString("load_more_url");

                            feed_remain_array.clear();
                            feed_remain_array.add(load_more_url);
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


        getFeedApi2(feed_remain_array.get(0));
    }


//    @Override
//    public void onResume() {
//        super.onResume();
//        if(!feedList.isEmpty() && feedList != null){
//
////            adapter.notifyDataSetChanged();
////            adapter.refresh();
//            if(positionForRefresh .equals("")){
//
//
////                getFeedApi(Constants.URL.AUTHORFEEDS);
//            }
//            else {
//                adapter.notifyItemChanged(Integer.parseInt(positionForRefresh));
//                positionForRefresh="";
//            }
////            adapter.notifyItemChanged(11);
//        }
//        else
//            {
//
//        }
//    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getFeedApi(Constants.URL.AUTHORFEEDS);

    }
}
