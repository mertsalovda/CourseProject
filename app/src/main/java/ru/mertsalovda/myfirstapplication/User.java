package ru.mertsalovda.myfirstapplication;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {
    @SerializedName("email")
    private String email;
    @SerializedName("name")
    private String name;
    @SerializedName("password")
    private String password;

    private String photoUri;

    private boolean hasSuccessLogin;

    public User(String email, String name, String password) {
        this.email = email;
        this.name = name;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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
