package com.mobilemerit.batterychecker;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

/**
 * Monitor battery usage.
 * 
 */
public class BatteryMonitorActivity extends Activity implements OnClickListener {

    private Button mStart;

    private Button mClear;

    private Button mRefresh;

    private TextView mStats;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        /**
         * feeding the context of thw application 
         * So as to use this at various places
         * 
         * */
        new App(getApplicationContext());
        
        mStart = (Button) findViewById(R.id.btn_start);
        mStart.setOnClickListener(this);
        mClear = (Button) findViewById(R.id.btn_clear);
        mClear.setOnClickListener(this);
        mRefresh = (Button) findViewById(R.id.btn_refresh);
        mRefresh.setOnClickListener(this);
        mStats = (TextView) findViewById(R.id.stats);
    }

    @Override
    public void onClick(View v) {
        if (v == mStart) {
            new Recorder("abc").save();
        }
        else if (v == mClear) {
            Recorder recorder = Recorder.get("abc");
            if (recorder != null) {
                recorder.remove();
            }
        }
        else if (v == mRefresh) {
            Recorder recorder = Recorder.get("abc");
            if (recorder == null) {
                return;
            }
            mStats.setText("duration: " + recorder.getDuration()
                    + "s\nconsumption: " + recorder.getConsumption()
                    + "mAh\naverage: " + (recorder.getAverageConsumption() * 60) + "mAh/min");
        }
    }

}
