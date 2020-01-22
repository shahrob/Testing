package com.positivevibes.app.Activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.analytics.FirebaseAnalytics;
import com.positivevibes.app.Fragments.SearchAuthors;
import com.positivevibes.app.Fragments.SearchCategory;
import com.positivevibes.app.R;

import java.util.ArrayList;
import java.util.List;

public class SearchCateAuthor extends AppCompatActivity {

    private TabLayout tabLayout;
    private ViewPager viewPager;
    String extra;
    public static String refresh="";
    public static String keyboard_stroke;
    ViewPagerAdapter viewPagerAdapter;



//    private int[] tabIcons_white = {
            // for two tabs
//            R.drawable.subscribed_feed_white,
//            R.drawable.random_feed_white,

            // for two tabs

//            for one tab
//            R.drawable.random_feed_white,

//            for one tab
//    };
//    private int[] tabIcons_grey = {
//            R.drawable.small_heart,
//            R.drawable.subscribed_feed_gray,
//            R.drawable.random_feed_gray,
//
//    };
    private String[] tabLabels = {
            // for two tabs

           "Categories","Authors"

            // for two tabs

//         for one tab
//            "Feeds"
//         for one tab
    };

    EditText search_edittextview;
    RelativeLayout back_rel;

    public FragmentRefreshListener_auth getFragmentRefreshListener_auth() {
        return fragmentRefreshListener_auth;
    }

    public void setFragmentRefreshListener_auth(FragmentRefreshListener_auth fragmentRefreshListener_auth) {
        this.fragmentRefreshListener_auth = fragmentRefreshListener_auth;
    }

    private FragmentRefreshListener_auth fragmentRefreshListener_auth;


    public FragmentRefreshListener_cate getFragmentRefreshListener_cate() {
        return fragmentRefreshListener_cate;
    }

    public void setFragmentRefreshListener_cate(FragmentRefreshListener_cate fragmentRefreshListener_cate) {
        this.fragmentRefreshListener_cate = fragmentRefreshListener_cate;
    }

    private FragmentRefreshListener_cate fragmentRefreshListener_cate;

    @SuppressLint("WrongConstant")
    @Override
    public void onBackPressed() {
        super.onBackPressed();

//        if (extra.equals("selectedcategory")){
//            Intent intent = new Intent(SearchCateAuthor.this, NavigationDrawerActivity.class);
//            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
//            startActivity(intent);
//            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
//        }else {
            finish();
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
//        }

    }

    @Override
    protected void onResume() {
        super.onResume();

//        AnalyticsTrackers.getInstance().trackScreenView("Search Categories and Authors Activity");

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_cate_author);


//        AnalyticsTrackers.initialize(this);
//
//        AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP, "UA-123157129-1");
//        AnalyticsTrackers.getInstance().trackScreenView("Search Categories and Authors Activity");



        // firebase analytics
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundlee = new Bundle();
        bundlee.putString(FirebaseAnalytics.Param.ITEM_NAME, "Search Category Activity");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundlee);

        // firebase analytics


        viewPager = (ViewPager) findViewById(R.id.viewpager);
        search_edittextview = findViewById(R.id.search_edittextview);
        back_rel = findViewById(R.id.back_rel);

        setupUI(findViewById(R.id.main_parent));
//        extra = getIntent().getStringExtra("EXTRA");

        back_rel.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("WrongConstant")
            @Override
            public void onClick(View v) {
//                if (extra.equals("selectedcategory")){
//                    Intent intent = new Intent(SearchCateAuthor.this, NavigationDrawerActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | IntentCompat.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(intent);
//                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
//                }else {
                    finish();
                    overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
//                }
            }
        });

        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        // test

        // test
        tabLayout.setupWithViewPager(viewPager);
//        tabLayout.setTabTextColors(Color.parseColor("#6d6c6c"), Color.parseColor("#FFFFFF"));
//        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FFFFFF"));

        setupTabIcons();



        // test
//        highLightCurrentTab(0);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {




                TextView v=tab.getCustomView().findViewById(R.id.tab_text);
                ImageView tab_img=tab.getCustomView().findViewById(R.id.tab_img);
                v.setTextColor(Color.parseColor("#FFFFFF"));
//                tab_img.setImageResource(tabIcons_white[tab.getPosition()]);

//                setupTabIconsChange(tab.getPosition());

