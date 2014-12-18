package com.mobilemerit.batterychecker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.ResolveInfo;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.BatteryManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.os.*;

import com.google.analytics.tracking.android.GoogleAnalytics;
import com.google.analytics.tracking.android.Tracker;

import com.mobilemerit.java.GetBatteryStats;
import com.mobilemerit.java.StartBatteryTestService;

public class BatteryCheck extends Activity {

	private TextView bLevel, header, health, voltage, tech, temp,uptime,uptime_tag,
	heath_tag,temp_tag,tech_tag,voltage_tag;
	private ProgressBar bProgress;
	private ImageButton cheakUsage;
	private int level;
	private Intent usageIntent;
	private ResolveInfo resolveInfo;
	private Typeface myTypeface,secondrayTypeFace;
	private Tracker tracker;
	private GoogleAnalytics gTracker;
	//private AdView adView;
	private ImageButton batteryTester;
	
	public static final String LEVEL="level";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.battery_check_new);
		
		/**
         * feeding the context of thw application 
         * So as to use this at various places
         * 
         * */
        new App(getApplicationContext());
		
		// Attaching the Font type with the Text Views
				myTypeface = Typeface.createFromAsset(getAssets(), "fonts/Franklin Gothic.otf");
				secondrayTypeFace=Typeface.createFromAsset(getAssets(),"fonts/GOTHIC.TTF" );
		/**
		 * Google Analytic code
		 */
		gTracker = GoogleAnalytics.getInstance(getApplicationContext());
		tracker = gTracker.getTracker("UA-41735784-1");

		// Finding the XML Views

		bLevel = (TextView) findViewById(R.id.blevel1);
		bProgress = (ProgressBar) findViewById(R.id.progressBar);
		cheakUsage = (ImageButton) findViewById(R.id.usage);
		header = (TextView) findViewById(R.id.header);
		tech = (TextView) findViewById(R.id.technology);
		temp = (TextView) findViewById(R.id.tempreture);
		voltage = (TextView) findViewById(R.id.voltage);
		health = (TextView) findViewById(R.id.health);
		uptime = (TextView) findViewById(R.id.uptime);
		
		batteryTester=(ImageButton)findViewById(R.id.battery_test);
		
		batteryTester.setOnClickListener(new OnClickListener() {			
			@Override
			public void onClick(View v) {
				startService(new Intent(BatteryCheck.this,StartBatteryTestService.class));
			}
		} );
		
		/**find up the tags view(of stats textviews)  as well to set up the typeface */
		heath_tag=(TextView)this.findViewById(R.id.health_tag);
		tech_tag=(TextView)this.findViewById(R.id.technology_tag);
		temp_tag=(TextView)this.findViewById(R.id.temp_tag);
		voltage_tag=(TextView)this.findViewById(R.id.voltage_tag);
		uptime_tag=(TextView)this.findViewById(R.id.uptime_tag);
		
		heath_tag.setTypeface(secondrayTypeFace);
		tech_tag.setTypeface(secondrayTypeFace);
		temp_tag.setTypeface(secondrayTypeFace);
		voltage_tag.setTypeface(secondrayTypeFace);
		uptime_tag.setTypeface(secondrayTypeFace);
		/** Code for Google Admob */

		//adView = (AdView) findViewById(R.id.adView);
		//AdRequest request = new AdRequest.Builder().build();
		//adView.loadAd(request);

		
		bLevel.setTypeface(myTypeface);
		header.setTypeface(myTypeface);
		// register the Broadcast Receiver
		this.registerReceiver(this.batteryInfoReceiver, new IntentFilter(
				Intent.ACTION_BATTERY_CHANGED));
		// Setting up the Intent for the Power Usage details
		usageIntent = new Intent(Intent.ACTION_POWER_USAGE_SUMMARY);
		// Check it for presence
		resolveInfo = getPackageManager().resolveActivity(usageIntent, 0);

		if (resolveInfo == null) {
			Toast.makeText(this, "Not Support!", Toast.LENGTH_LONG).show();
			cheakUsage.setEnabled(false);
		} else {
			cheakUsage.setEnabled(true);
		}

		// Setting up the functionality of the Button Clicks
		cheakUsage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub

				startActivity(usageIntent);

			}
		});

	}

	private BroadcastReceiver batteryInfoReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
			setUpStats(intent);
			// Toast.makeText(getBaseContext(),
			// ""+intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,-1),Toast.LENGTH_SHORT).show();
			new ShowProgressClass().execute();
		}
	};

	public void setUpStats(Intent intent) {
		GetBatteryStats stats = new GetBatteryStats(intent);
		health.setText("" + stats.getBatteryHealth());
		tech.setText("" + stats.getBatteryTechnology());
		temp.setText("" + stats.getBatteryTempreture());
		voltage.setText("" + stats.getBatteryVoltage());
		uptime.setText("" + Float.valueOf(((SystemClock.elapsedRealtime()/1000)/60)/60)+" Hrs");
		//Setting up typefaces for the stats 
		health.setTypeface(secondrayTypeFace);
		tech.setTypeface(secondrayTypeFace);
		temp.setTypeface(secondrayTypeFace);
		voltage.setTypeface(secondrayTypeFace);
		uptime.setTypeface(secondrayTypeFace);
		/**Save current voltage and level to calculate the Remaining time on Battery*/
		
		SharedPreferences prefs=this.getSharedPreferences(LEVEL, Context.MODE_PRIVATE);
		prefs.edit().putString("level",""+level);
		prefs.edit().putString("voltage",stats.getBatteryVoltage());
		
		
	}

	
	// For Upadating progress bar After loading
	class ShowProgressClass extends AsyncTask<Void, Integer, Void> {

		@Override
		protected Void doInBackground(Void... params) {

			for (int i = 0; i < level; i++) {
				publishProgress(1);
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		protected void onPreExecute() {			
			bProgress.setMax(100);
			bProgress.setProgress(0);
			
			// TODO Auto-generated method stub

		}
		@Override
		protected void onProgressUpdate(Integer... values) {
			bProgress.incrementProgressBy(values[0]);
			bLevel.setText(" " + bProgress.getProgress() + "%" );
			// TODO Auto-generated method stub
		}
		
	}

	@Override
	public boolean onKeyDown(int keycode, KeyEvent event) {
		if (keycode == KeyEvent.KEYCODE_BACK) {

			AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

			alertDialog.setTitle("Alert");

			// Setting Dialog Message
			alertDialog.setMessage("Are you sure you want to quit?");
			// Setting Positive "Yes" Button
			alertDialog.setPositiveButton("YES",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							onDestroy();
						}
					});
			// Setting Negative "NO" Button
			alertDialog.setNegativeButton("NO",
					new DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							// Write your code here to invoke NO event
							dialog.cancel();
						}
					});
			// Showing Alert Message
			alertDialog.show();
		}
		return super.onKeyDown(keycode, event);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		android.os.Process.killProcess(android.os.Process.myPid());
	}

}
