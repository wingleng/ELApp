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


import com.qmuiteam.qmui.layout.QMUIButton;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.wong.elapp.R;
import com.wong.elapp.databinding.FragmentReciteBinding;
import com.wong.elapp.pojo.RandomList;
import com.wong.elapp.ui.home.HomeViewModel;
import com.wong.elapp.utils.DensityUtil;
import com.wong.elapp.utils.cardtransformer.AlphaAndScalePageTransformer;

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

        //对viewpage的设置
        List<RandomList> rlist = homeViewModel.getList_word().getValue();//从viewmodel中获取到数据
        viewPager2.setAdapter(new ViewPagerAdapter(rlist));//设置适配器
        //设置Transformer的动画效果
        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        int margin_px = DensityUtil.dip2px(getContext(),getResources().getDimension(R.dimen.activity_horizontal_margin));
        MarginPageTransformer marginPageTransformer = new MarginPageTransformer(margin_px);//设置item之间的间距
        compositePageTransformer.addTransformer(marginPageTransformer);
        compositePageTransformer.addTransformer(new AlphaAndScalePageTransformer());//设置左右滑动的效果
        viewPager2.setPageTransformer(compositePageTransformer);
        viewPager2.setOffscreenPageLimit(3);

        //配置页面滑动以及监听器
//        viewPager2.setUserInputEnabled(false);//禁止用户进行滑动

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