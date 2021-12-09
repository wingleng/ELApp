package com.wong.elapp.pojo.vo.Youdaoresult;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Web {
    @SerializedName("value")
    private List<String> value;
    @SerializedName("key")
    private String key;

    public List<String> getValue() {
        return value;
    }

    public void setValue(List<String> value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
