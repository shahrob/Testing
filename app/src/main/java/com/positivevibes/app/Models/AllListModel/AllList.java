package com.positivevibes.app.Models.AllListModel;

import com.positivevibes.app.Models.FeedListModel.CategoriesList;

import java.util.ArrayList;

public class AllList {
    String _id;
    String title;
    String large_image;
    String small_image;
    int comments_count;
    int liked_count;
    boolean liked_by_me;
    String auther_id;
    String auther_name;
    String auther_img;
    boolean admin_quote;
    String user_dp;
    String user_id;
    String user_fullname;
    boolean author_check;

    public boolean isAuthor_check() {
        return author_check;
    }

    public void setAuthor_check(boolean author_check) {
        this.author_check = author_check;
    }

    ArrayList<CategoriesList> categoriesArrayList;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLarge_image() {
        return large_image;
    }

    public void setLarge_image(String large_image) {
        this.large_image = large_image;
    }

    public String getSmall_image() {
        return small_image;
    }

    public void setSmall_image(String small_image) {
        this.small_image = small_image;
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

    public boolean isLiked_by_me() {
        return liked_by_me;
    }

    public void setLiked_by_me(boolean liked_by_me) {
        this.liked_by_me = liked_by_me;
    }

    public String getAuther_id() {
        return auther_id;
    }

    public void setAuther_id(String auther_id) {
        this.auther_id = auther_id;
    }

    public String getAuther_name() {
        return auther_name;
    }

    public void setAuther_name(String auther_name) {
        this.auther_name = auther_name;
    }

    public String getAuther_img() {
        return auther_img;
    }

    public void setAuther_img(String auther_img) {
        this.auther_img = auther_img;
    }

    public boolean isAdmin_quote() {
        return admin_quote;
    }

    public void setAdmin_quote(boolean admin_quote) {
        this.admin_quote = admin_quote;
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

    public String getUser_fullname() {
        return user_fullname;
    }

    public void setUser_fullname(String user_fullname) {
        this.user_fullname = user_fullname;
    }

    public ArrayList<CategoriesList> getCategoriesArrayList() {
        return categoriesArrayList;
    }

    public void setCategoriesArrayList(ArrayList<CategoriesList> categoriesArrayList) {
        this.categoriesArrayList = categoriesArrayList;
    }
}
