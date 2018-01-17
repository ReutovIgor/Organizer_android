package com.example.ruireutov.organiser;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ruireutov.organiser.DatabaseWorkers.DatabaseDefines;

public class ListCursorAdapter extends CursorAdapter {
    private LayoutInflater layoutInflater;

    public ListCursorAdapter (Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = this.layoutInflater.inflate(R.layout.task_list_child_view, parent, false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ImageView categoryImage = view.findViewById(R.id.task_category_icon);
        TextView taskTitle = view.findViewById(R.id.task_title_text);
        TextView priority = view.findViewById(R.id.task_priority_icon);//TODO: change to image in future
        TextView dueDate = view.findViewById(R.id.due_time_text);
        TextView timeLeft = view.findViewById(R.id.time_left_text);

        Resources resources = context.getResources();
        String iconName = cursor.getString( cursor.getColumnIndex(DatabaseDefines.TASK_LIST_CATEGORY_ICON) );
        categoryImage.setImageDrawable(resources.getDrawable(resources.getIdentifier(iconName, "drawable", context.getPackageName())));
        categoryImage.setBackgroundColor(Color.parseColor(cursor.getString( cursor.getColumnIndex(DatabaseDefines.TASK_LIST_CATEGORY_COLOR))));

        taskTitle.setText(cursor.getString( cursor.getColumnIndex(DatabaseDefines.TASK_LIST_NAME)));
        dueDate.setText(cursor.getString( cursor.getColumnIndex(DatabaseDefines.TASK_LIST_END)));
        priority.setText(cursor.getString( cursor.getColumnIndex(DatabaseDefines.TASK_LIST_PRIORITY_MARK)));
        priority.setTextColor(Color.parseColor(cursor.getString( cursor.getColumnIndex(DatabaseDefines.TASK_LIST_PRIORITY_COLOR))));
        timeLeft.setText("12h");
    }
}
