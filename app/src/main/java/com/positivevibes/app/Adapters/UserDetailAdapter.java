package com.positivevibes.app.Adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.positivevibes.app.Activity.AddPreyActivity;
import com.positivevibes.app.Activity.PrayDetailedActivity;
import com.positivevibes.app.Activity.SelectFromSearchActivity;
import com.positivevibes.app.Activity.UserDetail;
import com.positivevibes.app.ApiStructure.ApiModelClass;
import com.positivevibes.app.ApiStructure.Constants;
import com.positivevibes.app.ApiStructure.ServerCallback;
import com.positivevibes.app.Fragments.PrayFragment;
import com.positivevibes.app.Fragments.RandomFeedFragment;
import com.positivevibes.app.Models.MyBounceInterpolator;
import com.positivevibes.app.Models.MySpannable;
import com.positivevibes.app.Models.PrayListModel.Prays;
import com.positivevibes.app.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class UserDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    Context mContext;
    private List<Prays> feedList;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    String USER_ID = "";

    LinearLayoutManager mLayoutManager;
    private CategoryListAdapter cate_adapter;
    ProgressDialog dialog;

    int like_img_status=0;

    public UserDetailAdapter(Context mContext, List<Prays> feedList) {
        this.mContext = mContext;
        this.feedList = feedList;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }
    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            //inflate your layout and pass it to view holder
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.row_user_detail, parent, false);
            return new MyViewHolder(itemView);
        } else if (viewType == TYPE_HEADER) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.head_user_detail, parent, false);
            //inflate your layout and pass it to view holder
            return new VHHeader(itemView);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }

    class VHHeader extends RecyclerView.ViewHolder{

        ImageView author_detail_image;
        TextView count_of_quotes, author_country, author_dateofborn, author_bio, count_of_age, author_name_main, author_achievements
                , count_of_prays, count_of_prayed;
        LinearLayout age_layout, quotes_count_linear, country_layout, dob_layout, achievement_layout, about_name_linear;
        RelativeLayout first_row_conatiner;

        public VHHeader(View itemView) {
            super(itemView);

            author_detail_image = itemView.findViewById(R.id.author_detail_image);
            first_row_conatiner = itemView.findViewById(R.id.first_row_conatiner);
            count_of_quotes = itemView.findViewById(R.id.count_of_quotes);
            author_country = itemView.findViewById(R.id.author_country);
            author_dateofborn = itemView.findViewById(R.id.author_dateofborn);
            author_bio = itemView.findViewById(R.id.author_bio);
            count_of_age = itemView.findViewById(R.id.count_of_age);
            age_layout = itemView.findViewById(R.id.age_layout);
            author_name_main = itemView.findViewById(R.id.author_name_main);
            author_achievements = itemView.findViewById(R.id.author_achievements);
            quotes_count_linear = itemView.findViewById(R.id.quotes_count_linear);
            count_of_prays = itemView.findViewById(R.id.count_of_prays);
            count_of_prayed = itemView.findViewById(R.id.count_of_prayed);
            country_layout = itemView.findViewById(R.id.country_layout);
            dob_layout = itemView.findViewById(R.id.dob_layout);
            achievement_layout = itemView.findViewById(R.id.achievement_layout);
            about_name_linear = itemView.findViewById(R.id.about_name_linear);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        CircleImageView author_profile_img;
        TextView author_profile_name, author_date, author_des, author_like_count, author_comment_count, pray_txt;
        ImageView author_img, author_like_heart_img;
        LinearLayout author_count_linear_parent, author_count_below_line_lin, author_add_parent_linear;
        RelativeLayout author_heart_count_img_rel, author_heart_count_rel, author_comment_count_img_rel, author_comment_count_rel, author_like_heart_rel,
                author_comment_rel, author_comment_text_rel, author_share_rel, author_share_text_rel, author_feed_dots, profile_img_rel;
        RecyclerView author_category_list;
        AdView author_add_one, author_add_two, author_add_three, author_add_four;

        public MyViewHolder(View itemView) {
            super(itemView);

            author_profile_img = itemView.findViewById(R.id.author_profile_img);
            pray_txt = itemView.findViewById(R.id.pray_text);
            profile_img_rel = itemView.findViewById(R.id.profile_img_rel);
            author_profile_name = itemView.findViewById(R.id.author_profile_name);
            author_date = itemView.findViewById(R.id.author_date);
            author_des = itemView.findViewById(R.id.author_des);
            author_like_count = itemView.findViewById(R.id.author_like_count);
            author_comment_count = itemView.findViewById(R.id.author_comment_count);
            author_img = itemView.findViewById(R.id.author_img);
            author_like_heart_img = itemView.findViewById(R.id.author_like_heart_img);
            author_count_linear_parent = itemView.findViewById(R.id.author_count_linear_parent);
            author_count_below_line_lin = itemView.findViewById(R.id.author_count_below_line_lin);
            author_add_parent_linear = itemView.findViewById(R.id.author_add_parent_linear);
            author_heart_count_img_rel = itemView.findViewById(R.id.author_heart_count_img_rel);
            author_heart_count_rel = itemView.findViewById(R.id.author_heart_count_rel);
            author_comment_count_img_rel = itemView.findViewById(R.id.author_comment_count_img_rel);
            author_comment_count_rel = itemView.findViewById(R.id.author_comment_count_rel);
            author_like_heart_rel = itemView.findViewById(R.id.author_like_heart_rel);
            author_comment_rel = itemView.findViewById(R.id.author_comment_rel);
            author_comment_text_rel = itemView.findViewById(R.id.author_comment_text_rel);
            author_share_rel = itemView.findViewById(R.id.author_share_rel);
            author_share_text_rel = itemView.findViewById(R.id.author_share_text_rel);
            author_category_list = itemView.findViewById(R.id.author_category_list);
            author_add_one = itemView.findViewById(R.id.author_add_one);
            author_add_two = itemView.findViewById(R.id.author_add_two);
            author_add_three = itemView.findViewById(R.id.author_add_three);
            author_add_four = itemView.findViewById(R.id.author_add_four);
            author_feed_dots = itemView.findViewById(R.id.author_feed_dots);


        }
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {

        if (holder instanceof VHHeader){

            ((VHHeader) holder).author_name_main.setText(UserDetail.p_user_full_name);
            ((VHHeader) holder).count_of_prays.setText(UserDetail.p_user_pray_count);
            ((VHHeader) holder).count_of_prayed.setText(UserDetail.p_user_prayed_count);

            // Hide Detail

            ((VHHeader) holder).country_layout.setVisibility(View.GONE);
            ((VHHeader) holder).dob_layout.setVisibility(View.GONE);
            ((VHHeader) holder).achievement_layout.setVisibility(View.GONE);

            // Hide Detail

            // show Detail

            ((VHHeader) holder).first_row_conatiner.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

            if (!UserDetail.p_user_dp.isEmpty()) {
                Picasso.with(mContext)
                        .load(Constants.URL.BASE_URL + UserDetail.p_user_dp)
                        .transform(new CropCircleTransformation())
                        .into(((VHHeader) holder).author_detail_image);
            }
            if (!UserDetail.p_user_quotes_count.equals("0")) {
                ((VHHeader) holder).count_of_quotes.setText(UserDetail.p_user_quotes_count);
            }else {
                ((VHHeader) holder).quotes_count_linear.setVisibility(View.GONE);
            }
//            if (!UserDetail.p_user_achievements.isEmpty()){
//                ((VHHeader) holder).author_achievements.setText(UserDetail.p_user_achievements);
//                if (((VHHeader) holder).author_achievements.getLineCount() > 2) {
//                    makeTextViewResizable(((VHHeader) holder).author_achievements, 2, "... See More", true);
//                }
//            }else {
//                ((VHHeader) holder).achievement_layout.setVisibility(View.GONE);
//            }
//            if (!UserDetail.p_user_country.isEmpty()){
//                ((VHHeader) holder).author_country.setText(UserDetail.p_user_country);
//            }else {
//                ((VHHeader) holder).country_layout.setVisibility(View.GONE);
//            }
//            if (!UserDetail.p_user_DOB.isEmpty()){
//                ((VHHeader) holder).author_dateofborn.setText(UserDetail.p_user_DOB);
//                ((VHHeader) holder).age_layout.setVisibility(View.VISIBLE);
//                ((VHHeader) holder).count_of_age.setText(UserDetail.p_user_age);
//            }else {
//                ((VHHeader) holder).dob_layout.setVisibility(View.GONE);
//            }
            if (!UserDetail.p_user_about.isEmpty()){
                ((VHHeader) holder).about_name_linear.setVisibility(View.VISIBLE);
                ((VHHeader) holder).author_bio.setText(UserDetail.p_user_about);
                if (((VHHeader) holder).author_bio.getLineCount() > 2) {
                    makeTextViewResizable(((VHHeader) holder).author_bio, 2, "... See More", true);
                }

            }else {
                ((VHHeader) holder).author_bio.setVisibility(View.GONE);
                ((VHHeader) holder).about_name_linear.setVisibility(View.GONE);
            }

            // show Detail


        }else if (holder instanceof MyViewHolder) {

            //        load add

//            if (position != 0) {
//
//                if (position == 1 || position % 6 == 0) {
//                    int add_number = 0;
//
//                    if (SelectFromSearchActivity.current_add.equals("one")) {
//                        add_number = 1;
//
//                        ((MyViewHolder) holder).author_add_parent_linear.setVisibility(View.VISIBLE);
//                        ((MyViewHolder) holder).author_add_one.setVisibility(View.VISIBLE);
//                        ((MyViewHolder) holder).author_add_two.setVisibility(View.GONE);
//                        ((MyViewHolder) holder).author_add_three.setVisibility(View.GONE);
//                        ((MyViewHolder) holder).author_add_four.setVisibility(View.GONE);
//                        MobileAds.initialize(mContext, String.valueOf(R.string.add_app_id));
//
//                        AdRequest ad_req = new AdRequest.Builder().build();
//                        ((MyViewHolder) holder).author_add_one.loadAd(ad_req);
//
//                    }
//                    if (UserDetail.current_add.equals("two")) {
//                        add_number = 2;
//
//                        ((MyViewHolder) holder).author_add_parent_linear.setVisibility(View.VISIBLE);
//                        ((MyViewHolder) holder).author_add_one.setVisibility(View.GONE);
//                        ((MyViewHolder) holder).author_add_two.setVisibility(View.VISIBLE);
//                        ((MyViewHolder) holder).author_add_three.setVisibility(View.GONE);
//                        ((MyViewHolder) holder).author_add_four.setVisibility(View.GONE);
//                        MobileAds.initialize(mContext, String.valueOf(R.string.add_app_id));
//
//                        AdRequest ad_req = new AdRequest.Builder().build();
//                        ((MyViewHolder) holder).author_add_two.loadAd(ad_req);
//
//                    }
//
//
//                    if (UserDetail.current_add.equals("three")) {
//
//                        add_number = 3;
//                        ((MyViewHolder) holder).author_add_parent_linear.setVisibility(View.VISIBLE);
//                        ((MyViewHolder) holder).author_add_one.setVisibility(View.GONE);
//                        ((MyViewHolder) holder).author_add_two.setVisibility(View.GONE);
//                        ((MyViewHolder) holder).author_add_three.setVisibility(View.VISIBLE);
//                        ((MyViewHolder) holder).author_add_four.setVisibility(View.GONE);
//                        MobileAds.initialize(mContext, String.valueOf(R.string.add_app_id));
//
//                        AdRequest ad_req = new AdRequest.Builder().build();
//                        ((MyViewHolder) holder).author_add_three.loadAd(ad_req);
//
//                    }
//
//
//                    if (UserDetail.current_add.equals("four")) {
//
//                        add_number = 4;
//
//                        ((MyViewHolder) holder).author_add_parent_linear.setVisibility(View.VISIBLE);
//                        ((MyViewHolder) holder).author_add_one.setVisibility(View.GONE);
//                        ((MyViewHolder) holder).author_add_two.setVisibility(View.GONE);
//                        ((MyViewHolder) holder).author_add_three.setVisibility(View.GONE);
//                        ((MyViewHolder) holder).author_add_four.setVisibility(View.VISIBLE);
//                        MobileAds.initialize(mContext, String.valueOf(R.string.add_app_id));
//
//                        AdRequest ad_req = new AdRequest.Builder().build();
//                        ((MyViewHolder) holder).author_add_four.loadAd(ad_req);
//
//                    }
//
//                    if (add_number == 1) {
//                        UserDetail.current_add = "two";
//
//                    }
//
//
//                    if (add_number == 2) {
//                        UserDetail.current_add = "three";
//
//                    }
//
//
//                    if (add_number == 3) {
//                        UserDetail.current_add = "four";
//
//                    }
//
//
//                    if (add_number == 4) {
//                        UserDetail.current_add = "one";
//
//                    }
//
//
//                } else {
//                    ((MyViewHolder) holder).author_add_parent_linear.setVisibility(View.GONE);
//                    ((MyViewHolder) holder).author_add_parent_linear.setVisibility(View.GONE);
//                    ((MyViewHolder) holder).author_add_two.setVisibility(View.GONE);
//                    ((MyViewHolder) holder).author_add_three.setVisibility(View.GONE);
//                    ((MyViewHolder) holder).author_add_four.setVisibility(View.GONE);
//                }
//
//            } else {
//                ((MyViewHolder) holder).author_add_parent_linear.setVisibility(View.GONE);
//                ((MyViewHolder) holder).author_add_one.setVisibility(View.GONE);
//                ((MyViewHolder) holder).author_add_two.setVisibility(View.GONE);
//                ((MyViewHolder) holder).author_add_three.setVisibility(View.GONE);
//                ((MyViewHolder) holder).author_add_four.setVisibility(View.GONE);
//            }

//        load add

            ((MyViewHolder) holder).author_profile_name.setText(feedList.get(position).getUser_full_name());

            ((MyViewHolder) holder).profile_img_rel.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

            if (!feedList.get(position).getUser_dp().isEmpty()) {
                Picasso.with(mContext)
                        .load(Constants.URL.BASE_URL + feedList.get(position).getUser_dp())
                        .into(((MyViewHolder) holder).author_profile_img);
            }

            // set time //////////////////////////

            ((MyViewHolder) holder).author_date.setVisibility(View.GONE);

            //  boom   2018-04-04 08:08:51
            //  pos    2018-04-06T09:48:40.540Z

//            String tim = feedList.get(position).getCreatedAt();
//            String dat = tim.substring(0, 10);
//            String time = tim.substring(11, 19);
//            String fin = dat + " " + time;
//
//            SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
//            dff.setTimeZone(TimeZone.getTimeZone("UTC"));
//            Date date = null;
//
//            try {
//                date = dff.parse(fin);
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//            dff.setTimeZone(TimeZone.getDefault());
//            String formattedDatee = dff.format(date);
//            fin = formattedDatee;
//
//
//            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//
//            String final_date;
//            SimpleDateFormat ddf = new SimpleDateFormat("dd-MMM-yyyy");
//
//            Calendar calendar = Calendar.getInstance();
//            calendar.setTime(date);
//            final_date = ddf.format(calendar.getTime());
//
//            try {
//                Date commentOldDate = dateFormat.parse(fin);
//
//                Calendar c = Calendar.getInstance();
//
//                SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                String formattedDate = df.format(c.getTime());
//
//                Date currentDate = dateFormat.parse(formattedDate);
//
//                long diff = currentDate.getTime() - commentOldDate.getTime();
//
//                long days = diff / (24 * 60 * 60 * 1000);
//                diff -= days * (24 * 60 * 60 * 1000);
//
//                long hours = diff / (60 * 60 * 1000);
//                diff -= hours * (60 * 60 * 1000);
//
//                long minutes = diff / (60 * 1000);
//                diff -= minutes * (60 * 1000);
//
//                long seconds = diff / 1000;
//
//
//                if (days > 30) {
//
//                    ((MyViewHolder) holder).author_date.setText(final_date);
//
//
//                } else {
//
//                    if (days >= 28) {
//                        ((MyViewHolder) holder).author_date.setText("4 Weeks ago");
//
//                    } else {
//                        if (days >= 21) {
//                            ((MyViewHolder) holder).author_date.setText("3 Weeks ago");
//
//                        } else {
//                            if (days >= 14) {
//                                ((MyViewHolder) holder).author_date.setText("2 Weeks ago");
//
//                            } else {
//                                if (days >= 7) {
//                                    ((MyViewHolder) holder).author_date.setText("1 Week ago");
//
//                                } else {
//
//
//                                    if (days == 1) {
//                                        ((MyViewHolder) holder).author_date.setText(days + " Day ago");
//                                    } else {
//                                        if (days > 1) {
//                                            ((MyViewHolder) holder).author_date.setText(days + " Days ago");
//
//                                        } else {
//                                            if (hours == 1) {
//                                                ((MyViewHolder) holder).author_date.setText(hours + " Hour ago");
//                                            } else {
//                                                if (hours > 1) {
//                                                    ((MyViewHolder) holder).author_date.setText(hours + " Hours ago");
//
//                                                } else {
//                                                    if (minutes == 1) {
//                                                        ((MyViewHolder) holder).author_date.setText(minutes + " Minute ago");
//
//                                                    } else {
//                                                        if (minutes > 1) {
//                                                            ((MyViewHolder) holder).author_date.setText(minutes + " Minutes ago");
//
//                                                        } else {
//                                                            if (seconds < 0) {
//                                                                ((MyViewHolder) holder).author_date.setText(" Now");
//
//                                                            } else {
//                                                                ((MyViewHolder) holder).author_date.setText(seconds + " Seconds ago");
//                                                            }
//                                                        }
//                                                    }
//                                                }
//
//                                            }
//
//                                        }
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }

            // set time //////////////////////////

            if (feedList.get(position).isPrayed_by_me()) {

                ((MyViewHolder) holder).author_like_heart_img.setImageResource(R.drawable.pray_filled);
                ((MyViewHolder) holder).pray_txt.setText("Prayed");
                ((MyViewHolder) holder).pray_txt.setTextColor(Color.parseColor("#109d58"));
            } else {
                ((MyViewHolder) holder).author_like_heart_img.setImageResource(R.drawable.pray_black);
            }
            if (position == 36) {
                int a = 45;
            }

            String like_count = String.valueOf(feedList.get(position).getLiked_count());
            String comment_count = String.valueOf(feedList.get(position).getComments_count());
            int chk_like_count, chk_comment_count;
            chk_like_count = Integer.parseInt(like_count);
            chk_comment_count = Integer.parseInt(comment_count);

            if (chk_like_count == 0 && chk_comment_count == 0) {

                ((MyViewHolder) holder).author_count_linear_parent.setVisibility(View.GONE);
                ((MyViewHolder) holder).author_count_below_line_lin.setVisibility(View.GONE);


            } else {
                ((MyViewHolder) holder).author_count_linear_parent.setVisibility(View.VISIBLE);
                ((MyViewHolder) holder).author_count_below_line_lin.setVisibility(View.VISIBLE);

                if (chk_like_count == 0) {

                    ((MyViewHolder) holder).author_heart_count_img_rel.setVisibility(View.GONE);
                    ((MyViewHolder) holder).author_heart_count_rel.setVisibility(View.GONE);
                } else {
                    ((MyViewHolder) holder).author_heart_count_img_rel.setVisibility(View.VISIBLE);
                    ((MyViewHolder) holder).author_heart_count_rel.setVisibility(View.VISIBLE);
                    if (like_count.equals("1")){
                        ((MyViewHolder) holder).author_like_count.setText(like_count + " person prayed");
                    }else {
                        ((MyViewHolder) holder).author_like_count.setText(like_count + " persons prayed");
                    }


                }

                if (chk_comment_count == 0) {
                    ((MyViewHolder) holder).author_comment_count_img_rel.setVisibility(View.GONE);
                    ((MyViewHolder) holder).author_comment_count_rel.setVisibility(View.GONE);

                } else {
                    ((MyViewHolder) holder).author_comment_count_img_rel.setVisibility(View.VISIBLE);
                    ((MyViewHolder) holder).author_comment_count_rel.setVisibility(View.VISIBLE);
                    ((MyViewHolder) holder).author_comment_count.setText(comment_count);
                }
            }

                ((MyViewHolder) holder).author_des.setVisibility(View.VISIBLE);
                ((MyViewHolder) holder).author_des.setText(feedList.get(position).getText());


            // like button action

            ((MyViewHolder) holder).author_like_heart_rel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    ((MyViewHolder) holder).author_like_heart_rel.setEnabled(false);


                    if(!feedList.get(position).isPrayed_by_me()){


                        feedList.get(position).setPrayed_by_me(true);
//                    bounce animation------------------------------------------------------

                        final Animation myAnim = AnimationUtils.loadAnimation(mContext, R.anim.bounce);
                        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.15, 25);
                        myAnim.setInterpolator(interpolator);
                        ((MyViewHolder) holder).author_like_heart_img.startAnimation(myAnim);
                        //                    bounce animation------------------------------------------------------


                        ((MyViewHolder) holder).author_like_heart_img.setImageResource(R.drawable.pray_filled);
                        ((MyViewHolder) holder).pray_txt.setText("Prayed");
                        ((MyViewHolder) holder).pray_txt.setTextColor(Color.parseColor("#109d58"));

                        feedList.get(position).setLiked_count(Integer.parseInt(String.valueOf(Integer.parseInt(String.valueOf(feedList.get(position).getLiked_count())) + 1)));
                        ((MyViewHolder) holder).author_like_count.setText(String.valueOf(feedList.get(position).getLiked_count()));
                        int like_count=feedList.get(position).getLiked_count();
                        int comment_count=feedList.get(position).getComments_count();

                        if(like_count == 0 && comment_count == 0 ){
                            ((MyViewHolder) holder).author_count_linear_parent.setVisibility(View.GONE);
                            ((MyViewHolder) holder).author_count_below_line_lin.setVisibility(View.GONE);
                        }
                        else {
                            ((MyViewHolder) holder).author_count_linear_parent.setVisibility(View.VISIBLE);
                            ((MyViewHolder) holder).author_count_below_line_lin.setVisibility(View.VISIBLE);

                            if (like_count == 0) {
                                ((MyViewHolder) holder).author_heart_count_img_rel.setVisibility(View.GONE);
                                ((MyViewHolder) holder).author_heart_count_rel.setVisibility(View.GONE);
                            } else {
                                ((MyViewHolder) holder).author_heart_count_img_rel.setVisibility(View.VISIBLE);
                                ((MyViewHolder) holder).author_heart_count_rel.setVisibility(View.VISIBLE);
                                if (like_count == 1){
                                    ((MyViewHolder) holder).author_like_count.setText(String.valueOf(like_count) + " person prayed");
                                }else {
                                    ((MyViewHolder) holder).author_like_count.setText(String.valueOf(like_count) + " persons prayed");
                                }


                            }

                        }

//                    hit api ---------------------------------------------------

                        SharedPreferences mPrefs = mContext.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                        String token = mPrefs.getString("USER_TOKEN", "");


                        Map<String, String> postParam = new HashMap<String, String>();


                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("x-sh-auth", token);

                        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.PRAYED+feedList.get(position).getPray_id(), mContext, postParam, headers, new ServerCallback() {
                            @Override
                            public void onSuccess(JSONObject result, String ERROR) {

                                if (ERROR.isEmpty()) {

                                    try {


                                        JSONObject jsonObject = new JSONObject(String.valueOf(result));

                                        int code = jsonObject.getInt("code");
                                        if (code == 200) {


                                            feedList.get(position).setPrayed_by_me(true);

                                        }


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }

                                }

                            }
                        });

//                    hit api ---------------------------------------------------

                    }
                }
            });

            // like button action

