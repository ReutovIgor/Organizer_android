package com.example.ruireutov.organiser.task.taskList.taskViews;

import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.media.Image;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ruireutov.organiser.R;
import com.example.ruireutov.organiser.databaseWorkers.DatabaseDefines;

public abstract class ATaskView {
    private boolean moved;
    //private boolean touchActive;
    private float maxLeft;
    private float maxRight;
    private View deleteTaskTab;
    private View taskLayout;
    private View completeTaskTab;
//    private ImageView deleteTask;
//    private ImageView completeTask;

    ATaskView (View view, Context context, Cursor cursor) {
        this.moved = false;

        this.deleteTaskTab = view.findViewById(R.id.delete_task_tab);
        this.taskLayout = view.findViewById(R.id.task_layout);
        ImageView priorityIndication = view.findViewById(R.id.task_priority_indication);
        ImageView categoryImage = view.findViewById(R.id.task_category_icon);
        TextView taskTitle = view.findViewById(R.id.task_title_text);
        TextView taskDetails = view.findViewById(R.id.task_description_text);
//        this.deleteTask = view.findViewById(R.id.delete_task_button);
//        this.completeTask = view.findViewById(R.id.complete_task_button);
        this.completeTaskTab = view.findViewById(R.id.complete_task_tab);

        String priorityStr = cursor.getString( cursor.getColumnIndex(DatabaseDefines.TASK_LIST_PRIORITY) );
        switch (priorityStr) {
            case "High":
                priorityIndication.setBackgroundColor(context.getResources().getColor(R.color.highPriority));
                break;
            case "Normal":
                priorityIndication.setBackgroundColor(context.getResources().getColor(R.color.normalPriority));
                break;
            case "Low":
                priorityIndication.setBackgroundColor(context.getResources().getColor(R.color.lowPriority));
                break;
        }
        Resources resources = context.getResources();
        String iconName = cursor.getString( cursor.getColumnIndex(DatabaseDefines.TASK_LIST_CATEGORY_ICON) );
        categoryImage.setImageDrawable(resources.getDrawable(resources.getIdentifier(iconName, "drawable", context.getPackageName())));
        categoryImage.setBackgroundColor(Color.parseColor(cursor.getString( cursor.getColumnIndex(DatabaseDefines.TASK_LIST_CATEGORY_COLOR))));
        taskTitle.setText(cursor.getString( cursor.getColumnIndex(DatabaseDefines.TASK_LIST_NAME)));
        taskDetails.setText(cursor.getString(cursor.getColumnIndex(DatabaseDefines.TASK_LIST_DETAILS)));

        DisplayMetrics metrics = resources.getDisplayMetrics();
        this.maxLeft = -40 * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        this.maxRight = 40 * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

//    public void onTouchStart() {
//
//    }

    public boolean onTouchMove(float dx) {
        Log.d("Task List element", "Touch move DX received " + dx);
        this.moved = true;
        if( dx < 0 ) {
            dx = dx > this.maxLeft ? dx : this.maxLeft;
        } else {
            dx = dx < this.maxRight ? dx : this.maxRight;
        }
        this.deleteTaskTab.setTranslationX(this.maxLeft + dx);
        this.taskLayout.setTranslationX(dx);
        this.completeTaskTab.setTranslationX(this.maxRight + dx);

        return true;
    }

    public boolean onTouchEnd() {
        boolean returnVal = this.moved;
        this.moved = false;
        this.deleteTaskTab.setTranslationX(this.maxLeft);
        this.taskLayout.setTranslationX(0.0f);
        this.completeTaskTab.setTranslationX(this.maxRight);
        return returnVal;
    }

    public void onCLick() {


    }

    public boolean onLongClick() {
        return !this.moved;
    }
}
