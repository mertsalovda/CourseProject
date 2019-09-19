package ru.mertsalovda.myfirstapplication.comments;

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
import ru.mertsalovda.myfirstapplication.model.Comment;
import ru.mertsalovda.myfirstapplication.model.Song;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsHolder> {

    @NonNull
    private final List<Comment> comments = new ArrayList<>();

    @Override
    public CommentsHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_comment, parent, false);
        return new CommentsHolder(view);
    }

    @Override
    public void onBindViewHolder(CommentsHolder holder, int position) {
        Comment comment = comments.get(position);
        holder.bind(comment);
    }

    @Override
    public int getItemCount() {
        return comments.size();
    }

    public void addData(List<Comment> comments, boolean isRefreshed) {
        if (isRefreshed) {
            this.comments.clear();
        }

        for (Comment comment : comments) {
            dateFormat(comment);
        }
        this.comments.addAll(comments);
        notifyDataSetChanged();
    }

    private void dateFormat(Comment comment){
        String oldDate = comment.getTimestamp();
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
            comment.setTimestamp(dt1.format(date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
