package com.telesoftas.retrofitimpl.data.repositories;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.provider.BaseColumns;

/**
 * @author Vidmantas Kerbelis (vkerbelis@yahoo.com) on 2015-12-22.
 */
public abstract class RepositoryEntity implements Parcelable, BaseColumns {

    protected static final int INDEX_ID = 0;
    private long id;

    public RepositoryEntity() {
        this.id = -1;
    }

    public RepositoryEntity(Parcel in) {
        this.id = in.readLong();
    }

    public RepositoryEntity(Cursor cursor) {
        this.id = cursor.getLong(INDEX_ID);
    }

    protected abstract ContentValues extractContentValues();

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
