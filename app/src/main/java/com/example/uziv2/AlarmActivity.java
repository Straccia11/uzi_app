package com.example.uziv2;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.media.AudioAttributes;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Random;

public class AlarmActivity extends AppCompatActivity {

    private final int alarmDuration = 10;
    //private boolean interrupt = false;
    private Ringtone ringtone;
    private Vibrator vib;
    public int alarmIdx;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        alarmIdx = getIntent().getIntExtra("alarmIdx", -1);
        setContentView(R.layout.activity_alarm);
        setTurnScreenOn(true);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        Toast.makeText(getApplicationContext(), "Alarm activity launched", Toast.LENGTH_SHORT).show();

        // Text display
        TextView textView = findViewById(R.id.textView);
        textView.setText("Ding dong");

        // Choice of song
        Log.d("CHOICE_SONG", "STEP 1");
        ContentResolver contentResolver = getContentResolver();
        Uri localUri = android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        //Uri contentUri;
        Cursor cursor = contentResolver.query(localUri, null, null, null, null); //TODO : take care of the case where reading external storage is not allowed
        Log.d("CHOICE_SONG", "STEP 2");
        if (cursor == null) {
            Toast.makeText(getApplicationContext(), "Query failed, default alarm", Toast.LENGTH_SHORT).show();
            //contentUri = Uri.parse("android.resource://" + getPackageName() + "/raw/rooster");
            setDefaultRingtone();
        } else if (!cursor.moveToFirst()) {
            Toast.makeText(getApplicationContext(), "No media on device, default alarm", Toast.LENGTH_SHORT).show();
            //contentUri = Uri.parse("android.resource://" + getPackageName() + "/raw/rooster");
            setDefaultRingtone();
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
            //contentUri = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
            setRandomRingtone(id);
            Log.d("CHOICE_SONG", "STEP 6");
        }
        // I want the default ringtone
        setDefaultRingtone();

        // Alarm setup
        //Uri uri = Uri.parse("android.resource://" + getPackageName() + "/raw/rooster"); //get URI of local file
        //ringtone = RingtoneManager.getRingtone(getApplicationContext(), contentUri); //create ringtone
        //ringtone.setAudioAttributes(new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).build()); //activate alarm mode
        Log.d("CHOICE_SONG", "STEP 7");
        if (cursor != null) {
            cursor.close();
        }

        launchVibrator();
        launchAlarm();
    }

    private void setDefaultRingtone() {
        Uri uri = Uri.parse("android.resource://" + getPackageName() + "/raw/rooster");
        ringtone = RingtoneManager.getRingtone(getApplicationContext(), uri); //create ringtone
        ringtone.setAudioAttributes(new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).build()); //activate alarm mode
        ringtone.setVolume(1);
        ringtone.setLooping(true);
    }

    private void setRandomRingtone(long id) {
        Uri contentUri = ContentUris.withAppendedId(android.provider.MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
        ringtone = RingtoneManager.getRingtone(getApplicationContext(), contentUri); //create ringtone
        ringtone.setAudioAttributes(new AudioAttributes.Builder().setUsage(AudioAttributes.USAGE_ALARM).build()); //activate alarm mode
        ringtone.setVolume(1);
        ringtone.setLooping(true);
    }

    private void launchVibrator() {
        vib = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        VibrationEffect vibEffect = VibrationEffect.createWaveform(new long[]{1000,1000},
                                                   new int[] {VibrationEffect.DEFAULT_AMPLITUDE, 0},
                                                   0); //amplitude from 1 to 255
        vib.vibrate(vibEffect);
    }

    private void launchAlarm() {
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (ringtone.isPlaying())
                    interruptAlarm();
            }
        }, 1000*alarmDuration);

        ringtone.play();
    }

    public void interruptAlarm() { //TODO : arrange the button so that the activity stops
        ringtone.stop();
        vib.cancel();
    }

    // Used by the stop_alarm button
    public void terminateAlarm(View view) { //TODO : arrange the button so that the activity stops
        interruptAlarm();
        MainActivity.isAlarmOn[alarmIdx] = false;
        finish();
    }
}