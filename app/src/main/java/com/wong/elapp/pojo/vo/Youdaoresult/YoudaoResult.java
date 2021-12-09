package com.wong.elapp.pojo.vo.Youdaoresult;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class YoudaoResult {

    @SerializedName("returnPhrase")
    private List<String> returnPhrase;
    @SerializedName("query")
    private String query;
    @SerializedName("errorCode")
    private String errorCode;
    @SerializedName("l")
    private String l;
    @SerializedName("tSpeakUrl")
    private String tSpeakUrl;
    @SerializedName("web")
    private List<Web> web;
    @SerializedName("requestId")
    private String requestId;
    @SerializedName("translation")
    private List<String> translation;
    @SerializedName("dict")
    private Dict dict;
    @SerializedName("webdict")
    private Webdict webdict;
    @SerializedName("basic")
    private Basic basic;
    @SerializedName("isWord")
    private Boolean isWord;
    @SerializedName("speakUrl")
    private String speakUrl;

    public List<String> getReturnPhrase() {
        return returnPhrase;
    }

    public void setReturnPhrase(List<String> returnPhrase) {
        this.returnPhrase = returnPhrase;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getL() {
        return l;
    }

    public void setL(String l) {
        this.l = l;
    }

    public String getTSpeakUrl() {
        return tSpeakUrl;
    }

    public void setTSpeakUrl(String tSpeakUrl) {
        this.tSpeakUrl = tSpeakUrl;
    }

    public List<Web> getWeb() {
        return web;
    }

    public void setWeb(List<Web> web) {
        this.web = web;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public List<String> getTranslation() {
        return translation;
    }

    public void setTranslation(List<String> translation) {
        this.translation = translation;
    }

    public Dict getDict() {
        return dict;
    }

    public void setDict(Dict dict) {
        this.dict = dict;
    }

    public Webdict getWebdict() {
        return webdict;
    }

    public void setWebdict(Webdict webdict) {
        this.webdict = webdict;
    }

    public Basic getBasic() {
        return basic;
    }

    public void setBasic(Basic basic) {
        this.basic = basic;
    }

    public Boolean getIsWord() {
        return isWord;
    }

    public void setIsWord(Boolean isWord) {
        this.isWord = isWord;
    }

    public String getSpeakUrl() {
        return speakUrl;
    }

    public void setSpeakUrl(String speakUrl) {
        this.speakUrl = speakUrl;
    }
}
