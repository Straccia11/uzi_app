package com.example.uziv2;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.uziv2.MESSAGE";
    public static final int N_ALARMS_MAX = 4;
    public static boolean[] isAlarmOn;
    private ViewPager pager ;
    private int nAlarms = 1;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        isAlarmOn = new boolean[N_ALARMS_MAX] ; // by default all values are false

        requestPerm();
        this.configureViewPager();

        Intent intent = new Intent(getApplicationContext(), AlarmService.class);
    }

    @Override
    protected void onRestart() { // TODO : actualize the switch when the alarm is set off + register alarms even after closing the app
        super.onRestart();
        Switch sw1 = findViewById(R.id.switch1);
        if (sw1 != null) {  sw1.setChecked(isAlarmOn[0]); }
        Switch sw2 = findViewById(R.id.switch2);
        if (sw2 != null) {  sw2.setChecked(isAlarmOn[1]); }
        Switch sw3 = findViewById(R.id.switch3);
        if (sw3 != null) {  sw3.setChecked(isAlarmOn[2]); }
        Switch sw4 = findViewById(R.id.switch4);
        if (sw4 != null) {  sw4.setChecked(isAlarmOn[3]); }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Switch sw1 = findViewById(R.id.switch1);
        if (sw1 != null) {  sw1.setChecked(isAlarmOn[0]); }
        Switch sw2 = findViewById(R.id.switch2);
        if (sw2 != null) {  sw2.setChecked(isAlarmOn[1]); }
        Switch sw3 = findViewById(R.id.switch3);
        if (sw3 != null) {  sw3.setChecked(isAlarmOn[2]); }
        Switch sw4 = findViewById(R.id.switch4);
        if (sw4 != null) {  sw4.setChecked(isAlarmOn[3]); }
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
            ConstraintLayout alarm_view = (ConstraintLayout) view.getParent();
            int idView = alarm_view.getResources().getIdentifier("switch" + nAlarms, "id", alarm_view.getContext().getPackageName());
            //Switch sw2 = alarm_view.findViewById(R.id.switch2);
            Switch sw = alarm_view.findViewById(idView);
            sw.setVisibility(View.VISIBLE);

            alarm_view.findViewById(R.id.button_rm).setVisibility(View.VISIBLE);
        }

        if (nAlarms == N_ALARMS_MAX) {
            view.setVisibility(View.GONE);
        }
    }

    /** Called when the user taps the - button */
    public void rmAlarm(View view) {
        if (nAlarms > 1) {
            ConstraintLayout alarm_view = (ConstraintLayout) view.getParent();
            int idView = alarm_view.getResources().getIdentifier("switch" + nAlarms, "id", alarm_view.getContext().getPackageName());
            Switch sw = alarm_view.findViewById(idView);
            sw.setVisibility(View.GONE);
            nAlarms--;

            alarm_view.findViewById(R.id.button_add).setVisibility(View.VISIBLE);
        }

        if (nAlarms == 1) {
            view.setVisibility(View.GONE);
        }
    }
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
