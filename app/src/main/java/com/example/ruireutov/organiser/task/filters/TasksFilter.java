package com.example.ruireutov.organiser.task.filters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class TasksFilter extends ATaskListFilter {

    public TasksFilter(boolean overdue, boolean completed, String timeStart, String timeEnd, Set<String> categories, Set<String> priorities) {
        super(overdue, completed, timeStart, timeEnd, categories, priorities);
    }

    public void generateQueryParams(HashMap<String, String> map) {
        this.where.setLength(0);
        this.selection = new ArrayList<>();
        this.parseTimeEndFilters(map.get(TasksFilter.TASK_FILTER_TIME_END));
        this.parseCompletedFilters(map.get(TasksFilter.TASK_FILTER_STATUS));
        this.parsePrioritiesFilters(map.get(TasksFilter.TASK_FILTER_PRIORITY));
        this.parseCategoriesFilters(map.get(TasksFilter.TASK_FILTER_CATEGORY));
    }
}
