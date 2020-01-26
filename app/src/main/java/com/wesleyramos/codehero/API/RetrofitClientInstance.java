package com.wesleyramos.codehero.API;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClientInstance {

    private static Retrofit sRetrofit;
    private static final String URL_API_MARVEL = "https://gateway.marvel.com/";

    public static Retrofit getRetrofitInstance() {
        if (sRetrofit != null) return sRetrofit;

        sRetrofit = new retrofit2.Retrofit.Builder()
                .baseUrl(URL_API_MARVEL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        return sRetrofit;
    }
}
