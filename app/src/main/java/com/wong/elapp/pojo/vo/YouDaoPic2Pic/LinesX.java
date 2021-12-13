package com.wong.elapp.pojo.vo.YouDaoPic2Pic;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class LinesX {
    @SerializedName("textHeight")
    private Integer textHeight;
    @SerializedName("boundingBox")
    private String boundingBox;
    @SerializedName("words")
    private List<WordsX> words;
    @SerializedName("text")
    private String text;
    @SerializedName("lang")
    private String lang;
    @SerializedName("firstLine")
    private Boolean firstLine;

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

    public List<WordsX> getWords() {
        return words;
    }

    public void setWords(List<WordsX> words) {
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

    public Boolean getFirstLine() {
        return firstLine;
    }

    public void setFirstLine(Boolean firstLine) {
        this.firstLine = firstLine;
    }
}
