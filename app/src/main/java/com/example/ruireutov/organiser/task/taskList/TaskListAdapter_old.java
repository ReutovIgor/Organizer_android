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

public class TaskListAdapter_old extends CursorAdapter {
    private LayoutInflater layoutInflater;
    private ArrayList<ATaskView> taskViews;
    private int lastActive;


    TaskListAdapter_old(Context context, Cursor cursor, int flags) {
        super(context, cursor, flags);
        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.taskViews = new ArrayList<>();
        this.lastActive = -1;
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

    }

    @Override
    public Cursor swapCursor(Cursor newCursor) {
        this.taskViews.clear();
        this.lastActive = -1;
        return super.swapCursor(newCursor);
    }

    private void collapseLastActive(int pos) {
        if(this.lastActive != -1 && this.lastActive != pos) {
            this.taskViews.get(this.lastActive).hideDetails();
        }
        this.lastActive = pos;
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
                taskViews.get(this.pos).onCLick();
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
            if(taskViews.size() < this.pos) {
                return false;
            }
            switch ( event.getAction() ) {
            case MotionEvent.ACTION_DOWN:
                collapseLastActive(this.pos);
                this.startX = event.getRawX();
                return true;
            case MotionEvent.ACTION_MOVE:

                float newX = event.getRawX();
                float dx = newX - this.startX;
                if(Math.abs(dx) > 20) {
                    v.getParent().requestDisallowInterceptTouchEvent(true);
                }
                Log.d("Task List element", "Touch event StartX: " + this.startX);
                Log.d("Task List element", "Touch event NewX" + newX);
                return taskViews.get(this.pos).onTouchMove(dx);
            case MotionEvent.ACTION_UP:
                if(!taskViews.get(this.pos).onTouchEnd()) {
                    if(event.getEventTime() - event.getDownTime() > 1000) {
                        v.performLongClick();
                    } else {
                        v.performClick();
                    }
                } else {
                    v.getParent().requestDisallowInterceptTouchEvent(false);
                }
                return true;
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
            if(taskViews.size() > this.pos) {
                taskViews.get(this.pos).onLongClick();
            }
            return true;
        }
    }
}
