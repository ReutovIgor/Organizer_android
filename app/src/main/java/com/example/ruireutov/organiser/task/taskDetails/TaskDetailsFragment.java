package com.example.ruireutov.organiser.task.taskDetails;


import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ruireutov.organiser.R;
import com.example.ruireutov.organiser.task.DateTimePickerHelper;
import com.example.ruireutov.organiser.task.SpinnerAdapter;
import com.example.ruireutov.organiser.task.TaskActivity;
import com.example.ruireutov.organiser.task.TaskDetailsData;

import java.util.Date;

public class TaskDetailsFragment extends Fragment implements ITaskDetailsUIControl, ITaskDetailsUINotification, ITaskDetailsActivityControl{

    private TaskDetailsControl taskDetailsControl;
    private ConstraintLayout parentLayout;
    private EditText taskName;
    private CheckBox deadlineCheckbox;
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
    private TaskDetailsData receivedData;

    public TaskDetailsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_task_details, container, false);

        //this.parentLayout = view.findViewById(R.id.task_detail_root);
        this.setDetailMode(false);

        this.taskName = view.findViewById(R.id.task_name);
        this.taskName.addTextChangedListener(new TaskDetailsFragment.ViewTextWatcher(this.taskName));

        this.deadlineCheckbox = view.findViewById(R.id.task_scheduled_checkbox);
        this.deadlineCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                toggleDateTime(isChecked);
            }
        });

        this.taskDueDateTime = view.findViewById(R.id.to_date_time);
        this.taskDueDate = view.findViewById(R.id.task_to_date);
        this.taskDueTime = view.findViewById(R.id.task_to_time);
        this.dueDateTimeHelper = new DateTimePickerHelper(getActivity(), this, this.taskDueDate, this.taskDueTime);

        this.taskPriority = view.findViewById(R.id.task_priority);
        this.taskPriorityAdapter = new SpinnerAdapter(getActivity(),null, R.layout.task_details_drop_down_item,0, SpinnerAdapter.TYPE_PRIORITY);
        this.taskPriority.setAdapter(this.taskPriorityAdapter);
        this.taskPriority.setOnItemSelectedListener(new TaskDetailsFragment.SpinnerSelectionChangeListener());

        this.taskCategory = view.findViewById(R.id.task_category);
        this.taskCategoryAdapter = new SpinnerAdapter(getActivity(), null, R.layout.task_details_drop_down_item, 0, SpinnerAdapter.TYPE_CATEGORY);
        this.taskCategory.setAdapter(this.taskCategoryAdapter);
        this.taskCategory.setOnItemSelectedListener(new TaskDetailsFragment.SpinnerSelectionChangeListener());

        this.taskDetails = view.findViewById(R.id.task_details);
        this.taskDetails.addTextChangedListener(new TaskDetailsFragment.ViewTextWatcher(this.taskDetails));

        this.taskButton1 = view.findViewById(R.id.task_button_1);
        this.taskButton1.setOnClickListener(new TaskDetailsFragment.ElementClickListener());

        this.taskButton2 = view.findViewById(R.id.task_button_2);
        this.taskButton2.setOnClickListener(new TaskDetailsFragment.ElementClickListener());

        this.taskButton3 = view.findViewById(R.id.task_button_3);
        this.taskButton3.setOnClickListener(new TaskDetailsFragment.ElementClickListener());

        TaskActivity activity = (TaskActivity) getActivity();
        this.taskDetailsControl = new TaskDetailsControl(activity, this, activity);

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        this.taskDetailsControl.parseTaskData(this.receivedData);
        this.receivedData = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        this.taskDetailsControl.onDestroy();
    }

    private void toggleDateTime(boolean show) {
        this.taskDueDateTime.setVisibility(show ? View.VISIBLE : View.GONE);
        this.taskDetailsControl.setDueDate(show ? this.dueDateTimeHelper.getDateTime(): null);
    }

    private void setDetailMode(boolean active) {
        this.editMode = active;
        //this.parentLayout.setFocusableInTouchMode(this.editMode);
    }

    @Override
    public void applyTaskDetails(TaskDetailsData data) {

        if(this.taskDetailsControl != null) {
            this.taskDetailsControl.parseTaskData(data);
        } else {
            this.receivedData = data;
        }
    }

    @Override
    public void showTaskDetails(TaskDetailsData data) {
        this.setDetailMode(true);
        this.taskName.setText(data.getName());
        if(data.hasDeadline()) {
            this.dueDateTimeHelper.setDateTime(data.getDateDue());
        } else {
            this.dueDateTimeHelper.setDefault();
        }
        this.deadlineCheckbox.setChecked(data.hasDeadline());
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
        this.taskName.setText("");
        this.dueDateTimeHelper.setDefault();
        this.deadlineCheckbox.setChecked(false);
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

    @Override
    public void onDateDueChange(Date d) {
        this.taskDetailsControl.setDueDate(d);
    }

    private class ElementClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.task_button_1:
                    taskDetailsControl.closeTask();
                    break;
                case R.id.task_button_2:
                    if(taskButton2.getText().toString() != getString(R.string.task_button_save)) {
                        taskDetailsControl.addTask();
                    } else {
                        taskDetailsControl.updateTask();
                    }
                    break;
                case R.id.task_button_3:
                    taskDetailsControl.deleteTask();
                    break;
            }
        }
    }

    private class ViewTextWatcher implements TextWatcher {
        private View textView;
        public ViewTextWatcher(View textView) {
            this.textView = textView;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
        @Override
        public void afterTextChanged(Editable editable) {
            switch (this.textView.getId()) {
                case R.id.task_name:
                    taskDetailsControl.setName(editable.toString());
                    break;
                case R.id.task_details:
                    taskDetailsControl.setDetails(editable.toString());
                    break;
                case R.id.task_to_date:
                    taskDetailsControl.setDueDate(dueDateTimeHelper.getDateTime());
                    break;
                case R.id.task_to_time:
                    taskDetailsControl.setDueDate(dueDateTimeHelper.getDateTime());
                    break;
            }
        }
    }

    private class SpinnerSelectionChangeListener implements AdapterView.OnItemSelectedListener {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            int isa = adapterView.getId();
            switch (adapterView.getId()) {
                case R.id.task_priority:
                    taskDetailsControl.setPriority(taskPriority.getSelectedItem().toString());
                    break;
                case R.id.task_category:
                    taskDetailsControl.setCategory(taskCategory.getSelectedItem().toString());
                    break;
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) { return;}
    }

}
