package ru.mertsalovda.myfirstapplication.albums;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import ru.mertsalovda.myfirstapplication.R;
import ru.mertsalovda.myfirstapplication.model.Albums;

public class AlbumsHolder extends RecyclerView.ViewHolder {

    private TextView title;
    private TextView releaseDate;

    public AlbumsHolder(View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.tv_title);
        releaseDate = itemView.findViewById(R.id.tv_release_date);
    }

    public void bind(Albums.DataBean item, AlbumsAdapter.OnItemClickListener onItemClickListener) {
        title.setText(item.getName());
        releaseDate.setText(item.getReleaseDate());
        if (onItemClickListener != null) {
            itemView.setOnClickListener(v -> onItemClickListener.onItemClick(item));
        }
    }
}
