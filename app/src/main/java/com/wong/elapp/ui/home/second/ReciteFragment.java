package com.wong.elapp.ui.home.second;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.wong.elapp.databinding.FragmentReciteBinding;
import com.wong.elapp.pojo.RandomList;
import com.wong.elapp.ui.home.HomeViewModel;

import java.util.List;


public class ReciteFragment extends Fragment {



    FragmentReciteBinding binding;
    HomeViewModel homeViewModel;
    ViewPager2 viewPager2;


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

        //获取数据源
        List<RandomList> rlist = homeViewModel.getList_word().getValue();
        viewPager2.setAdapter(new ViewPagerAdapter(rlist));

        return binding.getRoot();
    }
}