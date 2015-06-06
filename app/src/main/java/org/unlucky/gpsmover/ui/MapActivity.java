package org.unlucky.gpsmover.ui;

import android.app.ActivityManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.unlucky.gpsmover.service.GPSMoverService;
import org.unlucky.gpsmover.R;
import org.unlucky.gpsmover.model.FavoriteLocation;
import org.unlucky.gpsmover.model.FavoritesHelper;

import java.sql.SQLException;
import java.util.Map;

public class MapActivity extends BaseActivity
        implements View.OnClickListener, GotoLocationDialogFragment.GotoLocationDialogListener,
        AddLocationDialogFragment.AddLocationDialogListener,
        FavLocationDialogFragment.FavLocationDialogListener,
        GoogleMap.OnMarkerDragListener {
    private static final int REQ_SETTINGS = 1;

    private boolean isServiceBind = false;
    private int UPDATE_INTERVAL_TIME = 1000;// 1s
    private float current_zoomLevel = 1.0f;

    private LatLng current_location;
    private GoogleMap mMap;
    private MarkerOptions markerOpt = new MarkerOptions();
    private Marker marker;
    private GPSMoverService gpsMoverService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        gpsMoverService = null;
        initSettings(true);
        initMap();
        //initUI();
    }

    @Override
    protected void onPause() {
        storeSettings();
        super.onPause();
    }

    @Override
    protected int getSelfNavDrawerItem() {
        return NAVDRAWER_ITEM_MAP;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQ_SETTINGS) {
            initSettings(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent();
            intent.setClass(this, SettingsActivity.class);
            startActivityForResult(intent, REQ_SETTINGS);
            return true;
        } else if (id == R.id.action_add_location) {
            AddLocationDialogFragment dialog = new AddLocationDialogFragment();
            dialog.show(getSupportFragmentManager(), AddLocationDialogFragment.class.getName());
            return true;
        } else if (id == R.id.action_goto_location) {
            GotoLocationDialogFragment dialog = new GotoLocationDialogFragment();
            dialog.show(getSupportFragmentManager(), GotoLocationDialogFragment.class.getName());
            return true;
        } else if (id == R.id.action_about) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        /*switch (v.getId()) {
            case R.id.start_btn:
                Intent intent = new Intent();
                intent.setClass(this, GPSMoverService.class);
                intent.putExtra("longitude", current_location.longitude);
                intent.putExtra("latitude", current_location.latitude);
                startService(intent);
                bindService(intent, conn, BIND_AUTO_CREATE);
                Toast.makeText(this, "Start Fake Location", Toast.LENGTH_SHORT).show();
                break;
            case R.id.stop_btn:
                handler.removeCallbacks(updateLocationThread);
                if (isServiceBind) {
                    unbindService(conn);
                    isServiceBind = false;
                }
                if (isServiceRunning(GPSMoverService.class.getName())) {
                    Intent intent1 = new Intent();
                    intent1.setClass(this, GPSMoverService.class);
                    stopService(intent1);
                    Toast.makeText(this, "Stop Fake Location", Toast.LENGTH_SHORT).show();
                }
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
                FavLocationDialogFragment dialog = new FavLocationDialogFragment();
                dialog.show(getSupportFragmentManager(), FavLocationDialogFragment.class.getName());
                break;
            default:
                break;
        }*/

    }

    /**
     * initialize UI in activity
     */
    /*private void initUI() {
        final Button start_btn = (Button)findViewById(R.id.start_btn);
        start_btn.setOnClickListener(this);
        final Button stop_btn = (Button)findViewById(R.id.stop_btn);
        stop_btn.setOnClickListener(this);
        final Button zoom_in_btn = (Button)findViewById(R.id.zoom_in_btn);
        zoom_in_btn.setOnClickListener(this);
        final Button zoom_out_btn = (Button)findViewById(R.id.zoom_out_btn);
        zoom_out_btn.setOnClickListener(this);
        final ImageButton mode_btn = (ImageButton)findViewById(R.id.mode_btn);
        mode_btn.setOnClickListener(this);
        final ImageButton search_btn = (ImageButton)findViewById(R.id.search_btn);
        search_btn.setOnClickListener(this);
        final ImageButton history_btn = (ImageButton)findViewById(R.id.history_btn);
        history_btn.setOnClickListener(this);
        final ImageButton fav_btn = (ImageButton)findViewById(R.id.fav_btn);
        fav_btn.setOnClickListener(this);
    }*/

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
        mMap.setOnMarkerDragListener(this);

        // init a marker
        markerOpt.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE));
        marker = mMap.addMarker(markerOpt.position(current_location)
                .title(getString(R.string.map_marker_title))
                .snippet(String.format(getString(R.string.map_marker_snippet),
                        current_location.latitude, current_location.longitude)).draggable(true));

        mMap.moveCamera(CameraUpdateFactory.newLatLng(current_location));
    }

    /**
     * store last known location and zoom level
     */
    private void storeSettings() {
        SharedPreferences.Editor editor = getPreferences(MODE_PRIVATE).edit();
        editor.putString("latitude", String.valueOf(current_location.latitude));
        editor.putString("longitude", String.valueOf(current_location.longitude));
        editor.putString("zoom_level", String.valueOf(current_zoomLevel));
        editor.commit();
    }

    /**
     * restore related settings
     * @param isLaunch whether it is launching the app
     */
    private void initSettings(boolean isLaunch) {
        if (isLaunch) {
            SharedPreferences sp = getPreferences(MODE_PRIVATE);
            double lat = Double.valueOf(sp.getString("latitude", "0.0"));
            double lng = Double.valueOf(sp.getString("longitude", "0.0"));
            current_location = new LatLng(lat, lng);
            current_zoomLevel = Float.valueOf(sp.getString("zoom_level", "1.0f"));
        }
        SharedPreferences sp = getSharedPreferences("org.unlucky.gpsmover.app_preferences",
                MODE_PRIVATE);
        UPDATE_INTERVAL_TIME = Integer.valueOf(sp.getString("perf_key_update_interval", "1000"));
    }

    ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            gpsMoverService = ((GPSMoverService.MyBinder)service).getService();
            isServiceBind = true;
            handler.post(updateLocationThread);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            gpsMoverService = null;
            isServiceBind = false;
        }
    };

    Handler handler = new Handler();

    Runnable updateLocationThread = new Runnable() {
        @Override
        public void run() {
            LatLng prev = current_location;
            if (gpsMoverService != null) {
                current_location = gpsMoverService.getCurrentLatLng();
            }
            // calc distance
            Location a = new Location("prev");
            a.setLatitude(prev.latitude);
            a.setLongitude(prev.longitude);
            Location b = new Location("current");
            b.setLatitude(current_location.latitude);
            b.setLongitude(current_location.longitude);

            updateMapMarker(current_location, false, false);
            handler.postDelayed(updateLocationThread, UPDATE_INTERVAL_TIME);
        }
    };

    private void updateMapMarker(LatLng pos, boolean needZoom, boolean isDraggable) {
        marker.setPosition(pos);
        marker.setTitle(getString(R.string.map_marker_title));
        marker.setSnippet(String.format(getString(R.string.map_marker_snippet),
                pos.latitude, pos.longitude));
        marker.setDraggable(isDraggable);
        mMap.moveCamera(CameraUpdateFactory.newLatLng(pos));
        if (needZoom) {
            mMap.animateCamera(CameraUpdateFactory.zoomTo(current_zoomLevel));
        }
    }

    /**
     * check whether the service is running
     * @param name name of service
     * @return true - service is running<br/>
     *         false - service is not running
     */
    private boolean isServiceRunning(String name) {
        ActivityManager manager = (ActivityManager)getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (service.service.getClassName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        String tag = dialog.getTag();
        if (AddLocationDialogFragment.class.getName().equals(tag)) {
            String title = ((AddLocationDialogFragment)dialog).getEditText();
            try {
                FavoritesHelper.getInstance(this).open().insertFavoriteLocation(
                        new FavoriteLocation(title, current_location.latitude,
                                current_location.longitude, mMap.getCameraPosition().zoom));
            } catch (SQLException e) {
                e.printStackTrace();
            }
            dialog.dismiss();
        } else if (GotoLocationDialogFragment.class.getName().equals(tag)) {
            String[] text_array = ((GotoLocationDialogFragment)dialog).getEditText().split(",");
            if (text_array.length == 2) {
                try {
                    double lat = Double.valueOf(text_array[0]);
                    double lng = Double.valueOf(text_array[1]);
                    current_location = new LatLng(lat, lng);
                    updateMapMarker(current_location, false, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Toast.makeText(this, getString(R.string.error_format), Toast.LENGTH_SHORT).show();
            }
            dialog.dismiss();
        }
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {
        dialog.dismiss();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Map<String, Object> selectedItem = (Map<String, Object>)parent.getItemAtPosition(position);
        FavoriteLocation selectedFavorite = (FavoriteLocation)selectedItem.get("favorite");
        double lat = selectedFavorite.getLatitude();
        double lng = selectedFavorite.getLongitude();
        current_location = new LatLng(lat, lng);
        current_zoomLevel = selectedFavorite.getZoomLevel();
        updateMapMarker(current_location, true, true);
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {
        LatLng mLocation = marker.getPosition();
        marker.setSnippet(String.format(getString(R.string.map_marker_snippet),
                mLocation.latitude, mLocation.longitude));
        marker.showInfoWindow();
    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        current_location = marker.getPosition();
        updateMapMarker(current_location, false, true);
        marker.hideInfoWindow();
    }
}
