package jokesbook.app.Adapters;

import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.util.Log;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.google.android.gms.ads.AdView;
import jokesbook.app.Activity.AuthorDetail;
import jokesbook.app.Activity.FeedDetailedActivity;
import jokesbook.app.Activity.FirstActivity;
import jokesbook.app.Activity.NavigationDrawerActivity;
import jokesbook.app.Activity.ReportsList;
import jokesbook.app.ApiStructure.ApiModelClass;
import jokesbook.app.ApiStructure.Constants;
import jokesbook.app.ApiStructure.ServerCallback;
import jokesbook.app.Models.FeedListModel.CategoriesList;
import jokesbook.app.Models.FeedListModel.Feeds;
import jokesbook.app.Models.MyBounceInterpolator;
import jokesbook.app.Models.MySpannable;
import jokesbook.app.R;
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
import jp.wasabeef.picasso.transformations.CropCircleTransformation;

public class AuthorDetailAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    
    Context mContext;
    List<Feeds> feedList;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    LinearLayoutManager mLayoutManager;
    private CategoryListAdapter cate_adapter;
    ArrayList<CategoriesList> category_reference;
    ProgressDialog dialog;

    int like_img_status=0;



    public AuthorDetailAdapter(Context mContext, List<Feeds> list) {
        this.mContext = mContext;
        feedList = list;
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
                    .inflate(R.layout.row_author_detail, parent, false);
            return new MyViewHolder(itemView);
        } else if (viewType == TYPE_HEADER) {
            View itemView = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.head_author_detail, parent, false);
            //inflate your layout and pass it to view holder
            return new VHHeader(itemView);
        }
        throw new RuntimeException("there is no type that matches the type " + viewType + " + make sure your using types correctly");
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position  ) {


        if (holder instanceof VHHeader){

            Picasso.with(mContext)
                    .load(Constants.URL.BASE_URL + AuthorDetail.p_author_detail_image)
                    .transform(new CropCircleTransformation())
                    .into(((VHHeader) holder).author_detail_image);
            ((VHHeader) holder).count_of_quotes.setText(AuthorDetail.p_author_quotes_count);
            ((VHHeader) holder).author_name_main.setText(AuthorDetail.p_author_detail_name);
            ((VHHeader) holder).author_achievements.setText(AuthorDetail.p_author_achievements);
                makeTextViewResizable(((VHHeader) holder).author_achievements, 2, "... See More", true);
//            ((VHHeader) holder).author_detail_name.setText(AuthorDetail.p_author_detail_name);
            ((VHHeader) holder).author_country.setText(AuthorDetail.p_author_country);
            ((VHHeader) holder).author_dateofborn.setText(AuthorDetail.p_author_born);
            if (AuthorDetail.p_author_age.isEmpty()){
                ((VHHeader) holder).age_layout.setVisibility(View.GONE);
            }else {
                ((VHHeader) holder).count_of_age.setText(AuthorDetail.p_author_age);
            }
            if (AuthorDetail.p_author_dismissed.isEmpty()){
                ((VHHeader) holder).date_of_death_img.setVisibility(View.GONE);
            }else {
                ((VHHeader) holder).author_dateofdeath.setText(AuthorDetail.p_author_dismissed);
            }
            ((VHHeader) holder).author_bio.setText(AuthorDetail.p_author_bio);
                makeTextViewResizable(((VHHeader) holder).author_bio, 2, "... See More", true);


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
//                    if (AuthorDetail.current_add.equals("two")) {
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
//                    if (AuthorDetail.current_add.equals("three")) {
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
//                    if (AuthorDetail.current_add.equals("four")) {
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
//                        AuthorDetail.current_add = "two";
//
//                    }
//
//
//                    if (add_number == 2) {
//                        AuthorDetail.current_add = "three";
//
//                    }
//
//
//                    if (add_number == 3) {
//                        AuthorDetail.current_add = "four";
//
//                    }
//
//
//                    if (add_number == 4) {
//                        AuthorDetail.current_add = "one";
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
            if (feedList.get(position).getFeed_blog_url().isEmpty())
            {
                ((MyViewHolder) holder).feed_author_blog_url_llayout.setVisibility(View.GONE);
            }
            else
            {
                ((MyViewHolder) holder).feed_author_blog_url_llayout.setVisibility(View.VISIBLE);
                ((MyViewHolder) holder).feed_author_blog_url.setText(feedList.get(position).getFeed_blog_url());
            }
            ((MyViewHolder) holder).feed_author_blog_url_llayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(feedList.get(position).getFeed_blog_url()));
                    mContext.startActivity(browserIntent);
                }
            });

            ((MyViewHolder) holder).author_profile_name.setText(feedList.get(position).getAuther_name());

            Picasso.with(mContext)
                    .load(Constants.URL.BASE_URL + feedList.get(position).getAuther_img())
                    .into(((MyViewHolder) holder).author_profile_img);

            // Category List

            category_reference = feedList.get(position).getCategoriesArrayList();
            mLayoutManager = new LinearLayoutManager(mContext);
            mLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            ((MyViewHolder) holder).author_category_list.getItemAnimator().endAnimations();

            cate_adapter = new CategoryListAdapter(mContext, category_reference);

            ((MyViewHolder) holder).author_category_list.setAdapter(cate_adapter);
            ((MyViewHolder) holder).author_category_list.setLayoutManager(mLayoutManager);

            // Category List

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

            if (feedList.get(position).isLiked_by_me()) {

                ((MyViewHolder) holder).author_like_heart_img.setImageResource(R.drawable.fill_feed_like);
            } else {


                ((MyViewHolder) holder).author_like_heart_img.setImageResource(R.drawable.feed_like);
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
                    ((MyViewHolder) holder).author_like_count.setText(like_count);

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

                if (feedList.get(position).getTitle().isEmpty()) {
                    ((MyViewHolder) holder).author_des.setVisibility(View.GONE);
                } else {
                    ((MyViewHolder) holder).author_des.setVisibility(View.VISIBLE);
                    ((MyViewHolder) holder).author_des.setText(feedList.get(position).getTitle());
                }

                if (feedList.get(position).getLarge_image().isEmpty()){
                    ((MyViewHolder) holder).third_row_conatiner.setVisibility(View.GONE);
                }else {
                    try {
                        Picasso.with(mContext)
                                .load(Constants.URL.BASE_URL + feedList.get(position).getLarge_image())

                                .into(((MyViewHolder) holder).author_img);
                    } catch (Exception e) {

                    }
                }


            // like button action


            ((MyViewHolder) holder).author_like_heart_rel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    ((MyViewHolder) holder).author_like_heart_rel.setEnabled(false);


                    if (!feedList.get(position).isLiked_by_me()) {


                        feedList.get(position).setLiked_by_me(true);
//                    bounce animation------------------------------------------------------

                        final Animation myAnim = AnimationUtils.loadAnimation(mContext, R.anim.bounce);
                        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.15, 25);
                        myAnim.setInterpolator(interpolator);
                        ((MyViewHolder) holder).author_like_heart_img.startAnimation(myAnim);
                        //                    bounce animation------------------------------------------------------

                        like_img_status = 1;

                        ((MyViewHolder) holder).author_like_heart_img.setImageResource(R.drawable.fill_feed_like);

                        feedList.get(position).setLiked_count(Integer.parseInt(String.valueOf(Integer.parseInt(String.valueOf(feedList.get(position).getLiked_count())) + 1)));
                        ((MyViewHolder) holder).author_like_count.setText(String.valueOf(feedList.get(position).getLiked_count()));
                        int like_count = feedList.get(position).getLiked_count();
                        int comment_count = feedList.get(position).getComments_count();

                        if (like_count == 0 && comment_count == 0) {
                            ((MyViewHolder) holder).author_count_linear_parent.setVisibility(View.GONE);
                            ((MyViewHolder) holder).author_count_below_line_lin.setVisibility(View.GONE);
                        } else {
                            ((MyViewHolder) holder).author_count_linear_parent.setVisibility(View.VISIBLE);
                            ((MyViewHolder) holder).author_count_below_line_lin.setVisibility(View.VISIBLE);

                            if (like_count == 0) {
                                ((MyViewHolder) holder).author_heart_count_img_rel.setVisibility(View.GONE);
                                ((MyViewHolder) holder).author_heart_count_rel.setVisibility(View.GONE);
                            } else {
                                ((MyViewHolder) holder).author_heart_count_img_rel.setVisibility(View.VISIBLE);
                                ((MyViewHolder) holder).author_heart_count_rel.setVisibility(View.VISIBLE);


                            }

                        }

//                    hit api ---------------------------------------------------


                        SharedPreferences mPrefs = mContext.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                        String token = mPrefs.getString("USER_TOKEN", "");


                        Map<String, String> postParam = new HashMap<String, String>();


                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("x-sh-auth", token);

                        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.ADD_FAVOURITS + feedList.get(position).get_id(), mContext, postParam, headers, new ServerCallback() {
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
                                    ((MyViewHolder) holder).author_like_heart_rel.setEnabled(true);


                                } else {

                                    ((MyViewHolder) holder).author_like_heart_img.setImageResource(R.drawable.feed_like);
                                    feedList.get(position).setLiked_count(Integer.parseInt(String.valueOf(Integer.parseInt(String.valueOf(feedList.get(position).getLiked_count())) - 1)));
                                    ((MyViewHolder) holder).author_like_count.setText(String.valueOf(feedList.get(position).getLiked_count()));

                                    int like_count = feedList.get(position).getLiked_count();

                                    int comment_count = feedList.get(position).getComments_count();

                                    if (like_count == 0 && comment_count == 0) {
                                        ((MyViewHolder) holder).author_count_linear_parent.setVisibility(View.GONE);
                                        ((MyViewHolder) holder).author_count_below_line_lin.setVisibility(View.GONE);
                                    } else {
                                        ((MyViewHolder) holder).author_count_linear_parent.setVisibility(View.VISIBLE);
                                        ((MyViewHolder) holder).author_count_below_line_lin.setVisibility(View.VISIBLE);

                                        if (like_count == 0) {
                                            ((MyViewHolder) holder).author_heart_count_img_rel.setVisibility(View.GONE);
                                            ((MyViewHolder) holder).author_heart_count_rel.setVisibility(View.GONE);
                                        } else {
                                            ((MyViewHolder) holder).author_heart_count_img_rel.setVisibility(View.VISIBLE);
                                            ((MyViewHolder) holder).author_heart_count_rel.setVisibility(View.VISIBLE);


                                        }

                                    }

                                    ((MyViewHolder) holder).author_like_heart_rel.setEnabled(true);

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
                                        ((NavigationDrawerActivity) mContext).finish();

                                    } else {

                                        Toast.makeText(mContext, ERROR, Toast.LENGTH_SHORT).show();

                                    }

                                }

                            }
                        });

