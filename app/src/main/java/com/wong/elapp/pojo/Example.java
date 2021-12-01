package com.wong.elapp.pojo;

import com.google.gson.annotations.SerializedName;

public class Example {
    @SerializedName("img")
    private String img;
    @SerializedName("audio")
    private String audio;
    @SerializedName("sentence")
    private String sentence;
    @SerializedName("title")
    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getAudio() {
        return audio;
    }

    public void setAudio(String audio) {
        this.audio = audio;
    }

    public String getSentence() {
        return sentence;
    }

    public void setSentence(String sentence) {
        this.sentence = sentence;
    }
}
