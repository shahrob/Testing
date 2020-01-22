package com.positivevibes.app.Adapters;

import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.positivevibes.app.Activity.AddPreyActivity;
import com.positivevibes.app.Activity.AuthorDetail;
import com.positivevibes.app.Activity.FeedDetailedActivity;
import com.positivevibes.app.Activity.FirstActivity;
import com.positivevibes.app.Activity.NavigationDrawerActivity;
import com.positivevibes.app.Activity.PrayDetailedActivity;
import com.positivevibes.app.Activity.ReportToQuote;
import com.positivevibes.app.Activity.ReportsList;
import com.positivevibes.app.Activity.UserDetail;
import com.positivevibes.app.ApiStructure.ApiModelClass;
import com.positivevibes.app.ApiStructure.Constants;
import com.positivevibes.app.ApiStructure.ServerCallback;
import com.positivevibes.app.Fragments.AllFragment;
import com.positivevibes.app.Fragments.PrayFragment;
import com.positivevibes.app.Fragments.RandomFeedFragment;
import com.positivevibes.app.Models.AllListModel.AllList;
import com.positivevibes.app.Models.FeedListModel.CategoriesList;
import com.positivevibes.app.Models.FeedListModel.Feeds;
import com.positivevibes.app.Models.MyBounceInterpolator;
import com.positivevibes.app.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class AllFeedsAdapter extends RecyclerView.Adapter<AllFeedsAdapter.MyViewHolder> {

    private Context mContext;
    private List<AllList> feedList;
    FragmentTransaction transaction;
    int like_img_status=0;
    int poss;
    ProgressDialog dialog;
    String p_author_id;

    LinearLayoutManager mLayoutManager;
    private CategoryListAdapter cate_adapter;
    ArrayList<CategoriesList> category_reference;
    String USER_ID;
    View v;

    public static boolean COMING_FROM_FEED_ADAPTER ;
    public static String PROFILE_ID;




    public class MyViewHolder extends RecyclerView.ViewHolder {



        TextView comment_one_text,post,feed_date,profile_name,feed_des,like_count,comment_count,comment_one_like_count
                ,comment_one_time,comment_two_text,comment_two_like_count,comment_two_time, like_pray_fix;
        RelativeLayout share_rel,like_heart_rel,comment_rel,heart_count_img_rel,comment_one_heart_rel,heart_count_rel,
                comment_count_img_rel,comment_count_rel,comment_text_rel,share_text_rel, sub_menu_doted, feed_img_container, profile_img_rel;
        ImageView like_heart_img,feed_img,comment_one_heart_img,comment_two_heart_img, likecount_img_fix;
        LinearLayout add_parent_linear,comments_block,card_feed_parent_rel,count_linear_parent,count_below_line_lin, admin_linear;
        CircleImageView feed_profile_img,comment_one_pic,comment_two_pic;
        AdView add_one,add_two,add_three,add_four;
        RecyclerView feed_category_list;

        public MyViewHolder(View view) {
            super(view);

            comment_one_time = (TextView) view.findViewById(R.id.comment_one_time);
            admin_linear =(LinearLayout) view.findViewById(R.id.admin_linear);
            comment_one_like_count = (TextView) view.findViewById(R.id.comment_one_like_count);
            comment_one_text = (TextView) view.findViewById(R.id.coment_one_text);
            feed_des = (TextView) view.findViewById(R.id.feed_des);
            like_count = (TextView) view.findViewById(R.id.like_count);
            comment_count = (TextView) view.findViewById(R.id.comment_count);
            post = (TextView) view.findViewById(R.id.post);
            card_feed_parent_rel = (LinearLayout) view.findViewById(R.id.card_feed_parent_rel);
            share_rel = (RelativeLayout) view.findViewById(R.id.share_rel);
            comment_rel = (RelativeLayout) view.findViewById(R.id.comment_rel);
            comment_one_heart_rel = (RelativeLayout) view.findViewById(R.id.comment_one_heart_rel);
            like_heart_rel = (RelativeLayout) view.findViewById(R.id.like_heart_rel);
            like_heart_img = (ImageView) view.findViewById(R.id.like_heart_img);
            feed_img = (ImageView) view.findViewById(R.id.feed_img);
            comment_one_heart_img = (ImageView) view.findViewById(R.id.comment_one_heart_img);
            feed_profile_img=view.findViewById(R.id.feed_profile_img);
            comment_one_pic=view.findViewById(R.id.comment_one_pic);
            comments_block=view.findViewById(R.id.comments_block);
            profile_name=view.findViewById(R.id.profile_name);
            feed_date=view.findViewById(R.id.feed_date);
            add_parent_linear=view.findViewById(R.id.add_parent_linear);
            add_one=view.findViewById(R.id.add_one);
            add_two=view.findViewById(R.id.add_two);
            add_three=view.findViewById(R.id.add_three);
            add_four=view.findViewById(R.id.add_four);
            count_linear_parent=view.findViewById(R.id.count_linear_parent);
            heart_count_img_rel=view.findViewById(R.id.heart_count_img_rel);
            heart_count_rel=view.findViewById(R.id.heart_count_rel);
            comment_count_img_rel=view.findViewById(R.id.comment_count_img_rel);
            comment_count_rel=view.findViewById(R.id.comment_count_rel);
            count_below_line_lin=view.findViewById(R.id.count_below_line_lin);
            comment_text_rel=view.findViewById(R.id.comment_text_rel);
            share_text_rel=view.findViewById(R.id.share_text_rel);
            feed_category_list = view.findViewById(R.id.feed_category_list);
            sub_menu_doted = view.findViewById(R.id.sub_menu_doted);
            feed_img_container = view.findViewById(R.id.third_row_conatiner);
            like_pray_fix = view.findViewById(R.id.like_pray_fix);
            likecount_img_fix = view.findViewById(R.id.likecount_img_fix);
            profile_img_rel = view.findViewById(R.id.profile_img_rel);

        }
    }

    public AllFeedsAdapter(Context mContext, List<AllList> feedList, FragmentTransaction transaction) {
        this.mContext = mContext;
        this.feedList=feedList;
        this.transaction=transaction;

    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_all_feed, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

//        load add

//        if (  position != 0) {
//
//            if(position == 1 || position % 6 == 0) {
//
//                int add_number=0;
//
//                if(AllFragment.current_add.equals("one")){
//                    add_number=1;
//
//                    holder.add_parent_linear.setVisibility(View.VISIBLE);
//                    holder.add_one.setVisibility(View.VISIBLE);
//                    holder.add_two.setVisibility(View.GONE);
//                    holder.add_three.setVisibility(View.GONE);
//                    holder.add_four.setVisibility(View.GONE);
//                    MobileAds.initialize(mContext, String.valueOf(R.string.add_app_id));
//
//                    AdRequest ad_req = new AdRequest.Builder().build();
//                    holder.add_one.loadAd(ad_req);
//                }
//
//                if(AllFragment.current_add.equals("two")){
//                    add_number=2;
//
//                    holder.add_parent_linear.setVisibility(View.VISIBLE);
//                    holder.add_one.setVisibility(View.GONE);
//                    holder.add_two.setVisibility(View.VISIBLE);
//                    holder.add_three.setVisibility(View.GONE);
//                    holder.add_four.setVisibility(View.GONE);
//                    MobileAds.initialize(mContext, String.valueOf(R.string.add_app_id));
//
//                    AdRequest ad_req = new AdRequest.Builder().build();
//                    holder.add_two.loadAd(ad_req);
//                }
//
//                if(AllFragment.current_add.equals("three")){
//
//                    add_number=3;
//                    holder.add_parent_linear.setVisibility(View.VISIBLE);
//                    holder.add_one.setVisibility(View.GONE);
//                    holder.add_two.setVisibility(View.GONE);
//                    holder.add_three.setVisibility(View.VISIBLE);
//                    holder.add_four.setVisibility(View.GONE);
//                    MobileAds.initialize(mContext, String.valueOf(R.string.add_app_id));
//
//                    AdRequest ad_req = new AdRequest.Builder().build();
//                    holder.add_three.loadAd(ad_req);
//                }
//
//                if(AllFragment.current_add.equals("four")){
//
//                    add_number=4;
//
//                    holder.add_parent_linear.setVisibility(View.VISIBLE);
//                    holder.add_one.setVisibility(View.GONE);
//                    holder.add_two.setVisibility(View.GONE);
//                    holder.add_three.setVisibility(View.GONE);
//                    holder.add_four.setVisibility(View.VISIBLE);
//                    MobileAds.initialize(mContext, String.valueOf(R.string.add_app_id));
//
//                    AdRequest ad_req = new AdRequest.Builder().build();
//                    holder.add_four.loadAd(ad_req);
//                }
//
//                if(add_number == 1){
//                    AllFragment.current_add="two";
//                }
//
//                if(add_number == 2){
//                    AllFragment.current_add="three";
//                }
//
//                if(add_number == 3){
//                    AllFragment.current_add="four";
//                }
//
//                if(add_number == 4){
//                    AllFragment.current_add="one";
//                }
//            }
//            else{
//                holder.add_parent_linear.setVisibility(View.GONE);
//                holder.add_one.setVisibility(View.GONE);
//                holder.add_two.setVisibility(View.GONE);
//                holder.add_three.setVisibility(View.GONE);
//                holder.add_four.setVisibility(View.GONE);
//            }
//        }
//        else{
//            holder.add_parent_linear.setVisibility(View.GONE);
//            holder.add_one.setVisibility(View.GONE);
//            holder.add_two.setVisibility(View.GONE);
//            holder.add_three.setVisibility(View.GONE);
//            holder.add_four.setVisibility(View.GONE);
//        }

//        load add

//        holder.feed_progress.setVisibility(View.GONE);

        poss=holder.getAdapterPosition();
//        poss=position;

        holder.profile_img_rel.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        if (feedList.get(position).isAdmin_quote()){

            holder.admin_linear.setVisibility(View.VISIBLE);
            holder.profile_name.setText(feedList.get(position).getAuther_name());

            Picasso.with(mContext)
                    .load(Constants.URL.BASE_URL + feedList.get(position).getAuther_img())
                    .into(holder.feed_profile_img);

        }else {

            holder.admin_linear.setVisibility(View.GONE);
            holder.profile_name.setText(feedList.get(position).getUser_fullname());

            if (!feedList.get(position).getUser_dp().isEmpty()){
                Picasso.with(mContext)
                        .load(Constants.URL.BASE_URL + feedList.get(position).getUser_dp())
                        .into(holder.feed_profile_img);
            }else {
                holder.feed_profile_img.setImageResource(R.drawable.profile_icon);
            }

        }

        holder.profile_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (feedList.get(position).isAdmin_quote()){
                    if (feedList.get(position).isAuthor_check()) {
                        Intent intent = new Intent(mContext, AuthorDetail.class);
                        intent.putExtra("AUTH_ID", feedList.get(position).getAuther_id());
//                    p_author_id = feedList.get(position).getAuther_id();
                        mContext.startActivity(intent);
                    }
                }else {
                        Intent intent = new Intent(mContext, UserDetail.class);
                        intent.putExtra("AUTH_ID", feedList.get(position).getUser_id());
//                    p_author_id = feedList.get(position).getAuther_id();
                        mContext.startActivity(intent);
                }

            }
        });


        holder.feed_profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (feedList.get(position).isAdmin_quote()) {

                    if (feedList.get(position).isAuthor_check()) {
                        Intent intent = new Intent(mContext, AuthorDetail.class);
                        intent.putExtra("AUTH_ID", feedList.get(position).getAuther_id());
                        mContext.startActivity(intent);
                    }
                }else {
                        Intent intent = new Intent(mContext, UserDetail.class);
                        intent.putExtra("AUTH_ID", feedList.get(position).getUser_id());
//                    p_author_id = feedList.get(position).getAuther_id();
                        mContext.startActivity(intent);
                }
            }
        });

        if (feedList.get(position).isAdmin_quote()){
            // Category List

            category_reference = feedList.get(position).getCategoriesArrayList();
            mLayoutManager = new LinearLayoutManager(mContext);
            mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            holder.feed_category_list.getItemAnimator().endAnimations();
            cate_adapter = new CategoryListAdapter(mContext, category_reference);

            holder.feed_category_list.setAdapter(cate_adapter);
            holder.feed_category_list.setLayoutManager(mLayoutManager);

            // Category List
        }else {
            holder.feed_category_list.setVisibility(View.GONE);
        }


        // set time //////////////////////////

        holder.feed_date.setVisibility(View.GONE);

        //  boom   2018-04-04 08:08:51
        //  pos    2018-04-06T09:48:40.540Z

