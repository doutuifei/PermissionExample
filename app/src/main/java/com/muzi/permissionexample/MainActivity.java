package com.muzi.permissionexample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks,
        EasyPermissions.RationaleCallbacks {

    private MainActivity context = MainActivity.this;
    private Button btnsd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnsd = findViewById(R.id.btn_sd);
        btnsd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initPermission();
            }
        });
    }

    private void initPermission() {
        initStorage();
    }

    /**
     * 申请SD卡权限
     */
    @AfterPermissionGranted(PermissionUtils.PERMISSION_STORAGE_CODE)
    public void initStorage() {
        if (PermissionUtils.hasStoragePermission(context)) {
            //有权限
        } else {
            //申请权限
            EasyPermissions.requestPermissions(context, PermissionUtils.PERMISSION_STORAGE_MSG, PermissionUtils.PERMISSION_STORAGE_CODE, PermissionUtils.PERMISSION_STORAGE);
        }
    }

    /**
     * 同意权限
     *
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {
        Log.d("PermissionLog", "onPermissionsGranted:" + requestCode);
        for (String perm : perms) {
            Log.d("PermissionLog", "onPermissionsGranted:" + perm);
        }
    }

    /**
     * 拒绝权限
     *
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        Log.d("PermissionLog", "onPermissionsDenied:" + requestCode);
        for (String perm : perms) {
            Log.d("PermissionLog", "onPermissionsDenied:" + perm);
        }
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this)
                    .setTitle("提醒")
                    .setRationale("此app需要这些权限才能正常使用")
                    .build()
                    .show();
        }
    }

    /**
     * dialog提醒点击“确定”
     *
     * @param requestCode
     */
    @Override
    public void onRationaleAccepted(int requestCode) {
        Log.d("PermissionLog", "onRationaleAccepted:" + requestCode);
    }

    /**
     * dialog提醒点击“取消”
     *
     * @param requestCode
     */
    @Override
    public void onRationaleDenied(int requestCode) {
        Log.d("PermissionLog", "onRationaleDenied:" + requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    /**
     * 设置页面回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            Log.d("PermissionLog", "onActivityResult:" + PermissionUtils.hasStoragePermission(context));
        }
    }

}
