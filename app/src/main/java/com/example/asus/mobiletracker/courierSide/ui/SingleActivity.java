package com.example.asus.mobiletracker.courierSide.ui;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.asus.mobiletracker.R;
import com.example.asus.mobiletracker.userSide.googleApi.models.Checking;
import com.example.asus.mobiletracker.userSide.googleApi.models.ProductDetail;
import com.example.asus.mobiletracker.userSide.googleApi.network.ApiService;
import com.example.asus.mobiletracker.userSide.googleApi.network.RetrofitBuilder;
import com.example.asus.mobiletracker.userSide.googleApi.ui.SingleActivity2;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SingleActivity extends AppCompatActivity implements View.OnClickListener{

    private ProgressDialog dialog;
    private AlertDialog alertDialog;

    private ImageView image_single;
    private TextView text1,text2,text3,text4,title;


    @BindView(R.id.button_take)
    Button takeButton;

    private Call<Checking> callCheck;
    private Call<ProductDetail> call;

    private final String TAG = "SingleOrderActivity2";

    private ApiService apiService;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ButterKnife.bind(this);
        apiService = RetrofitBuilder.createServiceWithAuth(ApiService.class);


        image_single = (ImageView)findViewById(R.id.single_image);
        title = (TextView)findViewById(R.id.single_title);
        text1 = (TextView)findViewById(R.id.single_txt1);
        text2 = (TextView)findViewById(R.id.single_txt2);
        text3 = (TextView)findViewById(R.id.single_txt3);
        text4 = (TextView)findViewById(R.id.single_status);

        collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.toolbar_layout_single);

        getOrderById();

    }

    private int getIdOrder(){
        int order_id = 0;
        Bundle intent = getIntent().getExtras();

        if (intent != null){
            order_id = intent.getInt("order_id");
        }
        return  order_id;
    }

    private void getOrderById() {

        dialog = ProgressDialog.show(this,"Подождите","Загрузка...");
        call = apiService.getOrderById(getIdOrder());
        Toast.makeText(getApplicationContext(), "Все успешно" + getIdOrder(), Toast.LENGTH_SHORT).show();

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
                    else if (org.getStatus().equals("DRAFT")) {
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

    @OnClick(R.id.button_take)
    void actionTake(){

        dialog = ProgressDialog.show(SingleActivity.this,"","Загрузка... Подождите",true);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();


        callCheck = apiService.setTakeOrder(getIdOrder(),user.getUid());
        callCheck.enqueue(new Callback<Checking>() {

            @Override
            public void onResponse(Call<Checking> call, Response<Checking> response) {

                if (response.code() == 200) {

                    dialog.dismiss();

                    alertDialog = new AlertDialog.Builder(SingleActivity.this).create();
                    alertDialog.setTitle("Все гоотово!");
                    alertDialog.setMessage("Здорово! Вы взяли этот заказ.\n Желаем удачи :) ");

                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "готово",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                    alertDialog.show();

                    takeButton.setEnabled(false);

                }
                else{

                    dialog.dismiss();

                    alertDialog = new AlertDialog.Builder(SingleActivity.this).create();
                    alertDialog.setTitle("Ошибка!");
                    alertDialog.setMessage("Повторите попытку позже...");

                    alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "готово",
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


    @Override
    public void onClick(View v) {

    }
}
