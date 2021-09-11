package com.mohammedev.notesappdeveloped.Adapters;

import android.content.ClipData;
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
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.mohammedev.notesappdeveloped.Listener.ItemClickListener;
import com.mohammedev.notesappdeveloped.Listener.ItemLongClickListener;
import com.mohammedev.notesappdeveloped.R;
import com.mohammedev.notesappdeveloped.classes.CheckNote;
import com.mohammedev.notesappdeveloped.classes.Note;
import com.mohammedev.notesappdeveloped.classes.PhotoNote;
import com.mohammedev.notesappdeveloped.databinding.ItemNoteBinding;
import com.mohammedev.notesappdeveloped.databinding.ItemNoteCheckBinding;
import com.mohammedev.notesappdeveloped.databinding.ItemNotePhotoBinding;
import com.mohammedev.notesappdeveloped.extra.Constants;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NotesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Serializable
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
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == Constants.PHOTO_NOTE)
        {
            ItemNotePhotoBinding itemNotePhotoBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()), R.layout.item_note_photo , parent, false);
            return new PhotoNoteViewHolder(itemNotePhotoBinding, mItemLongClickListener, mItemClickListener);
        }
        else if (viewType == Constants.CHECK_NOTE)
        {
            ItemNoteCheckBinding itemNoteCheckBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()), R.layout.item_note_check, parent, false);
            return new CheckNoteViewHolder(itemNoteCheckBinding, mItemLongClickListener, mItemClickListener);
        }
        else
        {
            ItemNoteBinding itemNoteBinding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()), R.layout.item_note, parent, false);
            return new NotesViewHolder(itemNoteBinding, mItemLongClickListener, mItemClickListener);
        }
    }


    @Override
    public void onBindViewHolder (@NonNull final RecyclerView.ViewHolder holder, int position){
        final Note notesMain = notesArray.get(position);

        if (notesMain instanceof PhotoNote){
            PhotoNote photoNote = (PhotoNote) notesMain;
            ((PhotoNoteViewHolder)holder).itemNotePhotoBinding.setPhotoNote(photoNote);
            Toast.makeText(context, "this is a photoNote", Toast.LENGTH_SHORT).show();
        }else if (notesMain instanceof CheckNote){
            CheckNote checkNote = (CheckNote) notesMain;
            ((CheckNoteViewHolder)holder).itemNoteCheckBinding.setCheckNote(checkNote);
            Toast.makeText(context, "this is a checkNote", Toast.LENGTH_SHORT).show();
        }else{
            ((NotesViewHolder)holder).itemNoteBinding.setNote(notesMain);
            Toast.makeText(context, "this is a photoNote", Toast.LENGTH_SHORT).show();
        }

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
        int position;
        ItemNoteBinding itemNoteBinding;

        public NotesViewHolder(@NonNull ItemNoteBinding itemView, final ItemLongClickListener mItemLongClickListener, final ItemClickListener mItemClickListener)
        {
            super(itemView.getRoot());
            itemNoteBinding = itemView;

            itemNoteBinding.cardView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View view)
                {
                    mItemLongClickListener.onLongClickItem(position);

                    return false;
                }
            });

            itemNoteBinding.cardView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    mItemClickListener.onClickListener(position);
                }
            });

        }
    }

    public static class PhotoNoteViewHolder extends RecyclerView.ViewHolder implements Serializable
    {
        ItemNotePhotoBinding itemNotePhotoBinding;
        int position;
        public PhotoNoteViewHolder(@NonNull ItemNotePhotoBinding itemView, final ItemLongClickListener mItemLongClickListener, final ItemClickListener mItemClickListener)
        {
            super(itemView.getRoot());
            itemNotePhotoBinding = itemView;

            itemNotePhotoBinding.cardView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View view)
                {
                    mItemLongClickListener.onLongClickItem(position);

                    return false;
                }
            });

            itemNotePhotoBinding.cardView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    mItemClickListener.onClickListener(position);
                }
            });
        }
    }

    public static class CheckNoteViewHolder extends RecyclerView.ViewHolder implements Serializable
    {
        ItemNoteCheckBinding itemNoteCheckBinding;
        int position;
        public CheckNoteViewHolder(@NonNull ItemNoteCheckBinding itemView, ItemLongClickListener mItemLongClickListener, final ItemClickListener mItemClickListener)
        {
            super(itemView.getRoot());
            itemNoteCheckBinding = itemView;

            itemNoteCheckBinding.cardView.setOnLongClickListener(new View.OnLongClickListener()
            {
                @Override
                public boolean onLongClick(View view)
                {
                    mItemLongClickListener.onLongClickItem(position);

                    return false;
                }
            });

            itemNoteCheckBinding.cardView.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View view)
                {
                    mItemClickListener.onClickListener(position);
                }
            });
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