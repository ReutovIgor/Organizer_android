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
import com.example.ruireutov.organiser.task.taskDetails.ITaskDetailsActivityControl;
import com.example.ruireutov.organiser.task.taskDetails.TaskDetailsFragment;
import com.example.ruireutov.organiser.task.taskList.ITaskListActivityControl;
import com.example.ruireutov.organiser.task.taskList.TaskListFragment;

import java.util.HashMap;

public class TaskActivity extends AppCompatActivity implements IFragmentNotifier, ITaskActivity, ITaskActivityTaskFilterControl{
    private ToolBarControl toolBarControl;
    private SideMenuBar sideMenuBar;
    private ITaskListActivityControl taskListFragment;
    private ITaskDetailsActivityControl taskDetailsFragment;
    private TaskFragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        setSupportActionBar((Toolbar) findViewById(R.id.task_list_tool_bar));
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        this.fragmentManager = new TaskFragmentManager(this, (FrameLayout) findViewById(R.id.filter_frame), getSupportFragmentManager());
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

        this.toolBarControl = new ToolBarControl((Toolbar) findViewById(R.id.task_list_tool_bar));
        this.toolBarControl.addMenuItem(ToolBarControl.MENU_APPLY_FILTER, menu.findItem(R.id.apply_filter_button));
        this.toolBarControl.addMenuItem(ToolBarControl.MENU_CANCEL_FILTER, menu.findItem(R.id.cancel_filter_button));
        this.toolBarControl.addMenuItem(ToolBarControl.MENU_RESET_FILTER, menu.findItem(R.id.reset_filter_button));
        this.toolBarControl.addMenuItem(ToolBarControl.MENU_FILTER, menu.findItem(R.id.filter_button));
        this.toolBarControl.addMenuItem(ToolBarControl.MENU_CATEGORIES, menu.findItem(R.id.categories_button));
        this.toolBarControl.addMenuItem(ToolBarControl.MENU_SHOW_COMPLETED, menu.findItem(R.id.show_completed_button));
        this.toolBarControl.addMenuItem(ToolBarControl.MENU_SHOW_OVERDUE, menu.findItem(R.id.show_failed_button));

        SharedPreferences settings = getSharedPreferences(TaskDefines.PREFS_NAME, 0);
        this.toolBarControl.changeCheckState(ToolBarControl.MENU_SHOW_COMPLETED, settings.getBoolean(TaskDefines.SHOW_COMPLETED, false));
        this.toolBarControl.changeCheckState(ToolBarControl.MENU_SHOW_OVERDUE, settings.getBoolean(TaskDefines.SHOW_OVERDUE, false));

        //apply correct tool bar on init
        this.onFragmentChange(this.fragmentManager.getCurrentFragment());

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        SharedPreferences settings = getSharedPreferences(TaskDefines.PREFS_NAME, 0);
        SharedPreferences.Editor editor = settings.edit();
        switch (item.getItemId()) {
            case R.id.apply_filter_button:

                break;
            case R.id.cancel_filter_button:

                break;
            case R.id.reset_filter_button:

                break;
            case R.id.categories_button:
                this.showCategories();
                break;
            case R.id.filter_button:
                this.showFilters();
                break;
            case R.id.show_completed_button:
                editor.putBoolean(TaskDefines.SHOW_COMPLETED, !item.isChecked());
                editor.apply();
                this.toolBarControl.changeCheckState(ToolBarControl.MENU_SHOW_COMPLETED, !item.isChecked());
                this.taskListFragment.updateTaskListData();
                break;
            case R.id.show_failed_button:
                editor.putBoolean(TaskDefines.SHOW_OVERDUE, !item.isChecked());
                editor.apply();
                this.toolBarControl.changeCheckState(ToolBarControl.MENU_SHOW_OVERDUE, !item.isChecked());
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

    @Override
    public void onFragmentChange(String name) {
        if(this.toolBarControl == null) {
            return;
        }
        switch (name) {
            case TaskFragmentManager.TASK_LIST:
                this.toolBarControl.showTaskListBar();
                break;
            case TaskFragmentManager.TASK_DETAILS:
                this.toolBarControl.showTaskDetailsBar();
                break;
            case TaskFragmentManager.TASK_FILTER:
                this.toolBarControl.showTaskFiltersBar();
                break;
            case TaskFragmentManager.TASK_CATEGORIES:
                this.toolBarControl.showTaskCategoriesBar();
                break;
        }
    }

    class ToolBarControl {
        static final String MENU_FILTER = "FILTER_BUTTON";
        static final String MENU_CATEGORIES = "CATEGORIES_BUTTON";
        static final String MENU_SHOW_COMPLETED = "SHOW_COMPLETED_BUTTON";
        static final String MENU_SHOW_OVERDUE = "SHOW_OVERDUE_BUTTON";
        static final String MENU_APPLY_FILTER = "APPLY_FILTER_BUTTON";
        static final String MENU_CANCEL_FILTER = "CANCEL_FILTER_BUTTON";
        static final String MENU_RESET_FILTER = "RESET_FILTER_BUTTON";

        private Toolbar bar;
        private HashMap<String, MenuItem> menuItems;

        ToolBarControl(Toolbar bar) {
            this.menuItems = new HashMap<>();
            this.bar = bar;
        }

        void addMenuItem(String name, MenuItem item) {
            this.menuItems.put(name, item);
        }

        void changeCheckState(String name, boolean state) {
            this.menuItems.get(name).setChecked(state);
        }

        void showTaskListBar() {
            this.bar.setTitle(R.string.title_activity_task_list);
            this.menuItems.get(MENU_FILTER).setVisible(true);
            this.menuItems.get(MENU_CATEGORIES).setVisible(true);
            this.menuItems.get(MENU_SHOW_COMPLETED).setVisible(true);
            this.menuItems.get(MENU_SHOW_OVERDUE).setVisible(true);
            this.menuItems.get(MENU_APPLY_FILTER).setVisible(false);
            this.menuItems.get(MENU_CANCEL_FILTER).setVisible(false);
            this.menuItems.get(MENU_RESET_FILTER).setVisible(false);
        }

        void showTaskDetailsBar() {
            this.bar.setTitle(R.string.title_activity_task_details);
            this.menuItems.get(MENU_FILTER).setVisible(false);
            this.menuItems.get(MENU_CATEGORIES).setVisible(true);
            this.menuItems.get(MENU_SHOW_COMPLETED).setVisible(true);
            this.menuItems.get(MENU_SHOW_OVERDUE).setVisible(true);
            this.menuItems.get(MENU_APPLY_FILTER).setVisible(false);
            this.menuItems.get(MENU_CANCEL_FILTER).setVisible(false);
            this.menuItems.get(MENU_RESET_FILTER).setVisible(false);
        }

        void showTaskFiltersBar() {
            this.bar.setTitle(R.string.title_activity_task_filters);
            this.menuItems.get(MENU_FILTER).setVisible(false);
            this.menuItems.get(MENU_CATEGORIES).setVisible(false);
            this.menuItems.get(MENU_SHOW_COMPLETED).setVisible(false);
            this.menuItems.get(MENU_SHOW_OVERDUE).setVisible(false);
            this.menuItems.get(MENU_APPLY_FILTER).setVisible(true);
            this.menuItems.get(MENU_CANCEL_FILTER).setVisible(true);
            this.menuItems.get(MENU_RESET_FILTER).setVisible(true);
        }

        void showTaskCategoriesBar() {}
    }
}