// feed_img action.........................

            ((MyViewHolder) holder).author_des.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent inn = new Intent(mContext, PrayDetailedActivity.class);
                    inn.putExtra("KEYBOADR_STATUS", "PrayText");
                    inn.putExtra("COMING", "detail");
                    inn.putExtra("Feed_id", feedList.get(position).getPray_id());
                    inn.putExtra("Notification", "false");
                    // for refresh data in random fragment on resume
                    PrayFragment.refreshposition = String.valueOf(position);
                    mContext.startActivity(inn);
                }
            });

            // feed_img action.........................

            // comment_rel action.........................

            ((MyViewHolder) holder).author_comment_rel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent innn = new Intent(mContext, PrayDetailedActivity.class);
                    innn.putExtra("KEYBOADR_STATUS", "open_keyboard");
                    innn.putExtra("COMING", "detail");
                    innn.putExtra("Feed_id", feedList.get(position).getPray_id());
                    innn.putExtra("Notification", "false");

                    // for refresh data in random fragment on resume
                    PrayFragment.refreshposition = String.valueOf(position);

                    mContext.startActivity(innn);
                }
            });
            ((MyViewHolder) holder).author_comment_text_rel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent innn = new Intent(mContext, PrayDetailedActivity.class);
                    innn.putExtra("KEYBOADR_STATUS", "open_keyboard");
                    innn.putExtra("COMING", "detail");
                    innn.putExtra("Feed_id", feedList.get(position).getPray_id());
                    innn.putExtra("Notification", "false");

                    // for refresh data in random fragment on resume
                    PrayFragment.refreshposition = String.valueOf(position);

                    mContext.startActivity(innn);
                }
            });

            // comment_rel action.........................

            // share action

            ((MyViewHolder) holder).author_share_rel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }
            });

            ((MyViewHolder) holder).author_share_text_rel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                }
            });

            // share action

            SharedPreferences sp = mContext.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
            USER_ID = sp.getString("USER_ID", "");

            if (feedList.get(position).getUser_id().equals(USER_ID)){
                ((MyViewHolder) holder).author_feed_dots.setVisibility(View.VISIBLE);
            }else {
                ((MyViewHolder) holder).author_feed_dots.setVisibility(View.GONE);
            }

            ((MyViewHolder) holder).author_feed_dots.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (feedList.get(position).getUser_id().equals(USER_ID)) {

                        UserDetail.refreshposition = String.valueOf(position);
                        PopupMenu menu = new PopupMenu(mContext, ((MyViewHolder) holder).author_feed_dots);
                        menu.getMenuInflater().inflate(R.menu.pray_sub_menu, menu.getMenu());

                        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                if (item.getTitle().equals("Edit")) {

                                    Intent intent = new Intent(mContext, AddPreyActivity.class);
                                    intent.putExtra("ACTIVITY", "praylist");
                                    intent.putExtra("TEXT", feedList.get(position).getText());
                                    intent.putExtra("PRAY_ID", feedList.get(position).getPray_id());
                                    mContext.startActivity(intent);
                                }
                                if (item.getTitle().equals("Delete")) {

                                    // aleert for delete

                                    AlertDialog.Builder mbuilder = new AlertDialog.Builder(mContext);
                                    View mview = LayoutInflater.from(mContext).inflate(R.layout.confirm_delete_dialog, null);
                                    RelativeLayout cancel_rel = mview.findViewById(R.id.cancel_rel);
                                    final RelativeLayout yes_rel = mview.findViewById(R.id.yes_rel);
                                    mbuilder.setView(mview);
                                    final AlertDialog dialog2 = mbuilder.create();
                                    dialog2.setCanceledOnTouchOutside(false);
                                    dialog2.show();

                                    cancel_rel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            dialog2.dismiss();
                                        }
                                    });


                                    yes_rel.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {

                                            // hit api ---------------------------------------------------

                                            yes_rel.setEnabled(false);

                                            SharedPreferences mPrefs = mContext.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                                            String token = mPrefs.getString("USER_TOKEN", "");


                                            Map<String, String> postParam = new HashMap<String, String>();


                                            HashMap<String, String> headers = new HashMap<String, String>();
                                            headers.put("x-sh-auth", token);

                                            ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.DELPRAY + feedList.get(position).getPray_id(), mContext, postParam, headers, new ServerCallback() {
                                                @Override
                                                public void onSuccess(JSONObject result, String ERROR) {

                                                    if (ERROR.isEmpty()) {

                                                        try {


                                                            JSONObject jsonObject = new JSONObject(String.valueOf(result));

                                                            int code = jsonObject.getInt("code");
                                                            if (code == 200) {

                                                                feedList.remove(position);
                                                                notifyItemRemoved(position);
                                                                notifyItemRangeChanged(position, feedList.size());
                                                                yes_rel.setEnabled(true);
                                                                dialog2.dismiss();

                                                            }

                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                            yes_rel.setEnabled(true);
                                                        }

                                                    }else {
                                                        yes_rel.setEnabled(true);
                                                    }

                                                }
                                            });

