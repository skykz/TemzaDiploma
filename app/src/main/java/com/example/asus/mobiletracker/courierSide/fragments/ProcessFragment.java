package com.example.asus.mobiletracker.courierSide.fragments;

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

import com.example.asus.mobiletracker.R;
import com.example.asus.mobiletracker.courierSide.adapters.ListOfProductsAdapter2;
import com.example.asus.mobiletracker.courierSide.ui.SingleActivity;
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

public class ProcessFragment extends Fragment implements ListOfProductsAdapter.OnNoteListener{

    private static final String TAG = "MainOrdersFragment";

    private RecyclerView recyclerView;

    private List<Products> lister;
    private ListOfProductsAdapter2 mAdapter;
    private Call<ListSingleProduct> call;
    private ApiService apiService;
    private ProgressDialog progressDialog;
    private SwipeRefreshLayout swipeRefreshLayout;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_process, container, false);

        //         recycler view to do list
        recyclerView = view.findViewById(R.id.recycler_view_process);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);

        swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipe_up_refresh_process);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getProductList();
                    }
                }, 1000);
            }
        });

        apiService = RetrofitBuilder.createServiceWithAuth(ApiService.class);

        getProductList();

        return view;
    }


    public void getProductList(){

        progressDialog = ProgressDialog.show(getContext(),"Список ваших заказов","Загрузка... Подождите",true);

        call = apiService.listOfProcessCouriers();
        call.enqueue(new Callback<ListSingleProduct>() {

            @Override
            public void onResponse(Call<ListSingleProduct> call, Response<ListSingleProduct> response) {
                if (response.isSuccessful()) {

                    lister = response.body().getData();

                    mAdapter = new ListOfProductsAdapter2(lister, getContext(), new ListOfProductsAdapter2.OnNoteListener() {

                        @Override
                        public void onNoteClick(int position) {
                            int postId = lister.get(position).getId();
                            Intent intent = new Intent(getActivity(), SingleActivity.class);
                            intent.putExtra("order_id",postId);
                            startActivity(intent);
                        }
                    });

                    mAdapter.notifyDataSetChanged();
                    recyclerView.setVisibility(View.VISIBLE);
                    recyclerView.setAdapter(mAdapter);
                    progressDialog.dismiss();

                    swipeRefreshLayout.setRefreshing(false);

                    Toast.makeText(getContext(), "Все успешно загружено" , Toast.LENGTH_SHORT).show();

                } else {
//                    tokenManager.deleteToken();
                    Toast.makeText(getContext(), "Ошибка с сервера" , Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ListSingleProduct> call, Throwable t) {
                Toast.makeText(getContext(), "Не могу загрузить данные" + t.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onNoteClick(int position) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (call != null){
            call.cancel();
            call = null;
        }

        if(progressDialog != null)
        {
            progressDialog.dismiss();
            progressDialog = null;
        }
    }


}
