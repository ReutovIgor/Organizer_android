package com.example.ruireutov.organiser;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.example.ruireutov.organiser.DatabaseWorkers.DatabaseDefines;

/**
 * Created by ruireutov on 08-Dec-17.
 */

public class SpinnerAdapter extends CursorAdapter {
    public static final String TYPE_CATEGORY = "Category";
    public static final String TYPE_PRIORITY = "Priority";
    private Context context;
    private LayoutInflater layoutInflater;
    private String type;

    public SpinnerAdapter(@NonNull Context context, Cursor cursor, int flags, String type) {
        super(context, cursor, flags);

        this.context = context;
        this.type = type;
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public Object getItem(int position) {
        Cursor c = this.getCursor();
        c.moveToPosition(position);
        String nameKey = this.type == TYPE_CATEGORY ? DatabaseDefines.CATEGORIES_NAME : DatabaseDefines.PRIORITIES_NAME;
        return c.getString(c.getColumnIndex(nameKey));
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = this.layoutInflater.inflate(R.layout.task_details_drop_down_item, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView textView = view.findViewById(R.id.task_detail_name);
        Button colorView = view.findViewById(R.id.task_detail_color);
        String titleKey = "", colorKey = "";
        switch(this.type) {
            case TYPE_CATEGORY:
                titleKey = DatabaseDefines.CATEGORIES_NAME;
                colorKey = DatabaseDefines.CATEGORIES_COLOR;
                break;
            case TYPE_PRIORITY:
                titleKey = DatabaseDefines.PRIORITIES_NAME;
                colorKey = DatabaseDefines.PRIORITIES_COLOR;
                break;
        }
        String title = cursor.getString( cursor.getColumnIndex(titleKey) );
        textView.setText(title);
        colorView.setBackgroundColor(Color.parseColor(cursor.getString( cursor.getColumnIndex(colorKey) )));
    }
}