//        String tim = feedList.get(position).getCreatedAt();
//        String dat=tim.substring(0,10);
//        String time=tim.substring(11,19);
//        String fin=dat+" "+time;
//
//        SimpleDateFormat dff = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH);
//        dff.setTimeZone(TimeZone.getTimeZone("UTC"));
//        Date date = null;
//
//        try {
//            date = dff.parse(fin);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        dff.setTimeZone(TimeZone.getDefault());
//        String formattedDatee = dff.format(date);
//        fin=formattedDatee;
//
//
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//
//        String final_date;
//        SimpleDateFormat ddf = new SimpleDateFormat("dd-MMM-yyyy");
//
//        Calendar calendar = Calendar.getInstance();
//        calendar.setTime(date);
//        final_date = ddf.format(calendar.getTime());
//
//        try {
//            Date commentOldDate = dateFormat.parse(fin);
//
//            Calendar c = Calendar.getInstance();
//
//            SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//            String formattedDate = df.format(c.getTime());
//
//            Date currentDate = dateFormat.parse(formattedDate);
//
//            long diff = currentDate.getTime() - commentOldDate.getTime();
//
//            long days = diff / (24 * 60 * 60 * 1000);
//            diff -= days * (24 * 60 * 60 * 1000);
//
//            long hours = diff / (60 * 60 * 1000);
//            diff -= hours * (60 * 60 * 1000);
//
//            long minutes = diff / (60 * 1000);
//            diff -= minutes * (60 * 1000);
//
//            long seconds = diff / 1000;
//
//
//            if (days > 30) {
//
//                holder.feed_date.setText(final_date);
//
//
//            } else {
//
//                if (days >= 28) {
//                    holder.feed_date.setText("4 Weeks ago");
//
//                } else {
//                    if (days >= 21) {
//                        holder.feed_date.setText("3 Weeks ago");
//
//                    } else {
//                        if (days >= 14) {
//                            holder.feed_date.setText("2 Weeks ago");
//
//                        } else {
//                            if (days >= 7) {
//                                holder.feed_date.setText("1 Week ago");
//
//                            } else {
//
//
//                            if (days == 1) {
//                                holder.feed_date.setText(days + " Day ago");
//                            } else {
//                                if (days > 1) {
//                                    holder.feed_date.setText(days + " Days ago");
//
//                                } else {
//                                    if (hours == 1) {
//                                        holder.feed_date.setText(hours + " Hour ago");
//                                    } else {
//                                        if (hours > 1) {
//                                            holder.feed_date.setText(hours + " Hours ago");
//
//                                        } else {
//                                            if (minutes == 1) {
//                                                holder.feed_date.setText(minutes + " Minute ago");
//
//                                            } else {
//                                                if (minutes > 1) {
//                                                    holder.feed_date.setText(minutes + " Minutes ago");
//
//                                                } else {
//                                                    if (seconds < 0) {
//                                                        holder.feed_date.setText(" Now");
//
//                                                    } else {
//                                                        holder.feed_date.setText(seconds + " Seconds ago");
//                                                    }
//                                                }
//                                            }
//                                        }
//
//                                    }
//
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//            }
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }



        // set time //////////////////////////


        if(feedList.get(position).isLiked_by_me()){

            if (feedList.get(position).isAdmin_quote()){
                holder.like_heart_img.setImageResource(R.drawable.fill_feed_like);
            }else {
                holder.like_heart_img.setImageResource(R.drawable.pray_filled);
                holder.like_pray_fix.setText("Prayed");
                holder.like_pray_fix.setTextColor(Color.parseColor("#109d58"));
            }

        }
        else{

            if (feedList.get(position).isAdmin_quote()){
                holder.like_heart_img.setImageResource(R.drawable.feed_like);

            }else {
                holder.like_heart_img.setImageResource(R.drawable.pray_black);
                holder.like_pray_fix.setText("Pray");
            }

        }

        String like_count= String.valueOf(feedList.get(position).getLiked_count());
        String comment_count= String.valueOf(feedList.get(position).getComments_count());
        int chk_like_count,chk_comment_count;
        chk_like_count= Integer.parseInt(like_count);
        chk_comment_count= Integer.parseInt(comment_count);

        if(chk_like_count == 0 && chk_comment_count == 0){

            holder.count_linear_parent.setVisibility(View.GONE);
            holder.count_below_line_lin.setVisibility(View.GONE);


        }
        else {
            holder.count_linear_parent.setVisibility(View.VISIBLE);
            holder.count_below_line_lin.setVisibility(View.VISIBLE);

            if(chk_like_count == 0){

                holder.heart_count_img_rel.setVisibility(View.GONE);
                holder.heart_count_rel.setVisibility(View.GONE);
            }
            else {
                holder.heart_count_img_rel.setVisibility(View.VISIBLE);
                holder.heart_count_rel.setVisibility(View.VISIBLE);

                if (feedList.get(position).isAdmin_quote()){
                    holder.like_count.setText(like_count);
                }else {
                    holder.likecount_img_fix.setImageResource(R.drawable.pray_filled);

                    if (like_count.equals("1")){
                        holder.like_count.setText(like_count + " person prayed");
                    }else {
                        holder.like_count.setText(like_count + " persons prayed");
                    }

                }


            }

            if(chk_comment_count == 0){
                holder.comment_count_img_rel.setVisibility(View.GONE);
                holder.comment_count_rel.setVisibility(View.GONE);

            }
            else {
                holder.comment_count_img_rel.setVisibility(View.VISIBLE);
                holder.comment_count_rel.setVisibility(View.VISIBLE);
                holder.comment_count.setText(comment_count);
            }
        }


        if (feedList.get(position).isAdmin_quote()){
            if(feedList.get(position).getTitle().isEmpty()){
                holder.feed_des.setVisibility(View.GONE);
            }
            else {
                holder.feed_des.setVisibility(View.VISIBLE);
                holder.feed_des.setText(feedList.get(position).getTitle());

            }
            try {
                if (feedList.get(position).getLarge_image().isEmpty()){
                    holder.feed_img_container.setVisibility(View.GONE);
                }else {
                    Picasso.with(mContext)
                            .load(Constants.URL.BASE_URL + feedList.get(position).getLarge_image())
                            .into(holder.feed_img);
                }
            }
            catch (Exception e) {

            }
        }else {
            if(feedList.get(position).getTitle().isEmpty()){
                holder.feed_des.setVisibility(View.GONE);
            }
            else {
                holder.feed_des.setVisibility(View.VISIBLE);
                holder.feed_des.setText(feedList.get(position).getTitle());

            }
            holder.feed_img_container.setVisibility(View.GONE);
        }

        // like button action

        if (feedList.get(position).isAdmin_quote()){

            holder.like_heart_rel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    holder.like_heart_rel.setEnabled(false);


                    if(!feedList.get(position).isLiked_by_me()){


                        feedList.get(position).setLiked_by_me(true);
//                    bounce animation------------------------------------------------------

                        final Animation myAnim = AnimationUtils.loadAnimation(mContext, R.anim.bounce);
                        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.15, 25);
                        myAnim.setInterpolator(interpolator);
                        holder.like_heart_img.startAnimation(myAnim);
                        //                    bounce animation------------------------------------------------------

                        like_img_status=1;

                        holder.like_heart_img.setImageResource(R.drawable.fill_feed_like);

                        feedList.get(position).setLiked_count(Integer.parseInt(String.valueOf(Integer.parseInt(String.valueOf(feedList.get(position).getLiked_count())) + 1)));
                        holder.like_count.setText(String.valueOf(feedList.get(position).getLiked_count()));
                        int like_count=feedList.get(position).getLiked_count();
                        int comment_count=feedList.get(position).getComments_count();

                        if(like_count == 0 && comment_count == 0 ){
                            holder.count_linear_parent.setVisibility(View.GONE);
                            holder.count_below_line_lin.setVisibility(View.GONE);
                        }
                        else {

                            holder.count_linear_parent.setVisibility(View.VISIBLE);
                            holder.count_below_line_lin.setVisibility(View.VISIBLE);

                            if (comment_count == 0){
                                holder.comment_count_img_rel.setVisibility(View.GONE);
                                holder.comment_count_rel.setVisibility(View.GONE);
                            }else {
                                holder.comment_count_img_rel.setVisibility(View.VISIBLE);
                                holder.comment_count_rel.setVisibility(View.VISIBLE);
                            }
                            if (like_count == 0) {
                                holder.heart_count_img_rel.setVisibility(View.GONE);
                                holder.heart_count_rel.setVisibility(View.GONE);
                            } else {
                                holder.heart_count_img_rel.setVisibility(View.VISIBLE);
                                holder.heart_count_rel.setVisibility(View.VISIBLE);


                            }

                        }

//                    hit api ---------------------------------------------------



                        SharedPreferences mPrefs = mContext.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                        String token = mPrefs.getString("USER_TOKEN", "");


                        Map<String, String> postParam = new HashMap<String, String>();


                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("x-sh-auth", token);

                        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.ADD_FAVOURITS+feedList.get(position).get_id(), mContext, postParam, headers, new ServerCallback() {
                            @Override
                            public void onSuccess(JSONObject result, String ERROR) {

                                if (ERROR.isEmpty()) {

                                    try {


                                        JSONObject jsonObject = new JSONObject(String.valueOf(result));

                                        int code = jsonObject.getInt("code");
                                        if (code == 200) {


                                            feedList.get(position).setLiked_by_me(true);


                                            // set counts to  subscribed feed list

//                                        for(int a = 0; a< SubscribedFeedFragment.feedList.size(); a++){
//
//                                            String id=SubscribedFeedFragment.feedList.get(a).get_id();
//                                            if(id.equals(feedList.get(position).get_id())){
//                                                FeedFragment.refresh="refresh";
//                                                SubscribedFeedFragment.feedList.get(a).setLiked_by_me(true);
//                                                SubscribedFeedFragment.feedList.get(a).setLiked_count(SubscribedFeedFragment.feedList.get(a).getLiked_count()+1);
//                                                break;
//                                            }
//
//                                        }

                                            // set counts to  subscribed feed list


                                        }


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    holder.like_heart_rel.setEnabled(true);


                                } else {

                                    holder.like_heart_img.setImageResource(R.drawable.feed_like);
                                    feedList.get(position).setLiked_count(Integer.parseInt(String.valueOf(Integer.parseInt(String.valueOf(feedList.get(position).getLiked_count())) - 1)));
                                    holder.like_count.setText(String.valueOf(feedList.get(position).getLiked_count()));

                                    int like_count=feedList.get(position).getLiked_count();

                                    int comment_count=feedList.get(position).getComments_count();

                                    if(like_count == 0 && comment_count == 0 ){
                                        holder.count_linear_parent.setVisibility(View.GONE);
                                        holder.count_below_line_lin.setVisibility(View.GONE);
                                    }
                                    else {
                                        holder.count_linear_parent.setVisibility(View.VISIBLE);
                                        holder.count_below_line_lin.setVisibility(View.VISIBLE);

                                        if (like_count == 0) {
                                            holder.heart_count_img_rel.setVisibility(View.GONE);
                                            holder.heart_count_rel.setVisibility(View.GONE);
                                        } else {
                                            holder.heart_count_img_rel.setVisibility(View.VISIBLE);
                                            holder.heart_count_rel.setVisibility(View.VISIBLE);


                                        }

                                    }

                                    holder.like_heart_rel.setEnabled(true);

                                    if (ERROR.equals("401")) {

                                        SharedPreferences mPrefs = mContext.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor mEditor = mPrefs.edit();
                                        mEditor.putString("USER_EMAIL", "");
                                        mEditor.putString("USER_PASSWORD", "");
                                        mEditor.putString("USER_TOKEN", "");
                                        mEditor.putString("USER_PIC", "");
                                        mEditor.apply();
                                        Intent intt = new Intent(mContext, FirstActivity.class);
                                        mContext.startActivity(intt);
                                        ((NavigationDrawerActivity)mContext).finish();

                                    } else {

                                        Toast.makeText(mContext, ERROR, Toast.LENGTH_SHORT).show();

                                    }

                                }

                            }
                        });

//                    hit api ---------------------------------------------------

                    }
                    else{

                        feedList.get(position).setLiked_by_me(false);


                        like_img_status=0;

                        //                    bounce animation------------------------------------------------------
                        final Animation myAnim = AnimationUtils.loadAnimation(mContext, R.anim.bounce);
                        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.15, 25);
                        myAnim.setInterpolator(interpolator);
                        holder.like_heart_img.startAnimation(myAnim);

                        //                    bounce animation------------------------------------------------------

                        holder.like_heart_img.setImageResource(R.drawable.feed_like);
                        feedList.get(position).setLiked_count(Integer.parseInt(String.valueOf(Integer.parseInt(String.valueOf(feedList.get(position).getLiked_count())) - 1)));
                        holder.like_count.setText(String.valueOf(feedList.get(position).getLiked_count()));

                        int like_count=feedList.get(position).getLiked_count();
                        int comment_count=feedList.get(position).getComments_count();

                        if(like_count == 0 && comment_count == 0 ){
                            holder.count_linear_parent.setVisibility(View.GONE);
                            holder.count_below_line_lin.setVisibility(View.GONE);
                        }
                        else {
                            holder.count_linear_parent.setVisibility(View.VISIBLE);
                            holder.count_below_line_lin.setVisibility(View.VISIBLE);

                            if (like_count == 0) {
                                holder.heart_count_img_rel.setVisibility(View.GONE);
                                holder.heart_count_rel.setVisibility(View.GONE);
                            } else {
                                holder.heart_count_img_rel.setVisibility(View.VISIBLE);
                                holder.heart_count_rel.setVisibility(View.VISIBLE);


                            }
                        }


