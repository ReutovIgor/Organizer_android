package com.example.ruireutov.organiser.TaskDetails;

import android.database.Cursor;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ruireutov.organiser.DateTimePickerHelper;
import com.example.ruireutov.organiser.R;
import com.example.ruireutov.organiser.SpinnerAdapter;
import com.example.ruireutov.organiser.TaskDetailsData;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TaskDetailsActivity extends AppCompatActivity implements ITaskDetailsUIControl {

    private TaskDetailsControl taskDetailsControl;
    private ConstraintLayout parentLayout;
    private EditText taskName;
    private CheckBox dedlineCheckbox;
    private LinearLayout taskDueDateTime;
    private TextView taskDueDate;
    private TextView taskDueTime;
    private Spinner taskPriority;
    private SpinnerAdapter taskPriorityAdapter;
    private Spinner taskCategory;
    private SpinnerAdapter taskCategoryAdapter;
    private EditText taskDetails;
    private Button taskButton1;
    private Button taskButton2;
    private Button taskButton3;
    private DateTimePickerHelper dueDateTimeHelper;

    private boolean editMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        Toolbar myToolbar = findViewById(R.id.taskDetailsView_toolbar);
        setSupportActionBar(myToolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        this.parentLayout = findViewById(R.id.task_detail_root);
        this.setDetailMode(false);

        this.taskName = findViewById(R.id.task_name);
        //this.taskName.on
        this.dedlineCheckbox =findViewById(R.id.task_scheduled_checkbox);
        this.dedlineCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                toggleDateTime(isChecked);
            }
        });

        this.taskDueDateTime = findViewById(R.id.to_date_time);
        this.taskDueDate = findViewById(R.id.task_to_date);
        this.taskDueTime = findViewById(R.id.task_to_time);
        this.dueDateTimeHelper = new DateTimePickerHelper(this, this.taskDueDate, this.taskDueTime);

        this.taskPriority = findViewById(R.id.task_priority);
        this.taskPriorityAdapter = new SpinnerAdapter(this,null, R.layout.task_details_drop_down_item,0, SpinnerAdapter.TYPE_PRIORITY);
        this.taskPriority.setAdapter(this.taskPriorityAdapter);

        this.taskCategory = findViewById(R.id.task_category);
        this.taskCategoryAdapter = new SpinnerAdapter(this, null, R.layout.task_details_drop_down_item, 0, SpinnerAdapter.TYPE_CATEGORY);
        this.taskCategory.setAdapter(this.taskCategoryAdapter);

        this.taskDetails = findViewById(R.id.task_details);

        this.taskButton1 = findViewById(R.id.task_button_1);
        this.taskButton1.setOnClickListener(new ElementClickListener());

        this.taskButton2 = findViewById(R.id.task_button_2);
        this.taskButton2.setOnClickListener(new ElementClickListener());

        this.taskButton3 = findViewById(R.id.task_button_3);
        this.taskButton3.setOnClickListener(new ElementClickListener());

        this.taskDetailsControl = new TaskDetailsControl(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.taskDetailsControl.parseIntentData(getIntent());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.taskDetailsControl.onDestroy();
    }

    private String dateTimeConverter(String date, boolean toDb) {
        DateFormat fromFormat, toFormat;

        if(toDb) {
            fromFormat = SimpleDateFormat.getDateTimeInstance();
            toFormat = new SimpleDateFormat("yyyy-MM-DD HH:mm");
        } else {
            fromFormat = new SimpleDateFormat("yyyy-MM-DD HH:mm");
            toFormat = SimpleDateFormat.getDateTimeInstance();
        }

        Date d;
        try {
            d = fromFormat.parse(date);
        } catch (ParseException e) {
            d = Calendar.getInstance().getTime();
            e.printStackTrace();
        }
        String strDateNew = toFormat.format(d);
        return strDateNew;
    }

    private void toggleDateTime(boolean show) {
        this.taskDueDateTime.setVisibility(show ? View.VISIBLE : View.GONE);
    }

    private void setDetailMode(boolean active) {
        this.editMode = active;
        this.parentLayout.setFocusableInTouchMode(this.editMode);
    }

