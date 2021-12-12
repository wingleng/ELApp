package com.wong.elapp.pojo.vo.Baidu.resultData;

import com.google.gson.annotations.SerializedName;

public class Points {
    @SerializedName("x")
    private Integer x;
    @SerializedName("y")
    private Integer y;

    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }
}
