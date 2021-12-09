package com.wong.elapp.ui.dashboard;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.wong.elapp.R;
import com.wong.elapp.databinding.FragmentDashboardBinding;
import com.wong.elapp.hilt.types.LocalMapper;
import com.wong.elapp.network.TokenIncepter;
import com.wong.elapp.network.mapper.LocalService;
import com.wong.elapp.network.mapper.YoudaoService;
import com.wong.elapp.pojo.RandomList;
import com.wong.elapp.pojo.vo.LoginParam;
import com.wong.elapp.pojo.vo.Result;
import com.wong.elapp.pojo.vo.Youdaoresult.YoudaoResult;
import com.wong.elapp.ui.home.HomeViewModel;
import com.wong.elapp.utils.ERRCODE;
import com.wong.elapp.utils.WebError;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class DashboardFragment extends Fragment {

    @Inject
    LocalService localService;

    @Inject
    YoudaoService youdaoService;

    private HomeViewModel homeViewModel;
    private FragmentDashboardBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textView;
        //TODO:以下两个按钮都只是测试注册和登录是否可用而已，以后需要单独创建一个fragment来提供这两个功能。
        final Button btn = binding.button;
        final Button regisBtn = binding.registBtn;
        final Button tranlBtn = binding.textTranslate;

        /**
         * 一个点击按钮，测试用户注册
         */
        btn.setOnClickListener((v -> {
            Call<Result> call = localService.registe(new LoginParam("旺财", "000000"));
            call.enqueue(new Callback<Result>() {
                @Override
                public void onResponse(Call<Result> call, Response<Result> response) {
                    Log.i("应答数据：",response.body().toString());
                }

                @Override
                public void onFailure(Call<Result> call, Throwable t) {
                    Log.i(ERRCODE.REQUEST_FAILED.getMsgtype(), ERRCODE.REQUEST_FAILED.getMsg());
                }
            });
        }));

        /**
         * 登录测试按钮
         */
        regisBtn.setOnClickListener((v -> {
            Call<Result<String>> call = localService.login(new LoginParam("旺财", "000000"));
            call.enqueue(new Callback<Result<String>>() {
                @Override
                public void onResponse(Call<Result<String>> call, Response<Result<String>> response) {
//                    Log.i("登录成功:",response.body().toString());
//                    TokenIncepter.TOKEN =
                    //判断是否登录成功：
                    Result res = response.body();
                    if (res.getCode() == WebError.ACCOUNT_PWD_NOT_EXIST.getCode()){
                            Log.i("登录:","用户名或者密码不正确");
                    }else if (res.getCode() == 200){
                            Log.i("登录：",res.getMsg());
                            Log.i("登录：",res.getData()+"");
                            TokenIncepter.TOKEN = (String) res.getData();
                    }else if (res.getCode() == WebError.NO_LOGIN.getCode()){
                            Log.i("没有登录：","没有登录，无法访问这些资源");
                    }
                }

                @Override
                public void onFailure(Call<Result<String>> call, Throwable t) {
                    Log.i(ERRCODE.REQUEST_FAILED.getMsgtype(), ERRCODE.REQUEST_FAILED.getMsg());
                }
            });
        }));

        /**
         * 有道翻译接口的测试
         */
        tranlBtn.setOnClickListener(v -> {

            String APP_KEY = "5b9c6473842434f1";
            String APP_SECRET = "DePFvNrafw6FKqFzYKYJsXrF9fOXmLXl";

            Map<String,String> params = new HashMap<String,String>();
            String q = "world";
            String salt = String.valueOf(System.currentTimeMillis());
            params.put("from", "en");
            params.put("to", "zh-CHS");
            params.put("signType", "v3");
            String curtime = String.valueOf(System.currentTimeMillis() / 1000);
            params.put("curtime", curtime);
            String signStr = APP_KEY + truncate(q) + salt + curtime + APP_SECRET;
            String sign = getDigest(signStr);
            params.put("appKey", APP_KEY);
            params.put("q", q);
            params.put("salt", salt);
            params.put("sign", sign);
            Log.i("发送请求","发送请求");

            Call<YoudaoResult> call = youdaoService.textTranslate(params);
            call.enqueue(new Callback<YoudaoResult>() {
                @Override
                public void onResponse(Call<YoudaoResult> call, Response<YoudaoResult> response) {
                    Log.i("返回的数据：",response.body()==null?"数据为空":response.body().getBasic().getExplains().get(0));
                }

                @Override
                public void onFailure(Call<YoudaoResult> call, Throwable t) {
                    Log.i(ERRCODE.REQUEST_FAILED.getMsgtype(), ERRCODE.REQUEST_FAILED.getMsg());
                }
            });
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * 生成加密字段
     */
    public static String getDigest(String string) {
        if (string == null) {
            return null;
        }
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        byte[] btInput = string.getBytes(StandardCharsets.UTF_8);
        try {
            MessageDigest mdInst = MessageDigest.getInstance("SHA-256");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }

    /**
     * 关键词操作，超过20个字符时需要操作。。
     * @param q
     * @return
     */
    public static String truncate(String q) {
        if (q == null) {
            return null;
        }
        int len = q.length();
        String result;
        return len <= 20 ? q : (q.substring(0, 10) + len + q.substring(len - 10, len));
    }
}