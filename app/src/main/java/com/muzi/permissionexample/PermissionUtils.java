package com.muzi.permissionexample;

import android.Manifest;
import android.content.Context;

import pub.devrel.easypermissions.EasyPermissions;

/**
 * 作者: lipeng
 * 时间: 2018/5/23
 * 邮箱: lipeng@moyi365.com
 * 功能:
 */
public class PermissionUtils {

    //获取SD卡
    public static final int PERMISSION_STORAGE_CODE = 10002;
    public static final String PERMISSION_STORAGE_MSG = "此app需要获取SD卡读取权限";
    public static final String[] PERMISSION_STORAGE = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};


    public static boolean hasPermissions(Context context, String... permissions) {
        return EasyPermissions.hasPermissions(context, permissions);
    }


    /**
     * 是否有SD卡权限
     *
     * @param context
     * @return
     */
    public static boolean hasStoragePermission(Context context) {
        return hasPermissions(context, PERMISSION_STORAGE);
    }

}
