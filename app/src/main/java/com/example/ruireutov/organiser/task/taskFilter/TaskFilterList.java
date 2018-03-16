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
import com.example.ruireutov.organiser.task.TaskDefines;
import com.google.android.flexbox.FlexboxLayout;

import java.util.ArrayList;
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

    void fillList(Cursor cursor, Set<String> selectedItems) {
        try {
            while (cursor.moveToNext()) {
                String name;
                if(Objects.equals(this.filterKey, TaskDefines.SELECTED_CATEGORIES)) {
                    name = cursor.getString(cursor.getColumnIndex(DatabaseDefines.CATEGORIES_NAME));
                } else {
                    name = cursor.getString(cursor.getColumnIndex(DatabaseDefines.PRIORITIES_NAME));
                }
                ListItem item = new ListItem(this.root, name);
                this.items.add(item);
                if(selectedItems.contains(name)) {
                    item.select();
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

    void updateItemCount(Cursor cursor) {
        try {
            int lastIndex = this.items.size() - 1;
            ArrayList<Integer> updatedItems = new ArrayList<>();
            while (cursor.moveToNext()) {
                int count = cursor.getInt(cursor.getColumnIndex(DatabaseDefines.FILTER_COUNT));
                if( count > 0) {
                    String name = cursor.getString(cursor.getColumnIndex(DatabaseDefines.FILTER_NAME));
                    for( ListItem i : this.items) {
                        if(Objects.equals(i.getName(), name)) {
                            i.setName(name);
                            i.setCount(count);
                            updatedItems.add(this.items.indexOf(i));
                            break;
                        }
                    }
                }
            }

            for(int i = lastIndex; i > -1; i--) {
                int updated = updatedItems.indexOf(i);
                if(updated == -1) {
                    this.items.get(i).setCount(0);
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

        ListItem(FlexboxLayout root, String title) {
            this.name = title;
            this.selected = false;

            this.view = inflater.inflate(R.layout.task_filter_list_elements, root, false);
            TextView titleView = this.view.findViewById(R.id.filter_list_element_text);
            titleView.setText(title);

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
            root.addView(this.view);
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
