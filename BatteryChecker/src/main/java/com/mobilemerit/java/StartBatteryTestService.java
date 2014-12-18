package com.mobilemerit.java;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import android.widget.Toast;

import com.mobilemerit.batterychecker.DialogActivity;
import com.mobilemerit.batterychecker.R;
import com.mobilemerit.batterychecker.Recorder;

public class StartBatteryTestService extends Service{

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}
	Runnable runnable=new Runnable() {
		
		@Override
		public void run() {
			// TODO Auto-generated method stub
			sendTestNotification();
		}
	};
	Handler testHandler=new Handler();
	
	@Override
	public void onCreate() {
		super.onCreate();
		/**start test count */
		new Recorder("abc").save();
		Toast.makeText(getBaseContext(), "You will be notified about the result", Toast.LENGTH_SHORT).show();
		testHandler.postDelayed(runnable, 10000);
	}
	public void sendTestNotification(){
		 Recorder recorder = Recorder.get("abc");
         if (recorder == null) {
             stopSelf();
         }
         this.sendTestResult(recorder);
       /*  Toast.makeText(getApplicationContext(), "duration: " + recorder.getDuration()
                 + "s\nconsumption: " + recorder.getConsumption()
                 + "mAh\naverage: " + (recorder.getAverageConsumption() * 60) + "mAh/min"
        		 , Toast.LENGTH_LONG).show();
         
         */
         Recorder recorderer = Recorder.get("abc");
         if (recorderer != null) {
             recorderer.remove();
         }
         stopSelf();
	}
	
	public String sendTestResult(Recorder _recorder){
		String  time=null;
		String remark=null;
		float drainageRate=_recorder.getAverageConsumption();
		if(drainageRate<0){
			remark="Power saving is optional";
		}else{
			remark="Power saving is recomanded";
		}
		String TITLE="Battery Drainage Result";
		String MSG="Battery is draining at the average rate of"
				+(_recorder.getAverageConsumption()*60)+"\n";
		this.sendnotification(TITLE, MSG,remark);
		
		return time;
	}
	
	@SuppressWarnings("deprecation")
	protected void sendnotification(String title, String message,String remark) {
		String ns = Context.NOTIFICATION_SERVICE;
		int NOTIFICATION_ID = 1;
		NotificationManager mNotificationManager = (NotificationManager) getSystemService(ns);
		int icon = R.drawable.ic_launcher;
		CharSequence tickerText = message;
		long when = System.currentTimeMillis();
		Notification notification = new Notification(icon, tickerText, when);
		Context context = StartBatteryTestService.this;
		CharSequence contentTitle = title;
		CharSequence contentText = message;
		
		Intent notificationIntent = new Intent(this, DialogActivity.class);
		notificationIntent.putExtra("status",message);
		notificationIntent.putExtra("remark", remark);
		
		PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
				notificationIntent, 0);
		notification.flags = Notification.FLAG_AUTO_CANCEL;
		notification.setLatestEventInfo(context, contentTitle, contentText,
				contentIntent);
		mNotificationManager.notify(NOTIFICATION_ID, notification);
	}
}
