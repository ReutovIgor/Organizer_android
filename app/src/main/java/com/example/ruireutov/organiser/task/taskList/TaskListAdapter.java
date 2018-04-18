package com.example.ruireutov.organiser.task.taskList;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.support.constraint.ConstraintLayout;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ruireutov.organiser.R;
import com.example.ruireutov.organiser.databaseWorkers.DatabaseDefines;
import com.example.ruireutov.organiser.task.TaskDetailsData;
import com.example.ruireutov.organiser.task.main.TaskActivity;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TaskListAdapter extends RecyclerView.Adapter<TaskListAdapter.ViewHolder>{

    private LayoutInflater layoutInflater;
    private Cursor listData;

    private float maxLeft;
    private float maxRight;

    TaskListAdapter(Cursor cursor, Context context) {
        this.layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        this.listData = cursor;

        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        this.maxLeft = -40 * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        this.maxRight = 40 * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private ConstraintLayout taskView;

        private View taskLayout;
        private View deleteTaskLayout;
        private View completeTaskLayout;

        private ImageView priority;
        private ImageView category;
        private TextView taskTitle;
        private TextView taskStatus;
        private TextView taskInfo;

        private float maxLeft;
        private float maxRight;

        ViewHolder(View itemView, float maxLeft, float maxRight) {
            super(itemView);
            this.taskView = (ConstraintLayout) itemView;

            this.taskLayout = itemView.findViewById(R.id.task_layout);
            this.deleteTaskLayout = itemView.findViewById(R.id.delete_task_layout);
            this.completeTaskLayout = itemView.findViewById(R.id.complete_task_layout);

            this.priority = this.taskLayout.findViewById(R.id.task_priority_indication);
            this.category = this.taskLayout.findViewById(R.id.task_category_icon);
            this.taskTitle = this.taskLayout.findViewById(R.id.task_title_text);
            this.taskStatus = this.taskLayout.findViewById(R.id.task_status_text);
            this.taskInfo = this.taskLayout.findViewById(R.id.task_info_text);

            this.maxLeft = maxLeft;
            this.maxRight = maxRight;
        }

        void fillView(Cursor cursor) {
            Resources resources = this.taskView.getContext().getResources();
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
            this.category.setImageDrawable(ResourcesCompat.getDrawable(resources, resources.getIdentifier(iconName, "drawable", TaskActivity.PACKAGE_NAME), null));
            this.category.setBackgroundColor(Color.parseColor(cursor.getString( cursor.getColumnIndex(DatabaseDefines.TASK_LIST_CATEGORY_COLOR))));
            this.taskTitle.setText(cursor.getString( cursor.getColumnIndex(DatabaseDefines.TASK_LIST_NAME)));

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


                StringBuilder taskStatusStr = new StringBuilder();
                int status = cursor.getInt(cursor.getColumnIndex(DatabaseDefines.TASK_LIST_STATUS));
                switch (status) {
                    case DatabaseDefines.TASK_STATUS_IN_PROGRESS:
                        long secondsInMilli = 1000;
                        long minutesInMilli = secondsInMilli * 60;
                        long hoursInMilli = minutesInMilli * 60;
                        long daysInMilli = hoursInMilli * 24;

                        long difference =  date.getTime() - currentDate.getTime();

                        if(difference / daysInMilli > 0) {
                            taskStatusStr.append(res.getString(R.string.task_list_left));
                            taskStatusStr.append(String.valueOf(difference / daysInMilli));
                            taskStatusStr.append(" days");
                        } else if( difference / hoursInMilli > 0 ) {
                            taskStatusStr.append(res.getString(R.string.task_list_left));
                            taskStatusStr.append(String.valueOf(difference / hoursInMilli));
                            taskStatusStr.append(" hours");
                        } else if( difference / minutesInMilli <= 0 ) {
                            taskStatusStr.append(res.getString(R.string.task_list_overdue));
                        } else {
                            taskStatusStr.append(res.getString(R.string.task_list_expiring));
                        }
                        break;
                    case DatabaseDefines.TASK_STATUS_COMPLETED:
                        taskStatusStr.append(res.getString(R.string.task_list_completed));
                        break;
                    case DatabaseDefines.TASK_STATUS_CANCELLED:
                        taskStatusStr.append(res.getString(R.string.task_list_cancelled));
                        break;
                }

                this.taskInfo.setText(taskInfoStr.toString());
                this.taskStatus.setText(taskStatusStr.toString());
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        public void onClick() {
        }

        public void onLongClick(final ITaskListControl listControl, final TaskDetailsData data) {
//            this.taskStatus.animate().setDuration(600).translationX(this.taskStatus.getWidth()).alpha(0).withEndAction(new Runnable() {
//                @Override
//                public void run() {
//                    taskStatus.setText(R.string.task_list_completed);
//                    taskStatus.animate().setDuration(600).translationX(0).alpha(1).withEndAction(new Runnable() {
//                        @Override
//                        public void run() {
//                            listControl.closeTask(data, getAdapterPosition());
//                        }
//                    });
//                }
//            });
            //this.taskStatus.setText(R.string.task_list_completed);
//            this.taskStatus.animate().alpha(1).setStartDelay(1000).setDuration(1000).withEndAction(new Runnable() {
//                @Override
//                public void run() {
//                    listControl.closeTask(data, getAdapterPosition());
//                }
//            });
        }

        public void onSwipe(float deltaX) {
            Resources res = this.taskView.getContext().getResources();
            if(deltaX > 0 ) {
                this.taskView.setBackgroundColor(res.getColor(R.color.deleteTaskHighlight));
            } else {
                this.taskView.setBackgroundColor(res.getColor(R.color.completeTaskHighlight));
            }
            float deleteTaskTranslation = this.maxLeft + deltaX > 0 ? 0 : this.maxLeft + deltaX;
            float completeTaskTranslation = this.maxRight + deltaX < 0 ? 0: this.maxRight + deltaX;
            this.deleteTaskLayout.setTranslationX(deleteTaskTranslation);
            this.taskLayout.setTranslationX(deltaX);
            this.completeTaskLayout.setTranslationX(completeTaskTranslation);
        }

        public void onRelease(final ITaskListControl listControl, final TaskDetailsData data) {
            float deltaX = this.taskLayout.getTranslationX();
            if(deltaX > this.maxRight) {
                this.deleteTaskLayout.animate().translationX(this.taskLayout.getWidth());
                this.taskLayout.animate().translationX(this.taskLayout.getWidth() + this.maxRight).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        listControl.removeTask(data, getAdapterPosition());
                    }
                });
            }else if (deltaX < this.maxLeft){
                taskStatus.setText(R.string.task_list_completed);
                this.deleteTaskLayout.animate().translationX(this.maxLeft);
                this.taskLayout.animate().translationX(0);
                this.completeTaskLayout.animate().translationX(this.maxRight).withEndAction(new Runnable() {
                    @Override
                    public void run() {
                        listControl.closeTask(data, getAdapterPosition());
                    }
                });
            } else {
                this.deleteTaskLayout.animate().translationX(this.maxLeft);
                this.taskLayout.animate().translationX(0);
                this.completeTaskLayout.animate().translationX(this.maxRight);
            }
        }

        void resetPosition() {
            this.deleteTaskLayout.setTranslationX(this.maxLeft);
            this.taskLayout.setTranslationX(0);
            this.completeTaskLayout.setTranslationX(this.maxRight);
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = this.layoutInflater.inflate(R.layout.task_list_child_view, parent, false);
        return new ViewHolder(view, this.maxLeft, this.maxRight);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        if(this.listData.moveToPosition(position)) {
            holder.resetPosition();
            holder.fillView(this.listData);
        }
    }

    @Override
    public int getItemCount() {
        if(this.listData == null) {
            return 0;
        }
        return this.listData.getCount();
    }

    public TaskDetailsData getTaskData(int pos) {
        TaskDetailsData data = null;
        if(this.listData.moveToPosition(pos)) {
            data = new TaskDetailsData(this.listData);
        }
        return data;
    }

    public void setListData(Cursor c) {
        if(this.listData != null) {
            this.listData.close();
        }
        this.listData = c;
    }
}
