package jokesbook.app.Activity;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.ContactsContract;
import android.provider.Telephony;
import android.service.notification.StatusBarNotification;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.analytics.FirebaseAnalytics;
import jokesbook.app.Fragments.AllFragment;
import jokesbook.app.Fragments.SearchCategory;
import com.onegravity.contactpicker.contact.Contact;
import com.onegravity.contactpicker.contact.ContactDescription;
import com.onegravity.contactpicker.contact.ContactSortOrder;
import com.onegravity.contactpicker.core.ContactPickerActivity;
import com.onegravity.contactpicker.group.Group;
import com.onegravity.contactpicker.picture.ContactPictureType;
import jokesbook.app.ApiStructure.ApiModelClass;
import jokesbook.app.ApiStructure.Constants;
import jokesbook.app.ApiStructure.ServerCallback;
import jokesbook.app.Fragments.FeedFragment;
import jokesbook.app.Models.CircleTransform;
import jokesbook.app.Models.ProfileApiModel.Profile;
import jokesbook.app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import hotchemi.android.rate.AppRate;
import me.leolin.shortcutbadger.ShortcutBadger;

public class NavigationDrawerActivity extends AppCompatActivity {


    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private LinearLayout img_linear;
    private TextView txtName, txtWebsite;
    private Toolbar toolbar;
    MenuItem notification_menuitem, search_menuitem, add_prey;
    TextView noti_count;
    ImageView noti_img, search_img, add_img;
    RelativeLayout search_img_rel, noti_img_rel, add_prey_rel;
    Dialog d;
    TextView send;
    TextView cancel;

    private static final String EXTRA_DARK_THEME = "EXTRA_DARK_THEME";
    private static final String EXTRA_GROUPS = "EXTRA_GROUPS";
    private static final String EXTRA_CONTACTS = "EXTRA_CONTACTS";

    private static final int REQUEST_CONTACT = 0;
    private static final int PERMISSION_REQUEST_CODE = 1;


    private boolean mDarkTheme;
    private List<Contact> mContacts;
    private List<Group> mGroups;
    List<String> contacts_list;
    List<String> array;

    public static Profile profile_data;

//    private FloatingActionButton fab;

    // urls to load navigation header background image

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_HOME = "home";
    private static final String TAG_FAVOURITE = "Favourites";
    private static final String TAG_NOTIFICATION = "Notifications";
    private static final String TAG_SETTING = "Setting";
    private static final String TAG_HELP = "Help";
    private static final String APP_TITLE = "Positivity Vibes";
    public static String CURRENT_TAG = APP_TITLE;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;
    String USER_DP, USER_NAME;
    Menu menuu;
    int launch_count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_drawer);
        toolbar = findViewById(R.id.toolbar);
        
//        Toast.makeText(NavigationDrawerActivity.this,"Create",Toast.LENGTH_SHORT).show();

        SharedPreferences sp = getSharedPreferences("LaunchCounts", MODE_PRIVATE);
        if (sp.getBoolean("first_time", true)){
            sp.edit().putInt("count", 0).apply();
            sp.edit().putBoolean("first_time", false).apply();
        }



        // Rate App

        AppRate.with(this)
                .setInstallDays(0)
                .setLaunchTimes(6)
                .setRemindInterval(1)
                .monitor();

        AppRate.showRateDialogIfMeetsConditions(this);

        // Rate App

        // Invite Friend

        launch_count = sp.getInt("count", 0);
        sp.edit().putInt("count", launch_count+1).apply();
        launch_count = sp.getInt("count", 0);

        if (launch_count == 4){
            InviteFriend();
        }

        // Invite Friend

        setSupportActionBar(toolbar);

        ApiGetProfile();

        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        navigationView = (NavigationView) findViewById(R.id.nav_view);


