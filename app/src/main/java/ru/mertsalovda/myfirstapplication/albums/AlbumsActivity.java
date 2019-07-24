package ru.mertsalovda.myfirstapplication.albums;

import android.support.v4.app.Fragment;

import ru.mertsalovda.myfirstapplication.SingleFragmentActivity;


public class AlbumsActivity extends SingleFragmentActivity {

    @Override
    protected Fragment getFragment() {
        return AlbumsFragment.newInstance();
    }
}