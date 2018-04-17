package com.example.ruireutov.organiser.task.filters;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class PrioritiesFilter extends ATaskListFilter{
    public PrioritiesFilter(boolean overdue, boolean completed, String timeStart, String timeEnd, Set<String> categories, Set<String> priorities) {
        super(overdue, completed, timeStart, timeEnd, categories, priorities);
    }
    @Override
    public void generateQueryParams(HashMap<String, String> map) {
        this.where.setLength(0);
        this.selection = new ArrayList<>();
        this.parseTimeEndFilters(map.get(TaskFilter.TASK_FILTER_TIME_END));
        this.parseCompletedFilters(map.get(TaskFilter.TASK_FILTER_STATUS));
        this.parseCategoriesFilters(map.get(TaskFilter.TASK_FILTER_CATEGORY));
    }
}
