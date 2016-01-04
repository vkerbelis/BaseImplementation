package com.telesoftas.retrofitimpl.data;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.telesoftas.retrofitimpl.utils.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vidmantas Kerbelis (vkerbelis@yahoo.com) on 2015-10-19.
 */
public class DataReceiver extends BroadcastReceiver {

    private static final String TAG = Logger.makeLogTag(DataReceiver.class);
    private static final int FLAG_NONE = 0x00;
    private static final int FLAG_DO_NOT_REGISTER = 0x01;

    private List<CallbackItem> mListeners;

    private DataReceiver(DataReceiverCallbacks listener, String type) {
        if (mListeners == null) {
            mListeners = new ArrayList<>();
        }
        mListeners.add(new CallbackItem(listener, type));
    }

    public static DataReceiver getInstance(DataReceiverCallbacks listener, String type) {
        return LazyHolder.getInstance(listener, type);
    }

    public static void unregisterListener(DataReceiverCallbacks listener) {
        LazyHolder.getInstance(FLAG_DO_NOT_REGISTER).unregister(listener);
    }

    public static boolean isAnyListenerRegistered() {
        return LazyHolder.getInstance(FLAG_DO_NOT_REGISTER).mListeners.size() > 0;
    }

    private void unregister(DataReceiverCallbacks listener) {
        for (CallbackItem item : mListeners) {
            if (item.listener.equals(listener)) {
                mListeners.remove(item);
                break;
            }
        }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent != null) {
            Logger.d(TAG, "Received update: " + intent.getAction());
            if (mListeners != null && mListeners.size() > 0) {
                for (CallbackItem callback : mListeners) {
                    if (callback.type.equals(intent.getType())) {
                        callback.listener.onReceivedData(intent);
                    }
                }
            } else {
                Logger.d(TAG, "No listener to send update to");
            }
        }
    }

    public interface DataReceiverCallbacks {
        void onReceivedData(Intent intent);
    }

    private static class CallbackItem {
        private DataReceiverCallbacks listener;
        private String type;

        public CallbackItem(DataReceiverCallbacks listener, String type) {
            this.listener = listener;
            this.type = type;
        }
    }

    /**
     * Used for lazy, thread-safe initiation.
     */
    private static class LazyHolder {

        private static DataReceiver instance = null;

        public static DataReceiver getInstance(DataReceiverCallbacks listener, String type) {
            return getInstance(listener, type, FLAG_NONE);
        }

        public static DataReceiver getInstance(int flags) {
            return getInstance(null, "", flags);
        }

        public static DataReceiver getInstance(DataReceiverCallbacks listener, String type, int flags) {
            if (instance == null) {
                instance = new DataReceiver(listener, type);
            } else if ((flags & FLAG_DO_NOT_REGISTER) != FLAG_DO_NOT_REGISTER) {
                instance.mListeners.add(new CallbackItem(listener, type));
                Logger.d(TAG, "Listener size: " + instance.mListeners.size());
            }
            return instance;
        }
    }

}
