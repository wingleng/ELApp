package com.wong.elapp.pojo.vo.YouDaoPic2Pic;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class YoudaoPic2Pic {

    @SerializedName("orientation")
    private String orientation;
    @SerializedName("RequestId")
    private String requestId;
    @SerializedName("image_size")
    private List<Integer> imageSize;
    @SerializedName("lanFrom")
    private String lanFrom;
    @SerializedName("render_image")
    private String renderImage;
    @SerializedName("textAngle")
    private String textAngle;
    @SerializedName("errorCode")
    private String errorCode;
    @SerializedName("lanTo")
    private String lanTo;
    @SerializedName("lines")
    private List<Lines> lines;
    @SerializedName("exif")
    private String exif;
    @SerializedName("resRegions")
    private List<ResRegions> resRegions;

    public String getOrientation() {
        return orientation;
    }

    public void setOrientation(String orientation) {
        this.orientation = orientation;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public List<Integer> getImageSize() {
        return imageSize;
    }

    public void setImageSize(List<Integer> imageSize) {
        this.imageSize = imageSize;
    }

    public String getLanFrom() {
        return lanFrom;
    }

    public void setLanFrom(String lanFrom) {
        this.lanFrom = lanFrom;
    }

    public String getRenderImage() {
        return renderImage;
    }

    public void setRenderImage(String renderImage) {
        this.renderImage = renderImage;
    }

    public String getTextAngle() {
        return textAngle;
    }

    public void setTextAngle(String textAngle) {
        this.textAngle = textAngle;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getLanTo() {
        return lanTo;
    }

    public void setLanTo(String lanTo) {
        this.lanTo = lanTo;
    }

    public List<Lines> getLines() {
        return lines;
    }

    public void setLines(List<Lines> lines) {
        this.lines = lines;
    }

    public String getExif() {
        return exif;
    }

    public void setExif(String exif) {
        this.exif = exif;
    }

    public List<ResRegions> getResRegions() {
        return resRegions;
    }

    public void setResRegions(List<ResRegions> resRegions) {
        this.resRegions = resRegions;
    }
}
