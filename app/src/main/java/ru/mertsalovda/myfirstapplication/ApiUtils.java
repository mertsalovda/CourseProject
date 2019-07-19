package ru.mertsalovda.myfirstapplication;

import android.support.annotation.Nullable;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Authenticator;
import okhttp3.Credentials;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.Route;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiUtils {

    private static OkHttpClient okHttpClient;
    private static Retrofit retrofit;
    private static Gson gson;
    private static AcademyApi api;
    private static String mEmail = "";
    private static String mPassword = "";
    private static boolean mNewInstance = false;


    public static OkHttpClient getBasicAuthClient(String email, String password, boolean newInstance) {
        if (newInstance || okHttpClient == null) {
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

    public static Retrofit getRetrofit() {
        if (gson == null) {
            gson = new Gson();
        }
        if (retrofit == null || mNewInstance) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BuildConfig.SERVER_URL)
                    //need for interceptor
                    .client(getBasicAuthClient(mEmail, mPassword, mNewInstance))
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
            mNewInstance = false;
        }
        return retrofit;
    }

    public static AcademyApi getApiService() {
        if (api == null || mNewInstance) {
            api = getRetrofit().create(AcademyApi.class);
        }
        return api;
    }

    public static void setEmailPassword(String email, String password, boolean newInstance) {
        mEmail = email;
        mPassword = password;
        mNewInstance = newInstance;
    }
}
