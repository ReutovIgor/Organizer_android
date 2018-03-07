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
import java.util.HashSet;
import java.util.Set;

public class TaskFilterList {
    private LayoutInflater inflater;
    private FlexboxLayout root;
    private Drawable itemBackground;
    private Drawable itemBackgroundSelected;
    private int listItemId;
    private ArrayList<ListItem> items;


    public TaskFilterList(Context context, FlexboxLayout flexboxLayout, LayoutInflater inflater) {
        this.root = flexboxLayout;
        this.inflater = inflater;

        this.itemBackground = context.getDrawable(R.drawable.filter_item_background_drawable);
        this.itemBackgroundSelected = context.getDrawable(R.drawable.filter_item_background_slected_drawable);

        this.listItemId = R.layout.task_filter_list_elements;


        this.items = new ArrayList<>();
    }

    public Set<String> getSelectedItems() {
        HashSet<String> set = new HashSet<>();

        for(ListItem item : this.items) {
            if(item.isSelected()) {
                set.add(item.getItemName());
            }
        }
        return set;
    }

    public void removeSelections() {
        for(ListItem item : this.items) {
            item.deselect();
        }
    }

    public void updateList(Cursor cursor) {
        try {
            while (cursor.moveToNext()) {
                if(cursor.getInt(cursor.getColumnIndex(DatabaseDefines.FILTER_COUNT)) > 0) {
                    ListItem item = new ListItem(
                            this.root,
                            cursor.getString(cursor.getColumnIndex(DatabaseDefines.FILTER_NAME)),
                            cursor.getString(cursor.getColumnIndex(DatabaseDefines.FILTER_COUNT)));
                    this.items.add(item);
                }
            }

        } catch (Exception e) {
            Log.e("TaskFilterList", e.toString());
        } finally {
            if(cursor != null) {
                cursor.close();
            }
        }
    }

    private class ListItem {
        private String name;
        private boolean selected;
        private View view;

        ListItem(FlexboxLayout root, String title, String count) {
            this.name = title;
            this.selected = false;

            this.view = inflater.inflate(listItemId, root, false);
            TextView titleView = this.view.findViewById(R.id.filter_list_element_text);
            TextView countView = this.view.findViewById(R.id.filter_list_element_count);
            titleView.setText(title);
            countView.setText(count);
            FlexboxLayout.LayoutParams params = (FlexboxLayout.LayoutParams) view.getLayoutParams();
            params.setFlexGrow(1);

            this.view.setOnClickListener(new View.OnClickListener() {
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
            root.addView(this.view, params);
        }

        public void deselect() {
            int pL = this.view.getPaddingLeft();
            int pT = this.view.getPaddingTop();
            int pR = this.view.getPaddingRight();
            int pB = this.view.getPaddingBottom();
            this.view.setBackground(itemBackground);
            this.view.setPadding(pL, pT, pR, pB);
            this.selected = !selected;
        }

        public boolean isSelected() {
            return this.selected;
        }

        public String getItemName() {
            return this.name;
        }
    }
}
