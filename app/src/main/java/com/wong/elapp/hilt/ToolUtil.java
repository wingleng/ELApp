package com.wong.elapp.hilt;

import com.wong.elapp.utils.DensityUtil;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ApplicationComponent;

@Module
@InstallIn(ApplicationComponent.class)
public class ToolUtil {
    /**
     * 一个dp转px工具
     */
    @Provides
    DensityUtil getDensityUil(){
        return new DensityUtil();
    }

    /**
     * Room数据库
     */


}
