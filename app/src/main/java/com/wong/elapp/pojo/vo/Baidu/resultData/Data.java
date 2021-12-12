package com.wong.elapp.pojo.vo.Baidu.resultData;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Data {
    @SerializedName("from")
    private String from;
    @SerializedName("to")
    private String to;
    @SerializedName("content")
    private List<Content> content;
    @SerializedName("sumSrc")
    private String sumSrc;
    @SerializedName("sumDst")
    private String sumDst;
    @SerializedName("pasteImg")
    private String pasteImg;

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public List<Content> getContent() {
        return content;
    }

    public void setContent(List<Content> content) {
        this.content = content;
    }

    public String getSumSrc() {
        return sumSrc;
    }

    public void setSumSrc(String sumSrc) {
        this.sumSrc = sumSrc;
    }

    public String getSumDst() {
        return sumDst;
    }

    public void setSumDst(String sumDst) {
        this.sumDst = sumDst;
    }

    public String getPasteImg() {
        return pasteImg;
    }

    public void setPasteImg(String pasteImg) {
        this.pasteImg = pasteImg;
    }
}
