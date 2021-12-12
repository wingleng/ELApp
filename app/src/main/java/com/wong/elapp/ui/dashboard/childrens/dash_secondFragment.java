package com.wong.elapp.ui.dashboard.childrens;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.ExplainReasonCallbackWithBeforeParam;
import com.permissionx.guolindev.callback.RequestCallback;
import com.permissionx.guolindev.request.ExplainScope;
import com.qmuiteam.qmui.util.QMUIToastHelper;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.wong.elapp.R;
import com.wong.elapp.databinding.FragmentDashSecondBinding;
import com.wong.elapp.hilt.types.YoudaoRetrofit;
import com.wong.elapp.network.mapper.YoudaoService;
import com.wong.elapp.pojo.vo.YoudaoresultPic2Text.YoudaoresultPic2Text;
import com.wong.elapp.ui.home.HomeViewModel;
import com.wong.elapp.ui.notifications.NotificationsFragment;
import com.wong.elapp.utils.BitMap2Base64;
import com.wong.elapp.utils.ERRCODE;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link dash_secondFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
public class dash_secondFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    //TODO:用户定义的变量
    HomeViewModel homeViewModel;
    FragmentDashSecondBinding binding;

    QMUIRoundButton dash2Camer;
    QMUIRoundButton dash2Blum;
    ImageView dash2Img;
    Context context;

    String FROM = "auto";//翻译要转换的类型。
    String TO = "auto";

    //有道翻译的服务
    @Inject
    YoudaoService youdaoService;

    //图片查询结果：
    YoudaoresultPic2Text youdaoResult;


    private final int BLUM_SELECT=100;
    private final int CAMER_CAPTURE=101;
    private Uri imageUri;
    private final String filePath = Environment.getExternalStorageDirectory() + File.separator + "output_image.jpg";//文件默认缓存路径


    public dash_secondFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment dash_secondFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static dash_secondFragment newInstance(String param1, String param2) {
        dash_secondFragment fragment = new dash_secondFragment();
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
        homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        binding = FragmentDashSecondBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        context = root.getContext();
        //初始化组件
        dash2Camer = binding.dash2Camer;
        dash2Blum = binding.dash2Blum;
        dash2Img = binding.dash2Img;

        dash2Camer.setOnClickListener(new dash_secondFragment.CamerBtnListener());
        dash2Blum.setOnClickListener(new dash_secondFragment.BlumBtnListeber());


        return root;
    }

    /**
     * 选择图片或者拍照之后的回调
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BLUM_SELECT && resultCode == RESULT_OK){
            Glide.with(context).load(data.getData()).into(dash2Img);
        }


        if (requestCode == CAMER_CAPTURE && resultCode == RESULT_OK){
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));
                dash2Img.setImageBitmap(bitmap);//这步其实可以去掉，展示原始图片感觉没有必要
                String base64 = BitMap2Base64.bitmaptoString(bitmap,100);
                sendPic2TextQuery(base64,FROM,TO);

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 权限申请
     */
    private void getPermission(){
        PermissionX.init(dash_secondFragment.this)
                .permissions(Manifest.permission.CAMERA,Manifest.permission.ACCESS_NETWORK_STATE,Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .onExplainRequestReason(new ExplainReasonCallbackWithBeforeParam() {
                    @Override
                    public void onExplainReason(@NonNull ExplainScope scope, @NonNull List<String> deniedList, boolean beforeRequest) {
                        scope.showRequestReasonDialog(deniedList,"相机权限是必须的","明白","取消");
                    }
                })
                .request(new RequestCallback() {
                    @Override
                    public void onResult(boolean allGranted, @NonNull List<String> grantedList, @NonNull List<String> deniedList) {
                        if(allGranted){
                            Toast.makeText(context,"所有权限通过",Toast.LENGTH_LONG).show();
                        }else{
                            Toast.makeText(context,"您拒绝了如下权限："+deniedList,Toast.LENGTH_LONG).show();
                        }
                    }
                });
    }

    /**
     * 将获取到的图片转化到base64之后，发送到翻译服务器当中。
     * 然后将返回的图片再进行处理。
     */
    public void sendPic2TextQuery(String base64,String from,String to){
        String APP_KEY = getResources().getString(R.string.app_key);
        String APP_SECRET = getResources().getString(R.string.app_secret);

        Map<String,String> params = new HashMap<String,String>();
        String salt = String.valueOf(System.currentTimeMillis());
        String type = "1";//设置发送类型为base64
        String signStr = APP_KEY +base64 + salt + APP_SECRET;
        String sign = getDigest(signStr);
        params.put("from",from);
        params.put("to",to);
        params.put("type",type);
        params.put("q",base64);
        params.put("appKey",APP_KEY);
        params.put("salt",salt);
        params.put("sign",sign);

        Call<YoudaoresultPic2Text> call = youdaoService.pic2text(params);
        call.enqueue(new Callback<YoudaoresultPic2Text>() {
            @Override
            public void onResponse(Call<YoudaoresultPic2Text> call, Response<YoudaoresultPic2Text> response) {
                if(response.body()==null){
                    QMUIToastHelper.show(Toast.makeText(getActivity(),"出现异常，服务器返回的数据为空",Toast.LENGTH_LONG));
                }else {
                    Log.i("有道请求成功","内容"+response.body().getResRegions().toString());
                    youdaoResult = response.body();
                }
            }

            @Override
            public void onFailure(Call<YoudaoresultPic2Text> call, Throwable t) {
                    Log.i(ERRCODE.REQUEST_FAILED.getMsgtype(), ERRCODE.REQUEST_FAILED.getMsg());
                    QMUIToastHelper.show(Toast.makeText(getActivity(),"请求发送失败",Toast.LENGTH_LONG));
            }
        });
    }

    /**
     * 生成md5加密字段
     */
    public static String getDigest(String string) {
        if (string == null) {
            return null;
        }
        char hexDigits[] = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
        byte[] btInput = string.getBytes(StandardCharsets.UTF_8);
        try {
            MessageDigest mdInst = MessageDigest.getInstance("MD5");
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

    /**
     * 使用相机进行拍摄
     */
    class CamerBtnListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            //首先申请相机权限：
            getPermission();

            File outputImage = new File(filePath);

            //判断图片文件是否存在
            try {
                if(!outputImage.getParentFile().exists()){
                    outputImage.getParentFile().mkdir();
                }
                if(outputImage.exists()){
                    outputImage.delete();
                }
                outputImage.createNewFile();

                //获取Uri(必须是在API24才能使用这种方法，其他情况下直接获取就好了。
                imageUri = FileProvider.getUriForFile(context,"com.wong.elapp.fileprovider",outputImage);

                //Intent打开相机
                Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);

                startActivityForResult(intent,CAMER_CAPTURE);
            }catch (IOException e){
                e.printStackTrace();
            }

        }
    }


    /**
     * 从相册中选择图片
     */
    class BlumBtnListeber implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent gallery = new Intent(Intent.ACTION_PICK);
            gallery.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(gallery, BLUM_SELECT);
        }
    }



}