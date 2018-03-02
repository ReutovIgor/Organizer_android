package com.example.ruireutov.organiser.task.taskFilter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.ruireutov.organiser.R;
import com.example.ruireutov.organiser.databaseWorkers.DatabaseDefines;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;

public class TaskFilterList {
    private Context context;
    private LayoutInflater inflater;
    private FlexboxLayout root;
    private Drawable itemBackground;
    private Drawable itemBackgroundSelected;
    private int listItemId;
    private ArrayList<ListItem> items;


    public TaskFilterList(Context context, FlexboxLayout flexboxLayout, LayoutInflater inflater) {
        this.context = context;
        this.root = flexboxLayout;
        this.inflater = inflater;


        this.itemBackground = this.context.getDrawable(R.drawable.filter_item_background_drawable);
        this.itemBackgroundSelected = this.context.getDrawable(R.drawable.filter_item_background_slected_drawable);

        this.listItemId = R.layout.task_filter_list_elements;


        this.items = new ArrayList<>();
    }

    public void updateList(Cursor cursor) {
        try {
            while (cursor.moveToNext()) {
                if(cursor.getInt(cursor.getColumnIndex(DatabaseDefines.FILTER_COUNT)) > 0) {
                    ListItem item = new ListItem(
                            this.root,
                            this.items.size(),
                            cursor.getString(cursor.getColumnIndex(DatabaseDefines.FILTER_NAME)),
                            cursor.getString(cursor.getColumnIndex(DatabaseDefines.FILTER_COUNT)));
                    this.items.add(item);
                }
            }

        } catch (Exception e) {
            Log.e("TaskFilterList", e.toString());
        } finally {
            cursor.close();
        }
    }

    private class ListItem {
        private int pos;
        private String title;
        private boolean selected;

        ListItem(FlexboxLayout root, int pos, String title, String count) {
            this.pos = pos;
            this.title = title;
            this.selected = false;

            View view = inflater.inflate(listItemId, root, false);
            TextView titleView = view.findViewById(R.id.filter_list_element_text);
            TextView countView = view.findViewById(R.id.filter_list_element_count);
            titleView.setText(title);
            countView.setText(count);
            FlexboxLayout.LayoutParams params = (FlexboxLayout.LayoutParams) view.getLayoutParams();
            params.setFlexGrow(1);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pL = v.getPaddingLeft();
                    int pT = v.getPaddingTop();
                    int pR = v.getPaddingRight();
                    int pB = v.getPaddingBottom();
                    Drawable back = selected ? itemBackground : itemBackgroundSelected;
                    v.setBackground(back);
                    v.setPadding(pL, pT, pR, pB);
                    selected = !selected;
                }
            });
            root.addView(view, params);
        }
    }
}
