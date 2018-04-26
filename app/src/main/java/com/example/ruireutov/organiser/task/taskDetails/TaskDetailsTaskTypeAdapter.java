package com.example.ruireutov.organiser.task.taskDetails;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.ruireutov.organiser.R;

public class TaskDetailsTaskTypeAdapter extends ArrayAdapter {

    public TaskDetailsTaskTypeAdapter(@NonNull Context context) {
        super(context, R.layout.task_details_task_type_item,R.id.task_details_task_type_name);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.task_details_task_type_item, parent, false);
        }
        float width = parent.getWidth();
        TextView t = convertView.findViewById(R.id.task_details_task_type_name);
        switch (position) {
            case 0:
                t.setText(R.string.task_details_default_task_type);
                break;
            case 1:
                t.setText(R.string.task_details_scheduled_task_type);
                break;
            case 2:
                t.setText(R.string.task_details_daily_task_type);
                break;
            case 3:
                break;
            case 4:
                break;
            case 5:
                break;
            case 6:
                break;
        }

        return convertView;
    }

    @Override
    public int getCount() {
        return 3;
    }
}
