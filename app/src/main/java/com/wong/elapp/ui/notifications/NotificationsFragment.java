package com.wong.elapp.ui.notifications;

import static android.app.Activity.RESULT_OK;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.bumptech.glide.Glide;
import com.permissionx.guolindev.PermissionX;
import com.permissionx.guolindev.callback.ExplainReasonCallbackWithBeforeParam;
import com.permissionx.guolindev.callback.RequestCallback;
import com.permissionx.guolindev.request.ExplainScope;
import com.wong.elapp.MainActivity;
import com.wong.elapp.R;
import com.wong.elapp.databinding.FragmentNotificationsBinding;
import com.wong.elapp.ui.home.HomeViewModel;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class NotificationsFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentNotificationsBinding binding;
    private Context context;

    private Button cameBtn;
    private Button blumBtn;
    private ImageView imageView;

    private final int BLUM_SELECT=100;
    private final int CAMER_CAPTURE=101;
    private Uri imageUri;
    private final String filePath = Environment.getExternalStorageDirectory() + File.separator + "output_image.jpg";


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
         homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        context = root.getContext();


        cameBtn = binding.takepic;
        blumBtn = binding.blum;
        imageView = binding.imageView;

        cameBtn.setOnClickListener(new CamerBtnListener());
        blumBtn.setOnClickListener(new BlumBtnListeber());

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == BLUM_SELECT){
            Glide.with(context).load(data.getData()).into(imageView);
        }
        if (requestCode == CAMER_CAPTURE && resultCode == RESULT_OK){
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(getActivity().getContentResolver().openInputStream(imageUri));
                imageView.setImageBitmap(bitmap);
                //将图片解析成Bitmap对象，并把它显现出来
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    private void getPermission(){
        PermissionX.init(NotificationsFragment.this)
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

    class BlumBtnListeber implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent gallery = new Intent(Intent.ACTION_PICK);
            gallery.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
            startActivityForResult(gallery, BLUM_SELECT);
        }
    }
}