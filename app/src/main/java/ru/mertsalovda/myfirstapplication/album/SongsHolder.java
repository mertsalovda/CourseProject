package ru.mertsalovda.myfirstapplication.album;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import ru.mertsalovda.myfirstapplication.R;
import ru.mertsalovda.myfirstapplication.model.Song;


public class SongsHolder extends RecyclerView.ViewHolder {

    private TextView title;
    private TextView duration;

    public SongsHolder(View itemView) {
        super(itemView);
        title = itemView.findViewById(R.id.tv_title);
        duration = itemView.findViewById(R.id.tv_duration);
    }

    public void bind(Song item) {
        title.setText(item.getName());
        duration.setText(item.getDuration());
    }
}
