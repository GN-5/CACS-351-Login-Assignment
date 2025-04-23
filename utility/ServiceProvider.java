package com.gaurabneupane.id424.utility;

import com.gaurabneupane.id424.BuildConfig;
import com.gaurabneupane.id424.data.AuthService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ServiceProvider {

    private static OkHttpClient getOkHttpClient(){
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(
                BuildConfig.DEBUG ? HttpLoggingInterceptor.Level.BODY : HttpLoggingInterceptor.Level.NONE
        );
        return new OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build();
    }
    public static AuthService getService(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(AuthService.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                //this line logs API calls and logs maybe.
                .client(getOkHttpClient())
                .build();

        return retrofit.create(AuthService.class);
    }
}