//        fab = (FloatingActionButton) findViewById(R.id.fab);

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        txtWebsite = (TextView) navHeader.findViewById(R.id.website);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);
        img_linear = (LinearLayout) navHeader.findViewById(R.id.img_linear);

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        USER_DP = mPrefs.getString("USER_DP", "");
        USER_NAME = mPrefs.getString("USER_NAME", "");

        // load nav menu header data
        loadNavHeader();

        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = APP_TITLE;
            loadHomeFragment();
        }

//        AnalyticsTrackers.initialize(this);
//
//        AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP, "UA-123157129-1");
//        AnalyticsTrackers.getInstance().trackScreenView("NavigationDrawer Activity");



        // firebase analytics
        FirebaseAnalytics mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundlee = new Bundle();
        bundlee.putString(FirebaseAnalytics.Param.ITEM_NAME, "Navigation Drawer Activity");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundlee);

        // firebase analytics


    }

    /***
     * Load navigation menu header information
     * like background image, profile image
     * name, website, notifications action view (dot)
     */


    private void loadNavHeader() {
        // name, website

        txtName.setText(USER_NAME);
//        txtWebsite.setText("www.androidhive.info");

        // loading header background image
//        Glide.with(this).load(urlNavHeaderBg)
//                .crossFade()
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(imgNavHeaderBg);

        imgNavHeaderBg.setImageResource(R.drawable.cate_background_style_filled);

        // showing dot next to notifications label
        navigationView.getMenu().getItem(0).setActionView(R.layout.menu_dot);
        navigationView.getMenu().getItem(1).setActionView(R.layout.menu_dot);
        navigationView.getMenu().getItem(2).setActionView(R.layout.menu_dot);
        navigationView.getMenu().getItem(3).setActionView(R.layout.menu_dot);
        navigationView.getMenu().getItem(4).setActionView(R.layout.menu_dot);
        navigationView.getMenu().getItem(5).setActionView(R.layout.menu_dot);
    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {

        // selecting appropriate nav menu item
//        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            // show or hide the fab button
            toggleFab();
            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
//                Fragment fragment = FeedFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        // show or hide the fab button
        toggleFab();

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // home
                AllFragment homeFragment = new AllFragment();
                return homeFragment;
            case 1:
                // photos
//                ChallengeFragment photosFragment = new ChallengeFragment();
//                return photosFragment;
            case 2:

//                ChallengeFragment photoFragment = new ChallengeFragment();
//                return photoFragment;
                // movies fragment
//                MoviesFragment moviesFragment = new MoviesFragment();
//                return moviesFragment;
            case 3:
//                ChallengeFragment photosFrament = new ChallengeFragment();
//                return photosFrament;
                // notifications fragment
//                NotificationsFragment notificationsFragment = new NotificationsFragment();
//                return notificationsFragment;

            case 4:
//                ChallengeFragment photosFagment = new ChallengeFragment();
//                return photosFagment;
                // settings fragment
//                SettingsFragment settingsFragment = new SettingsFragment();
//                return settingsFragment;
            default:
                return new AllFragment();
        }
    }

    private void setToolbarTitle() {

        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
//        getSupportActionBar().setTitle("Positivity Vibes");
    }

    private void UncheckItems() {

        int size = navigationView.getMenu().size();
        for (int i = 0; i < size; i++) {
            navigationView.getMenu().getItem(i).setChecked(false);
        }
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

//                menuItem.setChecked(false);

                loadNavHeader();
                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;

                    case R.id.nav_profile:
                        navItemIndex = 0;
//                        CURRENT_TAG = TAG_SETTING;
                        startActivity(new Intent(NavigationDrawerActivity.this, EditProfileActivity.class));
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                        break;

//                    case R.id.nav_home:
//                        navItemIndex = 1;
////                        CURRENT_TAG = TAG_HOME;
//                        CURRENT_TAG = APP_TITLE;
//
//                        break;

                    case R.id.nav_favourite:
                        navItemIndex = 1;
//                        CURRENT_TAG = TAG_FAVOURITE;
                        startActivity(new Intent(NavigationDrawerActivity.this, FavouritesActivity.class));
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                        break;



                    case R.id.nav_notification:
                        navItemIndex = 2;
//                        CURRENT_TAG = TAG_NOTIFICATION;
                        startActivity(new Intent(NavigationDrawerActivity.this, NotificationActivity.class));
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                        break;

//                    case R.id.nav_search:
//                        navItemIndex = 4;
//                        CURRENT_TAG = TAG_NOTIFICATION;
//                        startActivity(new Intent(NavigationDrawerActivity.this, SearchCateAuthor.class));
//                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//                        break;

                    case R.id.nav_settings:
                        navItemIndex = 3;
//                        CURRENT_TAG = TAG_SETTING;

                        startActivity(new Intent(NavigationDrawerActivity.this, SettingActivity.class));
                        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);


                        break;

                    case R.id.nav_share:
                        navItemIndex = 4;

