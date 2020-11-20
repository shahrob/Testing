package jokesbook.app.Adapters;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
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

import com.android.volley.Request;
import com.google.android.gms.ads.AdView;
import jokesbook.app.Activity.AddPreyActivity;
import jokesbook.app.Activity.PrayDetailedActivity;
import jokesbook.app.Activity.UserDetail;
import jokesbook.app.ApiStructure.ApiModelClass;
import jokesbook.app.ApiStructure.Constants;
import jokesbook.app.ApiStructure.ServerCallback;
import jokesbook.app.Fragments.PrayFragment;
import jokesbook.app.Models.FeedListModel.CategoriesList;
import jokesbook.app.Models.MyBounceInterpolator;
import jokesbook.app.Models.PrayListModel.Prays;
import jokesbook.app.R;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class PraylistAdapter extends RecyclerView.Adapter<PraylistAdapter.MyViewHolder> {

    private Context mContext;
    private List<Prays> feedList;
    FragmentTransaction transaction;
    int poss;
    ProgressDialog dialog;
    String USER_ID = "";
    String p_author_id;

    LinearLayoutManager mLayoutManager;
    private CategoryListAdapter cate_adapter;
    ArrayList<CategoriesList> category_reference;

    public static boolean COMING_FROM_FEED_ADAPTER ;
    public static String PROFILE_ID;




    public class MyViewHolder extends RecyclerView.ViewHolder {



        TextView comment_one_text,post,feed_date,profile_name,feed_des,like_count,comment_count,comment_one_like_count
                ,comment_one_time,comment_two_text,comment_two_like_count,comment_two_time, pray_text;
        RelativeLayout share_rel,like_heart_rel,comment_rel,heart_count_img_rel,comment_one_heart_rel,heart_count_rel,
                comment_count_img_rel,comment_count_rel,comment_text_rel,share_text_rel, sub_menu_doted, profile_img_rel, pray_text_rel;
        ImageView like_heart_img,comment_one_heart_img,comment_two_heart_img;
        LinearLayout add_parent_linear,comments_block,card_feed_parent_rel,count_linear_parent,count_below_line_lin;
        CircleImageView feed_profile_img,comment_one_pic,comment_two_pic;
        AdView add_one,add_two,add_three,add_four;
        RecyclerView feed_category_list;

        public MyViewHolder(View view) {
            super(view);

            comment_one_time = (TextView) view.findViewById(R.id.comment_one_time);
            pray_text_rel = (RelativeLayout) view.findViewById(R.id.pray_text_rel);
            pray_text = (TextView) view.findViewById(R.id.pray_text);
            profile_img_rel = view.findViewById(R.id.profile_img_rel);
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

        }
    }


    public PraylistAdapter(Context mContext, List<Prays> feedList, FragmentTransaction transaction) {
        this.mContext = mContext;
        this.feedList=feedList;
        this.transaction=transaction;

    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.card_pray, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {

//        load add

//        if (  position != 0) {
//
//            if(position == 1 || position % 6 == 0) {
//                int add_number=0;
//
//                if(PrayFragment.current_add.equals("one")){
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
//
//                }
//                if(PrayFragment.current_add.equals("two")){
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
//
//                }
//
//
//
//                if(PrayFragment.current_add.equals("three")){
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
//
//                }
//
//
//                if(PrayFragment.current_add.equals("four")){
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
//
//                }
//
//                if(add_number == 1){
//                    PrayFragment.current_add="two";
//
//                }
//
//
//                if(add_number == 2){
//                    PrayFragment.current_add="three";
//
//                }
//
//
//                if(add_number == 3){
//                    PrayFragment.current_add="four";
//
//                }
//
//
//                if(add_number == 4){
//                    PrayFragment.current_add="one";
//
//                }
//
//
//            }
//            else{
//                holder.add_parent_linear.setVisibility(View.GONE);
//                holder.add_one.setVisibility(View.GONE);
//                holder.add_two.setVisibility(View.GONE);
//                holder.add_three.setVisibility(View.GONE);
//                holder.add_four.setVisibility(View.GONE);
//            }
//
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


        holder.profile_name.setText(feedList.get(position).getUser_full_name());

        // User Profile

        holder.profile_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(mContext, UserDetail.class);
                    intent.putExtra("AUTH_ID", feedList.get(position).getUser_id());
                    mContext.startActivity(intent);
            }
        });

        // User Profile

        holder.profile_img_rel.setLayerType(View.LAYER_TYPE_SOFTWARE, null);

        if (!feedList.get(position).getUser_dp().isEmpty()){
        Picasso.with(mContext)
                .load(Constants.URL.BASE_URL + feedList.get(position).getUser_dp())
                .into(holder.feed_profile_img);
        }else {
            holder.feed_profile_img.setImageResource(R.drawable.profile_icon);
        }

