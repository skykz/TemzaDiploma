package com.example.asus.mobiletracker.userSide.googleApi.network;

import com.example.asus.mobiletracker.userSide.googleApi.models.Checking;
import com.example.asus.mobiletracker.userSide.googleApi.models.ListSingleProduct;
import com.example.asus.mobiletracker.userSide.googleApi.models.ProductDetail;
import com.example.asus.mobiletracker.userSide.googleApi.models.Products;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;

import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiService {


    @GET("listForConsumers/{token}")
    Call<ListSingleProduct> listForConsumers(@Path("token") String token);

    @GET("listForCouriers")
    Call<ListSingleProduct> listOfProductsCouriers();

    @GET("listForCouriers/process")
    Call<ListSingleProduct> listOfProcessCouriers();

    @GET("listForCouriers/done")
    Call<ListSingleProduct> listOfDoneCouriers();

    //list of history
    @GET("listOfHistoryOrders/{token}")
    Call<ListSingleProduct> lisrOfHistoryProduct( @Path("token") String token);

    @GET("single/product/{id}")
    Call<ProductDetail> getOrderById(@Path("id") int id);

    @GET("check/order/track/{id}")
    Call <Checking> checkOrderTrack(@Path("id") int id);

    //set "DONE" status by consumer
    @GET("order/change/status/{id}")
    Call <Checking> setDoneStatus(@Path("id") int  id);


    // take a order by courier
    @GET("order/take/status/{id}/{token}")
    Call <Checking> setTakeOrder(@Path("id") int  id, @Path("token") String token);


    //TODO:  finish all request from application to Backend Laravel

}
