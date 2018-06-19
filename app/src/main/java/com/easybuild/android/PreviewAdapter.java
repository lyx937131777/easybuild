package com.easybuild.android;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.bumptech.glide.Glide;
import com.easybuild.android.db.Favorite;
import com.easybuild.android.db.Type;
import com.easybuild.android.gson.items;
import com.easybuild.android.util.LogUtil;
import com.easybuild.android.util.Utility;

import org.litepal.crud.DataSupport;

import java.util.List;

public class PreviewAdapter extends RecyclerView.Adapter<PreviewAdapter.ViewHolder>
{
    private Context mContext;
    private List<items> mItemsList;

    static class ViewHolder extends RecyclerView.ViewHolder
    {
        View preview;
        ImageView previewImage;
        TextView previewTitle;
        TextView previewPrice;
        TextView previewSource;
        TextView previewComments;
        ToggleButton previewFavorite;

        public  ViewHolder(View view)
        {
            super(view);
            preview = view;
            previewImage = view.findViewById(R.id.preview_img);
            previewTitle = view.findViewById(R.id.preview_title);
            previewPrice = view.findViewById(R.id.preview_price);
            previewComments = view.findViewById(R.id.preview_comments);
            previewSource = view.findViewById(R.id.preview_source);
            previewFavorite = view.findViewById(R.id.preview_favorite);
        }
    }

    public PreviewAdapter(List<items> itemsList)
    {
        mItemsList = itemsList;
    }
    @NonNull
    @Override
    public PreviewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        if(mContext == null)
        {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.preview_item,parent,false);
        final PreviewAdapter.ViewHolder holder = new PreviewAdapter.ViewHolder(view);
        holder.preview.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int position = holder.getAdapterPosition();
                items mitems = mItemsList.get(position);
                Intent intent = new Intent(mContext,DisplayActivity.class);
                mContext.startActivity(intent);
            }
        });
        holder.previewFavorite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                int position = holder.getAdapterPosition();
                items mitems = mItemsList.get(position);
                SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
                String userID = pref.getString("userID", null);
                String favorite_id = mitems.get_id().get$oid();
                if(isChecked)
                {
                    LogUtil.e("PreviewAdapter","ON!!!      : "+position);
                    Favorite favorite = DataSupport.where("favorite_id = ? and userID = ?",favorite_id,userID).findFirst(Favorite.class);
                    if(favorite == null)
                    {
                        favorite = new Favorite();
                        favorite.setFavorite_id(favorite_id);
                        favorite.setUserID(userID);
                        favorite.setType(mitems.getType());
                        favorite.save();
                    }
                }else
                {
                    DataSupport.deleteAll(Favorite.class,"favorite_id = ? and userID = ?",favorite_id,userID);
                    LogUtil.e("PreviewAdapter","OFF!!!    "+position);
                }
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull PreviewAdapter.ViewHolder holder, int position)
    {
        items mitems = mItemsList.get(position);
        holder.previewTitle.setText("hello");
        holder.previewTitle.setText(mitems.getTitle());
        holder.previewSource.setText("京东");
        holder.previewComments.setText("评论数："+mitems.getComments());
        holder.previewPrice.setText("￥"+mitems.getPrice());
        Glide.with(mContext).load(mitems.getImg()).into(holder.previewImage);
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(mContext);
        String userID = pref.getString("userID", null);
        String favorite_id = mitems.get_id().get$oid();

        if(DataSupport.where("favorite_id = ? and userID = ?",favorite_id,userID).findFirst(Favorite.class) != null)
        {
            holder.previewFavorite.setChecked(true);
            LogUtil.e("PreviewAdapter","init!!!"+position);
        }else
        {
            holder.previewFavorite.setChecked(false);
        }
    }

    @Override
    public int getItemCount()
    {
        return mItemsList.size();
    }
}
