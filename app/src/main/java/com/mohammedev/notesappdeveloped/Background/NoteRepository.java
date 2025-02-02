package com.mohammedev.notesappdeveloped.Background;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;

import com.mohammedev.notesappdeveloped.classes.CheckNote;
import com.mohammedev.notesappdeveloped.classes.Note;
import com.mohammedev.notesappdeveloped.classes.PhotoNote;
import com.mohammedev.notesappdeveloped.room.Daos.CheckNoteDao;
import com.mohammedev.notesappdeveloped.room.Daos.NoteDao;
import com.mohammedev.notesappdeveloped.room.Daos.PhotoNoteDao;
import com.mohammedev.notesappdeveloped.room.NotesRoomDb;
import com.mohammedev.notesappdeveloped.utils.AppExecutor;

import java.util.List;

public class NoteRepository {
    private NoteDao mNoteDao;
    private PhotoNoteDao mPhotoNoteDao;
    private CheckNoteDao mCheckNoteDao;
    private LiveData<List<Note>> getAllNotes;
    private LiveData<List<PhotoNote>> getAllPhotoNotes;
    private LiveData<List<CheckNote>> getAllCheckNotes;
    private NotesRoomDb db;

    public NoteRepository(Application app){
        db = NotesRoomDb.getInstance(app);
        mNoteDao = db.noteDao();
        mPhotoNoteDao = db.photoNoteDao();
        mCheckNoteDao = db.checkNoteDao();
        getAllNotes = mNoteDao.getAllNotes();
        getAllPhotoNotes = mNoteDao.getAllPhotoNotes();
        getAllCheckNotes = mNoteDao.getAllCheckNotes();
    }


    public LiveData<List<Note>> getAllNotes(){
        return getAllNotes;
    }

    public LiveData<List<CheckNote>> getAllCheckNotes(){
        return getAllCheckNotes;
    }

    public LiveData<List<PhotoNote>> getAllPhotoNotes(){
        if (getAllPhotoNotes == null){
            getAllPhotoNotes = db.photoNoteDao().getAllPhotoNotes();
        }
        return getAllPhotoNotes;
    }

