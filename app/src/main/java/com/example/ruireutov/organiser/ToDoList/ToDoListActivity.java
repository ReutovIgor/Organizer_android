package com.example.ruireutov.organiser.ToDoList;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.ruireutov.organiser.ListCursorAdapter;
import com.example.ruireutov.organiser.R;
import com.example.ruireutov.organiser.SideMenuBar;

public class ToDoListActivity extends AppCompatActivity implements IToDoListUiControl {

    private SideMenuBar sideMenuBar;
    private ListView listView;
    private ListCursorAdapter cursorAdapter;
    private IToDoListControl listControl;

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

        this.listControl = new ToDoListControl(this, this);
    }

    private void listItemClick(int id) {
        this.listControl.showDetails(id);
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
}
