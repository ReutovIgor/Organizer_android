package com.example.ruireutov.organiser.MainView;

import android.content.Context;
import android.database.Cursor;
import android.widget.ExpandableListView;

import com.example.ruireutov.organiser.DatabaseWorkers.DatabaseControl;
import com.example.ruireutov.organiser.ExpListAdapter;
import com.example.ruireutov.organiser.ExpListCursorAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ruireutov on 31-Oct-17.
 */

public class MainViewUIControl {
    private ExpListAdapter expListAdapter;
    private ExpListCursorAdapter expListCursorAdapter;
    private Context context;


    MainViewUIControl(Context context, ExpandableListView view) {
        this.context = context;
        //this.expListAdapter = new ExpListAdapter(context, new ArrayList<String>(), new HashMap<String, List<String>>());

//        DatabaseControl db = DatabaseControl.getInstance(context);
//        db.open();
//        Cursor c = db.getGroupTable();

//        this.expListCursorAdapter = new ExpListCursorAdapter(c, context);
        //view.setAdapter(this.expListAdapter);
//        view.setAdapter(this.expListCursorAdapter);
    }

    public void onActivityDestroy() {
//        DatabaseControl db = DatabaseControl.getInstance(context);
//        db.close();
    }

}
