package com.mohammedev.notesappdeveloped.ui;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.mohammedev.notesappdeveloped.Adapters.NotesAdapter;
import com.mohammedev.notesappdeveloped.Listener.ItemClickListener;
import com.mohammedev.notesappdeveloped.Listener.ItemLongClickListener;
import com.mohammedev.notesappdeveloped.NotesEdit.CheckNoteEdit;
import com.mohammedev.notesappdeveloped.NotesEdit.NormalNoteEdit;
import com.mohammedev.notesappdeveloped.NotesEdit.PhotoNoteEdit;
import com.mohammedev.notesappdeveloped.R;
import com.mohammedev.notesappdeveloped.classes.CheckNote;
import com.mohammedev.notesappdeveloped.classes.Note;
import com.mohammedev.notesappdeveloped.classes.PhotoNote;
import com.mohammedev.notesappdeveloped.databinding.ActivityMainBinding;
import com.mohammedev.notesappdeveloped.utils.AppExecutor;
import com.mohammedev.notesappdeveloped.utils.Constants;
import com.mohammedev.notesappdeveloped.utils.ViewSpaces;
import com.mohammedev.notesappdeveloped.room.ViewModels.NoteViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    private NotesAdapter mNotesAdapter;
    private NoteViewModel mNoteViewModel;
    ActivityMainBinding activityMainBinding;
    AppExecutor mAppExecutor;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        activityMainBinding = DataBindingUtil.setContentView(this, R.layout.activity_main);

        mAppExecutor = AppExecutor.getInstance();

        findViewById(R.id.floating_button_add).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, AddNewNoteActivity.class));
            }
        });

        activityMainBinding.recyclerViewPhotos.setLayoutManager(new StaggeredGridLayoutManager(2, 1));
        activityMainBinding.recyclerViewPhotos.setHasFixedSize(true);
        activityMainBinding.recyclerViewPhotos.addItemDecoration(new ViewSpaces(20));

        mNotesAdapter = new NotesAdapter(this, new ItemLongClickListener() {
            @Override
            public void onLongClickItem(int position) {
                removeNote(position);
            }
        }, new ItemClickListener() {
            @Override
            public void onClickListener(int position) {
                editNote(position);
            }
        });

        mNoteViewModel = new ViewModelProvider(this).get(NoteViewModel.class);

        mNoteViewModel.getAllNotes().observe(this, new Observer<List<Note>>() {
            @Override
            public void onChanged(List<Note> notes) {
                mAppExecutor.getMainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        mNotesAdapter.setNormalNotes(notes);
                    }
                });
            }
        });

        mNoteViewModel.getAllPhotoNotes().observe(this, new Observer<List<PhotoNote>>() {
            @Override
            public void onChanged(List<PhotoNote> photoNotes) {
                mAppExecutor.getMainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        mNotesAdapter.setPhotoNotes(photoNotes);
                    }
                });
            }
        });

        mNoteViewModel.getAllCheckNote().observe(this, new Observer<List<CheckNote>>() {
            @Override
            public void onChanged(List<CheckNote> checkNotes) {
                mAppExecutor.getMainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        mNotesAdapter.setCheckNotes(checkNotes);
                    }
                });
            }
        });

        activityMainBinding.recyclerViewPhotos.setAdapter(mNotesAdapter);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                int position = viewHolder.getAdapterPosition();
                if (mNotesAdapter.getNoteAt(position) instanceof PhotoNote){
                    mAppExecutor.getMainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            mNoteViewModel.deletePhotoNote((PhotoNote) mNotesAdapter.getNoteAt(position));
                        }
                    });

                }else if (mNotesAdapter.getNoteAt(position) instanceof CheckNote){
                    mAppExecutor.getMainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            mNoteViewModel.deleteCheckNote((CheckNote) mNotesAdapter.getNoteAt(position));
                        }
                    });
                }else{
                    mAppExecutor.getMainThread().execute(new Runnable() {
                        @Override
                        public void run() {
                            mNoteViewModel.deleteNormalNote(mNotesAdapter.getNoteAt(position));
                        }
                    });
                }
                mNotesAdapter.notifyItemRemoved(position);
            }
        }).attachToRecyclerView(activityMainBinding.recyclerViewPhotos);
    }

    private void removeNote(int position) {
        Toast.makeText(this, position + " , " + mNotesAdapter.notesArray.size() + " , " + mNotesAdapter.getNoteAt(position).getId() , Toast.LENGTH_SHORT).show();
        AlertDialog alertDialog = new AlertDialog.Builder(this )
                .setMessage(R.string.delete_confirmation)
                .setPositiveButton(R.string.Confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        mAppExecutor.getMainThread().execute(new Runnable() {
                            @Override
                            public void run() {
                                if (mNotesAdapter.getNoteAt(position) instanceof PhotoNote) {
                                    mNoteViewModel.deletePhotoNote((PhotoNote) mNotesAdapter.getNoteAt(position));
                                } else if (mNotesAdapter.getNoteAt(position) instanceof CheckNote) {
                                    mNoteViewModel.deleteCheckNote((CheckNote) mNotesAdapter.getNoteAt(position));
                                } else {
                                    mNoteViewModel.deleteNormalNote(mNotesAdapter.getNoteAt(position));
                                }
                                mNotesAdapter.notifyItemRemoved(position);
                            }
                        });
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
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