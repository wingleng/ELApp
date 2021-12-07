package com.wong.elapp.network.mapper;

import com.wong.elapp.pojo.RandomList;
import com.wong.elapp.pojo.vo.LoginParam;
import com.wong.elapp.pojo.vo.Result;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface LocalService {
    /**
     * 这个接口是连接到本地的服务器中的，
     * 本地服务器主要提供mongodb的查询服务
     * MySQL的增删改操作
     *
     */

    @GET("findwords/randWord/10")
    Call<Result<List<RandomList>>> getRandomWords();

    @GET("findwords/test")
    Call<String> getTest();

    /**
     * 配置用户注册接口
     */
    @POST("user/addUser")
    Call<Result> registe(@Body LoginParam loginParam);

    /**
     * 配置用户登录接口
     * @param loginParam
     * @return
     */
    @POST("user/login")
    Call<Result> login(@Body LoginParam loginParam);

}
