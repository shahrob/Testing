package com.positivevibes.app.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.positivevibes.app.Activity.FeedDetailedActivity;
import com.positivevibes.app.ApiStructure.Constants;
import com.positivevibes.app.Models.FavouritesApiModel.Favourites;
import com.positivevibes.app.Models.TopQuotes.TopQuotes;
import com.positivevibes.app.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchTopQuotesAdapter extends RecyclerView.Adapter<SearchTopQuotesAdapter.MyViewHolder>{

    Context tContext;
    ArrayList<TopQuotes> topQuotesArr;

    public SearchTopQuotesAdapter(Context tContext, ArrayList<TopQuotes> topQuotesArr) {
        this.tContext = tContext;
        this.topQuotesArr = topQuotesArr;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.top_quotes_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {

        Picasso.with(tContext)
                .load(Constants.URL.BASE_URL + topQuotesArr.get(position).getSmall_image())
                .into(holder.quotes_image);

        holder.quotes_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent inn=new Intent(tContext, FeedDetailedActivity.class);
                inn.putExtra("KEYBOADR_STATUS", "feed_img");
                inn.putExtra("Feed_id", topQuotesArr.get(position).get_id());
                inn.putExtra("Notification", "false");

                tContext.startActivity(inn);
            }
        });

    }


    public void addfeed(ArrayList<TopQuotes> array){

        for(int a=0;a<array.size(); a++){
            topQuotesArr.add(array.get(a));
        }
        notifyItemChanged(getItemCount());

    }


    @Override
    public int getItemCount() {
        return topQuotesArr.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{

        ImageView quotes_image;

        public MyViewHolder(View itemView) {
            super(itemView);

            quotes_image = itemView.findViewById(R.id.quote_image);
        }
    }
}
