package com.example.ruireutov.organiser.task.taskList;

import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.example.ruireutov.organiser.task.ListCursorAdapter;
import com.example.ruireutov.organiser.R;
import com.example.ruireutov.organiser.SideMenuBar;
import com.example.ruireutov.organiser.task.TaskListFilter;

public class TaskListActivity extends AppCompatActivity implements ITaskListUiControl {
    private static final String PREFS_NAME = "TASK_LIST_PREFS";
    private static final String SHOW_COMPLETED = "TASK_LIST_COMPLETED";
    private static final String SHOW_OVERDUE = "TASK_LIST_OVERDUE";
    private static final String SORTING = "TASK_LIST_SORTING";
    private static final String FILTER = "TASK_LIST_FILTER";

    private SideMenuBar sideMenuBar;
    private ListView listView;
    private ListCursorAdapter cursorAdapter;
    private ITaskListControl listControl;
    private Button newTaskButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_list);
        Toolbar myToolbar = findViewById(R.id.toDoListView_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        DrawerLayout drawerLayout = findViewById(R.id.toDoList_drawer);
        ListView drawerList = findViewById(R.id.navigation_list);
        this.sideMenuBar = new SideMenuBar(this, drawerLayout, drawerList, android.R.layout.simple_list_item_1, "ToDoList");

        this.listView = findViewById(R.id.toDoListView_list);
        this.listView.setOnItemClickListener(new ListViewEventListener());

        this.newTaskButton = findViewById(R.id.new_task_button);
        this.newTaskButton.setOnClickListener(new ElementClickListener());

        this.listControl = new TaskListControl(this, this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.task_menu, menu);

        SharedPreferences settings = getSharedPreferences(TaskListActivity.PREFS_NAME, 0);
        menu.findItem(R.id.show_completed_button).setChecked(settings.getBoolean(TaskListActivity.SHOW_COMPLETED, false));
        menu.findItem(R.id.show_failed_button).setChecked(settings.getBoolean(TaskListActivity.SHOW_OVERDUE, false));

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences settings = getSharedPreferences(TaskListActivity.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        switch (item.getItemId()) {
            case R.id.categories_button:
                return true;
            case R.id.filter_button:
                return true;
            case R.id.show_completed_button:
                editor.putBoolean(TaskListActivity.SHOW_COMPLETED, !item.isChecked());
                editor.apply();
                item.setChecked(!item.isChecked());
                this.listControl.getTaskList(getFilters());
                break;
            case R.id.show_failed_button:
                editor.putBoolean(TaskListActivity.SHOW_OVERDUE, !item.isChecked());
                editor.apply();
                item.setChecked(!item.isChecked());
                this.listControl.getTaskList(getFilters());
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        this.listControl.getTaskList(getFilters());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.listControl.onDestroy();
    }

    private void listItemClick(int id) {
        this.listControl.showDetails(id);
    }

    private void createNewTask() {
        this.listControl.newTask();
    }

    private TaskListFilter getFilters() {
        SharedPreferences settings = getSharedPreferences(TaskListActivity.PREFS_NAME, 0);
        boolean b1 = settings.getBoolean(TaskListActivity.SHOW_OVERDUE, false);
        boolean b2 = settings.getBoolean(TaskListActivity.SHOW_COMPLETED, false);
        TaskListFilter filter = new TaskListFilter(
                settings.getBoolean(TaskListActivity.SHOW_OVERDUE, false),
                settings.getBoolean(TaskListActivity.SHOW_COMPLETED, false),
                "",
                "",
                null,
                null
        );


        return filter;
    }

    @Override
    public void updateList(Cursor cursor) {
        if (this.cursorAdapter == null) {
            this.cursorAdapter = new ListCursorAdapter(this, cursor, 0);
            this.listView.setAdapter(this.cursorAdapter);
        } else {
            this.cursorAdapter.swapCursor(cursor);
        }
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
