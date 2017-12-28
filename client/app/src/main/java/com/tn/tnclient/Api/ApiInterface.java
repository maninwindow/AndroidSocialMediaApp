package com.tn.tnclient.Api;

/**
 * Created by Suhail on 8/7/2017.
 */

import com.tn.tnclient.Models.IMToken;
import com.tn.tnclient.Models.LoginResponse;
import com.tn.tnclient.Models.LoginUser;
import com.tn.tnclient.Models.PassResetResponse;
import com.tn.tnclient.Models.RegisterResponse;
import com.tn.tnclient.Models.RegisterUser;
import com.tn.tnclient.Models.ResponseBody;
import com.tn.tnclient.Models.User;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiInterface {

    @POST("user/login")
    Call<LoginResponse> login(@Body LoginUser loginUser);

    @POST("user/register")
    Call<RegisterResponse> register(@Body RegisterUser registerUser);

    @GET("user/phoneValidation")
    Call<PassResetResponse> validate(@Query("phone") String phone);

    @GET("user/registerValidation")
    Call<PassResetResponse> registerValidate(@Query("phone") String phone);

    @GET("user/resetPassword")
    Call<PassResetResponse> resetPassword(@Query("phone") String phone, @Query("new_pass") String new_password);

    @GET("user/size")
    Call<List<User>> retrieveUsers(@Path("users") String users);

    @Multipart
    @POST("user/profileImage")
    Call<ResponseBody> uploadImage(@Part MultipartBody.Part image, @Part("name") RequestBody name);

    @GET("user/token")
    Call<IMToken> retrieveToken(@Query("phone") String phone);
}

