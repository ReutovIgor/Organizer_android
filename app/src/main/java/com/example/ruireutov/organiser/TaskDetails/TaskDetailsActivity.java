package com.example.ruireutov.organiser.TaskDetails;

import android.database.Cursor;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ruireutov.organiser.DatabaseWorkers.DatabaseDefines;
import com.example.ruireutov.organiser.DateTimePickerHelper;
import com.example.ruireutov.organiser.R;
import com.example.ruireutov.organiser.SpinnerAdapter;
import com.example.ruireutov.organiser.TaskDetailsData;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

public class TaskDetailsActivity extends AppCompatActivity implements ITaskDetailsUIControl {

    private TaskDetailsControl taskDetailsControl;

    private EditText taskName;
    private CheckBox taskSchedulable;
    private TextView taskFromDateTimeHint;
    private LinearLayout taskFromDateTime;
    private TextView taskFromDate;
    private TextView taskFromTime;
    private TextView taskToDateTimeHint;
    private LinearLayout taskToDateTime;
    private TextView taskToDate;
    private TextView taskToTime;
    private Spinner taskPriority;
    private SpinnerAdapter taskPriorityAdapter;
    private Spinner taskCategory;
    private SpinnerAdapter taskCategoryAdapter;
    private EditText taskDetails;
    private Button taskButton;
    private DateTimePickerHelper fromDateTimeHelper;
    private DateTimePickerHelper toDateTimeHelper;

