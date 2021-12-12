package com.wong.elapp.pojo.vo.YoudaoresultPic2Text;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ResRegions {
    @SerializedName("boundingBox")
    private String boundingBox;
    @SerializedName("color")
    private String color;
    @SerializedName("linesCount")
    private Integer linesCount;
    @SerializedName("lineheight")
    private Integer lineheight;
    @SerializedName("context")
    private String context;
    @SerializedName("tranContent")
    private String tranContent;
    @SerializedName("dir")
    private String dir;
    @SerializedName("lang")
    private String lang;
    @SerializedName("colors")
    private List<String> colors;
    @SerializedName("text_align")
    private String textAlign;

    public String getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(String boundingBox) {
        this.boundingBox = boundingBox;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Integer getLinesCount() {
        return linesCount;
    }

    public void setLinesCount(Integer linesCount) {
        this.linesCount = linesCount;
    }

    public Integer getLineheight() {
        return lineheight;
    }

    public void setLineheight(Integer lineheight) {
        this.lineheight = lineheight;
    }

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getTranContent() {
        return tranContent;
    }

    public void setTranContent(String tranContent) {
        this.tranContent = tranContent;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public List<String> getColors() {
        return colors;
    }

    public void setColors(List<String> colors) {
        this.colors = colors;
    }

    public String getTextAlign() {
        return textAlign;
    }

    public void setTextAlign(String textAlign) {
        this.textAlign = textAlign;
    }
}
