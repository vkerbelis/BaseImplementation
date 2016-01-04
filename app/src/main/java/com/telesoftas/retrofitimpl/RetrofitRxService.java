package com.telesoftas.retrofitimpl;

import com.squareup.okhttp.ResponseBody;
import com.telesoftas.retrofitimpl.data.entities.LoginResponse;
import com.telesoftas.retrofitimpl.data.entities.Survey;
import com.telesoftas.retrofitimpl.data.entities.User;

import java.util.List;

import retrofit.http.Body;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Streaming;
import rx.Observable;

/**
 * @author Vidmantas Kerbelis (vkerbelis@yahoo.com) on 2016-01-04.
 */
public interface RetrofitRxService {

    @Streaming
    @GET("http://releases.ubuntu.com/14.04.3/ubuntu-14.04.3-desktop-amd64.iso")
    Observable<ResponseBody> download();

    @POST("login")
    Observable<LoginResponse> login(
            @Body User user
    );

    @GET("surveys")
    Observable<List<Survey>> getSurveyList(
            @Header("Authorization") String value
    );

}
