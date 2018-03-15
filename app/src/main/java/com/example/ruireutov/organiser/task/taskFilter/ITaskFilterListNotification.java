package com.example.ruireutov.organiser.task.taskFilter;

/**
 * Created by ruireutov on 14-Mar-18.
 */

public interface ITaskFilterListNotification {
    void onGroupItemSelect(String key, String name);
    void onGroupItemDeselect(String key, String name);
}
