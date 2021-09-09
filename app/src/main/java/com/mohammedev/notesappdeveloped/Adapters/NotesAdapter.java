package com.mohammedev.notesappdeveloped.Adapters;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.mohammedev.notesappdeveloped.Listener.ItemClickListener;
import com.mohammedev.notesappdeveloped.Listener.ItemLongClickListener;
import com.mohammedev.notesappdeveloped.R;
import com.mohammedev.notesappdeveloped.classes.CheckNote;
import com.mohammedev.notesappdeveloped.classes.Note;
import com.mohammedev.notesappdeveloped.classes.PhotoNote;
import com.mohammedev.notesappdeveloped.extra.Constants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<NotesAdapter.NotesViewHolder> implements Serializable
{

    public List<Note> notesArray = new ArrayList<>();
    ItemLongClickListener mItemLongClickListener;
    ItemClickListener mItemClickListener;
    private Context context;


    public NotesAdapter(Context context, ItemLongClickListener mItemLongClickListener, ItemClickListener mItemClickListener)
    {
        this.context = context;
        this.mItemLongClickListener = mItemLongClickListener;
        this.mItemClickListener = mItemClickListener;

    }

    @Override
    public int getItemViewType(int position)
    {
        Note note = notesArray.get(position);
        if (note instanceof PhotoNote)
            return Constants.PHOTO_NOTE;

        else if (note instanceof CheckNote)
            return Constants.CHECK_NOTE;

        else
            return Constants.NORMAL_NOTE;
    }

    @NonNull
    @Override
    public NotesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        NotesViewHolder viewHolder;
        View view;

        if (viewType == Constants.PHOTO_NOTE)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note_photo, parent, false);
            viewHolder = new PhotoNoteViewHolder(view, mItemLongClickListener, mItemClickListener);
        }
        else if (viewType == Constants.CHECK_NOTE)
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note_check, parent, false);
            viewHolder = new CheckNoteViewHolder(view, mItemLongClickListener, mItemClickListener);
        }
        else
        {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_note, parent, false);
            viewHolder = new NotesViewHolder(view, mItemLongClickListener, mItemClickListener);
        }

        return viewHolder;
    }


    @Override
    public void onBindViewHolder (@NonNull final NotesViewHolder holder, int position){
        final Note notesMain = notesArray.get(position);

        if (notesMain instanceof PhotoNote) {
            PhotoNote photoNote = (PhotoNote) notesMain;
            PhotoNoteViewHolder photoNoteViewHolder = (PhotoNoteViewHolder) holder;
            photoNoteViewHolder.photoNoteImageView.setImageURI(photoNote.getImage());
            holder.cardView.setBackgroundColor(notesMain.getColor());
        } else if (notesMain instanceof CheckNote) {
            final CheckNote checkNote = (CheckNote) notesMain;
            final CheckNoteViewHolder noteCheckViewHolder = (CheckNoteViewHolder) holder;

            noteCheckViewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final boolean isChecked = noteCheckViewHolder.checkBox.isChecked();
                    int colorChosen = notesMain.getColor();
                    checkNote.setCheckBox(isChecked);
                    if (isChecked) {
                        holder.cardView.setBackgroundColor(Color.parseColor("#ADFF2F"));
                    } else {
                        holder.cardView.setBackgroundColor(colorChosen);
                    }
                }
            });
            int colorChosen = notesMain.getColor();
            noteCheckViewHolder.checkBox.setChecked(checkNote.getCheckBox());
            if (checkNote.getCheckBox()) {
                holder.cardView.setBackgroundColor(Color.parseColor("#ADFF2F"));
            } else {
                holder.cardView.setBackgroundColor(colorChosen);
            }
        }else {
            holder.cardView.setBackgroundColor(notesMain.getColor());
        }
