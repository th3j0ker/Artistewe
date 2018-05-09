package com.typhon.studios.artistewe_prototype_3.Home.GlobalTimelineFragment.Models;

public class LikesHandler {
    private String postid;
    private String userd;

    public LikesHandler(String postid, String userd) {
        this.postid = postid;
        this.userd = userd;
    }

    public LikesHandler() {
    }

    public String getPostid() {
        return postid;
    }

    public void setPostid(String postid) {
        this.postid = postid;
    }

    public String getUserd() {
        return userd;
    }

    public void setUserd(String userd) {
        this.userd = userd;
    }
}
