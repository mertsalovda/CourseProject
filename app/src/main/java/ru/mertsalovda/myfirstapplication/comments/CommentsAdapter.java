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

    public void addData(List<Comment> data, boolean isRefreshed) {
        if (isRefreshed) {
            comments.clear();
        }
        for(Comment comment:data){
            String oldDate = comment.getTimestamp();
            @SuppressLint("SimpleDateFormat") SimpleDateFormat dt = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            Date date;
            try {
                date = dt.parse(oldDate);
                @SuppressLint("SimpleDateFormat") SimpleDateFormat dt1 = new SimpleDateFormat("HH:mm:ss dd.MM.yyyy");
                comment.setTimestamp(dt1.format(date));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        comments.addAll(data);
        notifyDataSetChanged();
    }
}