//        holder.noteText.setText(notesMain.getNote());
        holder.position = position;
    }


    @Override
    public int getItemCount()
    {
        if (notesArray == null){
            return 0;
        }else{
            return notesArray.size();
        }

    }

    public static class NotesViewHolder extends RecyclerView.ViewHolder implements Serializable
    {

        TextView noteText;
        int position;
        View cardView;

        public NotesViewHolder(@NonNull View itemView, final ItemLongClickListener mItemLongClickListener, final ItemClickListener mItemClickListener)
        {
            super(itemView);
            noteText = itemView.findViewById(R.id.Note);
            cardView = itemView.findViewById(R.id.cardView);

            itemView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View view)
                {
                    mItemLongClickListener.onLongClickItem(position);

                    return false;
                }
            });

            itemView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    mItemClickListener.onClickListener(position);
                }
            });

        }
    }

    public static class PhotoNoteViewHolder extends NotesViewHolder implements Serializable
    {
        ImageView photoNoteImageView;
        public PhotoNoteViewHolder(@NonNull View itemView, final ItemLongClickListener mItemLongClickListener, final ItemClickListener mItemClickListener)
        {
            super(itemView, mItemLongClickListener, mItemClickListener);
            photoNoteImageView = itemView.findViewById(R.id.imageView_photo_note);

        }
    }

    public static class CheckNoteViewHolder extends NotesViewHolder implements Serializable
    {
        CheckBox checkBox;
        public CheckNoteViewHolder(@NonNull View itemView, ItemLongClickListener mItemLongClickListener, final ItemClickListener mItemClickListener)
        {
            super(itemView, mItemLongClickListener, mItemClickListener);
            checkBox = itemView.findViewById(R.id.checkBox);
            Log.d("" , "");
        }
    }

    public Note getNoteAt(int pos){
        return notesArray.get(pos);
    }


    public void setNormalNotes(List<Note> notes){
        if (notesArray != null) {
            for (int i = 0; i < notesArray.size(); i++){
                Note note = notesArray.get(i);
                if (!(note instanceof PhotoNote) && !(note instanceof CheckNote)){
                    notesArray.remove(note);
                }
            }
            notesArray.addAll(notes);
            notifyDataSetChanged();

            for (int i = 0; i < notesArray.size(); i++){
                for (int j = i + 1; j < notesArray.size(); j++){
                    Note note1 = notesArray.get(j);
                    if (notesArray.get(i).getId() == notesArray.get(j).getId()){
                        notesArray.remove(note1);
                        notifyItemRemoved(j);
                    }
                }
            }

        }else{
            Toast.makeText(context, "Error Happened!", Toast.LENGTH_SHORT).show();
        }
    }



    public void setPhotoNotes(List<PhotoNote> photoNotes){
        if (notesArray != null){
            for (int i = 0; i < notesArray.size(); i++){
                Note note = notesArray.get(i);
                if (note instanceof PhotoNote){
                    notesArray.remove(note);
                }
            }
            notesArray.addAll(photoNotes);
            notifyDataSetChanged();

            for (int i = 0; i < notesArray.size(); i++){
                for (int j = i + 1; j < notesArray.size(); j++){
                    Note note1 = notesArray.get(j);
                    if (notesArray.get(i).getId() == notesArray.get(j).getId()){
                        notesArray.remove(note1);
                        notifyItemRemoved(j);
                    }
                }
            }
        }

    }

    public void setCheckNotes(List<CheckNote> checkNotes){
        if (notesArray != null){
            for (int i= 0; i < notesArray.size(); i++){
                Note note = notesArray.get(i);
                if (note instanceof CheckNote){
                    notesArray.remove(note);
                }
            }
            notesArray.addAll(checkNotes);
            notifyDataSetChanged();

            for (int i = 0; i < notesArray.size(); i++){
                for (int j = i + 1; j < notesArray.size(); j++){
                    Note note1 = notesArray.get(j);
                    if (notesArray.get(i).getId() == notesArray.get(j).getId()){
                        notesArray.remove(note1);
                        notifyItemRemoved(j);
                    }
                }
            }
        }
    }
}