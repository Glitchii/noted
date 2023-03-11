// https://developer.android.com/develop/ui/views/layout/recyclerview
// https://www.youtube.com/watch?v=Mc0XT58A1Z4
// https://www.youtube.com/watch?v=7GPUpvcU1FE

package com.example.noted;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.ArrayList;

public class FileCardRecyclerViewAdapter extends RecyclerView.Adapter<FileCardRecyclerViewAdapter.CustomViewHolder> {
    Context context;
    ArrayList<FileCardModel> fileCardModels;

    public FileCardRecyclerViewAdapter(Context context, ArrayList<FileCardModel> fileCardModels) {
        this.context = context;
        this.fileCardModels = fileCardModels;
    }

    @NonNull
    @Override
    public FileCardRecyclerViewAdapter.CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recycler_view_file_row, parent, false);
        return new CustomViewHolder(view);
    }

    /**
     * Removes file from ArrayList and RecyclerView
     *
     * @param position Position of file in ArrayList
     */
    public void removeItem(int position) {
        // Remove file from ArrayList and RecyclerView
        fileCardModels.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, fileCardModels.size());
    }

    @Override
    public void onBindViewHolder(@NonNull FileCardRecyclerViewAdapter.CustomViewHolder holder, int position) {
        holder.fileNameTextView.setText(fileCardModels.get(position).getFileName());
        holder.fileSizeTextView.setText(fileCardModels.get(position).getFileSize());

        // Long click listener to delete file
        holder.itemView.setOnLongClickListener(v -> {
            String filename = fileCardModels.get(position).getFileName();

            if (!Utils.deleteFile(context, filename)) {
                Toast.makeText(context, "Failed to delete '" + filename + "'", Toast.LENGTH_SHORT).show();
                return true;
            }

            // Remove file from ArrayList and RecyclerView
            removeItem(position);

            return true;
        });

        // Click listener to start FileEditorActivity
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, FileEditorActivity.class);
            FileCardModel model = fileCardModels.get(position);
            File file = new File(Utils.getAbsPath(context, model.getFileName()));

            intent.putExtra("filename", model.getFileName());

            if (!file.exists()) {
                Toast.makeText(context, "File no longer exists.", Toast.LENGTH_SHORT).show();
                removeItem(position);
                return;
            }

            context.startActivity(intent);
        });
    }

    @Override
    public int getItemCount() {
        return fileCardModels.size();
    }

    public static class CustomViewHolder extends RecyclerView.ViewHolder {
        public TextView fileNameTextView, fileSizeTextView;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            fileNameTextView = itemView.findViewById(R.id.fileName);
            fileSizeTextView = itemView.findViewById(R.id.fileSize);
        }
    }
}
