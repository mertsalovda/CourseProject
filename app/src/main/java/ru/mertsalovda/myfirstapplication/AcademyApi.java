package ru.mertsalovda.myfirstapplication;

import io.reactivex.Completable;
import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import ru.mertsalovda.myfirstapplication.model.Album;
import ru.mertsalovda.myfirstapplication.model.Albums;
import ru.mertsalovda.myfirstapplication.model.Song;
import ru.mertsalovda.myfirstapplication.model.Songs;
import ru.mertsalovda.myfirstapplication.model.User;
import ru.mertsalovda.myfirstapplication.model.UserRegistration;

public interface AcademyApi {
    @POST("registration")
    Completable registration(@Body UserRegistration user);

    @GET("user")
    Single<User> login();

    @GET("albums")
    Single<Albums> getAlbums();

    @GET("albums/{id}")
    Single<Album> getAlbum(@Path("id") int id);

    @GET("songs")
    Call<Songs> getSongs();

    @GET("songs/{id}")
    Call<Song> getSong(@Path("id") int id);

}
