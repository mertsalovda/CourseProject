package ru.mertsalovda.myfirstapplication.model.comment;

import com.google.gson.annotations.SerializedName;

public class CommentForSend {

    @SerializedName("text")
    private String text;
    @SerializedName("album_id")
    private int albumId;

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public int getAlbumId() {
        return albumId;
    }

    public void setAlbumId(int albumId) {
        this.albumId = albumId;
    }
}
