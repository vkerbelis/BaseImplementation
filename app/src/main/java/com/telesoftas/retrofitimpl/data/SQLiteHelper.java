package com.telesoftas.retrofitimpl.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.telesoftas.retrofitimpl.data.entities.Survey;
import com.telesoftas.retrofitimpl.utils.Logger;

/**
 * @author Vidmantas Kerbelis (vkerbelis@yahoo.com) on 2015-12-22.
 */
public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String CREATE_TABLE = "CREATE TABLE IF NOT EXISTS ";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS ";
    private static final String PRIMARY_KEY = " PRIMARY KEY";
    private static final String COMMA_SEP = ",";
    private static final String TYPE_TEXT = " TEXT";
    private static final String TYPE_INTEGER = " INTEGER";

    private static final String TAG = Logger.makeLogTag(SQLiteHelper.class);
    private static final String DATABASE_NAME = "retrofit.db";
    private static final int DATABASE_VERSION = 1;

    private static final String SQL_CREATE_TABLE_SURVEYS =
            CREATE_TABLE + Survey.TABLE_NAME + " (" +
                    Survey._ID + TYPE_INTEGER + PRIMARY_KEY + COMMA_SEP +
                    Survey.COLUMN_TITLE + TYPE_TEXT + ")";

    private static final String SQL_DELETE_TABLE_SURVEYS =
            DROP_TABLE + Survey.TABLE_NAME;

    private SQLiteHelper(Context context, String database) {
        super(context, database, null, DATABASE_VERSION);
    }

    public static synchronized SQLiteHelper getInstance(Context context) {
        return getInstance(context, DATABASE_NAME);
    }

    public static synchronized SQLiteHelper getInstance(Context context, String database) {
        return LazyHolder.getInstance(context, database);
    }

    @Override
    public void onCreate(SQLiteDatabase database) {
        Logger.i(TAG, "Creating database, version: " + DATABASE_VERSION);
        database.execSQL(SQL_CREATE_TABLE_SURVEYS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
        Logger.w(TAG, "Upgrading database from version " +
                oldVersion + " to " + newVersion);
        tearDown(database);
        onCreate(database);
    }

    public void tearDown(SQLiteDatabase database) {
        Logger.w(TAG, "Tearing down database.");
        database.execSQL(SQL_DELETE_TABLE_SURVEYS);
    }

    /**
     * Uses application context so as not to accidentally leak an Activities context.
     */
    private static class LazyHolder {
        private static SQLiteHelper instance = null;

        private static SQLiteHelper getInstance(Context context, String database) {
            if (instance == null) {
                instance = new SQLiteHelper(context.getApplicationContext(), database);
            }
            return instance;
        }
    }

}
