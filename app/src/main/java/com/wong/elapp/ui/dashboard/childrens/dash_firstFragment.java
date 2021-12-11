package com.wong.elapp.ui.dashboard.childrens;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.qmuiteam.qmui.util.QMUIToastHelper;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.wong.elapp.R;
import com.wong.elapp.databinding.FragmentDashFirstBinding;
import com.wong.elapp.databinding.FragmentDashboardBinding;
import com.wong.elapp.network.mapper.YoudaoService;
import com.wong.elapp.pojo.vo.Youdaoresult.YoudaoResult;
import com.wong.elapp.ui.home.HomeViewModel;
import com.wong.elapp.utils.ERRCODE;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link dash_firstFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
public class dash_firstFragment extends Fragment {

    @Inject
    YoudaoService youdaoService;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //TODO:这里开始才是用户自定义的数据
    HomeViewModel homeViewModel;
    FragmentDashFirstBinding binding;
    EditText dash1Edit;
    QMUIRoundButton dash1Send;

    String from="auto" ;//设置语言转化类型。
    String to="auto";

    YoudaoResult youdaoResult;

    public dash_firstFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment dash_firstFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static dash_firstFragment newInstance(String param1, String param2) {
        dash_firstFragment fragment = new dash_firstFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //默认创建的两个参数，说实话，我并不知道这两个参数到底有什么用。。。
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        binding = FragmentDashFirstBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        //初始化界面组件：
        dash1Edit = binding.dash1Edit;
        dash1Send = binding.dash1Send;

        dash1Send.setOnClickListener(v->{
            String querycontent = dash1Edit.getText().toString();
            sendQuery(querycontent);
        });
        // Inflate the layout for this fragment
        return root;
    }

    /**
     * 发送请求
     * @param query
     */
    public void sendQuery(String query){

        String APP_KEY = getResources().getString(R.string.app_key);
        String APP_SECRET = getResources().getString(R.string.app_secret);

        Map<String,String> params = new HashMap<String,String>();
        String q = query;
        String salt = String.valueOf(System.currentTimeMillis());
        params.put("from", from);
        params.put("to", to);
        params.put("signType", "v3");
        String curtime = String.valueOf(System.currentTimeMillis() / 1000);
        params.put("curtime", curtime);
        String signStr = APP_KEY + truncate(q) + salt + curtime + APP_SECRET;
        String sign = getDigest(signStr);
        params.put("appKey", APP_KEY);
        params.put("q", q);
        params.put("salt", salt);
        params.put("sign", sign);
        Log.i("发送请求","发送请求");

        Call<YoudaoResult> call = youdaoService.textTranslate(params);
        call.enqueue(new Callback<YoudaoResult>() {
            @Override
            public void onResponse(Call<YoudaoResult> call, Response<YoudaoResult> response) {
                Log.i("返回的数据：",response.body()==null?"数据为空":response.body().getBasic().getExplains().get(0));
                if (response.body()==null){
                    QMUIToastHelper.show(Toast.makeText(getActivity(),"出现异常，服务器返回数据为空",Toast.LENGTH_LONG));
                }else{
                    youdaoResult = response.body();//总感觉这个反序列化很容易出问题。
                }
            }

            @Override
            public void onFailure(Call<YoudaoResult> call, Throwable t) {
                Log.i(ERRCODE.REQUEST_FAILED.getMsgtype(), ERRCODE.REQUEST_FAILED.getMsg());
            }
        });
    }


    /**
     * 关键词操作，超过20个字符时需要操作。。
     * @param q
     * @return
     */
    public static String truncate(String q) {
        if (q == null) {
            return null;
        }
        int len = q.length();
        String result;
        return len <= 20 ? q : (q.substring(0, 10) + len + q.substring(len - 10, len));
    }

    /**
     * 生成加密字段
     */
    public static String getDigest(String string) {
        if (string == null) {
            return null;
        }
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        byte[] btInput = string.getBytes(StandardCharsets.UTF_8);
        try {
            MessageDigest mdInst = MessageDigest.getInstance("SHA-256");
            mdInst.update(btInput);
            byte[] md = mdInst.digest();
            int j = md.length;
            char str[] = new char[j * 2];
            int k = 0;
            for (byte byte0 : md) {
                str[k++] = hexDigits[byte0 >>> 4 & 0xf];
                str[k++] = hexDigits[byte0 & 0xf];
            }
            return new String(str);
        } catch (NoSuchAlgorithmException e) {
            return null;
        }
    }
}