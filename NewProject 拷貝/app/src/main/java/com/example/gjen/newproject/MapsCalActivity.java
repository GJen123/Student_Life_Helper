package com.example.gjen.newproject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsCalActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    public double Long,Lat;
    Location currentLocation;
    Marker currentMarker,itemMarker,lastMarker;
    LatLng currentLatLng;
    Double lastLat,lastLong;
    String user_id,weekly;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps_cal);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Bundle bundle = this.getIntent().getExtras();
        currentLatLng = new LatLng(bundle.getDouble("Lat"),bundle.getDouble("Long"));
        user_id = bundle.getString("user_id");

        Log.i("abc",currentLatLng.toString());

        //currentMarker = mMap.addMarker(new MarkerOptions().position(currentLatLng).title("Marker in current"));

        moveMap(currentLatLng);

        mMap.setOnMapClickListener(mapClickListener);

        mMap.setOnMarkerClickListener(markerClickListener);
    }
    private void moveMap(LatLng place) {
        // 建立地圖攝影機的位置物件
        CameraPosition cameraPosition =
                new CameraPosition.Builder()
                        .target(place)
                        .zoom(17)
                        .build();

        // 使用動畫的效果移動地圖
        mMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),
                new GoogleMap.CancelableCallback() {
                    @Override
                    public void onFinish() {
                        if (itemMarker != null) {
                            itemMarker.showInfoWindow();
                        }
                    }

                    @Override
                    public void onCancel() {

                    }
                });
    }
    GoogleMap.OnMapClickListener mapClickListener = new GoogleMap.OnMapClickListener() {
        @Override
        public void onMapClick(LatLng latLng) {
            lastMarker = mMap.addMarker(new MarkerOptions().position(latLng).title("Marker in point "));
            moveMap(latLng);
            lastLat = latLng.latitude;
            lastLong = latLng.longitude;
        }
    };
    GoogleMap.OnMarkerClickListener markerClickListener = new GoogleMap.OnMarkerClickListener() {
        @Override
        public boolean onMarkerClick(Marker marker) {
//            AlertDialog.Builder ab = new AlertDialog.Builder(MapsActivity.this);
//            ab.setTitle("選擇的位置")
//                    .setMessage("是否儲存目前位置")
//                    .setCancelable(true);

            AlertDialog alertDialog=new AlertDialog.Builder(MapsCalActivity.this)
                    .setIcon(R.mipmap.ic_launcher)
                    .setTitle("選擇的位置")
                    .setMessage("是否儲存")
                    .setCancelable(false)
                    .setPositiveButton("Ok", btnsClick)
                    .setNegativeButton("Cancel", btnsClick)
                    .create();
            alertDialog.show();

            return false;
        }
    };
    DialogInterface.OnClickListener btnsClick=new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            if(which==DialogInterface.BUTTON_POSITIVE){
                Toast.makeText(MapsCalActivity.this,"Ok",Toast.LENGTH_SHORT).show();
                Intent it = new Intent(MapsCalActivity.this,CalendarActivity.class);
                Bundle bundle = new Bundle();
                bundle.putDouble("lastLat",lastLat);
                bundle.putDouble("lastLong",lastLong);
                bundle.putString("user_id",user_id);
                it.putExtras(bundle);
                startActivity(it);
                MapsCalActivity.this.finish();
            }
            if(which==DialogInterface.BUTTON_NEGATIVE){
                Toast.makeText(MapsCalActivity.this,"Cancel",Toast.LENGTH_SHORT).show();
                lastLat = 0.0;
                lastLong = 0.0;
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(currentLatLng).title("Marker in current"));
                moveMap(currentLatLng);
            }
        }
    };
}
