package com.mohammedev.notesappdeveloped.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.databinding.ViewDataBinding;
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
        ViewDataBinding binding;
        if (viewType == Constants.PHOTO_NOTE)
        {
            binding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()), R.layout.item_note_photo , parent, false);
            return new NotesViewHolder((ItemNotePhotoBinding) binding, mItemLongClickListener, mItemClickListener);
        }
        else if (viewType == Constants.CHECK_NOTE)
        {
            binding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()), R.layout.item_note_check, parent, false);
            return new NotesViewHolder((ItemNoteCheckBinding) binding, mItemLongClickListener, mItemClickListener, ((ItemNoteCheckBinding) binding).checkBox);
        }
        else
        {
            binding = DataBindingUtil.inflate(
                    LayoutInflater.from(parent.getContext()), R.layout.item_note, parent, false);
            return new NotesViewHolder((ItemNoteBinding) binding, mItemLongClickListener, mItemClickListener);
        }
    }

    @Override
    public void onBindViewHolder (@NonNull final NotesViewHolder holder, int position){
        final Note notesMain = notesArray.get(position);
        if (notesMain instanceof PhotoNote){
            PhotoNote photoNote = (PhotoNote) notesMain;
            holder.itemNotePhotoBinding.setPhotoNote(photoNote);
        }else if (notesMain instanceof CheckNote){
            CheckNote checkNote = (CheckNote) notesMain;
            holder.itemNoteCheckBinding.setCheckNote(checkNote);
        }else{
            holder.itemNoteBinding.setNote(notesMain);
        }
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

    public static class NotesViewHolder extends RecyclerView.ViewHolder implements Serializable{
        private ItemNoteBinding itemNoteBinding;
        private ItemNotePhotoBinding itemNotePhotoBinding;
        private ItemNoteCheckBinding itemNoteCheckBinding;
        int position;
        CheckBox checkBox;

        public NotesViewHolder(ItemNoteBinding binding, ItemLongClickListener mItemLongClickListener, final ItemClickListener mItemClickListener){
            super(binding.getRoot());
            itemNoteBinding = binding;

            itemNoteBinding.cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    mItemLongClickListener.onLongClickItem(position);
                    return false;
                }
            });

            itemNoteBinding.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onClickListener(position);
                }
            });
        }

        public NotesViewHolder(ItemNotePhotoBinding binding, ItemLongClickListener mItemLongClickListener, final ItemClickListener mItemClickListener){
            super(binding.getRoot());
            itemNotePhotoBinding = binding;
            itemNotePhotoBinding.cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    mItemLongClickListener.onLongClickItem(position);

                    return false;
                }
            });

            itemNotePhotoBinding.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mItemClickListener.onClickListener(position);
                }
            });
        }

        public NotesViewHolder(ItemNoteCheckBinding binding, ItemLongClickListener mItemLongClickListener, final ItemClickListener mItemClickListener, CheckBox checkBoxBind){
            super(binding.getRoot());
            itemNoteCheckBinding = binding;
            checkBox = checkBoxBind;
            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (checkBox.isChecked()){
                        itemNoteCheckBinding.cardView.setCardBackgroundColor(Constants.GREEN_COLOR);
                        itemNoteCheckBinding.getCheckNote().setCheckBox(true);
                    }else
                    {
                        itemNoteCheckBinding.cardView.setCardBackgroundColor(itemNoteCheckBinding.getCheckNote().getColor());
                        itemNoteCheckBinding.getCheckNote().setCheckBox(false);
                    }
                }
            });

            itemNoteCheckBinding.cardView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    mItemLongClickListener.onLongClickItem(position);

                    return false;
                }
            });

            itemNoteCheckBinding.cardView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
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