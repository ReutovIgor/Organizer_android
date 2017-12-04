package com.example.ruireutov.organiser.TaskDetails;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.ruireutov.organiser.R;
import com.example.ruireutov.organiser.TaskDetailsData;

public class TaskDetailsActivity extends AppCompatActivity implements ITaskDetailsUIControl{

    private TaskDetailsControl taskDetailsControl;
    private EditText taskName;
    private EditText taskStatus;
    private ImageView taskPriority;
    private EditText taskCategory;
    private EditText taskDetails;
    private boolean blockedStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.toDoListView_toolbar);
        setSupportActionBar(myToolbar);

        this.blockedStatus = true;

        this.taskName = findViewById(R.id.task_name);
        this.taskStatus = findViewById(R.id.task_status);
        this.taskPriority = findViewById(R.id.task_priority);
        this.taskCategory = findViewById(R.id.task_category);
        this.taskDetails = findViewById(R.id.task_details);

        this.taskDetailsControl = new TaskDetailsControl(this);
        this.taskDetailsControl.parseIntentData(getIntent());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.taskDetailsControl.onDestroy();
    }

    public void showTaskDetails(TaskDetailsData data) {
        this.toggleDataEdit(false);
        this.taskName.setText(data.getName());
        this.taskStatus.setText(data.getStatus());
        this.taskCategory.setText(data.getCategory());
        this.taskDetails.setText(data.getDetails());
    }

    public void showTaskCreation() {
        this.toggleDataEdit(true);
        this.taskName.setText("");
        this.taskStatus.setText("");
        this.taskCategory.setText("");
        this.taskDetails.setText("");
    }

    public void toggleDataEdit(boolean blocked) {
        if(this.blockedStatus != blocked) {
            this.blockedStatus = blocked;
            this.taskName.setEnabled(blocked);
            this.taskName.setFocusable(blocked);
            this.taskStatus.setEnabled(blocked);
            this.taskStatus.setFocusable(blocked);
            this.taskCategory.setEnabled(blocked);
            this.taskCategory.setFocusable(blocked);
            this.taskDetails.setEnabled(blocked);
            this.taskDetails.setFocusable(blocked);
        }
    }
}
