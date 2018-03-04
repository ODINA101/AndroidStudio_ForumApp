package app.iraklisamniashvilii.com.geoforum;

import android.content.Intent;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.concurrent.ExecutionException;

import app.iraklisamniashvilii.com.geoforum.MyNotificationManager;

public class FBMessagingService extends FirebaseMessagingService  {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        // ...

        // TODO(developer): Handle FCM messages here.
        // Not getting messages here? See why this may be: https://goo.gl/39bRNJ
        Log.d("defaaq", "From: " + remoteMessage.getFrom());



        if (remoteMessage.getData().size() > 0) {
            Log.d("moitanaa", "Message data payload: " + remoteMessage.getData());




        }

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.d("raaaa", "Message Notification Body: " + remoteMessage.getNotification().getBody());

            notifyUser(remoteMessage.getFrom(),remoteMessage.getNotification().getBody());
        }
        // Also if you intend on generating your own notifications as a result of a received FCM
        // message, here is where that should be initiated. See sendNotification method below.
    }
    public void notifyUser(String from,String notification) {
        MyNotificationManager myNotificationManager = new MyNotificationManager( getApplicationContext() );
        myNotificationManager.showSmallNotification( from,notification,new Intent( getApplicationContext(),MainActivity.class ) );
    }
}