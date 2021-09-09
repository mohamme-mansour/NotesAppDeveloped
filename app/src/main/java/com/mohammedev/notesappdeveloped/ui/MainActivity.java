package com.mohammedev.notesappdeveloped.ui;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.mohammedev.notesappdeveloped.Adapters.NotesAdapter;
import com.mohammedev.notesappdeveloped.NotesEdit.CheckNoteEdit;
import com.mohammedev.notesappdeveloped.NotesEdit.NormalNoteEdit;
import com.mohammedev.notesappdeveloped.NotesEdit.PhotoNoteEdit;
import com.mohammedev.notesappdeveloped.R;
import com.mohammedev.notesappdeveloped.classes.CheckNote;
import com.mohammedev.notesappdeveloped.classes.Note;
import com.mohammedev.notesappdeveloped.classes.PhotoNote;
import com.mohammedev.notesappdeveloped.extra.Constants;
import com.mohammedev.notesappdeveloped.extra.ViewSpaces;
import com.mohammedev.notesappdeveloped.room.ViewModels.NoteViewModel;

public class MainActivity extends AppCompatActivity {

    private NotesAdapter mNotesAdapter;
    private NoteViewModel mNoteViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView notesRecyclerView = findViewById(R.id.recycler_view_photos);

        findViewById(R.id.floating_button_add).setOnClickListener(view -> startActivity(new Intent(MainActivity.this , AddNewNoteActivity.class)));


        StaggeredGridLayoutManager staggeredGridLayoutManager = new StaggeredGridLayoutManager(2 ,1);


        mNotesAdapter = new NotesAdapter(this, this::removeNote, this::editNote);

        notesRecyclerView.setAdapter(mNotesAdapter);
        notesRecyclerView.setLayoutManager(staggeredGridLayoutManager);
        notesRecyclerView.addItemDecoration(new ViewSpaces(20));

        mNoteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        mNoteViewModel.getAllNotes().observe(this, notes -> mNotesAdapter.setNormalNotes(notes));

        mNoteViewModel.getAllPhotoNotes().observe(this, photoNotes -> mNotesAdapter.setPhotoNotes(photoNotes));

        mNoteViewModel.getAllCheckNote().observe(this, checkNotes -> mNotesAdapter.setCheckNotes(checkNotes));



        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                if (mNotesAdapter.getNoteAt(position) instanceof PhotoNote){
                    mNoteViewModel.deletePhotoNote((PhotoNote) mNotesAdapter.getNoteAt(position));
                }else if (mNotesAdapter.getNoteAt(position) instanceof CheckNote){
                    mNoteViewModel.deleteCheckNote((CheckNote) mNotesAdapter.getNoteAt(position));
                }else{
                    mNoteViewModel.deleteNormalNote(mNotesAdapter.getNoteAt(position));

                }
                mNotesAdapter.notifyItemRemoved(position);
            }
        }).attachToRecyclerView(notesRecyclerView);
    }

    private void removeNote(final int position) {
        Toast.makeText(this, position + " , " + mNotesAdapter.notesArray.size(), Toast.LENGTH_SHORT).show();
        AlertDialog alertDialog = new AlertDialog.Builder(this )
                .setMessage(R.string.delete_confirmation)
                .setPositiveButton(R.string.Confirm, (dialogInterface, i) -> {
                    if (mNotesAdapter.getNoteAt(position) instanceof PhotoNote){
                        mNoteViewModel.deletePhotoNote((PhotoNote) mNotesAdapter.getNoteAt(position));
                    }else if (mNotesAdapter.getNoteAt(position) instanceof CheckNote){
                        mNoteViewModel.deleteCheckNote((CheckNote) mNotesAdapter.getNoteAt(position));
                    }else{
                        mNoteViewModel.deleteNormalNote(mNotesAdapter.getNoteAt(position));
                    }
                    mNotesAdapter.notifyItemRemoved(position);

                })
                .setNegativeButton(R.string.cancel, (dialogInterface, i) -> dialogInterface.dismiss())
                .create();

        alertDialog.show();

    }

    private void editNote(int position) {
        Note noteEdit = mNotesAdapter.getNoteAt(position);

        if (noteEdit instanceof PhotoNote) {
            PhotoNote photoNote = (PhotoNote) noteEdit;
            Intent photoNoteIntent = new Intent(MainActivity.this, PhotoNoteEdit.class);
            photoNoteIntent.putExtra("photoNote", (Parcelable) photoNote);
            photoNoteIntent.putExtra(Constants.EXTRA_ID, photoNote.getId());
            startActivity(photoNoteIntent);

        } else if (noteEdit instanceof CheckNote) {
            CheckNote checkNote = (CheckNote) noteEdit;
            Intent intent = new Intent(MainActivity.this, CheckNoteEdit.class);
            intent.putExtra(Constants.EXTRA_ID, checkNote.getId());
            intent.putExtra(Constants.EXTRA_NOTE_TEXT, checkNote.getNote());
            intent.putExtra(Constants.COLOR, checkNote.getColor());
            intent.putExtra("CheckBox status", checkNote.getCheckBox());
            startActivity(intent);

        } else {
            Intent intent = new Intent(MainActivity.this, NormalNoteEdit.class);
            intent.putExtra(Constants.EXTRA_ID, noteEdit.getId());
            intent.putExtra(Constants.EXTRA_NOTE_TEXT, noteEdit.getNote());
            intent.putExtra(Constants.COLOR, noteEdit.getColor());
            startActivity(intent);
        }
    }
}