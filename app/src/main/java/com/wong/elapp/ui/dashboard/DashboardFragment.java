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
import com.wong.elapp.network.mapper.LocalService;
import com.wong.elapp.pojo.RandomList;
import com.wong.elapp.pojo.vo.LoginParam;
import com.wong.elapp.pojo.vo.Result;
import com.wong.elapp.ui.home.HomeViewModel;
import com.wong.elapp.utils.ERRCODE;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class DashboardFragment extends Fragment {

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
        final Button btn = binding.button;

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

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}