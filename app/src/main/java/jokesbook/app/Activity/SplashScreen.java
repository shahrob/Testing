package jokesbook.app.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.WindowManager;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.analytics.FirebaseAnalytics;
import jokesbook.app.R;

import io.fabric.sdk.android.Fabric;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;
    public static int click=0;

    FirebaseAnalytics mFirebaseAnalytics;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash_screen);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);



        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        final Fabric fabric = new Fabric.Builder(this)
                .kits(new Crashlytics())
                .debuggable(true)  // Enables Crashlytics debugger
                .build();
        Fabric.with(fabric);


        new Handler().postDelayed(new Runnable() {

            /*
             * Showing splash screen with a timer. This will be useful when you
             * want to show case your app logo / company
             */

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity


//               SharedPreferences mPrefs = SplashScreen.this.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
//               String email=mPrefs.getString("USER_EMAIL","");
//               if (email.length()==0)
//               {
//                   Intent i = new Intent(SplashScreen.this, FirstActivity.class);
//                   startActivity(i);
//
//                   // close this activity
//                   finish();
//               }
//               else
//               {
//                   Intent i = new Intent(SplashScreen.this, NavigationDrawerActivity.class);
//                   startActivity(i);
//
//                   // close this activity
//                   finish();
//               }
                Intent i = new Intent(SplashScreen.this, FirstActivity.class);
                  startActivity(i);
//
//                   // close this activity
                   finish();


            }
        }, SPLASH_TIME_OUT);


//        AnalyticsTrackers.initialize(this);
////        Log.d("analyticsID", );
//        AnalyticsTrackers.getInstance().get(AnalyticsTrackers.Target.APP, "UA-123157129-1");
//        AnalyticsTrackers.getInstance().trackScreenView("Splash Activity");

    }
}
