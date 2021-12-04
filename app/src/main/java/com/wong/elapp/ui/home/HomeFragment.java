package com.wong.elapp.ui.home;

import android.animation.ValueAnimator;
import android.os.Bundle;
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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import com.qmuiteam.qmui.layout.QMUIButton;
import com.qmuiteam.qmui.widget.QMUIProgressBar;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.wong.elapp.R;
import com.wong.elapp.databinding.FragmentHomeBinding;
import com.wong.elapp.network.mapper.LocalService;
import com.wong.elapp.pojo.RandomList;
import com.wong.elapp.utils.ERRCODE;

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

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);//这里有点担心的问题就是，这个viewmodel是绑定到Fragment的，所以生命周期会不会收到影响
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //获取组件引用
        qm_send_btn = binding.qmSend;
        circleProgressBar = binding.circleProgressBar;


        //TODO:进度条组件仍未完成
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
     * 发送请求按钮的监听器
     */
    class Btn_sendListenter implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            //启动按钮动画
            Call<List<RandomList>> call = localService.getRandomWords();
            call.enqueue(new Callback<List<RandomList>>() {
                @Override
                public void onResponse(Call<List<RandomList>> call, Response<List<RandomList>> response) {
                    Log.i(ERRCODE.REQUEST_SUCCESS.getMsgtype(), ERRCODE.REQUEST_SUCCESS.getMsg());
                    Log.i("返回的数据：",response.body().toString());
                    homeViewModel.setList_word(response.body());

                    //跳转到其他界面
                    Navigation.findNavController(v).navigate(R.id.action_navigation_home_to_reciteFragment2);
                }

                @Override
                public void onFailure(Call<List<RandomList>> call, Throwable t) {
                    Log.i(ERRCODE.REQUEST_FAILED.getMsgtype(), ERRCODE.REQUEST_FAILED.getMsg());
                }
            });
        }
    }

    /**
     * next按钮的监听器
     */
    class Btn_NextListener implements View.OnClickListener{
        Iterator<RandomList> it;
        @Override
        public void onClick(View v) {
                it = homeViewModel.getItera_randlist().getValue();
                if(it.hasNext()){
                    //这一步有点多余，因为可以直接设置当前界面的。却设置到viewmodel中去，就为了那所谓的安全更新？
                    homeViewModel.setWord_ui(it.next());
                }
        }
    }
}