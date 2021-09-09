package com.mohammedev.notesappdeveloped.room;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.mohammedev.notesappdeveloped.classes.CheckNote;
import com.mohammedev.notesappdeveloped.classes.Note;
import com.mohammedev.notesappdeveloped.classes.PhotoNote;
import com.mohammedev.notesappdeveloped.room.Daos.CheckNoteDao;
import com.mohammedev.notesappdeveloped.room.Daos.NoteDao;
import com.mohammedev.notesappdeveloped.room.Daos.PhotoNoteDao;

@Database(entities = {Note.class, PhotoNote.class, CheckNote.class}, version = 2)
@TypeConverters(NoteConverter.class)
public abstract class NotesRoomDb extends RoomDatabase {
    private static NotesRoomDb instance;
    private static final String DATABASE_NAME = "notes-database";
    public abstract NoteDao noteDao();
    public abstract PhotoNoteDao photoNoteDao();
    public abstract CheckNoteDao checkNoteDao();

    public static synchronized NotesRoomDb getInstance(Context context){
        if (instance == null){
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    NotesRoomDb.class , DATABASE_NAME)
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallBack)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallBack = new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
        }
    };
}