//                    hit api ---------------------------------------------------



                        SharedPreferences mPrefs = mContext.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                        String token = mPrefs.getString("USER_TOKEN", "");


                        Map<String, String> postParam = new HashMap<String, String>();


                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("x-sh-auth", token);

                        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.REMOVE_FAVOURITS+feedList.get(position).get_id(), mContext, postParam, headers, new ServerCallback() {
                            @Override
                            public void onSuccess(JSONObject result, String ERROR) {

                                if (ERROR.isEmpty()) {

                                    try {


                                        JSONObject jsonObject = new JSONObject(String.valueOf(result));

                                        int code = jsonObject.getInt("code");
                                        if (code == 200) {


                                            feedList.get(position).setLiked_by_me(false);

                                            // set counts to  subscribed feed list

//                                        for(int a = 0; a< SubscribedFeedFragment.feedList.size(); a++){
//
//                                            String id=SubscribedFeedFragment.feedList.get(a).get_id();
//                                            if(id.equals(feedList.get(position).get_id())){
//                                                FeedFragment.refresh="refresh";
//
//                                                SubscribedFeedFragment.feedList.get(a).setLiked_by_me(false);
//                                                SubscribedFeedFragment.feedList.get(a).setLiked_count(SubscribedFeedFragment.feedList.get(a).getLiked_count()-1);
//                                                break;
//                                            }
//
//                                        }

                                            // set counts to  subscribed feed list

                                        }


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    holder.like_heart_rel.setEnabled(true);

                                } else {
                                    holder.like_heart_img.setImageResource(R.drawable.fill_feed_like);

                                    feedList.get(position).setLiked_count(Integer.parseInt(String.valueOf(Integer.parseInt(String.valueOf(feedList.get(position).getLiked_count())) + 1)));
                                    holder.like_count.setText(String.valueOf(feedList.get(position).getLiked_count()));
                                    holder.like_heart_rel.setEnabled(true);
                                    int like_count=feedList.get(position).getLiked_count();

                                    int comment_count=feedList.get(position).getComments_count();

                                    if(like_count == 0 && comment_count == 0 ){
                                        holder.count_linear_parent.setVisibility(View.GONE);
                                        holder.count_below_line_lin.setVisibility(View.GONE);
                                    }
                                    else {
                                        holder.count_linear_parent.setVisibility(View.VISIBLE);
                                        holder.count_below_line_lin.setVisibility(View.VISIBLE);

                                        if (like_count == 0) {
                                            holder.heart_count_img_rel.setVisibility(View.GONE);
                                            holder.heart_count_rel.setVisibility(View.GONE);
                                        } else {
                                            holder.heart_count_img_rel.setVisibility(View.VISIBLE);
                                            holder.heart_count_rel.setVisibility(View.VISIBLE);


                                        }
                                    }
                                    if (ERROR.equals("401")) {

                                        SharedPreferences mPrefs = mContext.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor mEditor = mPrefs.edit();
                                        mEditor.putString("USER_EMAIL", "");
                                        mEditor.putString("USER_PASSWORD", "");
                                        mEditor.putString("USER_TOKEN", "");
                                        mEditor.putString("USER_PIC", "");
                                        mEditor.apply();
                                        Intent intt = new Intent(mContext, FirstActivity.class);
                                        mContext.startActivity(intt);
                                        ((NavigationDrawerActivity)mContext).finish();

                                    } else {

                                        Toast.makeText(mContext, ERROR, Toast.LENGTH_SHORT).show();

                                    }

                                }

                            }
                        });

