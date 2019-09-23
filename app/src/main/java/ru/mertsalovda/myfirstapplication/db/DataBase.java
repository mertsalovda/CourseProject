package ru.mertsalovda.myfirstapplication.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import ru.mertsalovda.myfirstapplication.model.Album;
import ru.mertsalovda.myfirstapplication.model.Song;
import ru.mertsalovda.myfirstapplication.model.comment.Comment;

@Database(entities = {Album.class, Song.class, Comment.class}, version = 1)
public abstract class DataBase extends RoomDatabase {
    public abstract MusicDao getMusicDao();
}
