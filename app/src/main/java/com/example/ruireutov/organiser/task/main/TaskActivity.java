package com.example.ruireutov.organiser.task.main;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.FrameLayout;
import android.widget.ListView;

import com.example.ruireutov.organiser.R;
import com.example.ruireutov.organiser.SideMenuBar;
import com.example.ruireutov.organiser.task.TaskDefines;
import com.example.ruireutov.organiser.task.TaskDetailsData;
import com.example.ruireutov.organiser.task.TaskFragmentManager;
import com.example.ruireutov.organiser.task.taskDetails.ITaskDetailsActivityControl;
import com.example.ruireutov.organiser.task.taskDetails.TaskDetailsFragment;
import com.example.ruireutov.organiser.task.taskFilter.TaskFilterFragment;
import com.example.ruireutov.organiser.task.taskList.ITaskListActivityControl;
import com.example.ruireutov.organiser.task.taskList.TaskListFragment;

public class TaskActivity extends AppCompatActivity implements ITaskActivity{
    private SideMenuBar sideMenuBar;
    private ITaskListActivityControl taskListFragment;
    private ITaskDetailsActivityControl taskDetailsFragment;
    private TaskFragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);
        Toolbar myToolbar = findViewById(R.id.task_list_tool_bar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        this.fragmentManager = new TaskFragmentManager((FrameLayout) findViewById(R.id.filter_frame), getSupportFragmentManager());
        this.taskListFragment = (TaskListFragment) this.fragmentManager.addFragment(TaskFragmentManager.TASK_LIST);
        this.taskDetailsFragment = (TaskDetailsFragment) this.fragmentManager.addFragment(TaskFragmentManager.TASK_DETAILS);
        this.fragmentManager.addFragment(TaskFragmentManager.TASK_FILTER);
        //this.fragmentManager.addFragment(TaskFragmentManager.TASK_CATEGORIES);

        this.fragmentManager.showInitialFragment();

        DrawerLayout drawerLayout = findViewById(R.id.toDoList_drawer);
        ListView drawerList = findViewById(R.id.navigation_list);
        this.sideMenuBar = new SideMenuBar(this, drawerLayout, drawerList, android.R.layout.simple_list_item_1, "ToDoList");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.task_menu, menu);

        SharedPreferences settings = getSharedPreferences(TaskDefines.PREFS_NAME, 0);
        menu.findItem(R.id.show_completed_button).setChecked(settings.getBoolean(TaskDefines.SHOW_COMPLETED, false));
        menu.findItem(R.id.show_failed_button).setChecked(settings.getBoolean(TaskDefines.SHOW_OVERDUE, false));

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences settings = getSharedPreferences(TaskDefines.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        switch (item.getItemId()) {
            case R.id.categories_button:
                this.showCategories();
                return true;
            case R.id.filter_button:
                this.showFilters();
                return true;
            case R.id.show_completed_button:
                editor.putBoolean(TaskDefines.SHOW_COMPLETED, !item.isChecked());
                editor.apply();
                item.setChecked(!item.isChecked());
                this.taskListFragment.updateTaskListData();
                break;
            case R.id.show_failed_button:
                editor.putBoolean(TaskDefines.SHOW_OVERDUE, !item.isChecked());
                editor.apply();
                item.setChecked(!item.isChecked());
                this.taskListFragment.updateTaskListData();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }

    @Override
    public void onTaskListUpdate() {
        this.taskListFragment.updateTaskListData();
    }

    @Override
    public void showDetails(TaskDetailsData data) {
        this.taskDetailsFragment.applyTaskDetails(data);
        this.fragmentManager.showFragment(TaskFragmentManager.TASK_DETAILS);
    }

    @Override
    public void showTaskCreation() {
        this.taskDetailsFragment.applyTaskDetails(null);
        this.fragmentManager.showFragment(TaskFragmentManager.TASK_DETAILS);
    }

    @Override
    public void showTaskList() {
        this.fragmentManager.showFragment(TaskFragmentManager.TASK_LIST);
    }

    @Override
    public void showFilters() {
        this.fragmentManager.showFragment(TaskFragmentManager.TASK_FILTER);
    }

    @Override
    public void showCategories() {

    }
}
