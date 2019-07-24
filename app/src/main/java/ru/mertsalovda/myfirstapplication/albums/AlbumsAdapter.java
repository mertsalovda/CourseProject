package ru.mertsalovda.myfirstapplication.albums;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import ru.mertsalovda.myfirstapplication.R;
import ru.mertsalovda.myfirstapplication.model.Albums;

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsHolder> {

    @NonNull
    private final List<Albums.DataBean> albums = new ArrayList<>();
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
        Albums.DataBean album = albums.get(position);
        holder.bind(album, onClickListener);
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    public void addData(List<Albums.DataBean> data, boolean isRefreshed) {
        if (isRefreshed) {
            albums.clear();
        }

        albums.addAll(data);
        notifyDataSetChanged();
    }

    public interface OnItemClickListener {
        void onItemClick(Albums.DataBean album);
    }
}