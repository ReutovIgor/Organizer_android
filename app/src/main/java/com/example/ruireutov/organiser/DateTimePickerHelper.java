package com.example.ruireutov.organiser;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DateTimePickerHelper {
    private TextView dateView;
    private TextView timeView;
    private Calendar calendar;
    private DatePickerDialog datePickerDialog;
    private TimePickerDialog timePickerDialog;

    public DateTimePickerHelper(Context context, TextView dateView, TextView timeView) {
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

        String strDate = SimpleDateFormat.getTimeInstance().format(c.getTime());
        String strTime = SimpleDateFormat.getDateInstance().format(c.getTime());

        this.dateView.setText(strDate);
        this.timeView.setText(strTime);
    }

    public void setDateTime(Date date) {
        this.calendar.setTime(date);
        this.dateView.setText(SimpleDateFormat.getDateInstance().format(this.calendar.getTime()));
        this.timeView.setText(SimpleDateFormat.getTimeInstance().format(this.calendar.getTime()));
    }

    public Date getDateTime() {
        return this.calendar.getTime();
    }

    private void setDateView() {
        String strDate = SimpleDateFormat.getDateTimeInstance().format(this.calendar.getTime());
        this.dateView.setText(strDate);
    }

    private void setTimeView() {
        String strTime = SimpleDateFormat.getTimeInstance().format(this.calendar.getTime());
        this.timeView.setText(strTime);
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
