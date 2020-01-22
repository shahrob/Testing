package com.positivevibes.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.positivevibes.app.Adapters.SearchTopQuotesAdapter;
import com.positivevibes.app.ApiStructure.ApiModelClass;
import com.positivevibes.app.ApiStructure.Constants;
import com.positivevibes.app.ApiStructure.ServerCallback;
import com.positivevibes.app.GridSpacingItemDecoration;
import com.positivevibes.app.Models.FavouritesApiModel.Favourites;
import com.positivevibes.app.Models.TopQuotes.TopQuotes;
import com.positivevibes.app.NestedScroll;
import com.positivevibes.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SearchTopQuotes extends AppCompatActivity {

    TextView search_text, no_top_quotes;
    RecyclerView search_rv;
    ProgressBar progressBar_quotes, progress_top;
    SwipeRefreshLayout swipeContainer_top;
    NestedScrollView scrollView_top;
    String loadMoreUrl;
    SearchTopQuotesAdapter adapter;
    boolean isLoading =true ;
    RelativeLayout homerel;

    ArrayList<TopQuotes> topQuotesArrayList;

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_top_quotes);

        initialization();
        topQuotesArrayList = new ArrayList<>();

        search_text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SearchTopQuotes.this, SearchCateAuthor.class);
                intent.putExtra("EXTRA", "selectedTopQuotes");
                startActivity(intent);
            }
        });

        homerel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            }
        });

        ViewCompat.setNestedScrollingEnabled(search_rv, false);

        GridLayoutManager MyLayoutManager = new GridLayoutManager(this, 2);
        MyLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        search_rv.setLayoutManager(MyLayoutManager);
        int spacing = 3; // 50px

        search_rv.addItemDecoration(new GridSpacingItemDecoration(2, spacing, false));

        scrollView_top.setOnScrollChangeListener(new NestedScroll() {
            @Override
            public void onScroll() {


                if (isLoading) {
                    isLoading = false;
                    Log.d("info data object", "sdfasdfadsasdfasdfasdf");

                    progressBar_quotes.setVisibility(View.VISIBLE);

                    loadMoreItems();
                }
            }
        });

        ApiGetData();

        swipeContainer_top.setColorSchemeColors(Color.parseColor("#26a658"));

        swipeContainer_top.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                ApiGetData();

            }
        });



    }

    private void ApiGetData(){

        progress_top.setVisibility(View.VISIBLE);
        SharedPreferences mPrefs = SearchTopQuotes.this.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        final String token = mPrefs.getString("USER_TOKEN", "");


        Map<String, String> postParam = new HashMap<String, String>();


        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("x-sh-auth", token);

        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.TOP_QUOTES, SearchTopQuotes.this, postParam, headers, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result, String ERROR) {
                if (ERROR.isEmpty()){

                    try {

                        topQuotesArrayList.clear();

                        JSONObject jsonObject = new JSONObject(String.valueOf(result));
                        String load_more_url = jsonObject.getString("load_more_url");
                        int code = jsonObject.getInt("code");

                        loadMoreUrl = load_more_url;

                        JSONArray media_files = jsonObject.getJSONArray("media_files");
                            for (int i = 0; i < media_files.length(); i++){

                                TopQuotes topQuotesObj = new TopQuotes();
                                JSONObject jsonObject1 = media_files.getJSONObject(i);

                                String media_id = jsonObject1.getString("_id");
                                String media_large_image = jsonObject1.getString("large_image");
                                String media_small_image = jsonObject1.getString("small_image");
                                int media_liked_count = jsonObject1.getInt("liked_count");

                                topQuotesObj.set_id(media_id);
                                topQuotesObj.setLarge_image(media_large_image);
                                topQuotesObj.setSmall_image(media_small_image);
                                topQuotesObj.setLiked_count(media_liked_count);

                                topQuotesArrayList.add(topQuotesObj);
                            }
                            if (media_files.length() == 0){
                                no_top_quotes.setVisibility(View.VISIBLE);
                                swipeContainer_top.setVisibility(View.GONE);
                            }else {
                                no_top_quotes.setVisibility(View.GONE);
                                swipeContainer_top.setVisibility(View.VISIBLE);
                                adapter = new SearchTopQuotesAdapter(SearchTopQuotes.this, topQuotesArrayList);
                                search_rv.setAdapter(adapter);
                            }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    swipeContainer_top.setRefreshing(false);
                    progress_top.setVisibility(View.GONE);


                }else {
                    progress_top.setVisibility(View.GONE);
                    swipeContainer_top.setRefreshing(false);

                    if (ERROR.equals("401")) {

                        SharedPreferences mPrefs = SearchTopQuotes.this.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                        SharedPreferences.Editor mEditor = mPrefs.edit();
                        mEditor.putString("USER_EMAIL", "");
                        mEditor.putString("USER_PASSWORD", "");
                        mEditor.putString("USER_TOKEN", "");
                        mEditor.putString("USER_PIC", "");
                        mEditor.apply();
                        Intent intt = new Intent(SearchTopQuotes.this, FirstActivity.class);
                        SearchTopQuotes.this.startActivity(intt);
                        finish();

                    } else {

                        Toast.makeText(SearchTopQuotes.this, ERROR, Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });
    }

    private void ApiGetData2(String url){
        SharedPreferences mPrefs = SearchTopQuotes.this.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String token = mPrefs.getString("USER_TOKEN", "");


        Map<String, String> postParam = new HashMap<String, String>();


        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("x-sh-auth", token);

        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.BASE_URL + url, SearchTopQuotes.this, postParam, headers, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result, String ERROR) {
                if (ERROR.isEmpty()){
                    try {

                        isLoading = true;

                        ArrayList<TopQuotes> next_array=new ArrayList<>();

                        JSONObject jsonObject = new JSONObject(String.valueOf(result));

                        String load_more_url = jsonObject.getString("load_more_url");

                        loadMoreUrl = load_more_url;

                        JSONArray media_files = jsonObject.getJSONArray("media_files");

                        for (int i = 0; i < media_files.length(); i++){

                            TopQuotes topQuotesObj = new TopQuotes();

                            JSONObject jsonObject1 = media_files.getJSONObject(i);

                            String media_id = jsonObject1.getString("_id");
                            String media_large_image = jsonObject1.getString("large_image");
                            String media_small_image = jsonObject1.getString("small_image");
                            int media_liked_count = jsonObject1.getInt("liked_count");

                            topQuotesObj.set_id(media_id);
                            topQuotesObj.setLarge_image(media_large_image);
                            topQuotesObj.setSmall_image(media_small_image);
                            topQuotesObj.setLiked_count(media_liked_count);

                            next_array.add(topQuotesObj);

                        }

                        adapter.addfeed(next_array);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    progressBar_quotes.setVisibility(View.GONE);

                }else {
                    progressBar_quotes.setVisibility(View.GONE);


                    if (ERROR.equals("401")) {

                        SharedPreferences mPrefs = SearchTopQuotes.this.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                        SharedPreferences.Editor mEditor = mPrefs.edit();
                        mEditor.putString("USER_EMAIL", "");
                        mEditor.putString("USER_PASSWORD", "");
                        mEditor.putString("USER_TOKEN", "");
                        mEditor.putString("USER_PIC", "");
                        mEditor.apply();
                        Intent intt = new Intent(SearchTopQuotes.this, FirstActivity.class);
                        SearchTopQuotes.this.startActivity(intt);
                        finish();

                    } else {

                        Toast.makeText(SearchTopQuotes.this, ERROR, Toast.LENGTH_SHORT).show();

                    }
                }
            }
        });


    }


    private void loadMoreItems() {
        isLoading = false;


        ApiGetData2(loadMoreUrl);
    }

    private void initialization(){
        search_text = findViewById(R.id.search_text_quotes);
        search_rv = findViewById(R.id.search_rv_quotes);
        progressBar_quotes = findViewById(R.id.progressBar_quotes);
        no_top_quotes = findViewById(R.id.no_top_quotes);
        swipeContainer_top = findViewById(R.id.swipeContainer_top);
        scrollView_top = findViewById(R.id.scrollView_top);
        progress_top = findViewById(R.id.progress_top);
        homerel = findViewById(R.id.homerel);
    }
}
