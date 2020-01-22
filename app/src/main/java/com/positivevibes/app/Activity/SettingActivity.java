package com.positivevibes.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.positivevibes.app.ApiStructure.ApiModelClass;
import com.positivevibes.app.ApiStructure.Constants;
import com.positivevibes.app.ApiStructure.ServerCallback;
import com.positivevibes.app.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SettingActivity extends AppCompatActivity {

    RelativeLayout back_rel;
    TextView edit_profile,change_password,push_notification,privacy_policy,logout,terms_services,blog,
            report_problem,share_friends,select_catagories,rate_us, update, update_badge, invite;

    public static boolean COMMENT_LIKE_NOTIFICATION;
    public static boolean FEED_ADD_NOTIFICATION;

//    private static final String EXTRA_DARK_THEME = "EXTRA_DARK_THEME";
//    private static final String EXTRA_GROUPS = "EXTRA_GROUPS";
//    private static final String EXTRA_CONTACTS = "EXTRA_CONTACTS";

//    private static final int REQUEST_CONTACT = 0;
//    private static final int PERMISSION_REQUEST_CODE = 1;


    private boolean mDarkTheme;

    List<String> contacts_list;
    List<String> array;


    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }


    // send message code


//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_CONTACT && resultCode == Activity.RESULT_OK &&
//                data != null && data.hasExtra(ContactPickerActivity.RESULT_CONTACT_DATA)) {
//
//            // we got a result from the contact picker
//
//            if (Build.VERSION.SDK_INT >= 23) {
//                if (checkPermission()){
//                    contacts_list = new ArrayList<>();
//
//                    // process contacts
//                    List<Contact> contacts = (List<Contact>) data.getSerializableExtra(ContactPickerActivity.RESULT_CONTACT_DATA);
//
//                    array = new ArrayList<>();
//
//                    String mobile_name = Build.MANUFACTURER;
//
//                    if (mobile_name.equals("samsung")){
//                        for (int i = 0; i < contacts.size(); i++) {
//                            array.add(contacts.get(i).getPhone(0)+",");
//                        }
//                    }else {
//                        for (int i = 0; i < contacts.size(); i++) {
//                            array.add(contacts.get(i).getPhone(0)+";");
//                        }
//                    }
//
//                    sendSMS(array, "Have you seen Positive Vibes?\n" + "Download this App to get Motivation\n\n"
//                            + "https://play.google.com/store/apps/details?id=com.positivevibes.app");
//
//                    // process groups
//                    List<Group> groups = (List<Group>) data.getSerializableExtra(ContactPickerActivity.RESULT_GROUP_DATA);
//                    for (Group group : groups) {
//                        // process the groups...
//                    }
//                }else {
//                    requestPermission();
//                }
//            }else {
//                contacts_list = new ArrayList<>();
//
//                // process contacts
//                List<Contact> contacts = (List<Contact>) data.getSerializableExtra(ContactPickerActivity.RESULT_CONTACT_DATA);
//
////                String name = contacts.get(0).getDisplayName();
////                String address = contacts.get(0).getAddress(0);
////                String email = contacts.get(0).getEmail(0);
////                String phone = contacts.get(0).getPhone(0);
////                char contactLetter = contacts.get(0).getContactLetter();
////                long id = contacts.get(0).getId();
////                String lookupKey = contacts.get(0).getLookupKey();
////                String wdwe = "";
//
//                array = new ArrayList<>();
//
//                String mobile_name = Build.MANUFACTURER;
//
//                if (mobile_name.equals("samsung")){
//                    for (int i = 0; i < contacts.size(); i++) {
//                        array.add(contacts.get(i).getPhone(0)+",");
//                    }
//                }else {
//                    for (int i = 0; i < contacts.size(); i++) {
//                        array.add(contacts.get(i).getPhone(0)+";");
//                    }
//                }
//
//                sendSMS(array, "Have you seen Positive Vibes?\n" + "Download this App to get Motivation\n\n"
//                        + "https://play.google.com/store/apps/details?id=com.positivevibes.app");
//
//                // process groups
//                List<Group> groups = (List<Group>) data.getSerializableExtra(ContactPickerActivity.RESULT_GROUP_DATA);
//                for (Group group : groups) {
//                    // process the groups...
//                }
//            }
//        }
//    }

    // send message code



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        initialization();

