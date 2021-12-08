package com.wong.elapp.ui.notifications;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
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
import java.util.List;

public class NotificationsFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private FragmentNotificationsBinding binding;
    private Context context;
    private TextView textView;

    private Button cameBtn;

    private Button blumBtn;

    private ImageView imageView;

    private final int BLUM_SELECT=100;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
         homeViewModel = new ViewModelProvider(requireActivity()).get(HomeViewModel.class);
        binding = FragmentNotificationsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        context = root.getContext();

        textView = binding.textNotifications;
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
    }

    class CamerBtnListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            //首先申请相机权限：
            PermissionX.init(NotificationsFragment.this)
                    .permissions(Manifest.permission.CAMERA)
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