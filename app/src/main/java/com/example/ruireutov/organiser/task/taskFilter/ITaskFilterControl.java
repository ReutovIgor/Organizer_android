package com.example.ruireutov.organiser.task.taskFilter;

public interface ITaskFilterControl {
    void getTaskFilters();
    void getNewCategories();
    void setShowOverdue(boolean state);
    void setShowCompleted(boolean state);
    void setEndByDate(String date);
    void addCategory(String name);
    void removeCategory(String name);
    void addPriority(String name);
    void removePriority(String name);
    void saveNewFilters();
    void removeNewFilters();
    void resetFilters();
}
