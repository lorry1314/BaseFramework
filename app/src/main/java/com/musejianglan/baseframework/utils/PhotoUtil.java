package com.musejianglan.baseframework.utils;

/**
 * Created by liulei on 2015/9/26.
 */

public class PhotoUtil {
    //public static final String TAG = "PhotoUtil";
    //
    //
    //
    //// 使用系统当前日期加以调整作为照片的名称
    //private static String getPhotoFileName() {
    //    Date date = new Date(System.currentTimeMillis());
    //    SimpleDateFormat dateFormat = new SimpleDateFormat(
    //            "'IMG'_yyyyMMdd_HHmmss");
    //    return dateFormat.format(date) + ".jpg";
    //}
    //
    ///**
    // * @param activity
    // * 调用系统拍照
    // */
    //public static File startCamearPicCut(Activity activity) {
    //    // TODO Auto-generated method stub
    //    if (!SdCardUtil.isSdCardAvailable()) {
    //        ToastUtil.showNormalShortToast(activity,"SD卡不可用");
    //        return null;
    //    }
    //    File file = new File(Environment.getExternalStorageDirectory()
    //            + File.separator + "baidaitravel", getPhotoFileName());
    //    file.getParentFile().mkdirs();
    //
    //    // 调用系统的拍照功能
    //    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
    //
    //    intent.putExtra("camerasensortype", 1);// 调用后置摄像头
    //    //intent.putExtra("camerasensortype", 2);// 调用前置摄像头
    //    intent.putExtra("autofocus", true);// 自动对焦
    //    intent.putExtra("fullScreen", false);// 全屏
    //    intent.putExtra("showActionIcons", false);
    //    // 指定调用相机拍照后照片的储存路径
    //    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(file));
    //    activity.startActivityForResult(intent, PHOTO_REQUEST_TAKEPHOTO);
    //    return file;
    //}
    //
    ///**
    // * @param activity
    // * 调用系统图库
    // */
    //public static void startImageCaptrue(Activity activity) {
    //    // TODO Auto-generated method stub
    //    //Intent intent = new Intent(Intent.ACTION_PICK, null);
    //    //intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
    //
    //    Intent intent;
    //    if (Build.VERSION.SDK_INT < 19) {
    //        intent = new Intent(Intent.ACTION_GET_CONTENT);
    //        intent.setType("image/*");
    //
    //    } else {
    //        intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
    //    }
    //
    //
    //    activity.startActivityForResult(intent, PHOTO_REQUEST_GALLERY);
    //}
    //
    //
    ///**
    // * 图片裁剪
    // * @param activity
    // * @param uri
    // * @param size 大小 150
    // */
    //public static void startPhotoZoom(Activity activity,Uri uri, int size) {
    //    Intent intent = new Intent("com.android.camera.action.CROP");
    //    intent.setDataAndType(uri, "image/*");
    //    // crop为true是设置在开启的intent中设置显示的view可以剪裁
    //    intent.putExtra("crop", "true");
    //
    //    // aspectX aspectY 是宽高的比例
    //    intent.putExtra("aspectX", 1);
    //    intent.putExtra("aspectY", 1);
    //
    //    // outputX,outputY 是剪裁图片的宽高
    //    intent.putExtra("outputX", size);
    //    intent.putExtra("outputY", size);
    //    intent.putExtra("return-data", true);
    //
    //    activity.startActivityForResult(intent, PHOTO_REQUEST_CUT);
    //}
}
