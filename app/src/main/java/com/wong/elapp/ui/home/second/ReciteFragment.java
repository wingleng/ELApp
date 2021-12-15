package com.wong.elapp.ui.home.second;

import android.animation.ValueAnimator;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.qmuiteam.qmui.layout.QMUIButton;
import com.qmuiteam.qmui.util.QMUIToastHelper;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.wong.elapp.R;
import com.wong.elapp.databinding.FragmentReciteBinding;
import com.wong.elapp.network.mapper.LocalService;
import com.wong.elapp.pojo.RandomList;
import com.wong.elapp.pojo.vo.Result;
import com.wong.elapp.ui.home.HomeViewModel;
import com.wong.elapp.utils.DensityUtil;
import com.wong.elapp.utils.ERRCODE;
import com.wong.elapp.utils.cardtransformer.AlphaAndScalePageTransformer;
import com.wong.elapp.utils.viewpagerutil.CommonUtils;
import com.wong.elapp.utils.viewpagerutil.MyPagerHelper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


@AndroidEntryPoint
public class ReciteFragment extends Fragment {

    @Inject
    DensityUtil densityUtil;

    @Inject
    LocalService localService;

    FragmentReciteBinding binding;
    HomeViewModel homeViewModel;
    ViewPager2 viewPager2;
    QMUIRoundButton btnLeft;
    QMUIRoundButton btnRight;
    boolean isDragging;

    //存储各自的List
    private List<String> forget_list = new ArrayList<>();
    private List<String> remember_list = new ArrayList<>();

    private int WORDSIZE;//列表长度
    private int clickTime=0;//点击次数，当点击次数等于列表长度时，就弹出到底提示框。。

    public ReciteFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentReciteBinding.inflate(inflater,container,false);
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);

        viewPager2 = binding.viewPager2;
        btnLeft = binding.btnLeft;
        btnRight = binding.btnRight;

        //对viewpager设置适配器：
        List<RandomList> rlist = homeViewModel.getList_word().getValue();//从viewmodel中获取到数据
        WORDSIZE = rlist.size();//存一下数据长度
        viewPager2.setAdapter(new ViewPagerAdapter(rlist));//设置适配器,需要把数据和当前fragment或者activity的上下文传进去，因为要使用音乐播放器。



        //设置Transformer的动画效果
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        int margin_px = DensityUtil.dip2px(getContext(),getResources().getDimension(R.dimen.activity_horizontal_margin));
        MarginPageTransformer marginPageTransformer = new MarginPageTransformer(margin_px);//设置item之间的间距
        compositePageTransformer.addTransformer(marginPageTransformer);
        compositePageTransformer.addTransformer(new AlphaAndScalePageTransformer());//设置左右滑动的效果
        viewPager2.setPageTransformer(compositePageTransformer);
        viewPager2.setOffscreenPageLimit(3);


        //配置页面滑动以及监听器
        viewPager2.setUserInputEnabled(false);//禁止用户进行滑动，为了方便两个按钮。。。



        viewPager2.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                super.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Log.i("当前选择的位置：","position"+position);
                Log.i("当前点击次数：",clickTime+"====>"+WORDSIZE);
                forget_list.add(rlist.get(position).getId());
                if (clickTime-1 == WORDSIZE-1){//到达底部，发送请求，并且弹出提示框，返回主界面。。
                    getActivity().runOnUiThread(()->{
                        QMUIToastHelper.show(Toast.makeText(getActivity(),"到底了~"+forget_list.size(),Toast.LENGTH_LONG));
                    });

                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });


        //设置按钮的监听器，真是麻了，谷歌居然将ViewPager的速度限制死，需要用这种麻烦的手段来控制翻页速度
        btnLeft.setOnClickListener(v->{
            Log.i("当前的项:","："+viewPager2.getCurrentItem());
            clickTime +=1;
            forget_list.add(rlist.get(viewPager2.getCurrentItem()).getId());//后端只需要每个单词的id。
            if (clickTime == WORDSIZE){
                Log.i("最后一个了","单词");
                showDialog();
//                sendList();
            }
            if (!viewPager2.isFakeDragging()) {
                new Thread(() -> {
                    viewPager2.beginFakeDrag();
                    for (int i = 1; i <= 40; i++) {
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        new Handler(Looper.getMainLooper()).post(() -> {
                            viewPager2.fakeDragBy(-(float) (viewPager2.getWidth()) / 40);
                        });
                    }
                    viewPager2.endFakeDrag();
                }).start();
            }
        });

        btnRight.setOnClickListener(v->{
            clickTime +=1;
            remember_list.add(rlist.get(viewPager2.getCurrentItem()).getId());//后端只需要每个单词的id。
            Log.i("记住的单词:",""+remember_list.size());
            if (clickTime == WORDSIZE){
                Log.i("最后一个了","单词");
                showDialog();
            }
            if (!viewPager2.isFakeDragging()) {
                new Thread(() -> {
                    viewPager2.beginFakeDrag();
                    for (int i = 1; i <= 40; i++) {
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        new Handler(Looper.getMainLooper()).post(() -> {
                            viewPager2.fakeDragBy(-(float) (viewPager2.getWidth()) / 40);
                        });
                    }
                    viewPager2.endFakeDrag();
                }).start();
            }
        });


        return binding.getRoot();
    }

    /**
     * 弹出对话框，返回主界面，同时将homeViewModel中的数据清空
     */
    public void showDialog(){
        new QMUIDialog.MessageDialogBuilder(getContext())
                .setTitle("提示")
                .setMessage("你已完成目标")
                .addAction("确定", (dialog, index) -> {
                    dialog.dismiss();
                    Navigation.findNavController(getView()).navigateUp();
                    homeViewModel.getList_word().getValue().clear();
                })
                .create(R.style.QMUI_Dialog).show();
    }

    /**
     * 发送认识与不认识的列表到后台服务器。。
     */
    public void sendList(){
        Log.i("请求发送中。。。。","..");
        if (forget_list.size()!=0){

        }
        if (remember_list.size()!=0){
            Call<Result<String>> call = localService.insertRember(remember_list);
            call.enqueue(new Callback<Result<String>>() {
                @Override
                public void onResponse(Call<Result<String>> call, Response<Result<String>> response) {
                        if (response.body().getCode() == 200){
                            Log.i(ERRCODE.REQUEST_SUCCESS.getMsgtype(), ERRCODE.REQUEST_SUCCESS.getMsg());
                        }
                }

                @Override
                public void onFailure(Call<Result<String>> call, Throwable t) {
                    Log.i(ERRCODE.REQUEST_FAILED.getMsgtype(), ERRCODE.REQUEST_FAILED.getMsg());
                }
            });
        }
    }
}