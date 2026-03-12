package com.tqwc.feastcommon.utils;

import lombok.Data;

import java.io.Serializable;

/**
 * @author 49462
 * @data 2026/3/12 11:22
 */

@Data
public class Response implements Serializable {


    private int code; // 200是正常，非200表示异常
    private String msg;
    private Object data;

    public static Response success(Object data) {
        return success(200, "操作成功", data);
    }

    public static Response success(int code, String msg, Object data) {
        Response r = new Response();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }

    public static Response fail(String msg) {
        return fail(400, msg, null);
    }

    public static Response fail(String msg, Object data) {
        return fail(400, msg, data);
    }

    public static Response fail(int code, String msg, Object data) {
        Response r = new Response();
        r.setCode(code);
        r.setMsg(msg);
        r.setData(data);
        return r;
    }
}
