package com.mohammedev.notesappdeveloped.NotesEdit;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;

import com.mohammedev.notesappdeveloped.Adapters.NotesAdapter;
import com.mohammedev.notesappdeveloped.R;
import com.mohammedev.notesappdeveloped.classes.Note;
import com.mohammedev.notesappdeveloped.ui.MainActivity;
import com.mohammedev.notesappdeveloped.utils.AppExecutor;
import com.mohammedev.notesappdeveloped.utils.Constants;
import com.mohammedev.notesappdeveloped.room.ViewModels.NoteViewModel;

public class NormalNoteEdit extends AppCompatActivity {
    private EditText normalNoteEditEditText;
    private Button changeBtn;
    private ConstraintLayout constraintLayout;
    private int position;
    private NoteViewModel mNoteViewModel;
    private AppExecutor appExecutor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_details);
        normalNoteEditEditText = findViewById(R.id.noteEditText);
        changeBtn = findViewById(R.id.changeBtn);
        constraintLayout = findViewById(R.id.ConstraintLayout);
        appExecutor = AppExecutor.getInstance();

        Bundle bundle = getIntent().getExtras();
        String bundleNoteText = bundle.getString(Constants.EXTRA_NOTE_TEXT);

        int bundleNoteColor = bundle.getInt(Constants.COLOR);
        normalNoteEditEditText.setText(bundleNoteText);

        position = bundle.getInt(Constants.EXTRA_ID);
        constraintLayout.setBackgroundColor(bundleNoteColor);

        mNoteViewModel = ViewModelProviders.of(NormalNoteEdit.this).get(NoteViewModel.class);

        changeBtn.setOnClickListener(view -> {
            appExecutor.getMainThread().execute(new Runnable() {
                @Override
                public void run() {
                    String newText = normalNoteEditEditText.getText().toString();
                    Note noteRoom = new Note(bundleNoteColor , newText);
                    noteRoom.setId(position);

                    mNoteViewModel.updateNote(noteRoom);
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();
                }
            });

        });
    }
}