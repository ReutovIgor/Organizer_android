package com.example.ruireutov.organiser.task.main;

import java.util.HashMap;
import java.util.Objects;

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
        this.currentFragment = "";
        this.fragments = new HashMap<>();
        this.frame = frame;
        this.manager = manager;
        this.manager.addOnBackStackChangedListener(
            new FragmentManager.OnBackStackChangedListener() {
                public void onBackStackChanged() {
                    getCurrentFromBackStack();
                }
        });
    }

    private void getCurrentFromBackStack() {
        int length = manager.getBackStackEntryCount();
        if( length == 0 ) {
            currentFragment = TASK_LIST;
        } else {
            currentFragment = manager.getBackStackEntryAt(length - 1).getName();
        }
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
        return fragment;
    }

    public void showInitialFragment () {
        getCurrentFromBackStack();
        FragmentTransaction tr = this.manager.beginTransaction();
        tr.show(this.fragments.get(this.currentFragment));
        tr.commit();
    }

    public void showFragment(String name) {
        if(Objects.equals(name, this.currentFragment)) return;

        boolean skipBackStack = false;
        String key = name;
        if(Objects.equals(name, TASK_LIST)) {
            skipBackStack = true;
            key = "";
        }

        int length = this.manager.getBackStackEntryCount();
        int i = length - 1;
        while(i > -1) {
            String bKey = manager.getBackStackEntryAt(i).getName();
            if(Objects.equals(bKey, key)) {
                break;
            }
            this.manager.popBackStack();
            i--;
        }

        FragmentTransaction tr = this.manager.beginTransaction();
        tr.hide(this.fragments.get(this.currentFragment));
        tr.show(this.fragments.get(name));
        if(!skipBackStack) {
            tr.addToBackStack(name);
        }
        tr.commit();
        this.currentFragment = name;
    }
}
