package com.pendla.googlecalendaralarm.clock;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pendla.googlecalendaralarm.R;

import org.joda.time.DateTime;
import org.joda.time.Duration;
import org.joda.time.Period;
import org.joda.time.format.DateTimeFormatterBuilder;
import org.joda.time.format.PeriodFormatter;
import org.joda.time.format.PeriodFormatterBuilder;

/**
 * Created by Simon on 2014-11-19.
 */
public class ClockView extends Activity {

    private ClockPresenter clockPresenter;

    private Button skipEventButton;
    private Button syncAlarmButton;
    private TextView clockText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);

        clockPresenter = new ClockPresenter(this);

        clockText = (TextView) findViewById(R.id.clock_text);

        skipEventButton = (Button) findViewById(R.id.skip_button);
        skipEventButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clockPresenter.skipEventClicked();
            }
        });

        syncAlarmButton = (Button) findViewById(R.id.sync_button);
        syncAlarmButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clockPresenter.syncAlarmClicked();
            }
        });
    }

    public void setClockTime(String time){
        clockText.setText(time);
    }
}
