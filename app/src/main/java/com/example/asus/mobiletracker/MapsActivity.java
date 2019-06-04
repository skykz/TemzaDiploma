package com.example.asus.mobiletracker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;

import com.example.asus.mobiletracker.userSide.googleApi.models.Tracking;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.DecimalFormat;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private String uid;

    private DatabaseReference locations;
    private Double lat,lng;



    //TODO: fix location of current user at real time
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        locations = FirebaseDatabase.getInstance().getReference("locations");


        getTokenCourier();

        if (!TextUtils.isEmpty(uid))
            loadUserLocation(uid);

    }

    private void loadUserLocation( String uid) {

        Query user_location = locations.orderByChild("uid").equalTo(uid);

        user_location.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot postSnapShot:dataSnapshot.getChildren())
                {
                    Tracking tracking = postSnapShot.getValue(Tracking.class);

                    Log.d("Tracking Class","tracking data isssssss  " + tracking.getUid());
                    Log.d("Tracking Class","tracking data LAT  " + tracking.getLat());
                    Log.d("Tracking Class","tracking data LNG  " + tracking.getLng());
                    //Add maker for friend location
                    LatLng courierLocation = new LatLng(Double.parseDouble(tracking.getLng()),
                            Double.parseDouble(tracking.getLat()));


                    //create location from user coordinates
                    Location currentUser = new Location("");
                    currentUser.setLatitude(lat);
                    currentUser.setLongitude(lng);

//                    Log.d("Tracking Class","USER data LAT  " + currentUser.getLatitude());
//                    Log.d("Tracking Class","USER data LNG  " + currentUser.getLongitude());


                    //create location from courier coordinates
                    Location courierUser = new Location("");
                    courierUser.setLatitude(Double.parseDouble(tracking.getLat()));
                    courierUser.setLongitude(Double.parseDouble(tracking.getLng()));



//                    Log.d("Tracking Class","COURIER data LAT  " + courierUser.getLatitude());
//                    Log.d("Tracking Class","COURIER data LNG  " + courierUser.getLongitude());
                    //Add marker courier  on Map
                    mMap.addMarker(new MarkerOptions()

                    .position(courierLocation)
                    .title(tracking.getEmail())
                    .snippet("Дистанция " + new DecimalFormat("#.#").format(distance(currentUser,courierUser)))
                    .icon(bitmapDescriptorFromVector(getApplicationContext(),R.drawable.ic_courier)));

                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng),12.0f));
                }

                LatLng cur = new LatLng(lat,lng);
                //TODO: add some thing to design og modal on icon

                mMap.addMarker(new MarkerOptions().position(cur).
                        title(FirebaseAuth.getInstance().getCurrentUser().getEmail())
                        .icon(bitmapDescriptorFromVector(getApplicationContext(),R.drawable.ic_user_map_location)));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    private BitmapDescriptor bitmapDescriptorFromVector(Context context, int vectorResId) {
        Drawable vectorDrawable = ContextCompat.getDrawable(context, vectorResId);
        vectorDrawable.setBounds(0, 0, vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight());
        Bitmap bitmap = Bitmap.createBitmap(vectorDrawable.getIntrinsicWidth(), vectorDrawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        vectorDrawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }


    //TODO :  fix distance function
    private Double distance(Location currentUser,Location courierUser) {

        double theta = currentUser.getLatitude() - courierUser.getLatitude();
        double dist = Math.sin(deg2rad(currentUser.getLongitude()) * Math.sin(deg2rad(courierUser.getLongitude()))
                * Math.cos(deg2rad(currentUser.getLongitude())) * Math.cos(deg2rad(courierUser.getLongitude()))
                * Math.cos(deg2rad(theta)));

        dist = Math.acos(dist);
        dist = rad2deg(dist);

        dist = dist * 60 * 1.1515;


        return (dist);
    }

    private double rad2deg(double dist) {

        return (dist * 180 / Math.PI);
    }

    private double deg2rad(double latitude) {

        return (latitude * Math.PI / 180.0);
    }


    private void getTokenCourier() {

        //TODO: get location and Email from a recycler to this activity
        if (getIntent() != null)
        {
            // actually here is should be data from a another activity like a intent
            uid = getIntent().getStringExtra("uid");
            lng = getIntent().getDoubleExtra("lng",0);
            lat = getIntent().getDoubleExtra("lat",0);
        }

    }
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));

    }
}
