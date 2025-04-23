package com.gaurabneupane.id424.ui;

import static com.gaurabneupane.id424.ui.LoginViewModel.KEY_EMAIL;
import static com.gaurabneupane.id424.ui.LoginViewModel.KEY_PASSWORD;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.gaurabneupane.id424.R;
import com.gaurabneupane.id424.data.pojo.UserResponse;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.Map;
import java.util.Objects;

public class LoginFragment extends Fragment {
    LoginViewModel viewModel;
    TextInputLayout emailTil;
    TextInputEditText emailEt;
    TextInputLayout passwordTil;
    TextInputEditText passwordEt;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(requireActivity()).get(LoginViewModel.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_login, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        initializeView(view);
        setupObservers();
    }

    private void initializeView(View view){

        emailTil = view.findViewById(R.id.emailTIL);
        emailEt = view.findViewById(R.id.email);

        passwordTil = view.findViewById(R.id.passwordTIL);
        passwordEt = view.findViewById(R.id.password);


        view.findViewById(R.id.registerBtn).setOnClickListener((registerBtnView)->{
            viewModel.onRegisterClicked(
                    Objects.requireNonNull(emailEt.getText()).toString(),
                    Objects.requireNonNull(passwordEt.getText()).toString()
            );
        });
    }

    private void setupObservers() {
        viewModel.formErrors.observe(requireActivity(), formErrors -> {
            handleFormErrors(formErrors);
        });
    }


    private void handleLoginSuccess(UserResponse userResponse){
        Toast.makeText(requireActivity(),
                userResponse.getName() + " Logged in successfully.",
                Toast.LENGTH_LONG).show();
        //now navigate to dashboard or home screen.
    }
    private void handleMessage(String message){
        new AlertDialog.Builder(requireActivity())
                .setTitle("Message")
                .setMessage(message)
                .setPositiveButton(
                        "OK",
                        (dialog, which)-> dialog.dismiss()
                ).show();
    }

    private void handleFormErrors(Map<String, String> formErrors){
        String emailError = formErrors.get(KEY_EMAIL);
        String passwordError = formErrors.get(KEY_PASSWORD);


        if(emailError != null){
            emailTil.setError(emailError);
            emailTil.setErrorEnabled(true);
        }else{
            emailTil.setError(null);
            emailTil.setErrorEnabled(false);
        }

        if(passwordError != null){
            passwordTil.setError(passwordError);
            passwordTil.setErrorEnabled(true);
        }else{
            passwordTil.setError(null);
            passwordTil.setErrorEnabled(false);
        }
    }
}
