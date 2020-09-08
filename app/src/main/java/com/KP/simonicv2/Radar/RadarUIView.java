package com.KP.simonicv2.Radar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.DrawableRes;
import androidx.core.content.ContextCompat;
import androidx.core.content.res.ResourcesCompat;

import com.KP.simonicv2.R;

import org.altbeacon.beacon.Beacon;

import java.text.DecimalFormat;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.KP.simonicv2.Radar.Constant.RADAR_RADIS_VISION_METERS;

public class RadarUIView extends View implements SensorEventListener {
    private static String mMetricDisplayFormat = "%.0fm";
    private static String mEnglishDisplayFormat = "%.0fft";
    private static float METER_PER_FEET = 0.3048f;
    private static float FEET_PER_METER = 3.28084f;

    private float mDistanceRatio = 1.0f;
    private float[] mLastAccelerometer = new float[3];
    private float[] mLastMagnetometer = new float[3];
    private boolean mLastAccelerometerSet = false;
    private boolean mLastMagnetometerSet = false;
    private float[] mOrientation = new float[3];
    private AngleFilter angleFilter = new AngleFilter();
    private float mLast_bearing;
    private Context mContext;
    private WindowManager mWindowManager;
    private Map<String, DetectedBeacon> mBeacons = new LinkedHashMap();
    private boolean mHaveDetected = false;
    //private TextView mInfoView;
    private Rect mTextBounds = new Rect();
    private Paint mGridPaint;
    private Paint mErasePaint;
    private Bitmap mBlip, mBlip1, mBlip2, mBlip3, mBlip4, mBlip5;
    private boolean mUseMetric;
    /**
     * Used to draw the animated ring that sweeps out from the center
     */
    private Paint mSweepPaint0;
    /**
     * Used to draw the animated ring that sweeps out from the center
     */
    private Paint mSweepPaint1;
    /**
     * Used to draw the animated ring that sweeps out from the center
     */
    private Paint mSweepPaint2;
    /**
     * Used to draw a beacon
     */
    private Paint mBeaconPaint;
    /**
     * Time in millis when the most recent sweep began
     */
    private long mSweepTime;
    /**
     * True if the sweep has not yet intersected the blip
     */
    private boolean mSweepBefore;
    /**
     * Time in millis when the sweep last crossed the blip
     */
    private long mBlipTime;

    public RadarUIView(Context context) {
        this(context, null);

    }

