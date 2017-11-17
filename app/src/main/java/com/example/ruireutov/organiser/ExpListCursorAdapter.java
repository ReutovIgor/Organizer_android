package com.example.ruireutov.organiser;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorTreeAdapter;
import android.widget.TextView;

import java.util.HashMap;

/**
 * Created by ruireutov on 16-Nov-17.
 */

public class ExpListCursorAdapter extends CursorTreeAdapter {
    private Context context;
    private Cursor groups;
    private HashMap<String, Cursor> groupItems;
    private LayoutInflater layoutInflater;

    public ExpListCursorAdapter (Cursor groups, Context context) {
        super(groups, context);
        this.context = context;
        this.groups = groups;
        layoutInflater = (LayoutInflater) context.getSystemService(
                Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    protected Cursor getChildrenCursor(Cursor groupCursor) {
        return null;
    }

    @Override
    protected View newGroupView(Context context, Cursor cursor, boolean isExpanded, ViewGroup parent) {
        View view = this.layoutInflater.inflate(R.layout.main_list_group_view, parent, false);
        return view;
    }

    @Override
    protected void bindGroupView(View view, Context context, Cursor cursor, boolean isExpanded) {
        TextView textView = (TextView) view.findViewById(R.id.mainListGroupName);
        String title = "Group title";
        textView.setText(title);
    }

    @Override
    protected View newChildView(Context context, Cursor cursor, boolean isLastChild, ViewGroup parent) {
        View view = this.layoutInflater.inflate(R.layout.main_list_child_view, parent, false);
        return view;
    }

    @Override
    protected void bindChildView(View view, Context context, Cursor cursor, boolean isLastChild) {
        TextView textView = (TextView) view.findViewById(R.id.mainListGroupChild);
        String title = "Child element";
        textView.setText(title);
    }


}
