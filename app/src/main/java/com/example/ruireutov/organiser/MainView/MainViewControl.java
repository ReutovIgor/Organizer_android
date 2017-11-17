package com.example.ruireutov.organiser.MainView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by ruireutov on 27-Oct-17.
 */

public class MainViewControl {
    private List<String> groupHeaders;
    private HashMap<String, List<String>> groupItems;
    private MainViewUIControl uiControl;

    //constants
    private static final String MAIN_VIEW_TAG = "MainViewControl";


    MainViewControl(MainViewUIControl uiControl) {
        this.uiControl = uiControl;
        this.groupHeaders = new ArrayList<>();
        this.groupItems = new HashMap<>();
    }
}
