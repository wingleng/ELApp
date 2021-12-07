package com.wong.elapp.network;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class TokenIncepter implements Interceptor {
    public static String TOKEN=null;
    /**
     * 为当前客户端发送的请求添加token，如果没有，就设置为Null
     * @param chain
     * @return
     * @throws IOException
     */
    @Override
    public Response intercept(Chain chain) throws IOException {
        //如果token为空的情况下，什么都不做
        if (TOKEN ==null){
            return chain.proceed(chain.request());
        }
        Request origin = chain.request();
        Request.Builder builder = origin.newBuilder()
                .header("Authorization",TOKEN);
        Request request = builder.build();
        return chain.proceed(request);
    }
}
