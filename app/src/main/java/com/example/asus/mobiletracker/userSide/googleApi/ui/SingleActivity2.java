package com.example.asus.mobiletracker.userSide.googleApi.ui;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;

import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.asus.mobiletracker.MapsActivity;
import com.example.asus.mobiletracker.R;
import com.example.asus.mobiletracker.userSide.googleApi.models.Checking;
import com.example.asus.mobiletracker.userSide.googleApi.models.ProductDetail;
import com.example.asus.mobiletracker.userSide.googleApi.models.Tracking;
import com.example.asus.mobiletracker.userSide.googleApi.network.ApiService;
import com.example.asus.mobiletracker.userSide.googleApi.network.RetrofitBuilder;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SingleActivity2 extends AppCompatActivity implements View.OnClickListener,
        SwipeRefreshLayout.OnRefreshListener,
        GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private ImageView image_single;
    private TextView text1,text2,text3,text4,title;


    @BindView(R.id.button_done)
     Button doneButton;


    private Call<ProductDetail> call;
    private Call<Checking> callCheck;

    private final String TAG = "SingleOrderActivity";

    private ApiService apiService;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    //FireBase
    DatabaseReference locations;

    //locations variables
    private static final int MY_PERMISSION_REQUEST_CODE = 7171;
    private static final int PLAY_SERVICE_RES_REQUEST = 7172;

    private LocationRequest mLocationRequest;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    private static int UPDATE_INTERVAL = 5000;
    private static int FASTEST_INTERVAL = 3000;
    private static int DISTANCE = 10;

    private ProgressDialog dialog;
    private AlertDialog alertDialog;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single2);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ButterKnife.bind(this);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        toolbar.setTitleTextColor(0xFFFFFFFF);

        apiService = RetrofitBuilder.createServiceWithAuth(ApiService.class);


        image_single = (ImageView)findViewById(R.id.single_image);
        title = (TextView)findViewById(R.id.single_title);
        text1 = (TextView)findViewById(R.id.single_txt1);
        text2 = (TextView)findViewById(R.id.single_txt2);
        text3 = (TextView)findViewById(R.id.single_txt3);
        text4 = (TextView)findViewById(R.id.single_status);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout_single);


        locations = FirebaseDatabase.getInstance().getReference("locations");

        //fetching single item data
        getOrderById();


        //checking & getting permission for location
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
            },MY_PERMISSION_REQUEST_CODE);
        }
        else
        {
            if(checkPlayServices())
            {
                buildGoogleApiClient();
                createLocationRequest();
                displayLocation();
            }
        }


    }

    private int getIdOrder(){
        int order_id = 0;
        Bundle intent = getIntent().getExtras();

        if (intent != null){
            order_id = intent.getInt("order_id");
        }
        return  order_id;
    }

    //TODO: display all single product data from a server

    private void getOrderById() {

        dialog = ProgressDialog.show(SingleActivity2.this,"Подождите","Загрузка...");
        call = apiService.getOrderById(getIdOrder());
        call.enqueue(new Callback<ProductDetail>() {

            @Override
            public void onResponse(Call<ProductDetail> call, Response<ProductDetail> response) {

                if (response.isSuccessful()) {
                    ProductDetail org = response.body();

                    Glide.with(getApplicationContext())
                            .load("https://temza.kz/storage/"+org.getImage())
                            .into(image_single);

                    title.setText(org.getTitle());

                  collapsingToolbarLayout.setTitle(org.getTitle());
                  collapsingToolbarLayout.setExpandedTitleColor(0xFFFFFFFF);

                  text1.setText("Цена : " + String.valueOf(org.getPrice())+ " тг");

                  text3.setText("Материал товара: " + org.getMaterial());

                  if (org.getStatus().equals("PENDING")) {
                      text4.setBackground(getResources().getDrawable(R.drawable.status_pending));
                      text4.setText("ожидание");

                  }
                  else if (org.getStatus().equals("DONE")) {
                      text4.setBackground(getResources().getDrawable(R.drawable.status_done));
                      text4.setText("доставлено");
                  }
                  else if (org.getStatus().equals("PROCESS")) {
                      text4.setBackground(getResources().getDrawable(R.drawable.status_proccess));
                      text4.setText("в пути");
                  }
                  dialog.dismiss();

                    Toast.makeText(getApplicationContext(), "Все успешно загружено", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ProductDetail> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Ошибка " + t.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }

    //check if the order already took by courier and let's start tracking process on the map
    @OnClick(R.id.button_track2)
    public void CheckCourier(View v) {

        dialog = ProgressDialog.show(SingleActivity2.this,"Ищем курьера","Загрузка... Подождите",true);


        callCheck = apiService.checkOrderTrack(getIdOrder());
        callCheck.enqueue(new Callback<Checking>() {

            @Override
            public void onResponse(Call<Checking> call, Response<Checking> response) {


                if (response.isSuccessful()) {
                    Checking org = response.body();

                    String status = org.getCurier_id();

                    if (status == null) {

                        dialog.dismiss();

                        alertDialog = new AlertDialog.Builder(SingleActivity2.this).create();
                        alertDialog.setTitle("Предужпреждение!");
                        alertDialog.setMessage("Извините, пока что наши курьеры не взяли ваш заказ. Подождите немного ... ");

                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "готово",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                });
                        alertDialog.show();
                    }
                    else
                    {

                        Intent intent = new Intent(SingleActivity2.this, MapsActivity.class);
                        intent.putExtra("lat", mLastLocation.getLatitude());
                        intent.putExtra("lng", mLastLocation.getLongitude());
                        intent.putExtra("uid", org.getCurier_id());

                        startActivity(intent);

                    }
                    Toast.makeText(SingleActivity2.this, "Все успешно загружено", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<Checking> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Ошибка " + t.getMessage(),Toast.LENGTH_SHORT).show();
            }
        });


    }


    //set "done" status to order , if order is really delivered


    @OnClick(R.id.button_done)
    void setDoneStatus(View v) {

        dialog = ProgressDialog.show(SingleActivity2.this,"","Загрузка... Подождите",true);

        callCheck = apiService.setDoneStatus(getIdOrder());
        callCheck.enqueue(new Callback<Checking>() {

            @Override
            public void onResponse(Call<Checking> call, Response<Checking> response) {

                if (response.code() == 200) {

                    dialog.dismiss();

                    alertDialog = new AlertDialog.Builder(SingleActivity2.this).create();
                    alertDialog.setTitle("Все гоотово!");
                    alertDialog.setMessage("Спасибо за покупку! Заказывайте еще :) ");

                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "спасибо",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
                else{

                    dialog.dismiss();

                    alertDialog = new AlertDialog.Builder(SingleActivity2.this).create();
                    alertDialog.setTitle("Ошибка!");
                    alertDialog.setMessage("Повторите попытку позже...");

                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "ок",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();
                }
            }

            @Override
            public void onFailure(Call<Checking> call, Throwable t) {
                Toast.makeText(getApplicationContext(),"Ошибка " + t.getMessage(),Toast.LENGTH_SHORT).show();

            }
        });
    }


    private void displayLocation() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }

        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);


        if (mLastLocation != null)
        {
            //Updating to FireBase DataBase
            Log.d("MY LOCATION","mLocation ->>>>>>>> lat  " + mLastLocation.getLatitude());
            Log.d("MY LOCATION","mLocation ->>>>>>>> lng  " + mLastLocation.getLongitude());


            locations.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                    .setValue(new Tracking(FirebaseAuth.getInstance().getCurrentUser().getEmail(),
                            FirebaseAuth.getInstance().getCurrentUser().getUid(),
                            String.valueOf(mLastLocation.getLatitude()),
                            String.valueOf(mLastLocation.getLongitude())));
        }
        else
        {
            Toast.makeText(this,"Couldn't get the location",Toast.LENGTH_SHORT).show();
        }
    }
    private void createLocationRequest() {

        mLocationRequest = new LocationRequest();

        mLocationRequest.setInterval(UPDATE_INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setSmallestDisplacement(DISTANCE);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    private void buildGoogleApiClient() {

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
        mGoogleApiClient.connect();

        Log.d("TESSSSSSSST","Build   OKKKKKKKKKK" );
    }

    private boolean checkPlayServices() {

        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS)
        {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)){
                GooglePlayServicesUtil.getErrorDialog(resultCode,this,PLAY_SERVICE_RES_REQUEST).show();
            }
            else
            {
                Toast.makeText(this,"This device is not supported",Toast.LENGTH_SHORT).show();
                Log.d("TEEEEEEEEEEEEEEST","I can't load location");
                finish();
            }

            return false;
        }
        Log.d("TESSSSSSSST","CHECCK   " + true);
        return true;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        displayLocation();
        startLocationUpdate();
//        mGoogleApiClient.connect();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        switch (requestCode)
        {
            case MY_PERMISSION_REQUEST_CODE:
            {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    if (checkPlayServices())
                    {
                        buildGoogleApiClient();
                        createLocationRequest();
                        displayLocation();
                    }
                }
            }
        }
    }

    private void startLocationUpdate() {

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient,mLocationRequest,this);
    }

    @Override
    public void onConnectionSuspended(int i) {

        mGoogleApiClient.connect();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onLocationChanged(Location location) {

        mLastLocation = location;
        displayLocation();
    }


    @Override
    protected void onStart() {
        super.onStart();

        if (mGoogleApiClient != null)
            mGoogleApiClient.connect();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkPlayServices();
    }

    @Override
    protected void onStop() {
        super.onStop();

        if (mGoogleApiClient != null)
            mGoogleApiClient.disconnect();

        if (dialog != null)
            dialog.dismiss();

    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (call != null){
            call.cancel();
            call = null;
        }
        if (callCheck != null){
            callCheck.cancel();
            callCheck = null;
        }
    }

    @Override
    public void onClick(View v) {

    }
}
