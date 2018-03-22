package com.example.ruireutov.organiser.task.taskHelpers;

import android.app.DatePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DatePickerHelper {
    private Calendar calendar;
    private TextView dateView;
    private DatePickerDialog datePickerDialog;
    private IDatePickerNotification notifier;

    public DatePickerHelper(Context context, TextView dateView, IDatePickerNotification notifier) {
        this.dateView = dateView;
        this.calendar = Calendar.getInstance();
        this.notifier = notifier;
        this.datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                setDateView();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        this.dateView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.show();
            }
        });
    }

    public void setDate(Date date) {
        if(date == null) {
            this.datePickerDialog.updateDate(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
            this.dateView.setText("");
        } else {
            this.calendar.setTime(date);
            this.dateView.setText(SimpleDateFormat.getDateInstance().format(this.calendar.getTime()));
        }
    }

    private void setDateView() {
        this.dateView.setText(SimpleDateFormat.getDateInstance().format(this.calendar.getTime()));
        this.notifier.onDateChange(this.calendar.getTime());
    }
}
