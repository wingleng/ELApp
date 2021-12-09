package com.wong.elapp.network.mapper;

import com.wong.elapp.pojo.vo.Youdaoresult.YoudaoResult;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface YoudaoService {

    @POST("api")
    @FormUrlEncoded
    Call<YoudaoResult> textTranslate(@FieldMap Map<String,String> params);
}
