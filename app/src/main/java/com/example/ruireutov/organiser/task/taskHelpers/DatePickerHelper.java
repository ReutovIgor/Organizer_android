package com.example.ruireutov.organiser.task.taskHelpers;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class DatePickerHelper {
    private Calendar calendar;
    private TextView dateView;
    private DatePickerDialog datePickerDialog;
    public DatePickerHelper(Context context, TextView dateView) {
        this.dateView = dateView;
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
    }

    private void setDateView() {
        String strDate = SimpleDateFormat.getDateInstance().format(this.calendar.getTime());
        this.dateView.setText(strDate);
        //this.taskDetailsUINotification.onDateDueChange(this.calendar.getTime());
    }
}
