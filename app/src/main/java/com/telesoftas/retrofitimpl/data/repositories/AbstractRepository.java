package com.telesoftas.retrofitimpl.data.repositories;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;

import com.telesoftas.retrofitimpl.data.SQLiteHelper;
import com.telesoftas.retrofitimpl.utils.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Vidmantas Kerbelis (vkerbelis@yahoo.com) on 2015-12-22.
 */
public abstract class AbstractRepository<T extends RepositoryEntity> {

    static final String PLACEHOLDER = " = ?";
    static final String OR = " OR ";
    static final String AND = " AND ";
    static final String ASCENDING = " ASC ";
    private static final int CURSOR_MOVEMENT_FIRST = 0;
    private static final int CURSOR_MOVEMENT_NEXT = 1;
    private static final int CURSOR_MOVEMENT_LAST = 2;
    final Context mContext;
    private final String TAG = Logger.makeLogTag(this.getClass());
    private final SQLiteHelper mHelper;
    private final boolean mWritable;
    SQLiteDatabase mDatabase;

    AbstractRepository(Context context, boolean writable) {
        mContext = context.getApplicationContext();
        mHelper = SQLiteHelper.getInstance(mContext);
        mWritable = writable;
        openDatabase();
    }

    final void openDatabase() {
        if (mDatabase == null) {
            synchronized (mHelper) {
                if (mWritable) {
                    mDatabase = mHelper.getWritableDatabase();
                } else {
                    mDatabase = mHelper.getReadableDatabase();
                }
            }
        }
    }

    public long create(RepositoryEntity ent) {
        long id = -1;
        if (ent == null) {
            Logger.e(TAG, "Tried to insert a null object");
            return id;
        }
        ContentValues values = ent.extractContentValues();
        try {
            id = mDatabase.insertOrThrow(tableName(), null, values);
            Logger.i(TAG, "Inserted " + ent.getClass().getSimpleName() + ", with id: " + id);
        } catch (SQLiteException cause) {
            Logger.e(TAG, "Failed to insert " + ent.getClass().getSimpleName() + ", with values: " +
                    values, cause);
        }
        return id;
    }

    public List<T> list() {
        List<T> list = new ArrayList<>();
        Cursor cursor = mDatabase.query(tableName(), null, null, null, null, null, null);
        try {
            if (cursor != null && cursor.getCount() > 0) {
                for (int i = 0; i < cursor.getCount(); i++) {
                    list.add(nextItem(cursor));
                }
                return list;
            }
        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return list;
    }

    T firstItem(Cursor cursor) {
        return moveCursor(cursor, CURSOR_MOVEMENT_FIRST);
    }

    T nextItem(Cursor cursor) {
        return moveCursor(cursor, CURSOR_MOVEMENT_NEXT);
    }

    private T moveCursor(Cursor cursor, int movement) {
        T ent = null;
        if (cursor != null) {
            try {
                switch (movement) {
                    case CURSOR_MOVEMENT_FIRST:
                        cursor.moveToFirst();
                        break;
                    case CURSOR_MOVEMENT_LAST:
                        cursor.moveToLast();
                        break;
                    default:
                        cursor.moveToNext();
                        break;
                }
                if (cursor.getCount() != 0) {
                    ent = cursorToObject(cursor);
                }
            } finally {
                if (movement == CURSOR_MOVEMENT_LAST || movement == CURSOR_MOVEMENT_FIRST) {
                    cursor.close();
                }
            }
            return ent;
        } else {
            return null;
        }

    }

    public abstract String tableName();

    protected abstract T cursorToObject(Cursor cursor);
}
