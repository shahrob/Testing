package com.positivevibes.app.Models.CatagoriesApiModel;

public class Catagories {

    String _id;
    String title;
    boolean is_active;
    String cat_img;
    boolean select_all_chkbox;
    boolean selected_by_user;


    public boolean isSelect_all_chkbox() {
        return select_all_chkbox;
    }

    public void setSelect_all_chkbox(boolean select_all_chkbox) {
        this.select_all_chkbox = select_all_chkbox;
    }

    public boolean isSelected_by_user() {
        return selected_by_user;
    }

    public void setSelected_by_user(boolean selected_by_user) {
        this.selected_by_user = selected_by_user;
    }

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

    public boolean isIs_active() {
        return is_active;
    }

    public void setIs_active(boolean is_active) {
        this.is_active = is_active;
    }

    public String getCat_img() {
        return cat_img;
    }

    public void setCat_img(String cat_img) {
        this.cat_img = cat_img;
    }
}
