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

    @Query("SELECT * FROM Results ORDER BY  trackName")
    LiveData<List<Results>> fetchAllResultsSortedByTrackName();

    @Query("SELECT * FROM Results ORDER BY  artistName")
    LiveData<List<Results>> fetchAllResultsSortedByArtistName();

    @Query("SELECT * FROM Results ORDER BY  collectionName")
    LiveData<List<Results>> fetchAllResultsSortedByCollectionName();

    @Query("SELECT * FROM Results ORDER BY  collectionPrice DESC")
    LiveData<List<Results>> fetchAllResultsSortedByCollectionPriceDescending();

    @Insert(onConflict = OnConflictStrategy.REPLACE)  // or OnConflictStrategy.IGNORE
    Long insertTask(Results iResults);

}
