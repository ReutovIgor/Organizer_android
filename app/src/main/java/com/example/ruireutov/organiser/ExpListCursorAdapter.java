package com.example.ruireutov.organiser;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorTreeAdapter;

import java.util.HashMap;

/**
 * Created by ruireutov on 16-Nov-17.
 */

public class ExpListCursorAdapter extends CursorTreeAdapter {
    Context context;
    Cursor groups;
    HashMap<String, Cursor> groupItems;

    public ExpListCursorAdapter (Cursor groups, Context context) {
        super(groups, context);
        this.context = context;
        this.groups = groups;
    }

    @Override
    protected Cursor getChildrenCursor(Cursor groupCursor) {
        return null;
    }

    @Override
    protected View newGroupView(Context context, Cursor cursor, boolean isExpanded, ViewGroup parent) {
        return null;
    }

    @Override
    protected void bindGroupView(View view, Context context, Cursor cursor, boolean isExpanded) {

    }

    @Override
    protected View newChildView(Context context, Cursor cursor, boolean isLastChild, ViewGroup parent) {
        return null;
    }

    @Override
    protected void bindChildView(View view, Context context, Cursor cursor, boolean isLastChild) {

    }


}
