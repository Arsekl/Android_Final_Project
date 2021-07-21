package com.example.myapplication;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class VideoMessage implements Serializable {
    @SerializedName("_id")
    private String userid;
    @SerializedName("feedurl")
    private String videoUrl;
    @SerializedName("nickname")
    private String nickName;
    @SerializedName("description")
    private String description;
    @SerializedName("likecount")
    private int likeCount;
    @SerializedName("avatar")
    private String avaTorUrl;

    public VideoMessage(String userid, String videoUrl, String nickName, String description, int likeCount, String avaTorUrl) {
        this.userid = userid;
        this.videoUrl = videoUrl;
        this.nickName = nickName;
        this.description = description;
        this.likeCount = likeCount;
        this.avaTorUrl = avaTorUrl;
    }
    public String getVideoName(){
        return  "v" + getVideoUrl().substring(getVideoUrl().length()-8).replace('%','v').toLowerCase();
    }

    public String getPicName(){
        return getAvaTorUrl().substring(getAvaTorUrl().length()-8);
    }

    public String getUserid() {
        return userid;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public String getNickName() {
        return nickName;
    }

    public String getDescription() {
        return description;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public String getAvaTorUrl() {
        return avaTorUrl;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public void setAvaTorUrl(String avaTorUrl) {
        this.avaTorUrl = avaTorUrl;
    }
}
