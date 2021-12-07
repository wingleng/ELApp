package com.wong.elapp.hilt;

import com.wong.elapp.network.mapper.LocalService;

import java.util.concurrent.TimeUnit;

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
     * 配置okhttp，默认配置的话是不需要的，但是如果需要使用拦截器的话（比如每次发送请求的时候需要带上凭证之类的)
     */
    @Singleton
    @Provides
    OkHttpClient provideOkhttpForLocalService(){
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .build();
        return okHttpClient;
    }

    /**
     * 配置Retrofit，这个也可能需要配置两个不一样的
     */
    @Singleton
    @Provides
    Retrofit provideRetrofit(OkHttpClient okHttpClient){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://172.31.130.110:8888/")
                .addConverterFactory(GsonConverterFactory.create())
//                .addConverterFactory(ScalarsConverterFactory.create())
                .build();
        return retrofit;
    }

    /**
     * 最后配置mapper，这个Mapper可能需要两个。。
     */
    @Singleton
    @Provides
    LocalService provideLocalService(Retrofit retrofit){
       return retrofit.create(LocalService.class);
    }



}
