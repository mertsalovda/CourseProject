package ru.mertsalovda.myfirstapplication.album;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import ru.mertsalovda.myfirstapplication.R;
import ru.mertsalovda.myfirstapplication.model.Song;

public class SongsAdapter extends RecyclerView.Adapter<SongsHolder> {

    @NonNull
    private final List<Song> songs = new ArrayList<>();

    @Override
    public SongsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_song, parent, false);
        return new SongsHolder(view);
    }

    @Override
    public void onBindViewHolder(SongsHolder holder, int position) {
        Song song = songs.get(position);
        holder.bind(song);
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    public void addData(List<Song> data, boolean isRefreshed) {
        if (isRefreshed) {
            songs.clear();
        }
        songs.addAll(data);
        notifyDataSetChanged();
    }
}
