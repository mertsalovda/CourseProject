package ru.mertsalovda.myfirstapplication.db;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

import ru.mertsalovda.myfirstapplication.model.Album;
import ru.mertsalovda.myfirstapplication.model.Song;

@Dao
public interface MusicDao {

    //albums
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAlbums(List<Album> albums);

    @Query("SELECT * from album")
    List<Album> getAlbums();

    //удалить альбом
    @Delete
    void deleteAlbum(Album album);

    //удалить альбом по id
    @Query("DELETE FROM album where id = :albumId")
    void deleteAlbumById(int albumId);

    @Query("SELECT * FROM album where id = :albumId")
    Album getAlbumById(int albumId);


    //song
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertSongs(List<Song> songs);

    @Query("SELECT * from song where album_id = :albumId")
    List<Song> getSongsByAlbumId(int albumId);


}
