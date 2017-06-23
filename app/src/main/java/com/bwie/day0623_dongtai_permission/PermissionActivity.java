package com.bwie.day0623_dongtai_permission;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.widget.Button;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class PermissionActivity extends Activity {

    @BindView(R.id.permission1)
    Button permission1;
    public static String CAMERA = Manifest.permission.CAMERA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        ButterKnife.bind(this);
    }

    @OnClick(R.id.permission1)
    public void onClick() {

        PermissionActivityPermissionsDispatcher.cameraWithCheck(this);

    }

    //权限拒绝
    @OnPermissionDenied(Manifest.permission.CAMERA)
    public void onDenied(){
        Toast.makeText(this, "OnPermissionDenied", Toast.LENGTH_SHORT).show();

    }

    //选择了不再询问会调用
    @OnNeverAskAgain(Manifest.permission.CAMERA)
    public void onNeverAskAgain(){

        Toast.makeText(this, "onNeverAskAgain", Toast.LENGTH_SHORT).show();

        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.settings.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.Applicat*ionPkgName", getPackageName());
        }
        startActivity(localIntent);

    }

    @OnShowRationale(Manifest.permission.CAMERA)
    public void showRationaleForCamera(final PermissionRequest request){


        new AlertDialog.Builder(this).setTitle("title")
                .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        // 请求授权
                        request.proceed();

                    }
                }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                request.cancel();
            }
        }).create().show();

    }

    @NeedsPermission(Manifest.permission.CAMERA)
    public void camera() {
        //到相机
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        startActivityForResult(intent, 1);
    }
}
