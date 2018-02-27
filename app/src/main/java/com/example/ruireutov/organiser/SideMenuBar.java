package com.example.ruireutov.organiser;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.example.ruireutov.organiser.MainView.MainActivity;
import com.example.ruireutov.organiser.task.main.TaskActivity;

import java.util.HashMap;

/**
 * Created by ruireutov on 07-Nov-17.
 */

public class SideMenuBar {

    private ArrayAdapter<String> mAdapter;
    private Context context;
    private DrawerLayout drawer;
    private ListView sidebar;
    private HashMap<String, Class> components;
    private String[] screens;
    private String componentName;

    public SideMenuBar(Context context, DrawerLayout drawer, ListView sidebar, int listItem, String componentName) {
        this.drawer = drawer;
        this.sidebar = sidebar;
        this.componentName = componentName;
        this.screens = new String[] {"MainView" , "ToDoList"};
        this.context = context;
        this.components = new HashMap<>();
        this.components.put("MainView", MainActivity.class);
        this.components.put("ToDoList", TaskActivity.class);

        this.mAdapter = new ArrayAdapter<>(context, listItem, this.screens);
        sidebar.setAdapter(this.mAdapter);
        sidebar.setOnItemClickListener(new DrawerItemClickListener());
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            changeActivity(position);
        }
    }

    private void changeActivity(int pos) {
        String comp = this.screens[pos];
        if(comp.equals(this.componentName)) {
            this.drawer.closeDrawer(this.sidebar);
        } else {
            Intent i = new Intent(context, this.components.get(comp));
            context.startActivity(i);
        }
    }

    public void hideSidebar() {
        this.drawer.closeDrawers();
    }
}


