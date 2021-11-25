package com.example.uziv2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class DisplayTimeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_time);

        // final Context context = this;

        // Get the Intent that started this activity and extract the string
        Intent intent = getIntent();
        // String message = intent.getStringExtra(MainActivity.EXTRA_MESSAGE);
        //final boolean[] alarmOff = {true};

        // Capture the layout's TextView and set the string as its text
        TextView textView = findViewById(R.id.textView);
        /*if (Character.getNumericValue(strDate.charAt(strDate.length()-1)) == 5) {
            textView.setText(strDate);
        }*/

        //Update Thread
        Timer timer = new Timer();

        Runnable r = new Runnable() {
            public void run(){
                //Display date
                Date date = Calendar.getInstance().getTime();
                DateFormat dateFormatDisp = new SimpleDateFormat("HH:mm:ss");
                DateFormat dateFormatAlarm = new SimpleDateFormat("HH:mm");
                String strDateDisp = dateFormatDisp.format(date);
                textView.setText(strDateDisp);
            }
        };

        TimerTask task = new TimerTask() {
            public void run() {
                Handler mHandler = new Handler(Looper.getMainLooper());
                mHandler.post(r);
            }
        };

        timer.scheduleAtFixedRate(task, 0, 1000);

    }
}


/*
public class Clock extends LinearLayout {

    private Calendar mCalendar;
    private LinearLayout mLayoutTime;
    private TextView mAMPMText;
    private TextView mDateText;
    private TextView mTimeText;
    private View mSendFeedback;
    private boolean mAttached;

    private final Handler mHandler = new Handler();
    SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, MMM d, yyyy");
    SimpleDateFormat timeFormatter = new SimpleDateFormat("h:mm");

    public Clock(final Context context, int layoutResourceID, int dateResId, int meResId,int amPmResId) {
        super(context);
        LayoutInflater layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(layoutResourceID, null);
        addView(view, new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
        mAMPMText = (TextView) view.findViewById(amPmResId);
        mTimeText = (TextView) view.findViewById(timeResId);
        mDateText = (TextView) view.findViewById(dateResId);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if (!mAttached) {
            mAttached = true;
            IntentFilter filter = new IntentFilter();

            filter.addAction(Intent.ACTION_TIME_TICK);
            filter.addAction(Intent.ACTION_TIME_CHANGED);
            filter.addAction(Intent.ACTION_TIMEZONE_CHANGED);

            getContext().registerReceiver(mIntentReceiver, filter, null, mHandler);
        }

        // NOTE: It's safe to do these after registering the receiver since the receiver always runs
        // in the main thread, therefore the receiver can't run before this method returns.

        // The time zone may have changed while the receiver wasn't registered, so update the Time
        mCalendar = Calendar.getInstance();

        // Make sure we update to the current time
        onTimeChanged();
        updateView();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (mAttached) {
            getContext().unregisterReceiver(mIntentReceiver);
            mAttached = false;
        }
    }

    private void updateView(){
        mTimeText.setText(timeFormatter.format(mCalendar.getTime()));
        mDateText.setText(dateFormatter.format(mCalendar.getTime()));
        mAMPMText.setText(mCalendar.get(Calendar.AM_PM) == 0 ? "AM" : "PM");
    }

    private void onTimeChanged() {
        mCalendar.setTime(new Date());

        updateContentDescription(mCalendar);
    }

    private final BroadcastReceiver mIntentReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(Intent.ACTION_TIMEZONE_CHANGED)) {
                String tz = intent.getStringExtra("time-zone");
                mCalendar.setTimeZone(TimeZone.getTimeZone(tz));
            }

            onTimeChanged();

            updateView();
        }
    };

    private void updateContentDescription(Calendar calendar) {
        setContentDescription(calendar.toString());
    }
}
 */

/*
Timer t = new Timer();
timer = (TextView)findViewById(R.id.timer);

t.scheduleAtFixedRate(new TimerTask()
{
    @Override
    public void run() {
        Calendar c = Calendar.getInstance();
        final String finalTime = String.format(Locale.US, "%d:%d:%d %d/%d/%d", c.get(Calendar.HOUR), c.get(Calendar.MINUTE), c.get(Calendar.SECOND), c.get(Calendar.DAY_OF_MONTH), c.get(Calendar.MONTH), c.get(Calendar.YEAR));

        runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    timer.setText(finalTime);
                }
            });
    }

}, 1000, 1000); //Initial Delay and Period for update (in milliseconds)

 */