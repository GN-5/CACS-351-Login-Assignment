package com.gaurabneupane.id424.data;

import com.gaurabneupane.id424.data.pojo.LoginBody;
import com.gaurabneupane.id424.data.pojo.LoginResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface AuthService {
    static String BASE_URL = "https://sunilprasai.com.np";
    @POST("api/user/login")
    Call<LoginResponse> registerUser(@Body LoginBody body);
}