//         User Profile

        holder.feed_profile_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(mContext, UserDetail.class);
                    intent.putExtra("AUTH_ID", feedList.get(position).getUser_id());
                    mContext.startActivity(intent);
            }
        });

//         User Profile


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


        if(feedList.get(position).isPrayed_by_me()){

            holder.like_heart_img.setImageResource(R.drawable.pray_filled);
            holder.pray_text.setText("Prayed");
            holder.pray_text.setTextColor(Color.parseColor("#109d58"));
        }
        if(!feedList.get(position).isPrayed_by_me()){

            holder.like_heart_img.setImageResource(R.drawable.pray_black);
            holder.pray_text.setTextColor(Color.parseColor("#000000"));
            holder.pray_text.setText("Pray");
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
                if (like_count.equals("1")){
                    holder.like_count.setText(like_count + " person prayed");
                }else {
                    holder.like_count.setText(like_count + " persons prayed");
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

            holder.feed_des.setVisibility(View.VISIBLE);
            holder.feed_des.setText(feedList.get(position).getText());
            holder.feed_des.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent inn=new Intent(mContext, PrayDetailedActivity.class);
                    inn.putExtra("KEYBOADR_STATUS", "PrayText");
                    inn.putExtra("Feed_id", feedList.get(position).getPray_id());
                    inn.putExtra("Notification", "false");
                    // for refresh data in random fragment on resume
                    PrayFragment.refreshposition= String.valueOf(position);
                    mContext.startActivity(inn);
                }
            });


        // like button action

        holder.like_heart_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                holder.like_heart_rel.setEnabled(false);


                if(!feedList.get(position).isPrayed_by_me()){


                    feedList.get(position).setPrayed_by_me(true);
//                    bounce animation------------------------------------------------------

                    final Animation myAnim = AnimationUtils.loadAnimation(mContext, R.anim.bounce);
                    MyBounceInterpolator interpolator = new MyBounceInterpolator(0.15, 25);
                    myAnim.setInterpolator(interpolator);
                    holder.like_heart_img.startAnimation(myAnim);
                    //                    bounce animation------------------------------------------------------


                    holder.like_heart_img.setImageResource(R.drawable.pray_filled);
                    holder.pray_text.setText("Prayed");
                    holder.pray_text.setTextColor(Color.parseColor("#109d58"));

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

        holder.pray_text_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                holder.pray_text_rel.setEnabled(false);


                if(!feedList.get(position).isPrayed_by_me()){


                    feedList.get(position).setPrayed_by_me(true);
//                    bounce animation------------------------------------------------------

                    final Animation myAnim = AnimationUtils.loadAnimation(mContext, R.anim.bounce);
                    MyBounceInterpolator interpolator = new MyBounceInterpolator(0.15, 25);
                    myAnim.setInterpolator(interpolator);
                    holder.like_heart_img.startAnimation(myAnim);
                    //                    bounce animation------------------------------------------------------


                    holder.like_heart_img.setImageResource(R.drawable.pray_filled);
                    holder.pray_text.setText("Prayed");
                    holder.pray_text.setTextColor(Color.parseColor("#109d58"));

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

        // comment_rel action.........................

        holder.comment_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent innn=new Intent(mContext, PrayDetailedActivity.class);
                innn.putExtra("KEYBOADR_STATUS", "open_keyboard");
                innn.putExtra("Feed_id", feedList.get(position).getPray_id());
                innn.putExtra("Notification", "false");

                // for refresh data in random fragment on resume
                PrayFragment.refreshposition= String.valueOf(position);

                mContext.startActivity(innn);
            }
        });
        holder.comment_text_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent innn=new Intent(mContext, PrayDetailedActivity.class);
                innn.putExtra("KEYBOADR_STATUS", "open_keyboard");
                innn.putExtra("Feed_id", feedList.get(position).getPray_id());
                innn.putExtra("Notification", "false");

                // for refresh data in random fragment on resume
                PrayFragment.refreshposition= String.valueOf(position);

                mContext.startActivity(innn);
            }
        });

        // comment_rel action.........................

        // share action

        holder.share_rel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                dialog = ProgressDialog.show(mContext, "Loading", "Please wait...", true);
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Download Positive Vibes to pray " + feedList.get(position).getUser_full_name() + "\n" + feedList.get(position).getText());
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
                sendIntent.putExtra(Intent.EXTRA_TEXT, "Download Positive Vibes to pray " + feedList.get(position).getUser_full_name() + "\n" + feedList.get(position).getText());
                sendIntent.setType("text/plain");
                mContext.startActivity(sendIntent);
                dialog.dismiss();
            }
        });


        // share action

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

                    PrayFragment.refreshposition = String.valueOf(position);
                    PopupMenu menu = new PopupMenu(mContext, holder.sub_menu_doted);
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
