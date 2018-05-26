# EasyPermissions Android权限适配（带流程图）

## 前言
阅读前说明：

* 所有系统均为Android原生系统，其他国产ROM最后再讨论。
* 所有的申请都为主动触发，即主动点击申请按钮。
* 申请时，默认是没有权限的。
* 流程图中长方形中为方法名。

## 流程图
### 申请单个权限流程图

![img](https://github.com/mzyq/PermissionExample/blob/a5ccdc5254de9940aacd646fb1af024058480180/doc/simple.jpg)

[PDF版](https://github.com/mzyq/PermissionExample/blob/a5ccdc5254de9940aacd646fb1af024058480180/doc/simple.pdf)

[visio版](https://github.com/mzyq/PermissionExample/blob/a5ccdc5254de9940aacd646fb1af024058480180/doc/simple.vsdx)

### 申请权限组流程图
![img](https://github.com/mzyq/PermissionExample/blob/a5ccdc5254de9940aacd646fb1af024058480180/doc/group.jpg)

[PDF版](https://github.com/mzyq/PermissionExample/blob/a5ccdc5254de9940aacd646fb1af024058480180/doc/group.pdf)

[visio版](https://github.com/mzyq/PermissionExample/blob/a5ccdc5254de9940aacd646fb1af024058480180/doc/group.vsdx)

### 申请混合权限（单个权限+权限组）流程图
> 具体流程=单个权限流程图+权限组流程图
代码中有例子，就不详细说明了。

## EasyPermissions使用

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

![RationaleDialog](http://p7rrs468p.bkt.clouddn.com/Rationale%20Dialog.png)

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

详细使用参考：[easypermissions](https://github.com/googlesamples/easypermissions)

2. AppSettingsDialog:跳转setting页，申请被拒绝时使用。

![AppSettingsDialog](http://p7rrs468p.bkt.clouddn.com/AppSettingsDialog.png)

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

## 国产ROM适配
国产ROM问题一直是最多的，申请权限的弹窗不按Android原生来，有时候允许权限了，回调却是走失败。
如果想适配国产ROM，我推荐 [AndPermission](https://github.com/yanzhenjie/AndPermission)

### AndPermission概述
AndPermission可以简化在Android上请求权限的流程。

* 在Android6.0或者更高版本的系统中请求运行时权限。
* 在Android7.0或者更高版本的系统中分享私有文件。
* 在Android8.0或者更高版本的系统中安装Apk文件。

### 请求流程
为了兼容一些特殊中国产手机，AndPermission请求权限的流程是这样的：
```
                      发起请求
                         │
              调用SDK Api判断是否有权限
                         │
           ┌─────────────┴─────────────┐
         有权限                      没权限
           │                           │
           │                  使用SDK Api请求权限
           │                           │
           │                    用户授权或者拒绝
           └─────────────┬─────────────┘
                  执行权限相关代码
                 ┌───────┴───────┐
                正常            异常
                 │               │
             onGranted()      onDenied()
          ┌──────┴──────┐        │
         正常          异常───────┘
```
### 申请流程的释义
可以看出AndPermission的申请流程和标准的申请流程大相径庭，这样做主要是因为以下几点原因。

* 兼容Android5.0的系统判断是否有权限。
* 部分设备上使用SDK的Api判断是否有权限时，无论是否有权限都返回true。
* 部分设备上无论用户点击同意还是拒绝都返回true。
* 部分设备在申请权限时并不会弹出授权Dialog，而是在执行权限相关代码时才会弹出授权Dialog。

##### 特特特别注意：
 因为部分权限（例如发送短信、打电话、接听/挂断电话等），AndPermission不能执行权限相关代码做测试，所以对于这一类权限在执行权限相关代码步骤仅仅使用了AppOpsManager做检测。因为极少部分国产机总是返回true，因此直接回调到onGranted()中让开发者执行相关代码来辅助AndPermission的整个流程，如果onGranted()方法发生异常，那么则认为是没有权限，所以重新回调到onDenied()方法中，如果onGranted()方法正常执行那么则认为有权限。这样一来刚好是一个完整的流程，也可以最大限度的兼容到更多异常的手机。

回调onGranted()时AndPermission对onGranted()方法做了try catch。这样做可以保证两点，第一点是防止部分手机返回错误状态时回调了onGranted()后的崩溃，第二点是防止部分手机需要真正执行权限相关代码时触发授权Dialog后被用户拒绝后的崩溃。而这两点都是没有权限，因此会重新回调到onDenied()中让开发者处理没权限的情况。

#### 综上所述，如果onGranted()和onDenied()方法都被调用了，说明开发者的onGranted()方法发生了异常。

[AndPermission详细说明文档](http://www.yanzhenjie.com/AndPermission/cn/)