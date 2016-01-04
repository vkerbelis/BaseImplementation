package com.telesoftas.retrofitimpl.data.models;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;

import com.telesoftas.retrofitimpl.NetworkManager;
import com.telesoftas.retrofitimpl.RetrofitService;
import com.telesoftas.retrofitimpl.utils.Logger;

import java.util.List;

/**
 * @author Vidmantas Kerbelis (vkerbelis@yahoo.com) on 2016-01-04.
 */
public abstract class AbstractListModel<T> implements LoaderManager.LoaderCallbacks<List<T>> {

    final String TAG = Logger.makeLogTag(this.getClass());
    final Context mContext;
    final DataListModelCallbacks<T> mListener;
    final RetrofitService mService;
    List<T> mList;
    private FragmentActivity mActivity;
    private boolean mLoadFinished;
    private boolean mLoadStopped = true;

    public AbstractListModel(FragmentActivity activity, DataListModelCallbacks<T> callbacks) {
        mContext = activity.getApplicationContext();
        mActivity = activity;
        mListener = callbacks;
        mService = NetworkManager.getInstance().getService();
    }

    public void load() {
        Logger.d(TAG, "Load called, restart? - " + (mList == null ? "Yes" : "No"));
        LoaderManager loaderManager = mActivity.getSupportLoaderManager();
        loaderManager.initLoader(0, null, this);
    }

    public List<T> getList() {
        return mList;
    }

    /**
     * This method runs on a background thread, feel free to
     * do all the networking or database transactions here.
     *
     * @return a list of data
     */
    protected abstract List<T> loadData();

    @Override
    public Loader<List<T>> onCreateLoader(int id, Bundle args) {
        return new AsyncTaskLoader<List<T>>(mContext) {

            @Override
            public List<T> loadInBackground() {
                mLoadFinished = false;
                return loadData();
            }

            @Override
            protected void onStartLoading() {
                if (mLoadStopped && !mLoadFinished) {
                    forceLoad();
                }
                mLoadStopped = false;
            }

            @Override
            protected void onStopLoading() {
                mLoadStopped = true;
                cancelLoad();
            }
        };
    }

    @Override
    public void onLoadFinished(Loader<List<T>> loader, List<T> data) {
        mListener.onLoadFinished(data);
    }

    @Override
    public void onLoaderReset(Loader<List<T>> loader) {

    }

    public interface DataListModelCallbacks<T> {

        void onLoadFinished(List<T> data);

    }

}
