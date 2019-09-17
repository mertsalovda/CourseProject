package ru.mertsalovda.myfirstapplication.comments;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.Collections;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;
import ru.mertsalovda.myfirstapplication.ApiUtils;
import ru.mertsalovda.myfirstapplication.App;
import ru.mertsalovda.myfirstapplication.R;
import ru.mertsalovda.myfirstapplication.db.MusicDao;
import ru.mertsalovda.myfirstapplication.model.Album;
import ru.mertsalovda.myfirstapplication.model.Song;

public class AlbumCommentsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String ALBUM_KEY = "ALBUM_KEY";

    private RecyclerView recyclerView;
    private SwipeRefreshLayout refresher;
    private View errorView;
    private Album mAlbum;

    @NonNull
    private final CommentsAdapter commentsAdapter = new CommentsAdapter();

    public static AlbumCommentsFragment newInstance(Album album) {
        Bundle args = new Bundle();
        args.putSerializable(ALBUM_KEY, album);

        AlbumCommentsFragment fragment = new AlbumCommentsFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fr_comments, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        recyclerView = view.findViewById(R.id.recycler);
        refresher = view.findViewById(R.id.refresher);
        refresher.setOnRefreshListener(this);
        errorView = view.findViewById(R.id.errorView);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mAlbum = (Album) getArguments().getSerializable(ALBUM_KEY);

        getActivity().setTitle(mAlbum.getName());

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(commentsAdapter);

        onRefresh();
    }

    @Override
    public void onRefresh() {
        refresher.post(() -> {
            getComments();
        });
    }

    @SuppressLint("CheckResult")
    private void getComments() {

//        ApiUtils.getApiService().getAlbum(mAlbum.getId())
//                .subscribeOn(Schedulers.io())
//                .doOnSuccess(album -> {
//                    Collections.sort(album.getSongs());
//                    for (Song song : album.getSongs()) {
//                        song.setAlbumId(album.getId());
//                    }
//                    mAlbum = album;
//                    getMusicDao().insertSongs(album.getSongs());
//                })
//                .onErrorReturn(throwable -> {
//                    if (ApiUtils.NETWORK_EXCEPTIONS.contains(throwable.getClass())) {
//                        Album album = getMusicDao().getAlbumById(mAlbum.getId());
//                        album.setSongs(getMusicDao().getSongsByAlbumId(album.getId()));
//                        return album;
//                    } else return null;
//                })
//                .observeOn(AndroidSchedulers.mainThread())
//                .doOnSubscribe(disposable -> refresher.setRefreshing(true))
//                .doFinally(() -> refresher.setRefreshing(false))
//                .subscribe(album -> {
//                    errorView.setVisibility(View.GONE);
//                    recyclerView.setVisibility(View.VISIBLE);
//                    commentsAdapter.addData(album.getSongs(), true);
//                }, throwable -> {
//                    throwable.getCause().printStackTrace();
//                    if (throwable instanceof HttpException) {
//                        responseCodeProcessor(((HttpException) throwable).code());
//                        errorView.setVisibility(View.VISIBLE);
//                        recyclerView.setVisibility(View.GONE);
//                    }
//                });
    }

    private void responseCodeProcessor(int code) {
        switch (code) {
            case 401:
                showMessage(R.string.dont_auth);
                break;
            case 500:
                showMessage(R.string.server_error);
                break;
        }
    }

    private void showMessage(@StringRes int string) {
        Toast.makeText(getActivity(), string, Toast.LENGTH_LONG).show();
    }

    private MusicDao getMusicDao() {
        return ((App) getActivity().getApplication()).getDatabase().getMusicDao();
    }
}
