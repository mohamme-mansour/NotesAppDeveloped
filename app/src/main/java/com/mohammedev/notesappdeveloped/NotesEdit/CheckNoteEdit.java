package com.mohammedev.notesappdeveloped.NotesEdit;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;

import com.mohammedev.notesappdeveloped.R;
import com.mohammedev.notesappdeveloped.classes.CheckNote;
import com.mohammedev.notesappdeveloped.utils.AppExecutor;
import com.mohammedev.notesappdeveloped.utils.Constants;
import com.mohammedev.notesappdeveloped.room.ViewModels.NoteViewModel;

public class CheckNoteEdit extends AppCompatActivity {
    private EditText checkNoteEditEditText;
    private Button changeBtn;
    private CheckBox checkBox;
    private ConstraintLayout constraintLayout;
    private int position;
    private NoteViewModel mNoteViewModel;
    private AppExecutor appExecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_check_details);

        checkNoteEditEditText = findViewById(R.id.checkNoteEditText);
        changeBtn = findViewById(R.id.changeBtn);
        constraintLayout = findViewById(R.id.ConstraintLayout);
        checkBox = findViewById(R.id.checkNoteCheckBox);
        appExecutor = AppExecutor.getInstance();

        Bundle bundle = getIntent().getExtras();

        String noteText = bundle.getString(Constants.EXTRA_NOTE_TEXT);
        int noteColor = bundle.getInt(Constants.COLOR);
        position = bundle.getInt(Constants.EXTRA_ID);
        boolean checkBoxStatus = bundle.getBoolean("CheckBox status");

        checkBox.setChecked(checkBoxStatus);
        if (checkBox.isChecked()){
            checkBox.setBackgroundColor(Color.GREEN);
        }
        constraintLayout.setBackgroundColor(noteColor);
        checkNoteEditEditText.setText(noteText);
        mNoteViewModel = ViewModelProviders.of(this ).get(NoteViewModel.class);

        changeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                appExecutor.getMainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        String newText = checkNoteEditEditText.getText().toString();
                        CheckNote checkNoteRoom = new CheckNote(noteColor, newText, checkBox.isChecked());
                        checkNoteRoom.setId(position);
                        mNoteViewModel.updateCheckNote(checkNoteRoom);
                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_OK,returnIntent);
                        finish();
                    }
                });

            }
        });
    }
}