//      hit api ---------------------------------------------------
                                        }
                                    });

                                }

                                return false;
                            }
                        });
                        menu.show();
                    }

                }
            });


        }
    }





    public void addfeed(ArrayList<Prays> feed){

        int v=getItemCount();

        for(int a=0;a<feed.size(); a++){
            feedList.add(feed.get(a));
        }
//            notifyItemChanged(getItemCount());
        notifyItemInserted(getItemCount());

        int fv=getItemCount();
//        notifyDataSetChanged();

    }

    public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {

                ViewTreeObserver obs = tv.getViewTreeObserver();
                obs.removeGlobalOnLayoutListener(this);
                if (maxLine == 0) {
                    int lineEndIndex = tv.getLayout().getLineEnd(0);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                    int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                } else {
                    int lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                    String text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                    tv.setText(text);
                    tv.setMovementMethod(LinkMovementMethod.getInstance());
                    tv.setText(
                            addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, lineEndIndex, expandText,
                                    viewMore), TextView.BufferType.SPANNABLE);
                }
            }
        });

    }

    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
                                                                            final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {


            ssb.setSpan(new MySpannable(false){
                @Override
                public void onClick(View widget) {
                    if (viewMore) {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, -1, "See Less", false);
                    } else {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, 2, "... See More", true);
                    }
                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;

    }



}
