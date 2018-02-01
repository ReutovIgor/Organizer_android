package com.example.ruireutov.organiser.task;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.ruireutov.organiser.task.taskDetails.ITaskDetailsUINotification;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimePickerHelper {
    private Calendar calendar;
    private TextView dateView;
    private TextView timeView;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;
    private ITaskDetailsUINotification taskDetailsUINotification;

    public DateTimePickerHelper(Context context, ITaskDetailsUINotification taskDetailsUINotification,  TextView dateView, TextView timeView) {
        this.taskDetailsUINotification = taskDetailsUINotification;
        this.dateView = dateView;
        this.dateView.setOnClickListener(new TextViewClickHandlers());
        this.timeView = timeView;
        this.timeView.setOnClickListener(new TextViewClickHandlers());
        this.calendar = Calendar.getInstance();
        this.datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                setDateView();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        this.timePickerDialog = new TimePickerDialog(context, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                setTimeView();
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
    }

    public void setDefault() {
        Calendar c = Calendar.getInstance();
        c.add(Calendar.MINUTE, 30);

        String strDate = SimpleDateFormat.getDateInstance().format(c.getTime());
        String strTime = SimpleDateFormat.getTimeInstance(DateFormat.SHORT).format(c.getTime());

        this.dateView.setText(strDate);
        this.timeView.setText(strTime);
    }

    public void setDateTime(Date date) {
        this.calendar.setTime(date);
        this.dateView.setText(SimpleDateFormat.getDateInstance().format(this.calendar.getTime()));
        this.timeView.setText(SimpleDateFormat.getTimeInstance(DateFormat.SHORT).format(this.calendar.getTime()));
    }

    public Date getDateTime() {
        return this.calendar.getTime();
    }

    private void setDateView() {
        String strDate = SimpleDateFormat.getDateInstance().format(this.calendar.getTime());
        this.dateView.setText(strDate);
        this.taskDetailsUINotification.onDateDueChange(this.calendar.getTime());
    }

    private void setTimeView() {
        String strTime = SimpleDateFormat.getTimeInstance(DateFormat.SHORT).format(this.calendar.getTime());
        this.timeView.setText(strTime);
        this.taskDetailsUINotification.onDateDueChange(this.calendar.getTime());
    }

    private class TextViewClickHandlers implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            if(v.getId() == dateView.getId()) {
                datePickerDialog.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            } else if(v.getId() == timeView.getId()){
                timePickerDialog.updateTime(calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE));
                timePickerDialog.show();
            }
        }
    }
}
