package com.KP.simonicv2.Radar;

import org.altbeacon.beacon.Region;

import java.util.List;

public class Constant {
    public static final String RESTART_INTENT = "com.p2et.beacon";
    public static String UUID = "10000009-2020-0005-0001-100000000009";
    public static String UUIDmajor = "0";
    public static String UUIDminor = "0";
    //public static final Region regionbackground = new Region("backgroundRegion", Identifier.parse(UUID), null, null);
    public static final Region regionbackground = new Region("backgroundRegion", null, null, null);
    public static boolean alarmbeacon = false;
    public static String[] settingRadius = new String[]{
            "5m",
            "10m",
            "15m",
            "20m"
    };
    public static int[] nilaisettingRadius = new int[]{
            5,
            10,
            15,
            20
    };
    public static int radiusposisi = 3;
    public static int RADAR_RADIS_VISION_METERS = nilaisettingRadius[radiusposisi];
    public static String[] settingInterval = new String[]{
            "Continuous",
            "1s",
            "3s",
            "5s",
            "7s",
            "9s"
    };
    public static int[] nilaisettingInterval = new int[]{
            0,
            1*1000,
            3*1000,
            5*1000,
            7*1000,
            9*1000
    };
    public static int Intervalposisi = 1;
   ///server pusat
    public static String URLserver = "http://203.160.128.160:5055";;//"http://203.160.128.82:5055";
    //public static boolean statuspermission = false;
    public static String credentials = "mochamad.mardi.martadinata@gmail.com:Rahasia001!";//"monitoring.covid19@lipi.go.id:19cov20Lipi20"; //"daysdk63@gmail.com:awan2005";//
    public static String ULRservergetpostdevices = "http://203.160.128.160:8082/api/devices";//"http://203.160.128.82:8082/api/devices";
    //end
    public static List<String> serverDevicesList;
    public static String prevname ="ODP";
    //public static boolean statusserverdevices = false;
    public static int NotificationID = 205254;
    public static String CHANNEL_ID = "cov19";
    public static String CHANNEL_ID2 = "cov-19";
    public static String CHANNEL_NAME = "COV-19";
    public static String CHANNEL_NAME2 = "BeCOV-19";
    public static String CHANNEL_DESCRIPTION = "Services COVID-19";
    public static String CHANNEL_DESCRIPTION2 = "Monitoring COVID-19";
    public static String NotificationTitle = "BeCOV-19";
    public static String NotificationTxt = "Scanning for ODP/PDP";
    public static String NotificationTxtbeacon = "Starting BeCOV-19 Services";
    public static String iv = "20dcID98cov42020";
    public static String SecretKey = "19cov20ID9120512";
    public static Integer LocationInterval = 10000;
    public static Integer LocationFastestInterval = Constant.LocationInterval/2;
    public static String keyshared = "covid19";
    public static String keysharedinterval = "covid19interval";
    public static String keysharedradius = "covid19radius";
    public static boolean statusserver = false;
    public static String errorserver = "BeCOV-19: Server not response";
}
