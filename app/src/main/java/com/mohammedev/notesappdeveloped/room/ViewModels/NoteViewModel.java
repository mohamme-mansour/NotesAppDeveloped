package com.mohammedev.notesappdeveloped.room.ViewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.mohammedev.notesappdeveloped.Background.NoteRepository;
import com.mohammedev.notesappdeveloped.classes.CheckNote;
import com.mohammedev.notesappdeveloped.classes.Note;
import com.mohammedev.notesappdeveloped.classes.PhotoNote;

import java.util.List;


public class NoteViewModel extends AndroidViewModel {

    private NoteRepository mNotesRepository;

    private LiveData<List<Note>> mAllNotes;
    private LiveData<List<PhotoNote>> mAllPhotoNotes;
    private LiveData<List<CheckNote>> mAllCheckNotes;
    private MediatorLiveData mAllNotesMerged;


    public NoteViewModel(@NonNull Application application) {
        super(application);
        mNotesRepository = new NoteRepository(application);
        mAllNotes = mNotesRepository.getAllNotes();
        mAllPhotoNotes = mNotesRepository.getAllPhotoNotes();
        mAllNotesMerged = mNotesRepository.getAllNotesMerged();
        mAllCheckNotes = mNotesRepository.getAllCheckNotes();
    }


    public void insertNote(Note note) {
        mNotesRepository.insertNote(note);
    }

    public void insertPhotoNote(PhotoNote photoNote) {
        mNotesRepository.insertPhotoNote(photoNote);
    }

    public void insertCheckNote(CheckNote checkNote) {
        mNotesRepository.insertCheckNote(checkNote);
    }


    public void deleteAll() {
        mNotesRepository.deleteAllNotes();
    }

    public void deleteNormalNote(Note note) {
        mNotesRepository.deleteNote(note);
    }

    public void deletePhotoNote(PhotoNote photoNote) {
        mNotesRepository.deletePhotoNote(photoNote);
    }

    public void deleteCheckNote(CheckNote checkNote) {
        mNotesRepository.deleteCheckNote(checkNote);
    }


    public void updateNote(Note note) {
        mNotesRepository.updateNote(note);
    }

    public void updateCheckNote(CheckNote checkNote) {
        mNotesRepository.updateCheckNote(checkNote);
    }

    public void updatePhotoNote(PhotoNote photoNote) {
        mNotesRepository.updatePhotoNote(photoNote);
    }


    public LiveData<List<Note>> getAllNotes() {
        return mAllNotes;
    }

    public LiveData<List<CheckNote>> getAllCheckNote() {
        return mAllCheckNotes;
    }

    public MediatorLiveData getAllNotesMerged() {
        return mAllNotesMerged;
    }

    public LiveData<List<PhotoNote>> getAllPhotoNotes() {
        return mAllPhotoNotes;
    }
}