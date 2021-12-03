package com.wong.elapp.ui.home.second;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wong.elapp.R;
import com.wong.elapp.databinding.FragmentHomeBinding;
import com.wong.elapp.databinding.FragmentReciteBinding;
import com.wong.elapp.ui.home.HomeViewModel;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ReciteFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ReciteFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // TODO: 以下不是默认生成的变量：
    FragmentReciteBinding binding;
    HomeViewModel homeViewModel;


    public ReciteFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ReciteFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ReciteFragment newInstance(String param1, String param2){
        ReciteFragment fragment = new ReciteFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentReciteBinding.inflate(inflater,container,false);
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        return binding.getRoot();
    }
}