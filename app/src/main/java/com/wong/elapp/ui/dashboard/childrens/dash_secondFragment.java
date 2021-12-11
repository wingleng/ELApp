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
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;
import com.wong.elapp.R;
import com.wong.elapp.databinding.FragmentDashSecondBinding;
import com.wong.elapp.ui.home.HomeViewModel;
import com.wong.elapp.ui.notifications.NotificationsFragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link dash_secondFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
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
     * 设置回调
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BLUM_SELECT){
            Glide.with(context).load(data.getData()).into(dash2Img);
        }


        if (requestCode == CAMER_CAPTURE && resultCode == RESULT_OK){
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));
                dash2Img.setImageBitmap(bitmap);
                //将图片解析成Bitmap对象，并把它显现出来
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