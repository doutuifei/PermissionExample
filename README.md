# easypermissions示例 带流程图

## 前言
阅读前说明：

* 所有系统均为Android原生系统，其他国产ROM最后再讨论。
* 所有的申请都为主动触发，即主动点击申请按钮。
* 申请时，默认是没有权限的。
* 流程图中长方形中为方法名。

## 申请单个权限流程图
![img](https://github.com/mzyq/PermissionExample/blob/a5ccdc5254de9940aacd646fb1af024058480180/doc/simple.jpg)

[PDF版](https://github.com/mzyq/PermissionExample/blob/a5ccdc5254de9940aacd646fb1af024058480180/doc/simple.pdf)

[visio版](https://github.com/mzyq/PermissionExample/blob/a5ccdc5254de9940aacd646fb1af024058480180/doc/simple.vsdx)

## 申请权限组流程图
![img](https://github.com/mzyq/PermissionExample/blob/a5ccdc5254de9940aacd646fb1af024058480180/doc/group.jpg)

[PDF版](https://github.com/mzyq/PermissionExample/blob/a5ccdc5254de9940aacd646fb1af024058480180/doc/group.pdf)

[visio版](https://github.com/mzyq/PermissionExample/blob/a5ccdc5254de9940aacd646fb1af024058480180/doc/group.vsdx)

## 申请混合权限（单个权限+权限组）流程图
> 具体流程=单个权限流程图+权限组流程图
代码中有例子，就不详细说明了。

## easypermissions使用

[easypermissions](https://github.com/googlesamples/easypermissions)

1. 依赖

```
dependencies {
    implementation 'pub.devrel:easypermissions:1.2.0'
}
```
2. 检查权限并申请权限

```
    public static final int PERMISSION_STORAGE_CODE = 10001;
    public static final String PERMISSION_STORAGE_MSG = "此app需要获取SD卡读取权限";
    public static final String[] PERMISSION_STORAGE = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE};

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

    @AfterPermissionGranted(PERMISSION_STORAGE_CODE)
     public void initSimple() {
          if (hasStoragePermission(context)) {
             //有权限
         } else {
             //申请权限
             EasyPermissions.requestPermissions(context, PERMISSION_STORAGE_MSG, PERMISSION_STORAGE_CODE, PERMISSION_STORAGE);
         }
     }

```
3. 实现```EasyPermissions.PermissionCallbacks```接口，直接处理权限是否成功申请

```
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    //成功
    @Override
    public void onPermissionsGranted(int requestCode, List<String> list) {
        // Some permissions have been granted
        // ...
    }

    //失败
    @Override
    public void onPermissionsDenied(int requestCode, List<String> list) {
        // Some permissions have been denied
        // ...
    }
```

## Dailog说明
1. RationaleDialog：申请权限的说明。
```
EasyPermissions.requestPermissions(context, PERMISSION_STORAGE_MSG, PERMISSION_STORAGE_CODE, PERMISSION_STORAGE);
```
或者
```
EasyPermissions.requestPermissions(
        new PermissionRequest.Builder(this, RC_CAMERA_AND_LOCATION, perms)
                .setRationale(R.string.camera_and_location_rationale)
                .setPositiveButtonText(R.string.rationale_ask_ok)
                .setNegativeButtonText(R.string.rationale_ask_cancel)
                .setTheme(R.style.my_fancy_style)
                .build());
```

方法回调实现```EasyPermissions.RationaleCallbacks```接口:
```
   /**
     * dialog提醒点击“确定”
     *
     * @param requestCode
     */
    @Override
    public void onRationaleAccepted(int requestCode) {
    }

    /**
     * dialog提醒点击“取消”
     *
     * @param requestCode
     */
    @Override
    public void onRationaleDenied(int requestCode) {
    }
```
![RationaleDialog](http://p7rrs468p.bkt.clouddn.com/Rationale%20Dialog.png)

详细使用参考：[easypermissions](https://github.com/googlesamples/easypermissions)

2. AppSettingsDialog:跳转setting页，申请被拒绝时使用。
```
 /**
     * 拒绝权限
     *
     * @param requestCode
     * @param perms
     */
    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this)
                    .setTitle("提醒")
                    .setRationale("此app需要这些权限才能正常使用")
                    .build()
                    .show();
        }
    }
```
方法回调
```
/**
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
                if (PermissionUtils.hasStoragePermission(context)){
                    //有权限
                }else{
                    //没有权限
                }
            }
        }
```