//                    hit api ---------------------------------------------------

                    } else {

                        feedList.get(position).setLiked_by_me(false);


                        like_img_status = 0;

                        //                    bounce animation------------------------------------------------------
                        final Animation myAnim = AnimationUtils.loadAnimation(mContext, R.anim.bounce);
                        MyBounceInterpolator interpolator = new MyBounceInterpolator(0.15, 25);
                        myAnim.setInterpolator(interpolator);
                        ((MyViewHolder) holder).author_like_heart_img.startAnimation(myAnim);

                        //                    bounce animation------------------------------------------------------

                        ((MyViewHolder) holder).author_like_heart_img.setImageResource(R.drawable.feed_like);
                        feedList.get(position).setLiked_count(Integer.parseInt(String.valueOf(Integer.parseInt(String.valueOf(feedList.get(position).getLiked_count())) - 1)));
                        ((MyViewHolder) holder).author_like_count.setText(String.valueOf(feedList.get(position).getLiked_count()));

                        int like_count = feedList.get(position).getLiked_count();
                        int comment_count = feedList.get(position).getComments_count();

                        if (like_count == 0 && comment_count == 0) {
                            ((MyViewHolder) holder).author_count_linear_parent.setVisibility(View.GONE);
                            ((MyViewHolder) holder).author_count_below_line_lin.setVisibility(View.GONE);
                        } else {
                            ((MyViewHolder) holder).author_count_linear_parent.setVisibility(View.VISIBLE);
                            ((MyViewHolder) holder).author_count_below_line_lin.setVisibility(View.VISIBLE);

                            if (like_count == 0) {
                                ((MyViewHolder) holder).author_heart_count_img_rel.setVisibility(View.GONE);
                                ((MyViewHolder) holder).author_heart_count_rel.setVisibility(View.GONE);
                            } else {
                                ((MyViewHolder) holder).author_heart_count_img_rel.setVisibility(View.VISIBLE);
                                ((MyViewHolder) holder).author_heart_count_rel.setVisibility(View.VISIBLE);


                            }
                        }


//                    hit api ---------------------------------------------------


                        SharedPreferences mPrefs = mContext.getSharedPreferences("USER_DATA", Context.MODE_PRIVATE);
                        String token = mPrefs.getString("USER_TOKEN", "");


                        Map<String, String> postParam = new HashMap<String, String>();


                        HashMap<String, String> headers = new HashMap<String, String>();
                        headers.put("x-sh-auth", token);

                        ApiModelClass.GetApiResponse(Request.Method.GET, Constants.URL.REMOVE_FAVOURITS + feedList.get(position).get_id(), mContext, postParam, headers, new ServerCallback() {
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
                                    ((MyViewHolder) holder).author_like_heart_rel.setEnabled(true);

                                } else {
                                    ((MyViewHolder) holder).author_like_heart_img.setImageResource(R.drawable.fill_feed_like);

                                    feedList.get(position).setLiked_count(Integer.parseInt(String.valueOf(Integer.parseInt(String.valueOf(feedList.get(position).getLiked_count())) + 1)));
                                    ((MyViewHolder) holder).author_like_count.setText(String.valueOf(feedList.get(position).getLiked_count()));
                                    ((MyViewHolder) holder).author_like_heart_rel.setEnabled(true);
                                    int like_count = feedList.get(position).getLiked_count();

                                    int comment_count = feedList.get(position).getComments_count();

                                    if (like_count == 0 && comment_count == 0) {
                                        ((MyViewHolder) holder).author_count_linear_parent.setVisibility(View.GONE);
                                        ((MyViewHolder) holder).author_count_below_line_lin.setVisibility(View.GONE);
                                    } else {
                                        ((MyViewHolder) holder).author_count_linear_parent.setVisibility(View.VISIBLE);
                                        ((MyViewHolder) holder).author_count_below_line_lin.setVisibility(View.VISIBLE);

                                        if (like_count == 0) {
                                            ((MyViewHolder) holder).author_heart_count_img_rel.setVisibility(View.GONE);
                                            ((MyViewHolder) holder).author_heart_count_rel.setVisibility(View.GONE);
                                        } else {
                                            ((MyViewHolder) holder).author_heart_count_img_rel.setVisibility(View.VISIBLE);
                                            ((MyViewHolder) holder).author_heart_count_rel.setVisibility(View.VISIBLE);


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
                                        ((NavigationDrawerActivity) mContext).finish();

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

            // like button action

// feed_img action.........................


            ((MyViewHolder) holder).author_des.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent inn = new Intent(mContext, FeedDetailedActivity.class);
                    inn.putExtra("KEYBOADR_STATUS", "feed_img");
                    inn.putExtra("COMING", "detail");
                    inn.putExtra("Feed_id", feedList.get(position).get_id());
                    inn.putExtra("Notification", "false");
                    // for refresh data in random fragment on resume
                    AuthorDetail.positionForRefresh = String.valueOf(position);
                    mContext.startActivity(inn);
                }
            });

            ((MyViewHolder) holder).author_img.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent inn = new Intent(mContext, FeedDetailedActivity.class);
                    inn.putExtra("KEYBOADR_STATUS", "feed_img");
                    inn.putExtra("COMING", "detail");
                    inn.putExtra("Feed_id", feedList.get(position).get_id());
                    inn.putExtra("Notification", "false");
                    // for refresh data in random fragment on resume
                    AuthorDetail.positionForRefresh = String.valueOf(position);
                    mContext.startActivity(inn);
                }
            });

            // feed_img action.........................

            // comment_rel action.........................

            ((MyViewHolder) holder).author_comment_rel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent innn = new Intent(mContext, FeedDetailedActivity.class);
                    innn.putExtra("KEYBOADR_STATUS", "open_keyboard");
                    innn.putExtra("COMING", "detail");
                    innn.putExtra("Feed_id", feedList.get(position).get_id());
                    innn.putExtra("Notification", "false");

                    // for refresh data in random fragment on resume
                    AuthorDetail.positionForRefresh = String.valueOf(position);

                    mContext.startActivity(innn);
                }
            });
            ((MyViewHolder) holder).author_comment_text_rel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent innn = new Intent(mContext, FeedDetailedActivity.class);
                    innn.putExtra("KEYBOADR_STATUS", "open_keyboard");
                    innn.putExtra("Feed_id", feedList.get(position).get_id());
                    innn.putExtra("Notification", "false");

                    // for refresh data in random fragment on resume
                    AuthorDetail.positionForRefresh = String.valueOf(position);

                    mContext.startActivity(innn);
                }
            });

            // comment_rel action.........................

            // share action

            ((MyViewHolder) holder).author_share_rel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (feedList.get(position).getLarge_image().isEmpty())
                    {
                        dialog = ProgressDialog.show(mContext, "Loading", "Please wait...", true);
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, "Download Jokes book for fun " +"\n" + feedList.get(position).getTitle()+ "\n"+"https://play.google.com/store/apps/details?id=jokesbook.app ");
                        sendIntent.setType("text/plain");
                        mContext.startActivity(Intent.createChooser(sendIntent, "Select"));
                        dialog.dismiss();
                    }

                    else {
                        dialog = ProgressDialog.show(mContext, "Loading", "Please wait...", true);
                        String img = Constants.URL.BASE_URL + feedList.get(position).getLarge_image();
                        new AsyncTaskLoadImage(feedList.get(position).getTitle()).execute(img);
                    }
                }
            });

            ((MyViewHolder) holder).author_share_text_rel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (feedList.get(position).getLarge_image().isEmpty())
                    {
                        dialog = ProgressDialog.show(mContext, "Loading", "Please wait...", true);
                        Intent sendIntent = new Intent();
                        sendIntent.setAction(Intent.ACTION_SEND);
                        sendIntent.putExtra(Intent.EXTRA_TEXT, "Download Jokes book for fun " +"\n" + feedList.get(position).getTitle()+ "\n"+"https://play.google.com/store/apps/details?id=jokesbook.app ");
                        sendIntent.setType("text/plain");
                        mContext.startActivity(Intent.createChooser(sendIntent, "Select"));
                        dialog.dismiss();
                    }

                    else {
                        dialog = ProgressDialog.show(mContext, "Loading", "Please wait...", true);
                        String img = Constants.URL.BASE_URL + feedList.get(position).getLarge_image();
                        new AsyncTaskLoadImage(feedList.get(position).getTitle()).execute(img);
                    }
                }
            });
            // share action

            ((MyViewHolder) holder).author_feed_dots.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PopupMenu menu=new PopupMenu(mContext, ((MyViewHolder) holder).author_feed_dots);
                    menu.getMenuInflater().inflate(R.menu.feed_download_report_menu,menu.getMenu() );

                    menu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                        @Override
                        public boolean onMenuItemClick(MenuItem item) {

                            if(item.getTitle().equals("Save Image")){

                                dialog = ProgressDialog.show(mContext, "Loading", "Please wait...", true);
                                checkAndRequestPermissions();
                                if(feedList.get(position).getLarge_image().isEmpty())
                                {
                                    Toast.makeText(mContext, "No Image Attached", Toast.LENGTH_SHORT).show();
                                }
                                else
                                {
                                    DownLoadTask(feedList.get(position).getLarge_image(), feedList.get(position).get_id());
                                }


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


        }
    }

    @Override
    public int getItemCount() {
        return feedList.size();
    }

    class VHHeader extends RecyclerView.ViewHolder{

        ImageView author_detail_image;
        TextView count_of_quotes, author_country, author_dateofborn, author_dateofdeath, author_bio, count_of_age, author_name_main, author_achievements;
        LinearLayout date_of_death_img, age_layout;

        public VHHeader(View itemView) {
            super(itemView);

            author_detail_image = itemView.findViewById(R.id.author_detail_image);
            count_of_quotes = itemView.findViewById(R.id.count_of_quotes);
            author_country = itemView.findViewById(R.id.author_country);
            author_dateofborn = itemView.findViewById(R.id.author_dateofborn);
            author_dateofdeath = itemView.findViewById(R.id.author_dateofdeath);
            author_bio = itemView.findViewById(R.id.author_bio);
            date_of_death_img = itemView.findViewById(R.id.date_of_death_img);
            count_of_age = itemView.findViewById(R.id.count_of_age);
            age_layout = itemView.findViewById(R.id.age_layout);
            author_name_main = itemView.findViewById(R.id.author_name_main);
            author_achievements = itemView.findViewById(R.id.author_achievements);
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        CircleImageView author_profile_img;
        TextView author_profile_name, author_date, author_des, author_like_count, author_comment_count,feed_author_blog_url;
        ImageView author_img, author_like_heart_img;
        LinearLayout author_count_linear_parent, author_count_below_line_lin, author_add_parent_linear,feed_author_blog_url_llayout;
        RelativeLayout author_heart_count_img_rel, author_heart_count_rel, author_comment_count_img_rel, author_comment_count_rel, author_like_heart_rel,
                author_comment_rel, third_row_conatiner, author_comment_text_rel, author_share_rel, author_share_text_rel, author_feed_dots, profile_img_rel;
        RecyclerView author_category_list;
        AdView author_add_one, author_add_two, author_add_three, author_add_four;

        public MyViewHolder(View itemView) {
            super(itemView);

            author_profile_img = itemView.findViewById(R.id.author_profile_img);
            profile_img_rel = itemView.findViewById(R.id.profile_img_rel);
            author_profile_name = itemView.findViewById(R.id.author_profile_name);
            author_date = itemView.findViewById(R.id.author_date);
            author_des = itemView.findViewById(R.id.author_des);
            author_like_count = itemView.findViewById(R.id.author_like_count);
            author_comment_count = itemView.findViewById(R.id.author_comment_count);
            author_img = itemView.findViewById(R.id.author_img);
            third_row_conatiner = itemView.findViewById(R.id.third_row_conatiner);
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
            feed_author_blog_url=itemView.findViewById(R.id.feed_authot_blog_url);
            feed_author_blog_url_llayout=itemView.findViewById(R.id.feed_authot_blog_url_llayout);
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

                share.putExtra(Intent.EXTRA_TEXT, "Jokes Book \n "+description+" \n \nDownload Jokes book for fun. \nhttps://play.google.com/store/apps/details?id=jokesbook.app ");

                mContext.startActivity(Intent.createChooser(share, "Select"));


                dialog.dismiss();

            }
        }
    }
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    // Download Class

    private void DownLoadTask(String image_name, String image_id){

        if (checkAndRequestPermissions()) {
            String filename = image_id + ".jpg";
            String downloadUrlOfImage = Constants.URL.BASE_URL + image_name;
            File direct =
                    new File(Environment
                            .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
                            .getAbsolutePath() + "/" + "Jokes Book" + "/");


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
                            File.separator + "Jokes Book" + File.separator + filename);

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

    public void addfeed(ArrayList<Feeds> feed){

        int v=getItemCount();

        for(int a=0;a<feed.size(); a++){
            feedList.add(feed.get(a));
        }
//            notifyItemChanged(getItemCount());
        notifyItemInserted(getItemCount());

        int fv=getItemCount();
//        notifyDataSetChanged();

    }

    public static void makeTextViewResizable(final TextView tv, final int maxLine,
                                             final String expandText, final boolean viewMore) {

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
                                                                            final int maxLine, final String spanableText,
                                                                            final boolean viewMore) {
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
