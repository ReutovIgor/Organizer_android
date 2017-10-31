package com.example.ruireutov.organiser;

import java.util.List;

/**
 * Created by ruireutov on 31-Oct-17.
 */

public interface MainViewNotification {
    void onComponentReady(String component);
    void onDataChange(String component, List<String> data);
    void onDataChange();
}
