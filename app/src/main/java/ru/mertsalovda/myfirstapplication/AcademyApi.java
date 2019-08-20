package ru.mertsalovda.myfirstapplication;

import com.google.gson.annotations.SerializedName;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import ru.mertsalovda.myfirstapplication.model.Album;
import ru.mertsalovda.myfirstapplication.model.Song;
import ru.mertsalovda.myfirstapplication.model.User;
import ru.mertsalovda.myfirstapplication.model.UserRegistration;

public interface AcademyApi {
    @POST("registration")
    Completable registration(@Body UserRegistration user);

    @GET("user")
    Single<User> login();

    @GET("albums")
    Single<List<Album>> getAlbums();

    @GET("albums/{id}")
    Single<Album> getAlbum(@Path("id") int id);

    @GET("songs")
    Single<List<Song>> getSongs();

    @GET("songs/{id}")
    Single<Song> getSong(@Path("id") int id);

}
