package jokesbook.app.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.android.gms.ads.AdView;
import jokesbook.app.Activity.AddPreyActivity;
import jokesbook.app.Activity.FirstActivity;
import jokesbook.app.Adapters.PraylistAdapter;
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

public class PrayFragment extends Fragment {

    View view;
    ProgressBar progress_praylist, progressBar_praylist;
    private SwipeRefreshLayout swipeContainer_praylist;
    private RecyclerView rv_praylist;

    LinearLayout submit_prey_btn;

    private PraylistAdapter adapter;
    public static List<Prays> praysList;
    ArrayList<String> pray_remain_array;
    LinearLayoutManager mLayoutManager;
    FragmentTransaction transaction;
    boolean isLoading = true;
    SharedPreferences mPrefs;
    public static String current_add="one";
    public static String refreshposition = "";

    AdView banner_add;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_pray, container, false);

        // Load Add

//        banner_add= view.findViewById(R.id.banner_add);
//
//        MobileAds.initialize(getContext(), String.valueOf(R.string.add_app_id));
//
//        AdRequest ad_req = new AdRequest.Builder().build();
//        banner_add.loadAd(ad_req);

        // Load Add

        progress_praylist = view.findViewById(R.id.progress_praylist);
        progressBar_praylist = view.findViewById(R.id.progressBar_praylist);
        swipeContainer_praylist = view.findViewById(R.id.swipeContainer_praylist);
        rv_praylist = view.findViewById(R.id.rv_praylist);
        submit_prey_btn = view.findViewById(R.id.pray_btn_layout);

        transaction = getFragmentManager().beginTransaction();

        progress_praylist.setVisibility(View.VISIBLE);

        setRetainInstance(true);
        praysList = new ArrayList<>();

        pray_remain_array = new ArrayList<>();

        submit_prey_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), AddPreyActivity.class);
                intent.putExtra("ACTIVITY", "main");
                startActivity(intent);
            }
        });

        swipeContainer_praylist.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Your code to refresh the list here.
                // Make sure you call swipeContainer.setRefreshing(false)
                // once the network request has completed successfully.
//                fetchTimelineAsync(0);
//                Toast.makeText(getActivity(), " Swipe To Refresh ", Toast.LENGTH_SHORT).show();
                getPrayListApi(Constants.URL.PRAYSLIST);
            }
        });

        mLayoutManager = new LinearLayoutManager(getActivity());
        mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        rv_praylist.getItemAnimator().endAnimations();
//        rv_praylist.setNestedScrollingEnabled(false);

        progressBar_praylist.setVisibility(View.VISIBLE);
        getPrayListApi(Constants.URL.PRAYSLIST);




        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(!praysList.isEmpty() && praysList != null){

//            adapter.notifyDataSetChanged();
//            adapter.refresh();
            if(!refreshposition.equals("")){
                adapter.notifyItemChanged(Integer.parseInt(refreshposition));
                refreshposition="";
            }
//            adapter.notifyItemChanged(11);
        }
