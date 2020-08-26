package com.inderproduction.theinderapp.Utilities;


import android.app.Notification;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.core.app.NotificationCompat;

import com.google.firebase.auth.FirebaseAuth;
import com.inderproduction.theinderapp.R;

public class CustomBackgroundService extends Service {

    FirebaseAuth auth = FirebaseAuth.getInstance();

    @Override
    public void onCreate() {
        super.onCreate();

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this,"background");
            Notification f = notificationBuilder.setContentTitle("The Inder App")
                    .setContentText("Keeping you posted!")
                    .setSmallIcon(R.drawable.ic_shopping_cart_black_24dp)
                    .build();
            startForeground(102,f);
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public void onDestroy() {
        super.onDestroy();

    }
}

