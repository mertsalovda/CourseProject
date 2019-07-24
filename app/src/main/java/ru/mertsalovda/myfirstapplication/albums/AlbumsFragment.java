package ru.mertsalovda.myfirstapplication.albums;

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
import ru.mertsalovda.myfirstapplication.album.DetailAlbumFragment;
import ru.mertsalovda.myfirstapplication.model.Albums;

public class AlbumsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private RecyclerView recyclerView;
    private SwipeRefreshLayout refresher;
    private View errorView;

    @NonNull
    private final AlbumsAdapter albumAdapter = new AlbumsAdapter(album -> {
        getFragmentManager().beginTransaction()
                .replace(R.id.fragmentContainer, DetailAlbumFragment.newInstance(album))
                .addToBackStack(DetailAlbumFragment.class.getSimpleName())
                .commit();
    });

    public static AlbumsFragment newInstance() {
        return new AlbumsFragment();
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
        getActivity().setTitle(R.string.albums);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(albumAdapter);

        onRefresh();
    }

    @Override
    public void onRefresh() {
        refresher.post(() -> {
            refresher.setRefreshing(true);
            getAlbums();
        });
    }

    private void getAlbums() {

        ApiUtils.getApiService().getAlbums().enqueue(new Callback<Albums>() {
            @Override
            public void onResponse(Call<Albums> call, Response<Albums> response) {
                if (response.isSuccessful()) {
                    errorView.setVisibility(View.GONE);
                    recyclerView.setVisibility(View.VISIBLE);
                    albumAdapter.addData(response.body().getData(), true);
                } else {
                    errorView.setVisibility(View.VISIBLE);
                    recyclerView.setVisibility(View.GONE);
                }
                refresher.setRefreshing(false);
                errorRequestProcessor(response.code());
            }

            @Override
            public void onFailure(Call<Albums> call, Throwable t) {
                errorView.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.GONE);
                refresher.setRefreshing(false);
            }
        });
    }

    private void errorRequestProcessor(int code) {
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