    public MediatorLiveData<Note> getAllNotesMerged(){
        MediatorLiveData liveDataMerger = new MediatorLiveData<>();

        liveDataMerger.addSource(getAllNotes, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                liveDataMerger.postValue(notes);
            }
        });

        liveDataMerger.addSource(getAllPhotoNotes, new Observer<List<PhotoNote>>() {
            @Override
            public void onChanged(List<PhotoNote> photoNotes) {
                liveDataMerger.postValue(photoNotes);

            }
        });

        liveDataMerger.addSource(getAllCheckNotes, new Observer() {
            @Override
            public void onChanged(Object o) {
                liveDataMerger.postValue(o);
            }
        });

        return liveDataMerger;
    }

    //insert
    public void insertNote(Note note) {
        new InsertAsyncTaskNote(mNoteDao).execute(note);}

    public void deleteNote(Note note) {
        new DeleteAsyncTaskNote(mNoteDao).execute(note);}

    public void updateNote(Note note){
        new UpdateAsyncTaskNote(mNoteDao).execute(note);}


    public void insertPhotoNote(PhotoNote photoNote) {
        new InsertAsyncTaskPhotoNote(mPhotoNoteDao).execute(photoNote);
    }

    public void deletePhotoNote(PhotoNote photoNote){
        new DeleteAsyncTaskPhotoNote(mPhotoNoteDao).execute(photoNote);
    }

    public void updatePhotoNote(PhotoNote photoNote){
        new UpdateAsyncTaskPhotoNote(mPhotoNoteDao).execute(photoNote);
    }

    public void insertCheckNote(CheckNote checkNote){
        new InsertAsyncTaskCheckNote(mCheckNoteDao).execute(checkNote);
    }

    public void deleteCheckNote(CheckNote checkNote){
        new DeleteAsyncTaskCheckNote(mCheckNoteDao).execute(checkNote);
    }

    public void updateCheckNote(CheckNote checkNote) {
        new UpdateAsyncTaskCheckNote(mCheckNoteDao).execute(checkNote);
    }

    public void deleteAllNotes(){
        new DeleteAllAsyncTaskNote(mNoteDao).execute();
        new DeleteAllAsyncTaskPhotoNote(mPhotoNoteDao).execute();
    }



    private static class InsertAsyncTaskNote extends AsyncTask<Note, Void, Void> {

        private NoteDao mNotesDao;

        public InsertAsyncTaskNote(NoteDao notesDao){
            mNotesDao = notesDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            mNotesDao.insert(notes[0]);
            Thread.currentThread().setName("Insert Normal Note Thread");

            return null;
        }
    }

    private static class DeleteAsyncTaskNote extends AsyncTask<Note, Void, Void> {

        private NoteDao noteDao;

        public DeleteAsyncTaskNote(NoteDao mNotesDao) {
            this.noteDao = mNotesDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.delete(notes[0]);
            Thread.currentThread().setName("Delete Normal Note Thread");

            return null;
        }
    }

    private static class DeleteAllAsyncTaskNote extends AsyncTask<Note, Void, Void> {
        NoteDao mNoteDao;

        public DeleteAllAsyncTaskNote(NoteDao mNoteDao) {
            this.mNoteDao = mNoteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            mNoteDao.deleteAllNotes();
            return null;
        }
    }

    private static class DeleteAllAsyncTaskPhotoNote extends AsyncTask<PhotoNote, Void, Void> {
        PhotoNoteDao mPhotoNoteDao;

        public DeleteAllAsyncTaskPhotoNote(PhotoNoteDao photoNoteDao) {
            this.mPhotoNoteDao = photoNoteDao;
        }

        @Override
        protected Void doInBackground(PhotoNote... photoNotes) {
            mPhotoNoteDao.deleteAllNotes();
            return null;
        }
    }

    private static class UpdateAsyncTaskNote extends AsyncTask<Note, Void, Void> {

        private NoteDao noteDao;

        public UpdateAsyncTaskNote(NoteDao mNoteDao) {
            this.noteDao = mNoteDao;
        }

        @Override
        protected Void doInBackground(Note... notes) {
            noteDao.update(notes[0]);
            Thread.currentThread().setName("Update Normal Note Thread");

            return null;
        }
    }

    private static class InsertAsyncTaskPhotoNote extends AsyncTask<PhotoNote, Void, Void> {

        private PhotoNoteDao mPhotoNotesDao;

        public InsertAsyncTaskPhotoNote(PhotoNoteDao photoNoteDao){
            mPhotoNotesDao = photoNoteDao;
        }

        @Override
        protected Void doInBackground(PhotoNote... photoNotes) {
            mPhotoNotesDao.insert(photoNotes[0]);
            Thread.currentThread().setName("Insert Photo Note Thread");

            return null;
        }
    }


    private static class DeleteAsyncTaskPhotoNote extends AsyncTask<PhotoNote, Void, Void> {

        private PhotoNoteDao mPhotoNoteDao;

        public DeleteAsyncTaskPhotoNote(PhotoNoteDao photoNoteDao) {
            this.mPhotoNoteDao = photoNoteDao;
        }

        @Override
        protected Void doInBackground(PhotoNote... photoNotes) {
            mPhotoNoteDao.delete(photoNotes[0]);
            Thread.currentThread().setName("Delete Photo Note Thread");

            return null;
        }
    }

    private static class UpdateAsyncTaskPhotoNote extends AsyncTask<PhotoNote, Void, Void> {

        private PhotoNoteDao mPhotoNoteDao;

        public UpdateAsyncTaskPhotoNote(PhotoNoteDao photoNoteDao) {
            this.mPhotoNoteDao = photoNoteDao;
        }

        @Override
        protected Void doInBackground(PhotoNote... photoNotes) {
            mPhotoNoteDao.update(photoNotes[0]);
            Thread.currentThread().setName("Update Photo Note Thread");

            return null;
        }
    }

    private static class InsertAsyncTaskCheckNote extends AsyncTask<CheckNote, Void, Void> {
        private CheckNoteDao mCheckNoteDao;

        public InsertAsyncTaskCheckNote(CheckNoteDao mCheckNoteDao) {
            this.mCheckNoteDao = mCheckNoteDao;
        }

        @Override
        protected Void doInBackground(CheckNote... checkNotes) {
            mCheckNoteDao.insert(checkNotes[0]);
            Thread.currentThread().setName("Insert Check Note Thread");

            return null;
        }
    }

    private static class DeleteAsyncTaskCheckNote extends AsyncTask<CheckNote, Void, Void> {

        private CheckNoteDao mCheckNoteDao;

        public DeleteAsyncTaskCheckNote(CheckNoteDao mCheckNoteDao) {
            this.mCheckNoteDao = mCheckNoteDao;
        }

        @Override
        protected Void doInBackground(CheckNote... checkNotes) {
            mCheckNoteDao.delete(checkNotes[0]);
            Thread.currentThread().setName("Delete Check Note Thread");

            return null;
        }
    }

    private static class UpdateAsyncTaskCheckNote extends AsyncTask<CheckNote, Void, Void> {

        private CheckNoteDao mCheckNoteDao;

        public UpdateAsyncTaskCheckNote(CheckNoteDao mCheckNoteDao) {
            this.mCheckNoteDao = mCheckNoteDao;
        }

        @Override
        protected Void doInBackground(CheckNote... checkNotes) {
            mCheckNoteDao.update(checkNotes[0]);
            Thread.currentThread().setName("Update Check Note Thread");

            return null;
        }
    }

}
