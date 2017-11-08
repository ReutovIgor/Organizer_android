package com.example.ruireutov.organiser.ToDoList;

import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.example.ruireutov.organiser.R;
import com.example.ruireutov.organiser.SideMenuBar;

public class ToDoListActivity extends AppCompatActivity {

    private SideMenuBar sideMenuBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.toDoListView_drawer);
        ListView drawerList = (ListView) findViewById(R.id.toDoListView_navList);

        this.sideMenuBar = new SideMenuBar(this, drawerLayout, drawerList, android.R.layout.simple_list_item_1, "ToDoList");
    }

}
