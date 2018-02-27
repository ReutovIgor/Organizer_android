package com.example.ruireutov.organiser.task;

import java.util.HashMap;
import java.util.List;

import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

import com.example.ruireutov.organiser.task.taskDetails.TaskDetailsFragment;
import com.example.ruireutov.organiser.task.taskFilter.TaskFilterFragment;
import com.example.ruireutov.organiser.task.taskList.TaskListFragment;

public class TaskFragmentManager {
    public final static String TASK_LIST       = "list";
    public final static String TASK_FILTER     = "filter";
    public final static String TASK_DETAILS    = "details";
    public final static String TASK_CATEGORIES = "categories";

    private FrameLayout frame;
    private HashMap<String, Fragment> fragments;
    private String currentFragment;
    private FragmentManager manager;

    public TaskFragmentManager (FrameLayout frame, final FragmentManager manager){
        this.fragments = new HashMap<>();
        this.frame = frame;
        this.manager = manager;

        List<Fragment> a = this.manager.getFragments();
        int count = a.size();
        int backstack = this.manager.getBackStackEntryCount();
        this.manager.addOnBackStackChangedListener(
            new FragmentManager.OnBackStackChangedListener() {
                public void onBackStackChanged() {
                    int length = manager.getBackStackEntryCount();
                    if( length == 0 ) {
                        currentFragment = TASK_LIST;
                    } else {
                        String current = manager.getBackStackEntryAt(length - 1).getName();
                        currentFragment = current;
                    }
                }
            });
        this.currentFragment = "";
    }

    public Fragment addFragment (String name) {
        Fragment fragment = this.manager.findFragmentByTag(name);
        if(fragment == null) {
            switch (name) {
                case TASK_LIST:
                    fragment = new TaskListFragment();
                    break;
                case TASK_DETAILS:
                    fragment = new TaskDetailsFragment();
                    break;
                case TASK_FILTER:
                    fragment = new TaskFilterFragment();
                    break;
                case TASK_CATEGORIES:
                    //TODO implement later
                    break;
            }
            this.manager.beginTransaction().add(this.frame.getId(), fragment, name).hide(fragment).commit();
        }
        this.fragments.put(name, fragment);
        return null;
    }

    public void showInitialFragment () {
        int length = this.manager.getBackStackEntryCount();
        for(int i = 0; i < length; i++) {
            this.manager.popBackStack();
        }
        FragmentTransaction tr = this.manager.beginTransaction();
        tr.show(this.fragments.get(TASK_LIST));
        tr.commit();
        this.currentFragment = TASK_LIST;
    }

    public void showFragment(String name) {
        FragmentTransaction tr = this.manager.beginTransaction();
        tr.hide(this.fragments.get(this.currentFragment));
        tr.show(this.fragments.get(name));
        tr.addToBackStack(name);
        tr.commit();
    }
}
