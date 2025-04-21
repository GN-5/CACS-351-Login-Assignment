package com.gaurabneupane.id424.ui;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.gaurabneupane.id424.data.pojo.RegisterBody;
import com.gaurabneupane.id424.data.pojo.RegisterResponse;
import com.gaurabneupane.id424.data.pojo.UserResponse;
import com.gaurabneupane.id424.utility.AppStorage;

import java.io.IOException;
import java.security.Provider;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;

import okhttp3.ResponseBody;
import retrofit2.Response;

public class LoginViewModel extends AndroidViewModel {

    ExecutorService executor = Executors.newCachedThreadPool();

    public static final String KEY_FULL_NAME = "full_name";
    public static final String KEY_EMAIL = "email";
    public static final String KEY_PASSWORD = "password";
    public static final String KEY_CONFIRM_PASSWORD = "confirm_password";

    private MutableLiveData<Map<String, String>>  _formErrors = new MutableLiveData<>();
    LiveData<Map<String,String>> formErrors = _formErrors;

    private MutableLiveData<Boolean>  _isLoading = new MutableLiveData<>();
    LiveData<Boolean> isLoading = _isLoading;

    private MutableLiveData<UserResponse>  _registrationSuccess = new MutableLiveData<>();
    LiveData<UserResponse> registerUser = _registrationSuccess;

    private MutableLiveData<String> _message = new MutableLiveData<>();
    LiveData<String> message = _message;

    private AppStorage appStorage;

    public LoginViewModel(@NonNull Application application) {
        super(application);
        appStorage = new AppStorage(application);
    }

    @Override
    protected void onCleared() {
        executor.shutdown();
        super.onCleared();
    }

    Map<String, String> validateFullName(String fullName){
        HashMap<String, String> fullNameErrors = new HashMap<>();
        if(fullName.isEmpty()){
            fullNameErrors.put(KEY_FULL_NAME, "Full Name is required");
        } else if (!Pattern.compile("^[a-zA-Z]{4,}(?: [a-zA-Z]+){0,2}$").matcher(fullName).matches()) {
            fullNameErrors.put(KEY_FULL_NAME, "Full Name is required");
        }
        return fullNameErrors;
    }

    Map<String, String> validateEmail(String email){
        HashMap<String, String> emailErrors = new HashMap<>();

        if(email.isEmpty()){
            emailErrors.put(KEY_EMAIL, "Email is required");
        }else if(!Pattern.compile("^[\\w\\.-]+@[\\w\\.-]+\\.[a-zA-Z]{2,6}$").matcher(email).matches()){
            emailErrors.put(KEY_EMAIL, "Email is invalid");
        }
        return emailErrors;
    }

    Map<String, String> validatePassword(String password){
        HashMap<String, String> passwordErrors = new HashMap<>();

        if(password.isEmpty()){
            passwordErrors.put(KEY_PASSWORD, "Password is required");
        }

        return passwordErrors;
    }

    Map<String, String> validateConfirmPassword(String password, String confirmPassword){
        HashMap<String, String> confirmPasswordErrors = new HashMap<>();

        if(confirmPassword.isEmpty()){
            confirmPasswordErrors.put(KEY_CONFIRM_PASSWORD, "Confirm Password is required");
        } else if (validatePassword(password).isEmpty() && !password.equals(confirmPassword)) {
            confirmPasswordErrors.put(KEY_CONFIRM_PASSWORD, "Passwords do not match.");
        }

        return confirmPasswordErrors;
    }

    public boolean validate(String fullName, String email, String password, String confirmPassword) {

        HashMap<String, String> errorContainer = new HashMap<>();

        Map<String, String> fullNameErrors = validateFullName(fullName);
        Map<String, String> emailErrors = validateEmail(email);
        Map<String, String> passwordErrors = validatePassword(password);
        Map<String, String> confirmPasswordErrors = validateConfirmPassword(password, confirmPassword);

        errorContainer.putAll(fullNameErrors);
        errorContainer.putAll(emailErrors);
        errorContainer.putAll(passwordErrors);
        errorContainer.putAll(confirmPasswordErrors);

        _formErrors.setValue(errorContainer);

        return errorContainer.isEmpty();
    }

    public void onRegisterClicked(String fullName, String email, String password, String confirmPassword){
        if(validate(fullName, email, password, confirmPassword)){
            registerUser(
                    new RegisterBody(fullName, email, password, confirmPassword)
            );
        }
    }


    public void registerUser(RegisterBody registerBody){
        executor.execute(() -> {
            try {
                _isLoading.postValue(true);
                Provider ServiceProvider;
                Response<RegisterResponse> response = ServiceProvider.getService().registerUser(registerBody).execute();
                if(response.isSuccessful()){
                    RegisterResponse registerResponse = response.body();
                    if(registerResponse == null){
                        throw new RuntimeException("No Register response available");

                    }
                    appStorage.saveToken(registerResponse.getToken());
                    _registrationSuccess.postValue(registerResponse.getUser());
                }else {
                    //{"email:["The email is already taken.]} response occurs
                    try (ResponseBody errorRes = response.errorBody()) {
                        if (errorRes != null) {
                            String errorString = errorRes.string();
                            //Now convert raw response to class GSON
                            //Map<String, List<String>>
                            TypeToken<Map<String, List<String>>>  typeToken = new TypeToken<Map<String, List<String>>>() {};
                            Map<String, List<String>> mapError = new Gson().fromJson(errorString, typeToken);
                            HashMap<String, String> parsedError = new HashMap<>();

                            for(String key: mapError.keySet()){
                                List<String> errorList = mapError.get(key);
                                if(errorList != null && !errorList.isEmpty()){
                                    parsedError.put(key, errorList.get(0));
                                }
                            }

                            _formErrors.postValue(parsedError);
                        }
                    }
                }

            } catch (Exception e) {
                // If no internet, it will reach this condition
                Log.e("API_FAILED", "RegisterUser: ", e);

                //this means no internet
                if(e instanceof IOException){
                    _message.postValue("Please check your internet and try again.");
                }else{
                    _message.postValue("Something went wrong. Please try again later.");
                }
            }
            finally {
                _isLoading.postValue(false);
            }
        });
    }


}
