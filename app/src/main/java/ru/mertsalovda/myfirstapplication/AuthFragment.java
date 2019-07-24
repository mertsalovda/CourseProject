package ru.mertsalovda.myfirstapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.mertsalovda.myfirstapplication.albums.AlbumsActivity;
import ru.mertsalovda.myfirstapplication.model.User;

public class AuthFragment extends Fragment {

    private AutoCompleteTextView tvEmail;
    private EditText etPassword;
    private Button btnEnter;
    private Button btnRegister;
    private SharedPreferencesHelper sharedPreferencesHelper;
    private ArrayAdapter<String> emailedUsersAdapter;

    public static AuthFragment newInstance() {
        Bundle args = new Bundle();

        AuthFragment fragment = new AuthFragment();
        fragment.setArguments(args);
        return fragment;
    }

    private View.OnClickListener onEnterClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            if (isEmailPasswordValid()) {
                String email = tvEmail.getText().toString();
                String password = etPassword.getText().toString();
                // Создаю новый instance ApiUtils при авторизации нового пользователя
                ApiUtils.setEmailPassword(email, password, true);
                ApiUtils.getApiService().login().enqueue(new Callback<User>() {
                    Handler handler = new Handler(getActivity().getMainLooper());

                    @Override
                    public void onResponse(Call<User> call, Response<User> response) {
                        handler.post(() -> {
                            Log.d("TAG", "code " + response.code());
                            if (!response.isSuccessful()) {
                                //TODO детальная обработка ошибок
                                responseCodeProcessor(response.code());
//                                showMessage(R.string.login_error);
                            } else {
                                User user = new User(response.body().getData());

                                sharedPreferencesHelper
                                        .insertSuccessEmail(tvEmail.getText().toString());

//                                Intent startProfileIntent =
//                                        new Intent(getActivity(), ProfileActivity.class);
//                                startProfileIntent.putExtra(ProfileActivity.USER_KEY, user);
//                                startActivity(startProfileIntent);
                                startActivity(new Intent(getActivity(), AlbumsActivity.class));
                                getActivity().finish();
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<User> call, Throwable t) {
                        handler.post(() -> showMessage(R.string.request_error));
                    }
                });
            } else {
                showMessage(R.string.input_error);
            }

        }
    };

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
            // Решение ошибки при запусен на Android 8
            // Которая возникает, если попытаться отобразить список слишком рано.
            if (hasFocus) {
                new Handler().post(() -> tvEmail.showDropDown());
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

    private boolean isEmailPasswordValid() {
        if (!isEmailValid()) {
            tvEmail.setError("Неверный Email");
        }
        if (!isPasswordValid()) {
            etPassword.setError("Введите пароль");
        }
        return isEmailValid() && isPasswordValid();
    }

    private void showMessage(@StringRes int string) {
        Toast.makeText(getActivity(), string, Toast.LENGTH_LONG).show();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fr_auth, container, false);

        sharedPreferencesHelper = new SharedPreferencesHelper(getActivity());

        tvEmail = v.findViewById(R.id.etLogin);
        etPassword = v.findViewById(R.id.etPassword);
        btnEnter = v.findViewById(R.id.buttonEnter);
        btnRegister = v.findViewById(R.id.buttonRegister);

        btnEnter.setOnClickListener(onEnterClickListener);
        btnRegister.setOnClickListener(onRegisterClickListener);
        tvEmail.setOnFocusChangeListener(onLoginFocusChangeListener);

        emailedUsersAdapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_dropdown_item_1line,
                sharedPreferencesHelper.getSuccessEmails()
        );
        tvEmail.setAdapter(emailedUsersAdapter);

        return v;
    }

}
