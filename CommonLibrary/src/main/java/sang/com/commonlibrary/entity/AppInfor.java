package sang.com.commonlibrary.entity;

import android.content.ContentValues;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;

import org.litepal.crud.DataSupport;

import java.io.ByteArrayOutputStream;

/**
 * 作者： ${PING} on 2018/5/22.
 * App 信息
 */

public class AppInfor extends DataSupport {

    /**
     * App 包名
     */
    public String packageName;

    /**
     * App 名称
     */
    public String appName;

    /**
     * App 图标
     */
    private byte[] appIcon;


    private String apkFilePath;

    private int versionCode;

    private String versionName;

    private boolean isFirstOpen;

    private int userId;


    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public Drawable getAppIcon() {

        //第一步，从数据库中读取出相应数据，并保存在字节数组中
        byte[] blob = appIcon;
//第二步，调用BitmapFactory的解码方法decodeByteArray把字节数组转换为Bitmap对象

        BitmapDrawable bd = null;
        if (blob!=null) {
            Bitmap bmp = BitmapFactory.decodeByteArray(blob, 0, blob.length);
//第三步，调用BitmapDrawable构造函数生成一个BitmapDrawable对象，该对象继承Drawable对象，所以在需要处直接使用该对象即可
            bd = new BitmapDrawable(bmp);
        }
        return bd;
    }

    public void setAppIcon(Drawable drawable) {

        int w = drawable.getIntrinsicWidth();
        int h = drawable.getIntrinsicHeight();

        // 取 drawable 的颜色格式
        Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565;
        // 建立对应 bitmap
        Bitmap bitmap = Bitmap.createBitmap(w, h, config);
        // 建立对应 bitmap 的画布
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, w, h);
        // 把 drawable 内容画到画布中
        drawable.draw(canvas);

//第二步，声明并创建一个输出字节流对象
        ByteArrayOutputStream os = new ByteArrayOutputStream();
//第三步，调用compress将Bitmap对象压缩为PNG格式，第二个参数为PNG图片质量，第三个参数为接收容器，即输出字节流os
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, os);

        this.appIcon = os.toByteArray();
    }


    public String getApkFilePath() {
        return apkFilePath;
    }

    public void setApkFilePath(String apkFilePath) {
        this.apkFilePath = apkFilePath;
    }

    public int getVersionCode() {
        return versionCode;
    }

    public void setVersionCode(int versionCode) {
        this.versionCode = versionCode;
    }

    public String getVersionName() {
        return versionName;
    }

    public void setVersionName(String versionName) {
        this.versionName = versionName;
    }

    public boolean isFirstOpen() {
        return isFirstOpen;
    }

    public void setFirstOpen(boolean firstOpen) {
        isFirstOpen = firstOpen;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }


    @Override
    public String toString() {
        return "AppInfor{" +
                "packageName='" + packageName + '\'' +
                ", appName='" + appName + '\'' +
                ", apkFilePath='" + apkFilePath + '\'' +
                ", versionCode=" + versionCode +
                ", versionName='" + versionName + '\'' +
                ", isFirstOpen=" + isFirstOpen +
                ", userId=" + userId +
                '}';
    }
}
