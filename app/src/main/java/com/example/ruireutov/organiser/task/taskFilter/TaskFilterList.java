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
import java.util.Objects;
import java.util.Set;

class TaskFilterList {
    private String filterKey;
    private LayoutInflater inflater;
    private FlexboxLayout root;
    private Drawable itemBackground;
    private Drawable itemBackgroundSelected;
    private ArrayList<ListItem> items;
    private ITaskFilterListNotification taskFilterListNotification;

    TaskFilterList(Context context, FlexboxLayout flexboxLayout, LayoutInflater inflater,ITaskFilterListNotification notifier, String filterKey) {
        this.filterKey = filterKey;
        this.root = flexboxLayout;
        this.inflater = inflater;
        this.taskFilterListNotification = notifier;

        this.itemBackground = context.getDrawable(R.drawable.filter_item_background_drawable);
        this.itemBackgroundSelected = context.getDrawable(R.drawable.filter_item_background_slected_drawable);

        this.items = new ArrayList<>();
    }

    Set<String> getSelectedItems() {
        HashSet<String> set = new HashSet<>();

        for(ListItem item : this.items) {
            if(item.isSelected()) {
                set.add(item.getName());
            }
        }
        return set;
    }

    void updateList(Cursor cursor) {
        try {
            int lastIndex = this.items.size() - 1;
            ArrayList<Integer> updatedItems = new ArrayList<>();
            while (cursor.moveToNext()) {
                int count = cursor.getInt(cursor.getColumnIndex(DatabaseDefines.FILTER_COUNT));
                if( count > 0) {
                    int pos = -1;
                    String name = cursor.getString(cursor.getColumnIndex(DatabaseDefines.FILTER_NAME));
                    for( ListItem i : this.items) {
                        if(Objects.equals(i.getName(), name)) {
                            pos = this.items.indexOf(i);
                            break;
                        }
                    }

                    if (pos != -1) {
                        ListItem item = this.items.get(pos);
                        item.setName(name);
                        item.setCount(count);
                        updatedItems.add(pos);
                    } else {
                        ListItem item = new ListItem(this.root, name, count);
                        this.items.add(item);
                    }
                }
            }
            for(int i = lastIndex; i > -1; i--) {
                int updated = updatedItems.indexOf(i);
                if(updated == -1) {
                    this.items.get(i).remove();
                    this.items.remove(i);
                }
            }

            //i.remove();
            //this.items.remove(pos);
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

        ListItem(FlexboxLayout root, String title, int count) {
            this.name = title;
            this.selected = false;

            this.view = inflater.inflate(R.layout.task_filter_list_elements, root, false);
            TextView titleView = this.view.findViewById(R.id.filter_list_element_text);
            TextView countView = this.view.findViewById(R.id.filter_list_element_count);
            titleView.setText(title);
            countView.setText(String.valueOf(count));
            FlexboxLayout.LayoutParams params = (FlexboxLayout.LayoutParams) view.getLayoutParams();
            params.setFlexGrow(1);

            this.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selected = !selected;
                    if(selected) {
                        select();
                        taskFilterListNotification.onGroupItemSelect(filterKey, name);
                    } else {
                        deselect();
                        taskFilterListNotification.onGroupItemDeselect(filterKey, name);
                    }
                }
            });
            root.addView(this.view, params);
        }

        void select() {
            int pL = this.view.getPaddingLeft();
            int pT = this.view.getPaddingTop();
            int pR = this.view.getPaddingRight();
            int pB = this.view.getPaddingBottom();
            this.view.setBackground(itemBackgroundSelected);
            this.view.setPadding(pL, pT, pR, pB);
            this.selected = true;
        }

        void deselect() {
            int pL = this.view.getPaddingLeft();
            int pT = this.view.getPaddingTop();
            int pR = this.view.getPaddingRight();
            int pB = this.view.getPaddingBottom();
            this.view.setBackground(itemBackground);
            this.view.setPadding(pL, pT, pR, pB);
            this.selected = false;
        }

        void remove() {
            root.removeView(this.view);
        }

        boolean isSelected() { return this.selected; }

        String getName() { return this.name; }

        void setCount(int count) {
            TextView t = this.view.findViewById(R.id.filter_list_element_count);
            t.setText(String.valueOf(count));
        }

        void setName(String name) {
            this.name = name;
            TextView t = this.view.findViewById(R.id.filter_list_element_text);
            t.setText(name);
        }

    }
}
