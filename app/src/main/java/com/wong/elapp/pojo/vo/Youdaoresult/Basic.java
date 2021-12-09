package com.wong.elapp.pojo.vo.Youdaoresult;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Basic {
    @SerializedName("exam_type")
    private List<String> examType;
    @SerializedName("us-phonetic")
    private String usphonetic;
    @SerializedName("phonetic")
    private String phonetic;
    @SerializedName("uk-phonetic")
    private String ukphonetic;
    @SerializedName("uk-speech")
    private String ukspeech;
    @SerializedName("explains")
    private List<String> explains;
    @SerializedName("us-speech")
    private String usspeech;

    public List<String> getExamType() {
        return examType;
    }

    public void setExamType(List<String> examType) {
        this.examType = examType;
    }

    public String getUsphonetic() {
        return usphonetic;
    }

    public void setUsphonetic(String usphonetic) {
        this.usphonetic = usphonetic;
    }

    public String getPhonetic() {
        return phonetic;
    }

    public void setPhonetic(String phonetic) {
        this.phonetic = phonetic;
    }

    public String getUkphonetic() {
        return ukphonetic;
    }

    public void setUkphonetic(String ukphonetic) {
        this.ukphonetic = ukphonetic;
    }

    public String getUkspeech() {
        return ukspeech;
    }

    public void setUkspeech(String ukspeech) {
        this.ukspeech = ukspeech;
    }

    public List<String> getExplains() {
        return explains;
    }

    public void setExplains(List<String> explains) {
        this.explains = explains;
    }

    public String getUsspeech() {
        return usspeech;
    }

    public void setUsspeech(String usspeech) {
        this.usspeech = usspeech;
    }
}
