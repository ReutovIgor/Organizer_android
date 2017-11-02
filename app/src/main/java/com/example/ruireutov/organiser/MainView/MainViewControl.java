package com.example.ruireutov.organiser.MainView;

import android.util.Log;

import com.example.ruireutov.organiser.MainViewNotification;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ruireutov on 27-Oct-17.
 */

public class MainViewControl implements MainViewNotification {
    private List<String> groupHeaders;
    private HashMap<String, List<String>> groupItems;
    private List<String> notReadyComponents;
    private MainViewUIControl uiControl;

    //constants
    private static final String MAIN_VIEW_TAG = "MainViewControl";


    MainViewControl(MainViewUIControl uiControl) {
        this.uiControl = uiControl;
        this.groupHeaders = new ArrayList<>();
        this.groupItems = new HashMap<>();
        this.notReadyComponents = new ArrayList<>();
}

    private void appReady() {
        if(this.notReadyComponents.isEmpty()) {
            this.uiControl.setData(this.groupHeaders, this.groupItems);
        } else {
            Log.v(MAIN_VIEW_TAG, "Not all components are ready");
        }
    }

    @Override
    public void onComponentReady(String component) {
        this.notReadyComponents.remove(component);
        this.appReady();
    }

    @Override
    public void onDataChange(String component, List<String> data) {
        if(!this.groupHeaders.contains(component)) {
            this.groupHeaders.add(component);
        }
        this.groupItems.remove(component);
        this.groupItems.put(component, data);
        this.appReady();
    }

    @Override
    public void onDataChange() {

    }
}
