package ru.mertsalovda.myfirstapplication;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import ru.mertsalovda.myfirstapplication.model.User;

public class SharedPreferencesHelper {
    public static final String SHARED_PREF_NAME = "SHARED_PREF_NAME";
    public static final String USERS_KEY = "USERS_KEY";
//    public static final Type USERS_TYPE = new TypeToken<List<User>>() {
//    }.getType();

    public static final String SUCCESS_EMAILS_KEY = "SUCCESS_EMAILS_KEY";


    private SharedPreferences sharedPreferences;
//    private Gson gson = new Gson();

    public SharedPreferencesHelper(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

//    public List<User> getUsers() {
//        List<User> users = gson.fromJson(sharedPreferences.getString(USERS_KEY, ""), USERS_TYPE);
//        return users == null ? new ArrayList<User>() : users;
//    }
//
//    public boolean addUser(User user) {
//        List<User> users = getUsers();
//        for (User u : users) {
//            if (u.getData().getEmail().equalsIgnoreCase(user.getData().getEmail())) {
//                return false;
//            }
//        }
//        users.add(user);
//        sharedPreferences.edit().putString(USERS_KEY, gson.toJson(users, USERS_TYPE)).apply();
//        return true;
//    }
//
//    public boolean saveOrOverrideUser(User user) {
//        List<User> users = getUsers();
//        for (User u : users) {
//            if (u.getData().getEmail().equalsIgnoreCase(user.getData().getEmail())) {
//                users.remove(u);
//                break;
//            }
//        }
//        users.add(user);
//        sharedPreferences.edit().putString(USERS_KEY, gson.toJson(users, USERS_TYPE)).apply();
//        return true;
//    }
//
//    public List<String> getSuccessLogins() {
//        List<String> successLogins = new ArrayList<>();
//        List<User> allUsers = getUsers();
//        for (User user : allUsers) {
//            if (user.getData().hasSuccessLogin()) {
//                successLogins.add(user.getData().getEmail());
//            }
//        }
//        return successLogins;
//    }
//
//    public User login(String login, String password) {
//        List<User> users = getUsers();
//        for (User u : users) {
//            if (login.equalsIgnoreCase(u.getData().getEmail())
//                    && password.equals(u.getData().getPassword())) {
//                u.getData().setHasSuccessLogin(true);
//                sharedPreferences.edit().putString(USERS_KEY, gson.toJson(users, USERS_TYPE)).apply();
//                return u;
//            }
//        }
//        return null;
//    }

    public boolean insertSuccessEmail(String email){
        Set<String> emails = sharedPreferences.getStringSet(SUCCESS_EMAILS_KEY, new HashSet<>());

        emails.add(email);
        sharedPreferences.edit().putStringSet(SUCCESS_EMAILS_KEY, emails).apply();

        return true;
    }

    public List<String> getSuccessEmails() {
        Set<String> ret = sharedPreferences.getStringSet(SUCCESS_EMAILS_KEY, new HashSet<>());
        return (ret == null) ? new ArrayList<>() : new ArrayList<>(ret);
    }
}
