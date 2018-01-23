package com.example.ruireutov.organiser.DatabaseWorkers;

/**
 * Created by ruireutov on 04-Dec-17.
 */

public class DatabaseDefines {
    //task table names
    public static final String TASK_LIST_ID = "_id";
    public static final String TASK_LIST_NAME = "Name";
    public static final String TASK_LIST_DETAILS = "Details";
    public static final String TASK_LIST_START = "Start";
    public static final String TASK_LIST_END = "End";
    public static final String TASK_LIST_STATUS = "Status";
    public static final String TASK_LIST_CATEGORY = "Category";
    public static final String TASK_LIST_CATEGORY_ICON = "CategoryIcon";
    public static final String TASK_LIST_CATEGORY_COLOR = "CategoryColor";
    public static final String TASK_LIST_PRIORITY = "Priority";
    public static final String TASK_LIST_PRIORITY_MARK = "PriorityMark";
    public static final String TASK_LIST_PRIORITY_COLOR = "PriorityColor";

    public static final int TASK_STATUS_IN_PROGRESS = 0;
    public static final int TASK_STATUS_CLOSED = 1;

    //Categories table names
    public static final String CATEGORIES_NAME = "Name";
    public static final String CATEGORIES_ICON = "CategoryIcon";
    public static final String CATEGORIES_COLOR = "CategoryColor";

    //Categories table names
    public static final String PRIORITIES_NAME = "Name";
    public static final String PRIORITIES_MARK = "PriorityMark";
    public static final String PRIORITIES_COLOR = "PriorityColor";
}
