package com.android.myapplication;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Results.class,CartItem.class}, version = 1, exportSchema = false)
public abstract class ResultsDataBase extends RoomDatabase {

    public abstract ResultsDAO resultsDAOAccess();

    public abstract CartDAO cartDAOAccess();
}
