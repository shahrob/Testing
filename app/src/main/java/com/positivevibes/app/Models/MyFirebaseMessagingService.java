package com.positivevibes.app.Models;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;

import com.positivevibes.app.Activity.FeedDetailedActivity;
import com.positivevibes.app.Activity.PrayDetailedActivity;
import com.positivevibes.app.Activity.ReportsList;
import com.positivevibes.app.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import me.leolin.shortcutbadger.ShortcutBadger;

public class MyFirebaseMessagingService extends FirebaseMessagingService {

    String TAG="";
    String feed_id;
    Context context;
    Intent intent;

    SharedPreferences sp;
    int requestCode;

    NotificationChannel mChannel;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {



        sendNotification(remoteMessage);



    }
    private void sendNotification(RemoteMessage message) {

        sp = getSharedPreferences("REQUESTCODE", MODE_PRIVATE);
        requestCode = sp.getInt("CODE", 0);
//        sp.edit().putInt("CODE", requestCode).apply();

        String title = message.getData().get("title");
        String msg = message.getData().get("message");
        feed_id = message.getData().get("feed_id");
        String type = message.getData().get("type");

        if (type.equals("true")){

            // set badge count
            SharedPreferences mPrefs=getSharedPreferences("NotificationBadgeCount",Context.MODE_PRIVATE);

            int count=mPrefs.getInt("count",0);
            count=count+1;

            ShortcutBadger.applyCount(getApplicationContext(), count);

            SharedPreferences.Editor editor=mPrefs.edit();
            editor.putInt("count",count);
            editor.apply();


            // set badge count

            intent=new Intent(this,FeedDetailedActivity.class);

            intent.putExtra("KEYBOADR_STATUS", "feed_img");
            intent.putExtra("Feed_id", feed_id);
            intent.putExtra("Notification", "true");
        }
        if (type.equals("false")){

            // set badge count
            SharedPreferences mPrefs=getSharedPreferences("NotificationBadgeCount",Context.MODE_PRIVATE);

            int count=mPrefs.getInt("count",0);
            count=count+1;

            ShortcutBadger.applyCount(getApplicationContext(), count);

            SharedPreferences.Editor editor=mPrefs.edit();
            editor.putInt("count",count);
            editor.apply();


            // set badge count

            intent=new Intent(this,PrayDetailedActivity.class);

            intent.putExtra("KEYBOADR_STATUS", "PrayText");
            intent.putExtra("Feed_id", feed_id);
            intent.putExtra("Notification", "true");
        }
        if (type.equals("update")){

            // set update count

            SharedPreferences sp = getSharedPreferences("UpdateBadgeCount",Context.MODE_PRIVATE);

//        ShortcutBadger.applyCount(getApplicationContext(), update_count);

            SharedPreferences.Editor speditor = sp.edit();
            speditor.putInt("update_count", 1);
            speditor.apply();

            // set update count

            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + "com.positivevibes.app"));
        }
        if (type.equals("report")){

            intent=new Intent(this,ReportsList.class);
            intent.putExtra("feed_id", feed_id);

        }

//

        int color = 0xff123456;
        color = getResources().getColor(R.color.green);
        color = ContextCompat.getColor(MyFirebaseMessagingService.this, R.color.green);

        String channel_ID = "idillionaire_channel";
        CharSequence name = "Push Notification";
        int importance = NotificationManager.IMPORTANCE_HIGH;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mChannel = new NotificationChannel(channel_ID, name, importance);
            mChannel.enableLights(true);
            mChannel.setLightColor(Color.YELLOW);
            mChannel.setShowBadge(true);
            mChannel.setLockscreenVisibility(Notification.VISIBILITY_PUBLIC);
        }



        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,requestCode,intent,PendingIntent.FLAG_ONE_SHOT);
        Uri defaultSoundUri= RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificatin_builder=new NotificationCompat.Builder(this, channel_ID);
        notificatin_builder
                .setSmallIcon(R.drawable.notification_small_icon)
                .setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.app_icon_))
                .setContentTitle(title)
                .setContentIntent(pendingIntent)
                .setContentText(msg)
                .setSound(defaultSoundUri)
                .setDefaults(android.app.Notification.DEFAULT_ALL)
                .setAutoCancel(true)
                .setChannelId(channel_ID)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setColor(color)

//                .setCustomContentView(normal_layout)


                .setPriority(android.app.Notification.PRIORITY_HIGH);

        NotificationManager notification_manager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        notification_manager.notify(0,notificatin_builder.build());

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            notification_manager.createNotificationChannel(mChannel);
        }

        requestCode = requestCode + 1;

        sp = getSharedPreferences("REQUESTCODE", MODE_PRIVATE);
        sp.edit().putInt("CODE", requestCode).apply();
    }
}