//                        CURRENT_TAG = TAG_SETTING;
                        Intent share = new Intent(Intent.ACTION_SEND);
                        share.setType("text/plain");
                        share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

                        // Add data to the intent, the receiving app will decide
                        // what to do with it.

//                share.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post: ");
                        share.putExtra(Intent.EXTRA_TEXT, "Positive Vibes \n" + " Download this great App to bring a positive change in your life and society \n" + "\n" + "https://play.google.com/store/apps/details?id=com.positivevibes.app");

//        share.putExtra(Intent.EXTRA_TEXT, Constants.URL.BASE_URL+feed.getPhoto_url()+" \n "+feed.getFeed_description());
//                share.putExtra(Intent.EXTRA_TEXT, "hi how r u ");


                        NavigationDrawerActivity.this.startActivity(Intent.createChooser(share, "Share link!"));



                        break;

                        case R.id.nav_rate_us:
                        navItemIndex = 5;
//                        CURRENT_TAG = TAG_SETTING;

                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.positivevibes.app")));

                        break;


                    default:
                        navItemIndex =0;
                }




                //Checking if the item is in checked state or not, if not make it in checked state
//                if (menuItem.isChecked()) {
//                    menuItem.setChecked(false);
//                } else {
//                    menuItem.setChecked(true);  // if  uncomment then shade ill come on
//                }
//                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                UncheckItems();
                super.onDrawerClosed(drawerView);

            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);

            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();


            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }

        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

//        Toast.makeText(NavigationDrawerActivity.this,"Menu",Toast.LENGTH_SHORT).show();

//        custom layout
        menuu = menu;

        getMenuInflater().inflate(R.menu.main, menu);

        notification_menuitem = menu.findItem(R.id.notification);
        search_menuitem = menu.findItem(R.id.search);
        add_prey = menu.findItem(R.id.add_prey);


        View actionView = notification_menuitem.getActionView();
        View actionViewSearch = search_menuitem.getActionView();
        View actionViewadd = add_prey.getActionView();

        if (actionView != null) {
            noti_count = actionView.findViewById(R.id.count_tv);
            noti_img = actionView.findViewById(R.id.noti_img);
            noti_img_rel = actionView.findViewById(R.id.noti_img_rel);

            // get badge count
            SharedPreferences mPrefs = getSharedPreferences("NotificationBadgeCount", Context.MODE_PRIVATE);

            int count = mPrefs.getInt("count", 0);


            // get badge count

            if (count > 0) {
                noti_count.setVisibility(View.VISIBLE);

                String stringCount = String.valueOf(count);

                noti_count.setText(stringCount);


            } else {
//                    clear count
                noti_count.setVisibility(View.GONE);


            }


        }
        if (actionViewSearch != null) {
            search_img = actionViewSearch.findViewById(R.id.search_img);
            search_img_rel = actionViewSearch.findViewById(R.id.search_img_rel);

        }


        search_img_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(NavigationDrawerActivity.this,SearchCateAuthor.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);


            }
        });

        noti_img_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // clear badge count
                SharedPreferences mPrefs = getSharedPreferences("NotificationBadgeCount", Context.MODE_PRIVATE);


                SharedPreferences.Editor editor = mPrefs.edit();
                editor.putInt("count", 0);
                editor.apply();

                ShortcutBadger.removeCount(NavigationDrawerActivity.this);

                // clear badge count

                startActivity(new Intent(NavigationDrawerActivity.this, NotificationActivity.class));
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                updateBadgeCount();
//                navItemIndex = 3;
//                CURRENT_TAG = TAG_NOTIFICATION;

