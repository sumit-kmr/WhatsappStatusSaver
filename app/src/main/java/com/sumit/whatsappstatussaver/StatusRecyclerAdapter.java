package com.sumit.whatsappstatussaver;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.Toast;


import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class StatusRecyclerAdapter extends RecyclerView.Adapter<StatusRecyclerAdapter.MyViewHolder> {
    private ArrayList<Status> statuses;
    private Activity context;
    private boolean fromDownloads;

    public StatusRecyclerAdapter(Activity ctx, ArrayList<Status> statuses, boolean fromDownloadsActivity) {
        this.statuses = new ArrayList<>(statuses);
        context = ctx;
        fromDownloads = fromDownloadsActivity;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_item_recycler_view, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StatusRecyclerAdapter.MyViewHolder holder, int position) {
        holder.statusImageView.setImageBitmap(statuses.get(position).getBitmap());
        if(statuses.get(position).getType() == Status.VIDEO_TYPE) {
            holder.playButtonImageView.setVisibility(View.VISIBLE);
        } else if(statuses.get(position).getType() == Status.IMAGE_TYPE) {
            holder.playButtonImageView.setVisibility(View.GONE);
        }
        holder.statusFrameLayout.setOnClickListener(l -> {
            PreviewDialog dialog = new PreviewDialog(context, statuses.get(position), fromDownloads);
            dialog.show();
        });
    }

    public void updateList(ArrayList<Status> newList) {
        statuses.clear();
        statuses.addAll(newList);
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return statuses.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView statusImageView, playButtonImageView;
        FrameLayout statusFrameLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            statusImageView = itemView.findViewById(R.id.status_image_view);
            playButtonImageView = itemView.findViewById(R.id.play_button_image_view);
            statusFrameLayout = itemView.findViewById(R.id.status_frame_layout);
        }
    }
}
