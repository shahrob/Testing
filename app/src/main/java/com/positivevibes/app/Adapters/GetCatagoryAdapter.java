package com.positivevibes.app.Adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.positivevibes.app.Activity.EditProfileActivity;
import com.positivevibes.app.Activity.SearchCateAuthor;
import com.positivevibes.app.Activity.SearchMainActivity;
import com.positivevibes.app.Activity.SelectCatagoryActivity;
import com.positivevibes.app.Activity.SelectFromSearchActivity;
import com.positivevibes.app.Activity.SettingActivity;
import com.positivevibes.app.ApiStructure.Constants;
import com.positivevibes.app.Fragments.RandomFeedFragment;
import com.positivevibes.app.Models.CatagoriesApiModel.Catagories;
import com.positivevibes.app.Models.FeedListModel.Feeds;
import com.positivevibes.app.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class GetCatagoryAdapter extends RecyclerView.Adapter<GetCatagoryAdapter.MyViewHolder> {

    private Context mContext;
    private List<Catagories> catagoriesList;

    private ArrayList<String> category_history_array;
    private ArrayList<String> category_id_array;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView cat_img;
        TextView title;
        LinearLayout parent_lin;

        public MyViewHolder(View view) {
            super(view);
            cat_img =  view.findViewById(R.id.cat_img);
            title = (TextView) view.findViewById(R.id.title);
            parent_lin =  view.findViewById(R.id.parent_lin);

        }
    }


    public GetCatagoryAdapter(Context mContext, List<Catagories> catagoriesList) {
        this.catagoriesList = catagoriesList;
        this.mContext = mContext;

    }

    @Override
    public GetCatagoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.get_catagory_adapter, parent, false);

        return new GetCatagoryAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final GetCatagoryAdapter.MyViewHolder holder, final int position) {

        category_history_array = RandomFeedFragment.category_history_array;
        category_id_array = RandomFeedFragment.category_id_history;

        Glide
                .with( mContext )
                .load( Constants.URL.BASE_URL + catagoriesList.get(position).getCat_img() )
                .error(R.drawable.profile_icon)
                .into( holder.cat_img );


        holder.title.setText(catagoriesList.get(position).getTitle());


        holder.parent_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SearchCateAuthor activity = (SearchCateAuthor) mContext;

                category_history_array.add(catagoriesList.get(position).getTitle());
                category_id_array.add(catagoriesList.get(position).get_id());

                Intent intt = new Intent(mContext, SelectFromSearchActivity.class);
                intt.putExtra("CAT_ID",catagoriesList.get(position).get_id());
                intt.putExtra("CAT_NAME",catagoriesList.get(position).getTitle());
                mContext.startActivity(intt);

                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

            }
        });

    }

    public void addfeed(ArrayList<Catagories> new_array){

        int add_at_podition=catagoriesList.size();
        for(int a=0;a<new_array.size(); a++){
            catagoriesList.add(new_array.get(a));
        }
        notifyItemChanged(getItemCount());

    }


    @Override
    public int getItemCount() {
        return catagoriesList.size();
    }
}