package com.wong.elapp.ui.home.second;

import android.animation.ValueAnimator;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;
import androidx.viewpager2.widget.ViewPager2;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import com.qmuiteam.qmui.layout.QMUIButton;
import com.qmuiteam.qmui.util.QMUIToastHelper;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.wong.elapp.R;
import com.wong.elapp.databinding.FragmentReciteBinding;
import com.wong.elapp.pojo.RandomList;
import com.wong.elapp.ui.home.HomeViewModel;
import com.wong.elapp.utils.DensityUtil;
import com.wong.elapp.utils.cardtransformer.AlphaAndScalePageTransformer;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;


@AndroidEntryPoint
public class ReciteFragment extends Fragment {

    @Inject
    DensityUtil densityUtil;


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
                forget_list.add(rlist.get(position).getId());
                if (position == WORDSIZE-1){//到达底部，发送请求，并且弹出提示框，返回主界面。。
                    QMUIToastHelper.show(Toast.makeText(getActivity(),"到底了~"+forget_list.size(),Toast.LENGTH_LONG));
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                super.onPageScrollStateChanged(state);
            }
        });

        //设置按钮监听器,点击就模拟滑动
        btnLeft.setOnClickListener(v -> {
            viewPager2.beginFakeDrag();
            viewPager2.fakeDragBy(-(DensityUtil.dip2px(getContext(),getResources().getDimension(R.dimen.fake_drag))));
            Log.i("ReciteFragment",""+viewPager2.getCurrentItem());
            viewPager2.endFakeDrag();
        });

        return binding.getRoot();
    }
}