package com.wesleyramos.codehero.API;

import android.content.Context;

import com.wesleyramos.codehero.Helpers.MD5;
import com.wesleyramos.codehero.R;

import java.util.Date;
import java.util.HashMap;

public class ServiceMarvel {
    private static HashMap<String, String> auth;

    public static GetDataServicesInterface getInstance() {
        return RetrofitClientInstance
                .getRetrofitInstance()
                .create(GetDataServicesInterface.class);
    }

    public static HashMap<String, String> getAuth(Context pContext) {
        if (auth == null) {
            auth = new HashMap<>();
            auth.put("apikey", pContext.getResources().getString(R.string.api_marvel_key_public));
        }

        String ts = String.valueOf(new Date().hashCode()), hash = ts;

        hash += pContext.getResources().getString(R.string.api_marvel_key_private);
        hash += auth.get("apikey");

        auth.put("ts", ts);
        auth.put("hash", MD5.create(hash));

        return auth;
    }
}
