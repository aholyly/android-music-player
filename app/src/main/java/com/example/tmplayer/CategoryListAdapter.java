package com.example.tmplayer;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tmplayer.Models.MusicArtist;
import com.example.tmplayer.Models.MusicCategory;
import com.example.tmplayer.Tools.MusicPlayer;

import java.io.File;
import java.util.LinkedList;

public class CategoryListAdapter extends RecyclerView.Adapter<CategoryListAdapter.CategoryViewHolder> {

    Context myContext;
    MusicCategory musicCategory;

    LinkedList<MusicArtist> musicArtistLinkedList;

    File appFolder;

    int defaultArtistImage = R.drawable.cover_image;


    public CategoryListAdapter(Context myContext, MusicCategory musicCategory, File appFolder) {
        this.myContext = myContext;
        this.musicCategory = musicCategory;
        this.appFolder = appFolder;

        ArtistInfo artistInfo = new ArtistInfo(myContext, appFolder, musicCategory);
        artistInfo.check();
        musicArtistLinkedList = artistInfo.getArtists();
    }

    @NonNull
    @Override
    public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(myContext);
        View view = layoutInflater.inflate(R.layout.item_album_cover, parent, false);

        return new CategoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoryViewHolder holder, int position) {

        holder.albumTitle.setText(musicArtistLinkedList.get(position).getName());
        holder.cover.setImageResource(defaultArtistImage);
        holder.cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent songListActivityIntent = new Intent(myContext,SongListActivity.class);
                songListActivityIntent.putExtra("title",musicArtistLinkedList.get(position).getName());
                songListActivityIntent.putExtra("category", musicCategory.getDisplayName());
                songListActivityIntent.putExtra("artistName", musicArtistLinkedList.get(position).getName());
                songListActivityIntent.putExtra("titleId", musicCategory.getId());
                songListActivityIntent.putExtra("appFolder", appFolder.toString());
                songListActivityIntent.putExtra("albumId", musicArtistLinkedList.get(position).getId());
                songListActivityIntent.putExtra("songs", musicArtistLinkedList.get(position).getSongs().toString());
                Log.d("DOWNLOADED ADAPTER", "onClick: " + String.valueOf(musicCategory.isDownloaded()));
                songListActivityIntent.putExtra("downloaded", String.valueOf(musicCategory.isDownloaded()));
                myContext.startActivity(songListActivityIntent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return musicArtistLinkedList.size();
    }

    public class CategoryViewHolder extends RecyclerView.ViewHolder {

        TextView albumTitle;
        ImageView cover;

        public CategoryViewHolder(@NonNull View itemView) {
            super(itemView);

            albumTitle = itemView.findViewById(R.id.textViewAlbumName);
            cover = itemView.findViewById(R.id.imageViewAlbumAvatar);
        }
    }


}
