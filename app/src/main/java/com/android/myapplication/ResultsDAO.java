package com.android.myapplication;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ResultsDAO {

    @Query("SELECT * FROM Results")
    LiveData<List<Results>> fetchAllResults();

    @Insert(onConflict = OnConflictStrategy.REPLACE)  // or OnConflictStrategy.IGNORE
    Long insertTask(Results iResults);



}
