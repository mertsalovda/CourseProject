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

import okhttp3.MediaType;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.mertsalovda.myfirstapplication.model.UserRegistration;

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
                // UserRegistration для регистрации,
                // т.к. модель запроса на регистрацию отличается от модели,
                // получаемой при GET запросе User
                UserRegistration user = new UserRegistration(
                        etLogin.getText().toString(),
                        etName.getText().toString(),
                        etPassword.getText().toString());

                // Асинхронный запрос
                ApiUtils.getApiService().registration(user).enqueue(new Callback<Void>() {
                    // Обрабатываем запрос в UI-потоке
                    Handler handler = new Handler(getActivity().getMainLooper());

                    @Override
                    public void onResponse(Call<Void> call, final Response<Void> response) {
                        handler.post(() -> {
                            if (response.isSuccessful()) {
                                responseCodeProcessor(response.code());
                                if (getFragmentManager() != null) {
                                    getFragmentManager().popBackStack();
                                }
                            } else {
                                //TODO детальная обработка ошибок
                                responseCodeProcessor(response.code());
                                showMessage(R.string.registration_error);
                            }
                        });
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        handler.post(() -> showMessage(R.string.request_error));
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

    private void responseCodeProcessor(int code) {
        switch (code) {
            case 204:
                showMessage(R.string.auth_ok);
                break;
            case 400:
                showMessage(R.string.dont_valid);
                break;
            case 500:
                showMessage(R.string.server_error);
                break;
        }
    }

    private void showMessage(@StringRes int string) {
        Toast.makeText(getActivity(), string, Toast.LENGTH_LONG).show();
    }

}
