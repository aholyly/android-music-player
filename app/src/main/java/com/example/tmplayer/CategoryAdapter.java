package com.example.tmplayer;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tmplayer.Models.MusicCategory;
import com.example.tmplayer.Tools.MusicPlayer;

import java.io.File;
import java.util.LinkedList;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder> {

    Context myContext;
    LinkedList<MusicCategory> musicCategories;

    File appFolder;

    public CategoryAdapter(Context myContext, LinkedList<MusicCategory> musicCategories, File appFolder) {
        this.myContext = myContext;
        this.musicCategories = musicCategories;
        this.appFolder = appFolder;
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(myContext);
        View view = layoutInflater.inflate(R.layout.empty, parent, false);

        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {
        holder.categoryTitle.setText(musicCategories.get(position).getDisplayName());

        if (musicCategories.get(position).isDownloaded()) {
            holder.imageView.setImageResource(R.drawable.ic_downloaded);
        }
        else
        {
            holder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    v.setOnClickListener(null);
                    ImageView imageView = (ImageView) v;
                    imageView.setImageResource(R.drawable.ic_download_disabled);
                    boolean status = musicCategories.get(position).download(myContext,appFolder, (ImageView) v);
                    musicCategories.get(position).setDownloaded(status);
                }
            });
        }

        CategoryListAdapter categoryListAdapter = new CategoryListAdapter(myContext, musicCategories.get(position), appFolder);
        holder.recyclerView.setAdapter(categoryListAdapter);
        holder.recyclerView.setLayoutManager(new LinearLayoutManager(myContext,LinearLayoutManager.HORIZONTAL, false));
    }

    @Override
    public int getItemCount() {
        return musicCategories.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        TextView categoryTitle;
        RecyclerView recyclerView;
        ImageView imageView;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            categoryTitle = itemView.findViewById(R.id.categoryNameTextView);
            recyclerView = itemView.findViewById(R.id.recyclerViewCategoryItems);
            imageView = itemView.findViewById(R.id.categoryDownlaodImageView);

        }
    }
}
