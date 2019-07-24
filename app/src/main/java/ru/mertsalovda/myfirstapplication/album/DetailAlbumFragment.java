package ru.mertsalovda.myfirstapplication.album;

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


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.mertsalovda.myfirstapplication.ApiUtils;
import ru.mertsalovda.myfirstapplication.R;
import ru.mertsalovda.myfirstapplication.model.Album;
import ru.mertsalovda.myfirstapplication.model.Albums;

public class DetailAlbumFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String ALBUM_KEY = "ALBUM_KEY";

    private RecyclerView recyclerView;
    private SwipeRefreshLayout refresher;
    private View errorView;
    private Albums.DataBean album;

    @NonNull
    private final SongsAdapter songsAdapter = new SongsAdapter();

    public static DetailAlbumFragment newInstance(Albums.DataBean album) {
        Bundle args = new Bundle();
        args.putSerializable(ALBUM_KEY, album);

        DetailAlbumFragment fragment = new DetailAlbumFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fr_recycler, container, false);
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

        album = (Albums.DataBean) getArguments().getSerializable(ALBUM_KEY);

        getActivity().setTitle(album.getName());

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(songsAdapter);

        onRefresh();
    }

    @Override
    public void onRefresh() {
        refresher.post(() -> {
            refresher.setRefreshing(true);
            getAlbum();
        });
    }

    private void getAlbum() {

        ApiUtils.getApiService().getAlbum(album.getId()).enqueue(new Callback<Album>() {
            @Override
            public void onResponse(Call<Album> call, Response<Album> response) {
                if (response.isSuccessful()) {
                    errorView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    songsAdapter.addData(response.body().getData().getSongs(), true);
                } else {
                    errorView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                    responseCodeProcessor(response.code());
                }
                refresher.setRefreshing(false);
            }

            @Override
            public void onFailure(Call<Album> call, Throwable t) {
                errorView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                refresher.setRefreshing(false);
            }
        });
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

}
