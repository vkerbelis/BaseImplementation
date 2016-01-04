package com.telesoftas.retrofitimpl.data;

import android.app.IntentService;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;

import com.telesoftas.retrofitimpl.data.repositories.AbstractRepository;
import com.telesoftas.retrofitimpl.data.repositories.RepositoryEntity;
import com.telesoftas.retrofitimpl.data.repositories.SurveyRepository;
import com.telesoftas.retrofitimpl.utils.Logger;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 *
 * @author Vidmantas Kerbelis (vkerbelis@yahoo.com) on 2015-10-19.
 */
public class DatabaseService extends IntentService {

    public static final String ACTION_PUT = "com.telesoftas.ormimpl.data.action.PUT";
    public static final String ACTION_PUT_SURVEY = "com.telesoftas.ormimpl.data.action.PUT_SURVEY";
    public static final String EXTRA_PARCELABLE = "com.telesoftas.ormimpl.data.extra.PARCELABLE";
    public static final String EXTRA_BROADCAST = "com.telesoftas.ormimpl.data.extra.BROADCAST";
    public static final String EXTRA_ID = "com.telesoftas.ormimpl.data.extra.ID";
    public static final String TYPE_ACCOUNT = "com.telesoftas.ormimpl.type/ACCOUNT";
    public static final String TYPE_SOME = "com.telesoftas.ormimpl.type/SOME";

    private static final String TAG = Logger.makeLogTag(DatabaseService.class);
    private static final int MAX_RETRIES = 5;
    private static final long RETRY_TIME = 200;

    public DatabaseService() {
        super("DatabaseService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            final String type = intent.getType();
            switch (action) {
                case ACTION_PUT_SURVEY:
                    executeActionPut(intent, new SurveyRepository(this, true));
                    intent.setAction(ACTION_PUT);
                    break;
            }
            sendRetryBroadcast(intent, action, type);
        }
    }

    private void executeActionPut(Intent intent, AbstractRepository<? extends RepositoryEntity> repository) {
        RepositoryEntity item = intent.getParcelableExtra(EXTRA_PARCELABLE);
        item.setId(repository.create(item));
        intent.setAction(ACTION_PUT);
    }

    private void sendRetryBroadcast(Intent intent, String action, String type) {
        boolean broadcast = intent.getBooleanExtra(EXTRA_BROADCAST, true);
        if (broadcast) {
            int retryCount = 0;
            long retryTime = RETRY_TIME;
            while (retryCount < MAX_RETRIES) {
                boolean listenerRegistered = DataReceiver.isAnyListenerRegistered();
                if (listenerRegistered) {
                    Logger.d(TAG, "Sending update for: " + action + ", type: " + type);
                    LocalBroadcastManager.getInstance(this).sendBroadcast(intent);
                    break;
                } else {
                    retryCount++;
                    Logger.d(TAG, "No listener is attached, retrying to send broadcast" +
                            " in: " + retryTime + "ms [" + retryCount + "/" + MAX_RETRIES + "]");
                    try {
                        Thread.sleep(retryTime);
                        retryTime *= 2;
                    } catch (InterruptedException cause) {
                        Logger.e(TAG, "", cause);
                    }
                }
            }
        }
    }

}
