package com.example.uidemo.mark.backgroun;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.FileNotFoundException;

import static com.wildma.pictureselector.PictureSelectUtils.createImagePathUri;

public class PictureSelectUtils {

    public static final int GET_BY_ALBUM = 0x11;//相册标记
    public static final int GET_BY_CAMERA = 0x12;//拍照标记
    public static final int CROP = 0x13;//裁剪标记
    private static Uri takePictureUri;//拍照图片uri
    public static Uri cropPictureTempUri;//裁剪图片uri

    /**
     * 通过相册获取图片
     *
     * @param activity
     */
    public static void getByAlbum(Activity activity) {
        Intent intent = new Intent(Intent.ACTION_PICK,
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        activity.startActivityForResult(intent, GET_BY_ALBUM);
    }

    /**
     * 通过拍照获取图片
     *
     * @param activity
     */
    public static void getByCamera(Activity activity) {
        takePictureUri = createImagePathUri(activity);
        if (takePictureUri != null) {
            Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            i.putExtra(MediaStore.EXTRA_OUTPUT, takePictureUri);//输出路径（拍照后的保存路径）
            activity.startActivityForResult(i, GET_BY_CAMERA);
        } else {
            Toast.makeText(activity, "无法保存到相册", Toast.LENGTH_LONG).show();
        }
    }

    /**
     * 创建一个图片地址uri,用于保存拍照后的照片
     *
     * @param activity
     * @return 图片的uri
     */
//    public static Uri createImagePathUri(Activity activity) {
//
//        try {
//            FileUtils.createOrExistsDir(Constant.DIR_ROOT);
//            StringBuffer buffer = new StringBuffer();
//            String pathName = buffer.append(Constant.DIR_ROOT).append(Constant.APP_NAME).append(".").append(System.currentTimeMillis()).append(".jpg").toString();
//            File file = new File(pathName);
//
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) { //解决Android 7.0 拍照出现FileUriExposedException的问题
//                String authority = activity.getPackageName() + ".fileProvider";
//                takePictureUri = FileProvider.getUriForFile(activity, authority, file);
//            } else {
//                takePictureUri = Uri.fromFile(file);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            Toast.makeText(activity, "无法保存到相册", Toast.LENGTH_LONG).show();
//        }
//        return takePictureUri;
//    }

    /**
     * 处理拍照或相册获取的图片，默认大小480*480，比例1:1
     *
     * @param activity    上下文
     * @param requestCode 请求码
     * @param resultCode  结果码
     * @param data        Intent
     * @return
     */
    public static Bitmap onActivityResult(Activity activity, int requestCode, int resultCode, Intent data) {
        return onActivityResult(activity, requestCode, resultCode, data, 0, 0, 0, 0);
    }

    /**
     * 处理拍照或相册获取的图片
     *
     * @param activity    上下文
     * @param requestCode 请求码
     * @param resultCode  结果码
     * @param data        Intent
     * @param w           输出宽
     * @param h           输出高
     * @param aspectX     宽比例
     * @param aspectY     高比例
     * @return
     */
    public static Bitmap onActivityResult(Activity activity, int requestCode, int resultCode, Intent data,
                                          int w, int h, int aspectX, int aspectY) {
        Bitmap bm = null;
        if (resultCode == Activity.RESULT_OK) {
            Uri uri = null;
            switch (requestCode) {
                case GET_BY_ALBUM:
                    uri = data.getData();
                    activity.startActivityForResult(crop(uri, w, h, aspectX, aspectY), CROP);
                    break;
                case GET_BY_CAMERA:
                    uri = takePictureUri;
                    activity.startActivityForResult(crop(uri, w, h, aspectX, aspectY), CROP);
                    break;
                case CROP:
                    bm = dealCrop(activity);
                    break;
            }
        }
        return bm;
    }

    /**
     * 裁剪,默认裁剪输出480*480，比例1:1
     *
     * @param uri 图片的uri
     * @return
     */
    public static Intent crop(Uri uri, int x, int y, int z, int t) {
        return crop(uri, 480, 480, 1, 1);
    }

    /**
     * 裁剪，例如：输出100*100大小的图片，宽高比例是1:1
     *
     * @param uri     图片的uri
     * @param w       输出宽
     * @param h       输出高
     * @param aspectX 宽比例
     * @param aspectY 高比例
     * @return
     */
//    public static Intent crop(Uri uri, int w, int h, int aspectX, int aspectY) {
//        if (w == 0 && h == 0) {
//            w = h = 480;
//        }
//        if (aspectX == 0 && aspectY == 0) {
//            aspectX = aspectY = 1;
//        }
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.setDataAndType(uri, "image/*");
//        intent.putExtra("crop", "true");
//        intent.putExtra("aspectX", aspectX);
//        intent.putExtra("aspectY", aspectY);
//        intent.putExtra("outputX", w);
//        intent.putExtra("outputY", h);
//
//        /*解决图片有黑边问题*/
//        intent.putExtra("scale", true);
//        intent.putExtra("scaleUpIfNeeded", true);
//
//        /*解决跳转到裁剪提示“图片加载失败”问题*/
//        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//        intent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
//
//        /*解决小米miui系统调用系统裁剪图片功能camera.action.CROP后崩溃或重新打开app的问题*/
//        StringBuffer buffer = new StringBuffer();
//        String pathName = buffer.append("file:///").append(FileUtils.getRootPath()).append(File.separator).append(Constant.APP_NAME).append(".temp.jpg").toString();
//        cropPictureTempUri = Uri.parse(pathName);
//        intent.putExtra(MediaStore.EXTRA_OUTPUT, cropPictureTempUri);//输出路径(裁剪后的保存路径)
//        // 输出格式
//        intent.putExtra("outputFormat", "JPEG");
//        // 不启用人脸识别
//        intent.putExtra("noFaceDetection", true);
//        //是否将数据保留在Bitmap中返回
//        intent.putExtra("return-data", false);
//        return intent;
//    }

    /**
     * 处理裁剪，获取裁剪后的图片
     *
     * @param context 上下文
     * @return
     */
    public static Bitmap dealCrop(Activity context) {
        Bitmap bitmap = null;
        try {
            bitmap = BitmapFactory.decodeStream(context.getContentResolver().openInputStream(cropPictureTempUri));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }
}
