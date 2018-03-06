package com.example.ruireutov.organiser.task.filters;


import java.util.HashMap;

public interface ITaskListFilter {
    void generateQueryParams(HashMap<String, String> map);
    String getWhere();
    String[] getSelection();
}
