package com.example.ruireutov.organiser.task.taskList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ruireutov.organiser.databaseWorkers.DatabaseDefines;
import com.example.ruireutov.organiser.R;
import com.example.ruireutov.organiser.task.taskList.taskViews.ATaskView;
import com.example.ruireutov.organiser.task.taskList.taskViews.ScheduledTaskView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TaskListAdapter extends CursorAdapter {
    private LayoutInflater layoutInflater;
    private int viewCount;
    private ArrayList<ATaskView> taskViews;


    TaskListAdapter(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.taskViews = new ArrayList<>();
        this.viewCount = 0;
    }
    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view = this.layoutInflater.inflate(R.layout.task_list_child_view, parent, false);

        view.setOnTouchListener(new TaskViewTouchListeners(parent.getChildCount()));
        view.setOnClickListener(new TaskViewClickListeners(parent.getChildCount()));
        view.setOnLongClickListener(new TaskViewLongClickListeners(parent.getChildCount()));

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        this.taskViews.add(new ScheduledTaskView(view, context, cursor));
//        ImageView priorityIndication = view.findViewById(R.id.task_priority_indication);
//        ImageView categoryImage = view.findViewById(R.id.task_category_icon);
//        TextView taskTitle = view.findViewById(R.id.task_title_text);
//        TextView taskTimeLabel = view.findViewById(R.id.task_time_label);
//        TextView taskTime = view.findViewById(R.id.task_time_text);
//
//        Resources resources = context.getResources();
//        String iconName = cursor.getString( cursor.getColumnIndex(DatabaseDefines.TASK_LIST_CATEGORY_ICON) );
//        categoryImage.setImageDrawable(resources.getDrawable(resources.getIdentifier(iconName, "drawable", context.getPackageName())));
//        categoryImage.setBackgroundColor(Color.parseColor(cursor.getString( cursor.getColumnIndex(DatabaseDefines.TASK_LIST_CATEGORY_COLOR))));
//        taskTitle.setText(cursor.getString( cursor.getColumnIndex(DatabaseDefines.TASK_LIST_NAME)));
//        String priorityStr = cursor.getString( cursor.getColumnIndex(DatabaseDefines.TASK_LIST_PRIORITY) );
//
//        switch (priorityStr) {
//            case "High":
//                priorityIndication.setBackgroundColor(context.getResources().getColor(R.color.highPriority));
//                //priorityIndication.setBackground(context.getDrawable(R.drawable.high_priority_item_background));
//                break;
//            case "Normal":
//                priorityIndication.setBackgroundColor(context.getResources().getColor(R.color.normalPriority));
//                //priorityIndication.setBackground(context.getDrawable(R.drawable.normal_priority_item_background));
//                break;
//            case "Low":
//                priorityIndication.setBackgroundColor(context.getResources().getColor(R.color.lowPriority));
//                //priorityIndication.setBackground(context.getDrawable(R.drawable.low_priority_item_background));
//                break;
//        }
//        String startDateStr = cursor.getString( cursor.getColumnIndex(DatabaseDefines.TASK_LIST_START));
//        String dueDateStr = cursor.getString( cursor.getColumnIndex(DatabaseDefines.TASK_LIST_END));
//        if(dueDateStr.length() <= 0 ) {
//            taskTimeLabel.setText(context.getResources().getString(R.string.task_list_lasts));
//        }
//        taskTime.setText(this.getDisplayTime(startDateStr, dueDateStr));
//
//        view.setOnTouchListener(new View.OnTouchListener() {
//            private boolean moved;
//            private float startX;
//            @Override
//            public boolean onTouch(View v, MotionEvent event) {
//                Log.d("Task List element", "Touch event received" + event.getAction());
//                float x;
//
//                //DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
//                //Math.round(width / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT))
//                View taskView = v.findViewById(R.id.task_layout);
//                View deleteTaskView = v.findViewById(R.id.delete_task_tab);
//                float width = deleteTaskView.getWidth();
//                float max = width;
//                float norm = 0;
//                float min = -1 * width;
//
//
//
//                switch ( event.getAction() ) {
//                    case MotionEvent.ACTION_DOWN:
//                        //this.startX = event.getX();
//                        this.moved = false;
//                        return false;
//                    case MotionEvent.ACTION_MOVE:
//                        this.moved = true;
////                        x = event.getX();
////                        float translationX = taskView.getTranslationX() + (x - this.startX);
////                        if(translationX <= 0) {
////                            translationX = 0;
////                        } else if(translationX >= 40) {
////                            translationX = 40;
////                        }
////                        taskView.setTranslationX(max);
////                        deleteTaskView.setTranslationX(norm);
//                        return true;
//                    case MotionEvent.ACTION_UP:
////                        taskView.setTranslationX(norm);
////                        deleteTaskView.setTranslationX(min);
//                        return this.moved;
//                    case MotionEvent.ACTION_CANCEL:
////                        taskView.setTranslationX(norm);
////                        deleteTaskView.setTranslationX(min);
//                        return this.moved;
//                }
//                return false;
//            }
//        });
//
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                String a = "";
//                Log.d("Task List element", "Click received");
//            }
//        });
//
//        view.setOnLongClickListener(new View.OnLongClickListener() {
//            @Override
//            public boolean onLongClick(View v) {
//                Log.d("Task List element", "LongClick received");
//                return true;
//            }
//        });
    }

    @Override
    public Cursor swapCursor(Cursor newCursor) {
        this.taskViews.clear();
        return super.swapCursor(newCursor);
    }

    private String getDisplayTime(String startDate, String dueDate) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-DD HH:mm");
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
        return outputStr + " (" + timeStr + ")";
    }

    private class TaskViewClickListeners implements View.OnClickListener{
        private int pos;

        TaskViewClickListeners (int pos) {
            this.pos = pos;
        }

        @Override
        public void onClick(View v) {
            if(taskViews.size() > this.pos) {

            }
        }
    }

    private class TaskViewTouchListeners implements View.OnTouchListener{
        private int pos;
        private float startX;

        TaskViewTouchListeners (int pos) {
            this.pos = pos;
        }

        @Override
        public boolean onTouch(View v, MotionEvent event) {
            Log.d("Task List element", "Touch event received" + event.getAction());
            ViewParent parent = v.getParent();
            if(taskViews.size() < this.pos) {
                return false;
            }
            switch ( event.getAction() ) {
            case MotionEvent.ACTION_DOWN:
                this.startX = event.getX();
                return true;
            case MotionEvent.ACTION_MOVE:
                float newX = event.getX();
                float dx = newX - this.startX;
                if(Math.abs(dx) > 10) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                }
                return taskViews.get(this.pos).onTouchMove(dx);
            case MotionEvent.ACTION_UP:
                taskViews.get(this.pos).onTouchEnd();
                v.getParent().requestDisallowInterceptTouchEvent(false);
                return taskViews.get(this.pos).onTouchEnd();
            case MotionEvent.ACTION_CANCEL:
                taskViews.get(this.pos).onTouchEnd();
                v.getParent().requestDisallowInterceptTouchEvent(false);
                return taskViews.get(this.pos).onTouchEnd();
            default:
                return false;
            }
        }
    }

    private class TaskViewLongClickListeners implements View.OnLongClickListener{
        private int pos;

        TaskViewLongClickListeners (int pos) {
            this.pos = pos;
        }

        @Override
        public boolean onLongClick(View v) {
            return taskViews.size() > this.pos && taskViews.get(this.pos).onLongClick();
        }
    }
}