//                loadHomeFragment();

//                Toast.makeText(getApplicationContext(), "Notification NEW !", Toast.LENGTH_LONG).show();

//                noti_count.setText("22");
            }
        });

        if (actionViewadd != null) {
            add_img = actionViewadd.findViewById(R.id.add_img_img);
            add_prey_rel = actionViewadd.findViewById(R.id.add_prey_img_rel);
        }

        add_prey_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NavigationDrawerActivity.this, AddPreyActivity.class);
                intent.putExtra("ACTIVITY", "main");
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });




//        custom layout


        // Inflate the menu; this adds items to the action bar if it is present.

        // show menu only when home fragment is selected
//        if (navItemIndex == 0) {
//            getMenuInflater().inflate(R.menu.main, menu);
//        }
//
//        // when fragment is notifications, load the menu created for notifications
//        if (navItemIndex == 3) {
//            getMenuInflater().inflate(R.menu.notifications, menu);
//        }


        // test

////        void tintMenuIcons(Menu menu)
////        {
//            MenuItem item;
//            int color;
//
//            AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
//
//            for(int i = 0; i < menu.size(); i++)
//            {
//                item  = menu.getItem(i);
//                color = Color.parseColor("#26a658");
//
//                DrawableCompat.setTint(item.getIcon(), color);
//            }
////        }
        // test
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        // user is in notifications fragment
        // and selected 'Mark all as Read'
        if (id == R.id.action_mark_all_read) {
            Toast.makeText(getApplicationContext(), "All notifications marked as read!", Toast.LENGTH_LONG).show();
        }

        // user is in notifications fragment
        // and selected 'Clear All'
        if (id == R.id.action_clear_notifications) {
            Toast.makeText(getApplicationContext(), "Clear all notifications!", Toast.LENGTH_LONG).show();
        }


        if (id == R.id.search) {
            Toast.makeText(getApplicationContext(), "Search !", Toast.LENGTH_LONG).show();
        }
        if (id == R.id.notification) {
            Toast.makeText(getApplicationContext(), "Notification !", Toast.LENGTH_LONG).show();

            noti_count.setText("22");
        }

        return super.onOptionsItemSelected(item);
    }

    // show or hide the fab
    private void toggleFab() {
//        if (navItemIndex == 0)
//            fab.show();
//        else
//            fab.hide();
    }

    public void updateBadgeCount() {

        try {

        SharedPreferences mPrefs = getSharedPreferences("NotificationBadgeCount", Context.MODE_PRIVATE);

        final int count = mPrefs.getInt("count", 0);

        //
        if (count > 0) {
            noti_count.setVisibility(View.VISIBLE);

            String stringCount = String.valueOf(count);

            noti_count.setText(stringCount);


        } else {
            noti_count.setVisibility(View.GONE);


        }
        }catch (Exception e){
            Log.e("Count Visibility", "Count Visibility me mslaa aya hy");
        }


    }

    @Override
    protected void onResume() {
        super.onResume();

//        AnalyticsTrackers.getInstance().trackScreenView("NavigationDrawer Activity");

        ApiGetProfile();

        SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        USER_DP = mPrefs.getString("USER_DP", "");
        USER_NAME = mPrefs.getString("USER_NAME", "");

        txtName.setText(USER_NAME);


//        // Loading profile image
//        Glide.with(this).load(Constants.URL.BASE_URL + profile_data.getActive_dp())
//                .crossFade()
//                .thumbnail(0.5f)
//                .bitmapTransform(new CircleTransform(this))
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .error(R.drawable.profile_icon)
//                .into(imgProfile);


//        updateHotCount();

//        Toast.makeText(NavigationDrawerActivity.this,"Resume",Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        updateBadgeCount();

//        Toast.makeText(NavigationDrawerActivity.this,"Restart",Toast.LENGTH_SHORT).show();
    }

    private void ApiGetProfile(){
        // hit api............

        SharedPreferences mPrefs = NavigationDrawerActivity.this.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String token = mPrefs.getString("USER_TOKEN", "");


        Map<String, String> postParam = new HashMap<String, String>();
//        postParam.put("problem",problem.getText().toString());


        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("x-sh-auth", token);

        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.GET_PROFILE, NavigationDrawerActivity.this, postParam, headers, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result, String ERROR) {

                if (ERROR.isEmpty()) {

                    try {


                        JSONObject jsonObject = new JSONObject(String.valueOf(result));

                        String id = jsonObject.getString("_id");
                        String Email = jsonObject.getString("email");
                        boolean confirmed=jsonObject.getBoolean("confirmed");
                        String fullName = jsonObject.getString("full_name");
                        String Gender = jsonObject.getString("gender");
                        boolean admin_access=jsonObject.getBoolean("admin_access");
                        String dp_active_file = jsonObject.getString("dp_active_file");
                        String country = jsonObject.getString("country");
                        String about = jsonObject.getString("about");
                        String achievements = jsonObject.getString("achievements");
                        String date_of_birth = jsonObject.getString("date_of_birth");
                        String age = jsonObject.getString("age");


                        Profile obj=new Profile();
                        obj.setProfile_id(id);
                        obj.setEmail(Email);
                        obj.setConfirmed(confirmed);
                        obj.setFull_name(fullName);
                        obj.setGender(Gender);
                        obj.setAdmin_access(admin_access);
                        obj.setActive_dp(dp_active_file);
                        obj.setCountry(country);
                        obj.setAbout(about);
                        obj.setAchievements(achievements);
                        obj.setDate_of_birth(date_of_birth);
                        obj.setAge(age);

                        profile_data=obj;

                        SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                        mPrefs.edit().putString("USER_DP", profile_data.getActive_dp()).apply();

                        // Loading profile image

                        img_linear.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

                        Glide.with(NavigationDrawerActivity.this).load(Constants.URL.BASE_URL + profile_data.getActive_dp())
                                .crossFade()
                                .thumbnail(0.5f)
                                .bitmapTransform(new CircleTransform(NavigationDrawerActivity.this))
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .error(R.drawable.profile_icon)
                                .into(imgProfile);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {


                    if (ERROR.equals("401")) {

                        SharedPreferences mPrefs = NavigationDrawerActivity.this.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                        SharedPreferences.Editor mEditor = mPrefs.edit();
                        mEditor.putString("USER_EMAIL", "");
                        mEditor.putString("USER_PASSWORD", "");
                        mEditor.putString("USER_TOKEN", "");
                        mEditor.putString("USER_PIC", "");
                        mEditor.apply();
                        Intent intt = new Intent(NavigationDrawerActivity.this, FirstActivity.class);
                        startActivity(intt);
                        finish();
                    } else {

                        Toast.makeText(NavigationDrawerActivity.this, ERROR, Toast.LENGTH_SHORT).show();

                    }

                }

            }
        });
        // hit api--------------------
    }

    private void InviteFriend(){

        d = new Dialog(NavigationDrawerActivity.this, getChangingConfigurations());
        d.requestWindowFeature(Window.FEATURE_NO_TITLE);
        d.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        d.setContentView(R.layout.invite_friends_dialogbox);
        send = d.findViewById(R.id.send);
        cancel = d.findViewById(R.id.cancel);
        d.setCanceledOnTouchOutside(false);

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= 23) {
                    if (checkPermission()){
                        Intent intent = new Intent(NavigationDrawerActivity.this, ContactPickerActivity.class)
                                .putExtra(ContactPickerActivity.EXTRA_THEME, mDarkTheme ? R.style.ContactPicker_Theme_Dark : R.style.ContactPicker_Theme_Light)
                                .putExtra(ContactPickerActivity.EXTRA_CONTACT_BADGE_TYPE, ContactPictureType.ROUND.name())
                                .putExtra(ContactPickerActivity.EXTRA_SHOW_CHECK_ALL, true)
                                .putExtra(ContactPickerActivity.EXTRA_CONTACT_DESCRIPTION, ContactDescription.ADDRESS.name())
                                .putExtra(ContactPickerActivity.EXTRA_CONTACT_DESCRIPTION_TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                                .putExtra(ContactPickerActivity.EXTRA_CONTACT_SORT_ORDER, ContactSortOrder.AUTOMATIC.name());
                        startActivityForResult(intent, REQUEST_CONTACT);
                        d.dismiss();
                    }else {
                        requestPermission();
                        d.dismiss();
                    }
                }else {
                    Intent intent = new Intent(NavigationDrawerActivity.this, ContactPickerActivity.class)
                            .putExtra(ContactPickerActivity.EXTRA_THEME, mDarkTheme ? R.style.ContactPicker_Theme_Dark : R.style.ContactPicker_Theme_Light)
                            .putExtra(ContactPickerActivity.EXTRA_CONTACT_BADGE_TYPE, ContactPictureType.ROUND.name())
                            .putExtra(ContactPickerActivity.EXTRA_SHOW_CHECK_ALL, true)
                            .putExtra(ContactPickerActivity.EXTRA_CONTACT_DESCRIPTION, ContactDescription.ADDRESS.name())
                            .putExtra(ContactPickerActivity.EXTRA_CONTACT_DESCRIPTION_TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
                            .putExtra(ContactPickerActivity.EXTRA_CONTACT_SORT_ORDER, ContactSortOrder.AUTOMATIC.name());
                    startActivityForResult(intent, REQUEST_CONTACT);
                    d.dismiss();
                }
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss();
            }
        });

        d.show();
        Window window = d.getWindow();
        window.setLayout(RelativeLayout.LayoutParams.FILL_PARENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_CONTACT && resultCode == Activity.RESULT_OK &&
                data != null && data.hasExtra(ContactPickerActivity.RESULT_CONTACT_DATA)) {

            // we got a result from the contact picker

            if (Build.VERSION.SDK_INT >= 23) {
                if (checkPermission()){
                    contacts_list = new ArrayList<>();

                    // process contacts
                    List<Contact> contacts = (List<Contact>) data.getSerializableExtra(ContactPickerActivity.RESULT_CONTACT_DATA);

                    array = new ArrayList<>();

                    String mobile_name = Build.MANUFACTURER;

                    if (mobile_name.equals("samsung")){
                        for (int i = 0; i < contacts.size(); i++) {
                            array.add(contacts.get(i).getPhone(0)+",");
                        }
                    }else {
                        for (int i = 0; i < contacts.size(); i++) {
                            array.add(contacts.get(i).getPhone(0)+";");
                        }
                    }

                    sendSMS(array, "Have you seen Positive Vibes?\n" + "Download this App to get Motivation\n\n"
                            + "https://play.google.com/store/apps/details?id=com.positivevibes.app");
                    // process groups
                    List<Group> groups = (List<Group>) data.getSerializableExtra(ContactPickerActivity.RESULT_GROUP_DATA);
//                    for (Group group : groups) {
                        // process the groups...
//                    }
                }else {
                    requestPermission();
                }
            }else {
                contacts_list = new ArrayList<>();

                // process contacts
                List<Contact> contacts = (List<Contact>) data.getSerializableExtra(ContactPickerActivity.RESULT_CONTACT_DATA);

//                String name = contacts.get(0).getDisplayName();
//                String address = contacts.get(0).getAddress(0);
//                String email = contacts.get(0).getEmail(0);
//                String phone = contacts.get(0).getPhone(0);
//                char contactLetter = contacts.get(0).getContactLetter();
//                long id = contacts.get(0).getId();
//                String lookupKey = contacts.get(0).getLookupKey();
//                String wdwe = "";

                array = new ArrayList<>();

                String mobile_name = Build.MANUFACTURER;

                if (mobile_name.equals("samsung")){
                    for (int i = 0; i < contacts.size(); i++) {
                        array.add(contacts.get(i).getPhone(0)+",");
                    }
                }else {
                    for (int i = 0; i < contacts.size(); i++) {
                        array.add(contacts.get(i).getPhone(0)+";");
                    }
                }

                sendSMS(array, "Have you seen Positive Vibes?\n" + "Download this App to get Motivation\n\n"
                        + "https://play.google.com/store/apps/details?id=com.positivevibes.app");

                // process groups
                List<Group> groups = (List<Group>) data.getSerializableExtra(ContactPickerActivity.RESULT_GROUP_DATA);
//                for (Group group : groups) {
                    // process the groups...
//                }
            }
        }
    }

    private void sendSMS(List<String> contacts, String message) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) // At least KitKat
        {
            String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(this); // Need to change the build to API 19

            String numbers = String.valueOf(contacts);
            if (!numbers.equals("[]")) {
                numbers = numbers.substring(1, numbers.length() - 2);
            }else {
                numbers = "";
            }

            Intent sendIntent = new Intent(Intent.ACTION_SEND);
            sendIntent.setType("text/plain");
            sendIntent.putExtra(Intent.EXTRA_TEXT, message);
            sendIntent.putExtra("address", numbers);

            if (defaultSmsPackageName != null)// Can be null in case that there is no default, then the user would be able to choose
            // any app that support this intent.
            {
                sendIntent.setPackage(defaultSmsPackageName);
            }
            startActivity(sendIntent);

        }
        else // For early versions, do what worked for you before.
        {
            Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
            smsIntent.setType("vnd.android-dir/mms-sms");
            smsIntent.putExtra("address","phoneNumber");
            smsIntent.putExtra("sms_body","message");
            startActivity(smsIntent);
        }
    }

    public boolean checkPermission() {

        int CallPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS);
        int ContactPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CONTACTS);

        return CallPermissionResult == PackageManager.PERMISSION_GRANTED &&
                ContactPermissionResult == PackageManager.PERMISSION_GRANTED;

    }

    private void requestPermission() {

        ActivityCompat.requestPermissions(NavigationDrawerActivity.this, new String[]
                {
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.SEND_SMS
                }, PERMISSION_REQUEST_CODE);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {

            case PERMISSION_REQUEST_CODE:


                if (grantResults.length > 0) {

                    boolean SMSPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean ReadContactsPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (SMSPermission && ReadContactsPermission) {

                        Toast.makeText(NavigationDrawerActivity.this,
                                "Permission accepted", Toast.LENGTH_LONG).show();
//
//                        Intent intent = new Intent(NavigationDrawerActivity.this, ContactPickerActivity.class)
//                                .putExtra(ContactPickerActivity.EXTRA_THEME, mDarkTheme ? R.style.ContactPicker_Theme_Dark : R.style.ContactPicker_Theme_Light)
//                                .putExtra(ContactPickerActivity.EXTRA_CONTACT_BADGE_TYPE, ContactPictureType.ROUND.name())
//                                .putExtra(ContactPickerActivity.EXTRA_SHOW_CHECK_ALL, true)
//                                .putExtra(ContactPickerActivity.EXTRA_CONTACT_DESCRIPTION, ContactDescription.ADDRESS.name())
//                                .putExtra(ContactPickerActivity.EXTRA_CONTACT_DESCRIPTION_TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
//                                .putExtra(ContactPickerActivity.EXTRA_CONTACT_SORT_ORDER, ContactSortOrder.AUTOMATIC.name());
//                        startActivityForResult(intent, REQUEST_CONTACT);

//If permission is denied...//

                    } else {
                        Toast.makeText(NavigationDrawerActivity.this,
                                "Permission denied", Toast.LENGTH_LONG).show();

//....disable the Call and Contacts buttons//

//                        send.setEnabled(false);
                    }
                    break;
                }
        }
    }


}