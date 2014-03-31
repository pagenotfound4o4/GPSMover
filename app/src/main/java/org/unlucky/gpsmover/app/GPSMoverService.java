package org.unlucky.gpsmover.app;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.LocationManager;
import android.os.Binder;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;

import com.google.android.gms.maps.model.LatLng;

import org.unlucky.gpsmover.app.util.Common;

public class GPSMoverService extends Service
        implements SensorEventListener {
    private static final int UPDATE_INTERVAL_TIME = 5000;
    private static final int NOTIFICATION_ID = 0x0;
    private static final float GRAVITY = 9.8f;

    private SensorManager mSensorManager;
    private LatLng curPos, prevPos;
    private LocationManager mLocationManager;
    private NotificationManager mNotificationManager;

    public static GPSMoverService instance = null;

    public GPSMoverService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        mSensorManager = (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        mLocationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);

        mNotificationManager = (NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
        mNotificationManager.notify(NOTIFICATION_ID, createNotification());

        Common.log("service created!");
    }

    @Override
    public void onDestroy() {
        instance = null;
        mSensorManager.unregisterListener(this);
        mNotificationManager.cancel(NOTIFICATION_ID);
        Common.log("service destroyed!");
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_GRAVITY),
                SensorManager.SENSOR_DELAY_NORMAL);
        handler.post(updateGpsThread);
        Common.log("service started!");
        return Service.START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        MyBinder mBinder = new MyBinder();
        return mBinder;
    }

    /**
     * Create customized notification
     * @return customized notification
     */
    private Notification createNotification() {
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher))
                .setSmallIcon(R.drawable.ic_launcher)
                .setContentTitle(getResources().getString(R.string.app_name))
                .setContentText(String.format(getResources()
                        .getString(R.string.msg_fake_gps), 120.123456, 30.123456))
                .setAutoCancel(false);

        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0,
                intent, PendingIntent.FLAG_UPDATE_CURRENT);//can be clicked repeatedly

        Notification notification = mBuilder.setContentIntent(pendingIntent).build();
        notification.flags |= Notification.FLAG_NO_CLEAR;//don't clear notification

        return notification;
    }

    Handler handler = new Handler();

    Runnable updateGpsThread = new Runnable() {
        @Override
        public void run() {
            //TODO update location
            //Location location = new Location("GPSFaker");
            //location.setLatitude(curPos.latitude);
            //location.setLongitude(curPos.longitude);
            //location.setAltitude(0.0);
            //location.setTime(System.currentTimeMillis());
            //mLocationManager.setTestProviderLocation(LocationManager.GPS_PROVIDER, location);
            handler.postDelayed(updateGpsThread, UPDATE_INTERVAL_TIME);
        }
    };

    public class MyBinder extends Binder {
        /**
         * Get current location
         * @return current location
         */
        public LatLng getLocation() {
            return curPos;
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor.getType() == Sensor.TYPE_GRAVITY) {
            prevPos = curPos;
            float x = event.values[0];
            float y = event.values[1];
            float z = event.values[2];
            Common.log("X->" + x + " Y->" + y + " Z->" + z);
        }
    }
}
