package com.telesoftas.retrofitimpl;

import android.util.Log;

import com.telesoftas.retrofitimpl.data.entities.LoginResponse;
import com.telesoftas.retrofitimpl.data.entities.Survey;
import com.telesoftas.retrofitimpl.data.entities.User;

import java.util.List;

import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * @author Vidmantas Kerbelis (vkerbelis@yahoo.com) on 2015-12-31.
 */
public class RxIt {

    private static final String TAG = "RxJava";
    private RetrofitRxService mService;

    public RxIt() {
        mService = NetworkManager.getInstance().getRxService();
    }

    public void doSomeTazkz() {
        Observer<List<Survey>> observer = new Observer<List<Survey>>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
            }

            @Override
            public void onNext(List<Survey> surveys) {
                Log.d(TAG, "Surveys? " + surveys);
            }
        };
        mService.login(new User("email2@test.com", "test"))
                .flatMap(new Func1<LoginResponse, Observable<List<Survey>>>() {
                    @Override
                    public Observable<List<Survey>> call(LoginResponse loginResponse) {
                        return mService.getSurveyList(loginResponse.getToken());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Survey>>() {
                    @Override
                    public void call(List<Survey> surveys) {
                        String nullString = null;
                        nullString.toString();
                    }
                }, new Action1<Throwable>() {
                    @Override
                    public void call(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });

        mService.login(new User("email2@test.com", "test"))
                .flatMap(new Func1<LoginResponse, Observable<List<Survey>>>() {
                    @Override
                    public Observable<List<Survey>> call(LoginResponse loginResponse) {
                        try {
                            Thread.sleep(5000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return mService.getSurveyList(loginResponse.getToken());
                    }
                })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(observer);
//        call.enqueue(new Callback<LoginResponse>() {
//            @Override
//            public void onResponse(Response<LoginResponse> response, Retrofit retrofit) {
//                Log.d(TAG, "Something? Did I get a body? " + response.body().getToken());
//
//
//                Call<List<Survey>> call = mService.getSurveyList(response.body().getToken());
//                call.enqueue(new Callback<List<Survey>>() {
//                    @Override
//                    public void onResponse(Response<List<Survey>> response, Retrofit retrofit) {
//                        Log.d(TAG, "Surveys? " + response.body());
//
//                    }
//
//                    @Override
//                    public void onFailure(Throwable t) {
//                        Log.d(TAG, "And... You fail... #2");
//
//                    }
//                });
//
//            }
//
//            @Override
//            public void onFailure(Throwable t) {
//                Log.d(TAG, "And... You fail...");
//            }
//        });
    }

    public void doRxArrayStuffz() {
        Observable.create(new Observable.OnSubscribe<Integer>() {
            @Override
            public void call(Subscriber<? super Integer> subscriber) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                subscriber.onNext(1463);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.computation()).flatMap(new Func1<Integer, Observable<String>>() {
            @Override
            public Observable<String> call(final Integer integer) {
                return Observable.create(new Observable.OnSubscribe<String>() {
                    @Override
                    public void call(Subscriber<? super String> subscriber) {
                        subscriber.onNext("Watafak? " + integer);
                        subscriber.onCompleted();
                    }
                });
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(String s) {
                        Log.d(TAG, "Complete result: " + s);
                    }
                });
    }

    public void doRxMappedHelloWorld() {
        Observable.just("Hello").map(new Func1<String, String>() {
            @Override
            public String call(String s) {
                return s + " World!";
            }
        }).subscribe(new Observer<String>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(String s) {
                Log.d(TAG, s);
            }
        });
    }

}
