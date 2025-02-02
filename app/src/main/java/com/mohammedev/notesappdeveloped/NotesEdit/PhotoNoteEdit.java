package com.mohammedev.notesappdeveloped.NotesEdit;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProviders;

import com.mohammedev.notesappdeveloped.R;
import com.mohammedev.notesappdeveloped.classes.PhotoNote;
import com.mohammedev.notesappdeveloped.utils.AppExecutor;
import com.mohammedev.notesappdeveloped.utils.Constants;
import com.mohammedev.notesappdeveloped.room.ViewModels.NoteViewModel;

import java.io.ByteArrayOutputStream;

public class PhotoNoteEdit extends AppCompatActivity {

    private static final int PICK_IMAGE = 10;
    private Uri mSelectedPhotoUri;

    private EditText photoNoteEditEditText;
    public ImageView photoNoteEditImageView;
    private Button changeBtn;
    private ConstraintLayout constraintLayout;
    private int Position;
    public String newText;
    private NoteViewModel mNoteViewModel;
    private AppExecutor appExecutor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_photo_details);

        photoNoteEditEditText = findViewById(R.id.photoNoteEditTextEdit);
        photoNoteEditImageView = findViewById(R.id.photoImageView);
        changeBtn = findViewById(R.id.changeBtn);
        constraintLayout = findViewById(R.id.ConstraintLayout);
        appExecutor = AppExecutor.getInstance();

        PhotoNote photoNote = (PhotoNote) getIntent().getSerializableExtra("photoNote");
        Bundle bundle = getIntent().getExtras();

        String photoNoteText = bundle.getString(Constants.EXTRA_NOTE_TEXT);

        photoNoteEditEditText.setText(photoNote.getNote());
        photoNoteEditImageView.setImageURI(photoNote.getImage());
        constraintLayout.setBackgroundColor(photoNote.getColor());
        Position = bundle.getInt(Constants.EXTRA_ID);
        mNoteViewModel = ViewModelProviders.of(this).get(NoteViewModel.class);

        photoNoteEditImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectPhoto();
            }
        });

        changeBtn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                newText = photoNoteEditEditText.getText().toString();

                appExecutor.getMainThread().execute(new Runnable() {
                    @Override
                    public void run() {
                        if (mSelectedPhotoUri != null)
                        {
                            PhotoNote photoNoteRoom = new PhotoNote(photoNote.getColor(), newText, mSelectedPhotoUri);
                            photoNoteRoom.setId(Position);
                            mNoteViewModel.updatePhotoNote(photoNoteRoom);
                        }else{
                            BitmapDrawable drawable = (BitmapDrawable) photoNoteEditImageView.getDrawable();
                            Bitmap bitmap = drawable.getBitmap();
                            PhotoNote photoNoteRoom = new PhotoNote(photoNote.getColor(), newText, getImageUri(PhotoNoteEdit.this,bitmap));
                            photoNoteRoom.setId(Position);
                            mNoteViewModel.updatePhotoNote(photoNoteRoom);
                        }
                        Intent returnIntent = new Intent();
                        setResult(Activity.RESULT_OK,returnIntent);
                        finish();
                    }
                });



            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {       super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE)
        {
            if (resultCode == RESULT_OK && data != null && data.getData() != null)
            {
                setSelectedPhoto(data.getData());
                getContentResolver().takePersistableUriPermission(data.getData() , Intent.FLAG_GRANT_READ_URI_PERMISSION);
            }
            else
            {
                Toast.makeText(this, R.string.failed_to_get_image, Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void selectPhoto()
    {
        firePickPhotoIntent();
    }

    private void setSelectedPhoto(Uri data)
    {
        photoNoteEditImageView.setImageURI(data);
        mSelectedPhotoUri = data;
    }

    private void firePickPhotoIntent()
    {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION);
        startActivityForResult(Intent.createChooser(intent , getString(R.string.choose_picture) ) , PICK_IMAGE);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
}