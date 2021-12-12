package com.wong.elapp.network.mapper;

import com.wong.elapp.pojo.vo.Baidu.accesstoken.Access_Token;
import com.wong.elapp.pojo.vo.Baidu.resultData.resultData;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface BaiduService {
    @GET("oauth/2.0/token")
    Call<Access_Token> getToken(@Query("grant_type")String grant_type,
                                @Query("client_id")String client_id,
                                @Query("client_secret")String client_secret);

    @Multipart
    @Headers("Content-Type:mutipart/form-data")
    @POST("file/2.0/mt/pictrans/v1")
    Call<resultData> sendPic(@Part("image")MultipartBody multipartBody,
                             @Query("access_token")String access_token
                             );

}
