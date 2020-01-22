package com.positivevibes.app.Fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.positivevibes.app.Adapters.NotificationAdapter;
import com.positivevibes.app.R;

import java.util.ArrayList;
import java.util.List;


public class NotificationFragment extends Fragment {


    View rootView;
    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    List<String> notificationList;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_notification, container, false);


        notificationList = new ArrayList<>();
        recyclerView = (RecyclerView) rootView.findViewById(R.id.notification_recyclerview);

        prepareFeedList();


//        adapter = new NotificationAdapter(getActivity(), notificationList);
//
//        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());
//
//        recyclerView.setLayoutManager(mLayoutManager);
//
//        recyclerView.setItemAnimator(new DefaultItemAnimator());
//
//        recyclerView.setAdapter(adapter);


        return rootView;
    }

    private void prepareFeedList() {

//        Notification notification;

//        for (int i = 0; i < 10; i++) {
//            notification = new Notification("www.google.com", "John Doe ","date");
            notificationList.add("got a new silver Badge got a new silver Badge got a new silver");
            notificationList.add("liked by Ali");
            notificationList.add("inviting you on a challenge");
            notificationList.add("got a new silver Badge got a new silver Badge got a new silver and he is going to next level hurrahhhh hurrahhhh hurrahhhh hurrahhhh hurrahhhh");
            notificationList.add("liked this pic");
            notificationList.add("got a new silver Badge got a new silver Badge got a new silver got a new silver Badge got a new silver Badge got a new silver");
            notificationList.add("inviting you on a challenge");
            notificationList.add("liked by Ali");
            notificationList.add("inviting you on a challenge");
//        }
    }
}