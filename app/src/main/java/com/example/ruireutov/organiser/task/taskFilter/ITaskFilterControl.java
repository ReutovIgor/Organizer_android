package com.example.ruireutov.organiser.task.taskFilter;

import java.util.Date;

public interface ITaskFilterControl {
    void getTaskFilters();
    void getNewCategories();
    void setShowOverdue(boolean state);
    void setShowCompleted(boolean state);
    void setEndByDate(Date date);
    void addCategory(String name);
    void removeCategory(String name);
    void addPriority(String name);
    void removePriority(String name);
    void saveNewFilters();
    void removeNewFilters();
    void resetFilters();
}
