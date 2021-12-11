package com.wong.elapp.hilt;

import android.media.MediaPlayer;

import com.wong.elapp.hilt.types.LocalMapper;
import com.wong.elapp.hilt.types.LocalRetrofit;
import com.wong.elapp.hilt.types.YoudaoMapper;
import com.wong.elapp.hilt.types.YoudaoRetrofit;
import com.wong.elapp.network.TokenIncepter;
import com.wong.elapp.network.mapper.LocalService;
import com.wong.elapp.network.mapper.YoudaoService;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

@Module
@InstallIn(ApplicationComponent.class)
public class NetworkModule {
    /**
     * 此类为网络模块
     */

    /**
     * 配置okhttp,
     * 这个okhttp是为连接本地的java服务器提供的。
     */

    @Singleton
    @Provides
    OkHttpClient provideOkhttpForLocalService(){
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .addInterceptor(new TokenIncepter())
                .build();
        return okHttpClient;
    }

    /**
     * 配置第二个okhttp
     * 这个okhttp是位有道翻译准备的。
     */
//    @Singleton
//    @Provides
//    OkHttpClient provideOkhttpForYoudao(){
//        OkHttpClient okHttpClient = new OkHttpClient.Builder()
//                .connectTimeout(10, TimeUnit.SECONDS)
////                .addInterceptor(new TokenIncepter())
//                .build();
//        return okHttpClient;
//    }

    /**
     * 配置Retrofit，
     * 这个retrofit是为本地java服务器提供的
     */
    @LocalRetrofit
    @Singleton
    @Provides
    Retrofit provideRetrofit(OkHttpClient okHttpClient){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.wingloong.top/")
//                .baseUrl("http://172.31.129.43:8888/")
                .client(okHttpClient)
                .addConverterFactory(GsonConverterFactory.create())
//                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        return retrofit;
    }

    /**
     * 配置第二个retrofit,
     * 这个retrofit是为有道翻译准备的
     */
    @YoudaoRetrofit
    @Singleton
    @Provides
    Retrofit provideRetrofitForYoudao(){
        Retrofit retrofit2 = new Retrofit.Builder()
                .baseUrl("https://openapi.youdao.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit2;
    }

    /**
     * 配置Service，
     * 这个是为Java服务器准备的。
     */
    @Singleton
    @Provides
    LocalService provideLocalService(@LocalRetrofit Retrofit retrofit){
       return retrofit.create(LocalService.class);
    }

    /**
     * 配置Service
     * 这个是为有道翻译准备的
     */
    @Singleton
    @Provides
    YoudaoService provideYoudaoService(@YoudaoRetrofit Retrofit retrofit2){
        return retrofit2.create(YoudaoService.class);
    }


}
