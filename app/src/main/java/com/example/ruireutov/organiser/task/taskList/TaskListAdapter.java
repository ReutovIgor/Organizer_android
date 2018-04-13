package com.example.ruireutov.organiser.task.taskList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ruireutov.organiser.R;
import com.example.ruireutov.organiser.databaseWorkers.DatabaseDefines;
import com.example.ruireutov.organiser.task.main.TaskActivity;
import com.example.ruireutov.organiser.task.taskList.taskViews.ATaskView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolder>{
    private Context context;
    private LayoutInflater layoutInflater;
    private ArrayList<ATaskView> taskViews;
    private Cursor listData;

    TaskListAdapter(Cursor cursor, Context context) {
        this.context = context;
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.taskViews = new ArrayList<>();
        this.listData = cursor;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private CardView taskCard;

        private View taskLayout;
        private View completeTaskLayout;
        private View taskDetailsLayout;

        private ImageView priority;
        private ImageView category;
        private TextView taskTitle;
        private TextView taskStatus;
        private TextView taskInfo;
        private TextView taskDetails;
        private CheckBox completeTask;


        ViewHolder(View itemView) {
            super(itemView);
            this.taskCard = (CardView) itemView;

            this.taskLayout = itemView.findViewById(R.id.task_layout);
            this.completeTaskLayout = itemView.findViewById(R.id.complete_task_layout);
            this.taskDetailsLayout = itemView.findViewById(R.id.task_details_layout);

            this.priority = this.taskLayout.findViewById(R.id.task_priority_indication);
            this.category = this.taskLayout.findViewById(R.id.task_category_icon);
            this.taskTitle = this.taskLayout.findViewById(R.id.task_title_text);
            this.taskStatus = this.taskLayout.findViewById(R.id.task_status_text);
            this.taskInfo = this.taskLayout.findViewById(R.id.task_info_text);

            this.completeTask = this.completeTaskLayout.findViewById(R.id.complete_task_button);

            this.taskDetails = this.taskDetailsLayout.findViewById(R.id.task_description_text);
        }

        void fillView(Cursor cursor, Context context) {
            Resources resources = context.getResources();
            String priorityStr = cursor.getString( cursor.getColumnIndex(DatabaseDefines.TASK_LIST_PRIORITY) );
            switch (priorityStr) {
                case "High":
                    this.priority.setBackgroundColor(resources.getColor(R.color.highPriority));
                    break;
                case "Normal":
                    this.priority.setBackgroundColor(resources.getColor(R.color.normalPriority));
                    break;
                case "Low":
                    this.priority.setBackgroundColor(resources.getColor(R.color.lowPriority));
                    break;
            }
            String iconName = cursor.getString( cursor.getColumnIndex(DatabaseDefines.TASK_LIST_CATEGORY_ICON) );
            //this.category.setImageDrawable(resources.getDrawable(resources.getIdentifier(iconName, "drawable", TaskActivity.PACKAGE_NAME)));
            this.category.setImageDrawable(ResourcesCompat.getDrawable(resources, resources.getIdentifier(iconName, "drawable", TaskActivity.PACKAGE_NAME), null));
            this.category.setBackgroundColor(Color.parseColor(cursor.getString( cursor.getColumnIndex(DatabaseDefines.TASK_LIST_CATEGORY_COLOR))));
            this.taskTitle.setText(cursor.getString( cursor.getColumnIndex(DatabaseDefines.TASK_LIST_NAME)));
            this.taskDetails.setText(cursor.getString(cursor.getColumnIndex(DatabaseDefines.TASK_LIST_DETAILS)));

            int type = cursor.getInt(cursor.getColumnIndex(DatabaseDefines.TASK_LIST_TYPE));
            switch (type) {
                case DatabaseDefines.TASK_TYPE_DEFAULT:
                    this.fillDefaultView(cursor, resources);
                    break;
                case  DatabaseDefines.TASK_TYPE_SCHEDULED:
                    this.fillScheduledView(cursor, resources);
                    break;
            }
        }

        private void fillDefaultView(Cursor cursor, Resources res) {
            String dateStr = cursor.getString(cursor.getColumnIndex(DatabaseDefines.TASK_LIST_START));
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dateFormatter = new SimpleDateFormat(DatabaseDefines.DB_DATE_TIME_FORMAT);
            Date date;
            try {
                date = dateFormatter.parse(dateStr);
                //get formatted task due date
                StringBuilder taskInfoStr = new StringBuilder(res.getString(R.string.task_list_created));
                taskInfoStr.append(SimpleDateFormat.getDateTimeInstance().format(date));

                Date currentDate = new Date ((Calendar.getInstance().getTime()).getTime());

                long secondsInMilli = 1000;
                long minutesInMilli = secondsInMilli * 60;
                long hoursInMilli = minutesInMilli * 60;
                long daysInMilli = hoursInMilli * 24;

                long difference =  currentDate.getTime() - date.getTime();

                //get task status
                StringBuilder taskStatusStr = new StringBuilder();
                if(difference / daysInMilli > 0) {
                    taskStatusStr.append(res.getString(R.string.task_list_lasts));
                    taskStatusStr.append(String.valueOf(difference / daysInMilli));
                    taskStatusStr.append(" days");
                } else if( difference / hoursInMilli > 0 ) {
                    taskStatusStr.append(res.getString(R.string.task_list_lasts));
                    taskStatusStr.append(String.valueOf(difference / hoursInMilli));
                    taskStatusStr.append(" hours");
                } else {
                    taskInfoStr.append(res.getString(R.string.task_list_expiring));
                }

                this.taskInfo.setText(taskInfoStr.toString());
                this.taskStatus.setText(taskStatusStr.toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        private void fillScheduledView(Cursor cursor, Resources res) {
            String dateStr = cursor.getString(cursor.getColumnIndex(DatabaseDefines.TASK_LIST_END));
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat dateFormatter = new SimpleDateFormat(DatabaseDefines.DB_DATE_TIME_FORMAT);
            Date date;
            try {
                date = dateFormatter.parse(dateStr);
                //get formatted task due date
                StringBuilder taskInfoStr = new StringBuilder(res.getString(R.string.task_list_due));
                taskInfoStr.append(SimpleDateFormat.getDateTimeInstance().format(date));

                Date currentDate = new Date ((Calendar.getInstance().getTime()).getTime());

                long secondsInMilli = 1000;
                long minutesInMilli = secondsInMilli * 60;
                long hoursInMilli = minutesInMilli * 60;
                long daysInMilli = hoursInMilli * 24;

                long difference =  date.getTime() - currentDate.getTime();

                //get task status
                StringBuilder taskStatusStr = new StringBuilder();
                if(difference / daysInMilli > 0) {
                    taskStatusStr.append(res.getString(R.string.task_list_left));
                    taskStatusStr.append(String.valueOf(difference / daysInMilli));
                    taskStatusStr.append(" days");
                } else if( difference / hoursInMilli > 0 ) {
                    taskStatusStr.append(res.getString(R.string.task_list_left));
                    taskStatusStr.append(String.valueOf(difference / hoursInMilli));
                    taskStatusStr.append(" hours");
                } else if( difference / minutesInMilli <= 0 ) {
                    taskInfoStr.append(res.getString(R.string.task_list_overdue));
                } else {
                    taskInfoStr.append(res.getString(R.string.task_list_expiring));
                }

                this.taskInfo.setText(taskInfoStr.toString());
                this.taskStatus.setText(taskStatusStr.toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        public  void onClick() {
            if(this.taskDetails.getText().length() > 0) {
                this.taskDetails.setVisibility(View.VISIBLE);
            }
        }

        public void onLongClick() {

        }

        public void onSwipe(float deltaX) {

        }

        public void onRelease() {

        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = this.layoutInflater.inflate(R.layout.task_list_child_view, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(this.listData.moveToPosition(position)) {
            holder.fillView(this.listData, context);
        }
    }

    @Override
    public int getItemCount() {
        if(this.listData == null) {
            return 0;
        }
        return this.listData.getCount();
    }

    public void setListData(Cursor c) {
        if(this.listData != null) {
            this.listData.close();
        }
        this.listData = c;
        this.notifyDataSetChanged();
    }
}
