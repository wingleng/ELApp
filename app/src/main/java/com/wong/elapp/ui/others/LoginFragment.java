package com.wong.elapp.ui.others;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.qmuiteam.qmui.util.QMUIToastHelper;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.wong.elapp.R;
import com.wong.elapp.databinding.FragmentLoginBinding;
import com.wong.elapp.network.TokenIncepter;
import com.wong.elapp.network.mapper.LocalService;
import com.wong.elapp.pojo.vo.LoginParam;
import com.wong.elapp.pojo.vo.Result;
import com.wong.elapp.ui.home.HomeViewModel;
import com.wong.elapp.utils.ERRCODE;
import com.wong.elapp.utils.WebError;

import java.util.Date;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * 一个很简单的界面，只是用来登录。
 */
@AndroidEntryPoint
public class LoginFragment extends Fragment {
    @Inject
    LocalService localService;

    HomeViewModel homeViewModel;
    FragmentLoginBinding binding;
    MutableLiveData<Boolean> wasLogined;

    QMUIRoundButton loginButton;
    QMUIRoundButton registerBtn;

    EditText editAccount;
    EditText editPassword;


    public LoginFragment() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        binding = FragmentLoginBinding.inflate(inflater, container, false);
        wasLogined = homeViewModel.getWasLogined();
        loginButton = binding.loginButton;
        registerBtn = binding.registerBtn;
        editAccount = binding.editAccount;
        editPassword = binding.editPassword;

        loginButton.setOnClickListener(v->{
            {
                Call<Result<String>> call = localService.login(new LoginParam(editAccount.getText().toString(), editPassword.getText().toString()));
                call.enqueue(new Callback<Result<String>>() {
                    @Override
                    public void onResponse(Call<Result<String>> call, Response<Result<String>> response) {

                        //根据后端返回的代码，进行操作。
                        Result res = response.body();
                        if (res == null){
                            QMUIToastHelper.show(Toast.makeText(getActivity(),"服务器返回数据为空",Toast.LENGTH_LONG));
                            return ;
                        }
                        if (res.getCode() == WebError.ACCOUNT_PWD_NOT_EXIST.getCode()){
                            Log.i("登录:","用户名或者密码不正确");
                            QMUIToastHelper.show(Toast.makeText(getActivity(),"用户名或者密码不正确",Toast.LENGTH_LONG));
                        }else if (res.getCode() == 200){
                            Log.i("登录：",res.getMsg());
                            Log.i("登录：",res.getData()+"");

                            TokenIncepter.TOKEN = (String) res.getData();//设置拦截器的token，以后每次发送请求的时候回带上token

                            //将服务器返回的token存起来，可以用来避免多次登录。
                            SharedPreferences sp = getContext().getSharedPreferences("config", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sp.edit();
                            editor.putString("TOKEN",(String)res.getData());
                            editor.putLong("setTime",new Date().getTime());
                            editor.commit();

                            //一切操作完成之后，使用Navigation退回到上一级的界面。
                            QMUIToastHelper.show(Toast.makeText(getActivity(),"登录成功，返回主界面",Toast.LENGTH_LONG));
                            Navigation.findNavController(getView()).navigateUp();
                        }else if (res.getCode() == WebError.NO_LOGIN.getCode()){
                            Log.i("没有登录：","没有登录，无法访问这些资源");
                        }
                    }

                    @Override
                    public void onFailure(Call<Result<String>> call, Throwable t) {
                        Log.i(ERRCODE.REQUEST_FAILED.getMsgtype(), ERRCODE.REQUEST_FAILED.getMsg());
                        QMUIToastHelper.show(Toast.makeText(getActivity(),"请求发送失败",Toast.LENGTH_LONG));
                    }
                });
            }
        });



        registerBtn.setOnClickListener(v->{
            Call<Result> call = localService.registe(new LoginParam(editAccount.getText().toString(), editPassword.getText().toString()));
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
        });

        return binding.getRoot();
    }
}