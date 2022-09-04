package com.mohammedev.notesappdeveloped.room.Daos;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.TypeConverters;
import androidx.room.Update;

import com.mohammedev.notesappdeveloped.classes.CheckNote;
import com.mohammedev.notesappdeveloped.classes.Note;
import com.mohammedev.notesappdeveloped.classes.PhotoNote;
import com.mohammedev.notesappdeveloped.room.NoteConverter;

import java.util.List;

@Dao
@TypeConverters(NoteConverter.class)
public interface NoteDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Note note);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void update(Note note);

    @Delete
    void delete(Note note);

    @Query("SELECT * from notesTable LIMIT 1")
    Note[] getAnyNote();

    @Query("SELECT * FROM photoNotesTable")
    LiveData<List<PhotoNote>> getAllPhotoNotes();

    @Query("SELECT * FROM checknotestable")
    LiveData<List<CheckNote>> getAllCheckNotes();

    @Query("SELECT * FROM notesTable")
    LiveData<List<Note>> getAllNotes();


    @Query("DELETE FROM notesTable")
    void deleteAllNotes();

}
