package com.example.ruireutov.organiser.TaskDetails;

import android.content.Intent;
import java.util.Date;

public interface ITaskDetailsControl {
    void parseIntentData(Intent intent);
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
