package com.mohammedev.notesappdeveloped.NotesEdit;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;

import com.mohammedev.notesappdeveloped.R;
import com.mohammedev.notesappdeveloped.classes.Note;
import com.mohammedev.notesappdeveloped.extra.Constants;
import com.mohammedev.notesappdeveloped.room.ViewModels.NoteViewModel;

public class NormalNoteEdit extends AppCompatActivity {
    private EditText normalNoteEditEditText;
    private Button changeBtn;
    private ConstraintLayout constraintLayout;
    private int position;
    private NoteViewModel mNoteViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);
        normalNoteEditEditText = findViewById(R.id.noteEditText);
        changeBtn = findViewById(R.id.changeBtn);
        constraintLayout = findViewById(R.id.ConstraintLayout);

        Bundle bundle = getIntent().getExtras();
        String bundleNoteText = bundle.getString(Constants.EXTRA_NOTE_TEXT);

        int bundleNoteColor = bundle.getInt(Constants.COLOR);
        normalNoteEditEditText.setText(bundleNoteText);

        position = bundle.getInt(Constants.EXTRA_ID);
        constraintLayout.setBackgroundColor(bundleNoteColor);

        mNoteViewModel = ViewModelProviders.of(NormalNoteEdit.this).get(NoteViewModel.class);

        changeBtn.setOnClickListener(view -> {
            String newText = normalNoteEditEditText.getText().toString();
            Note noteRoom = new Note(bundleNoteColor , newText);
            noteRoom.setId(position);
            mNoteViewModel.updateNote(noteRoom);
            finish();
        });
    }
}