package ru.mertsalovda.myfirstapplication.model;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class User implements Serializable {

    @SerializedName("data")
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public User(DataBean data) {
        this.data = data;
    }

    public User(String email, String name, String password) {
        data.setEmail(email);
        data.setName(name);
        data.setPassword(password);
    }

    public static class DataBean implements Serializable {
        @SerializedName("id")
        private int id;
        @SerializedName("name")
        private String name;
        @SerializedName("email")
        private String email;
        @SerializedName("password")
        private String password;

        private boolean hasSuccessLogin;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

        public boolean hasSuccessLogin() {
            return hasSuccessLogin;
        }

        public void setHasSuccessLogin(boolean hasSuccessLogin) {
            this.hasSuccessLogin = hasSuccessLogin;
        }
    }
}