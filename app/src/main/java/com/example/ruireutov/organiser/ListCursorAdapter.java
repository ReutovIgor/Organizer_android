package com.example.ruireutov.organiser;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ruireutov.organiser.DatabaseWorkers.DatabaseDefines;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

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
        TextView priority = view.findViewById(R.id.task_priority_icon);
        TextView taskTimeLabel = view.findViewById(R.id.task_time_label);
        TextView taskTime = view.findViewById(R.id.task_time_text);

        Resources resources = context.getResources();
        String iconName = cursor.getString( cursor.getColumnIndex(DatabaseDefines.TASK_LIST_CATEGORY_ICON) );
        categoryImage.setImageDrawable(resources.getDrawable(resources.getIdentifier(iconName, "drawable", context.getPackageName())));
        categoryImage.setBackgroundColor(Color.parseColor(cursor.getString( cursor.getColumnIndex(DatabaseDefines.TASK_LIST_CATEGORY_COLOR))));
        taskTitle.setText(cursor.getString( cursor.getColumnIndex(DatabaseDefines.TASK_LIST_NAME)));
        priority.setText(cursor.getString( cursor.getColumnIndex(DatabaseDefines.TASK_LIST_PRIORITY_MARK)));
        priority.setTextColor(Color.parseColor(cursor.getString( cursor.getColumnIndex(DatabaseDefines.TASK_LIST_PRIORITY_COLOR))));
        String startDateStr = cursor.getString( cursor.getColumnIndex(DatabaseDefines.TASK_LIST_START));
        String dueDateStr = cursor.getString( cursor.getColumnIndex(DatabaseDefines.TASK_LIST_END));
        if(dueDateStr.length() <= 0 ) {
            taskTimeLabel.setText(context.getResources().getString(R.string.task_list_lasts));
        }
        taskTime.setText(this.getDisplayTime(startDateStr, dueDateStr));
    }

    private String getDisplayTime(String startDate, String dueDate) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date;
        try {
            if(dueDate.length() > 0) {
                date = dateFormatter.parse(dueDate);
                return this.getTimeLeft(date);
            }else {
                date = dateFormatter.parse(startDate);
                return this.getTimeSpent(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
            return "";
        }
    }

    private String getTimeSpent(Date date) {
        Date currentDate = new Date ((Calendar.getInstance().getTime()).getTime());

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long difference =  currentDate.getTime() - date.getTime();

        if(difference / daysInMilli > 0) {
            return String.valueOf(difference / daysInMilli) + " days ";
        } else if( difference / hoursInMilli > 0 ) {
            return String.valueOf(difference / hoursInMilli) + "h";
        } else if( difference / minutesInMilli > 0 ) {
            return " less than a minute";
        } else {
            return "FOREVER";
        }
    }

    private String getTimeLeft(Date date) {
        String outputStr = SimpleDateFormat.getDateTimeInstance().format(date);
        Date currentDate = new Date ((Calendar.getInstance().getTime()).getTime());

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long difference =  date.getTime() - currentDate.getTime();

        String timeStr;
        if(difference / daysInMilli > 0) {
            timeStr = String.valueOf(difference / daysInMilli) + " days left";
        } else if( difference / hoursInMilli > 0 ) {
            timeStr = String.valueOf(difference / hoursInMilli) + "h left";
        } else if( difference / minutesInMilli <= 0 ) {
            timeStr = "Overdue";
        } else {
            timeStr = "Hurry";
        }
        return outputStr + "(" + timeStr + ")";
    }
}
