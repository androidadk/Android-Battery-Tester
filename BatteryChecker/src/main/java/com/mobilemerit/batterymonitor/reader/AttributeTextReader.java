
package com.mobilemerit.batterymonitor.reader;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import android.util.Log;

/**
 * Reader for attributes text file.
 */
class AttributeTextReader extends PowerReader {

    private static final String DEFAULT_ATTRIBUTE = "batt_current:";

    private String dischargeField;

    AttributeTextReader(String dischargeField) {
        this.dischargeField = dischargeField;
    }

    @Override
    public Long getValue(File file) {
        BufferedReader reader = null;
        try {
            reader = new BufferedReader(new FileReader(file));

            String line;
            while ((line = reader.readLine()) != null) {
                Long value = getLongValue(line, DEFAULT_ATTRIBUTE);
                if (value != null) {
                    return value;
                }
                value = getLongValue(line, dischargeField + ":");
                if (value != null) {
                    return - value;
                }
            }
            return null;
        } catch (Exception e) {
            Log.e(TAG, "Parse value from file " + file + " failed.", e);
            return null;
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // Let it go
                }
            }
        }
    }

    private static Long getLongValue(String line, String attributeName) {
        int index = line.indexOf(attributeName);
        if (index < 0) {
            return null;
        }
        String text = line.substring(index + attributeName.length());
        try {
            return Long.parseLong(text);
        } catch (NumberFormatException e) {
            Log.e(TAG, "Parse text " + text + " failed.", e);
            return null;
        }
    }

}
