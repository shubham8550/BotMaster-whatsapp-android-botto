package com.basementgeniusstudios.botmaster;

import android.content.Context;
import android.os.Bundle;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;

import com.basementgeniusstudios.botmaster.api.AccountManager;
import com.robj.notificationhelperlibrary.utils.NotificationUtils;

import org.json.JSONException;

import models.Action;


public class NotificationService extends NotificationListenerService {

    Context context;
    String TAG="TAG";

    public static int cycle=0;



    @Override
    public void onCreate() {

        super.onCreate();
        context = getApplicationContext();
        Log.d("pokemon","on create called");
        new sync(context,getString(R.string.server_url)).start();



    }
    public void syncer(){
        if(cycle>sync.frequency){
            cycle=0;
            Log.d("pokemon","syncing");
            new sync(context,getText(R.string.server_url).toString()).start();
        }
        cycle++;
    }
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        syncer();
        if(AccountManager.isExpired){
            Log.d("pokemon","Expired or data not exist :isEx :"+AccountManager.isExpired);
            return;
        }

        NotificationService.this.cancelNotification(sbn.getKey());
        final Action action = NotificationUtils.getQuickReplyAction(sbn.getNotification(), getPackageName());

        if (action != null) {
            Bundle extras = sbn.getNotification().extras;

            String title = extras.getString("android.title");
            String text = extras.getCharSequence("android.text").toString();
            String package_name = sbn.getPackageName();

          //  new Adapter(text,title,package_name,action,context);
            Log.d("pokemon",title+" =: "+text);



//
//            try {
//                //action.sendReply(getApplicationContext(), "Hello");
//                action.sendReply(getApplicationContext(),"hello");
//
//            } catch (PendingIntent.CanceledException e) {
//                Log.i(TAG, "CRAP " + e.toString());
//            }
        }else {
            Log.i(TAG, "not success");
        }


    }




    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i("Msg","Notification Removed");

    }

}