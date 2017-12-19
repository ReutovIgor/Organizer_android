package com.example.ruireutov.organiser.ToDoList;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.ruireutov.organiser.ListCursorAdapter;
import com.example.ruireutov.organiser.R;
import com.example.ruireutov.organiser.SideMenuBar;

public class ToDoListActivity extends AppCompatActivity implements IToDoListUiControl {

    private SideMenuBar sideMenuBar;
    private ListView listView;
    private ListCursorAdapter cursorAdapter;
    private IToDoListControl listControl;
    private Button newTaskButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do_list);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.toDoListView_toolbar);
        setSupportActionBar(myToolbar);

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.toDoList_drawer);
        ListView drawerList = (ListView) findViewById(R.id.navigation_list);
        this.sideMenuBar = new SideMenuBar(this, drawerLayout, drawerList, android.R.layout.simple_list_item_1, "ToDoList");

        this.listView = findViewById(R.id.toDoListView_list);
        this.cursorAdapter = new ListCursorAdapter(this, null, 0);
        this.listView.setAdapter(this.cursorAdapter);
        this.listView.setOnItemClickListener(new ListViewEventListener());

        this.newTaskButton = findViewById(R.id.new_task_button);
        this.newTaskButton.setOnClickListener(new ElementClickListener());

        this.listControl = new ToDoListControl(this, this);
        this.listControl.getTaskList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.listControl.getTaskList();
    }

    private void listItemClick(int id) {
        this.listControl.showDetails(id);
    }

    private void createNewTask() {
        this.listControl.newTask();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.listControl.onDestroy();
    }

    @Override
    public void updateList(Cursor cursor) {
        this.cursorAdapter.swapCursor(cursor);
    }

    private class ListViewEventListener implements AdapterView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            listItemClick(position);
        }
    }

    private class ElementClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.new_task_button:
                    createNewTask();
                    break;
            }
        }
    }
}
