package com.example.ruireutov.organiser.task.taskDetails;

import com.example.ruireutov.organiser.task.TaskDetailsData;

import java.util.Date;

public interface ITaskDetailsControl {
    void parseTaskData(TaskDetailsData data);
    void addTask();
    void updateTask();
    void closeTask();
    void deleteTask();
    void setName(String str);
    void setDueDate(Date date);
    void setDetails(String str);
    void setCategory(String str);
    void setPriority(String str);

    void onDestroy();
}
