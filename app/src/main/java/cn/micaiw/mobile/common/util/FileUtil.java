package cn.micaiw.mobile.common.util;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;

import cn.micaiw.mobile.common.constant.Constans;

/**
 * Created by simon on 2018/2/10 0010.
 */

public class FileUtil {

    private String ROOT_CACHE;
    public static String ROOT_DIR="gaotezhipei";
    private static FileUtil instance = null;

    public static FileUtil getInstance(Context context) {
        if (instance == null) {
            instance = new FileUtil();
            if (Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED)) {
                instance.ROOT_CACHE = (Environment.getExternalStorageDirectory() + "/"
                        + ROOT_DIR + "/");
            } else {
                instance.ROOT_CACHE = (context.getFilesDir().getAbsolutePath() + "/"+ROOT_DIR+"/");
            }
            File dir = new File(instance.ROOT_CACHE);
            if (!dir.exists()) {
                dir.mkdirs();
            }
        }
        return instance;
    }

    public File makeDir(String dir) {
        File fileDir = new File(ROOT_CACHE + dir);
        if (fileDir.exists()) {
            return fileDir;
        } else {
            fileDir.mkdirs();
            return fileDir;
        }
    }

    /**
     * 判断SD卡是否存在
     * @return
     */
    public static boolean isSDCardExist() {
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static void closeInputStream(InputStream in) {
        if (in != null) {
            try {
                in.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeBufferedReader(BufferedReader bufferedReader) {
        if (bufferedReader != null) {
            try {
                bufferedReader.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static void closeInputStreamReader(InputStreamReader inputStreamReader) {
        if (inputStreamReader != null) {
            try {
                inputStreamReader.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }

    public static void closeOutputStream(OutputStream out) {
        if (out != null) {
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    // Get error info
    private static String getErrorInfo(Throwable ex) {
        Writer writer = new StringWriter();
        PrintWriter pw = new PrintWriter(writer);
        ex.printStackTrace(pw);
        String error = writer.toString();
        return error;
    }

    // Write exception log to file
    public static void writeExceptionLog(Context context, Throwable ex) {
        String exception = getErrorInfo(ex);
        String fileName = "";
        if (TextUtils.isEmpty(fileName)) {
            fileName = "exception";
        } else {
            fileName = fileName + "_exception";
        }
        String fileUrl = Constans.LOCATION_ERROR_FILE;
        File file = new File(fileUrl);
        if(!file.exists()) {
            file.mkdirs();
        }
        String filePath = file.getPath() + "/" + fileName + ".log";
        try {
            FileOutputStream outputStream = new FileOutputStream(filePath, true);
            outputStream.write(exception.getBytes());
            outputStream.flush();
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Delete directory and its sub-dir or sub-files
    public static boolean delete(File file) {
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            if (files != null)
                for (File f : files) delete(f);
        }
        return file.delete();
    }

    public String getRootCache() {
        return ROOT_CACHE;
    }
}