//        AnalyticsTrackers.initialize(this);
//
//        AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP, "UA-123157129-1");
//        AnalyticsTrackers.getInstance().trackScreenView("Settings Activity");


        hitApiForPushNotification();

        // Update Count

        SharedPreferences sp = getSharedPreferences("UpdateBadgeCount",Context.MODE_PRIVATE);
        final int update_count = sp.getInt("update_count",0);

        if (update_count > 0) {
            update_badge.setVisibility(View.VISIBLE);
            update_badge.setText(String.valueOf(update_count));
        }else {
            update_badge.setVisibility(View.GONE);
        }

        // Update Count


        rate_us.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.positivevibes.app")));

            }
        });

        edit_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intt = new Intent(SettingActivity.this, EditProfileActivity.class);
                startActivity(intt);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);



            }
        });

        change_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intt = new Intent(SettingActivity.this, ChangePasswordActivity.class);
                startActivity(intt);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

//                replaceFragment(new InProfileSettingChangePassword(),"InProfileSettingChangePassword");

            }
        });
        push_notification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intt = new Intent(SettingActivity.this, PushNotificationActivity.class);
                startActivity(intt);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

//                replaceFragment(new InProfileSettingPushNotification(),"InProfileSettingPushNotification");

            }
        });
        select_catagories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intt = new Intent(SettingActivity.this, SelectCatagoryActivity.class);
                intt.putExtra("FromSetting",true);
                startActivity(intt);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

//                replaceFragment(new InProfileSettingEmailSmsNotification(),"InProfileSettingEmailSmsNotification");

            }
        });
        privacy_policy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intt = new Intent(SettingActivity.this, PrivacyPolicyActivity.class);
                startActivity(intt);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

//                replaceFragment(new InProfileSettingPrivacyPolicy(),"InProfileSettingPrivacyPolicy");

            }
        });
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Clear Count

                SharedPreferences mPrefs = getSharedPreferences("UpdateBadgeCount",Context.MODE_PRIVATE);

                SharedPreferences.Editor editor = mPrefs.edit();
                editor.putInt("update_count", 0);
                editor.apply();

                update_badge.setVisibility(View.GONE);

                // Clear Count

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.positivevibes.app"));
                startActivity(intent);
            }
        });
        terms_services.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intt = new Intent(SettingActivity.this, TermsOfServiceActivity.class);
                startActivity(intt);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

//                replaceFragment(new InProfileSettingTermsOfService(),"InProfileSettingTermsOfService");

            }
        });
        blog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("http://positivitymessage.com/"); // missing 'http://' will cause crashed
                Intent intent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });

        report_problem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent intt = new Intent(SettingActivity.this, ReportProblemActivity.class);
                startActivity(intt);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);



                // for mail
//                Intent intent = new Intent(Intent.ACTION_SEND);
//                intent.setType("plain/text");
//                intent.putExtra(Intent.EXTRA_EMAIL, new String[] { "haseeb@dynamiclogix.com" });
//                intent.putExtra(Intent.EXTRA_SUBJECT, "Report");
////       mContext.startActivity(Intent.createChooser(intent, "Send your email in:"));
//
//                SettingActivity.this.startActivity(intent);
//                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                // for mail
            }
        });

        share_friends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

                // Add data to the intent, the receiving app will decide
                // what to do with it.

//                share.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post: ");
                share.putExtra(Intent.EXTRA_TEXT, "Positivity Vibes \n"+" Text \n"+"\n"+"http://positivitymessage.com/");

//        share.putExtra(Intent.EXTRA_TEXT, Constants.URL.BASE_URL+feed.getPhoto_url()+" \n "+feed.getFeed_description());
//                share.putExtra(Intent.EXTRA_TEXT, "hi how r u ");


                SettingActivity.this.startActivity(Intent.createChooser(share, "Share link!"));
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





