package com.KP.simonicv2.Radar;

import android.os.Parcel;
import android.os.Parcelable;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.utils.UrlBeaconUrlCompressor;

public class DetectedBeacon extends Beacon {
    protected long mLastSeen;
    public static final int TYPE_EDDYSTONE_TLM = 32;
    public static final int TYPE_EDDYSTONE_UID = 0;
    public static final int TYPE_EDDYSTONE_URL = 16;
    public static final int TYPE_IBEACON_ALTBEACON = 1;

    public static final Parcelable.Creator<DetectedBeacon> CREATOR =
            new Parcelable.Creator<DetectedBeacon>() {
                @Override
                public DetectedBeacon createFromParcel(Parcel in) {
                    Beacon b = Beacon.CREATOR.createFromParcel(in);
                    DetectedBeacon dbeacon = new DetectedBeacon(b);
                    dbeacon.mLastSeen = in.readLong();
                    return dbeacon;
                }

                @Override
                public DetectedBeacon[] newArray(int size) {
                    return new DetectedBeacon[size];
                }
            };

    public DetectedBeacon(Beacon paramBeacon) {
        super(paramBeacon);
    }

    @Override
    public void writeToParcel(Parcel parcel, int flags) {
        super.writeToParcel(parcel, flags);
        parcel.writeLong(mLastSeen);
    }

    public String getId() {
        return getUUID() + ";" + getMajor() + ";" + getMinor() + ";FF:FF:FF:FF:FF:FF"; // ";" + getBluetoothAddress();
    }

    public int getType() {
        return getBeaconTypeCode();
    }

    public String getUUID() {
        return getId1().toString();
    }

    public boolean isEddystone() {
        return (getBeaconTypeCode() == TYPE_EDDYSTONE_UID)
                || (getBeaconTypeCode() == TYPE_EDDYSTONE_URL) || (getBeaconTypeCode() == TYPE_EDDYSTONE_TLM);
    }

    public String getMajor() {
        if (isEddystone()) {
            return getId2().toHexString();
        }
        return getId2().toString();
    }

    public String getMinor() {
        return getId3().toString();
    }


    public String getEddystoneURL() {
        return UrlBeaconUrlCompressor.uncompress(getId1().toByteArray());
    }

    public long getTimeLastSeen() {
        return this.mLastSeen;
    }

    public void setTimeLastSeen(long lastSeen) {
        this.mLastSeen = lastSeen;
    }

}
