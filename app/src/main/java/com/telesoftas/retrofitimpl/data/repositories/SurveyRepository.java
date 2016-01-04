package com.telesoftas.retrofitimpl.data.repositories;

import android.content.Context;
import android.database.Cursor;

import com.telesoftas.retrofitimpl.data.entities.Survey;

/**
 * @author Vidmantas Kerbelis (vkerbelis@yahoo.com) on 2016-01-04.
 */
public class SurveyRepository extends AbstractRepository<Survey> {

    public SurveyRepository(Context context, boolean writable) {
        super(context, writable);
    }

    @Override
    public String tableName() {
        return Survey.TABLE_NAME;
    }

    @Override
    protected Survey cursorToObject(Cursor cursor) {
        return null;
    }

}
