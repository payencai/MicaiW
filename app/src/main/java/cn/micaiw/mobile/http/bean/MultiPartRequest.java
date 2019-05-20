package cn.micaiw.mobile.http.bean;

import java.io.File;
import java.util.Map;

/**
 * 作者：凌涛 on 2018/6/4 16:59
 * 邮箱：771548229@qq..com
 */
public interface MultiPartRequest {

    public void addFileUpload(String param, File file);

    public void addStringUpload(String param, String content);

    public Map<String, File> getFileUploads();

    public Map<String, String> getStringUploads();

}
