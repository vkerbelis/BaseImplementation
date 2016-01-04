package com.telesoftas.retrofitimpl.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.telesoftas.retrofitimpl.R;
import com.telesoftas.retrofitimpl.RxIt;
import com.telesoftas.retrofitimpl.data.entities.Survey;
import com.telesoftas.retrofitimpl.data.models.AbstractListModel;
import com.telesoftas.retrofitimpl.data.models.SurveyListModel;
import com.telesoftas.retrofitimpl.utils.Logger;

import java.util.List;

public class MainActivity extends AppCompatActivity implements AbstractListModel.DataListModelCallbacks<Survey> {

    private static final String TAG = MainActivity.class.getSimpleName();
    private SurveyListModel mModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onResume() {
        Logger.d("MainActivity", "onStart()");
        super.onResume();
        mModel = new SurveyListModel(this, this);
        mModel.load();
        RxIt rxIt = new RxIt();
        rxIt.doRxArrayStuffz();
        rxIt.doRxMappedHelloWorld();
        rxIt.doSomeTazkz();
    }

    @Override
    public void onLoadFinished(List<Survey> data) {
        Logger.d(TAG, "Loaded list: " + data);
        mModel.doSomeFileDownloading();
    }
}
