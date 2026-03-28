package com.tqwc.feastcommon.utils;



import java.io.Serializable;

/**
 * @author Tang
 * @data 2026/3/12 11:22
 */

//返回结果对象
public class Result {
    private Integer conde;
    private String message;
    private Object data;

    public Result() {
    }

    public Result(Integer conde, String message) {
        this.conde = conde;
        this.message = message;
    }

    public Result( Integer conde, String message, Object data) {
        this.conde = conde;
        this.message = message;
        this.data = data;
    }


    public Integer getConde() {
        return conde;
    }

    public void setConde(Integer conde) {
        this.conde = conde;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "conde=" + conde +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }
}

