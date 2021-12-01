package com.wong.elapp.pojo;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class RandomList {

    @SerializedName("_id")
    private String id;
    @SerializedName("names")
    private String names;
    @SerializedName("means")
    private List<String> means;
    @SerializedName("voices")
    private List<String> voices;
    @SerializedName("derive")
    private List<String> derive;
    @SerializedName("examples")
    private List<Example> examples;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNames() {
        return names;
    }

    public void setNames(String names) {
        this.names = names;
    }

    public List<String> getMeans() {
        return means;
    }

    public void setMeans(List<String> means) {
        this.means = means;
    }

    public List<String> getVoices() {
        return voices;
    }

    public void setVoices(List<String> voices) {
        this.voices = voices;
    }

    public List<String> getDerive() {
        return derive;
    }

    public void setDerive(List<String> derive) {
        this.derive = derive;
    }

    public List<Example> getExamples() {
        return examples;
    }

    public void setExamples(List<Example> examples) {
        this.examples = examples;
    }
}
