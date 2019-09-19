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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.HttpException;
import ru.mertsalovda.myfirstapplication.ApiUtils;
import ru.mertsalovda.myfirstapplication.App;
import ru.mertsalovda.myfirstapplication.R;
import ru.mertsalovda.myfirstapplication.db.MusicDao;
import ru.mertsalovda.myfirstapplication.model.Album;
import ru.mertsalovda.myfirstapplication.model.Comment;

public class AlbumCommentsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String ALBUM_KEY = "ALBUM_KEY";

    private RecyclerView recyclerView;
    private SwipeRefreshLayout refresher;
    private View errorView;
    private View emptyView;
    private ImageButton btnSend;
    private EditText etCommetn;
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
        emptyView = view.findViewById(R.id.emptyView);

        btnSend = view.findViewById(R.id.btn_send);
        etCommetn = view.findViewById(R.id.et_message);
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

        ApiUtils.getApiService().getComments(mAlbum.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe(disposable -> refresher.setRefreshing(true))
                .doFinally(() -> refresher.setRefreshing(false))
                .subscribe(comments -> {
                    if (comments.isEmpty()) {
                        errorView.setVisibility(View.GONE);
                        emptyView.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        errorView.setVisibility(View.GONE);
                        emptyView.setVisibility(View.GONE);
                        recyclerView.setVisibility(View.VISIBLE);
                        if (comments.size() != recyclerView.getAdapter().getItemCount()) {
                            commentsAdapter.addData(comments, true);
                            if (recyclerView.getAdapter().getItemCount() == 0)
                                showMessage(R.string.update_comments_list);
                        } else {
                            showMessage(R.string.no_new_comments);
                        }
                    }
                }, throwable -> {
                    throwable.getCause().printStackTrace();
                    if (throwable instanceof HttpException) {
                        responseCodeProcessor(((HttpException) throwable).code());
                        errorView.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
                    } else {
                        errorView.setVisibility(View.VISIBLE);
                        recyclerView.setVisibility(View.GONE);
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

    private MusicDao getMusicDao() {
        return ((App) getActivity().getApplication()).getDatabase().getMusicDao();
    }
}