//                    hit api ---------------------------------------------------

                    }
                }
            });


        }else {

            holder.like_heart_rel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    holder.like_heart_rel.setEnabled(false);


                    if(!feedList.get(position).isLiked_by_me()){


                        feedList.get(position).setLiked_by_me(true);
//                    bounce animation------------------------------------------------------

                        final Animation myAnim = AnimationUtils.loadAnimation(mContext, R.anim.bounce);
                        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.15, 25);
                        myAnim.setInterpolator(interpolator);
                        holder.like_heart_img.startAnimation(myAnim);
                        //                    bounce animation------------------------------------------------------


                        holder.like_heart_img.setImageResource(R.drawable.pray_filled);
                        holder.like_pray_fix.setText("Prayed");
                        holder.like_pray_fix.setTextColor(Color.parseColor("#109d58"));

                        feedList.get(position).setLiked_count(Integer.parseInt(String.valueOf(Integer.parseInt(String.valueOf(feedList.get(position).getLiked_count())) + 1)));
                        holder.like_count.setText(String.valueOf(feedList.get(position).getLiked_count()));
                        int like_count=feedList.get(position).getLiked_count();
                        int comment_count=feedList.get(position).getComments_count();

                        if(like_count == 0 && comment_count == 0 ){
                            holder.count_linear_parent.setVisibility(View.GONE);
                            holder.count_below_line_lin.setVisibility(View.GONE);
                        }else {
                            holder.count_linear_parent.setVisibility(View.VISIBLE);
                            holder.count_below_line_lin.setVisibility(View.VISIBLE);

                            if (comment_count == 0){
                                holder.comment_count_img_rel.setVisibility(View.GONE);
                                holder.comment_count_rel.setVisibility(View.GONE);
                            }else {
                                holder.comment_count_img_rel.setVisibility(View.VISIBLE);
                                holder.comment_count_rel.setVisibility(View.VISIBLE);
                            }
                            if (like_count == 0) {
                                holder.heart_count_img_rel.setVisibility(View.GONE);
                                holder.heart_count_rel.setVisibility(View.GONE);
                            } else {
                                holder.likecount_img_fix.setImageResource(R.drawable.pray_filled);
                                holder.heart_count_img_rel.setVisibility(View.VISIBLE);
                                holder.heart_count_rel.setVisibility(View.VISIBLE);
                                if (like_count == 1){
                                    holder.like_count.setText(String.valueOf(like_count) + " person prayed");
                                }else {
                                    holder.like_count.setText(String.valueOf(like_count) + " persons prayed");
                                }

                            }

                        }

//                    hit api ---------------------------------------------------

                        SharedPreferences mPrefs = mContext.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                        String token = mPrefs.getString("USER_TOKEN", "");


                        Map<String, String> postParam = new HashMap<String, String>();


                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("x-sh-auth", token);

                        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.PRAYED+feedList.get(position).get_id(), mContext, postParam, headers, new ServerCallback() {
                            @Override
                            public void onSuccess(JSONObject result, String ERROR) {

                                if (ERROR.isEmpty()) {

                                    try {


                                        JSONObject jsonObject = new JSONObject(String.valueOf(result));

                                        int code = jsonObject.getInt("code");
                                        if (code == 200) {


                                            feedList.get(position).setLiked_by_me(true);

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

        }


        // like button action

        // feed_img action.........................

        if (feedList.get(position).isAdmin_quote()){

            holder.feed_des.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent inn=new Intent(mContext, FeedDetailedActivity.class);
                    inn.putExtra("KEYBOADR_STATUS", "feed_img");
                    inn.putExtra("Feed_id", feedList.get(position).get_id());
                    inn.putExtra("Notification", "false");
                    // for refresh data in random fragment on resume
                    AllFragment.positionForRefresh= String.valueOf(position);
                    mContext.startActivity(inn);
                }
            });

            holder.feed_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent inn=new Intent(mContext, FeedDetailedActivity.class);
                    inn.putExtra("KEYBOADR_STATUS", "feed_img");
                    inn.putExtra("Feed_id", feedList.get(position).get_id());
                    inn.putExtra("Notification", "false");
                    // for refresh data in random fragment on resume
                    AllFragment.positionForRefresh= String.valueOf(position);
                    mContext.startActivity(inn);
                }
            });

        }else {

            holder.feed_des.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent inn=new Intent(mContext, PrayDetailedActivity.class);
                    inn.putExtra("KEYBOADR_STATUS", "PrayText");
                    inn.putExtra("Feed_id", feedList.get(position).get_id());
                    inn.putExtra("Notification", "false");
                    // for refresh data in random fragment on resume
                    AllFragment.positionForRefresh= String.valueOf(position);
                    mContext.startActivity(inn);
                }
            });

        }

        // feed_img action.........................

        // comment_rel action.........................

        if (feedList.get(position).isAdmin_quote()){

            holder.comment_rel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent innn=new Intent(mContext, FeedDetailedActivity.class);
                    innn.putExtra("KEYBOADR_STATUS", "open_keyboard");
                    innn.putExtra("Feed_id", feedList.get(position).get_id());
                    innn.putExtra("Notification", "false");

                    // for refresh data in random fragment on resume
                    AllFragment.positionForRefresh= String.valueOf(position);

                    mContext.startActivity(innn);
                }
            });
            holder.comment_text_rel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent innn=new Intent(mContext, FeedDetailedActivity.class);
                    innn.putExtra("KEYBOADR_STATUS", "open_keyboard");
                    innn.putExtra("Feed_id", feedList.get(position).get_id());
                    innn.putExtra("Notification", "false");

                    // for refresh data in random fragment on resume
                    AllFragment.positionForRefresh= String.valueOf(position);

                    mContext.startActivity(innn);
                }
            });

        }else {

            holder.comment_rel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent innn=new Intent(mContext, PrayDetailedActivity.class);
                    innn.putExtra("KEYBOADR_STATUS", "open_keyboard");
                    innn.putExtra("Feed_id", feedList.get(position).get_id());
                    innn.putExtra("Notification", "false");

                    // for refresh data in random fragment on resume
                    AllFragment.positionForRefresh= String.valueOf(position);

                    mContext.startActivity(innn);
                }
            });
            holder.comment_text_rel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent innn=new Intent(mContext, PrayDetailedActivity.class);
                    innn.putExtra("KEYBOADR_STATUS", "open_keyboard");
                    innn.putExtra("Feed_id", feedList.get(position).get_id());
                    innn.putExtra("Notification", "false");

                    // for refresh data in random fragment on resume
                    AllFragment.positionForRefresh= String.valueOf(position);

                    mContext.startActivity(innn);
                }
            });

        }

        // comment_rel action.........................

        // share action

        if (feedList.get(position).isAdmin_quote()){

            holder.share_rel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog = ProgressDialog.show(mContext, "Loading", "Please wait...", true);
                    String img=Constants.URL.BASE_URL+feedList.get(position).getLarge_image();
                    new AsyncTaskLoadImage(feedList.get(position).getTitle()).execute(img);
                }
            });

            holder.share_text_rel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog = ProgressDialog.show(mContext, "Loading", "Please wait...", true);
                    String img=Constants.URL.BASE_URL+feedList.get(position).getLarge_image();
                    new AsyncTaskLoadImage(feedList.get(position).getTitle()).execute(img);
                }
            });

        }else {

            holder.share_rel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog = ProgressDialog.show(mContext, "Loading", "Please wait...", true);
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "Download Positive Vibes to pray " + feedList.get(position).getUser_fullname() + "\n" + feedList.get(position).getTitle());
                    sendIntent.setType("text/plain");
                    mContext.startActivity(sendIntent);
                    dialog.dismiss();

                }
            });

            holder.share_text_rel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    dialog = ProgressDialog.show(mContext, "Loading", "Please wait...", true);
                    Intent sendIntent = new Intent();
                    sendIntent.setAction(Intent.ACTION_SEND);
                    sendIntent.putExtra(Intent.EXTRA_TEXT, "Download Positive Vibes to pray " + feedList.get(position).getUser_fullname() + "\n" + feedList.get(position).getTitle());
                    sendIntent.setType("text/plain");
                    mContext.startActivity(sendIntent);
                    dialog.dismiss();

                }
            });

        }

        // share action

        // Sub Menu

        if (feedList.get(position).isAdmin_quote()){

            holder.sub_menu_doted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    PopupMenu menu=new PopupMenu(mContext, holder.sub_menu_doted);
                    menu.getMenuInflater().inflate(R.menu.feed_download_report_menu,menu.getMenu() );

                    menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            if(item.getTitle().equals("Save Image")){

                                dialog = ProgressDialog.show(mContext, "Loading", "Please wait...", true);
                                checkAndRequestPermissions();
                                DownLoadTask(feedList.get(position).getLarge_image());

                            }
                            if(item.getTitle().equals("Report a problem")){

                                Intent intent = new Intent(mContext, ReportsList.class);
                                intent.putExtra("feed_id", feedList.get(position).get_id());
                                mContext.startActivity(intent);

                            }

                            return false;
                        }
                    });
                    menu.show();

                }
            });

        }else {

            SharedPreferences sp = mContext.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
            USER_ID = sp.getString("USER_ID", "");

            if (feedList.get(position).getUser_id().equals(USER_ID)){
                holder.sub_menu_doted.setVisibility(View.VISIBLE);
            }else {
                holder.sub_menu_doted.setVisibility(View.GONE);
            }

            holder.sub_menu_doted.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (feedList.get(position).getUser_id().equals(USER_ID)) {

                        // for refresh data in random fragment on resume
                        AllFragment.positionForRefresh= String.valueOf(position);

                        PopupMenu menu = new PopupMenu(mContext, holder.sub_menu_doted);
                        menu.getMenuInflater().inflate(R.menu.pray_sub_menu, menu.getMenu());

                        menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                            @Override
                            public boolean onMenuItemClick(MenuItem item) {

                                if (item.getTitle().equals("Edit")) {

                                    Intent intent = new Intent(mContext, AddPreyActivity.class);
                                    intent.putExtra("ACTIVITY", "praylist");
                                    intent.putExtra("TEXT", feedList.get(position).getTitle());
                                    intent.putExtra("PRAY_ID", feedList.get(position).get_id());
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

                                            ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.DELPRAY + feedList.get(position).get_id(), mContext, postParam, headers, new ServerCallback() {
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

    public class AsyncTaskLoadImage  extends AsyncTask<String, String, Bitmap> {
        private final static String TAG = "AsyncTaskLoadImage";
        private ImageView imageView;
        String description;
        public AsyncTaskLoadImage(String description) {
            this.description = description;
        }
        @Override
        protected Bitmap doInBackground(String... params) {
            Bitmap bitmap = null;
            try {
                URL url = new URL(params[0]);
                bitmap = BitmapFactory.decodeStream((InputStream)url.getContent());
            } catch (IOException e) {
                Log.e(TAG, e.getMessage());
            }
            return bitmap;
        }
        @Override
        protected void onPostExecute(Bitmap b) {

            if(checkAndRequestPermissions()) {

                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("image/*");
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                b.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                String path = MediaStore.Images.Media.insertImage(mContext.getContentResolver(),
                        b, "Title", null);
                Uri imageUri = Uri.parse(path);
                share.putExtra(Intent.EXTRA_STREAM, imageUri);
                share.putExtra(Intent.EXTRA_SUBJECT, "hellooo the image");

                share.putExtra(Intent.EXTRA_TEXT, "Positive Vibes \n "+description+" \n \nDownload this app to get daily motivational and inspirational quotes. \nhttps://play.google.com/store/apps/details?id=com.positivevibes.app ");

                mContext.startActivity(Intent.createChooser(share, "Select"));

//                PackageManager pm = v.getContext().getPackageManager();
//                List<ResolveInfo> activityList = pm.queryIntentActivities(share, 0);

//                for (final ResolveInfo app : activityList)
//
//                if ("com.facebook.katana".equals(app.activityInfo.name)){
//                    Toast.makeText(mContext, "Please paste the string here...!!", Toast.LENGTH_SHORT).show();
//                }

                dialog.dismiss();

            }
        }
    }
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    // Download Class

    private void DownLoadTask(String image_name){

        if (checkAndRequestPermissions()) {
            String filename = image_name + ".jpg";
            String downloadUrlOfImage = Constants.URL.BASE_URL + image_name;
            File direct =
                    new File(Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                            .getAbsolutePath() + "/" + "Idillionaire" + "/");


            if (!direct.exists()) {
                direct.mkdir();
                Log.d("First time Msg.", "dir created for first time");
            }

            DownloadManager dm = (DownloadManager) mContext.getSystemService(Context.DOWNLOAD_SERVICE);
            Uri downloadUri = Uri.parse(downloadUrlOfImage);
            DownloadManager.Request request = new DownloadManager.Request(downloadUri);
            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false)
                    .setTitle(filename)
                    .setMimeType("image/jpeg")
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationInExternalPublicDir(Environment.DIRECTORY_PICTURES,
                            File.separator + "Idillionaire" + File.separator + filename);

            dm.enqueue(request);
            Toast.makeText(mContext, "Download Started...", Toast.LENGTH_SHORT).show();
        }
    }

    // Download Class

    private  boolean checkAndRequestPermissions() {
        dialog.dismiss();

//        int camera = ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.CAMERA);
        int storage = ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int loc = ContextCompat.checkSelfPermission(mContext, android.Manifest.permission.READ_EXTERNAL_STORAGE);
        List<String> listPermissionsNeeded = new ArrayList<>();

//        if (camera != PackageManager.PERMISSION_GRANTED) {
//            listPermissionsNeeded.add(android.Manifest.permission.CAMERA);
//        }
        if (storage != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (loc != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(android.Manifest.permission.READ_EXTERNAL_STORAGE);
        }
        if (!listPermissionsNeeded.isEmpty())
        {
            ActivityCompat.requestPermissions(((NavigationDrawerActivity)mContext),listPermissionsNeeded.toArray
                    (new String[listPermissionsNeeded.size()]),REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    public void addfeed(ArrayList<AllList> feed){

        int v=getItemCount();

        for(int a=0;a<feed.size(); a++){
            feedList.add(feed.get(a));
        }
//            notifyItemChanged(getItemCount());
        notifyItemInserted(getItemCount());

        int fv=getItemCount();
//        notifyDataSetChanged();

    }

    public void refresh(){

//        int add_at_podition=feedList.size();
//        feedList=feed;
//            notifyItemChanged(getItemCount());
        notifyItemChanged(getItemCount());

    }

//    public void replaceFragment(Fragment someFragment, String tag) {
//
//        transaction.replace(R.id.fragment, someFragment, tag);
//        transaction.addToBackStack(null);
//        transaction.commit();
//
//    }


    protected void hideKeyboard(View view)
    {
        InputMethodManager in = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
        in.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);

    }


    @Override
    public long getItemId(int position) {
        //return unique id for each view
        return position;
    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }

    private void like(final int position, final View v) {


//        int temp=feed.getUser_like();
//        if(temp == 1){
//            feed_like_star.setImageResource(R.drawable.favorites_large_icon);
//            notifyItemChanged(position);
////            notifyItemChanged(position);
////                                    notifyDataSetChanged();
//        }
//        if(temp == 0){
//            feed_like_star.setImageResource(R.drawable.camera_icon);
//            notifyItemChanged(position);
//
////                                    notifyDataSetChanged();
////            notifyItemChanged(position);
//        }

    }

}
