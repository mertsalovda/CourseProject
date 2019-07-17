package ru.mertsalovda.myfirstapplication;

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
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class RegistrationFragment extends Fragment {

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    private EditText etLogin;
    private EditText etName;
    private EditText etPassword;
    private EditText etPasswordAgain;
    private Button btnRegistration;
    private SharedPreferencesHelper sharedPreferencesHelper;

    public static RegistrationFragment newInstance() {
        return new RegistrationFragment();
    }

    private View.OnClickListener onRegistrationClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (isInputValid()) {
                User user = new User(
                        etLogin.getText().toString(),
                        etName.getText().toString(),
                        etPassword.getText().toString());
                // Запрос на регистрацияю
                Request request = new Request.Builder()
                        .url(BuildConfig.SERVER_URL.concat("/registration"))
                        .post(RequestBody.create(JSON, new Gson().toJson(user))) // Тело запроса
                        .build();
                // Добавляем клиент
                OkHttpClient client = new OkHttpClient();
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
                            if (response.isSuccessful()) {
                                showMessage(R.string.registration_success);
                                getFragmentManager().popBackStack();
                            } else {
                                //TODO детальная обработка ошибок
                                showMessage(R.string.registration_error);
                            }
                        });
                    }
                });

            }
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fr_registration, container, false);

        sharedPreferencesHelper = new SharedPreferencesHelper(getActivity());

        etLogin = view.findViewById(R.id.etLogin);
        etName = view.findViewById(R.id.etName);
        etPassword = view.findViewById(R.id.etPassword);
        etPasswordAgain = view.findViewById(R.id.tvPasswordAgain);
        btnRegistration = view.findViewById(R.id.btnRegistration);

        btnRegistration.setOnClickListener(onRegistrationClickListener);

        return view;
    }

    private boolean isInputValid() {
        String email = etLogin.getText().toString();
        if (isEmailValid(email) && isPasswordsValid() && isNameValid()) {
            return true;
        }

        return false;
    }

    private boolean isEmailValid(String email) {
        return !TextUtils.isEmpty(email) && Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean isPasswordsValid() {
        String password = etPassword.getText().toString();
        String passwordAgain = etPasswordAgain.getText().toString();

        return password.equals(passwordAgain)
                && !TextUtils.isEmpty(password)
                && !TextUtils.isEmpty(passwordAgain);
    }

    private boolean isNameValid() {
        String name = etName.getText().toString();
        return !name.isEmpty();
    }

    private void showMessage(@StringRes int string) {
        Toast.makeText(getActivity(), string, Toast.LENGTH_LONG).show();
    }

}
