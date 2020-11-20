package jokesbook.app.Fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdView;
import jokesbook.app.Activity.NavigationDrawerActivity;
import jokesbook.app.R;

import java.util.ArrayList;
import java.util.List;


public class FeedFragment extends Fragment {

    View rootView;
    AdView  adView;


    private TabLayout tabLayout;
    private ViewPager viewPager;
    public static String refresh="";


    private int[] tabIcons_white = {
            // for two tabs
//            R.drawable.subscribed_feed_white,
//            R.drawable.random_feed_white,

            // for two tabs

//            for one tab
            R.drawable.random_feed_white,
            R.drawable.random_feed_white,
            R.drawable.random_feed_white

//            for one tab
    };
    private int[] tabIcons_grey = {
//            R.drawable.small_heart,
            R.drawable.subscribed_feed_gray,
            R.drawable.random_feed_gray,
            R.drawable.random_feed_gray

    };
 private String[] tabLabels = {
         // for two tabs

//           " My Feed"," All"

         // for two tabs

//         for one tab with pray
         "All",
         "Quotes",
         "Prayers"
//         for one tab with pray
    };




    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_feed, container, false);

        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        setupViewPager(viewPager);
        tabLayout = (TabLayout) rootView.findViewById(R.id.tabs);
 // test

        // test

        tabLayout.setupWithViewPager(viewPager);
//        tabLayout.setTabTextColors(Color.parseColor("#6d6c6c"), Color.parseColor("#FFFFFF"));
//        tabLayout.setSelectedTabIndicatorColor(Color.parseColor("#FFFFFF"));


//        MobileAds.initialize(getContext(),String.valueOf(R.string.add_app_id));
//        adView = rootView.findViewById(R.id.adView);
//        AdRequest adRequest = new AdRequest.Builder().build();
//        adView.loadAd(adRequest);




        setupTabIcons();



        // test
//        highLightCurrentTab(0);

        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {




                TextView v=tab.getCustomView().findViewById(R.id.tab_text);
                ImageView tab_img=tab.getCustomView().findViewById(R.id.tab_img);
                v.setTextColor(Color.parseColor("#FFFFFF"));
                tab_img.setImageResource(tabIcons_white[tab.getPosition()]);

//                setupTabIconsChange(tab.getPosition());

//                NavigationDrawerActivity .updateBadgeCount();
                try {
                ((NavigationDrawerActivity)getContext()).updateBadgeCount();
                }catch (Exception e){
                    Log.e("Update Badge", "Badge count update krny me mslaa aya hy");
                }


            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

                TextView v=tab.getCustomView().findViewById(R.id.tab_text);
                ImageView tab_img=tab.getCustomView().findViewById(R.id.tab_img);

//        tabLayout.setTabTextColors(Color.parseColor("#6d6c6c"), Color.parseColor("#FFFFFF"));

                v.setTextColor(Color.parseColor("#a6dbbb"));
                tab_img.setImageResource(tabIcons_grey[tab.getPosition()]);

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


        return rootView;

    }


    private void setupTabIcons() {

        // for two tabs

//        for (int i = 0; i < 2; i++ ) {
//
//
//            RelativeLayout tab = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.custom_tab_layout, null);
//
//            TextView tab_text = (TextView) tab.findViewById(R.id.tab_text);
//            ImageView tab_img = (ImageView) tab.findViewById(R.id.tab_img);
//            tab_text.setText(tabLabels[i]);
//
//            if(i == 0){
//                tab_text.setTextColor(Color.parseColor("#FFFFFF"));
//                tab_img.setImageResource(tabIcons_white[i]);
//
//
//            }
//            else {
//                tab_text.setTextColor(Color.parseColor("#a6dbbb"));
//                tab_img.setImageResource(tabIcons_grey[i]);
//
//
//            }
//
//
//            tabLayout.getTabAt(i).setCustomView(tab);
//
//        }

        // for two tabs

//        for one tab with pray

//        RelativeLayout tab = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.custom_tab_layout, null);
//
//        TextView tab_text = (TextView) tab.findViewById(R.id.tab_text);
//        ImageView tab_img = (ImageView) tab.findViewById(R.id.tab_img);
//        tab_text.setText(tabLabels[0]);
//        tab_text.setTextColor(Color.parseColor("#FFFFFF"));
//        tab_img.setImageResource(tabIcons_white[0]);
//        tabLayout.getTabAt(0).setCustomView(tab);

                for (int i = 0; i < 3; i++ ) {


            RelativeLayout tab = (RelativeLayout) LayoutInflater.from(getContext()).inflate(R.layout.custom_tab_layout, null);

            TextView tab_text = (TextView) tab.findViewById(R.id.tab_text);
            ImageView tab_img = (ImageView) tab.findViewById(R.id.tab_img);
            tab_text.setText(tabLabels[i]);

            if(i == 0){
                tab_text.setTextColor(Color.parseColor("#FFFFFF"));
                tab_img.setImageResource(tabIcons_white[i]);


            }
            else {
                tab_text.setTextColor(Color.parseColor("#a6dbbb"));
                tab_img.setImageResource(tabIcons_grey[i]);


            }


            tabLayout.getTabAt(i).setCustomView(tab);

        }


//        for one tab with pray


    }




    private void setupViewPager(ViewPager viewPager) {

        ViewPagerAdapter adapter = new ViewPagerAdapter(getChildFragmentManager());

//        adapter.addFragment(new SubscribedFeedFragment(), "My Feed");
        adapter.addFragment(new AllFragment(), "All");
        adapter.addFragment(new RandomFeedFragment(), "Quotes");
        adapter.addFragment(new PrayFragment(), "Prayers");


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


