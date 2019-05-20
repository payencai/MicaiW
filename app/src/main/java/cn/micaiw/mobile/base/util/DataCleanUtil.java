package cn.micaiw.mobile.base.util;

/**
 * 文 件 名:  DataCleanManager.java
 * 描    述:  主要功能有清除内/外缓存，清除数据库，清除sharedPreference，清除files和清除自定义目录
 */

import android.content.Context;
import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
//import android.webkit.CacheManager;

/**
 * imported by Hans on 2016.8.18
 */

/** * 本应用数据清除管理器 */
public class DataCleanUtil {
    /** * 清除本应用内部缓存(/data/data/com.xxx.xxx/cache) * * @param context */
    public static void cleanInternalCache(Context context) {
        deleteFilesByDirectory(context.getCacheDir(),
                System.currentTimeMillis());
    }

    /** * 清除本应用所有数据库(/data/data/com.xxx.xxx/databases) * * @param context */
    public static void cleanDatabases(Context context) {
        deleteFilesByDirectory(
                new File("/data/data/" + context.getPackageName()
                        + "/databases"), System.currentTimeMillis());
    }

    /**
     * * 清除本应用SharedPreference(/data/data/com.xxx.xxx/shared_prefs) * * @param
     * context
     */
    public static void cleanSharedPreference(Context context) {
        deleteFilesByDirectory(
                new File("/data/data/" + context.getPackageName()
                        + "/shared_prefs"), System.currentTimeMillis());
    }

    /** * 按名字清除本应用数据库 * * @param context * @param dbName */
    public static void cleanDatabaseByName(Context context, String dbName) {
        context.deleteDatabase(dbName);
    }


    /** * 清除/data/data/com.xxx.xxx/files下的内容 * * @param context */
    public static void cleanFiles(Context context) {
        deleteFilesByDirectory(context.getFilesDir(),
                System.currentTimeMillis());
    }

    /**
     * * 清除外部cache下的内容(/mnt/sdcard/android/data/com.xxx.xxx/cache) * * @param
     * context
     */
    public static void cleanExternalCache(Context context) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {
            deleteFilesByDirectory(context.getExternalCacheDir(),
                    System.currentTimeMillis());
        }
    }

    /** * 清除自定义路径下的文件，使用需小心，请不要误删。而且只支持目录下的文件删除 * * @param filePath */
    public static void cleanCustomCache(String filePath) {
        deleteFilesByDirectory(new File(filePath), System.currentTimeMillis());
    }

    /** * 清除本应用所有的数据 * * @param context * @param filepath */
    public static void cleanApplicationData(Context context, String... filepath) {
        cleanInternalCache(context);
        cleanExternalCache(context);
        cleanDatabases(context);
        cleanSharedPreference(context);
        cleanFiles(context);
        for (String filePath : filepath) {
            cleanCustomCache(filePath);
        }
    }

    /** * 删除方法 这里只会删除某个文件夹下的文件，如果传入的directory是个文件，将不做处理 * * @param directory */
    public static void deleteFilesByDirectory(File directory, long curTime) {
        if (directory != null && directory.isDirectory()) {
            try {
                for (File child : directory.listFiles()) {
                    if (child.isDirectory()) {
                        deleteFilesByDirectory(child, curTime);
                    } else if (child.lastModified() < curTime) {
                        child.delete();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        // if (directory != null && directory.exists() ) {
        // for (File item : directory.listFiles()) {
        // if(item.isDirectory()){
        // deleteFilesByDirectory(item);
        // }
        // else
        // item.delete();
        // }
        // }
    }

    public void clearAppCache(Context context) {
        // 清除webview缓存
        //@SuppressWarnings("deprecation")
        //File file = CacheManager.getCacheFileBaseDir();

        // 先删除WebViewCache目录下的文件

        //deleteFilesByDirectory(file, System.currentTimeMillis());

        // cleanDatabaseByName
        context.deleteDatabase("webview.db");
        context.deleteDatabase("webview.db-shm");
        context.deleteDatabase("webview.db-wal");
        context.deleteDatabase("webviewCache.db");
        context.deleteDatabase("webviewCache.db-shm");
        context.deleteDatabase("webviewCache.db-wal");
        // 清除数据缓存
        deleteFilesByDirectory(context.getFilesDir(),
                System.currentTimeMillis());
        deleteFilesByDirectory(context.getCacheDir(),
                System.currentTimeMillis());
        // 2.2版本才有将应用缓存转移到sd卡的功能
        if (isMethodsCompat(android.os.Build.VERSION_CODES.FROYO)) {
            deleteFilesByDirectory(context.getExternalCacheDir(),
                    System.currentTimeMillis());
        }

    }

    /**
     * 判断当前版本是否兼容目标版本的方法
     *
     * @param VersionCode
     * @return
     */
    public static boolean isMethodsCompat(int VersionCode) {
        int currentVersion = android.os.Build.VERSION.SDK_INT;
        return currentVersion >= VersionCode;
    }

    public static long getFileSizes(File f) throws Exception {// 取得文件大小
        long s = 0;
        FileInputStream fis = null;
        if (f.exists()) {

            fis = new FileInputStream(f);
            s = fis.available();
        } else {
            f.createNewFile();
            System.out.println("文件不存在");
        }
        fis.close();
        return s;
    }

    // 递归
    public static long getFileSizeFromDirectory(File f) throws Exception// 取得文件夹大小
    {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSizeFromDirectory(flist[i]);
            } else {
                size = size + getFileSizes(flist[i]);
            }
        }
        return size;
    }

    // public String FormetFileSize(long fileS) {//转换文件大小
    // DecimalFormat df = new DecimalFormat("#.00");
    // String fileSizeString = "";
    // if (fileS < 1024) {
    // fileSizeString = df.format((double) fileS) + "B";
    // } else if (fileS < 1048576) {
    // fileSizeString = df.format((double) fileS / 1024) + "K";
    // } else if (fileS < 1073741824) {
    // fileSizeString = df.format((double) fileS / 1048576) + "M";
    // } else {
    // fileSizeString = df.format((double) fileS / 1073741824) + "G";
    // }
    // return fileSizeString;
    // }

    public long getlist(File f) {// 递归求取目录文件个数
        long size = 0;
        File flist[] = f.listFiles();
        size = flist.length;
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getlist(flist[i]);
                size--;
            }
        }
        return size;

    }

    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte < 1) {
            return size + "Byte(s)";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        BigDecimal result4 = new BigDecimal(teraBytes);
        return result4.setScale(2, BigDecimal.ROUND_HALF_UP).toPlainString()
                + "TB";
    }
}