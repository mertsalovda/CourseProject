package ru.mertsalovda.myfirstapplication.comments;

import android.annotation.SuppressLint;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ru.mertsalovda.myfirstapplication.R;
import ru.mertsalovda.myfirstapplication.model.Comment;
import ru.mertsalovda.myfirstapplication.model.Song;


public class CommentsHolder extends RecyclerView.ViewHolder {

    private TextView author;
    private TextView text;
    private TextView timestamp;

    public CommentsHolder(View itemView) {
        super(itemView);
        author = itemView.findViewById(R.id.tv_author);
        text = itemView.findViewById(R.id.tv_text);
        timestamp = itemView.findViewById(R.id.tv_date);
    }

    public void bind(Comment item) {
        author.setText(item.getAuthor());
        text.setText(item.getText());
        timestamp.setText(item.getTimestamp());
    }
}
