package com.wong.elapp.pojo.vo.Baidu.resultData;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Content {
    @SerializedName("src")
    private String src;
    @SerializedName("dst")
    private String dst;
    @SerializedName("rect")
    private String rect;
    @SerializedName("lineCount")
    private Integer lineCount;
    @SerializedName("pasteImg")
    private String pasteImg;
    @SerializedName("points")
    private List<Points> points;

    public String getSrc() {
        return src;
    }

    public void setSrc(String src) {
        this.src = src;
    }

    public String getDst() {
        return dst;
    }

    public void setDst(String dst) {
        this.dst = dst;
    }

    public String getRect() {
        return rect;
    }

    public void setRect(String rect) {
        this.rect = rect;
    }

    public Integer getLineCount() {
        return lineCount;
    }

    public void setLineCount(Integer lineCount) {
        this.lineCount = lineCount;
    }

    public String getPasteImg() {
        return pasteImg;
    }

    public void setPasteImg(String pasteImg) {
        this.pasteImg = pasteImg;
    }

    public List<Points> getPoints() {
        return points;
    }

    public void setPoints(List<Points> points) {
        this.points = points;
    }
}
