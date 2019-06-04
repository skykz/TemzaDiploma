package com.example.asus.mobiletracker.userSide.googleApi.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.asus.mobiletracker.MapsActivity;
import com.example.asus.mobiletracker.R;
import com.example.asus.mobiletracker.userSide.googleApi.adapters.ListOfProductsAdapter;
import com.example.asus.mobiletracker.userSide.googleApi.models.ListSingleProduct;
import com.example.asus.mobiletracker.userSide.googleApi.models.Products;
import com.example.asus.mobiletracker.userSide.googleApi.network.ApiService;
import com.example.asus.mobiletracker.userSide.googleApi.network.RetrofitBuilder;
import com.example.asus.mobiletracker.userSide.googleApi.ui.SingleActivity2;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class OrdersFragment extends Fragment implements ListOfProductsAdapter.OnNoteListener, SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "MainOrdersFragment";

    private RecyclerView recyclerView;

    List<Products> lister;
    ListOfProductsAdapter mAdapter;
    Call<ListSingleProduct> call;
    ApiService apiService;

    private ProgressDialog dialog;
    private ListOfProductsAdapter.OnNoteListener onNoteListener;

    private SwipeRefreshLayout swipeRefreshLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);


        //         recycler view to do list
        recyclerView = view.findViewById(R.id.recycler_order_main);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);


        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_up_refresh_main);
        swipeRefreshLayout.setOnRefreshListener(this);

        apiService = RetrofitBuilder.createServiceWithAuth(ApiService.class);

        getProductList();

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        SharedPreferences preferences = this.getActivity().getSharedPreferences("preference", Context.MODE_PRIVATE);

    }

    @Override
    public void onNoteClick(int position) { }

    public void getProductList(){


        dialog = ProgressDialog.show(getContext(),"Список ваших заказов","Загрузка... Подождите",true);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        Log.d("USEr","user uid is" + user.getUid());

        call = apiService.listForConsumers(user.getUid());
        call.enqueue(new Callback<ListSingleProduct>() {

            @Override
            public void onResponse(Call<ListSingleProduct> call, Response<ListSingleProduct> response) {
                if (response.isSuccessful()) {

                    lister = response.body().getData();
                    mAdapter = new ListOfProductsAdapter(lister, getContext(), new ListOfProductsAdapter.OnNoteListener() {

                        @Override
                        public void onNoteClick(int position) {
                            int postId = lister.get(position).getId();

                            Toast.makeText(getContext(), "Position id is >>  " + postId, Toast.LENGTH_SHORT).show();

                            Intent intent = new Intent(getActivity(), SingleActivity2.class);
                            intent.putExtra("order_id",postId);///model.getEmail();
//                            intent.putExtra("lat",)
                            startActivity(intent);
                        }
                    });

                    mAdapter.notifyDataSetChanged();

                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.setAdapter(mAdapter);
                    dialog.dismiss();

//                    HideLoading();
                    Toast.makeText(getContext(), "Все успешно загружено" , Toast.LENGTH_SHORT).show();

                }

            }

            @Override
            public void onFailure(Call<ListSingleProduct> call, Throwable t) {
                Toast.makeText(getContext(), "Не могу загрузить данные" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

    }


    @Override
    public void onStop() {
        super.onStop();
        if (dialog != null)
            dialog.dismiss();

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        if (call != null){
            call.cancel();
            call = null;
        }

        if (dialog != null)
            dialog.dismiss();
    }

    @Override
    public void onRefresh() {
        Toast.makeText(getContext(), "Обновление...", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                getProductList();
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 1000);
    }
}


