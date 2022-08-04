package com.example.uziv2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import java.util.Hashtable;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PageFragment extends Fragment {

    // 1 - Create keys for our Bundle
    private static final String KEY_POSITION="position";
    private Hashtable<Integer, Integer> idxToId;
    private boolean notInitialized = true;
    // private static final String KEY_COLOR="color";
    @SuppressLint("UseSwitchCompatOrMaterialCode")



    public PageFragment() { }


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

            if (notInitialized) { // TODO : make it work
                initSwitch(result.findViewById(R.id.switch1), intent);
                initSwitch(result.findViewById(R.id.switch2), intent);
                initSwitch(result.findViewById(R.id.switch3), intent);
                initSwitch(result.findViewById(R.id.switch4), intent);

                result.findViewById(R.id.switch2).setVisibility(View.GONE);
                result.findViewById(R.id.switch3).setVisibility(View.GONE);
                result.findViewById(R.id.switch4).setVisibility(View.GONE);
                result.findViewById(R.id.button_rm).setVisibility(View.GONE);

                idxToId = new Hashtable<Integer, Integer>();
                idxToId.put(R.id.switch1, 0);
                idxToId.put(R.id.switch2, 1);
                idxToId.put(R.id.switch3, 2);
                idxToId.put(R.id.switch4, 3);

                notInitialized = false ;
            }


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
                if (isChecked) {
                    // The toggle is enabled
                    // EditText editText = findViewById(R.id.editTextTextPersonName);
                    String message = "03:32"; //editText.getText().toString();
                    intent.putExtra("alarmTime", message);
                    intent.putExtra("alarmIdx", idxToId.get(sw.getId()));
                    getContext().startForegroundService(intent);
                    MainActivity.isAlarmOn[idxToId.get(sw.getId())] = true;
                    Toast.makeText(getContext().getApplicationContext(), "Alarm activated",Toast.LENGTH_SHORT).show();
                } else {
                    // The toggle is disabled
                    getContext().stopService(intent);
                    //MainActivity.isAlarmOn[idxToId.get(sw.getId())] = false;
                    Toast.makeText(getContext().getApplicationContext(), "Alarm deactivated",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}