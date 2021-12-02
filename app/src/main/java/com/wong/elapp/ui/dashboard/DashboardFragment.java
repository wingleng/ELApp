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
        homeViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        btn.setOnClickListener((View v)->{
           Call<List<RandomList>> call = localService.getRandomWords();
           call.enqueue(new Callback<List<RandomList>>() {
               @Override
               public void onResponse(Call<List<RandomList>> call, Response<List<RandomList>> response) {
                   Log.i(ERRCODE.REQUEST_SUCCESS.getMsgtype(),ERRCODE.REQUEST_SUCCESS.getMsg());
               }

               @Override
               public void onFailure(Call<List<RandomList>> call, Throwable t) {
                   Log.i(ERRCODE.REQUEST_FAILED.getMsgtype(), ERRCODE.REQUEST_FAILED.getMsg());
               }
           });

//            Call<String> test = localService.getTest();
//            test.enqueue(new Callback<String>() {
//                @Override
//                public void onResponse(Call<String> call, Response<String> response) {
//                    Log.i(ERRCODE.REQUEST_SUCCESS.getMsgtype(), ERRCODE.REQUEST_SUCCESS.getMsg());
//                }
//
//                @Override
//                public void onFailure(Call<String> call, Throwable t) {
//                    Log.i(ERRCODE.REQUEST_FAILED.getMsgtype(), ERRCODE.REQUEST_FAILED.getMsg());
//                }
//            });
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}