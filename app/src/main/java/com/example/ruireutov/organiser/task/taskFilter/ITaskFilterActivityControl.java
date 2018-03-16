package com.example.ruireutov.organiser.task.taskFilter;

/**
 * Created by ruireutov on 16-Mar-18.
 */

public interface ITaskFilterActivityControl {
    void onShow();
    void onFilterUpdate();
    void onShowOverdueChange(boolean state);
    void onShowCompletedChange(boolean state);
    void applyNewFilters();
    void cancelNewFilters();
    void resetFilters();
}