//        getPrayListApi(Constants.URL.PRAYSLIST);
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            try {
                if(!FeedFragment.refresh.equals("")) {
                    adapter.notifyDataSetChanged();
                    FeedFragment.refresh = "";
                }
            }
            catch (Exception e){
            }
        }
    }

    private void getPrayListApi(String url) {

        mPrefs = getActivity().getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String token = mPrefs.getString("USER_TOKEN", "");

        Map<String, String> postParam = new HashMap<String, String>();


        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("x-sh-auth", token);

        ApiModelClass.GetApiResponse(Request.Method.GET, url, getActivity(), postParam, headers, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result, String ERROR) {

                if (ERROR.isEmpty()) {

                    try {

                        praysList.clear();
//                        Feeds feed_list_obj = new Feeds();


                        JSONObject jsonObject = new JSONObject(String.valueOf(result));

                        JSONArray pray_req = jsonObject.getJSONArray("pray_res");

//                        ArrayList<Feeds> feeds_arr=new ArrayList<>();
//                        isLoading=true;
                        if (pray_req.length() > 0) {



                            for (int a = 0; a < pray_req.length(); a++) {


                                Prays prays_obj = new Prays();
                                JSONObject jsonObject1 = pray_req.getJSONObject(a);

                                String pray_id = jsonObject1.getString("_id");
                                String text = jsonObject1.getString("title");
                                boolean prayed_by_me=jsonObject1.getBoolean("liked_by_me");
                                int comment_count = jsonObject1.getInt("comment_count");
                                int prayed_count = jsonObject1.getInt("liked_count");

                                JSONObject user = new JSONObject(jsonObject1.getString("user"));

                                String user_dp_active_file = user.getString("dp_active_file");
                                String user_id = user.getString("_id");
                                String user_full_name = user.getString("full_name");

                                prays_obj.setPrayed_by_me(prayed_by_me);
                                prays_obj.setComments_count(comment_count);
                                prays_obj.setLiked_count(prayed_count);
                                prays_obj.setPray_id(pray_id);
                                prays_obj.setText(text);
                                prays_obj.setUser_dp(user_dp_active_file);
                                prays_obj.setUser_full_name(user_full_name);
                                prays_obj.setUser_id(user_id);

                                praysList.add(prays_obj);
                            }

                        } else {


                        }

                        String load_more_url = jsonObject.getString("load_more_url");

                        pray_remain_array.clear();
                        pray_remain_array.add(load_more_url);

                        adapter = new PraylistAdapter(getActivity(), praysList, transaction);

//                        recyclerView.setHasFixedSize(true);
                        rv_praylist.setItemViewCacheSize(20);
                        rv_praylist.setDrawingCacheEnabled(true);
                        rv_praylist.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);



                        rv_praylist.setAdapter(adapter);
                        rv_praylist.setLayoutManager(mLayoutManager);

                        rv_praylist.setOnScrollListener(recyclerViewOnScrollListener);


//                        ringProgressDialog.dismiss();

                        progressBar_praylist.setVisibility(View.GONE);
                        progress_praylist.setVisibility(View.GONE);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    swipeContainer_praylist.setRefreshing(false);

                } else {
                    progress_praylist.setVisibility(View.GONE);
                    swipeContainer_praylist.setRefreshing(false);

                    progressBar_praylist.setVisibility(View.GONE);

                    if(ERROR.equals("401")){

//                        SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                        SharedPreferences.Editor mEditor = mPrefs.edit();
                        mEditor.putString("USER_EMAIL", "");
                        mEditor.putString("USER_PASSWORD", "");
                        mEditor.putString("USER_TOKEN", "");
                        mEditor.putString("USER_PIC", "");
                        mEditor.apply();
                        Intent intt = new Intent(getActivity(), FirstActivity.class);
                        startActivity(intt);
                        getActivity().finish();
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
    private void loadMoreItems() {
        isLoading = false;


        getFeedApi2(pray_remain_array.get(0));
    }

    private void getFeedApi2(String url) {

        SharedPreferences mPrefs = getActivity().getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String token = mPrefs.getString("USER_TOKEN", "");

        Map<String, String> postParam = new HashMap<String, String>();


        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("x-sh-auth", token);

        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.BASE_URL + url, getActivity(), postParam, headers, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result, String ERROR) {

                if (ERROR.isEmpty()) {

                    try {

//                        Feeds feed_list_obj = new Feeds();
                        isLoading=true;

                        ArrayList<Prays> test=new ArrayList<>();

                        JSONObject jsonObject = new JSONObject(String.valueOf(result));

                        JSONArray pray_req = jsonObject.getJSONArray("pray_res");

//                        ArrayList<Feeds> feeds_arr=new ArrayList<>();
//                        isLoading=true;
                        if (pray_req.length() > 0) {

                            for (int a = 0; a < pray_req.length(); a++) {


                                Prays prays_obj = new Prays();
                                JSONObject jsonObject1 = pray_req.getJSONObject(a);

                                String pray_id = jsonObject1.getString("_id");
                                String text = jsonObject1.getString("title");
                                boolean prayed_by_me=jsonObject1.getBoolean("liked_by_me");
                                int comment_count = jsonObject1.getInt("comment_count");
                                int prayed_count = jsonObject1.getInt("liked_count");

                                JSONObject user = new JSONObject(jsonObject1.getString("user"));

                                String user_dp_active_file = user.getString("dp_active_file");
                                String user_id = user.getString("_id");
                                String user_full_name = user.getString("full_name");

                                prays_obj.setPrayed_by_me(prayed_by_me);
                                prays_obj.setComments_count(comment_count);
                                prays_obj.setLiked_count(prayed_count);
                                prays_obj.setPray_id(pray_id);
                                prays_obj.setText(text);
                                prays_obj.setUser_dp(user_dp_active_file);
                                prays_obj.setUser_full_name(user_full_name);
                                prays_obj.setUser_id(user_id);


                                test.add(prays_obj);




                            }


                            String load_more_url = jsonObject.getString("load_more_url");

                            pray_remain_array.clear();
                            pray_remain_array.add(load_more_url);
//
//

                            progressBar_praylist.setVisibility(View.GONE);


                            adapter.addfeed(test);



                        } else {

                            progressBar_praylist.setVisibility(View.GONE);

                        }

//                            ringProgressDialog.dismiss();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    progressBar_praylist.setVisibility(View.GONE);

//                    ringProgressDialog.dismiss();
                    Toast.makeText(getActivity(), "Your internet Connection is slow", Toast.LENGTH_SHORT).show();


                }

            }
        });
        // hit api--------------------


    }





}
