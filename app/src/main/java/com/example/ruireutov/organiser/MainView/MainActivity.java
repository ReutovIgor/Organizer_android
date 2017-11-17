package com.example.ruireutov.organiser.MainView;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.os.Bundle;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.example.ruireutov.organiser.R;
import com.example.ruireutov.organiser.SideMenuBar;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private MainViewUIControl mainViewUIControl;
    private MainViewControl mainViewControl;
    private ExpandableListView expListView;
    private SideMenuBar sideMenuBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar myToolbar = (Toolbar) findViewById(R.id.mainView_toolbar);
        setSupportActionBar(myToolbar);

        DrawerLayout drawerLayout = findViewById(R.id.mainView_drawer);
        ListView drawerList = findViewById(R.id.navigation_list);
        this.sideMenuBar = new SideMenuBar(this, drawerLayout, drawerList, android.R.layout.simple_list_item_1, "MainView");

        this.expListView = findViewById(R.id.mainListView);
        this.mainViewUIControl = new MainViewUIControl(this, this.expListView);
        this.mainViewControl = new MainViewControl(this.mainViewUIControl);

//        List<String> strs = new ArrayList<>();
//        strs.add("line1");
//        strs.add("line2");
//        strs.add("line3");
//        strs.add("line4");
//        strs.add("line5");
//
//        this.mainViewControl.onDataChange("group1", strs);
    }

    @Override
    protected void onPause() {
        super.onPause();
        this.sideMenuBar.hideSidebar();
    }
}
