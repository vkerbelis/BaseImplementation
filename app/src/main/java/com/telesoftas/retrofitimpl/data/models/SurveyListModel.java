package com.telesoftas.retrofitimpl.data.models;

import android.support.v4.app.FragmentActivity;
import android.util.Log;

import com.squareup.okhttp.Interceptor;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.ResponseBody;
import com.telesoftas.retrofitimpl.ProgressResponseBody;
import com.telesoftas.retrofitimpl.data.entities.LoginResponse;
import com.telesoftas.retrofitimpl.data.entities.Survey;
import com.telesoftas.retrofitimpl.data.entities.User;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import retrofit.Call;
import retrofit.Callback;
import retrofit.Response;
import retrofit.Retrofit;

/**
 * @author Vidmantas Kerbelis (vkerbelis@yahoo.com) on 2016-01-04.
 */
public class SurveyListModel extends AbstractListModel<Survey> {

    public SurveyListModel(FragmentActivity activity, DataListModelCallbacks<Survey> callbacks) {
        super(activity, callbacks);
    }

    public void doSomeFileDownloading() {
        final OkHttpClient client = new OkHttpClient();

        final ProgressResponseBody.ProgressListener progressListener = new ProgressResponseBody.ProgressListener() {
            @Override
            public void update(long bytesRead, long contentLength, boolean done) {
                Thread.currentThread().getName();
                Log.d(TAG, "" + bytesRead);
                Log.d(TAG, "" + contentLength);
                Log.d(TAG, "" + done);
                Log.d(TAG, String.format("%d%% done\n", (100 * bytesRead) / contentLength));
            }
        };
        client.networkInterceptors().add(new Interceptor() {
            @Override
            public com.squareup.okhttp.Response intercept(Chain chain) throws IOException {
                com.squareup.okhttp.Response originalResponse = chain.proceed(chain.request());
                return originalResponse.newBuilder()
                        .body(new ProgressResponseBody(originalResponse.body(), progressListener))
                        .build();
            }
        });

        mService.download()
                .enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(final Response<ResponseBody> response, Retrofit retrofit) {
                        Log.d(TAG, "File get: " + response.raw());
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    InputStream inputStream = response.body().byteStream();
//                                    while (1 == 1) {
//                                        inputStream.read();
//                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        }).start();
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        t.printStackTrace();
                    }
                });
    }


    @Override
    protected List<Survey> loadData() {
        Call<LoginResponse> call = mService.login(new User("email2@test.com", "test"));
        try {
            Response<LoginResponse> response = call.execute();
            LoginResponse loginResponse = response.body();
            Log.d(TAG, "Login token: " + loginResponse.getToken());
            Call<List<Survey>> listCall = mService.getSurveyList(loginResponse.getToken());
            Response<List<Survey>> listResponse = listCall.execute();
            return listResponse.body();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

}