//    private HashMap<String, String> getTaskData() {
//        HashMap<String, String> data = new HashMap<>();
//        //data.put(DatabaseDefines.TASK_LIST_ID, this.taskDetailsControl.getTaskId());
//        data.put(DatabaseDefines.TASK_LIST_NAME, taskName.getText().toString());
//        data.put(DatabaseDefines.TASK_LIST_STATUS, Integer.toString(DatabaseDefines.TASK_STATUS_IN_PROGRESS));
//        Date d = Calendar.getInstance().getTime();
//        DateFormat dateFormatter = SimpleDateFormat.getDateTimeInstance();
//        String str = dateFormatter.format(d);
//        String fromDate = this.dateTimeConverter(str, true);
//        data.put(DatabaseDefines.TASK_LIST_START, fromDate);
//        if(dedlineCheckbox.isChecked()) {
//            str = this.taskDueDate.getText().toString() + " " + this.taskDueTime.getText().toString();
//            String toDate = this.dateTimeConverter(str, true);
//            data.put(DatabaseDefines.TASK_LIST_END, toDate);
//        }else {
//            data.put(DatabaseDefines.TASK_LIST_END, "");
//        }
//        data.put(DatabaseDefines.TASK_LIST_PRIORITY, this.taskPriority.getSelectedItem().toString());
//        data.put(DatabaseDefines.TASK_LIST_CATEGORY, this.taskCategory.getSelectedItem().toString());
//
//        return data;
//    }

    @Override
    public void showTaskDetails(TaskDetailsData data) {
        this.setDetailMode(true);

        this.taskName.setText(data.getName());

        this.dedlineCheckbox.setChecked(data.hasDeadline());
        this.toggleDateTime(data.hasDeadline());
        if(data.hasDeadline()) {
            this.dueDateTimeHelper.setDateTime(data.getDateDue());
        } else {
            this.dueDateTimeHelper.setDefault();
        }

        int priorityPos = this.taskPriorityAdapter.getItemPosition(data.getPriority());
        this.taskPriority.setSelection(priorityPos);

        int categoryPos = this.taskCategoryAdapter.getItemPosition(data.getCategory());
        this.taskCategory.setSelection(categoryPos);

        this.taskDetails.setText(data.getDetails());
        this.taskButton1.setVisibility(View.VISIBLE);
        this.taskButton2.setText(R.string.task_button_save);
        this.taskButton1.setVisibility(View.VISIBLE);
    }

    @Override
    public void showTaskCreation() {
        this.setDetailMode(false);
        this.toggleDateTime(false);
        this.taskName.setText("");
        this.dedlineCheckbox.setChecked(false);
        this.dueDateTimeHelper.setDefault();
        this.taskPriority.setSelection(0);
        this.taskCategory.setSelection(0);
        this.taskDetails.setText("");
        this.taskButton1.setVisibility(View.GONE);
        this.taskButton2.setText(R.string.task_button_create);
        this.taskButton3.setVisibility(View.GONE);
    }

    @Override
    public void fillCategories(Cursor cursor) {
        this.taskCategoryAdapter.swapCursor(cursor);
    }

    @Override
    public void fillPriorities(Cursor cursor) {
        this.taskPriorityAdapter.swapCursor(cursor);
    }

    private class ElementClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.task_button_1:
                    break;
                case R.id.task_button_2:
                    if(taskButton2.getText().toString() != getString(R.string.task_button_save)) {
                        //taskDetailsControl.addTask(new TaskDetailsData( getTaskData()));
                    } else {
                        //taskDetailsControl.updateTask(new TaskDetailsData( getTaskData()));
                    }
                    break;
                case R.id.task_button_3:
                    break;
            }
        }
    }
}