    private boolean blockedStatus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_details);

        Toolbar myToolbar = (Toolbar) findViewById(R.id.taskDetailsView_toolbar);
        setSupportActionBar(myToolbar);

        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);

        this.blockedStatus = true;

        this.taskName = findViewById(R.id.task_name);
        this.taskSchedulable =findViewById(R.id.task_scheduled_checkbox);
        this.taskSchedulable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                toggleDateTime(isChecked);
            }
        });

        this.taskFromDateTimeHint = findViewById(R.id.from_date_time_hint);
        this.taskFromDateTime = findViewById(R.id.from_date_time);
        this.taskFromDate = findViewById(R.id.task_from_date);
        this.taskFromTime = findViewById(R.id.task_from_time);
        this.fromDateTimeHelper = new DateTimePickerHelper(this, this.taskFromDate, this.taskFromTime);

        this.taskToDateTimeHint = findViewById(R.id.to_date_time_hint);
        this.taskToDateTime = findViewById(R.id.to_date_time);
        this.taskToDate = findViewById(R.id.task_to_date);
        this.taskToTime = findViewById(R.id.task_to_time);
        this.toDateTimeHelper = new DateTimePickerHelper(this, this.taskToDate, this.taskToTime);

        this.taskPriority = findViewById(R.id.task_priority);
        this.taskPriorityAdapter = new SpinnerAdapter(this, null, 0, SpinnerAdapter.TYPE_PRIORITY);
        this.taskPriority.setAdapter(this.taskPriorityAdapter);

        this.taskCategory = findViewById(R.id.task_category);
        this.taskCategoryAdapter = new SpinnerAdapter(this, null, 0, SpinnerAdapter.TYPE_CATEGORY);
        this.taskCategory.setAdapter(this.taskCategoryAdapter);

        this.taskDetails = findViewById(R.id.task_details);

        this.taskButton = findViewById(R.id.task_button);
        this.taskButton.setOnClickListener(new ElementClickListener());

        this.taskDetailsControl = new TaskDetailsControl(this);
        this.taskDetailsControl.parseIntentData(getIntent());
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
            fromFormat = new SimpleDateFormat("dd/MMMM/yyyy HH:mm");
            toFormat = new SimpleDateFormat("yyyy-MM-DD HH:mm");
        } else {
            fromFormat = new SimpleDateFormat("yyyy-MM-DD HH:mm");
            toFormat = new SimpleDateFormat("dd/MMMM/yyyy HH:mm");
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
        int visible = show ? View.VISIBLE : View.GONE;
        this.taskFromDateTimeHint.setVisibility(visible);
        this.taskFromDateTime.setVisibility(visible);
        this.taskToDateTimeHint.setVisibility(visible);
        this.taskToDateTime.setVisibility(visible);
    }

    private HashMap<String, String> getTaskData() {
        HashMap<String, String> data = new HashMap<>();
        data.put(DatabaseDefines.TODO_LIST_NAME, taskName.getText().toString());
        data.put(DatabaseDefines.TODO_LIST_STATUS, DatabaseDefines.STATUS_IN_PROGRESS);
        if(taskSchedulable.isChecked()) {
            String str = this.taskFromDate.getText().toString() + " " + this.taskFromTime.getText().toString();
            String fromDate = this.dateTimeConverter(str, true);
            data.put(DatabaseDefines.TODO_LIST_START, fromDate);
            str = this.taskToDate.getText().toString() + " " + this.taskToTime.getText().toString();
            String toDate = this.dateTimeConverter(str, true);
            data.put(DatabaseDefines.TODO_LIST_END, toDate);
        }else {
            data.put(DatabaseDefines.TODO_LIST_START, "");
            data.put(DatabaseDefines.TODO_LIST_END, "");
        }
        data.put(DatabaseDefines.TODO_LIST_PRIORITY, this.taskPriority.getSelectedItem().toString());
        data.put(DatabaseDefines.TODO_LIST_CATEGORY, this.taskCategory.getSelectedItem().toString());

        return data;
    }

    @Override
    public void showTaskDetails(TaskDetailsData data) {
        this.toggleDataEdit(false);
        this.taskName.setText(data.getName());
        boolean showDateTime = data.getDateFrom().length() > 0 ? true : false;
        this.taskSchedulable.setChecked(showDateTime);
        this.toggleDateTime(showDateTime);
        this.fromDateTimeHelper.setDateTime(this.dateTimeConverter(data.getDateFrom(), false));
        this.toDateTimeHelper.setDateTime(this.dateTimeConverter(data.getDateTo(), false));
        int priorityPos = this.taskPriorityAdapter.getItemPosition(data.getPriority());
        this.taskPriority.setSelection(priorityPos);
        int categoryPos = this.taskPriorityAdapter.getItemPosition(data.getCategory());
        this.taskCategory.setSelection(categoryPos);
        this.taskDetails.setText(data.getDetails());
        this.taskButton.setText(R.string.task_button_finish);
    }

    @Override
    public void showTaskCreation() {
        this.toggleDataEdit(true);
        this.toggleDateTime(false);
        this.taskName.setText("");
        this.taskSchedulable.setChecked(false);
        this.fromDateTimeHelper.setDefault(false);
        this.toDateTimeHelper.setDefault(true);
        this.taskPriority.setSelection(0);
        this.taskCategory.setSelection(0);
        this.taskDetails.setText("");
        this.taskButton.setText(R.string.task_button_create);
    }

    @Override
    public void fillCategories(Cursor cursor) {
        this.taskCategoryAdapter.swapCursor(cursor);
    }

    @Override
    public void fillPriorities(Cursor cursor) {
        this.taskPriorityAdapter.swapCursor(cursor);
    }

    @Override
    public void toggleDataEdit(boolean blocked) {
        if(this.blockedStatus != blocked) {
            this.blockedStatus = blocked;
            this.taskName.setEnabled(blocked);
            this.taskName.setFocusable(blocked);
            this.taskSchedulable.setEnabled(blocked);
            this.taskSchedulable.setFocusable(blocked);
            this.taskFromDate.setEnabled(blocked);
            this.taskFromDate.setFocusable(blocked);
            this.taskFromTime.setEnabled(blocked);
            this.taskFromTime.setFocusable(blocked);
            this.taskToDate.setEnabled(blocked);
            this.taskToDate.setFocusable(blocked);
            this.taskToTime.setEnabled(blocked);
            this.taskToTime.setFocusable(blocked);
            this.taskPriority.setEnabled(blocked);
            this.taskPriority.setFocusable(blocked);
            this.taskCategory.setEnabled(blocked);
            this.taskCategory.setFocusable(blocked);
            this.taskDetails.setEnabled(blocked);
            this.taskDetails.setFocusable(blocked);
        }
    }

    private class ElementClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.task_button:
                    if(taskButton.getText().toString() != getString(R.string.task_button_finish)) {
                        taskDetailsControl.addTask(new TaskDetailsData( getTaskData()));
                    }
                    break;
            }
        }
    }
}