//                    hit api ---------------------------------------------------



                SharedPreferences mPrefs = SettingActivity.this.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                String token = mPrefs.getString("USER_TOKEN", "");


                Map<String, String> postParam = new HashMap<String, String>();


                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("x-sh-auth", token);

                ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.LOGOUT, SettingActivity.this, postParam, headers, new ServerCallback() {
                    @Override
                    public void onSuccess(JSONObject result, String ERROR) {

                        if (ERROR.isEmpty()) {

                            try {


                                JSONObject jsonObject = new JSONObject(String.valueOf(result));

                                int code = jsonObject.getInt("code");
                                if (code == 200) {




                                    // clear badge count
                                    SharedPreferences mPrefs=getSharedPreferences("NotificationBadgeCount",Context.MODE_PRIVATE);


                                    SharedPreferences.Editor editor=mPrefs.edit();
                                    editor.putInt("count",0);
                                    editor.apply();


                                    // clear badge count

                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        } else {


                            if (ERROR.equals("401")) {

                                SharedPreferences mPrefs = SettingActivity.this.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                                SharedPreferences.Editor mEditor = mPrefs.edit();
                                mEditor.putString("USER_EMAIL", "");
                                mEditor.putString("USER_PASSWORD", "");
                                mEditor.putString("USER_TOKEN", "");
                                mEditor.putString("USER_PIC", "");
                                mEditor.apply();
                                Intent intt = new Intent(SettingActivity.this, FirstActivity.class);
                                SettingActivity.this.startActivity(intt);
                                finish();

                            } else {

                                Toast.makeText(SettingActivity.this, ERROR, Toast.LENGTH_SHORT).show();

                            }

                        }

                    }
                });




//                    hit api ---------------------------------------------------




                mPrefs = SettingActivity.this.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                SharedPreferences.Editor mEditor = mPrefs.edit();
                mEditor.putString("USER_EMAIL", "");
                mEditor.putString("USER_PASSWORD", "");
                mEditor.putString("USER_TOKEN", "");
                mEditor.putString("USER_PIC", "");
                mEditor.apply();

                Intent intent = new Intent(SettingActivity.this, FirstActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

//                SettingActivity.this.finish();


                finish();



            }
        });

        back_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

            }
        });

        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {





                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

                // Add data to the intent, the receiving app will decide
                // what to do with it.

//                share.putExtra(Intent.EXTRA_SUBJECT, "Title Of The Post: ");
                share.putExtra(Intent.EXTRA_TEXT, "Have you seen Positive Vibes?\n" + "Download this App to get Motivation\n\n"
                        + "https://play.google.com/store/apps/details?id=com.positivevibes.app");

//        share.putExtra(Intent.EXTRA_TEXT, Constants.URL.BASE_URL+feed.getPhoto_url()+" \n "+feed.getFeed_description());
//                share.putExtra(Intent.EXTRA_TEXT, "hi how r u ");


                SettingActivity.this.startActivity(Intent.createChooser(share, "Share link!"));



            }
        });
    }
    // send message code

