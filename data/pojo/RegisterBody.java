package com.gaurabneupane.id424.data.pojo;

import com.google.gson.annotations.SerializedName;

public class RegisterBody {

    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;


    public RegisterBody(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
