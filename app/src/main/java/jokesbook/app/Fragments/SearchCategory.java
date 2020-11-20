package jokesbook.app.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import jokesbook.app.Activity.FirstActivity;
import jokesbook.app.Activity.SearchCateAuthor;
import jokesbook.app.Adapters.GetCatagoryAdapter;
import jokesbook.app.ApiStructure.ApiModelClass;
import jokesbook.app.ApiStructure.Constants;
import jokesbook.app.ApiStructure.ServerCallback;
import jokesbook.app.Models.CatagoriesApiModel.Catagories;
import jokesbook.app.NestedScroll;
import jokesbook.app.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchCategory extends Fragment {

    View view;
    ProgressBar progress_cate_search, progressBar_cate_search;
    NestedScrollView scrollView_cate_search;
    TextView no_result_cate_search;
    boolean isLoading =true ;
    private GetCatagoryAdapter adapter;
    RecyclerView catagory_search_rv;
    List<Catagories> catagoriesList;
    String loadMoreUrl;
    String keyboard_keywords;


//    @Override
//    public void setUserVisibleHint(boolean isVisibleToUser) {
//        super.setUserVisibleHint(isVisibleToUser);
//        if (isVisibleToUser){
//            keyboard_keywords = SearchCateAuthor.keyboard_stroke;
//            searchAPi(keyboard_keywords);
//        }
//    }


//    @Override
//    public void onResume() {
//        super.onResume();
//        keyboard_keywords = SearchCateAuthor.keyboard_stroke;
//            searchAPi(keyboard_keywords);
//    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_search_category, container, false);

        progress_cate_search = view.findViewById(R.id.progress_cate);
        progressBar_cate_search = view.findViewById(R.id.progressBar_cate);
        scrollView_cate_search = view.findViewById(R.id.scrollView_cate);
        no_result_cate_search = view.findViewById(R.id.no_result_cate);
        catagory_search_rv = view.findViewById(R.id.catagory_search_rv);

        catagoriesList=new ArrayList<>();
        progress_cate_search.setVisibility(View.VISIBLE);

        scrollView_cate_search.setOnScrollChangeListener(new NestedScroll() {
            @Override
            public void onScroll() {


                if (isLoading) {
                    isLoading = false;
                    Log.d("info data object", "sdfasdfadsasdfasdfasdf");

//                    if (hasNextPage) {
                    progressBar_cate_search.setVisibility(View.VISIBLE);

                    loadMoreItems();
//                    }
                }
            }
        });

        if (keyboard_keywords == null){
            keyboard_keywords = " ";
            searchAPi(keyboard_keywords);
        }else {
            keyboard_keywords = SearchCateAuthor.keyboard_stroke;
            searchAPi(keyboard_keywords);
        }

        ((SearchCateAuthor)getActivity()).setFragmentRefreshListener_cate(new SearchCateAuthor.FragmentRefreshListener_cate() {
            @Override
            public void onRefresh() {

                keyboard_keywords = SearchCateAuthor.keyboard_stroke;
                searchAPi(keyboard_keywords);
//                 Refresh Your Fragment
            }
        });


        return view;
    }

    private void searchAPi(String word) {


        // hit api............



        SharedPreferences mPrefs = getActivity().getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String token = mPrefs.getString("USER_TOKEN", "");


        Map<String, String> postParam = new HashMap<String, String>();


        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("x-sh-auth", token);

        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.SEARCH_KEY_CAT + word, getContext(), postParam, headers, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result, String ERROR) {
                if (ERROR.isEmpty()) {

                    try {


                        catagoriesList.clear();
                        progress_cate_search.setVisibility(View.VISIBLE);
                        JSONObject jsonObject = new JSONObject(String.valueOf(result));

                        int code = jsonObject.getInt("code");
                        String load_more_url = jsonObject.getString("load_more_url");
                        loadMoreUrl=load_more_url;
                        if (code == 200) {

                            progress_cate_search.setVisibility(View.VISIBLE);
                            JSONArray categories = jsonObject.getJSONArray("categories");

                            for (int a = 0; a < categories.length(); a++) {


                                Catagories cat_obj = new Catagories();

                                JSONObject jsonObject1 = categories.getJSONObject(a);
                                String id = jsonObject1.getString("_id");
                                String title = jsonObject1.getString("title");
                                boolean is_active = jsonObject1.getBoolean("is_active");
                                String cat_img = jsonObject1.getString("cat_img");

                                cat_obj.set_id(id);
                                cat_obj.setTitle(title);
                                cat_obj.setIs_active(is_active);
                                cat_obj.setCat_img(cat_img);

                                catagoriesList.add(cat_obj);


                            }

                                no_result_cate_search.setVisibility(View.GONE);
                                catagory_search_rv.setVisibility(View.VISIBLE);

                                adapter = new GetCatagoryAdapter(getContext(), catagoriesList);

                                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getContext());

                                catagory_search_rv.setLayoutManager(mLayoutManager);

                                ViewCompat.setNestedScrollingEnabled(catagory_search_rv, false);
                                catagory_search_rv.setNestedScrollingEnabled(false);
                                catagory_search_rv.getItemAnimator().endAnimations();
                                catagory_search_rv.setAdapter(adapter);


                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    progress_cate_search.setVisibility(View.GONE);

                } else {

                    progress_cate_search.setVisibility(View.GONE);

                    if (ERROR.equals("401")) {

                        SharedPreferences mPrefs = getContext().getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                        SharedPreferences.Editor mEditor = mPrefs.edit();
                        mEditor.putString("USER_EMAIL", "");
                        mEditor.putString("USER_PASSWORD", "");
                        mEditor.putString("USER_TOKEN", "");
                        mEditor.putString("USER_PIC", "");
                        mEditor.apply();
                        Intent intt = new Intent(getContext(), FirstActivity.class);
                        startActivity(intt);
                        getActivity().finish();
                    } else {

                        Toast.makeText(getActivity().getApplicationContext(), ERROR, Toast.LENGTH_SHORT).show();

                    }

                }

            }
        });
    }

    private void prepareList2(String url) {

        // hit api............

        SharedPreferences mPrefs = getActivity().getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String token = mPrefs.getString("USER_TOKEN", "");


        Map<String, String> postParam = new HashMap<String, String>();


        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("x-sh-auth", token);

        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.BASE_URL+url, getContext(), postParam, headers, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result, String ERROR) {

                if (ERROR.isEmpty()) {

                    try {

                        isLoading = true;

                        JSONObject jsonObject = new JSONObject(String.valueOf(result));

                        int code = jsonObject.getInt("code");
                        String load_more_url = jsonObject.getString("load_more_url");
                        loadMoreUrl=load_more_url;
                        if (code == 200) {


                            JSONArray categories = jsonObject.getJSONArray("categories");

                            ArrayList<Catagories> new_array=new ArrayList<>();
                            for (int a = 0; a < categories.length(); a++) {

                                Catagories cat_obj=new Catagories();

                                JSONObject jsonObject1 = categories.getJSONObject(a);
                                String id = jsonObject1.getString("_id");
                                String title = jsonObject1.getString("title");
                                boolean is_active=jsonObject1.getBoolean("is_active");
                                String cat_img = jsonObject1.getString("cat_img");

                                cat_obj.set_id(id);
                                cat_obj.setTitle(title);
                                cat_obj.setIs_active(is_active);
                                cat_obj.setCat_img(cat_img);

                                new_array.add(cat_obj);


                            }


                            adapter.addfeed(new_array);






                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    progressBar_cate_search.setVisibility(View.GONE);

                } else {

                    progressBar_cate_search.setVisibility(View.GONE);

                    if (ERROR.equals("401")) {

                        SharedPreferences mPrefs = getContext().getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                        SharedPreferences.Editor mEditor = mPrefs.edit();
                        mEditor.putString("USER_EMAIL", "");
                        mEditor.putString("USER_PASSWORD", "");
                        mEditor.putString("USER_TOKEN", "");
                        mEditor.putString("USER_PIC", "");
                        mEditor.apply();
                        Intent intt = new Intent(getContext(), FirstActivity.class);
                        startActivity(intt);
                        getActivity().finish();
                    } else {

                        Toast.makeText(getActivity().getApplicationContext(), ERROR, Toast.LENGTH_SHORT).show();

                    }

                }

            }
        });
        // hit api--------------------
    }


    private void loadMoreItems() {
        isLoading = false;


        prepareList2(loadMoreUrl);
    }

}
