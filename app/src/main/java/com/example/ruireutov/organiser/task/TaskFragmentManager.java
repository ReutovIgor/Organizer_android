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

    public TaskFragmentManager (FrameLayout frame, FragmentManager manager){
        this.fragments = new HashMap<>();
        this.frame = frame;
        this.manager = manager;
        this.currentFragment = "";
    }

    public void addFragment (String name, Fragment fragment) {
        this.fragments.put(name, fragment);
        this.manager.beginTransaction().add(this.frame.getId(), this.fragments.get(name)).hide(this.fragments.get(name)).commit();
    }

    public void showFragment(String name) {
        FragmentTransaction tr = this.manager.beginTransaction();
        if(this.currentFragment != "") {
            tr.hide(this.fragments.get(this.currentFragment));
        }
        tr.show(this.fragments.get(name));
        //if(this.currentFragment == "") {
            tr.addToBackStack(null);
        //}
        tr.commit();
        this.currentFragment = name;
    }
}
