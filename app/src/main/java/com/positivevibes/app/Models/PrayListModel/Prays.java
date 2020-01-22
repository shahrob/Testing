package com.positivevibes.app.Models.PrayListModel;

public class Prays {
    String pray_id;
    String text;
    boolean prayed_by_me;
    boolean admin_quote;
    String user_dp;
    String user_id;
    String user_full_name;
    int comments_count;
    int liked_count;

    public boolean isAdmin_quote() {
        return admin_quote;
    }

    public void setAdmin_quote(boolean admin_quote) {
        this.admin_quote = admin_quote;
    }

    public String getPray_id() {
        return pray_id;
    }

    public void setPray_id(String pray_id) {
        this.pray_id = pray_id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public boolean isPrayed_by_me() {
        return prayed_by_me;
    }

    public void setPrayed_by_me(boolean prayed_by_me) {
        this.prayed_by_me = prayed_by_me;
    }

    public String getUser_dp() {
        return user_dp;
    }

    public void setUser_dp(String user_dp) {
        this.user_dp = user_dp;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUser_full_name() {
        return user_full_name;
    }

    public void setUser_full_name(String user_full_name) {
        this.user_full_name = user_full_name;
    }

    public int getComments_count() {
        return comments_count;
    }

    public void setComments_count(int comments_count) {
        this.comments_count = comments_count;
    }

    public int getLiked_count() {
        return liked_count;
    }

    public void setLiked_count(int liked_count) {
        this.liked_count = liked_count;
    }
}
