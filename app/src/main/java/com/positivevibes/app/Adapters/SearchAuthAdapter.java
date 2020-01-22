package com.positivevibes.app.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.positivevibes.app.Activity.AuthorDetail;
import com.positivevibes.app.Activity.SearchCateAuthor;
import com.positivevibes.app.Activity.SelectFromSearchActivity;
import com.positivevibes.app.ApiStructure.Constants;
import com.positivevibes.app.Models.CatagoriesApiModel.Catagories;
import com.positivevibes.app.Models.SearchApiModel.SelectAuthorSearch;
import com.positivevibes.app.R;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchAuthAdapter extends RecyclerView.Adapter<SearchAuthAdapter.MyViewHolder>{

    private Context mContext;
    private List<SelectAuthorSearch> authorSearchList;

    public class MyViewHolder extends RecyclerView.ViewHolder {

        CircleImageView cat_img;
        TextView title;
        LinearLayout parent_lin;
        ProgressBar progress_search;

        public MyViewHolder(View view) {
            super(view);
            cat_img =  view.findViewById(R.id.cat_img);
            title = view.findViewById(R.id.title);
            parent_lin =  view.findViewById(R.id.parent_lin);
            progress_search =  view.findViewById(R.id.progress_search);

        }
    }

    public SearchAuthAdapter(Context mContext, List<SelectAuthorSearch> authorSearchList) {
        this.mContext = mContext;
        this.authorSearchList = authorSearchList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.get_catagory_adapter, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        Glide
                .with( mContext )
                .load( Constants.URL.BASE_URL + authorSearchList.get(position).getImg() )
                .into( holder.cat_img );


        holder.title.setText(authorSearchList.get(position).getName());


        holder.parent_lin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SearchCateAuthor activity = (SearchCateAuthor) mContext;

                Intent intent = new Intent(mContext, AuthorDetail.class);
                intent.putExtra("AUTH_ID", authorSearchList.get(position).get_id());
                mContext.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
            }
        });

    }

    public void addfeed(ArrayList<SelectAuthorSearch> new_array){

        int add_at_podition=authorSearchList.size();
        for(int a=0;a<new_array.size(); a++){
            authorSearchList.add(new_array.get(a));
        }
        notifyItemChanged(getItemCount());

    }

    @Override
    public int getItemCount() {
        return authorSearchList.size();
    }


}
