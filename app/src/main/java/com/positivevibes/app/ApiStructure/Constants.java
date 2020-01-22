package com.positivevibes.app.ApiStructure;

/**
 * Created by M on 3/7/2018.
 */

public interface Constants {

    interface URL {
        String BASE_URL="http://50.116.22.10:4200";
//        String BASE_URL="http://172.104.191.9";

        String REGISTER = BASE_URL + "/user/register";
        String LOGIN    = BASE_URL + "/user/login";
        String CATAGORY    = BASE_URL + "/user/list_categories";
        String SELECT_CATAGORY    = BASE_URL + "/user/select_categories";
        String SUBSCRIBED_FEED    = BASE_URL + "/user/list_categorized_media_files";
        String ADD_FAVOURITS    = BASE_URL + "/user/add_favourite/";
        String REMOVE_FAVOURITS    = BASE_URL + "/user/remove_favourite/";
        String GET_FAVOURITES    = BASE_URL + "/user/list_favourite";
        String LOGOUT    = BASE_URL + "/user/logout";
        String GET = BASE_URL + "/user/get_media/";
        String PRIVACY_POLICY = BASE_URL +"/user/privacy/policy";
        String TERMS_CONDITION = BASE_URL +"/user/terms/conditions";
        String ADD_COMMENT = BASE_URL +"/user/add_comment/";
        String LIKE_COMMENT = BASE_URL +"/user/like_comment/";
        String UNLIKE_COMMENT = BASE_URL +"/user/unlike_comment/";
        String EDIT_COMMENT = BASE_URL +"/user/edit_comment/";
        String DELETE_COMMENT = BASE_URL +"/user/delete_comment/";
        String CHANGE_PASSWORD = BASE_URL +"/user/change/password";
        String GET_PROFILE = BASE_URL +"/user/profile";
        String UPDATE_PROFILE = BASE_URL +"/user/update/profile";
        String UPLOAD_DP = BASE_URL +"/media/upload_dp";
        String EDIT_SELECTED_CATAGORY = BASE_URL +"/user/edit_subscribed_categories";
        String NOTIFICATION = BASE_URL +"/user/getNotifications";
        String SELECT_CAT_FROM_SEARCH = BASE_URL +"/user/get_category/?category_id=";
        String SEARCH_KEY_CAT = BASE_URL +"/user/search_category/?category_title=";
        String GET_NOTIFICATION_STATUS = BASE_URL +"/user/get_notify_status";
        String UPDATE_NOTIFICATION_STATUS = BASE_URL +"/user/update_notify_status";
        String REPORT_PROBLEM = BASE_URL +"/user/report";
        String FORGOT_PASSWORD = BASE_URL + "/user/forgot/password/email_pin";
        String RESET_PASSWORD = BASE_URL + "/user/reset/password/with/pin";
        String UPDATE_TOEKN = BASE_URL + "/user/update/token?token=";
        String FEED_LIST = BASE_URL +"/user/list_media_files";
        String AUTHORFEEDS = BASE_URL + "/user/get_author/?author_id=";
        String TOP_QUOTES = BASE_URL + "/user/search_top_quotes";
        String SEARCH_KEY_AUTH = BASE_URL + "/user/search_author?author_name=";
        String QUOTEREPORT = BASE_URL + "/user//report/feed/";
        String ADDPREY = BASE_URL + "/pray_req/add_request_to_pray";
        String PRAYED = BASE_URL + "/pray_req/add_like/";
        String PRAYSLIST = BASE_URL + "/pray_req/list_pray_req";
        String DELPRAY = BASE_URL + "/pray_req/remove_request_to_pray/";
        String EDITPRAY = BASE_URL + "/pray_req/edit_request_to_pray/";
        String GETPRAY = BASE_URL + "/pray_req/get_pray_req/";
        String GETUSER = BASE_URL + "/user/get_user/";
        String GETALLFEED = BASE_URL + "/user/list_all_feeds";
        String NOTIFYPRAY = BASE_URL + "/admin/notify_at_approve/";
        String REPORTLIST = BASE_URL + "/user/reports";



    }
    }
