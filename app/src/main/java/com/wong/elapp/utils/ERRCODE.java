package com.wong.elapp.utils;

/**
 * 这个枚举类主要是用来保存安卓app的错误
 */

public enum ERRCODE {
    REQUEST_SUCCESS("网络请求","请求发送成功"),
    REQUEST_FAILED("网络请求","请求发送失败");
    private String msgtype;
    private String msg;
    ERRCODE(String spot,String msg){
        this.msgtype = spot;
        this.msg = msg;
    }

    public String getMsgtype() {
        return msgtype;
    }

    public void setMsgtype(String msgtype) {
        this.msgtype = msgtype;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
