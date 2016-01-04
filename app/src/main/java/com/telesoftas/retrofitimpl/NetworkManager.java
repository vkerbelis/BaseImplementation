package com.telesoftas.retrofitimpl;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.telesoftas.retrofitimpl.data.entities.Survey;

import java.lang.reflect.Type;
import java.util.List;

import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;

/**
 * @author Vidmantas Kerbelis (vkerbelis@yahoo.com) on 2016-01-04.
 */
public class NetworkManager {

    private final RetrofitRxService mRxService;
    private final RetrofitService mService;

    private NetworkManager() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        Type surveyListType = new TypeToken<List<Survey>>() {
        }.getType();
        gsonBuilder.registerTypeAdapter(surveyListType, new Survey.SurveyListDeserializer());
        Gson gson = gsonBuilder.create();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://baglandet-dev.elasticbeanstalk.com/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        mService = retrofit.create(RetrofitService.class);
        mRxService = retrofit.create(RetrofitRxService.class);
    }

    public static NetworkManager getInstance() {
        return LazyHolder.INSTANCE;
    }

    public RetrofitService getService() {
        Log.d("NetworkManager", "Getting Service: " + mService);
        return mService;
    }

    public RetrofitRxService getRxService() {
        return mRxService;
    }

    private static class LazyHolder {
        public static final NetworkManager INSTANCE = new NetworkManager();
    }
}
