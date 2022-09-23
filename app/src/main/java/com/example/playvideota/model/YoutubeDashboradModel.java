package com.example.playvideota.model;

public class YoutubeDashboradModel {
    String videoImage;
    String videoDescription;
    String videoTitle;
    String videoId;
    String videoLiveBroadcastContent;



    public YoutubeDashboradModel(String videoImage, String videoDescription, String videoTitle, String videoId, String videoLiveBroadcastContent) {
        this.videoImage = videoImage;
        this.videoDescription = videoDescription;
        this.videoTitle = videoTitle;
        this.videoId = videoId;
        this.videoLiveBroadcastContent = videoLiveBroadcastContent;
    }

    public String getVideoImage() {
        return videoImage;
    }

    public void setVideoImage(String videoImage) {
        this.videoImage = videoImage;
    }

    public String getVideoLiveBroadcastContent() {
        return videoLiveBroadcastContent;
    }

    public void setVideoLiveBroadcastContent(String videoLiveBroadcastContent) {
        this.videoLiveBroadcastContent = videoLiveBroadcastContent;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public String getVideoId() {
        return videoId;
    }

    public void setVideoId(String videoId) {
        this.videoId = videoId;
    }


    public String getVideoDescription() {
        return videoDescription;
    }

    public void setVideoDescription(String videoDescription) {
        this.videoDescription = videoDescription;
    }
}
