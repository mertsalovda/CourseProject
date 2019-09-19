package ru.mertsalovda.myfirstapplication.albums;

import android.annotation.SuppressLint;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ru.mertsalovda.myfirstapplication.R;
import ru.mertsalovda.myfirstapplication.model.Album;

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsHolder> {

    @NonNull
    private final List<Album> albums = new ArrayList<>();
    private final OnItemClickListener onClickListener;

    public AlbumsAdapter(OnItemClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    @Override
    public AlbumsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_album, parent, false);
        return new AlbumsHolder(view);
    }

    @Override
    public void onBindViewHolder(AlbumsHolder holder, int position) {
        Album album = albums.get(position);
        holder.bind(album, onClickListener);
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    public void addData(List<Album> albums, boolean isRefreshed) {
        if (isRefreshed) {
            this.albums.clear();
        }

        for (Album Album : albums) {
            dateFormat(Album);
        }

        this.albums.addAll(albums);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(Album album);
    }

    private void dateFormat(Album album){
        String oldDate = album.getReleaseDate();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        Date date;
        Date today = new Date();
        SimpleDateFormat dt1;
        try {
            date = dt.parse(oldDate);
            if (today.after(date)) {
                dt1 = new SimpleDateFormat("dd.MM.yyyy");
            } else {
                dt1 = new SimpleDateFormat("HH:mm:ss");
            }
            album.setReleaseDate(dt1.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}