package com.telesoftas.retrofitimpl.data.models;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.telesoftas.retrofitimpl.data.DataReceiver;
import com.telesoftas.retrofitimpl.data.DatabaseService;
import com.telesoftas.retrofitimpl.data.repositories.AbstractRepository;
import com.telesoftas.retrofitimpl.data.repositories.RepositoryEntity;
import com.telesoftas.retrofitimpl.utils.Logger;

import java.util.List;

/**
 * @author Vidmantas Kerbelis (vkerbelis@yahoo.com) on 2016-01-04.
 */
public abstract class AbstractRepositoryModel<T extends RepositoryEntity,
        Repo extends AbstractRepository<T>> extends AbstractListModel<T>
        implements DataReceiver.DataReceiverCallbacks {

    final AbstractRepository<T> mRepository;
    String mType;
    private LocalBroadcastManager mLocalBroadcastManager;
    private DataReceiver mReceiver;

    public AbstractRepositoryModel(FragmentActivity activity, AbstractRepository<T> repository,
                                   DataListModelCallbacks<T> callbacks) {
        super(activity, callbacks);
        mRepository = repository;
    }

    public abstract void putItem(T item, int adapterPosition);

    public abstract void deleteItem(T item, int adapterPosition);

    public List<T> getList() {
        return mList;
    }

    @Override
    protected List<T> loadData() {
        mList = mRepository.list();
        return mList;
    }

    /**
     * This MUST be called before registering other listeners,
     * else all registering will be ignored
     */
    public void initializeReceiver(String type) {
        this.mType = type;
        this.mLocalBroadcastManager = LocalBroadcastManager.getInstance(mContext);
        this.mReceiver = DataReceiver.getInstance(this, mType);
    }

    public void registerDefaultListeners() {
        if (mReceiver != null) {
            Logger.d(TAG, "Registering receiver of type: " + mType);
            registerActionListener(DatabaseService.ACTION_PUT, mType);
        }
    }

    public void registerActionListener(String action, String type) {
        registerActionListener(action, type, mReceiver);
    }

    void registerActionListener(String action, String type, BroadcastReceiver receiver) {
        if (mReceiver != null) {
            try {
                IntentFilter filter = new IntentFilter(action);
                if (!TextUtils.isEmpty(type)) {
                    filter.addDataType(type);
                }
                mLocalBroadcastManager.registerReceiver(receiver, filter);
            } catch (IntentFilter.MalformedMimeTypeException cause) {
                Logger.e(TAG, "Malformed MIME type", cause);
            }
        }
    }

    public void unregisterActionListener() {
        if (mReceiver != null) {
            Logger.d(TAG, "Unregistering receiver of type: " + mType);
            DataReceiver.unregisterListener(this);
            mLocalBroadcastManager.unregisterReceiver(mReceiver);
        }
    }

    @Override
    public void onReceivedData(Intent intent) {
        Logger.d(TAG, "Received intent: " + intent);
    }

}
