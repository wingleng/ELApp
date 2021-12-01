package com.wong.elapp.network.mapper;

import com.wong.elapp.pojo.RandomList;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface LocalService {
    /**
     * 这个接口是连接到本地的服务器中的，
     * 本地服务器主要提供mongodb的查询服务
     * MySQL的增删改操作
     *
     */

    @GET("findwords/randWord/10")
    Call<List<RandomList>> getRandomWords();

    @GET("findwords/test")
    Call<String> getTest();
}
