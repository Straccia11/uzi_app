package com.example.uziv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class AlarmActivity extends AppCompatActivity {

    private final int alarmDuration = 10;
    //private boolean interrupt = false;
    private Ringtone ringtone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm);

        Toast.makeText(getApplicationContext(), "Alarm activity launched", Toast.LENGTH_SHORT).show();

        // Text display
        TextView textView = findViewById(R.id.textView2);
        textView.setText("Ding dong, réveille-toi où je te dégonde");

        // Choice of song
        Log.d("CHOICE_SONG", "STEP 1");
        ContentResolver contentResolver = getContentResolver();
        Uri localUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Uri contentUri;
        Cursor cursor = contentResolver.query(localUri, null, null, null, null); //TODO : take care of the case where reading external storage is not allowed
        Log.d("CHOICE_SONG", "STEP 2");
        if (cursor == null) {
            Toast.makeText(getApplicationContext(), "Query failed, default alarm", Toast.LENGTH_SHORT).show();
            contentUri = Uri.parse("android.resource://" + getPackageName() + "/raw/rooster");
        } else if (!cursor.moveToFirst()) {
            Toast.makeText(getApplicationContext(), "No media on device, default alarm", Toast.LENGTH_SHORT).show();
            contentUri = Uri.parse("android.resource://" + getPackageName() + "/raw/rooster");
        } else {
            Log.d("CHOICE_SONG", "STEP 3");
            int nTitles = cursor.getColumnCount();
            int rowIdx = new Random().nextInt(nTitles);
            int titleColumn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Media.TITLE);
            int idColumn = cursor.getColumnIndex(android.provider.MediaStore.Audio.Media._ID);
            Log.d("N_TITLES", "STEP 4");
            for (int i = 0; i < rowIdx; i++) {
                if (!cursor.moveToNext()) {
                    cursor.moveToFirst();
                    Toast.makeText(getApplicationContext(), "Error, last song selected", Toast.LENGTH_SHORT).show();
                    Log.d("CHOICE_SONG", "Last song selected");
                }
            }
            Log.d("CHOICE_SONG", "STEP 5");
            long id = cursor.getLong(idColumn);
            String name = cursor.getString(titleColumn);
            contentUri = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
            Log.d("CHOICE_SONG", "STEP 6");
        }

        // Alarm setup
        //Uri uri = Uri.parse("android.resource://" + getPackageName() + "/raw/rooster"); //get URI of local file
        ringtone = RingtoneManager.getRingtone(getApplicationContext(), contentUri); //create ringtone
        ringtone.setAudioAttributes(new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).build()); //activate alarm mode
        Log.d("CHOICE_SONG", "STEP 7");
        // ringtone.play();

        // The alarms rings for alarmDuration seconds
        // Thread that counts the time

        /*final int start = Calendar.getInstance().get(Calendar.SECOND);
        Timer timer = new Timer();
        Runnable r = new Runnable() {
            public void run(){
                //Display date
                int time = Calendar.getInstance().get(Calendar.SECOND);
                Toast.makeText(getApplicationContext(), time-start, Toast.LENGTH_SHORT).show();
                if (time-start > alarmDuration) {
                    interruptAlarm();
                }
            }
        };
        TimerTask task = new TimerTask() {
            public void run() {
                Handler mHandler = new Handler(Looper.getMainLooper());
                mHandler.post(r);
            }
        };

        timer.scheduleAtFixedRate(task, 0, 1000);*/

        launchAlarm();
    }

    /*public void interruptAlarm() {
        ringtone.stop();
    }*/

    private void launchAlarm() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (ringtone.isPlaying())
                    ringtone.stop();
            }
        }, 1000*alarmDuration);

        ringtone.setLooping(true);
        ringtone.play();
    }

    // Used by the stop_alarm button
    public void interruptAlarm(View view) { //TODO : arrange the button so that the activity stops
        ringtone.stop();
    }
}