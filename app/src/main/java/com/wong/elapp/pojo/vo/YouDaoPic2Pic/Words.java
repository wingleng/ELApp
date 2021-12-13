package com.wong.elapp.pojo.vo.YouDaoPic2Pic;

import com.google.gson.annotations.SerializedName;

public class Words {
    @SerializedName("boundingBox")
    private String boundingBox;
    @SerializedName("text")
    private String text;
    @SerializedName("word")
    private String word;

    public String getBoundingBox() {
        return boundingBox;
    }

    public void setBoundingBox(String boundingBox) {
        this.boundingBox = boundingBox;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }
}
