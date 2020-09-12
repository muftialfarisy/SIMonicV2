package com.KP.simonicv2.Radar;

import android.location.Location;
import android.net.Uri;

public class ProtocolFormatter {
    public static String formatRequest(String uuid, String url, Location location, double batt, String alarm) {
        Uri serverUrl = Uri.parse(url);
        Uri.Builder builder = serverUrl.buildUpon()
                .appendQueryParameter("id", uuid)
                .appendQueryParameter("timestamp", String.valueOf(location.getTime() / 1000))
                .appendQueryParameter("lat", String.valueOf(location.getLatitude()))
                .appendQueryParameter("lon", String.valueOf(location.getLongitude()))
                .appendQueryParameter("speed", String.valueOf(location.getSpeed()))
                .appendQueryParameter("bearing", String.valueOf(location.getBearing()))
                .appendQueryParameter("altitude", String.valueOf(location.getAltitude()))
                .appendQueryParameter("accuracy", String.valueOf(location.getAccuracy()))
                .appendQueryParameter("batt", String.valueOf(batt));

        if (location.isFromMockProvider()) {
            builder.appendQueryParameter("mock", String.valueOf(location.isFromMockProvider()));
        }

        if (alarm != null) {
            builder.appendQueryParameter("alarm", alarm);
        }

        return builder.build().toString();
    }
}
