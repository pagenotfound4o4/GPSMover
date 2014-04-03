package org.unlucky.gpsmover.app;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends FragmentActivity
        implements View.OnClickListener {
    private Button start_btn, stop_btn;
    private Button zoom_in_btn, zoom_out_btn;
    private ImageButton mode_btn, search_btn, history_btn, fav_btn;

    private GoogleMap mMap;
    private GoogleMapOptions options = new GoogleMapOptions();
    private MarkerOptions marker = new MarkerOptions();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initMap();
        initUI();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_btn:
                Toast.makeText(this, "Start Fake Location", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.setClass(this, GPSMoverService.class);
                startService(intent);
                bindService(intent, conn, BIND_AUTO_CREATE);
                break;
            case R.id.stop_btn:
                Toast.makeText(this, "Stop Fake Location", Toast.LENGTH_SHORT).show();
                Intent intent1 = new Intent();
                intent1.setClass(this, GPSMoverService.class);
                unbindService(conn);
                stopService(intent1);
                break;
            case R.id.zoom_in_btn:
                mMap.animateCamera(CameraUpdateFactory.zoomIn());
                break;
            case R.id.zoom_out_btn:
                mMap.animateCamera(CameraUpdateFactory.zoomOut());
                break;
            case R.id.mode_btn:
                if (mMap.getMapType() == GoogleMap.MAP_TYPE_NORMAL) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
                } else if (mMap.getMapType() == GoogleMap.MAP_TYPE_HYBRID) {
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                } else {
                    mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                }
                break;
            case R.id.search_btn:
                break;
            case R.id.history_btn:
                break;
            case R.id.fav_btn:
                break;
            default:
                break;
        }

    }

    /**
     * initialize UI in activity
     */
    private void initUI() {
        start_btn = (Button)findViewById(R.id.start_btn);
        start_btn.setOnClickListener(this);
        stop_btn = (Button)findViewById(R.id.stop_btn);
        stop_btn.setOnClickListener(this);
        zoom_in_btn = (Button)findViewById(R.id.zoom_in_btn);
        zoom_in_btn.setOnClickListener(this);
        zoom_out_btn = (Button)findViewById(R.id.zoom_out_btn);
        zoom_out_btn.setOnClickListener(this);
        mode_btn = (ImageButton)findViewById(R.id.mode_btn);
        mode_btn.setOnClickListener(this);
        search_btn = (ImageButton)findViewById(R.id.search_btn);
        search_btn.setOnClickListener(this);
        history_btn = (ImageButton)findViewById(R.id.history_btn);
        history_btn.setOnClickListener(this);
        fav_btn = (ImageButton)findViewById(R.id.fav_btn);
        fav_btn.setOnClickListener(this);
    }

    /**
     * initialize map in activity
     */
    private void initMap() {
        mMap = ((SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.mapview)).getMap();
        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setCompassEnabled(false);
        mMap.getUiSettings().setRotateGesturesEnabled(false);
        mMap.getUiSettings().setTiltGesturesEnabled(false);

        // init a marker
        marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        LatLng pos = new LatLng(30, 120);
        mMap.addMarker(marker.position(pos).title("Unlucky"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(pos));
    }

    public int dpToPx(int dp) {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        int px = Math.round(dp * (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return px;
    }

    public int pxToDp(int px) {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        return dp;
    }

    ServiceConnection conn = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            GPSMoverService.MyBinder mBinder = (GPSMoverService.MyBinder)service;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
        }
    };

}
