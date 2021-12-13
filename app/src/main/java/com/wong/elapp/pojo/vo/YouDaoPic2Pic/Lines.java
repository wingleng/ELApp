package com.wong.elapp.pojo.vo.YouDaoPic2Pic;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Lines {
    @SerializedName("textHeight")
    private Integer textHeight;
    @SerializedName("boundingBox")
    private String boundingBox;
    @SerializedName("words")
    private List<Words> words;
    @SerializedName("text")
    private String text;
    @SerializedName("lang")
    private String lang;
    @SerializedName("dir")
    private String dir;
    @SerializedName("firstLine")
    private Boolean firstLine;
    @SerializedName("text_align")
    private Object textAlign;
    @SerializedName("lineSpace")
    private Double lineSpace;

    public Integer getTextHeight() {
        return textHeight;
    }

    public void setTextHeight(Integer textHeight) {
        this.textHeight = textHeight;
    }

    public String getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(String boundingBox) {
        this.boundingBox = boundingBox;
    }

    public List<Words> getWords() {
        return words;
    }

    public void setWords(List<Words> words) {
        this.words = words;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getLang() {
        return lang;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }

    public Boolean getFirstLine() {
        return firstLine;
    }

    public void setFirstLine(Boolean firstLine) {
        this.firstLine = firstLine;
    }

    public Object getTextAlign() {
        return textAlign;
    }

    public void setTextAlign(Object textAlign) {
        this.textAlign = textAlign;
    }

    public Double getLineSpace() {
        return lineSpace;
    }

    public void setLineSpace(Double lineSpace) {
        this.lineSpace = lineSpace;
    }
}
