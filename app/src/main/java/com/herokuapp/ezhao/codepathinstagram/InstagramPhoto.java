package com.herokuapp.ezhao.codepathinstagram;

import java.util.ArrayList;

/**
 * Created by emily on 2/4/15.
 */
public class InstagramPhoto {
    private String username;
    private String userImageUrl;
    private String caption;
    private String imageUrl;
    private int imageHeight;
    private int likeCount;
    private int commentCount;
    private long createdTime;
    private ArrayList<InstagramPhotoComment> comments;

    public InstagramPhoto() {
        comments = new ArrayList<InstagramPhotoComment>();
    }

    public void addComment(String username, String text) {
        InstagramPhotoComment newComment = new InstagramPhotoComment();
        newComment.setUsername(username);
        newComment.setText(text);
        comments.add(newComment);
    }

    public ArrayList<InstagramPhotoComment> getComments() {
        return comments;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getImageHeight() {
        return imageHeight;
    }

    public void setImageHeight(int imageHeight) {
        this.imageHeight = imageHeight;
    }

    public int getLikeCount() {
        return likeCount;
    }

    public void setLikeCount(int likeCount) {
        this.likeCount = likeCount;
    }

    public long getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(long createdTime) {
        this.createdTime = createdTime;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }
}
