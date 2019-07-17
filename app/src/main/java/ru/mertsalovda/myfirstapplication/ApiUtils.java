package ru.mertsalovda.myfirstapplication;

import android.support.annotation.Nullable;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.logging.HttpLoggingInterceptor;

public class ApiUtils {

    private static OkHttpClient okHttpClient;

    public static OkHttpClient getBasicAuthClietn(String email, String password, boolean newInstance){
        if (newInstance || okHttpClient == null){
            OkHttpClient.Builder builder = new OkHttpClient.Builder();

            builder.authenticator(new Authenticator() {
                @Nullable
                @Override
                public Request authenticate(Route route, Response response) throws IOException {
                    String credentials = Credentials.basic(email, password);
                    return response.request().newBuilder().header("Authorization", credentials).build();
                }
            });
            builder.addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY));
            okHttpClient = builder.build();
        }
        return okHttpClient;
    }
}
