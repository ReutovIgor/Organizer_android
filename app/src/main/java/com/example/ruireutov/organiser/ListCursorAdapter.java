package com.example.ruireutov.organiser;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.ruireutov.organiser.DatabaseWorkers.DatabaseDefines;

import java.util.HashMap;

/**
 * Created by ruireutov on 29-Nov-17.
 */

public class ListCursorAdapter extends CursorAdapter {
    //private Context context;
    private LayoutInflater layoutInflater;

    public ListCursorAdapter (Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
        //this.context = context;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = this.layoutInflater.inflate(R.layout.to_do_list_child_view, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        int count = cursor.getPosition();

        TextView textView = (TextView) view.findViewById(R.id.textView);
        String title = cursor.getString( cursor.getColumnIndex(DatabaseDefines.TODO_LIST_NAME) );
        textView.setText(title);
        cursor.moveToNext();
    }
}