//    private void sendSMS(List<String> contacts, String message) {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) // At least KitKat
//        {
//            String defaultSmsPackageName = Telephony.Sms.getDefaultSmsPackage(this); // Need to change the build to API 19
//
//            String numbers = String.valueOf(contacts);
//            if (!numbers.equals("[]")) {
//                numbers = numbers.substring(1, numbers.length() - 2);
//            }else {
//                numbers = "";
//            }
//
//            Intent sendIntent = new Intent(Intent.ACTION_SEND);
//            sendIntent.setType("text/plain");
//            sendIntent.putExtra(Intent.EXTRA_TEXT, message);
//            sendIntent.putExtra("address", numbers);
//
//            if (defaultSmsPackageName != null)// Can be null in case that there is no default, then the user would be able to choose
//            // any app that support this intent.
//            {
//                sendIntent.setPackage(defaultSmsPackageName);
//            }
//            startActivity(sendIntent);
//
//        }
//        else // For early versions, do what worked for you before.
//        {
//            Intent smsIntent = new Intent(android.content.Intent.ACTION_VIEW);
//            smsIntent.setType("vnd.android-dir/mms-sms");
//            smsIntent.putExtra("address","phoneNumber");
//            smsIntent.putExtra("sms_body","message");
//            startActivity(smsIntent);
//        }
//    }
//
//    public boolean checkPermission() {
//
//        int CallPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS);
//        int ContactPermissionResult = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_CONTACTS);
//
//        return CallPermissionResult == PackageManager.PERMISSION_GRANTED &&
//                ContactPermissionResult == PackageManager.PERMISSION_GRANTED;
//
//    }
//
//    private void requestPermission() {
//
//        ActivityCompat.requestPermissions(SettingActivity.this, new String[]
//                {
//                        Manifest.permission.READ_CONTACTS,
//                        Manifest.permission.SEND_SMS
//                }, PERMISSION_REQUEST_CODE);
//
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        switch (requestCode) {
//
//            case PERMISSION_REQUEST_CODE:
//
//
//                if (grantResults.length > 0) {
//
//                    boolean SMSPermission = grantResults[0] == PackageManager.PERMISSION_GRANTED;
//                    boolean ReadContactsPermission = grantResults[1] == PackageManager.PERMISSION_GRANTED;
//
//                    if (SMSPermission && ReadContactsPermission) {
//
//                        Toast.makeText(SettingActivity.this,
//                                "Permission accepted", Toast.LENGTH_LONG).show();
//
//                        Intent intent = new Intent(SettingActivity.this, ContactPickerActivity.class)
//                                .putExtra(ContactPickerActivity.EXTRA_THEME, mDarkTheme ? R.style.ContactPicker_Theme_Dark : R.style.ContactPicker_Theme_Light)
//                                .putExtra(ContactPickerActivity.EXTRA_CONTACT_BADGE_TYPE, ContactPictureType.ROUND.name())
//                                .putExtra(ContactPickerActivity.EXTRA_SHOW_CHECK_ALL, true)
//                                .putExtra(ContactPickerActivity.EXTRA_CONTACT_DESCRIPTION, ContactDescription.ADDRESS.name())
//                                .putExtra(ContactPickerActivity.EXTRA_CONTACT_DESCRIPTION_TYPE, ContactsContract.CommonDataKinds.Email.TYPE_WORK)
//                                .putExtra(ContactPickerActivity.EXTRA_CONTACT_SORT_ORDER, ContactSortOrder.AUTOMATIC.name());
//                        startActivityForResult(intent, REQUEST_CONTACT);
//
////If permission is denied...//
//
//                    } else {
//                        Toast.makeText(SettingActivity.this,
//                                "Permission denied", Toast.LENGTH_LONG).show();
//
////....disable the Call and Contacts buttons//
//
//                        invite.setEnabled(false);
//                    }
//                    break;
//                }
//        }
//    }

    // send message code

    private void hitApiForPushNotification() {

        // hit api............

        SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
        String token = mPrefs.getString("USER_TOKEN", "");


        Map<String, String> postParam = new HashMap<String, String>();


        HashMap<String, String> headers = new HashMap<String, String>();
        headers.put("x-sh-auth", token);

        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.GET_NOTIFICATION_STATUS, SettingActivity.this, postParam, headers, new ServerCallback() {
            @Override
            public void onSuccess(JSONObject result, String ERROR) {

                if (ERROR.isEmpty()) {

                    try {


                        JSONObject jsonObject = new JSONObject(String.valueOf(result));

                        int code = jsonObject.getInt("code");
                        if (code == 200) {

                            boolean comment_like_notification = jsonObject.getBoolean("comment_like_notification");
                            boolean feed_add_notification = jsonObject.getBoolean("feed_add_notification");
                            COMMENT_LIKE_NOTIFICATION =comment_like_notification;
                            FEED_ADD_NOTIFICATION =feed_add_notification;


                        }


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else {


                    if (ERROR.equals("401")) {

                        SharedPreferences mPrefs = getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                        SharedPreferences.Editor mEditor = mPrefs.edit();
                        mEditor.putString("USER_EMAIL", "");
                        mEditor.putString("USER_PASSWORD", "");
                        mEditor.putString("USER_TOKEN", "");
                        mEditor.putString("USER_PIC", "");
                        mEditor.apply();
                        Intent intt = new Intent(SettingActivity.this, FirstActivity.class);
                        startActivity(intt);
                        finish();
                    } else {

                        Toast.makeText(SettingActivity.this, ERROR, Toast.LENGTH_SHORT).show();

                    }

                }

            }
        });
        // hit api--------------------



    }




    private void initialization() {


        back_rel = findViewById(R.id.back_rel);
        rate_us = findViewById(R.id.rate_us);
        edit_profile = findViewById(R.id.edit_profile);
        change_password = findViewById(R.id.change_password);
        push_notification = findViewById(R.id.push_notification);
        privacy_policy = findViewById(R.id.privacy_policy);
        terms_services = findViewById(R.id.terms_services);
        report_problem = findViewById(R.id.report_problem);
        share_friends = findViewById(R.id.share_friends);
        select_catagories = findViewById(R.id.select_catagories);
        blog = findViewById(R.id.blog);
        logout = findViewById(R.id.logout);
        update = findViewById(R.id.update);
        update_badge = findViewById(R.id.update_badge);
        invite = findViewById(R.id.invite);


    }


}
