package com.example.ruireutov.organiser.task.taskDetails;

import android.content.Context;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.ruireutov.organiser.R;

import java.util.List;

public class TaskDetailsSpinnerAdapter extends ArrayAdapter<TaskDetailsSpinnerAdapter.SpinnerItem>{
    public static final int TYPE_TASK_TYPES = 0;
    public static final int TYPE_CATEGORIES = 1;
    public static final int TYPE_PRIORITIES = 2;

    private int spinnerType;

    static interface SpinnerItem {
        void fillView(View itemView);
        String getName();
    }

    static class TaskTypesView implements SpinnerItem {
        String name;

        TaskTypesView (String taskType) {
            this.name = taskType;
        }

        @Override
        public void fillView(View itemView) {
            TextView nameView = itemView.findViewById(R.id.task_details_name);
            ImageView iconView = itemView.findViewById(R.id.task_details_icon);

            nameView.setText(this.name);
            //iconView.setVisibility(View.GONE);
        }

        @Override
        public String getName() {
            return this.name;
        }
    }

    TaskDetailsSpinnerAdapter(@NonNull Context context, int spinnerType) {
        super(context, R.layout.task_details_spinner_item, R.id.task_details_name);
        this.spinnerType = spinnerType;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.task_details_spinner_item, parent, false);
        }
        SpinnerItem item = this.getItem(position);
        assert item != null;
        item.fillView(convertView);
        return  convertView;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if(convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.task_details_spinner_item, parent, false);
        }
        SpinnerItem item = this.getItem(position);
        assert item != null;
        item.fillView(convertView);
        return  convertView;
    }

    public void setData(List<String> dataSet) {
        for (String item : dataSet) {
            SpinnerItem spinnerItem = new TaskTypesView(item);
            this.add(spinnerItem);
        }
        this.notifyDataSetChanged();
    }

    public void setData(Cursor cursor){

    }
}
