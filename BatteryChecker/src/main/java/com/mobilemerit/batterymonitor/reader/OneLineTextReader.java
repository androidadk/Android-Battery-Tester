
package com.mobilemerit.batterymonitor.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import android.util.Log;

/**
 */
class OneLineTextReader extends PowerReader {

    private boolean convertToMillis;

    OneLineTextReader(boolean convertToMillis) {
        this.convertToMillis = convertToMillis;
    }

    @Override
    public Long getValue(File file) {
        String text = null;

        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));
            text = reader.readLine();
        } catch (Exception e) {
            Log.e(TAG, "Read the first line in file " + file + " failed.", e);
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // Let it go
                }
            }
        }
        if (text == null) {
            return null;
        }

        try {
            long value = Long.parseLong(text);
            return convertToMillis ? value / 1000 : value;
        } catch (NumberFormatException e) {
            Log.e(TAG, "Parse text '" + text + "' to long failed.", e);
            return null;
        }
    }

}
