package com.example.myapplication.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.myapplication.database.entiy.Meldungen;

import java.util.List;

@Dao
public interface MeldungenDao {

    @Insert
    void insert(Meldungen meldungen);

    @Update
    void update(Meldungen meldungen);

    @Delete
    void delete(Meldungen meldungen);

    @Query("SELECT * FROM meldungen")
    LiveData<List<Meldungen>> getAll();

    @Query("SELECT * FROM meldungen WHERE SystemName = :systemName")
    LiveData<List<Meldungen>> getAllbySystemName(String systemName);

}
