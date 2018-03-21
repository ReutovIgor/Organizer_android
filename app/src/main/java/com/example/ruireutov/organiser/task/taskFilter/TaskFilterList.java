package com.example.ruireutov.organiser.task.taskFilter;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ruireutov.organiser.R;
import com.example.ruireutov.organiser.databaseWorkers.DatabaseDefines;
import com.example.ruireutov.organiser.task.TaskDefines;
import com.google.android.flexbox.FlexboxLayout;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

class TaskFilterList {
    private Context context;
    private String filterKey;
    private LayoutInflater inflater;
    private FlexboxLayout root;
    private ArrayList<ListItem> items;
    private ArrayList<ListItem> selectedItems;
    private ITaskFilterListNotification taskFilterListNotification;

    TaskFilterList(Context context, FlexboxLayout flexboxLayout, LayoutInflater inflater,ITaskFilterListNotification notifier, String filterKey) {
        this.context = context;
        this.filterKey = filterKey;
        this.root = flexboxLayout;
        this.inflater = inflater;
        this.taskFilterListNotification = notifier;

        this.items = new ArrayList<>();
        this.selectedItems = new ArrayList<>();
    }

    void fillList(Cursor cursor) {
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
        Log.i("TaskFilterList", "updateItemCount called for " + this.filterKey);
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
                            if(this.selectedItems.indexOf(i) != -1) {
                                i.setSelected();
                            } else if(this.selectedItems.size() > 0) {
                                i.setDeselected();
                            } else {
                                i.setDeselectedDefault();;
                            }
                            break;
                        }
                    }
                }
            }

            for(int i = lastIndex; i > -1; i--) {
                int updated = updatedItems.indexOf(i);
                if(updated == -1) {
                    ListItem item = this.items.get(i);
                    item.setCount(0);
                    item.setDisabled();
                }
            }
        } catch (Exception e) {
            Log.e("TaskFilterList", e.toString());
        } finally {
            if(cursor != null) {
                Log.i("TaskFilterList", "closing cursor!");
                cursor.close();
            }
        }
    }

    void updateItemSelection(Set<String> selectedItems) {
        for( ListItem i : this.items) {
            if(selectedItems.contains(i.getName())) {
                this.selectedItems.add(i);
            }
        }
    }

    private void onItemSelect(ListItem item) {
        if(this.selectedItems.indexOf(item) == -1) {
            this.selectedItems.add(item);
            item.setSelected();
        }

        if(this.selectedItems.size() == 1) {
            for( ListItem i : this.items) {
                if(this.selectedItems.indexOf(i) == -1 && i.getCount() > 0) {
                    i.setDeselected();
                }
            }
        }
    }

    private void onItemDeselect(ListItem item) {
        if(this.selectedItems.indexOf(item) != -1) {
            this.selectedItems.remove(item);
        }
        if(this.selectedItems.size() == 0) {
            for( ListItem i : this.items) {
                if(i.getCount() > 0) {
                    i.setDeselectedDefault();
                }
            }
        } else {
            item.setDeselected();
        }
    }

    private class ListItem {
        private String name;
        private int count;
        private boolean selected;
        private View view;
        private TextView tName;
        private TextView tCount;

        ListItem(FlexboxLayout root, String title) {
            this.name = title;
            this.selected = false;
            this.count = 0;

            this.view = inflater.inflate(R.layout.task_filter_list_elements, root, false);
            this.tName = this.view.findViewById(R.id.filter_list_element_text);
            this.tName.setText(title);
            this.tCount = this.view.findViewById(R.id.filter_list_element_count);
            this.tCount.setText(String.valueOf(this.count));

            this.view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(count == 0) return;
                    selected = !selected;
                    if(selected) {
                        Log.i("TaskFilterList", "On item selected " + name + " start");
                        select();
                        taskFilterListNotification.onGroupItemSelect(filterKey, name);
                        Log.i("TaskFilterList", "On item selected " + name + " end");
                    } else {
                        Log.i("TaskFilterList", "On item deselected " + name + " start");
                        deselect();
                        taskFilterListNotification.onGroupItemDeselect(filterKey, name);
                        Log.i("TaskFilterList", "On item deselected " + name + " end");
                    }
                }
            });
            root.addView(this.view);
        }

        private void select() {
            onItemSelect(this);
        }

        private void deselect() {
            onItemDeselect(this);
        }

        void remove() {
            root.removeView(this.view);
        }

        void setCount(int count) {
            this.count = count;
            this.tCount.setText(String.valueOf(count));
        }

        int getCount() { return this.count;}

        void setName(String name) {
            this.name = name;
            this.tName.setText(name);
        }

        String getName() { return this.name; }

        void setSelected() {
            this.selected = true;
            int colorHex = ContextCompat.getColor(context, R.color.filterTextEnabled);
            this.tName.setTextColor(colorHex);
            this.tCount.setTextColor(colorHex);
            this.view.setBackground(context.getDrawable(R.drawable.filter_item_background_selected));
        }

        void setDeselectedDefault() {
            this.selected = false;
            int colorHex = ContextCompat.getColor(context, R.color.filterTextEnabled);
            this.tName.setTextColor(colorHex);
            this.tCount.setTextColor(colorHex);
            this.view.setBackground(context.getDrawable(R.drawable.filter_item_background_default));
        }

        void setDeselected() {
            this.selected = false;
            int colorHex = ContextCompat.getColor(context, R.color.filterTextEnabled);
            this.tName.setTextColor(colorHex);
            this.tCount.setTextColor(colorHex);
            this.view.setBackground(context.getDrawable(R.drawable.filter_item_background));
        }

        void setDisabled() {
            int colorHex = ContextCompat.getColor(context, R.color.filterTextDisabled);
            this.tName.setTextColor(colorHex);
            this.tCount.setTextColor(colorHex);
            this.view.setBackground(context.getDrawable(R.drawable.filter_item_background));
        }
    }
}
