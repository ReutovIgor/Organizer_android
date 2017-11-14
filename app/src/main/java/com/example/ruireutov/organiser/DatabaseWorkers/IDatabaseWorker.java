package com.example.ruireutov.organiser.DatabaseWorkers;

import android.database.Cursor;

/**
 * Created by ruireutov on 14-Nov-17.
 */

public interface IDatabaseWorker {
    public Cursor get(String[] vals, String sorting);
    public void insert(String[] params);
    public void update(int id, String[] params);
    public void delete(int id);

}
