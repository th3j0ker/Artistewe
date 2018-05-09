package com.typhon.studios.artistewe_prototype_3.Home.GlobalTimelineFragment;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class NewPost {
    private String title;
    private String body;
    private String email;
    private String imageuri;
    private String firstname;
    private String lastname;
    private String uniqueid;
    private String nodevalue;
    public int likecount = 0;
    public int notifymecount = 0;
    private boolean liked = false;
    private boolean saved = false;
    private long time;
    public Map<String, Boolean> stars = new HashMap<>();
    public Map<String, Boolean> bells = new HashMap<>();



    public NewPost(String title, String body, String email, String imageuri, String firstname, String lastname, String uniqueid, String nodevalue, boolean liked, boolean saved, long time) {
        this.title = title;
        this.body = body;
        this.email = email;
        this.imageuri = imageuri;
        this.firstname = firstname;
        this.lastname = lastname;
        this.uniqueid = uniqueid;
        this.nodevalue = nodevalue;
        this.liked = liked;
        this.saved = saved;
        this.time = time;
    }

    public NewPost(String title, String body, String email, String imageuri, String firstname, String lastname, String uniqueid, boolean liked, boolean saved, long time) {
        this.title = title;
        this.body = body;
        this.email = email;
        this.imageuri = imageuri;
        this.firstname = firstname;
        this.lastname = lastname;
        this.uniqueid = uniqueid;
        this.liked = liked;
        this.saved = saved;
        this.time = time;
    }

    public NewPost(String title, String body, String email, String imageuri, String firstname, String lastname, String uniqueid, long time) {
        this.title = title;
        this.body = body;
        this.email = email;
        this.imageuri = imageuri;
        this.firstname = firstname;
        this.lastname = lastname;
        this.uniqueid = uniqueid;
        this.time = time;
    }

    public NewPost(String title, String body, String email, String imageuri, long time) {
        this.title = title;
        this.body = body;
        this.email = email;
        this.imageuri = imageuri;
        this.time = time;
    }

    public NewPost(String title, String body, String email) {
        this.title = title;
        this.body = body;
        this.email = email;
        time = new Date().getTime();
    }

    public NewPost(String title, String body) {
        this.title = title;
        this.body = body;
    }

    public NewPost() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImageuri() {
        return imageuri;
    }

    public void setImageuri(String imageuri) {
        this.imageuri = imageuri;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getUniqueid() {
        return uniqueid;
    }

    public void setUniqueid(String uniqueid) {
        this.uniqueid = uniqueid;
    }

    public boolean isLiked() {
        return liked;
    }

    public void setLiked(boolean liked) {
        this.liked = liked;
    }

    public boolean isSaved() {
        return saved;
    }

    public void setSaved(boolean saved) {
        this.saved = saved;
    }

    public String getNodevalue() {
        return nodevalue;
    }

    public void setNodevalue(String nodevalue) {
        this.nodevalue = nodevalue;
    }

    public int getLikecount() {
        return likecount;
    }

    public void setLikecount(int likecount) {
        this.likecount = likecount;
    }
}
