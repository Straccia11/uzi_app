package com.example.uziv2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.uziv2.MESSAGE";
    public static final int N_ALARMS_MAX = 4;
    public static boolean[] isAlarmOn;
    public static int[][] timeOfAlarm;
    private ViewPager pager ;
    public static int nAlarms = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isAlarmOn = new boolean[N_ALARMS_MAX] ; // by default all values are false
        timeOfAlarm = new int[N_ALARMS_MAX][2] ;


        requestPerm();
        this.configureViewPager();

        Intent intent = new Intent(getApplicationContext(), AlarmService.class);
    }

    @Override
    protected void onRestart() { // TODO : actualize the switch when the alarm is set off + register alarms even after closing the app
        super.onRestart();
        /*for (int i = 0; i < nAlarms; i++) {
            int idView = getResources().getIdentifier("switch" + i, "id", getPackageName());
            Switch sw = findViewById(idView) ;
            if (sw != null) {
                sw.setVisibility(View.VISIBLE);
                sw.setChecked(isAlarmOn[i]);
            }
        }*/
    }

    @Override
    protected void onPause() {
        super.onPause();

        //Save state
        SharedPreferences.Editor editor = getSharedPreferences("alarms", MODE_PRIVATE).edit();
        editor.putInt("nAlarms", nAlarms);
        for (int i = 0; i < nAlarms; i++) {
            editor.putBoolean("sw"+(i+1), isAlarmOn[i]);
            editor.putInt("hourAlarm" + (i+1), timeOfAlarm[i][0]);
            editor.putInt("minuteAlarm" + (i+1), timeOfAlarm[i][1]);
        }
        editor.apply();
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences pref = getSharedPreferences("alarms", MODE_PRIVATE);
        int savedNAlarms = pref.getInt("nAlarms", 0);

        if (savedNAlarms > 0) {
            nAlarms = savedNAlarms;
            for (int i = 0; i < nAlarms; i++) {
                isAlarmOn[i] = pref.getBoolean("sw"+(i+1), false);
                timeOfAlarm[i][0] = pref.getInt("hourAlarm" + (i+1), 0);
                timeOfAlarm[i][1] = pref.getInt("minuteAlarm" + (i+1), 0);
            }
        }
    }


    private void requestPerm() {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            || checkSelfPermission(Manifest.permission.VIBRATE) != PackageManager.PERMISSION_GRANTED) {
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)
                || shouldShowRequestPermissionRationale(Manifest.permission.VIBRATE)) {
                // Explain to the user why we need to read the contacts
                Toast.makeText(getApplicationContext(), "Don't ask questions",Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.VIBRATE}, 1);
        }
    }

    private void configureViewPager(){
        // 1 - Get ViewPager from layout
        pager = (ViewPager)findViewById(R.id.pager);
        // 2 - Set Adapter PageAdapter and glue it together
        pager.setAdapter(new UziPageAdapter(getSupportFragmentManager()) {});
        pager.setCurrentItem(1);
    }

    /** Called when the user taps the Show Clock button */
    public void dispClock(View view) {
        Intent intent = new Intent(this, DisplayTimeActivity.class);
        startActivity(intent);
    }

    /** Called when the user taps the Settings button */
    public void dispSettings(View view) {
        setContentView(R.layout.fragment_settings);
    }

    /** Called when the user taps the Settings button */
    public void dispMain(View view) { pager.setCurrentItem(1); }

    /** Called when the user taps the + button */
    public void addAlarm(View view) {
        if (nAlarms < N_ALARMS_MAX) {
            nAlarms++;
            ConstraintLayout lay = (ConstraintLayout) view.getParent();
            //int idView = alarm_view.getResources().getIdentifier("switch" + nAlarms, "id", alarm_view.getContext().getPackageName());
            //Switch sw2 = alarm_view.findViewById(R.id.switch2);
            //Switch sw = alarm_view.findViewById(idView);
            //sw.setVisibility(View.VISIBLE);
            int idView = lay.getResources().getIdentifier("alarm" + nAlarms, "id", lay.getContext().getPackageName());
            View alarm = lay.findViewById(idView);
            alarm.setVisibility(View.VISIBLE);

            lay.findViewById(R.id.button_rm).setVisibility(View.VISIBLE);
        }

        if (nAlarms == N_ALARMS_MAX) {
            view.setVisibility(View.GONE);
        }
    }

    /** Called when the user taps the - button */
    public void rmAlarm(View view) {
        if (nAlarms > 1) {
            ConstraintLayout lay = (ConstraintLayout) view.getParent();
            //int idView = lay.getResources().getIdentifier("switch" + nAlarms, "id", lay.getContext().getPackageName());
            //Switch sw = lay.findViewById(idView);
            //sw.setVisibility(View.GONE);
            int idView = lay.getResources().getIdentifier("alarm" + nAlarms, "id", lay.getContext().getPackageName());
            View alarm = lay.findViewById(idView);
            alarm.setVisibility(View.GONE);
            nAlarms--;

            lay.findViewById(R.id.button_add).setVisibility(View.VISIBLE);
        }

        if (nAlarms == 1) {
            view.setVisibility(View.GONE);
        }
    }

    /*
    public void timeSelect(View view) {
        RelativeLayout rel_layout = (RelativeLayout) view.getParent().getParent();
        rel_layout.findViewById(R.id.select_screen).setVisibility(View.VISIBLE);
    }*/
}

/*
 * @Override
 * public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
 *     //------------< onViewCreated() >------------
 *     super.onViewCreated(view, savedInstanceState);
 *     //< init >
 *     this.fragmentView =  view ;
 *     //</ init >
 *
 *     //----< route switch events >----
 *     //*get onClick() evente from multiple views in fragment
 *     for (int iSwitch=0;iSwitch<7;iSwitch++)
 *     {
 *         int idView=getResources().getIdentifier("SwitchAlarm" + iSwitch,"id", getContext().getPackageName());
 *         View eventView =view.findViewById(idView);
 *         eventView.setOnClickListener(new View.OnClickListener() {
 *                                      @Override
 *                                      public void onClick(View view) {
 *                                          SwitchAlarmOnClick(view);
 *                                      }
 *                                  }
 *         );
 *     }
 *     //----</ route switch events >----
 */
