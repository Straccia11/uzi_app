package com.example.uziv2;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.material.switchmaterial.SwitchMaterial;

public class MainActivity extends AppCompatActivity {

    public static final String EXTRA_MESSAGE = "com.example.uziv2.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Switch activity
        Intent intent = new Intent(getApplicationContext(), AlarmService.class);
        Switch sw = (Switch) findViewById(R.id.switch1);
        sw.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    // The toggle is enabled
                    EditText editText = (EditText) findViewById(R.id.editTextTextPersonName);
                    String message = editText.getText().toString();
                    intent.putExtra(EXTRA_MESSAGE, message);
                    startForegroundService(intent);
                    Toast.makeText(getApplicationContext(), "Alarm activated",Toast.LENGTH_SHORT).show();
                } else {
                    // The toggle is disabled
                    stopService(intent);
                    Toast.makeText(getApplicationContext(), "Alarm deactivated",Toast.LENGTH_SHORT).show();
                }
            }
        });
        requestExtStorage();
    }

    private void requestExtStorage() {
        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Explain to the user why we need to read the contacts
                Toast.makeText(getApplicationContext(), "Don't ask questions",Toast.LENGTH_SHORT).show();
            }
            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
        }
    }

    /** Called when the user taps the Send button */
    public void sendMessage(View view) {
        Intent intent = new Intent(this, AlarmActivity.class);
        EditText editText = (EditText) findViewById(R.id.editTextTextPersonName);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }

    /** Called when the user taps the Show Clock button */
    public void dispClock(View view) {
        Intent intent = new Intent(this, DisplayTimeActivity.class);
        startActivity(intent);
    }
}