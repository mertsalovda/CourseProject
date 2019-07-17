package ru.mertsalovda.myfirstapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import ru.mertsalovda.myfirstapplication.model.User;

public class AuthFragment extends Fragment {

    private AutoCompleteTextView tvEmail;
    private EditText etPassword;
    private Button btnEnter;
    private Button btnRegister;

    public static AuthFragment newInstance() {
        Bundle args = new Bundle();

        AuthFragment fragment = new AuthFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private View.OnClickListener onEnterClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (isEmailValid() && isPasswordValid()) {

                // Запрос на регистрацияю
                Request request = new Request.Builder()
                        .url(BuildConfig.SERVER_URL.concat("user/"))
                        .build();
                // Добавляем клиент
                OkHttpClient client = ApiUtils.getBasicAuthClietn(
                        tvEmail.getText().toString(),
                        etPassword.getText().toString(),
                        true);
                // Асинхронный запрос
                client.newCall(request).enqueue(new Callback() {
                    // Обрабатываем запрос в UI-потоке
                    Handler handler = new Handler(getActivity().getMainLooper());

                    @Override
                    public void onFailure(Call call, IOException e) {
                        handler.post(() -> {
                            showMessage(R.string.request_error);
                        });
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        handler.post(() -> {
                            if (!response.isSuccessful()) {
                                //TODO детальная обработка ошибок
                                showMessage(R.string.login_error);
                            } else {
                                try {
                                    Gson gson = new Gson();
                                    JsonObject json = gson.fromJson(response.body().string(), JsonObject.class);
                                    User user = gson.fromJson(json.get("data"), User.class);

                                    Intent startProfileIntent =
                                            new Intent(getActivity(), ProfileActivity.class);
                                    startProfileIntent.putExtra(ProfileActivity.USER_KEY, user);
                                    startActivity(startProfileIntent);
                                    getActivity().finish();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                });

            } else {
                showMessage(R.string.input_error);
            }

        }
    };

    private View.OnClickListener onRegisterClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragmentContainer, RegistrationFragment.newInstance())
                    .addToBackStack(RegistrationFragment.class.getName())
                    .commit();
        }
    };

    private View.OnFocusChangeListener onLoginFocusChangeListener = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View view, boolean hasFocus) {
            if (hasFocus) {
                tvEmail.showDropDown();
            }
        }
    };

    private boolean isEmailValid() {
        return !TextUtils.isEmpty(tvEmail.getText())
                && Patterns.EMAIL_ADDRESS.matcher(tvEmail.getText()).matches();
    }

    private boolean isPasswordValid() {
        return !TextUtils.isEmpty(etPassword.getText());
    }

    private void showMessage(@StringRes int string) {
        Toast.makeText(getActivity(), string, Toast.LENGTH_LONG).show();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_auth, container, false);

        tvEmail = v.findViewById(R.id.etLogin);
        etPassword = v.findViewById(R.id.etPassword);
        btnEnter = v.findViewById(R.id.buttonEnter);
        btnRegister = v.findViewById(R.id.buttonRegister);

        btnEnter.setOnClickListener(onEnterClickListener);
        btnRegister.setOnClickListener(onRegisterClickListener);
        tvEmail.setOnFocusChangeListener(onLoginFocusChangeListener);

        return v;
    }
}
