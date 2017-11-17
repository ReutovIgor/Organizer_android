package com.example.ruireutov.organiser.ToDoList;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.ListView;

import com.example.ruireutov.organiser.DatabaseWorkers.TasksDatabaseWorker;
import com.example.ruireutov.organiser.R;
import com.example.ruireutov.organiser.SideMenuBar;

public class ToDoListActivity extends AppCompatActivity {

    private SideMenuBar sideMenuBar;
    private TasksDatabaseWorker taskWorker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toDoListView_toolbar);
        setSupportActionBar(myToolbar);

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.toDoListView_drawer);
        ListView drawerList = (ListView) findViewById(R.id.navigation_list);

        taskWorker = new TasksDatabaseWorker(this);
        String[] a = new String[10];
        Cursor c = this.taskWorker.get(a, "blah blah");

        this.sideMenuBar = new SideMenuBar(this, drawerLayout, drawerList, android.R.layout.simple_list_item_1, "ToDoList");
    }

}
