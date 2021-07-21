package com.example.myapplication;

import android.content.Context;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class ViewPageAdapter extends RecyclerView.Adapter<ViewPageHolder> {
    public int pageNum;
    private int position;
    List<VideoMessage> messageList;
    List<ViewPageHolder> viewPageHolders =new ArrayList<>();
    private final Context context;
    private final GestureDetector gesture;

    public ViewPageAdapter(Context context, List<VideoMessage> messageList, GestureDetector gesture) {
        pageNum=messageList.size();
        this.messageList=messageList;
        this.context=context;
        this.gesture=gesture;
    }

    @NonNull
    @Override
    public ViewPageHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.page_view,parent,false);
        return new ViewPageHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewPageHolder holder, int position) {
        try {
            holder.Bind(messageList.get(position),context,gesture);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        this.position=position;
        viewPageHolders.add(holder);
    }

    @Override
    public int getItemCount() {
        return pageNum;
    }

    @Override
    public void onViewAttachedToWindow(@NonNull ViewPageHolder holder) {
        if(position==0)
        {
            holder.pause.setVisibility(View.INVISIBLE);
            holder.f1pic.setVisibility(View.INVISIBLE);
            holder.videoView.setVisibility(View.VISIBLE);
            holder.videoView.start();
        }
        else{
            holder.f1pic.setVisibility(View.VISIBLE);
            holder.pause.setVisibility(View.INVISIBLE);
            holder.videoView.setVisibility(View.INVISIBLE);
        }

        super.onViewAttachedToWindow(holder);
    }
}
