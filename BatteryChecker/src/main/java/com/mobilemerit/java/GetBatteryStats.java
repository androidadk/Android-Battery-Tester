package com.mobilemerit.java;

import android.content.Intent;
import android.os.BatteryManager;

public class GetBatteryStats {

	Intent batteryInfoIntent;
	public GetBatteryStats(Intent intent) {
		// TODO Auto-generated constructor stub
		this.batteryInfoIntent=intent;
	}
	public String getBatteryChargingState(){
		String chargingstate=null;
		int chargingMode=batteryInfoIntent.getIntExtra(BatteryManager.EXTRA_PLUGGED, -1);
		if(chargingMode==0){
			chargingstate="Not Charging";
		}else if(chargingMode==BatteryManager.BATTERY_PLUGGED_AC){
			chargingstate="Charing with AC Source";
		}else if(chargingMode==BatteryManager.BATTERY_PLUGGED_USB){
			chargingstate="Charing with USB";
		}
		return chargingstate;
	}
	public String getBatteryTempreture(){
		return ""+(float)(batteryInfoIntent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,-1)/10.0f)+(char) 0x00B0+ "C";
	}
	public String getBatteryVoltage(){
		
		return ""+batteryInfoIntent.getIntExtra(BatteryManager.EXTRA_VOLTAGE,-1)+"mV";
	}
	public String getBatteryHealth(){
		String currentBatteryHealth=null;
		int health=batteryInfoIntent.getIntExtra(BatteryManager.EXTRA_HEALTH,-1);
		if(health==BatteryManager.BATTERY_HEALTH_COLD){
			currentBatteryHealth="Cold";
		}else if(health==BatteryManager.BATTERY_HEALTH_GOOD){
			currentBatteryHealth="Good";
		}else if(health==BatteryManager.BATTERY_HEALTH_DEAD){
			currentBatteryHealth="Dead";
		}else if(health==BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE){
			currentBatteryHealth="Over Voltage";
		}else if(health==BatteryManager.BATTERY_HEALTH_OVERHEAT){
			currentBatteryHealth="Over Heat";
		}else{
			currentBatteryHealth="Unspecified";
		}
		return currentBatteryHealth;
	}
	public String getBatteryTechnology(){
		return batteryInfoIntent.getStringExtra(BatteryManager.EXTRA_TECHNOLOGY);
	}
}
