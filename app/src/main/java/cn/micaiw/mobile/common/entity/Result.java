package cn.micaiw.mobile.common.entity;

/**
 * Created by ckerv on 2018/2/4.
 */

public class Result<T> {

    public static final int SUCCESS_CODE = 0;
    public static final int TOKEN_EXPIRE_CODE = 6666;

    public int resultCode;
    public String message;
    public T data;

    public int getResultCode() {
        return resultCode;
    }

    public void setResultCode(int code) {
        this.resultCode = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
