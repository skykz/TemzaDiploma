package com.example.asus.mobiletracker.userSide.googleApi.network;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;

import com.facebook.stetho.okhttp3.BuildConfig;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import java.io.IOException;
import java.util.concurrent.TimeUnit;


import okhttp3.Request;
import retrofit2.Retrofit;
import okhttp3.Response;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class RetrofitBuilder {

    private final static String BASE_URL = "https://temza.kz/api/";

    private final static OkHttpClient client = buildClient();

    private final static  Retrofit retrofit = buildRetrofit(client);


    private static OkHttpClient buildClient(){

        OkHttpClient.Builder builder = new OkHttpClient.Builder().connectTimeout(15, TimeUnit.SECONDS)
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {

                        Request request = chain.request();

                        Request.Builder builder = request.newBuilder()
                                .addHeader("Accept","application/json")
                                .addHeader("Connection","close");

                        request = builder.build();

                        return  chain.proceed(request);
                    }
                });

        if (BuildConfig.DEBUG){
            builder.addNetworkInterceptor(new StethoInterceptor());
        }

        return builder.build();
    }

    private static Retrofit buildRetrofit(OkHttpClient client){

        return new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create())
                .build();
    }

    public static <T> T createService(Class<T> service)
    {
        return retrofit.create(service);
    }

    public static <T> T createServiceWithAuth(Class <T> service)
    {
        OkHttpClient newClient = client.newBuilder().addInterceptor(new Interceptor() {
            @Override
            public Response intercept(Chain chain) throws IOException {

                Request request = chain.request();

                Request.Builder builder = request.newBuilder();


                request = builder.build();
                return chain.proceed(request);

            }
        }).build();

        Retrofit newRetrofit = retrofit.newBuilder().client(newClient).build();
        return  newRetrofit.create(service);
    }


    public static Retrofit getRetrofit(){
        return retrofit;
    }
}
