package com.example.ruireutov.organiser.task;

import java.util.HashMap;

import android.support.v4.app.FragmentTransaction;
import android.widget.FrameLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;

public class TaskFragmentManager {
    public static String TASK_LIST       = "list";
    public static String TASK_FILTER     = "filter";
    public static String TASK_DETAILS    = "details";
    public static String TASK_CATEGORIES = "categories";

    private FrameLayout frame;
    private HashMap<String, Fragment> fragments;
    private String currentFragment;
    private FragmentManager manager;

    public TaskFragmentManager (FrameLayout frame, final FragmentManager manager){
        this.fragments = new HashMap<>();
        this.frame = frame;
        this.manager = manager;
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

    public void addFragment (String name, Fragment fragment) {
        this.fragments.put(name, fragment);
        this.manager.beginTransaction().add(this.frame.getId(), this.fragments.get(name)).hide(this.fragments.get(name)).commit();
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
