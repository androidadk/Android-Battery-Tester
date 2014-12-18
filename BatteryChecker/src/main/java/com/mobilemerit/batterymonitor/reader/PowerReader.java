
package com.mobilemerit.batterymonitor.reader;

import java.io.File;

import android.os.Build;

/**
 * 
 * @author GuoLin
 */
public abstract class PowerReader {

    protected static final String TAG = "BatteryChecker.Reader";

    abstract protected Long getValue(File file);

    /**
     * Read current battery.
     * @return Power in mAh
     */
    public static Long read() {
        for (Device device : Device.values()) {
            Long value = device.getValue();
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    /**
     * Device to read.
     * 
     * @author GuoLin
     *
     */
    private static enum Device {

        /**
         * HTC Wildfire S.
         */
        HTC_WILDFIRE_S("wildfire s", "/sys/class/power_supply/battery/smem_text", new AttributeTextReader("eval_current")),

        /**
         * HTC Trimuph with CM7.
         */
        HTC_TRIMUPH_CM7("triumph", "/sys/class/power_supply/battery/current_now", new OneLineTextReader(false)),

        /**
         * HTC Desire HD.
         */
        HTC_DESIRE_HD("desire hd", "/sys/class/power_supply/battery/batt_current", new OneLineTextReader(false)),

        /**
         * HTC Desire Z.
         */
        HTC_DESIRE_Z("desire z", "/sys/class/power_supply/battery/batt_current", new OneLineTextReader(false)),

        /**
         * HTC Inspire 4G.
         */
        HTC_INSPIRE("inspire", "/sys/class/power_supply/battery/batt_current", new OneLineTextReader(false)),

        /**
         * HTC EVO View 4G Tablet.
         */
        HTC_EVO_VIEW_4G("pg41200", "/sys/class/power_supply/battery/batt_current", new OneLineTextReader(false)),

        /**
         * Nexus One with CM.
         */
        NEXUS_ONE_CM("/sys/devices/platform/ds2784-battery/getcurrent", new OneLineTextReader(true)),

        /**
         * Sony Ericsson Xperia X1.
         */
        SE_X1("/sys/devices/platform/i2c-adapter/i2c-0/0-0036/power_supply/ds2746-battery/current_now", new OneLineTextReader(false)),

        /**
         * XDAndroid.
         */
        XD_ANDROID("/sys/devices/platform/i2c-adapter/i2c-0/0-0036/power_supply/battery/current_now", new OneLineTextReader(false)),

        /**
         * HTC Droid Eris
         */
        HTC_DROID_ERIS("/sys/class/power_supply/battery/smem_text", new AttributeTextReader("I_MBAT")),

        /**
         * HTC Sensation / EVO 3D.
         */
        HTC_SENSATION("/sys/class/power_supply/battery/batt_attr_text", new AttributeTextReader("batt_discharge_current")),

        /**
         * Some HTC devices.
         */
        HTC_OTHER1("/sys/class/power_supply/battery/batt_current", new OneLineTextReader(false)),
        
        /**
         * Nexus One.
         */
        NEXUS_ONE("/sys/class/power_supply/battery/current_now", new OneLineTextReader(true)),

        /**
         * Samsung Galaxy Vibrant.
         */
        SAMSUNG_GALAXY_VIBRANT("/sys/class/power_supply/battery/batt_chg_current", new OneLineTextReader(false)),

        /**
         * Sony Ericsson X10.
         */
        SE_X10("/sys/class/power_supply/battery/charger_current", new OneLineTextReader(false)),

        /**
         * Nook Color.
         */
        NOOK_COLOR("/sys/class/power_supply/max17042-0/current_now", new OneLineTextReader(false)),

        /**
         * Sony Ericsson Xperia Arc.
         */
        SE_XPERIA_ARC("/sys/class/power_supply/bq27520/current_now", new OneLineTextReader(false)),

        /**
         * Motorola Atrix.
         */
        MOTOROLA_ATRIX("/sys/devices/platform/cpcap_battery/power_supply/usb/current_now", new OneLineTextReader(false)),

        /**
         * Acer Iconia Tab A500.
         */
        ACER_ICONIA_TAB_A500("/sys/EcControl/BatCurrent", new OneLineTextReader(false)),

        /**
         * Samsung Note, charge current only.
         */
        SAMSUNG_NOTE("/sys/class/power_supply/battery/batt_current_now", new OneLineTextReader(false));

        final String[] models;

        final String filepath;

        final PowerReader reader;

        Device(String filepath, PowerReader reader) {
            this((String) null, filepath, reader);
        }

        Device(String model, String filepath, PowerReader reader) {
            this.models = model != null ? new String[] { model } : null;
            this.filepath = filepath;
            this.reader = reader;
        }

        Device(String[] models, String filepath, PowerReader reader) {
            this.models = models;
            this.filepath = filepath;
            this.reader = reader;
        }
        
        Long getValue() {
            if (!matchModel()) {
                return null;
            }
            File file = new File(filepath);
            //Log.v(TAG, "************************** " + Build.MODEL + " [" + file.exists() + "] " + filepath);
            if (!file.exists()) {
                return null;
            }
            Long value = reader.getValue(file);
            return value;
        }

        private boolean matchModel() {
            if (models == null) {
                return true;
            }
            String current = Build.MODEL.toLowerCase();
            for (String model : models) {
                if (current.contains(model)) {
                    return true;
                }
            }
            return false;
        }

    }

}
