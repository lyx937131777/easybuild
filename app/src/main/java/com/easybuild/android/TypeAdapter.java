package com.easybuild.android;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.easybuild.android.db.Type;
import com.easybuild.android.util.LogUtil;

import java.util.List;

public class TypeAdapter extends RecyclerView.Adapter<TypeAdapter.ViewHolder>
{
    private Context mContext;
    private List<Type> mTypeList;
    private int mposition;
    static class ViewHolder extends RecyclerView.ViewHolder
    {
        CardView cardView;
        ImageView typeImage;
        TextView typenName;

        public  ViewHolder(View view)
        {
            super(view);
            cardView = (CardView) view;
            typeImage = (ImageView) view.findViewById(R.id.type_image);
            typenName = (TextView) view.findViewById(R.id.type_name);
        }
    }

    public TypeAdapter(List<Type> typeList,int positon)
    {
        mTypeList = typeList;
        mposition = positon;
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        if(mContext == null)
        {
            mContext = parent.getContext();
        }
        View view = LayoutInflater.from(mContext).inflate(R.layout.type_item,parent,false);
        final ViewHolder holder = new ViewHolder(view);
        holder.cardView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                int position = holder.getAdapterPosition();
                Type type = mTypeList.get(position);
                if(mposition == 0)
                {
                    Intent intent = new Intent(mContext,SearchActivity.class);
                    intent.putExtra("type",type.getTypeName());
                    mContext.startActivity(intent);
                }else
                {
                    MainActivity.setCommond(3);
                    CompareActivity compareActivity = (CompareActivity)mContext;
                    LogUtil.e("TypeAdapter","hahahhahahahahahha"+String.valueOf(mposition));
                    Intent intent = new Intent(compareActivity,SearchActivity.class);
                    intent.putExtra("type",type.getTypeName());
                    compareActivity.startActivityForResult(intent,mposition);
                }

            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position)
    {
        Type type = mTypeList.get(position);
        holder.typenName.setText(type.getName());
        Glide.with(mContext).load(type.getImageID()).into(holder.typeImage);
    }

    @Override
    public int getItemCount()
    {
        return mTypeList.size();
    }
}
