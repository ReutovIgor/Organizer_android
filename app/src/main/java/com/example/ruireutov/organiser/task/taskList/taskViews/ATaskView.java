package com.example.ruireutov.organiser.task.taskList.taskViews;

import android.animation.Animator;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Color;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ruireutov.organiser.R;
import com.example.ruireutov.organiser.databaseWorkers.DatabaseDefines;

public abstract class ATaskView {
    private boolean moved;
    private int moveLock;
    private float maxLeft;
    private float maxRight;
    private float deleteTaskMinTransition;
    private float completeTaskMinTransition;
    private Adapter adapter;
    private View taskCardView;
    private View taskLayout;
    private View completeTaskLayout;
    private View taskDetailsLayout;
    private TextView taskDetails;
    private CheckBox completeTask;

    ATaskView (View view, Context context, Cursor cursor) {
        this.moved = false;
        this.moveLock = -1;

        this.taskCardView = view;
        this.taskLayout = view.findViewById(R.id.task_layout);
        ImageView priorityIndication = view.findViewById(R.id.task_priority_indication);
        ImageView categoryImage = view.findViewById(R.id.task_category_icon);
        TextView taskTitle = view.findViewById(R.id.task_title_text);
        this.completeTask = view.findViewById(R.id.complete_task_button);
        this.completeTaskLayout = view.findViewById(R.id.complete_task_layout);
        this.taskDetailsLayout = view.findViewById(R.id.task_details_layout);
        this.taskDetails = view.findViewById(R.id.task_description_text);

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
        this.taskDetails.setText(cursor.getString(cursor.getColumnIndex(DatabaseDefines.TASK_LIST_DETAILS)));

        DisplayMetrics metrics = resources.getDisplayMetrics();
        this.maxLeft = -40 * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        this.maxRight = 40 * ((float)metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public void showDetails() {
        if(this.taskDetails.getText().length() > 0) {
            //this.taskDetails.animate().setDuration(1000);
            this.taskDetails.setVisibility(View.VISIBLE);
        }
    }

    public void hideDetails() {
        this.taskDetails.setVisibility(View.GONE);
    }

    public boolean onTouchMove(float dx) {
        Log.d("Task List element", "Touch move DX received " + dx);
        this.moved = true;
        if(dx > 0 && this.moveLock == -1) {
            Log.d("Task List element", "Delete task lock is assigned");
            this.moveLock = 0;
        } else if( dx < 0 && this.moveLock == -1) {
            Log.d("Task List element", "Complete task lock is assigned");
            this.moveLock = 1;
        }

        switch (this.moveLock) {
            case 0:
                dx = dx > 0 ? dx : 0;
                this.taskCardView.setTranslationX(dx);
                Log.d("Task List element", "Task Card view is moved by " + dx);
            break;
            case 1:
                if( dx < this.maxLeft) {
                    dx = this.maxLeft;
                } else if (dx >= 0){
                    dx = 0;
                }

                this.taskLayout.setTranslationX(dx);
                this.completeTaskLayout.setTranslationX(this.maxRight + dx);
                Log.d("Task List element", "Task Layout view is moved by " + dx);
            break;
        }

        return true;
    }

    public boolean onTouchEnd() {
        boolean returnVal = this.moved;
        this.moved = false;
        switch (this.moveLock) {
            case 0:
                float a = this.taskCardView.getWidth() * 0.3f;
                if(this.taskCardView.getTranslationX() > (this.taskCardView.getWidth() * 0.1f)) {
                    CardView.LayoutParams params = (CardView.LayoutParams)taskCardView.getLayoutParams();
                    params.height = 0;
                    taskCardView.setLayoutParams(params);
//                    this.taskCardView.animate()
//                            .translationX(this.taskCardView.getWidth())
//                            //.setDuration(400)
//                            .setListener(new Animator.AnimatorListener() {
//                        @Override
//                        public void onAnimationStart(Animator animation) {
//                            Log.d("Task List ", "Animation Start is called");
//                        }
//
//                        @Override
//                        public void onAnimationEnd(Animator animation) {
//                            Log.d("Task List ", "Animation End is called");
//                            ViewGroup.LayoutParams params = taskCardView.getLayoutParams();
//                            params.height = 0;
//                            taskCardView.setLayoutParams(params);
//                            //taskCardView.clearAnimation();
//                            //taskCardView.setVisibility(View.GONE);
//                        }
//
//                        @Override
//                        public void onAnimationCancel(Animator animation) {
//                            Log.d("Task List ", "Animation Cancel is called");
//                        }
//
//                        @Override
//                        public void onAnimationRepeat(Animator animation) {
//                            Log.d("Task List ", "Animation Repeat is called");
//                        }
//                    });
                    //taskCardView.setVisibility(View.GONE);
                    //((ViewGroup) this.taskCardView.getParent()).removeView(this.taskCardView);

                } else {
                    this.taskCardView.animate().translationX(0.0f).setDuration(400);
                }
                break;
            case 1:
                break;
        }
        this.moveLock = -1;
        //this.taskCardView.animate().translationX(0.0f).setDuration(400);
        //this.taskLayout.animate().translationX(0.0f).setDuration(400);
        //this.completeTaskLayout.animate().translationX(this.maxRight).setDuration(400);
        return returnVal;
    }

    public void onCLick() {
        this.showDetails();
    }

    public boolean onLongClick() {
        return !this.moved;
    }
}
