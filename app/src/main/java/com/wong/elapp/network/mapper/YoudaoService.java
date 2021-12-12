package com.wong.elapp.network.mapper;

import com.wong.elapp.pojo.vo.Youdaoresult.YoudaoResult;
import com.wong.elapp.pojo.vo.YoudaoresultPic2Text.YoudaoresultPic2Text;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface YoudaoService {

    /**
     * 普通的文字翻译请求接口
     * @param params
     * @return
     */
    @POST("api")
    @FormUrlEncoded
    Call<YoudaoResult> textTranslate(@FieldMap Map<String,String> params);

    /**
     * 图片翻译成文字的接口
     * @param params
     * @return
     */
    @POST("ocrtransapi")
    @FormUrlEncoded
    Call<YoudaoresultPic2Text> pic2text(@FieldMap Map<String,String> params);

    /**
     * 图片翻译成图片的接口
     */
}