    public RadarUIView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public RadarUIView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        mContext = context;
        mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);

        // Paint used for the rings and ring text
        mGridPaint = new Paint();
        mGridPaint.setColor(getResources().getColor(R.color.primary_light));

        mGridPaint.setAntiAlias(true);
        mGridPaint.setStyle(Paint.Style.STROKE);
        mGridPaint.setStrokeWidth(2.0f);
        mGridPaint.setTextSize(16.0f);
        mGridPaint.setTextAlign(Paint.Align.CENTER);

        // Paint used to erase the rectangle behind the ring text
        mErasePaint = new Paint();
        mErasePaint.setColor(getResources().getColor(R.color.primary));
        //mErasePaint.setColor(getResources().getColor(R.color.hn_orange_lighter));

        mErasePaint.setStyle(Paint.Style.FILL);

        mBeaconPaint = new Paint();
        mBeaconPaint.setColor(getResources().getColor(R.color.primary));
        mBeaconPaint.setAntiAlias(true);
        mBeaconPaint.setStyle(Paint.Style.FILL_AND_STROKE);

        // Outer ring of the sweep
        mSweepPaint0 = new Paint();
        mSweepPaint0.setColor(ContextCompat.getColor(context, R.color.accent));
        mSweepPaint0.setAntiAlias(true);
        mSweepPaint0.setStyle(Paint.Style.STROKE);
        mSweepPaint0.setStrokeWidth(3f);

        // Middle ring of the sweep
        mSweepPaint1 = new Paint();
        mSweepPaint1.setColor(ContextCompat.getColor(context, R.color.accent));

        mSweepPaint1.setAntiAlias(true);
        mSweepPaint1.setStyle(Paint.Style.STROKE);
        mSweepPaint1.setStrokeWidth(3f);

        // Inner ring of the sweep
        mSweepPaint2 = new Paint();
        mSweepPaint2.setColor(ContextCompat.getColor(context, R.color.accent));

        mSweepPaint2.setAntiAlias(true);
        mSweepPaint2.setStyle(Paint.Style.STROKE);
        mSweepPaint2.setStrokeWidth(3f);

        //mBlip = BitmapFactory.decodeResource(context.getResources(),R.drawable.ic_location_on_red_24dp);
        //mBlip = ((BitmapDrawable) ResourcesCompat.getDrawable(context.getResources(), R.drawable.ic_location_on_red_24dp, null)).getBitmap();
        mBlip = convertbitmap(context, R.drawable.ic_baseline_location_on_red_24);
        mBlip1 = convertbitmap(context, R.drawable.ic_baseline_location_on_biru_24);
        mBlip2 = convertbitmap(context, R.drawable.ic_baseline_location_on_pink_24);
        mBlip3 = convertbitmap(context, R.drawable.ic_baseline_location_on_hijau_24);
        mBlip4 = convertbitmap(context, R.drawable.ic_baseline_location_on_kuning_24);
        mBlip5 = convertbitmap(context, R.drawable.ic_baseline_location_on_orange_24);
    }

    private Bitmap convertbitmap(Context context, @DrawableRes int vectorDrawableResourceId) {
        Drawable vectorDrawable = ResourcesCompat.getDrawable(context.getResources(), vectorDrawableResourceId, null);
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.setBounds(0, 0, canvas.getWidth(), canvas.getHeight());
        vectorDrawable.draw(canvas);
        return bitmap;
    }

    /**
     * Sets the view that we will use to report distance
     *
     * @param t The text view used to report distance
     */
    public void setDistanceView(TextView t) {
        //mInfoView = t;
    }



    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int center = getWidth() / 2;
        int radius = center - 8;

        // Draw the rings
        final Paint gridPaint = mGridPaint;
        canvas.drawCircle(center, center, radius, gridPaint);
        canvas.drawCircle(center, center, radius * 3 / 4, gridPaint);
        canvas.drawCircle(center, center, radius >> 1, gridPaint);
        canvas.drawCircle(center, center, radius >> 2, gridPaint);

        int blipRadius = (int) (mDistanceRatio * radius);

        final long now = SystemClock.uptimeMillis();
        if (mSweepTime > 0) {
            // Draw the sweep. Radius is determined by how long ago it started
            long sweepDifference = now - mSweepTime;
            if (sweepDifference < 512L) {
                int sweepRadius = (int) (((radius + 6) * sweepDifference) >> 9);
                canvas.drawCircle(center, center, sweepRadius, mSweepPaint0);
                canvas.drawCircle(center, center, sweepRadius - 2, mSweepPaint1);
                canvas.drawCircle(center, center, sweepRadius - 4, mSweepPaint2);

                // Note when the sweep has passed the blip
                boolean before = sweepRadius < blipRadius;
                if (!before && mSweepBefore) {
                    mSweepBefore = false;
                    mBlipTime = now;
                }
            } else {
                mSweepTime = now + 1000;
                mSweepBefore = true;
            }
            postInvalidate();
        }

        // Draw horizontal and vertical lines
        canvas.drawLine(center, center - (radius >> 2) + 6, center, center - radius - 6, gridPaint);
        canvas.drawLine(center, center + (radius >> 2) - 6, center, center + radius + 6, gridPaint);
        canvas.drawLine(center - (radius >> 2) + 6, center, center - radius - 6, center, gridPaint);
        canvas.drawLine(center + (radius >> 2) - 6, center, center + radius + 6, center, gridPaint);

        // Draw X in the center of the screen
        canvas.drawLine(center - 4, center - 4, center + 4, center + 4, gridPaint);
        canvas.drawLine(center - 4, center + 4, center + 4, center - 4, gridPaint);

        if (mHaveDetected) {
            //mHaveDetected = false;
            // Draw the blip. Alpha is based on how long ago the sweep crossed the blip
            long blipDifference = now - mBlipTime;
            gridPaint.setAlpha(255 - (int) ((128 * blipDifference) >> 10));

            double bearingToTarget = mLast_bearing;
            double drawingAngle = Math.toRadians(bearingToTarget) - (Math.PI / 2);
            float cos = (float) Math.cos(drawingAngle);
            float sin = (float) Math.sin(drawingAngle);

            addText(canvas, getRatioDistanceText(0.25f), center, center + (radius >> 2));
            addText(canvas, getRatioDistanceText(0.5f), center, center + (radius >> 1));
            addText(canvas, getRatioDistanceText(0.75f), center, center + radius * 3 / 4);
            addText(canvas, getRatioDistanceText(1.0f), center, center + radius);

            int ind = 0;
            for (Map.Entry<String, DetectedBeacon> entry : mBeacons.entrySet()) {
                //String key = entry.getKey();
                DetectedBeacon dBeacon = entry.getValue();
                //System.out.println("value: " + dBeacon);

                // drawing the beacon
                if (((System.currentTimeMillis() - dBeacon.getTimeLastSeen()) / 1000 < 2)) {
                    if(dBeacon.getDistance() < RADAR_RADIS_VISION_METERS) {
                        if(ind < 6) {
                            if(ind==0) {
                                canvas.drawBitmap(mBlip, center + (cos * distanceToPix(dBeacon.getDistance())) - 8,
                                        center + (sin * distanceToPix(dBeacon.getDistance())) - 8, gridPaint);
                            }else if(ind==1) {
                                canvas.drawBitmap(mBlip1, center + (cos * distanceToPix(dBeacon.getDistance())) - 8,
                                        center + (sin * distanceToPix(dBeacon.getDistance())) - 8, gridPaint);
                            }else if(ind==2) {
                                canvas.drawBitmap(mBlip2, center + (cos * distanceToPix(dBeacon.getDistance())) - 8,
                                        center + (sin * distanceToPix(dBeacon.getDistance())) - 8, gridPaint);
                            }else if(ind==3) {
                                canvas.drawBitmap(mBlip3, center + (cos * distanceToPix(dBeacon.getDistance())) - 8,
                                        center + (sin * distanceToPix(dBeacon.getDistance())) - 8, gridPaint);
                            }else if(ind==4) {
                                canvas.drawBitmap(mBlip4, center + (cos * distanceToPix(dBeacon.getDistance())) - 8,
                                        center + (sin * distanceToPix(dBeacon.getDistance())) - 8, gridPaint);
                            }else if(ind==5) {
                                canvas.drawBitmap(mBlip5, center + (cos * distanceToPix(dBeacon.getDistance())) - 8,
                                        center + (sin * distanceToPix(dBeacon.getDistance())) - 8, gridPaint);
                            }

                        }else {
                            canvas.drawBitmap(mBlip, center + (cos * distanceToPix(dBeacon.getDistance())) - 8,
                                    center + (sin * distanceToPix(dBeacon.getDistance())) - 8, gridPaint);
                        }
                        canvas.drawText("RSSI: "+String.valueOf(dBeacon.getRssi()),center + (cos * distanceToPix(dBeacon.getDistance())) - 8,
                                center + (sin * distanceToPix(dBeacon.getDistance())) - 8,gridPaint);
                        ind++;
                    }
                }
            }

            gridPaint.setAlpha(255);
        }
    }

    private String getRatioDistanceText(float ringRation) {
        return new DecimalFormat("##0.00").format(RADAR_RADIS_VISION_METERS * mDistanceRatio * ringRation);
    }

    /**
     * max radar range is 20 meters
     *
     * @param distance
     * @return distance in px
     */
    private float distanceToPix(double distance) {
        int center = getWidth() / 2;
        int radius = center - 8;
        return Math.round((radius * distance) / RADAR_RADIS_VISION_METERS * mDistanceRatio);
    }

    private void addText(Canvas canvas, String str, int x, int y) {
        mGridPaint.getTextBounds(str, 0, str.length(), mTextBounds);
        mTextBounds.offset(x - (mTextBounds.width() >> 1), y);
        mTextBounds.inset(-2, -2);
        canvas.drawRect(mTextBounds, mErasePaint);
        canvas.drawText(str, x, y, mGridPaint);
    }


    /**
     * Update state to reflect whether we are using metric or standard units.
     *
     * @param useMetric True if the display should use metric units
     */
    public void setUseMetric(boolean useMetric) {
        mUseMetric = useMetric;
        if (mHaveDetected) {
            // TODO
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        calcBearing(event);
    }

    private synchronized void calcBearing(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            System.arraycopy(event.values, 0, mLastAccelerometer, 0, event.values.length);
            mLastAccelerometerSet = true;
        } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            System.arraycopy(event.values, 0, mLastMagnetometer, 0, event.values.length);
            mLastMagnetometerSet = true;
        }
        if (mLastAccelerometerSet && mLastMagnetometerSet) {

            /* Create rotation Matrix */
            float[] rotationMatrix = new float[9];
            if (SensorManager.getRotationMatrix(rotationMatrix, null,
                    mLastAccelerometer, mLastMagnetometer)) {
                SensorManager.getOrientation(rotationMatrix, mOrientation);

                float azimuthInRadians = mOrientation[0];

                angleFilter.add(azimuthInRadians);

                mLast_bearing = (float) (Math.toDegrees(angleFilter.average()) + 360) % 360;

                postInvalidate();

                //Log.d(Constants.TAG, "orientation bearing: " + mLast_bearing);

            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }



    private void insertBeacons(Collection<Beacon> beacons) {
        Iterator<Beacon> iterator = beacons.iterator();
        while (iterator.hasNext()) {
            DetectedBeacon dBeacon = new DetectedBeacon(iterator.next());
            dBeacon.setTimeLastSeen(System.currentTimeMillis());
            this.mBeacons.put(dBeacon.getId(), dBeacon);
        }
    }



    public void onDetectedBeacons(final Collection<Beacon> beacons) {

        insertBeacons(beacons);

        updateDistances();

        updateBeaconsInfo(beacons);

        invalidate();
    }

    private void updateBeaconsInfo(final Collection<Beacon> beacons) {
        //mInfoView.setText(String.format(getResources().getString(R.string.text_scanner_found_beacons_size), beacons.size()));
    }


    /**
     * update on radar
     */
    private void updateDistances() {
        if (!mHaveDetected) {
            mHaveDetected = true;
        }
    }

    private void updateDistanceText(double distanceM) {
        String displayDistance;
        if (mUseMetric) {
            displayDistance = String.format(mMetricDisplayFormat, distanceM);
        } else {
            displayDistance = String.format(mEnglishDisplayFormat, distanceM * FEET_PER_METER);
        }
        //mInfoView.setText(displayDistance);
    }

    /**
     * Turn on the sweep animation starting with the next draw
     */
    public void startSweep() {
        //mInfoView.setText(R.string.text_scanning);
        mSweepTime = SystemClock.uptimeMillis();
        mSweepBefore = true;
    }

    /**
     * Turn off the sweep animation
     */
    public void stopSweep() {
        mSweepTime = 0L;
        //mInfoView.setText("");
    }

}