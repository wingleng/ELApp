package com.wong.elapp.ui.dashboard;

import android.os.Bundle;
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
import com.wong.elapp.pojo.RandomList;
import com.wong.elapp.pojo.vo.LoginParam;
import com.wong.elapp.pojo.vo.Result;
import com.wong.elapp.ui.home.HomeViewModel;
import com.wong.elapp.utils.ERRCODE;
import com.wong.elapp.utils.WebError;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class DashboardFragment extends Fragment {

    @LocalMapper
    @Inject
    LocalService localService;

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

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}