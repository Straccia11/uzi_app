package com.example.uziv2;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Hashtable;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PageFragment extends Fragment {

    // 1 - Create keys for our Bundle
    private static final String KEY_POSITION="position";
    private Hashtable<Integer, Integer> idxToId;
    //private boolean notInitialized = true;
    // private static final String KEY_COLOR="color";
    private int alarmSelected = -1;



    public PageFragment() {
        idxToId = new Hashtable<Integer, Integer>();
        idxToId.put(R.id.switch1, 0);
        idxToId.put(R.id.switch2, 1);
        idxToId.put(R.id.switch3, 2);
        idxToId.put(R.id.switch4, 3);
    }


    // 2 - Method that will create a new instance of PageFragment, and add data to its bundle.
    public static PageFragment newInstance(int position) {

        // 2.1 Create new fragment
        PageFragment frag = new PageFragment();

        // 2.2 Create bundle and add it some data
        Bundle args = new Bundle();
        args.putInt(KEY_POSITION, position);
        // args.putInt(KEY_COLOR, color);
        frag.setArguments(args);

        return(frag);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // 3 - Get layout of PageFragment
        // View result = inflater.inflate(R.layout.fragment_page, container, false);

        // 4 - Get widgets from layout and serialise it
        // LinearLayout rootView= (LinearLayout) result.findViewById(R.id.fragment_page_rootview);
        // TextView textView= (TextView) result.findViewById(R.id.fragment_page_title);

        // 5 - Get data from Bundle (created in method newInstance)
        int position = getArguments().getInt(KEY_POSITION, -1);
        // int color = getArguments().getInt(KEY_COLOR, -1);

        View result;
        if (position == 1) {
            result = inflater.inflate(R.layout.fragment_alarms, container, false);
            Intent intent = new Intent(getContext().getApplicationContext(), AlarmService.class);

            // - TODO
            //TextView alarmName1 = result.findViewById(R.id.name_alarm1);
            //EditText alarmEdit1 = result.findViewById(R.id.edit_name_alarm1);
            View dummyView = result.findViewById(R.id.dummy_view);

            /*
            alarmName1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    view.setVisibility(View.GONE);
                    alarmEdit1.setVisibility(View.VISIBLE);
                    alarmEdit1.requestFocus();
                    dummyView.setVisibility(View.VISIBLE);
                    InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(alarmEdit1, InputMethodManager.SHOW_IMPLICIT);
                }
            });
            */

            dummyView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    getActivity().getCurrentFocus().clearFocus();
                }
            });

            /*
            alarmEdit1.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {
                    // When focus is lost check that the text field has valid values.
                    if (!hasFocus) {
                        alarmEdit1.setVisibility(View.GONE);
                        dummyView.setVisibility(View.GONE);
                        alarmName1.setVisibility(View.VISIBLE);
                        alarmName1.setText(alarmEdit1.getText().toString());
                        alarmEdit1.getText().clear();

                        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(alarmEdit1.getWindowToken(), 0);
                    }
                }
            });
            */

            //
            //result.findViewById(R.id.select_screen).bringToFront();

            /*initSwitch(result.findViewById(R.id.switch1), intent);

            View alarm1 = result.findViewById(R.id.time_alarm1);
            alarm1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    result.findViewById(R.id.select_layout).setVisibility(View.VISIBLE);

                }
            });




            initSwitch(result.findViewById(R.id.switch2), intent);
            initSwitch(result.findViewById(R.id.switch3), intent);
            initSwitch(result.findViewById(R.id.switch4), intent);*/

            for (int i=1; i <= MainActivity.N_ALARMS_MAX; i++) {
                // Get the id for each view
                int idTimeView = result.getResources().getIdentifier("time_alarm" + i, "id", result.getContext().getPackageName());
                int idNameView = result.getResources().getIdentifier("name_alarm" + i, "id", result.getContext().getPackageName());
                int idSwitch = result.getResources().getIdentifier("switch" + i, "id", result.getContext().getPackageName());
                int idEditView = result.getResources().getIdentifier("edit_name_alarm" + i, "id", result.getContext().getPackageName());
                TextView tw = result.findViewById(idTimeView);
                TextView nw = result.findViewById(idNameView);
                EditText ew = result.findViewById(idEditView);
                Switch sw = result.findViewById(idSwitch);


                if (i > MainActivity.nAlarms) {
                    ((View) tw.getParent()).setVisibility(View.GONE);
                }



                // Time display
                tw.setText(String.format(Locale.FRANCE,"%02d:%02d", MainActivity.timeOfAlarm[i-1][0], MainActivity.timeOfAlarm[i-1][0]));
                tw.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        result.findViewById(R.id.select_layout).setVisibility(View.VISIBLE);
                        alarmSelected = idxToId.get(sw.getId());
                    }
                });

                // Name display
                nw.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        view.setVisibility(View.GONE);
                        ew.setVisibility(View.VISIBLE);
                        ew.requestFocus();
                        dummyView.setVisibility(View.VISIBLE);
                        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.showSoftInput(ew, InputMethodManager.SHOW_IMPLICIT);
                    }
                });

                // Name Edit
                ew.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        // When focus is lost check that the text field has valid values.
                        if (!hasFocus) {
                            ew.setVisibility(View.GONE);
                            dummyView.setVisibility(View.GONE);
                            nw.setVisibility(View.VISIBLE);
                            nw.setText(ew.getText().toString());
                            ew.getText().clear();

                            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(ew.getWindowToken(), 0);
                        }
                    }
                });

                // Switch
                initSwitch(sw, intent);
            }


            TimePicker tp = result.findViewById(R.id.time_picker);
            tp.setIs24HourView(true);

            View sel_view = result.findViewById(R.id.select_view);
            sel_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    result.findViewById(R.id.select_layout).setVisibility(View.GONE);
                    if (alarmSelected >= 0) {
                        int hour = tp.getHour();
                        int min = tp.getMinute();
                        int idTimeView = result.getResources().getIdentifier("time_alarm" + (alarmSelected+1), "id", result.getContext().getPackageName());
                        TextView timeAlarm = result.findViewById(idTimeView);
                        timeAlarm.setText(String.format(Locale.FRANCE,"%02d:%02d", hour, min));

                        MainActivity.timeOfAlarm[alarmSelected][0] = hour;
                        MainActivity.timeOfAlarm[alarmSelected][1] = min;
                    }
                    alarmSelected = -1;

                }
            });


            if (MainActivity.nAlarms == 1) {
                //result.findViewById(R.id.alarm2).setVisibility(View.GONE);
                //result.findViewById(R.id.alarm3).setVisibility(View.GONE);
                //result.findViewById(R.id.alarm4).setVisibility(View.GONE);
                result.findViewById(R.id.button_rm).setVisibility(View.GONE);
            //} else if (MainActivity.nAlarms == 2) {
            //    result.findViewById(R.id.alarm3).setVisibility(View.GONE);
            //    result.findViewById(R.id.alarm4).setVisibility(View.GONE);
            //} else if (MainActivity.nAlarms == 3) {
            //    result.findViewById(R.id.alarm4).setVisibility(View.GONE);
            } else if (MainActivity.nAlarms == 4) {
                result.findViewById(R.id.button_add).setVisibility(View.GONE);
            }


            //result.findViewById(R.id.switch2).setVisibility(View.GONE);
            //result.findViewById(R.id.switch3).setVisibility(View.GONE);
            //result.findViewById(R.id.switch4).setVisibility(View.GONE);
            //result.findViewById(R.id.button_rm).setVisibility(View.GONE);
            //result.findViewById(R.id.button_add).setVisibility(View.GONE);

        } else if (position == 2) {
            result = inflater.inflate(R.layout.fragment_time_select, container, false);
        } else {
            result = inflater.inflate(R.layout.fragment_settings, container, false);
        }


        // 6 - Update widgets with it
        // rootView.setBackgroundColor(color);
        // textView.setText("Page numéro "+position);

        // Log.e(getClass().getSimpleName(), "onCreateView called for fragment number "+position);

        return result;
    }

    public void initSwitch(Switch sw, Intent intent) { // TODO : change global variables to local
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int idx = idxToId.get(sw.getId());
                if (isChecked) {
                    // The toggle is enabled
                    // EditText editText = findViewById(R.id.editTextTextPersonName);
                    if (!MainActivity.isAlarmOn[idx]) {
                        MainActivity.isAlarmOn[idx] = true;

                        //String message = +":"+MainActivity.timeOfAlarm[idx][1]; //editText.getText().toString();
                        String message = String.format(Locale.FRANCE,"%02d:%02d", MainActivity.timeOfAlarm[idx][0], MainActivity.timeOfAlarm[idx][1]);
                        intent.putExtra("alarmTime", message);
                        intent.putExtra("alarmIdx", idxToId.get(sw.getId()));
                        getContext().startForegroundService(intent);
                        Toast.makeText(getContext().getApplicationContext(), "Alarm activated", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // The toggle is disabled
                    getContext().stopService(intent);
                    //MainActivity.isAlarmOn[idxToId.get(sw.getId())] = false;
                    if (MainActivity.isAlarmOn[idx]) {
                        MainActivity.isAlarmOn[idx] = false;
                        Toast.makeText(getContext().getApplicationContext(), "Alarm deactivated", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        if (MainActivity.isAlarmOn[idxToId.get(sw.getId())]) {
            sw.setChecked(true);
        }

        /*TextView timeView = (TextView) ((LinearLayout) sw.getParent()).getChildAt(0);
        timeView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.getRootView().findViewById(R.id.select_layout).setVisibility(View.VISIBLE);

                TimePicker tp = (TimePicker) view.getRootView().findViewById(R.id.time_picker);
                tp.setIs24HourView(true);

                tp.setOnTimeChangedListener(new TimePicker.OnTimeChangedListener() {
                    @Override
                    public void onTimeChanged(TimePicker tpView, int hourOfDay, int minute) {
                        ((TextView) view).setText(hourOfDay + ":" + minute);
                    }
                });
            }
        });*/
    }
}