//                NavigationDrawerActivity .updateBadgeCount();
//                (getApplicationContext().updateBadgeCount();


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                TextView v=tab.getCustomView().findViewById(R.id.tab_text);
                ImageView tab_img=tab.getCustomView().findViewById(R.id.tab_img);

//        tabLayout.setTabTextColors(Color.parseColor("#6d6c6c"), Color.parseColor("#FFFFFF"));

                v.setTextColor(Color.parseColor("#a6dbbb"));
//                tab_img.setImageResource(tabIcons_grey[tab.getPosition()]);

//                tab_img.setImageResource(R.drawable.random_feed_white);


            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });


        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                int a=10;

            }

            @Override
            public void onPageSelected(int position) {
//                highLightCurrentTab(position);
                int a=10;


//                TextView v=tabLayout.getCustomView().findViewById(R.id.tab_text);
//                tabLayout.setTabTextColors(Color.parseColor("#6d6c6c"), Color.parseColor("#FFFFFF"));
//                tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FFFFFF"));
//                setupTabIconsChange(position);



            }

            @Override
            public void onPageScrollStateChanged(int state) {
                int a=10;

            }
        });

        // test


        search_edittextview.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    hideKeyboard(v);
                    return true;
                }
                return false;
            }
        });
        search_edittextview.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {}

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String word=s.toString();
                keyboard_stroke = word;

                if(getFragmentRefreshListener_auth()!=null){
                    getFragmentRefreshListener_auth().onRefresh();
                }
                if(getFragmentRefreshListener_cate()!=null){
                    getFragmentRefreshListener_cate().onRefresh();
                }

                //                Toast.makeText(getContext(),s,Toast.LENGTH_SHORT).show();
            }
        });


    }

    public interface FragmentRefreshListener_auth{
        void onRefresh();
    }
    public interface FragmentRefreshListener_cate{
        void onRefresh();
    }


    public void setupUI(View view) {

        // Set up touch listener for non-text box views to hide keyboard.
        if (!(view instanceof EditText)) {
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    hideSoftKeyboard(SearchCateAuthor.this);
                    return false;
                }
            });
        }

        //If a layout container, iterate over children and seed recursion.
        if (view instanceof ViewGroup) {
            for (int i = 0; i < ((ViewGroup) view).getChildCount(); i++) {
                View innerView = ((ViewGroup) view).getChildAt(i);
                setupUI(innerView);
            }
        }
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    protected void hideKeyboard(View view)
    {

//
//        InputMethodManager in = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
//        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

        InputMethodManager imm = (InputMethodManager)SearchCateAuthor.this.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }

    private void setupTabIcons() {

        // for two tabs

        for (int i = 0; i < 2; i++ ) {


            RelativeLayout tab = (RelativeLayout) LayoutInflater.from(SearchCateAuthor.this).inflate(R.layout.custom_tab_layout, null);

            TextView tab_text = (TextView) tab.findViewById(R.id.tab_text);
//            ImageView tab_img = (ImageView) tab.findViewById(R.id.tab_img);
            tab_text.setText(tabLabels[i]);

            if(i == 0){
                tab_text.setTextColor(Color.parseColor("#FFFFFF"));
//                tab_img.setImageResource(tabIcons_white[i]);
            }



            else {
                tab_text.setTextColor(Color.parseColor("#a6dbbb"));
//                tab_img.setImageResource(tabIcons_grey[i]);


            }


            tabLayout.getTabAt(i).setCustomView(tab);

        }

        // for two tabs

//        for one tab

//        RelativeLayout tab = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.custom_tab_layout, null);
//
//        TextView tab_text = (TextView) tab.findViewById(R.id.tab_text);
//        ImageView tab_img = (ImageView) tab.findViewById(R.id.tab_img);
//        tab_text.setText(tabLabels[0]);
//        tab_text.setTextColor(Color.parseColor("#FFFFFF"));
//        tab_img.setImageResource(tabIcons_white[0]);
//        tabLayout.getTabAt(0).setCustomView(tab);

//        for one tab



//        if (extra.equals("selectedcategory")){
//            TabLayout.Tab tab = tabLayout.getTabAt(1);
//            tab.select();
//        }

    }

    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new SearchCategory(), "Categories");
        adapter.addFragment(new SearchAuthors(), "Authors");

        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();

        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {

            mFragmentList.add(fragment);

            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }




    }



}
