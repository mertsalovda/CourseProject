package ru.mertsalovda.myfirstapplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AuthFragment extends Fragment {

    private AutoCompleteTextView tvLogin;
    private EditText etPassword;
    private Button btnEnter;
    private Button btnRegister;
    private SharedPreferencesHelper sharedPreferencesHelper;

    private ArrayAdapter<String> loginedUsersAdapter;

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
                User user = sharedPreferencesHelper.login(
                        tvLogin.getText().toString(),
                        etPassword.getText().toString());
                if (user != null) {
                    Intent startProfileIntent =
                            new Intent(getActivity(), ProfileActivity.class);
                    startProfileIntent.putExtra(ProfileActivity.USER_KEY, user);
                    startActivity(startProfileIntent);
                    getActivity().finish();
                } else {
                    showMessage(R.string.login_error);
                }
            } else {
                showMessage(R.string.input_error);
            }

            for (User user : sharedPreferencesHelper.getUsers()) {
                if (user.getEmail().equalsIgnoreCase(tvLogin.getText().toString())
                        && user.getPassword().equals(etPassword.getText().toString())) {
                    break;
                }
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
                tvLogin.showDropDown();
            }
        }
    };

    private boolean isEmailValid() {
        return !TextUtils.isEmpty(tvLogin.getText())
                && Patterns.EMAIL_ADDRESS.matcher(tvLogin.getText()).matches();
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

        sharedPreferencesHelper = new SharedPreferencesHelper(getActivity());

        tvLogin = v.findViewById(R.id.etLogin);
        etPassword = v.findViewById(R.id.etPassword);
        btnEnter = v.findViewById(R.id.buttonEnter);
        btnRegister = v.findViewById(R.id.buttonRegister);

        btnEnter.setOnClickListener(onEnterClickListener);
        btnRegister.setOnClickListener(onRegisterClickListener);
        tvLogin.setOnFocusChangeListener(onLoginFocusChangeListener);

        loginedUsersAdapter = new ArrayAdapter<>(
                getActivity(),
                android.R.layout.simple_dropdown_item_1line,
                sharedPreferencesHelper.getSuccessLogins()
        );
        tvLogin.setAdapter(loginedUsersAdapter);

        return v;
    }
}
