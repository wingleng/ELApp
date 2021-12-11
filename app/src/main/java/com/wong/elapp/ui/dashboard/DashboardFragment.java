package com.wong.elapp.ui.dashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.qmuiteam.qmui.widget.tab.QMUITabSegment;
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
import com.wong.elapp.ui.dashboard.childrens.dash_firstFragment;
import com.wong.elapp.ui.dashboard.childrens.dash_secondFragment;
import com.wong.elapp.ui.home.HomeViewModel;
import com.wong.elapp.utils.ERRCODE;
import com.wong.elapp.utils.WebError;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Date;
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
    private ViewPager2 contentViewPager;
    private TabLayout tabLayout;


    private List<Fragment> fragments = new ArrayList<>();
    private List<String> titles = new ArrayList<>();
    private final int TEXT_TRANSLATE = 0;
    private final int PIC_TRANSLATE = 1;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        binding = FragmentDashboardBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        contentViewPager = binding.contentViewPager;
        tabLayout = binding.tabLayout;


        fragments.add(dash_firstFragment.newInstance("1","1"));
        fragments.add(dash_secondFragment.newInstance("2","2"));
        titles.add("文字翻译");
        titles.add("图片翻译");

        //这里用了ViewPager2，和ViewPager的区别就是，只需要传递进去Activity或者Fragment，它会自动生成FragmentManager
        contentViewPager.setAdapter(new MyFragmentAdapter(this));

        initTabLayou();

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    /**
     * 配置TabLayout
     */
    public void initTabLayou(){
        for (int i = 0; i < titles.size(); i++) {
            tabLayout.addTab(tabLayout.newTab().setText(titles.get(i)), false);
        }
        tabLayout.setSelectedTabIndicatorColor(getResources().getColor(R.color.app_color_blue));
        tabLayout.setTabIndicatorFullWidth(false);
        tabLayout.addOnTabSelectedListener(new TabLayouLis());
        new TabLayoutMediator(tabLayout, contentViewPager, new TabLayoutMediator.TabConfigurationStrategy() {
            @Override
            public void onConfigureTab(@NonNull TabLayout.Tab tab, int position) {
                tab.setText(titles.get(position));
            }
        }).attach();
    }

    /**
     * 一个ViewPager2适配器，不过这个是适配Fragment的。
     */
    class MyFragmentAdapter extends FragmentStateAdapter{
        public MyFragmentAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        public MyFragmentAdapter(@NonNull Fragment fragment) {
            super(fragment);
        }

        public MyFragmentAdapter(@NonNull FragmentManager fragmentManager, @NonNull Lifecycle lifecycle) {
            super(fragmentManager, lifecycle);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            switch (position){
                case TEXT_TRANSLATE:
                    return fragments.get(TEXT_TRANSLATE);
                case PIC_TRANSLATE:
                    return fragments.get(PIC_TRANSLATE);
                default:
                    return fragments.get(TEXT_TRANSLATE);//默认这个。。总感觉这个会有问题。
            }
        }

        @Override
        public int getItemCount() {
            return fragments.size();
        }
    }

    /**
     * tabLayout的监听器
     */
    class TabLayouLis implements TabLayout.OnTabSelectedListener{
        @Override
        public void onTabSelected(TabLayout.Tab tab) {
            //为选中的选项加点特效。
            TextView textView = new TextView(getActivity());
            float selectedSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_PX, 20, getResources().getDisplayMetrics());
            textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, selectedSize);
            textView.setTextColor(getResources().getColor(R.color.app_color_blue_pressed));
            textView.setText(tab.getText());
            textView.setGravity(Gravity.CENTER_HORIZONTAL);
            textView.setTypeface(Typeface.defaultFromStyle(Typeface.BOLD));
            tab.setCustomView(textView);
        }

        @Override
        public void onTabUnselected(TabLayout.Tab tab) {
            tab.setCustomView(null);
        }

        @Override
        public void onTabReselected(TabLayout.Tab tab) {

        }
    }


}