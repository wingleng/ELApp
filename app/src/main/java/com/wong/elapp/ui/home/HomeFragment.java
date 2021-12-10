package com.wong.elapp.ui.home;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.qmuiteam.qmui.layout.QMUIButton;
import com.qmuiteam.qmui.widget.QMUIProgressBar;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.wong.elapp.R;
import com.wong.elapp.databinding.FragmentHomeBinding;
import com.wong.elapp.hilt.types.LocalMapper;
import com.wong.elapp.network.TokenIncepter;
import com.wong.elapp.network.mapper.LocalService;
import com.wong.elapp.pojo.RandomList;
import com.wong.elapp.pojo.vo.Result;
import com.wong.elapp.utils.ERRCODE;
import com.wong.elapp.utils.WebError;

import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

@AndroidEntryPoint
public class HomeFragment extends Fragment {

    @Inject
    LocalService localService;

    private QMUIRoundButton qm_send_btn;

    private QMUIProgressBar circleProgressBar;

    private HomeViewModel homeViewModel;

    private FragmentHomeBinding binding;

    //在主界面设置一个登录检测，检测以前是否登录过，没有的话，就弹出登录界面。
    private MutableLiveData<Boolean> wasLogined;

    /**
     * 组件初始化
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        wasLogined = homeViewModel.getWasLogined();//这个是个动态数据。


        //获取组件引用
        qm_send_btn = binding.qmSend;
        circleProgressBar = binding.circleProgressBar;


        //TODO:进度条组件仍未完成，需要显示总单词数量和当前用户已经背了的单词数量。
        //设置进度条
        circleProgressBar.setMaxValue(100);
        circleProgressBar.setProgress(30,true);
        circleProgressBar.setQMUIProgressBarTextGenerator(new QMUIProgressBar.QMUIProgressBarTextGenerator() {
            @Override
            public String generateText(QMUIProgressBar progressBar, int value, int maxValue) {
                return 100 * value / maxValue + "%";
            }
        });




        //发送请求
        qm_send_btn.setOnClickListener(new Btn_sendListenter());

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * 检查是否登录
     * 因为界面跳转需要使用到view，上面的oncreateview中，view暂时还不能使用，所以就在这里进行登录的检查。
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //检查登录：
        if (wasLogined.getValue() == false){
            Log.i("当前没有登录","自动跳转到登录界面");
            checkLogin();
        }
    }

    /**
     * 检查方法
     */
    public void checkLogin(){
        SharedPreferences sp = getContext().getSharedPreferences("config", Context.MODE_PRIVATE);
        String token = sp.getString("TOKEN", "");
        Long setTime = sp.getLong("setTime",0L);

        if (!token.equals("")){//已经登录过，先检查时间，如果时间在期限之内，就将token放置到请求头中；如果过期了，就直接删除sp，然后拉出登录界面。
            Date date = new Date();
            Long now = date.getTime();
            Long during = now - setTime;
            if (during > 1000 * 60 * 60 * 2){//设定超时时间为两个小时，超时了就直接清空token和时间。
                SharedPreferences.Editor editor = sp.edit();
                editor.clear().commit();
                //拉起登录界面
                startLoginFragment();
            }else{//还在时效内，设置token
                TokenIncepter.TOKEN = token;
                wasLogined.setValue(true);
            }
        }else{//之前没有登录过，所以这里直接拉出登录界面
                startLoginFragment();
        }
    }

    /**
     * 拉起登录界面
     */
    public void startLoginFragment(){
        Navigation.findNavController(getView()).navigate(R.id.loginFragment);
    }

    /**
     * 发送请求按钮的监听器
     */
    class Btn_sendListenter implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            //启动按钮动画
            Call<Result<List<RandomList>>> call = localService.getRandomWords();
            call.enqueue(new Callback<Result<List<RandomList>>>() {
                @Override
                public void onResponse(Call<Result<List<RandomList>>> call, Response<Result<List<RandomList>>> response) {
                    Log.i(ERRCODE.REQUEST_SUCCESS.getMsgtype(), ERRCODE.REQUEST_SUCCESS.getMsg());
                    if (response.body().getCode() == WebError.NO_LOGIN.getCode()){
                        Log.i("没有登录：","当前资源无法访问");
                    }else if (response.body().getCode() == 200){
                        Log.i("登录成功：",response.body().getMsg());
                        Log.i("返回的数据：",response.body().toString());
                        List<RandomList> list = (List<RandomList>) response.body().getData();
                        homeViewModel.setList_word((List<RandomList>) response.body().getData());
                        //跳转到其他界面
                        Navigation.findNavController(v).navigate(R.id.action_navigation_home_to_reciteFragment2);
                    }



                }

                @Override
                public void onFailure(Call<Result<List<RandomList>>> call, Throwable t) {
                    Log.i(ERRCODE.REQUEST_FAILED.getMsgtype(), ERRCODE.REQUEST_FAILED.getMsg());
                }
            });
        }
    }

}