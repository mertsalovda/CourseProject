package ru.mertsalovda.myfirstapplication;

import java.io.Serializable;

public class User implements Serializable {
    private String login;
    private String password;
    private String photoUri;
    private boolean hasSuccessLogin;

    public User(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhotoUri() {
        return photoUri;
    }

    public void setPhotoUri(String photoUri) {
        this.photoUri = photoUri;
    }

    public boolean hasSuccessLogin() {
        return hasSuccessLogin;
    }

    public void setHasSuccessLogin(boolean hasSuccessLogin) {
        this.hasSuccessLogin = hasSuccessLogin;
    }
}
