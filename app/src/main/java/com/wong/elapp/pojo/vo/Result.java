package com.wong.elapp.pojo.vo;


/**
 * 这个Result是和后端的Result是一致的，就是多了一个泛型类T，是因为谷歌的gson无法自己推断，Object类型的数据应该转换为哪种类型，所以需要手动转换
 * 这就导致了，一个问题吧，就是必须要和知道后端返回的Result的数据类型。
 */
public class Result <T>{

    private boolean success;

    private int code;

    private String msg;

    private T data;

    public static Result success(Object data){
        return new Result(true,200,"success",data);
    }
    public static Result fail(int code, String msg){
        return new Result(false,code,msg,null);
    }

    public Result(boolean success, int code, String msg, T data) {
        this.success = success;
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public Object getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "Result{" +
                "success=" + success +
                ", code=" + code +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}