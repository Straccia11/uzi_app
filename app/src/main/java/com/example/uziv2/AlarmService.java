package com.example.uziv2;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class AlarmService extends Service {

    private boolean[] alarmStreamOccupied = {false};
    private Timer timer;
    String message;

    public AlarmService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        Toast.makeText(getApplicationContext(), "Alarm service launched",Toast.LENGTH_SHORT).show();
        message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);

        setNotification();

        //Update Thread
        timer = new Timer();

        Runnable r = new Runnable() {
            public void run(){
                //Display date
                Date date = Calendar.getInstance().getTime();
                DateFormat dateFormatAlarm = new SimpleDateFormat("HH:mm");
                String strDateAlarm = dateFormatAlarm.format(date);

                if (strDateAlarm.equals(message)) {
                    if (alarmStreamOccupied[0]) {
                        Toast.makeText(getApplicationContext(), "You were supposed to shut",Toast.LENGTH_SHORT).show();
                    } else {
                        Intent dialogIntent = new Intent(getApplicationContext(), AlarmActivity.class);
                        dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(dialogIntent);
                        // Shutting down
                        stopSelf();
                        alarmStreamOccupied[0] = true;
                    }
                }
            }
        };

        TimerTask task = new TimerTask() {
            public void run() {
                Handler mHandler = new Handler(Looper.getMainLooper());
                mHandler.post(r);
            }
        };

        timer.scheduleAtFixedRate(task, 0, 1000);
        //stopSelf();

        // If we get killed, after returning from here, restart
        return START_NOT_STICKY;
    }

    private void setNotification() {
        Intent notificationIntent = new Intent(this, MainActivity.class);
        // Intent
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, 0);

        // Definition of the notification channel (ONLY FOR APIS 26+)
        CharSequence name = "Uzi";
        String description = "Bratata";
        int importance = NotificationManager.IMPORTANCE_DEFAULT;
        NotificationChannel channel = new NotificationChannel("uzi", name, importance);
        channel.setDescription(description);
        // Register the channel with the system; you can't change the importance
        // or other notification behaviors after this
        NotificationManager notificationManager = getSystemService(NotificationManager.class);
        notificationManager.createNotificationChannel(channel);

        // Definition of the notification
        Notification notification =
                new Notification.Builder(this, "uzi")
                        .setContentTitle("Alarm Ongoing")
                        .setContentText("Set for "+message)
                        //.setSmallIcon(R.drawable.icon)
                        .setContentIntent(pendingIntent)
                        .setTicker("wtf")
                        .build();

        // Launch
        startForeground(1, notification);

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        timer.cancel();
        timer.purge();
    }
}