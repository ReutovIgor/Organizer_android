package com.example.ruireutov.organiser.MainView;

import android.content.Context;
import android.widget.ExpandableListView;

import com.example.ruireutov.organiser.task.ExpListAdapter;
import com.example.ruireutov.organiser.task.ExpListCursorAdapter;

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
