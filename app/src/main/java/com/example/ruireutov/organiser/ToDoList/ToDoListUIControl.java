package com.example.ruireutov.organiser.ToDoList;

import android.content.Context;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ruireutov on 03-Nov-17.
 */

public class ToDoListUIControl {
    private List<String> items;
    private ArrayAdapter<String> adapter;
    ToDoListUIControl(Context context, ListView listView, int listId) {
        this.items = new ArrayList<>();
        context.getResources();
        this.adapter = new ArrayAdapter<>(context, listId, this.items);
        listView.setAdapter(this.adapter);

    }
}
