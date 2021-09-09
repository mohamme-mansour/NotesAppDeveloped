package com.mohammedev.notesappdeveloped.room.Daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.mohammedev.notesappdeveloped.classes.CheckNote;

import java.util.List;

@Dao
public interface CheckNoteDao {

    @Insert
    void insert(CheckNote checkNote);

    @Update
    void update(CheckNote checkNote);

    @Delete
    void delete(CheckNote checkNote);

    @Query("SELECT * FROM checknotestable")
    LiveData<List<CheckNote>> getAllCheckNotes();
}
